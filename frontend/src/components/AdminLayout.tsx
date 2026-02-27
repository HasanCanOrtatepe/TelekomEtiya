import { Link, NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import styles from './AdminLayout.module.css';

export function AdminLayout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const isAdmin = user?.authorities?.includes('ROLE_ADMIN') ?? false;

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <div className={styles.wrapper}>
      <header className={styles.header}>
        <Link to="/admin" className={styles.logo}>
          <img src="/etiya-telekom-logo.png" alt="Etiya Telekom" className={styles.logoImg} />
        </Link>
        <div className={styles.headerRight}>
          <span className={styles.userEmail}>{user?.email}</span>
          <Link to="/" className={styles.navLink}>Ana Sayfa</Link>
          <button type="button" onClick={handleLogout} className={styles.navLinkBtn}>Çıkış</button>
        </div>
      </header>

      <div className={styles.body}>
        <aside className={styles.sidebar}>
          <h2 className={styles.sidebarTitle}>{isAdmin ? 'Admin Paneli' : 'Agent Paneli'}</h2>
          <nav className={styles.nav}>
            <NavLink to="/admin" end className={({ isActive }) => isActive ? styles.navItemActive : styles.navItem}>
              Özet
            </NavLink>
            <NavLink to="/admin/tickets" className={({ isActive }) => isActive ? styles.navItemActive : styles.navItem}>
              Talepler
            </NavLink>
            {isAdmin && (
              <>
                <NavLink to="/admin/departments" className={({ isActive }) => isActive ? styles.navItemActive : styles.navItem}>
                  Departmanlar
                </NavLink>
                <NavLink to="/admin/service-domains" className={({ isActive }) => isActive ? styles.navItemActive : styles.navItem}>
                  Servis Alanları
                </NavLink>
              </>
            )}
            <NavLink to="/admin/subscriptions" className={({ isActive }) => isActive ? styles.navItemActive : styles.navItem}>
              Abonelikler
            </NavLink>
            {isAdmin && (
              <>
                <NavLink to="/admin/agents" className={({ isActive }) => isActive ? styles.navItemActive : styles.navItem}>
                  Agentler
                </NavLink>
                <NavLink to="/admin/customers" className={({ isActive }) => isActive ? styles.navItemActive : styles.navItem}>
                  Müşteriler
                </NavLink>
              </>
            )}
          </nav>
        </aside>

        <main className={styles.main}>
          <Outlet />
        </main>
      </div>
    </div>
  );
}
