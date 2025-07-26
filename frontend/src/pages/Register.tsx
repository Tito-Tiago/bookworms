import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { BookOpenIcon, UserIcon, MailIcon, LockIcon } from 'lucide-react';

interface ErrosDeFormulario {
  form?: string;
  nomeCompleto?: string;
  username?: string;
  email?: string;
  senha?: string;
  confirmarSenha?: string;
}

const Register: React.FC = () => {
  const [nomeCompleto, setNomeCompleto] = useState('');
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [confirmarSenha, setConfirmarSenha] = useState('');
  const [erros, setErros] = useState<ErrosDeFormulario>({});
  const [carregando, setCarregando] = useState(false);
  const auth = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErros({});

    if (senha !== confirmarSenha) {
      setErros({ confirmarSenha: 'As senhas não coincidem.' });
      return;
    }

    setCarregando(true);
    try {
      await auth.register(nomeCompleto, username, email, senha);

      alert('Cadastro realizado com sucesso! Por favor, faça o login.');
      navigate('/login');

    } catch (error: any) {
      if (error.errors) {
        setErros(error.errors);
      } else {
        setErros({ form: error.message || 'Não foi possível realizar o cadastro.' });
      }
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
        <p className="text-gray-600">
          Biblioteca EEMTI Adelino Cunha Alcântara
        </p>
      </div>
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-semibold mb-6 text-center">Cadastre-se</h2>

        {erros.form && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
            {erros.form}
          </div>
        )}

        <form onSubmit={handleSubmit} noValidate>
          <div className="mb-4">
            <label className="block text-gray-700 mb-2" htmlFor="nomeCompleto">Nome Completo</label>
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <UserIcon className="h-5 w-5 text-gray-400" />
              </div>
              <input id="nomeCompleto" type="text" value={nomeCompleto} onChange={e => setNomeCompleto(e.target.value)} className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Digite seu nome completo" />
            </div>
            {erros.nomeCompleto && <p className="text-red-500 text-sm mt-1">{erros.nomeCompleto}</p>}
          </div>

          <div className="mb-4">
            <label className="block text-gray-700 mb-2" htmlFor="username">Nome de Usuário</label>
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <UserIcon className="h-5 w-5 text-gray-400" />
              </div>
              <input id="username" type="text" value={username} onChange={e => setUsername(e.target.value)} className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Escolha um nome de usuário" />
            </div>
            {erros.username && <p className="text-red-500 text-sm mt-1">{erros.username}</p>}
          </div>

          <div className="mb-4">
            <label className="block text-gray-700 mb-2" htmlFor="email">E-mail</label>
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <MailIcon className="h-5 w-5 text-gray-400" />
              </div>
              <input id="email" type="email" value={email} onChange={e => setEmail(e.target.value)} className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Digite seu e-mail" />
            </div>
            {erros.email && <p className="text-red-500 text-sm mt-1">{erros.email}</p>}
          </div>

          <div className="mb-4">
            <label className="block text-gray-700 mb-2" htmlFor="senha">Senha</label>
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <LockIcon className="h-5 w-5 text-gray-400" />
              </div>
              <input id="senha" type="password" value={senha} onChange={e => setSenha(e.target.value)} className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Crie uma senha" />
            </div>
            {erros.senha && <p className="text-red-500 text-sm mt-1">{erros.senha}</p>}
          </div>

          <div className="mb-6">
            <label className="block text-gray-700 mb-2" htmlFor="confirmarSenha">Confirmar Senha</label>
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <LockIcon className="h-5 w-5 text-gray-400" />
              </div>
              <input id="confirmarSenha" type="password" value={confirmarSenha} onChange={e => setConfirmarSenha(e.target.value)} className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Confirme sua senha" />
            </div>
            {erros.confirmarSenha && <p className="text-red-500 text-sm mt-1">{erros.confirmarSenha}</p>}
          </div>

          <button type="submit" className="w-full bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors disabled:bg-blue-300" disabled={carregando}>
            {carregando ? 'Cadastrando...' : 'Cadastrar'}
          </button>
        </form>
        <div className="mt-6 text-center">
          <p className="text-gray-600">
            Já tem uma conta?{' '}
            <Link to="/login" className="text-blue-600 hover:underline">
              Entrar
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;