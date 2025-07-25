import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

interface AdminRouteProps {
  children: React.ReactNode;
}

const AdminRoute: React.FC<AdminRouteProps> = ({ children }) => {
  const { usuarioAtual, ehAdmin } = useAuth();

  if (!usuarioAtual || !ehAdmin) {
    return <Navigate to="/admin-login" />;
  }

  return <>{children}</>;
};

export default AdminRoute;