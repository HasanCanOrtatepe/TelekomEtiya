import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../../api/client';
import { Button } from '../../components/Button';
import styles from './AdminShared.module.css';

interface Ticket {
  id: number;
  complaintId: number;
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

export function AdminTickets() {
  const navigate = useNavigate();
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [agents, setAgents] = useState<{ id: number; fullName: string; isActive?: boolean }[]>([]);
  const [assignAgents, setAssignAgents] = useState<{ id: number; fullName: string }[]>([]);
  const [departments, setDepartments] = useState<{ id: number; name: string }[]>([]);
  const [serviceDomains, setServiceDomains] = useState<{ id: number; name: string }[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [modal, setModal] = useState<'assign' | 'status' | 'routing' | null>(null);
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const [form, setForm] = useState({ agentId: '', status: '', departmentId: '', serviceDomainId: '' });

  const fetchData = async () => {
    try {
      const [tRes, aRes, dRes, sRes] = await Promise.all([
        api.get<{ items: Ticket[] }>('/tickets'),
        api.get<{ agents: { id: number; fullName: string; isActive?: boolean }[] }>('/agents'),
        api.get<{ items: { id: number; name: string }[] }>('/departments/active'),
        api.get<{ items: { id: number; name: string }[] }>('/service-domains/active'),
      ]);
      setTickets(tRes.items || []);
      setAgents(aRes.agents || []);
      setDepartments(dRes.items || []);
      setServiceDomains(sRes.items || []);
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Yüklenemedi');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchData(); }, []);

  const openModal = async (type: 'assign' | 'status' | 'routing', id: number) => {
    setModal(type);
    setSelectedId(id);
    setForm({ agentId: '', status: '', departmentId: '', serviceDomainId: '' });
    if (type === 'assign') {
      const ticket = tickets.find((t) => t.id === id);
      if (ticket?.departmentId && ticket?.serviceDomainId) {
        try {
          const aRes = await api.get<{ agents: { id: number; fullName: string }[] }>(
            `/agents/available/${ticket.departmentId}/${ticket.serviceDomainId}`,
          );
          setAssignAgents(aRes.agents || []);
        } catch {
          setAssignAgents(agents.filter((a) => a.isActive !== false));
        }
      } else {
        setAssignAgents(agents.filter((a) => a.isActive !== false));
      }
    }
  };

  const handleAssign = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedId || !form.agentId) return;
    setError('');
    try {
      await api.patch(`/tickets/${selectedId}/assign`, { agentId: Number(form.agentId) });
      setModal(null);
      fetchData();
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Atama başarısız');
    }
  };

  const handleStatus = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedId || !form.status) return;
    setError('');
    try {
      await api.patch(`/tickets/${selectedId}/status`, { status: form.status });
      setModal(null);
      fetchData();
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Güncelleme başarısız');
    }
  };

