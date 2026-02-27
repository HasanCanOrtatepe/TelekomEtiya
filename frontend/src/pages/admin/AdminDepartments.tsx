import { useState, useEffect } from 'react';
import { api } from '../../api/client';
import { Button } from '../../components/Button';
import { Input } from '../../components/Input';
import styles from './AdminShared.module.css';

interface Department {
  id: number;
  name: string;
  slaHours: number;
  isActive?: boolean;
}

export function AdminDepartments() {
  const [items, setItems] = useState<Department[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [editing, setEditing] = useState<Department | null>(null);
  const [form, setForm] = useState({ name: '', slaHours: 24 });

  const fetchData = async () => {
    try {
      const res = await api.get<{ items: Department[] }>('/departments');
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
        await api.put(`/departments/${editing.id}`, form);
      } else {
        await api.post('/departments', form);
      }
      setEditing(null);
      setForm({ name: '', slaHours: 24 });
      fetchData();
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Kaydedilemedi');
    }
  };

  const handleActivate = async (id: number) => {
    setError('');
    try {
      await api.patch(`/departments/${id}/activate`);
      fetchData();
    } catch (e) {
      setError(e instanceof Error ? e.message : 'İşlem başarısız');
    }
  };

  const handleDeactivate = async (id: number) => {
    setError('');
    try {
      await api.patch(`/departments/${id}/deactivate`);
      fetchData();
    } catch (e) {
      setError(e instanceof Error ? e.message : 'İşlem başarısız');
    }
  };

  const startEdit = (d: Department) => {
    setEditing(d);
    setForm({ name: d.name, slaHours: d.slaHours });
  };

  if (loading) return <p>Yükleniyor...</p>;

  return (
    <div className={styles.page}>
      <div className={styles.sectionHeader}>
        <h1>Departmanlar</h1>
        <Button onClick={() => { setEditing(null); setForm({ name: '', slaHours: 24 }); }}>
          + Yeni Departman
        </Button>
      </div>
      {error && <p style={{ color: 'var(--error)' }}>{error}</p>}

      <div style={{ background: 'white', padding: '1.5rem', borderRadius: 12, marginBottom: '1rem', boxShadow: '0 2px 12px var(--border)' }}>
          <h3 style={{ marginBottom: '1rem' }}>{editing ? 'Düzenle' : 'Yeni Departman'}</h3>
          <form onSubmit={handleSubmit}>
            <Input label="Ad" value={form.name} onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))} required />
            <Input label="SLA (saat)" type="number" min={1} max={720} value={String(form.slaHours)} onChange={(e) => setForm((f) => ({ ...f, slaHours: Number(e.target.value) }))} required />
            <div style={{ display: 'flex', gap: '0.75rem', marginTop: '1rem' }}>
              <Button type="submit">{editing ? 'Güncelle' : 'Oluştur'}</Button>
              {editing && <Button type="button" variant="outline" onClick={() => setEditing(null)}>İptal</Button>}
            </div>
          </form>
      </div>

      <div className={styles.tableWrap}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>ID</th>
              <th>Ad</th>
              <th>SLA (saat)</th>
              <th>Durum</th>
              <th>İşlemler</th>
            </tr>
          </thead>
          <tbody>
            {items.map((d) => (
              <tr key={d.id}>
                <td>{d.id}</td>
                <td>{d.name}</td>
                <td>{d.slaHours}</td>
                <td>{d.isActive !== false ? 'Aktif' : 'Pasif'}</td>
                <td>
                  <div className={styles.btnGroup}>
                    <button className={`${styles.btnSm} ${styles.btnSmPrimary}`} onClick={() => startEdit(d)}>Düzenle</button>
                    {d.isActive !== false ? (
                      <button className={`${styles.btnSm} ${styles.btnSmDanger}`} onClick={() => handleDeactivate(d.id)}>Pasif Yap</button>
                    ) : (
                      <button className={`${styles.btnSm} ${styles.btnSmSecondary}`} onClick={() => handleActivate(d.id)}>Aktif Yap</button>
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
