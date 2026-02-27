import { useState, useCallback, useRef } from 'react';
import styles from './Carousel.module.css';

interface CarouselProps {
  images: { src: string; alt: string }[];
  className?: string;
}

export function Carousel({ images, className = '' }: CarouselProps) {
  const [current, setCurrent] = useState(0);
  const touchStart = useRef<number>(0);
  const touchEnd = useRef<number>(0);

  const goTo = useCallback((index: number) => {
    setCurrent(((index % images.length) + images.length) % images.length);
  }, [images.length]);

  const goPrev = useCallback(() => goTo(current - 1), [current, goTo]);
  const goNext = useCallback(() => goTo(current + 1), [current, goTo]);

  const handleTouchStart = useCallback((e: React.TouchEvent) => {
    touchStart.current = e.targetTouches[0].clientX;
  }, []);

  const handleTouchMove = useCallback((e: React.TouchEvent) => {
    touchEnd.current = e.targetTouches[0].clientX;
  }, []);

  const handleTouchEnd = useCallback(() => {
    const diff = touchStart.current - touchEnd.current;
    const threshold = 50;
    if (diff > threshold) goNext();
    else if (diff < -threshold) goPrev();
  }, [goNext, goPrev]);

  const handleMouseDown = useCallback((e: React.MouseEvent) => {
    touchStart.current = e.clientX;
  }, []);

  const handleMouseUp = useCallback((e: React.MouseEvent) => {
    touchEnd.current = e.clientX;
    const diff = touchStart.current - touchEnd.current;
    const threshold = 50;
    if (diff > threshold) goNext();
    else if (diff < -threshold) goPrev();
  }, [goNext, goPrev]);

  if (images.length === 0) return null;

  return (
    <div className={`${styles.carousel} ${className}`}>
      <div
        className={styles.track}
        style={{ transform: `translateX(-${current * 100}%)` }}
        onTouchStart={handleTouchStart}
        onTouchMove={handleTouchMove}
        onTouchEnd={handleTouchEnd}
        onMouseDown={handleMouseDown}
        onMouseUp={handleMouseUp}
      >
        {images.map((img, i) => (
          <div key={i} className={styles.slide}>
            <img src={img.src} alt={img.alt} />
          </div>
        ))}
      </div>
      <button type="button" className={styles.arrow} aria-label="Önceki" onClick={goPrev}>
        ‹
      </button>
      <button type="button" className={`${styles.arrow} ${styles.arrowRight}`} aria-label="Sonraki" onClick={goNext}>
        ›
      </button>
      <div className={styles.dots}>
        {images.map((_, i) => (
          <button
            key={i}
            type="button"
            className={`${styles.dot} ${i === current ? styles.dotActive : ''}`}
            aria-label={`Slayt ${i + 1}`}
            onClick={() => goTo(i)}
          />
        ))}
      </div>
    </div>
  );
}