  const handleRouting = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedId || !form.departmentId || !form.serviceDomainId) return;
    setError('');
    try {
      await api.patch(`/tickets/${selectedId}/routing`, {
        departmentId: Number(form.departmentId),
        serviceDomainId: Number(form.serviceDomainId),
      });
      setModal(null);
      fetchData();
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Yönlendirme başarısız');
    }
  };

  const handleClose = async (id: number) => {
    setError('');
    try {
      await api.patch(`/tickets/${id}/close`);
      fetchData();
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Kapatma başarısız');
    }
  };

  const formatDate = (s: string) => s ? new Date(s).toLocaleString('tr-TR') : '-';

  if (loading) return <p>Yükleniyor...</p>;

  return (
    <div className={styles.page}>
      <div className={styles.sectionHeader}>
        <h1>Talepler</h1>
      </div>
      {error && <p className={styles.error} style={{ color: 'var(--error)' }}>{error}</p>}
      <div className={styles.tableWrap}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>ID</th>
              <th>Şikayet</th>
              <th>Durum</th>
              <th>Öncelik</th>
              <th>SLA</th>
              <th>Oluşturulma</th>
              <th>İşlemler</th>
            </tr>
          </thead>
          <tbody>
            {tickets.map((t) => (
              <tr key={t.id}>
                <td>
                  <button type="button" className={styles.ticketLink} onClick={() => navigate(`/admin/tickets/${t.id}`)}>
                    {t.id}
                  </button>
                </td>
                <td>#{t.complaintId}</td>
                <td>{t.status}</td>
                <td>{t.priority}</td>
                <td>{formatDate(t.slaDueAt)}</td>
                <td>{formatDate(t.createdAt)}</td>
                <td>
                  <div className={styles.btnGroup}>
                    <button className={`${styles.btnSm} ${styles.btnSmSecondary}`} onClick={() => navigate(`/admin/tickets/${t.id}`)}>Detaylar</button>
                    {t.status !== 'CLOSED' && (
                      <>
                        {!t.assignedAgentId && (
                          <button className={`${styles.btnSm} ${styles.btnSmPrimary}`} onClick={() => openModal('assign', t.id)}>Ata</button>
                        )}
                        <button className={`${styles.btnSm} ${styles.btnSmSecondary}`} onClick={() => openModal('status', t.id)}>Durum</button>
                        <button className={`${styles.btnSm} ${styles.btnSmSecondary}`} onClick={() => openModal('routing', t.id)}>Yönlendir</button>
                        <button className={`${styles.btnSm} ${styles.btnSmDanger}`} onClick={() => handleClose(t.id)}>Kapat</button>
                      </>
                    )}
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {modal === 'assign' && selectedId && (
        <div className={styles.modalOverlay} onClick={() => setModal(null)}>
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <h3>Agent Ata</h3>
            <form onSubmit={handleAssign}>
              <label>Agent</label>
              <select
                value={form.agentId}
                onChange={(e) => setForm((f) => ({ ...f, agentId: e.target.value }))}
                required
                style={{ width: '100%', padding: '0.5rem', marginBottom: '1rem' }}
              >
                <option value="">Seçin</option>
                {(assignAgents.length > 0 ? assignAgents : agents.filter((a) => a.isActive !== false)).map((a) => (
                  <option key={a.id} value={a.id}>{a.fullName}</option>
                ))}
              </select>
              <div className={styles.modalActions}>
                <Button type="button" variant="outline" onClick={() => setModal(null)}>İptal</Button>
                <Button type="submit">Ata</Button>
              </div>
            </form>
          </div>
        </div>
      )}

      {modal === 'status' && selectedId && (
        <div className={styles.modalOverlay} onClick={() => setModal(null)}>
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <h3>Durum Güncelle</h3>
            <form onSubmit={handleStatus}>
              <label>Durum</label>
              <select
                value={form.status}
                onChange={(e) => setForm((f) => ({ ...f, status: e.target.value }))}
                required
                style={{ width: '100%', padding: '0.5rem', marginBottom: '1rem' }}
              >
                <option value="">Seçin</option>
                <option value="CREATED">Oluşturuldu</option>
                <option value="ASSIGNED">Atandı</option>
                <option value="RESOLVED">Çözüldü</option>
                <option value="ESCALATED">Yükseltildi</option>
                <option value="CLOSED">Kapatıldı</option>
              </select>
              <div className={styles.modalActions}>
                <Button type="button" variant="outline" onClick={() => setModal(null)}>İptal</Button>
                <Button type="submit">Güncelle</Button>
              </div>
            </form>
          </div>
        </div>
      )}

      {modal === 'routing' && selectedId && (
        <div className={styles.modalOverlay} onClick={() => setModal(null)}>
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <h3>Yönlendir</h3>
            <form onSubmit={handleRouting}>
              <label>Departman</label>
              <select
                value={form.departmentId}
                onChange={(e) => setForm((f) => ({ ...f, departmentId: e.target.value }))}
                required
                style={{ width: '100%', padding: '0.5rem', marginBottom: '1rem' }}
              >
                <option value="">Seçin</option>
                {departments.map((d) => (
                  <option key={d.id} value={d.id}>{d.name}</option>
                ))}
              </select>
              <label>Servis Alanı</label>
              <select
                value={form.serviceDomainId}
                onChange={(e) => setForm((f) => ({ ...f, serviceDomainId: e.target.value }))}
                required
                style={{ width: '100%', padding: '0.5rem', marginBottom: '1rem' }}
              >
                <option value="">Seçin</option>
                {serviceDomains.map((s) => (
                  <option key={s.id} value={s.id}>{s.name}</option>
                ))}
              </select>
              <div className={styles.modalActions}>
                <Button type="button" variant="outline" onClick={() => setModal(null)}>İptal</Button>
                <Button type="submit">Yönlendir</Button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
