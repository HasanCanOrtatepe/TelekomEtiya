import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { api } from '../../api/client';
import { Button } from '../../components/Button';
import styles from './AdminShared.module.css';

interface Ticket {
  id: number;
  complaintId?: number | null;
  departmentId: number;
  serviceDomainId: number;
  assignedAgentId: number | null;
  status: string;
  priority: string;
  riskLevel: string;
  slaDueAt: string;
  createdAt: string;
  closedAt: string | null;
}

interface Complaint {
  id: number;
  customerId: number;
  title: string;
  description: string;
  createdAt: string;
}

interface TicketStatusHistory {
  id: number;
  ticketId: number;
  fromStatus: string | null;
  toStatus: string;
  changedAt: string;
  agentId?: number;
  AgentId?: number; /* backend may serialize as AgentId */
}

export function AdminTicketDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [ticket, setTicket] = useState<Ticket | null>(null);
  const [complaint, setComplaint] = useState<Complaint | null>(null);
  const [history, setHistory] = useState<TicketStatusHistory[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!id) return;
    const fetchData = async () => {
      try {
        const ticketRes = await api.get<Ticket>(`/tickets/${id}`);
        setTicket(ticketRes);

        const complaintId = ticketRes.complaintId;
        const fetchComplaint = complaintId
          ? api.get<Complaint>(`/complaints/${complaintId}`)
          : Promise.reject(new Error('No complaintId'));
        const fetchHistory = api.get<{ items?: TicketStatusHistory[]; count?: number } | TicketStatusHistory[]>(
          `/ticketStatusHistories/ticket/${id}`,
        );

        const [complaintSettled, historySettled] = await Promise.allSettled([fetchComplaint, fetchHistory]);

        if (complaintSettled.status === 'fulfilled') setComplaint(complaintSettled.value);
        if (historySettled.status === 'fulfilled') {
          const h = historySettled.value;
          setHistory(Array.isArray(h) ? h : (h.items ?? []));
        }
      } catch (e) {
        setError(e instanceof Error ? e.message : 'Yüklenemedi');
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [id]);

  const formatDate = (s: string) => (s ? new Date(s).toLocaleString('tr-TR') : '-');

  if (loading) return <p>Yükleniyor...</p>;
  if (error) return <p style={{ color: 'var(--error)' }}>{error}</p>;
  if (!ticket) return <p>Talep bulunamadı.</p>;

  return (
    <div className={styles.page}>
      <div className={styles.sectionHeader}>
        <h1>Talep #{ticket.id}</h1>
        <Button variant="outline" onClick={() => navigate('/admin/tickets')}>
          ← Listeye Dön
        </Button>
      </div>

      <div style={{ display: 'grid', gap: '1.5rem', maxWidth: 800 }}>
        <section style={{ background: 'white', padding: '1.5rem', borderRadius: 12, boxShadow: '0 2px 12px var(--border)' }}>
          <h2 style={{ fontSize: '1.1rem', marginBottom: '1rem', color: 'var(--secondary)' }}>Talep Bilgileri</h2>
          <table className={styles.table} style={{ margin: 0 }}>
            <tbody>
              <tr><td><strong>Durum</strong></td><td>{ticket.status}</td></tr>
              <tr><td><strong>Öncelik</strong></td><td>{ticket.priority}</td></tr>
              <tr><td><strong>Risk Seviyesi</strong></td><td>{ticket.riskLevel}</td></tr>
              <tr><td><strong>Departman ID</strong></td><td>{ticket.departmentId}</td></tr>
              <tr><td><strong>Servis Alanı ID</strong></td><td>{ticket.serviceDomainId}</td></tr>
              <tr><td><strong>Atanan Agent ID</strong></td><td>{ticket.assignedAgentId ?? '-'}</td></tr>
              <tr><td><strong>SLA Bitiş</strong></td><td>{formatDate(ticket.slaDueAt)}</td></tr>
              <tr><td><strong>Oluşturulma</strong></td><td>{formatDate(ticket.createdAt)}</td></tr>
              <tr><td><strong>Kapanma</strong></td><td>{formatDate(ticket.closedAt || '') || '-'}</td></tr>
            </tbody>
          </table>
        </section>

        {complaint && (
          <section style={{ background: 'white', padding: '1.5rem', borderRadius: 12, boxShadow: '0 2px 12px var(--border)' }}>
            <h2 style={{ fontSize: '1.1rem', marginBottom: '1rem', color: 'var(--secondary)' }}>İlişkili Şikayet</h2>
            <p><strong>Başlık:</strong> {complaint.title}</p>
            <p><strong>Oluşturulma:</strong> {formatDate(complaint.createdAt)}</p>
            <p><strong>Müşteri ID:</strong> {complaint.customerId}</p>
            <p style={{ marginTop: '0.75rem' }}><strong>Açıklama:</strong></p>
            <p style={{ background: '#f8f9fa', padding: '1rem', borderRadius: 8, whiteSpace: 'pre-wrap' }}>{complaint.description}</p>
          </section>
        )}

        <section style={{ background: 'white', padding: '1.5rem', borderRadius: 12, boxShadow: '0 2px 12px var(--border)' }}>
          <h2 style={{ fontSize: '1.1rem', marginBottom: '1rem', color: 'var(--secondary)' }}>Talep Geçmişi</h2>
          {history.length === 0 ? (
            <p style={{ color: 'var(--secondary-light)' }}>Henüz geçmiş kaydı yok.</p>
          ) : (
            <div className={styles.tableWrap}>
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th>Tarih</th>
                    <th>Önceki Durum</th>
                    <th>Yeni Durum</th>
                    <th>Agent ID</th>
                  </tr>
                </thead>
                <tbody>
                  {history.map((h) => (
                    <tr key={h.id}>
                      <td>{formatDate(h.changedAt)}</td>
                      <td>{h.fromStatus ?? '-'}</td>
                      <td>{h.toStatus}</td>
                      <td>{h.agentId ?? (h as { AgentId?: number }).AgentId ?? '-'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </div>
    </div>
  );
}
