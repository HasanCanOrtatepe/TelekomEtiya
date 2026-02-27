import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../../api/client';
import styles from './AdminShared.module.css';

export function AdminDashboard() {
  const [stats, setStats] = useState({ tickets: 0, departments: 0, agents: 0, customers: 0 });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      api.get<{ count?: number; items?: unknown[] }>('/tickets'),
      api.get<{ count?: number; items?: unknown[] }>('/departments'),
      api.get<{ count?: number; agents?: unknown[] }>('/agents'),
      api.get<{ count?: number; customerResponses?: unknown[] }>('/customers'),
    ])
      .then(([t, d, a, c]) => {
        setStats({
          tickets: t.items?.length ?? t.count ?? 0,
          departments: d.items?.length ?? d.count ?? 0,
          agents: a.agents?.length ?? a.count ?? 0,
          customers: c.customerResponses?.length ?? c.count ?? 0,
        });
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p>Yükleniyor...</p>;

  return (
    <div className={styles.page}>
      <h1>Yönetim Özeti</h1>
      <div className={styles.statsGrid}>
        <Link to="/admin/tickets" className={styles.statCard}>
          <span className={styles.statNumber}>{stats.tickets}</span>
          <span className={styles.statLabel}>Talep</span>
        </Link>
        <Link to="/admin/departments" className={styles.statCard}>
          <span className={styles.statNumber}>{stats.departments}</span>
          <span className={styles.statLabel}>Departman</span>
        </Link>
        <Link to="/admin/agents" className={styles.statCard}>
          <span className={styles.statNumber}>{stats.agents}</span>
          <span className={styles.statLabel}>Agent</span>
        </Link>
        <Link to="/admin/customers" className={styles.statCard}>
          <span className={styles.statNumber}>{stats.customers}</span>
          <span className={styles.statLabel}>Müşteri</span>
        </Link>
      </div>
    </div>
  );
}
