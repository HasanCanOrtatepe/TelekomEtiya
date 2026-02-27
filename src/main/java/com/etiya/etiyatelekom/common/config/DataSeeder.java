package com.etiya.etiyatelekom.common.config;

import com.etiya.etiyatelekom.common.enums.AgentRoleEnums;
import com.etiya.etiyatelekom.entity.Agent;
import com.etiya.etiyatelekom.entity.Department;
import com.etiya.etiyatelekom.entity.ServiceDomain;
import com.etiya.etiyatelekom.entity.Subscription;
import com.etiya.etiyatelekom.repository.AgentRepository;
import com.etiya.etiyatelekom.repository.DepartmentRepository;
import com.etiya.etiyatelekom.repository.ServiceDomainRepository;
import com.etiya.etiyatelekom.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final AgentRepository agentRepository;
    private final DepartmentRepository departmentRepository;
    private final ServiceDomainRepository serviceDomainRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.init.admin.email:admin@etiyatelekom.com}")
    private String adminEmail;

    @Value("${app.init.admin.password:Admin123!}")
    private String adminPassword;

    @Value("${app.init.admin.fullName:Sistem Yöneticisi}")
    private String adminFullName;

    @Override
    public void run(ApplicationArguments args) {
        seedDepartment();
        seedServiceDomain();
        seedSubscriptions();
        seedAdmin();
    }

    private void seedDepartment() {
        if (departmentRepository.count() > 0) return;
        Department dept = Department.builder()
                .name("Yönetim")
                .slaHours(24)
                .isActive(true)
                .build();
        departmentRepository.save(dept);
        log.info("DataSeeder: Varsayılan departman oluşturuldu.");
    }

    private void seedServiceDomain() {
        if (serviceDomainRepository.count() > 0) return;
        ServiceDomain sd = ServiceDomain.builder()
                .name("Genel")
                .isActive(true)
                .build();
        serviceDomainRepository.save(sd);
        log.info("DataSeeder: Varsayılan servis alanı oluşturuldu.");
    }

    private void seedSubscriptions() {
        if (subscriptionRepository.count() > 0) return;
        ServiceDomain genel = serviceDomainRepository.findByIsActiveTrue().stream().findFirst().orElseThrow();

        List<Subscription> subs = List.of(
                buildSubscription(genel, "İnternet 50 Mbps", 30, 299.0),
                buildSubscription(genel, "İnternet 100 Mbps", 30, 449.0),
                buildSubscription(genel, "İnternet + TV Paketi", 30, 599.0)
        );
        subscriptionRepository.saveAll(subs);
        log.info("DataSeeder: {} abonelik paketi oluşturuldu.", subs.size());
    }

    private Subscription buildSubscription(ServiceDomain sd, String packageName, int days, double price) {
        return Subscription.builder()
                .serviceDomain(sd)
                .packageName(packageName)
                .createDate(LocalDate.now())
                .durationDays(days)
                .price(price)
                .build();
    }

    private void seedAdmin() {
        if (agentRepository.existsByRole(AgentRoleEnums.ADMIN)) return;

        List<Department> depts = departmentRepository.findByIsActiveTrue();
        List<ServiceDomain> domains = serviceDomainRepository.findByIsActiveTrue();
        if (depts.isEmpty() || domains.isEmpty()) {
            log.warn("DataSeeder: Admin oluşturmak için departman ve servis alanı gerekli.");
            return;
        }

        if (agentRepository.existsByEmail(adminEmail)) {
            log.info("DataSeeder: Admin e-posta zaten kullanımda, atlanıyor.");
            return;
        }

        Agent admin = Agent.builder()
                .fullName(adminFullName)
                .email(adminEmail)
                .role(AgentRoleEnums.ADMIN)
                .department(depts.get(0))
                .serviceDomain(domains.get(0))
                .password(passwordEncoder.encode(adminPassword))
                .isActive(true)
                .build();
        agentRepository.save(admin);
        log.info("DataSeeder: İlk admin oluşturuldu. E-posta: {}", adminEmail);
    }
}
