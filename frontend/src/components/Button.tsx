import styles from './Button.module.css';

type Variant = 'primary' | 'secondary' | 'outline';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: Variant;
  fullWidth?: boolean;
}

export function Button({
  variant = 'primary',
  fullWidth = false,
  className = '',
  ...props
}: ButtonProps) {
  return (
    <button
      className={`${styles.btn} ${styles[variant]} ${fullWidth ? styles.fullWidth : ''} ${className}`}
      {...props}
    />
  );
}
