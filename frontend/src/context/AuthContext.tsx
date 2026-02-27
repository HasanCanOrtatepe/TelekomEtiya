import {
  createContext,
  useCallback,
  useContext,
  useState,
  useEffect,
  type ReactNode,
} from 'react';

export type UserType = 'CUSTOMER' | 'ADMIN' | 'SUPERVISOR' | 'SENIOR_AGENT' | 'AGENT';

export interface AuthUser {
  token: string;
  userId: number;
  email: string;
  userType: UserType;
  authorities: string[];
}

const AuthContext = createContext<{
  user: AuthUser | null;
  login: (token: string, userId: number, email: string, userType: UserType, authorities: string[]) => void;
  logout: () => void;
  isAuthenticated: boolean;
} | null>(null);

const STORAGE_KEY = 'etiya_auth';

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(() => {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as AuthUser;
    } catch {
      return null;
    }
  });

  const login = useCallback(
    (token: string, userId: number, email: string, userType: UserType, authorities: string[]) => {
      const u: AuthUser = { token, userId, email, userType, authorities };
      setUser(u);
      localStorage.setItem('token', token);
      localStorage.setItem(STORAGE_KEY, JSON.stringify(u));
    },
    []
  );

  const logout = useCallback(() => {
    setUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem(STORAGE_KEY);
  }, []);

  useEffect(() => {
    if (user) {
      localStorage.setItem('token', user.token);
    }
  }, [user]);

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        logout,
        isAuthenticated: !!user,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
