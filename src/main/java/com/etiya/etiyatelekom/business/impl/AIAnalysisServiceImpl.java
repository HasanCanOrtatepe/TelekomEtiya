package com.etiya.etiyatelekom.business.impl;

import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisListResponse;
import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisResponse;
import com.etiya.etiyatelekom.api.dto.response.departmentResponse.DepartmentResponse;
import com.etiya.etiyatelekom.api.dto.response.serviceDomainResponse.ServiceDomainResponse;
import com.etiya.etiyatelekom.common.enums.TicketPriorityEnums;
import com.etiya.etiyatelekom.common.enums.TicketRiskLevelEnums;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.AIAnalysis;
import com.etiya.etiyatelekom.entity.Complaint;
import com.etiya.etiyatelekom.entity.CustomerSubscription;
import com.etiya.etiyatelekom.repository.AIAnalysisRepository;
import com.etiya.etiyatelekom.repository.CustomerSubscriptionRepository;
import com.etiya.etiyatelekom.business.abst.AIAnalysisService;
import com.etiya.etiyatelekom.business.abst.DepartmentService;
import com.etiya.etiyatelekom.business.abst.ServiceDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class AIAnalysisServiceImpl implements AIAnalysisService {

    private static final String DEFAULT_SUMMARY = "Analiz bekleniyor";
    private static final long DEFAULT_DEPARTMENT_ID = 1L;
    private static final long DEFAULT_SERVICE_DOMAIN_ID = 1L;
    private static final float DEFAULT_CONFIDENCE = 0.5f;

    private final AIAnalysisRepository aiAnalysisRepository;
    private final ModelMapperService modelMapperService;
    private final ServiceDomainService serviceDomainService;
    private final DepartmentService departmentService;
    private final ChatModel chatModel;
    private final CustomerSubscriptionRepository customerSubscriptionRepository;

    @Override
    public AIAnalysisResponse getById(Long id) {
        AIAnalysis aiAnalysis = aiAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AIAnalysis", "Id", id));
        return toResponse(aiAnalysis);
    }

    @Override
    public AIAnalysisResponse getByComplaintId(Long complaintId) {
        if (!aiAnalysisRepository.existsByComplaintId(complaintId)) {
            throw new ResourceNotFoundException("AIAnalysis", "Complaint", complaintId);
        }
        AIAnalysis aiAnalysis = aiAnalysisRepository.findByComplaintId(complaintId);
        return toResponse(aiAnalysis);
    }

    private AIAnalysisResponse toResponse(AIAnalysis aiAnalysis) {
        AIAnalysisResponse response = modelMapperService.forResponse().map(aiAnalysis, AIAnalysisResponse.class);
        if (aiAnalysis.getComplaint() != null && aiAnalysis.getComplaint().getTicket() != null) {
            var ticket = aiAnalysis.getComplaint().getTicket();
            if (ticket.getDepartment() != null) {
                response.setDepartmentId(ticket.getDepartment().getId());
            }
            if (ticket.getServiceDomain() != null) {
                response.setServiceDomainId(ticket.getServiceDomain().getId());
            }
        }
        return response;
    }

    @Override
    public AIAnalysisListResponse getAll() {
        List<AIAnalysis> aiAnalyses = aiAnalysisRepository.findAll();
        List<AIAnalysisResponse> aiAnalysisResponses = aiAnalyses.stream()
                .map(this::toResponse)
                .toList();
        return AIAnalysisListResponse.builder()
                .items(aiAnalysisResponses)
                .count(aiAnalysisResponses.size())
                .build();
    }

    @Override
    public AIAnalysisListResponse getActive() {
        List<AIAnalysis> aiAnalyses = aiAnalysisRepository.findByIsActiveTrue();
        List<AIAnalysisResponse> aiAnalysisResponses = aiAnalyses.stream()
                .map(this::toResponse)
                .toList();
        return AIAnalysisListResponse.builder()
                .items(aiAnalysisResponses)
                .count(aiAnalysisResponses.size())
                .build();
    }

    @Override
    public AIAnalysisResponse create(Complaint complaint) {

        if (departmentService.getActive().getCount() == 0) {
            throw new ResourceNotFoundException(
                    "Department", "System", "System not ready wait please");
        }

        if (serviceDomainService.getActive().getCount() == 0) {
            throw new ResourceNotFoundException(
                    "ServiceDomain", "System", "System not ready wait please");
        }

        String json = analyzeWithAI(complaint);

        boolean isRelevantComplaint = extractBoolean(json, "isRelevantComplaint", true);
        Long predictedDepartmentId = extractLong(json, "departmentId", DEFAULT_DEPARTMENT_ID);
        Long predictedServiceDomainId = extractLong(json, "serviceDomainId", DEFAULT_SERVICE_DOMAIN_ID);
        if (!isRelevantComplaint) {
            predictedDepartmentId = DEFAULT_DEPARTMENT_ID;
            predictedServiceDomainId = DEFAULT_SERVICE_DOMAIN_ID;
        }
        TicketPriorityEnums priority = parsePriority(extractString(json, "priority"));
        TicketRiskLevelEnums riskLevel = parseRiskLevel(extractString(json, "riskLevel"));
        Float confidence = extractFloat(json, "confidenceScore", DEFAULT_CONFIDENCE);
        String summary = extractString(json, "summary");
        if (summary == null || summary.isBlank()) summary = DEFAULT_SUMMARY;

        AIAnalysis aiAnalysis = AIAnalysis.builder()
                .complaint(complaint)
                .createdAt(OffsetDateTime.now())
                .confidenceScore(confidence)
                .riskLevel(riskLevel)
                .priority(priority)
                .summary(summary)
                .isActive(true)
                .build();
        aiAnalysisRepository.save(aiAnalysis);

        return AIAnalysisResponse.builder()
                .summary(aiAnalysis.getSummary())
                .serviceDomainId(predictedServiceDomainId)
                .riskLevel(aiAnalysis.getRiskLevel())
                .priority(aiAnalysis.getPriority())
                .id(aiAnalysis.getId())
                .departmentId(predictedDepartmentId)
                .createdAt(aiAnalysis.getCreatedAt())
                .confidenceScore(aiAnalysis.getConfidenceScore())
                .complaintId(aiAnalysis.getComplaint().getId())
                .isRelevantComplaint(isRelevantComplaint)
                .build();
    }

    private static final String SYSTEM_PROMPT = """
            Sen bir telekomünikasyon müşteri hizmetleri şikayet analiz uzmanısın. Her şikayeti titizlikle analiz edip JSON formatında yanıt verirsin.

            KURALLAR:
            1. Yanıtın SADECE geçerli JSON olsun. Başka metin ekleme.
            2. isRelevantComplaint: KRİTİK - Metin geçerli bir telekomünikasyon şikayeti mi? Sadece internet, telefon, fatura, hizmet kesintisi, paket, şebeke vb. konularında gerçek sorun/şikayet varsa true. Soğan doğrama, maç anlat, "bıktım", selam, test, anlamsız veya konu dışı metinlerse false.
            3. isRelevantComplaint false ise departmentId=1, serviceDomainId=1 ver.
            4. departmentId ve serviceDomainId mutlaka verilen listeden sayısal ID olarak seçilmeli.
            5. Emin değilsen (ama geçerli şikayetse) departmentId=1, serviceDomainId=1 ver.
            6. priority: LOW (basit sorun), MEDIUM (standart), HIGH (acil), CRITICAL (toplu kesinti).
            7. riskLevel: LOW, MEDIUM, HIGH.
            8. confidenceScore: 0.0–1.0 arası.
            9. summary: 1–2 cümle profesyonel özet. Geçersiz şikayetse kısa açıklama yeter.
            """;

    private String analyzeWithAI(Complaint complaint) {
        String userMessage = buildPromptMessage(complaint);

        Prompt prompt = new Prompt(List.of(
                new SystemMessage(SYSTEM_PROMPT),
                new UserMessage(userMessage)
        ));

        String responseText = chatModel.call(prompt).getResult().getOutput().getText();
        return extractJson(responseText);
    }

    private String buildPromptMessage(Complaint complaint) {
        List<DepartmentResponse> departments = departmentService.getActive()
                .getItems()
                .stream()
                .filter(d -> !d.getId().equals(1L))
                .toList();
        List<ServiceDomainResponse> serviceDomains = serviceDomainService.getActive()
                .getItems()
                .stream()
                .filter(s -> !s.getId().equals(1L))
                .toList();

        String departmentList = departments.stream()
                .map(d -> "ID:%d - %s".formatted(d.getId(), d.getName()))
                .reduce((a, b) -> a + "; " + b)
                .orElse("(yok)");
        String serviceList = serviceDomains.stream()
                .map(s -> "ID:%d - %s".formatted(s.getId(), s.getName()))
                .reduce((a, b) -> a + "; " + b)
                .orElse("(yok)");

        List<CustomerSubscription> customerSubscriptions = customerSubscriptionRepository.findByCustomerId(complaint.getCustomer().getId());
        String uyelikler = customerSubscriptions.isEmpty()
                ? "Bilinmiyor"
                : customerSubscriptions.stream()
                        .map(cs -> cs.getSubscription().getPackageName())
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("");

        return String.format("""
                ŞİKAYET:
                Başlık: %s
                İçerik: %s

                MÜŞTERİ ÜYELİKLERİ: %s

                DEPARTMANLAR (birini seç, ID kullan): %s

                SERVİS ALANLARI (birini seç, ID kullan): %s

                Yukarıdaki metni analiz et. Önce geçerli bir telekom şikayeti mi karar ver. SADECE JSON döndür:
                {"isRelevantComplaint":true|false,"summary":"...","priority":"LOW|MEDIUM|HIGH|CRITICAL","riskLevel":"LOW|MEDIUM|HIGH","confidenceScore":0.0-1.0,"departmentId":sayı,"serviceDomainId":sayı}
                """,
                complaint.getTitle(),
                complaint.getDescription(),
                uyelikler,
                departmentList,
                serviceList
        );
    }

    private String extractJson(String text) {
        if (text == null || text.isBlank()) {
            return "{}";
        }
        Pattern pattern = Pattern.compile("```(?:json)?\\s*([\\s\\S]*?)```");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        return text.trim();
    }

    private String extractString(String json, String key) {
        if (json == null) return null;
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher m = p.matcher(json);
        return m.find() ? m.group(1).trim() : null;
    }

    private Long extractLong(String json, String key, long defaultVal) {
        if (json == null) return defaultVal;
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*(\\d+)");
        Matcher m = p.matcher(json);
        return m.find() ? Long.parseLong(m.group(1)) : defaultVal;
    }

    private Float extractFloat(String json, String key, float defaultVal) {
        if (json == null) return defaultVal;
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*([0-9.]+)");
        Matcher m = p.matcher(json);
        return m.find() ? Float.parseFloat(m.group(1)) : defaultVal;
    }

    private boolean extractBoolean(String json, String key, boolean defaultVal) {
        if (json == null) return defaultVal;
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*(true|false)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(json);
        return m.find() ? Boolean.parseBoolean(m.group(1)) : defaultVal;
    }

    private TicketPriorityEnums parsePriority(String priority) {
        if (priority == null) return TicketPriorityEnums.MEDIUM;
        try {
            return TicketPriorityEnums.valueOf(priority.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return TicketPriorityEnums.MEDIUM;
        }
    }

    private TicketRiskLevelEnums parseRiskLevel(String riskLevel) {
        if (riskLevel == null) return TicketRiskLevelEnums.LOW;
        try {
            return TicketRiskLevelEnums.valueOf(riskLevel.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return TicketRiskLevelEnums.LOW;
        }
    }

    @Override
    public AIAnalysis getEntityById(Long id) {
        return aiAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AIAnalysis", "Id", id));
    }


}
