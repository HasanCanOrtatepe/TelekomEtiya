import { useState, useEffect } from 'react';
import { api } from '../../api/client';
import { Button } from '../../components/Button';
import { Input } from '../../components/Input';
import styles from './AdminShared.module.css';

interface Agent {
  id: number;
  departmentId: number;
  serviceDomainId: number;
  fullName: string;
  email: string;
  role: string;
  isActive: boolean;
}

interface Department { id: number; name: string; }
interface ServiceDomain { id: number; name: string; }

export function AdminAgents() {
  const [items, setItems] = useState<Agent[]>([]);
  const [departments, setDepartments] = useState<Department[]>([]);
  const [serviceDomains, setServiceDomains] = useState<ServiceDomain[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [editing, setEditing] = useState<Agent | null>(null);
  const [form, setForm] = useState({ departmentId: '', serviceDomainId: '', fullName: '', email: '', role: 'AGENT', password: '' });

  const fetchData = async () => {
    try {
      const [aRes, dRes, sRes] = await Promise.all([
        api.get<{ agents: Agent[] }>('/agents'),
        api.get<{ items: Department[] }>('/departments/active'),
        api.get<{ items: ServiceDomain[] }>('/service-domains/active'),
      ]);
      setItems(aRes.agents || []);
      setDepartments(dRes.items || []);
      setServiceDomains(sRes.items || []);
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Yüklenemedi');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchData(); }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    const payload = {
      departmentId: Number(form.departmentId),
      serviceDomainId: Number(form.serviceDomainId),
      fullName: form.fullName,
      email: form.email,
      role: form.role,
      ...(form.password ? { password: form.password } : {}),
    };
    try {
      if (editing) {
        await api.put(`/agents/${editing.id}`, payload);
      } else {
        if (!form.password || form.password.length < 8) {
          setError('Şifre en az 8 karakter olmalıdır');
          return;
        }
        await api.post('/agents', { ...payload, password: form.password });
      }
      setEditing(null);
      setForm({ departmentId: '', serviceDomainId: '', fullName: '', email: '', role: 'AGENT', password: '' });
      fetchData();
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Kaydedilemedi');
    }
  };

  const handleActivate = async (id: number) => {
    setError('');
    try { await api.patch(`/agents/${id}/activate`); fetchData(); }
    catch (e) { setError(e instanceof Error ? e.message : 'İşlem başarısız'); }
  };

  const handleDeactivate = async (id: number) => {
    setError('');
    try { await api.patch(`/agents/${id}/deactivate`); fetchData(); }
    catch (e) { setError(e instanceof Error ? e.message : 'İşlem başarısız'); }
  };

  if (loading) return <p>Yükleniyor...</p>;

  return (
    <div className={styles.page}>
      <div className={styles.sectionHeader}>
        <h1>Agentler</h1>
        <Button onClick={() => { setEditing(null); setForm({ departmentId: '', serviceDomainId: '', fullName: '', email: '', role: 'AGENT', password: '' }); }}>+ Yeni Agent</Button>
      </div>
      {error && <p style={{ color: 'var(--error)' }}>{error}</p>}

      <div style={{ background: 'white', padding: '1.5rem', borderRadius: 12, marginBottom: '1rem', boxShadow: '0 2px 12px var(--border)' }}>
        <h3 style={{ marginBottom: '1rem' }}>{editing ? 'Düzenle' : 'Yeni Agent'}</h3>
        <form onSubmit={handleSubmit}>
          <Input label="Ad Soyad" value={form.fullName} onChange={(e) => setForm((f) => ({ ...f, fullName: e.target.value }))} required />
          <Input label="E-posta" type="email" value={form.email} onChange={(e) => setForm((f) => ({ ...f, email: e.target.value }))} required />
          <Input label="Şifre" type="password" value={form.password} onChange={(e) => setForm((f) => ({ ...f, password: e.target.value }))} placeholder={editing ? 'Değiştirmek için doldurun (boş bırakın)' : 'En az 8 karakter'} required={!editing} />
          <label>Rol</label>
          <select value={form.role} onChange={(e) => setForm((f) => ({ ...f, role: e.target.value }))} required style={{ width: '100%', padding: '0.5rem', marginBottom: '1rem' }}>
            <option value="AGENT">Agent</option>
            <option value="SUPERVISOR">Supervisor</option>
            <option value="SENIOR_AGENT">Senior Agent</option>
            <option value="ADMIN">Admin</option>
          </select>
          <label>Departman</label>
          <select value={form.departmentId} onChange={(e) => setForm((f) => ({ ...f, departmentId: e.target.value }))} required style={{ width: '100%', padding: '0.5rem', marginBottom: '1rem' }}>
            <option value="">Seçin</option>
            {departments.map((d) => <option key={d.id} value={d.id}>{d.name}</option>)}
          </select>
          <label>Servis Alanı</label>
          <select value={form.serviceDomainId} onChange={(e) => setForm((f) => ({ ...f, serviceDomainId: e.target.value }))} required style={{ width: '100%', padding: '0.5rem', marginBottom: '1rem' }}>
            <option value="">Seçin</option>
            {serviceDomains.map((s) => <option key={s.id} value={s.id}>{s.name}</option>)}
          </select>
          <div style={{ display: 'flex', gap: '0.75rem', marginTop: '1rem' }}>
            <Button type="submit">{editing ? 'Güncelle' : 'Oluştur'}</Button>
            {editing && <Button type="button" variant="outline" onClick={() => setEditing(null)}>İptal</Button>}
          </div>
        </form>
      </div>

      <div className={styles.tableWrap}>
        <table className={styles.table}>
          <thead>
            <tr><th>ID</th><th>Ad</th><th>E-posta</th><th>Rol</th><th>Durum</th><th>İşlemler</th></tr>
          </thead>
          <tbody>
            {items.map((a) => (
              <tr key={a.id}>
                <td>{a.id}</td>
                <td>{a.fullName}</td>
                <td>{a.email}</td>
                <td>{a.role}</td>
                <td>{a.isActive ? 'Aktif' : 'Pasif'}</td>
                <td>
                  <div className={styles.btnGroup}>
                    <button className={`${styles.btnSm} ${styles.btnSmPrimary}`} onClick={() => {
                      setEditing(a);
                      setForm({
                        departmentId: String(a.departmentId),
                        serviceDomainId: String(a.serviceDomainId),
                        fullName: a.fullName,
                        email: a.email,
                        role: a.role,
                        password: '',
                      });
                    }}>Düzenle</button>
                    {a.role !== 'ADMIN' && (
                      a.isActive
                        ? <button className={`${styles.btnSm} ${styles.btnSmDanger}`} onClick={() => handleDeactivate(a.id)}>Pasif Yap</button>
                        : <button className={`${styles.btnSm} ${styles.btnSmSecondary}`} onClick={() => handleActivate(a.id)}>Aktif Yap</button>
                    )}
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
