import { useState, useEffect } from 'react';
import { api } from '../../api/client';
import { Button } from '../../components/Button';
import { Input } from '../../components/Input';
import { useAuth } from '../../context/AuthContext';
import styles from './AdminShared.module.css';

interface Subscription {
  id: number;
  serviceType: string;
  packageName: string;
  durationDays: number;
  price?: number;
}

interface ServiceDomain {
  id: number;
  name: string;
}

export function AdminSubscriptions() {
  const { user } = useAuth();
  const isAdmin = user?.authorities?.includes('ROLE_ADMIN') ?? false;
  const [items, setItems] = useState<Subscription[]>([]);
  const [serviceDomains, setServiceDomains] = useState<ServiceDomain[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [editing, setEditing] = useState<Subscription | null>(null);
  const [form, setForm] = useState({ serviceDomainId: '', packageName: '', price: '', durationDays: '30' });

  const fetchData = async () => {
    try {
      const [sRes, sdRes] = await Promise.all([
        api.get<{ items: Subscription[] }>('/subscriptions'),
        api.get<{ items: ServiceDomain[] }>('/service-domains/active'),
      ]);
      setItems(sRes.items || []);
      setServiceDomains(sdRes.items || []);
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
    const payload = { serviceDomainId: Number(form.serviceDomainId), packageName: form.packageName, price: Number(form.price), durationDays: Number(form.durationDays) };
    try {
      if (editing) {
        await api.put(`/subscriptions/${editing.id}`, payload);
      } else {
        await api.post('/subscriptions', payload);
      }
      setEditing(null);
      setForm({ serviceDomainId: '', packageName: '', price: '', durationDays: '30' });
      fetchData();
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Kaydedilemedi');
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm('Bu aboneliği silmek istediğinize emin misiniz?')) return;
    setError('');
    try {
      await api.delete(`/subscriptions/${id}/delete`);
      fetchData();
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Silinemedi');
    }
  };

  if (loading) return <p>Yükleniyor...</p>;

  return (
    <div className={styles.page}>
      <div className={styles.sectionHeader}>
        <h1>Abonelikler</h1>
        {isAdmin && (
          <Button onClick={() => { setEditing(null); setForm({ serviceDomainId: '', packageName: '', price: '', durationDays: '30' }); }}>+ Yeni Abonelik</Button>
        )}
      </div>
      {error && <p style={{ color: 'var(--error)' }}>{error}</p>}

      {isAdmin && (
      <div style={{ background: 'white', padding: '1.5rem', borderRadius: 12, marginBottom: '1rem', boxShadow: '0 2px 12px var(--border)' }}>
        <h3 style={{ marginBottom: '1rem' }}>{editing ? 'Düzenle' : 'Yeni Abonelik'}</h3>
        <form onSubmit={handleSubmit}>
          <label>Servis Alanı</label>
          <select value={form.serviceDomainId} onChange={(e) => setForm((f) => ({ ...f, serviceDomainId: e.target.value }))} required style={{ width: '100%', padding: '0.5rem', marginBottom: '1rem' }}>
            <option value="">Seçin</option>
            {serviceDomains.map((s) => <option key={s.id} value={s.id}>{s.name}</option>)}
          </select>
          <Input label="Paket Adı" value={form.packageName} onChange={(e) => setForm((f) => ({ ...f, packageName: e.target.value }))} required />
          <Input label="Fiyat (₺)" type="number" step="0.01" value={form.price} onChange={(e) => setForm((f) => ({ ...f, price: e.target.value }))} required />
          <Input label="Süre (gün)" type="number" min={1} value={form.durationDays} onChange={(e) => setForm((f) => ({ ...f, durationDays: e.target.value }))} required />
          <div style={{ display: 'flex', gap: '0.75rem', marginTop: '1rem' }}>
            <Button type="submit">{editing ? 'Güncelle' : 'Oluştur'}</Button>
            {editing && <Button type="button" variant="outline" onClick={() => setEditing(null)}>İptal</Button>}
          </div>
        </form>
      </div>
      )}

      <div className={styles.tableWrap}>
        <table className={styles.table}>
          <thead>
            <tr><th>ID</th><th>Paket</th><th>Servis</th><th>Süre</th><th>Fiyat</th><th>İşlemler</th></tr>
          </thead>
          <tbody>
            {items.map((s) => (
              <tr key={s.id}>
                <td>{s.id}</td>
                <td>{s.packageName}</td>
                <td>{s.serviceType}</td>
                <td>{s.durationDays} gün</td>
                <td>₺{s.price?.toLocaleString('tr-TR')}</td>
                <td>
                  {isAdmin ? (
                    <div className={styles.btnGroup}>
                      <button className={`${styles.btnSm} ${styles.btnSmPrimary}`} onClick={() => {
                        const sdId = serviceDomains.find(sd => sd.name === s.serviceType)?.id ?? '';
                        setEditing(s);
                        setForm({ serviceDomainId: String(sdId), packageName: s.packageName, price: String(s.price ?? ''), durationDays: String(s.durationDays) });
                      }}>Düzenle</button>
                      <button className={`${styles.btnSm} ${styles.btnSmDanger}`} onClick={() => handleDelete(s.id)}>Sil</button>
                    </div>
                  ) : (
                    <span style={{ color: 'var(--secondary-light)' }}>Salt okunur</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
