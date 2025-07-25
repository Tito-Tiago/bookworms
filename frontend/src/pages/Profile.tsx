import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { UserIcon, MailIcon, EditIcon, SaveIcon, TrashIcon, BookOpenIcon, LockIcon } from 'lucide-react';

interface ErrosDeFormulario {
  form?: string;
  nomeCompleto?: string;
  username?: string;
  email?: string;
  senha?: string;
  confirmarSenha?: string;
}

const Profile: React.FC = () => {
  const { usuarioAtual, ehAdmin, updateProfile, deleteAccount } = useAuth();

  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    nomeCompleto: '',
    username: '',
    email: '',
    senha: '',
    confirmarSenha: ''
  });
  const [erros, setErros] = useState<ErrosDeFormulario>({});
  const [carregando, setCarregando] = useState(false);

  useEffect(() => {
    if (usuarioAtual) {
      setFormData({
        nomeCompleto: usuarioAtual.nomeCompleto || '',
        username: usuarioAtual.username || '',
        email: usuarioAtual.email || '',
        senha: '',
        confirmarSenha: ''
      });
    }
  }, [usuarioAtual]);

  if (!usuarioAtual) {
    return null;
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErros({});

    if (!ehAdmin && formData.senha && formData.senha !== formData.confirmarSenha) {
      setErros({ confirmarSenha: 'As senhas não coincidem.' });
      return;
    }

    setCarregando(true);
    try {
      const dadosParaAtualizar: any = {
        nomeCompleto: formData.nomeCompleto,
        email: formData.email,
      };

      if (!ehAdmin) {
        dadosParaAtualizar.username = formData.username;
        if (formData.senha) {
          dadosParaAtualizar.senha = formData.senha;
        }
      }

      await updateProfile(dadosParaAtualizar);
      setIsEditing(false);
      alert('Perfil atualizado com sucesso!');
    } catch (error: any) {
      if (error.errors) {
        setErros(error.errors);
      } else {
        setErros({ form: error.message || 'Não foi possível atualizar o perfil.' });
      }
    } finally {
      setCarregando(false);
    }
  };

  const handleDeleteAccount = async () => {
    if (window.confirm('Tem certeza que deseja excluir sua conta? Esta ação não pode ser desfeita.')) {
      try {
        await deleteAccount();
        alert('Conta excluída com sucesso.');
      } catch (error: any) {
        alert(error.message || 'Não foi possível excluir a conta.');
      }
    }
  };

  return (
    <div className="max-w-4xl mx-auto">
      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <div className="md:flex">
          <div className="md:w-1/3 p-6 flex flex-col items-center border-r border-gray-200">
            <div className="w-32 h-32 rounded-full overflow-hidden mb-4 bg-gray-200">
              <img src={usuarioAtual.fotoPerfilUrl} alt={usuarioAtual.nomeCompleto} className="w-full h-full object-cover" />
            </div>
            <h1 className="text-xl font-bold text-center">{usuarioAtual.nomeCompleto}</h1>
            <p className="text-gray-600 text-center">@{usuarioAtual.username}</p>
            <div className="mt-6 w-full">
              {!isEditing ? (
                <button onClick={() => setIsEditing(true)} className="w-full flex items-center justify-center space-x-2 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                  <EditIcon className="h-4 w-4" />
                  <span>Editar Perfil</span>
                </button>
              ) : (
                <button onClick={() => setIsEditing(false)} className="w-full flex items-center justify-center space-x-2 bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600">
                  <span>Cancelar Edição</span>
                </button>
              )}

              {!ehAdmin && (
                <button onClick={handleDeleteAccount} className="w-full mt-4 flex items-center justify-center space-x-2 border border-red-500 text-red-500 px-4 py-2 rounded hover:bg-red-50">
                  <TrashIcon className="h-4 w-4" />
                  <span>Excluir Conta</span>
                </button>
              )}
            </div>
          </div>
          <div className="md:w-2/3 p-6">
            {isEditing ? (
              <div>
                <h2 className="text-xl font-semibold mb-4">Editar Perfil</h2>
                {erros.form && <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">{erros.form}</div>}
                <form onSubmit={handleSubmit} noValidate>
                  <div className="mb-4">
                    <label className="block text-gray-700 mb-2" htmlFor="nomeCompleto">Nome Completo</label>
                    <div className="relative">
                      <UserIcon className="absolute h-5 w-5 text-gray-400 left-3 top-1/2 -translate-y-1/2" />
                      <input id="nomeCompleto" name="nomeCompleto" type="text" value={formData.nomeCompleto} onChange={handleInputChange} className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg" />
                    </div>
                    {erros.nomeCompleto && <p className="text-red-500 text-sm mt-1">{erros.nomeCompleto}</p>}
                  </div>

                  <div className="mb-4">
                    <label className="block text-gray-700 mb-2" htmlFor="email">E-mail</label>
                    <div className="relative">
                      <MailIcon className="absolute h-5 w-5 text-gray-400 left-3 top-1/2 -translate-y-1/2" />
                      <input id="email" name="email" type="email" value={formData.email} onChange={handleInputChange} className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg" />
                    </div>
                    {erros.email && <p className="text-red-500 text-sm mt-1">{erros.email}</p>}
                  </div>

                  {!ehAdmin && (
                    <>
                      <div className="mb-4">
                        <label className="block text-gray-700 mb-2" htmlFor="username">Nome de Usuário</label>
                        <div className="relative">
                          <UserIcon className="absolute h-5 w-5 text-gray-400 left-3 top-1/2 -translate-y-1/2" />
                          <input id="username" name="username" type="text" value={formData.username} onChange={handleInputChange} className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg" />
                        </div>
                        {erros.username && <p className="text-red-500 text-sm mt-1">{erros.username}</p>}
                      </div>

                      <div className="mb-4">
                        <label className="block text-gray-700 mb-2" htmlFor="senha">Nova Senha (deixe em branco para manter a atual)</label>
                        <div className="relative">
                          <LockIcon className="absolute h-5 w-5 text-gray-400 left-3 top-1/2 -translate-y-1/2" />
                          <input id="senha" name="senha" type="password" value={formData.senha} onChange={handleInputChange} className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg" />
                        </div>
                        {erros.senha && <p className="text-red-500 text-sm mt-1">{erros.senha}</p>}
                      </div>

                      <div className="mb-6">
                        <label className="block text-gray-700 mb-2" htmlFor="confirmarSenha">Confirmar Nova Senha</label>
                        <div className="relative">
                          <LockIcon className="absolute h-5 w-5 text-gray-400 left-3 top-1/2 -translate-y-1/2" />
                          <input id="confirmarSenha" name="confirmarSenha" type="password" value={formData.confirmarSenha} onChange={handleInputChange} className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg" />
                        </div>
                        {erros.confirmarSenha && <p className="text-red-500 text-sm mt-1">{erros.confirmarSenha}</p>}
                      </div>
                    </>
                  )}

                  <button type="submit" className="flex items-center space-x-2 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:bg-blue-300" disabled={carregando}>
                    <SaveIcon className="h-4 w-4" />
                    <span>{carregando ? 'A salvar...' : 'Salvar Alterações'}</span>
                  </button>
                </form>
              </div>
            ) : (
              <div>
                <h2 className="text-xl font-semibold mb-6">Informações do Perfil</h2>
                <div className="mb-4">
                  <div className="flex items-center mb-2">
                    <UserIcon className="h-5 w-5 text-gray-500 mr-2" />
                    <span className="text-gray-700 font-medium">Nome Completo:</span>
                  </div>
                  <p className="text-gray-800 ml-7">{usuarioAtual.nomeCompleto}</p>
                </div>
                <div className="mb-4">
                  <div className="flex items-center mb-2">
                    <UserIcon className="h-5 w-5 text-gray-500 mr-2" />
                    <span className="text-gray-700 font-medium">Nome de Usuário:</span>
                  </div>
                  <p className="text-gray-800 ml-7">@{usuarioAtual.username}</p>
                </div>
                <div className="mb-6">
                  <div className="flex items-center mb-2">
                    <MailIcon className="h-5 w-5 text-gray-500 mr-2" />
                    <span className="text-gray-700 font-medium">E-mail:</span>
                  </div>
                  <p className="text-gray-800 ml-7">{usuarioAtual.email}</p>
                </div>

                {!ehAdmin && (
                  <div className="mt-8">
                    <h3 className="text-lg font-semibold mb-4 flex items-center">
                      <BookOpenIcon className="h-5 w-5 text-blue-600 mr-2" />
                      Sua Estante Virtual
                    </h3>
                    <p className="text-gray-500 italic">Em breve</p>
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;