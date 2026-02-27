import { useState, useEffect } from 'react';
import { api } from '../../api/client';
import { Button } from '../../components/Button';
import { Input } from '../../components/Input';
import styles from './AdminShared.module.css';

interface ServiceDomain {
  id: number;
  name: string;
  isActive?: boolean;
}

export function AdminServiceDomains() {
  const [items, setItems] = useState<ServiceDomain[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [editing, setEditing] = useState<ServiceDomain | null>(null);
  const [form, setForm] = useState({ name: '' });

  const fetchData = async () => {
    try {
      const res = await api.get<{ items: ServiceDomain[] }>('/service-domains');
      setItems(res.items || []);
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
    try {
      if (editing) {
        await api.put(`/service-domains/${editing.id}`, form);
      } else {
        await api.post('/service-domains', form);
      }
      setEditing(null);
      setForm({ name: '' });
      fetchData();
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Kaydedilemedi');
    }
  };

  const handleActivate = async (id: number) => {
    setError('');
    try { await api.patch(`/service-domains/${id}/activate`); fetchData(); }
    catch (e) { setError(e instanceof Error ? e.message : 'İşlem başarısız'); }
  };

  const handleDeactivate = async (id: number) => {
    setError('');
    try { await api.patch(`/service-domains/${id}/deactivate`); fetchData(); }
    catch (e) { setError(e instanceof Error ? e.message : 'İşlem başarısız'); }
  };

  if (loading) return <p>Yükleniyor...</p>;

  return (
    <div className={styles.page}>
      <div className={styles.sectionHeader}>
        <h1>Servis Alanları</h1>
        <Button onClick={() => { setEditing(null); setForm({ name: '' }); }}>+ Yeni Servis Alanı</Button>
      </div>
      {error && <p style={{ color: 'var(--error)' }}>{error}</p>}

      <div style={{ background: 'white', padding: '1.5rem', borderRadius: 12, marginBottom: '1rem', boxShadow: '0 2px 12px var(--border)' }}>
        <h3 style={{ marginBottom: '1rem' }}>{editing ? 'Düzenle' : 'Yeni Servis Alanı'}</h3>
        <form onSubmit={handleSubmit}>
          <Input label="Ad" value={form.name} onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))} required />
          <div style={{ display: 'flex', gap: '0.75rem', marginTop: '1rem' }}>
            <Button type="submit">{editing ? 'Güncelle' : 'Oluştur'}</Button>
            {editing && <Button type="button" variant="outline" onClick={() => setEditing(null)}>İptal</Button>}
          </div>
        </form>
      </div>

      <div className={styles.tableWrap}>
        <table className={styles.table}>
          <thead>
            <tr><th>ID</th><th>Ad</th><th>Durum</th><th>İşlemler</th></tr>
          </thead>
          <tbody>
            {items.map((s) => (
              <tr key={s.id}>
                <td>{s.id}</td>
                <td>{s.name}</td>
                <td>{s.isActive !== false ? 'Aktif' : 'Pasif'}</td>
                <td>
                  <div className={styles.btnGroup}>
                    <button className={`${styles.btnSm} ${styles.btnSmPrimary}`} onClick={() => { setEditing(s); setForm({ name: s.name }); }}>Düzenle</button>
                    {s.isActive !== false ? (
                      <button className={`${styles.btnSm} ${styles.btnSmDanger}`} onClick={() => handleDeactivate(s.id)}>Pasif Yap</button>
                    ) : (
                      <button className={`${styles.btnSm} ${styles.btnSmSecondary}`} onClick={() => handleActivate(s.id)}>Aktif Yap</button>
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
