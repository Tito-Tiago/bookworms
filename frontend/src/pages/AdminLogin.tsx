import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { BookOpenIcon, UserIcon, LockIcon } from 'lucide-react';

const AdminLogin: React.FC = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [erro, setErro] = useState('');
  const [carregando, setCarregando] = useState(false);
  const { adminLogin } = useAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErro('');
    if (!username || !password) {
      setErro('Todos os campos são obrigatórios.');
      return;
    }

    setCarregando(true);
    try {
      await adminLogin(username, password);
      
    } catch (error: any) {
      setErro(error.message || 'Credenciais inválidas para acesso de bibliotecário.');
    } finally {
      setCarregando(false);
    }
  };

  return (
    <div className="max-w-md mx-auto">
      <div className="text-center mb-8">
        <div className="flex justify-center mb-2">
          <BookOpenIcon className="h-12 w-12 text-blue-600" />
        </div>
        <h1 className="text-3xl font-bold text-gray-800">Bookworms</h1>
        <p className="text-gray-600">Acesso para Bibliotecários</p>
      </div>
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-semibold mb-6 text-center">
          Login Administrativo
        </h2>
        {erro && <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
            {erro}
          </div>}
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-gray-700 mb-2" htmlFor="username">
              Nome de Usuário
            </label>
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <UserIcon className="h-5 w-5 text-gray-400" />
              </div>
              <input id="username" type="text" value={username} onChange={e => setUsername(e.target.value)} className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Digite o usuário administrativo" />
            </div>
          </div>
          <div className="mb-6">
            <label className="block text-gray-700 mb-2" htmlFor="password">
              Senha
            </label>
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <LockIcon className="h-5 w-5 text-gray-400" />
              </div>
              <input id="password" type="password" value={password} onChange={e => setPassword(e.target.value)} className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Digite a senha administrativa" />
            </div>
          </div>
          <button type="submit" className="w-full bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors" disabled={carregando}>
            {carregando ? 'Entrando...' : 'Entrar como Bibliotecário'}
          </button>
        </form>
        <div className="mt-6 text-center">
          <Link to="/login" className="text-blue-600 hover:underline">
            Voltar para login de aluno
          </Link>
        </div>
      </div>
    </div>
  );
};

export default AdminLogin;