import { useState, useEffect } from 'react';
import { api } from '../../api/client';
import { Button } from '../../components/Button';
import { Input } from '../../components/Input';
import styles from './AdminShared.module.css';

interface Customer {
  id: number;
  customerNo: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
}

export function AdminCustomers() {
  const [items, setItems] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [editing, setEditing] = useState<Customer | null>(null);
  const [form, setForm] = useState({ firstName: '', lastName: '', email: '', phone: '' });

  const fetchData = async () => {
    try {
      const res = await api.get<{ customerResponses: Customer[] }>('/customers');
      setItems(res.customerResponses || []);
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
        await api.put(`/customers/${editing.id}`, form);
      }
      setEditing(null);
      setForm({ firstName: '', lastName: '', email: '', phone: '' });
      fetchData();
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Kaydedilemedi');
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm('Bu müşteriyi silmek istediğinize emin misiniz?')) return;
    try {
      await api.delete(`/customers/${id}`);
      fetchData();
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Silinemedi');
    }
  };

  if (loading) return <p>Yükleniyor...</p>;

  return (
    <div className={styles.page}>
      <div className={styles.sectionHeader}>
        <h1>Müşteriler</h1>
      </div>
      {error && <p style={{ color: 'var(--error)' }}>{error}</p>}

      {editing && (
        <div style={{ background: 'white', padding: '1.5rem', borderRadius: 12, marginBottom: '1rem', boxShadow: '0 2px 12px var(--border)' }}>
          <h3 style={{ marginBottom: '1rem' }}>Müşteri Düzenle</h3>
          <form onSubmit={handleSubmit}>
            <Input label="Ad" value={form.firstName} onChange={(e) => setForm((f) => ({ ...f, firstName: e.target.value }))} required />
            <Input label="Soyad" value={form.lastName} onChange={(e) => setForm((f) => ({ ...f, lastName: e.target.value }))} required />
            <Input label="E-posta" type="email" value={form.email} onChange={(e) => setForm((f) => ({ ...f, email: e.target.value }))} required />
            <Input label="Telefon" value={form.phone} onChange={(e) => setForm((f) => ({ ...f, phone: e.target.value }))} />
            <div style={{ display: 'flex', gap: '0.75rem', marginTop: '1rem' }}>
              <Button type="submit">Güncelle</Button>
              <Button type="button" variant="outline" onClick={() => setEditing(null)}>İptal</Button>
            </div>
          </form>
        </div>
      )}

      <div className={styles.tableWrap}>
        <table className={styles.table}>
          <thead>
            <tr><th>ID</th><th>Müşteri No</th><th>Ad Soyad</th><th>E-posta</th><th>Telefon</th><th>İşlemler</th></tr>
          </thead>
          <tbody>
            {items.map((c) => (
              <tr key={c.id}>
                <td>{c.id}</td>
                <td>{c.customerNo}</td>
                <td>{c.firstName} {c.lastName}</td>
                <td>{c.email}</td>
                <td>{c.phone || '-'}</td>
                <td>
                  <div className={styles.btnGroup}>
                    <button className={`${styles.btnSm} ${styles.btnSmPrimary}`} onClick={() => {
                      setEditing(c);
                      setForm({ firstName: c.firstName, lastName: c.lastName, email: c.email, phone: c.phone || '' });
                    }}>Düzenle</button>
                    <button className={`${styles.btnSm} ${styles.btnSmDanger}`} onClick={() => handleDelete(c.id)}>Sil</button>
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
