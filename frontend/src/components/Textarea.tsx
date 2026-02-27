import styles from './Input.module.css';

interface TextareaProps extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
  label?: string;
  error?: string;
}

export function Textarea({ label, error, id, ...props }: TextareaProps) {
  const inputId = id || label?.toLowerCase().replace(/\s/g, '-');
  return (
    <div className={styles.field}>
      {label && (
        <label htmlFor={inputId} className={styles.label}>
          {label}
        </label>
      )}
      <textarea id={inputId} className={`${styles.input} ${styles.textarea}`} {...props} />
      {error && <p className={styles.error}>{error}</p>}
    </div>
  );
}
