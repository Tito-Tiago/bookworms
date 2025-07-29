import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import { LivroProvider } from './contexts/LivroContext';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Register from './pages/Register';
import Profile from './pages/Profile';
import AdminLogin from './pages/AdminLogin';
import PainelAdministrativo from './pages/AdminPanel';
import Inicio from './pages/Home';
import DetalheLivro from './pages/LivroDetail';
import ProtectedRoute from './components/ProtectedRoute';
import AdminRoute from './components/AdminRoute';

const AppRoutes: React.FC = () => {
  const { usuarioAtual, ehAdmin, loading } = useAuth();

  if (loading) {
    return <div className="text-center py-12">A carregar...</div>;
  }

  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" />} />
      <Route path="/login" element={usuarioAtual && !ehAdmin ? <Navigate to="/home" /> : <Login />} />
      <Route path="/admin-login" element={usuarioAtual && ehAdmin ? <Navigate to="/admin" /> : <AdminLogin />} />
      <Route path="/register" element={<Register />} />
      <Route path="/home" element={<ProtectedRoute><Inicio /></ProtectedRoute>} />
      <Route path="/livro/:id" element={<ProtectedRoute><DetalheLivro /></ProtectedRoute>} />
      <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
      <Route path="/admin" element={<AdminRoute><PainelAdministrativo /></AdminRoute>} />
    </Routes>
  );
};

export function App() {
  return (
    <Router>
      <AuthProvider>
        <LivroProvider>
          <div className="min-h-screen bg-gray-50">
            <Navbar />
            <main className="container mx-auto px-4 py-8">
              <AppRoutes />
            </main>
          </div>
        </LivroProvider>
      </AuthProvider>
    </Router>
  );
}