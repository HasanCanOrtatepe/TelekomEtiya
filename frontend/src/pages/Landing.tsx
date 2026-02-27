import { Link } from 'react-router-dom';
import { Layout } from '../components/Layout';
import { Button } from '../components/Button';
import { Carousel } from '../components/Carousel';
import { useAuth } from '../context/AuthContext';
import styles from './Landing.module.css';

const CAROUSEL_IMAGES = [
  { src: '/etiya-hero.png', alt: 'Etiya Telekom' },
  { src: '/carousel-2.png', alt: 'Aylık 20 Gb İnternet' },
  { src: '/carousel-3.png', alt: 'Sınırsız Maç Keyfi' },
  { src: '/carousel-4.png', alt: 'Haftalık 10 Gb İnternet' },
  { src: '/carousel-5.png', alt: 'Yıllık 240 Gb İnternet' },
];

export function Landing() {
  const { isAuthenticated } = useAuth();

  return (
    <Layout>
      <section className={styles.hero}>
        <div className={styles.heroContent}>
          <h1 className={styles.heroTitle}>
            İletişimin Geleceği
            <span className={styles.accent}> Burada</span>
          </h1>
          <p className={styles.heroSubtitle}>
            Etiya Telekom ile kesintisiz bağlantı ve hızlı destek. Şikayetlerinizi 7/24 gönderin,
            yapay zeka destekli sistemimiz anında değerlendirir.
          </p>
          {!isAuthenticated && (
            <div className={styles.heroActions}>
              <Link to="/register">
                <Button variant="primary" className={styles.ctaBtn}>
                  Ücretsiz Kayıt Ol
                </Button>
              </Link>
              <Link to="/login">
                <Button variant="outline" className={styles.ctaBtn}>
                  Giriş Yap
                </Button>
              </Link>
            </div>
          )}
        </div>
        <div className={styles.heroVisual}>
          <Carousel images={CAROUSEL_IMAGES} />
        </div>
      </section>

      <section className={styles.features}>
        <h2 className={styles.sectionTitle}>Neden Etiya Telekom?</h2>
        <div className={styles.featureGrid}>
          <div className={styles.featureCard}>
            <div className={styles.featureIcon}>⚡</div>
            <h3>Hızlı Analiz</h3>
            <p>Yapay zeka destekli şikayet analizi ile anında yönlendirme</p>
          </div>
          <div className={styles.featureCard}>
            <div className={styles.featureIcon}>🛡️</div>
            <h3>Güvenilir Destek</h3>
            <p>Uzman ekibimiz sorunlarınıza en kısa sürede çözüm üretir</p>
          </div>
          <div className={styles.featureCard}>
            <div className={styles.featureIcon}>📱</div>
            <h3>Her Yerden Erişim</h3>
            <p>Şikayetlerinizi web üzerinden kolayca takip edin</p>
          </div>
        </div>
      </section>
    </Layout>
  );
}
