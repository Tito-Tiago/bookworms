import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { BookOpenIcon, UserIcon, LockIcon } from 'lucide-react';

const Login: React.FC = () => {
  const [login, setLogin] = useState('');
  const [senha, setSenha] = useState('');
  const [erro, setErro] = useState('');
  const [carregando, setCarregando] = useState(false);
  const auth = useAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErro('');

    if (!login || !senha) {
      setErro('Todos os campos são obrigatórios.');
      return;
    }

    setCarregando(true);
    try {
      await auth.login(login, senha);
      
    } catch (error: any) {
      setErro(error.message || 'Usuário ou senha incorretos.');
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
        <p className="text-gray-600">Biblioteca EEMTI Adelino Cunha Alcântara</p>
      </div>
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-semibold mb-6 text-center">Entrar</h2>
        {erro && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
            {erro}
          </div>
        )}
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-gray-700 mb-2" htmlFor="login">
              Nome de usuário ou E-mail
            </label>
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <UserIcon className="h-5 w-5 text-gray-400" />
              </div>
              <input
                id="login"
                type="text"
                value={login}
                onChange={(e) => setLogin(e.target.value)}
                className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Digite seu usuário ou e-mail"
              />
            </div>
          </div>
          <div className="mb-6">
            <label className="block text-gray-700 mb-2" htmlFor="senha">
              Senha
            </label>
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <LockIcon className="h-5 w-5 text-gray-400" />
              </div>
              <input
                id="senha"
                type="password"
                value={senha}
                onChange={(e) => setSenha(e.target.value)}
                className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Digite sua senha"
              />
            </div>
          </div>
          <button
            type="submit"
            className="w-full bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors disabled:bg-blue-300"
            disabled={carregando}
          >
            {carregando ? 'Entrando...' : 'Entrar'}
          </button>
        </form>
        <div className="mt-6 text-center">
          <p className="text-gray-600">
            Não tem uma conta?{' '}
            <Link to="/register" className="text-blue-600 hover:underline">
              Cadastre-se
            </Link>
          </p>
        </div>
        <div className="mt-4 text-center">
          <Link to="/admin-login" className="text-blue-600 hover:underline text-sm">
            Acesso para Bibliotecários
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Login;