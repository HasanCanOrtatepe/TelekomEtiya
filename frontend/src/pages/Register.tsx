import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Layout } from '../components/Layout';
import { Button } from '../components/Button';
import { Input } from '../components/Input';
import { api } from '../api/client';
import styles from './Auth.module.css';

export function Register() {
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    password: '',
    confirmPassword: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm((f) => ({ ...f, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    if (form.password !== form.confirmPassword) {
      setError('Şifreler eşleşmiyor');
      return;
    }
    setLoading(true);
    try {
      await api.post('/auth/register', {
        firstName: form.firstName,
        lastName: form.lastName,
        email: form.email,
        phone: form.phone || undefined,
        password: form.password,
        confirmPassword: form.confirmPassword,
      });
      navigate('/login');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Kayıt başarısız');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Layout>
      <div className={styles.container}>
        <div className={styles.card}>
          <h1 className={styles.title}>Kayıt Ol</h1>
          <form onSubmit={handleSubmit}>
            <div className={styles.row}>
              <Input
                label="Ad"
                name="firstName"
                value={form.firstName}
                onChange={handleChange}
                required
              />
              <Input
                label="Soyad"
                name="lastName"
                value={form.lastName}
                onChange={handleChange}
                required
              />
            </div>
            <Input
              label="E-posta"
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              required
            />
            <Input
              label="Telefon (isteğe bağlı)"
              type="tel"
              name="phone"
              value={form.phone}
              onChange={handleChange}
              placeholder="+90 5XX XXX XX XX"
            />
            <Input
              label="Şifre"
              type="password"
              name="password"
              value={form.password}
              onChange={handleChange}
              required
              minLength={8}
            />
            <Input
              label="Şifre Tekrar"
              type="password"
              name="confirmPassword"
              value={form.confirmPassword}
              onChange={handleChange}
              required
            />
            {error && <p className={styles.error}>{error}</p>}
            <Button type="submit" fullWidth disabled={loading}>
              {loading ? 'Kaydediliyor...' : 'Kayıt Ol'}
            </Button>
          </form>
          <p className={styles.footer}>
            Zaten hesabınız var mı? <Link to="/login">Giriş yapın</Link>
          </p>
        </div>
      </div>
    </Layout>
  );
}
