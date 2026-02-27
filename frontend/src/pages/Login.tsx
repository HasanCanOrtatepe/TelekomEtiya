import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Layout } from '../components/Layout';
import { Button } from '../components/Button';
import { Input } from '../components/Input';
import { api } from '../api/client';
import { useAuth } from '../context/AuthContext';
import type { UserType } from '../context/AuthContext';
import styles from './Auth.module.css';

export function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const auth = localStorage.getItem('etiya_auth');
    if (auth) {
      try {
        const u = JSON.parse(auth);
        const authorities = u?.authorities ?? [];
        const isAgent = authorities.some((a: string) =>
          ['ROLE_ADMIN', 'ROLE_SUPERVISOR', 'ROLE_SENIOR_AGENT', 'ROLE_AGENT'].includes(a)
        );
        if (isAgent) {
          navigate('/admin', { replace: true });
        } else if (u?.userType === 'CUSTOMER') {
          navigate('/dashboard', { replace: true });
        }
      } catch { /* ignore */ }
    }
  }, [navigate]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = await api.post<{
        accessToken: string;
        userId: number;
        email: string;
        userType: UserType;
        authorities: string[];
      }>('/auth/login', { email, password });
      login(
        res.accessToken,
        res.userId,
        res.email,
        res.userType,
        res.authorities || []
      );
      const isAgent = (res.authorities ?? []).some((a: string) =>
        ['ROLE_ADMIN', 'ROLE_SUPERVISOR', 'ROLE_SENIOR_AGENT', 'ROLE_AGENT'].includes(a)
      );
      if (isAgent) {
        navigate('/admin');
      } else if (res.userType === 'CUSTOMER') {
        navigate('/dashboard');
      } else {
        navigate('/');
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Giriş başarısız');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Layout>
      <div className={styles.container}>
        <div className={styles.card}>
          <h1 className={styles.title}>Giriş Yap</h1>
          <form onSubmit={handleSubmit}>
            <Input
              label="E-posta"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="ornek@email.com"
              required
            />
            <Input
              label="Şifre"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
            {error && <p className={styles.error}>{error}</p>}
            <Button type="submit" fullWidth disabled={loading}>
              {loading ? 'Giriş yapılıyor...' : 'Giriş Yap'}
            </Button>
          </form>
          <p className={styles.footer}>
            Hesabınız yok mu? <Link to="/register">Kayıt olun</Link>
          </p>
        </div>
      </div>
    </Layout>
  );
}
