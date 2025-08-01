import React, { useEffect, useState, createContext, useContext } from 'react';
import { apiService } from '../services/apiService';

interface Usuario {
  id: string;
  nomeCompleto: string;
  username: string;
  email: string;
  fotoPerfilUrl: string;
  isAdmin?: boolean;
}

interface AuthContextType {
  usuarioAtual: Usuario | null;
  ehAdmin: boolean;
  token: string | null;
  loading: boolean;
  login: (login: string, senha: string) => Promise<void>;
  adminLogin: (username: string, password: string) => Promise<void>;
  register: (nomeCompleto: string, username: string, email: string, senha: string) => Promise<void>;
  logout: () => void;
  updateProfile: (data: Partial<Omit<Usuario, 'id' | 'fotoPerfilUrl'>>) => Promise<void>;
  deleteAccount: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth deve ser usado dentro de um AuthProvider');
  }
  return context;
};

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [usuarioAtual, setUsuarioAtual] = useState<Usuario | null>(null);
  const [ehAdmin, setEhAdmin] = useState<boolean>(false);
  const [token, setToken] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const storedUser = localStorage.getItem('usuario');
    const storedToken = localStorage.getItem('token');
    if (storedUser && storedToken) {
      const usuario = JSON.parse(storedUser);
      setUsuarioAtual(usuario);
      setToken(storedToken);
      setEhAdmin(!!usuario.isAdmin);
    }
    setLoading(false);
  }, []);

  const login = async (login: string, senha: string): Promise<void> => {
    const responseData = await apiService.loginAluno(login, senha);
    const { token: novoToken, aluno } = responseData.data;
    setToken(novoToken);
    setUsuarioAtual(aluno);
    setEhAdmin(false);
    localStorage.setItem('token', novoToken);
    localStorage.setItem('usuario', JSON.stringify(aluno));
  };

  const adminLogin = async (username: string, password: string): Promise<void> => {
    const responseData = await apiService.loginBibliotecario(username, password);
    const { token: novoToken } = responseData.data;
    const adminUser: Usuario = {
      id: 'admin', nomeCompleto: 'Bibliotecário', username: 'admin',
      email: 'admin@biblioteca.edu', fotoPerfilUrl: 'https://i.pravatar.cc/300?img=10', isAdmin: true,
    };
    setToken(novoToken);
    setUsuarioAtual(adminUser);
    setEhAdmin(true);
    localStorage.setItem('token', novoToken);
    localStorage.setItem('usuario', JSON.stringify(adminUser));
  }

  const register = async (nomeCompleto: string, username: string, email: string, senha: string): Promise<void> => {
    await apiService.registerAluno(nomeCompleto, username, email, senha);
  };

  const logout = () => {
    setUsuarioAtual(null);
    setEhAdmin(false);
    setToken(null);
    localStorage.removeItem('usuario');
    localStorage.removeItem('token');
  };

  const updateProfile = async (data: Partial<Omit<Usuario, 'id' | 'fotoPerfilUrl'>>): Promise<void> => {
    if (!usuarioAtual || !token) throw new Error("Utilizador não autenticado.");

    const utilizadorAtualizado = await apiService.updateAluno(usuarioAtual.id, data);
    setUsuarioAtual(utilizadorAtualizado);
    localStorage.setItem('usuario', JSON.stringify(utilizadorAtualizado));
  };

  const deleteAccount = async (): Promise<void> => {
    if (!usuarioAtual || !token) throw new Error("Utilizador não autenticado.");

    await apiService.deleteAluno(usuarioAtual.id);
    logout();
  };

  const value = { usuarioAtual, ehAdmin, token, loading, login, adminLogin, register, logout, updateProfile, deleteAccount };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};