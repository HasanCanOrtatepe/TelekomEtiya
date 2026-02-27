import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import styles from './Layout.module.css';

export function Layout({ children }: { children: React.ReactNode }) {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <div className={styles.wrapper}>
      <header className={styles.header}>
        <Link to="/" className={styles.logo}>
          <img src="/etiya-telekom-logo.png" alt="Etiya Telekom" className={styles.logoImg} />
        </Link>
        <nav className={styles.nav}>
          {!isAuthenticated ? (
            <>
              <Link to="/login" className={styles.navLink}>Giriş Yap</Link>
              <Link to="/register" className={styles.navCta}>Kayıt Ol</Link>
            </>
          ) : (
            <>
              {user?.userType === 'CUSTOMER' && (
                <>
                  <Link to="/dashboard" className={styles.navLink}>Şikayetlerim</Link>
                  <Link to="/subscriptions" className={styles.navLink}>Aboneliklerim</Link>
                </>
              )}
              {(user?.authorities?.some((a) => ['ROLE_ADMIN', 'ROLE_SUPERVISOR', 'ROLE_SENIOR_AGENT', 'ROLE_AGENT'].includes(a))) && (
                <Link to="/admin" className={styles.navLink}>Agent Paneli</Link>
              )}
              <span className={styles.userEmail}>{user?.email}</span>
              <button type="button" onClick={handleLogout} className={styles.navLinkBtn}>
                Çıkış
              </button>
            </>
          )}
        </nav>
      </header>
      <main className={styles.main}>{children}</main>
      <footer className={styles.footer}>
        <p>© {new Date().getFullYear()} Etiya Telekom. Tüm hakları saklıdır.</p>
      </footer>
    </div>
  );
}
