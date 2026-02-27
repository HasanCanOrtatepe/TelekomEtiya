import { useState, useEffect } from 'react';
import { Layout } from '../components/Layout';
import { Button } from '../components/Button';
import { api } from '../api/client';
import { useAuth } from '../context/AuthContext';
import styles from './Dashboard.module.css';

interface Subscription {
  id: number;
  serviceType: string;
  packageName: string;
  durationDays: number;
  price?: number;
}

interface CustomerSubscription {
  id: number;
  customerId: number;
  subscriptionId: number;
  serviceType: string;
  packageName: string;
  startDate: string;
  endDate: string;
  status: string;
}

export function MySubscriptions() {
  const { user } = useAuth();
  const customerId = user?.userType === 'CUSTOMER' ? user.userId : null;
  const [mySubscriptions, setMySubscriptions] = useState<CustomerSubscription[]>([]);
  const [availableSubscriptions, setAvailableSubscriptions] = useState<Subscription[]>([]);
  const [loading, setLoading] = useState(true);
  const [purchasingId, setPurchasingId] = useState<number | null>(null);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const fetchSubscriptions = async () => {
    if (!customerId) return;
    try {
      const [subsRes, myRes] = await Promise.all([
        api.get<{ items: Subscription[] }>('/subscriptions'),
        api.get<{ items: CustomerSubscription[] }>(`/customer-subscriptions/customer/active/${customerId}`),
      ]);
      setAvailableSubscriptions(subsRes.items || []);
      setMySubscriptions(myRes.items || []);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Abonelikler yüklenemedi');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSubscriptions();
  }, [customerId]);

  const formatShortDate = (s: string) => {
    try {
      return new Date(s).toLocaleDateString('tr-TR', {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
      });
    } catch {
      return s;
    }
  };

  const handleCancelSubscription = async (subscriptionId: number) => {
    if (!customerId) return;
    if (!confirm('Bu aboneliği iptal etmek istediğinize emin misiniz?')) return;
    setError('');
    setSuccess('');
    try {
      await api.patch(`/customer-subscriptions/${subscriptionId}/cancel`);
      setSuccess('Abonelik iptal edildi.');
      fetchSubscriptions();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Abonelik iptal edilemedi');
    }
  };

  const handlePurchase = async (subscriptionId: number) => {
    if (!customerId) return;
    setError('');
    setSuccess('');
    setPurchasingId(subscriptionId);
    try {
      await api.post('/customer-subscriptions', { customerId, subscriptionId });
      setSuccess('Abonelik başarıyla satın alındı.');
      fetchSubscriptions();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Abonelik satın alınamadı');
    } finally {
      setPurchasingId(null);
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
          <h1>Aboneliklerim</h1>
        </div>
        {error && <p className={styles.error}>{error}</p>}
        {success && <p className={styles.success}>{success}</p>}

        {loading ? (
          <p>Yükleniyor...</p>
        ) : (
          <>
            {mySubscriptions.length > 0 && (
              <div className={styles.subList}>
                <h2 className={styles.sectionTitle}>Aktif Abonelikler</h2>
                {mySubscriptions.map((s) => (
                  <div key={s.id} className={styles.subCard}>
                    <div className={styles.subInfo}>
                      <strong>{s.packageName}</strong>
                      <span>{s.serviceType}</span>
                      <span>
                        {formatShortDate(s.startDate)} - {formatShortDate(s.endDate)}
                      </span>
                    </div>
                    <div className={styles.subActions}>
                      <span className={styles.badge}>{s.status}</span>
                      {s.status === 'ACTIVE' && (
                        <Button variant="outline" className={styles.cancelBtn} onClick={() => handleCancelSubscription(s.id)}>
                          İptal Et
                        </Button>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
            <div className={styles.subList}>
              <h2 className={styles.sectionTitle}>Mevcut Paketler</h2>
              <p className={styles.subHint}>Aşağıdaki paketlerden abone olabilirsiniz.</p>
              <div className={styles.packageGrid}>
                {availableSubscriptions.map((p) => {
                  const alreadyOwned = mySubscriptions.some((s) => s.subscriptionId === p.id);
                  return (
                    <div key={p.id} className={styles.packageCard}>
                      <h4>{p.packageName}</h4>
                      <span className={styles.serviceType}>{p.serviceType}</span>
                      <span className={styles.duration}>{p.durationDays} gün</span>
                      {p.price != null && (
                        <span className={styles.price}>₺{p.price.toLocaleString('tr-TR')}</span>
                      )}
                      <Button
                        variant="primary"
                        disabled={alreadyOwned || purchasingId === p.id}
                        onClick={() => handlePurchase(p.id)}
                      >
                        {alreadyOwned ? 'Zaten abonesiniz' : purchasingId === p.id ? 'İşleniyor...' : 'Satın Al'}
                      </Button>
                    </div>
                  );
                })}
              </div>
              {availableSubscriptions.length === 0 && (
                <p className={styles.emptyHint}>Henüz abonelik paketi bulunmuyor.</p>
              )}
            </div>
          </>
        )}
      </div>
    </Layout>
  );
}
