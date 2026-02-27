import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { AdminLayout } from './components/AdminLayout';
import { Landing } from './pages/Landing';
import { Login } from './pages/Login';
import { Register } from './pages/Register';
import { Dashboard } from './pages/Dashboard';
import { MySubscriptions } from './pages/MySubscriptions';
import { AdminDashboard } from './pages/admin/AdminDashboard';
import { AdminTickets } from './pages/admin/AdminTickets';
import { AdminTicketDetail } from './pages/admin/AdminTicketDetail';
import { AdminDepartments } from './pages/admin/AdminDepartments';
import { AdminServiceDomains } from './pages/admin/AdminServiceDomains';
import { AdminSubscriptions } from './pages/admin/AdminSubscriptions';
import { AdminAgents } from './pages/admin/AdminAgents';
import { AdminCustomers } from './pages/admin/AdminCustomers';

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const token = localStorage.getItem('token');
  if (!token) return <Navigate to="/login" replace />;
  return <>{children}</>;
}

function AdminRoute({ children }: { children: React.ReactNode }) {
  const token = localStorage.getItem('token');
  const auth = localStorage.getItem('etiya_auth');
  if (!token) return <Navigate to="/login" replace />;
  let canAccessAdmin = false;
  try {
    const u = auth ? JSON.parse(auth) : null;
    const authorities = u?.authorities ?? [];
    canAccessAdmin = authorities.some((a: string) =>
      ['ROLE_ADMIN', 'ROLE_SUPERVISOR', 'ROLE_SENIOR_AGENT', 'ROLE_AGENT'].includes(a)
    );
  } catch { /* ignore */ }
  if (!canAccessAdmin) return <Navigate to="/" replace />;
  return <>{children}</>;
}

function AdminOnlyRoute({ children }: { children: React.ReactNode }) {
  const auth = localStorage.getItem('etiya_auth');
  const isAdmin = auth ? (() => { try { const u = JSON.parse(auth); return u?.authorities?.includes('ROLE_ADMIN') ?? false; } catch { return false; } })() : false;
  if (!isAdmin) return <Navigate to="/admin" replace />;
  return <>{children}</>;
}

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Landing />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
          <Route path="/subscriptions" element={<ProtectedRoute><MySubscriptions /></ProtectedRoute>} />
          <Route path="/admin" element={<AdminRoute><AdminLayout /></AdminRoute>}>
            <Route index element={<AdminDashboard />} />
            <Route path="tickets" element={<AdminTickets />} />
            <Route path="tickets/:id" element={<AdminTicketDetail />} />
            <Route path="departments" element={<AdminOnlyRoute><AdminDepartments /></AdminOnlyRoute>} />
            <Route path="service-domains" element={<AdminOnlyRoute><AdminServiceDomains /></AdminOnlyRoute>} />
            <Route path="subscriptions" element={<AdminSubscriptions />} />
            <Route path="agents" element={<AdminOnlyRoute><AdminAgents /></AdminOnlyRoute>} />
            <Route path="customers" element={<AdminOnlyRoute><AdminCustomers /></AdminOnlyRoute>} />
          </Route>
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
