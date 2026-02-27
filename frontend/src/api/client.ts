const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

function getToken(): string | null {
  return localStorage.getItem('token');
}

export async function apiFetch<T>(
  endpoint: string,
  options: RequestInit = {}
): Promise<T> {
  const token = getToken();
  const headers: HeadersInit = {
    'Content-Type': 'application/json',
    ...options.headers,
  };
  if (token) {
    (headers as Record<string, string>)['Authorization'] = `Bearer ${token}`;
  }

  const res = await fetch(`${API_BASE}${endpoint}`, { ...options, headers });

  if (!res.ok) {
    const data = await res.json().catch(() => ({}));
    const message = data.message || data.error || `HTTP ${res.status}`;
    throw new Error(Array.isArray(message) ? message.join(', ') : message);
  }

  const contentType = res.headers.get('content-type');
  const hasNoBody = res.status === 204 || res.headers.get('content-length') === '0';
  if (hasNoBody || !contentType?.includes('application/json')) {
    await res.text().catch(() => '');
    return {} as T;
  }
  const data = await res.json().catch(() => ({}));
  return data as T;
}

export const api = {
  post: <T>(endpoint: string, body: unknown) =>
    apiFetch<T>(endpoint, { method: 'POST', body: JSON.stringify(body) }),
  get: <T>(endpoint: string) => apiFetch<T>(endpoint, { method: 'GET' }),
  put: <T>(endpoint: string, body: unknown) =>
    apiFetch<T>(endpoint, { method: 'PUT', body: JSON.stringify(body) }),
  patch: <T>(endpoint: string, body?: unknown) =>
    apiFetch<T>(endpoint, { method: 'PATCH', body: body ? JSON.stringify(body) : undefined }),
  delete: <T>(endpoint: string) => apiFetch<T>(endpoint, { method: 'DELETE' }),
};
