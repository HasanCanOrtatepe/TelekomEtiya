import { useState, useEffect } from 'react';
import { Layout } from '../components/Layout';
import { Button } from '../components/Button';
import { Input } from '../components/Input';
import { Textarea } from '../components/Textarea';
import { api } from '../api/client';
import { useAuth } from '../context/AuthContext';
import styles from './Dashboard.module.css';

interface Complaint {
  id: number;
  customerId: number;
  title: string;
  description: string;
  createdAt: string;
  aiAnalysisId: number | null;
  ticketId: number | null;
  departmentId: number | null;
  serviceDomainId: number | null;
  validationMessage?: string;
}

export function Dashboard() {
  const { user } = useAuth();
  const customerId = user?.userType === 'CUSTOMER' ? user.userId : null;
  const [complaints, setComplaints] = useState<Complaint[]>([]);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [purchasingId, setPurchasingId] = useState<number | null>(null);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [form, setForm] = useState({ title: '', description: '' });
  const [showForm, setShowForm] = useState(false);

  const fetchComplaints = async () => {
    if (!customerId) return;
    try {
      const res = await api.get<{ items: Complaint[] }>(`/complaints/customer/${customerId}`);
      setComplaints(res.items || []);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Şikayetler yüklenemedi');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchComplaints();
  }, [customerId]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!customerId) return;
    setError('');
    setSuccess('');
    setSubmitting(true);
    try {
      const res = await api.post<Complaint>('/complaints', {
        customerId,
        title: form.title,
        description: form.description,
      });
      setComplaints((prev) => [res, ...prev]);
      setForm({ title: '', description: '' });
      setShowForm(false);
      if (res.validationMessage) {
        setSuccess(res.validationMessage);
      } else if (res.ticketId == null && res.departmentId === 1 && res.serviceDomainId === 1) {
        setSuccess(
          'Şikayetiniz kaydedildi. İçerik analiz edilemediği için talep oluşturulmadı. Lütfen daha açıklayıcı bir şikayet gönderin.'
        );
      } else {
        setSuccess('Şikayetiniz başarıyla oluşturuldu ve değerlendirmeye alındı.');
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Şikayet gönderilemedi');
    } finally {
      setSubmitting(false);
    }
  };

  const formatDate = (s: string) => {
    try {
      return new Date(s).toLocaleDateString('tr-TR', {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      });
    } catch {
      return s;
    }
  };

  if (!customerId) {
    return (
      <Layout>
        <div className={styles.container}>
          <p className={styles.error}>Bu sayfaya erişim için müşteri hesabı gereklidir.</p>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className={styles.container}>
        <div className={styles.header}>
          <h1>Şikayetlerim</h1>
          <Button onClick={() => setShowForm(!showForm)}>
            {showForm ? 'İptal' : '+ Yeni Şikayet'}
          </Button>
        </div>
        {error && <p className={styles.error}>{error}</p>}
        {success && <p className={styles.success}>{success}</p>}

        {showForm && (
          <div className={styles.formCard}>
            <h2>Yeni Şikayet Gönder</h2>
            <form onSubmit={handleSubmit}>
              <Input
                label="Başlık"
                value={form.title}
                onChange={(e) => setForm((f) => ({ ...f, title: e.target.value }))}
                placeholder="Şikayet başlığını yazın"
                required
                maxLength={200}
              />
              <Textarea
                label="Açıklama"
                value={form.description}
                onChange={(e) => setForm((f) => ({ ...f, description: e.target.value }))}
                placeholder="Şikayetinizi detaylı olarak açıklayın. Net ve anlamlı bir açıklama yapay zeka analizi için önemlidir."
                required
                rows={5}
                maxLength={5000}
              />
              <Button type="submit" fullWidth disabled={submitting}>
                {submitting ? 'Gönderiliyor...' : 'Gönder'}
              </Button>
            </form>
          </div>
        )}

        {loading ? (
          <p>Yükleniyor...</p>
        ) : complaints.length === 0 ? (
          <div className={styles.empty}>
            <p>Henüz şikayetiniz bulunmuyor.</p>
            <Button variant="primary" onClick={() => setShowForm(true)}>
              İlk Şikayetinizi Gönderin
            </Button>
          </div>
        ) : (
          <div className={styles.list}>
            {complaints.map((c) => (
              <div key={c.id} className={styles.complaintCard}>
                <div className={styles.complaintHeader}>
                  <h3>{c.title}</h3>
                  <span className={styles.date}>{formatDate(c.createdAt)}</span>
                </div>
                <p className={styles.description}>{c.description}</p>
                <div className={styles.status}>
                  {c.ticketId != null ? (
                    <span className={styles.badge}>Talep #{c.ticketId} oluşturuldu</span>
                  ) : (
                    <span className={styles.badgeWarning}>
                      Talep oluşturulmadı (analiz sonucu belirsiz)
                    </span>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </Layout>
  );
}
