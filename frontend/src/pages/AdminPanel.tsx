import React, { useState } from 'react';
import { useLivros } from '../contexts/LivroContext';
import { PlusIcon, BookIcon, TrashIcon, SearchIcon } from 'lucide-react';

const PainelAdministrativo: React.FC = () => {
  const {
    livros,
    adicionarLivro,
    excluirLivro,
    buscarLivros,
    loading,
    error
  } = useLivros();
  const [mostrarFormularioAdicionar, setMostrarFormularioAdicionar] = useState(false);
  const [consultaPesquisa, setConsultaPesquisa] = useState('');
  const [novoLivro, setNovoLivro] = useState({
    titulo: '',
    autor: '',
    sinopse: '',
    genero: ''
  });
  const livrosFiltrados = consultaPesquisa ? buscarLivros(consultaPesquisa) : livros;
  const lidarMudancaInput = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const {
      name,
      value
    } = e.target;
    setNovoLivro(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const lidarAdicaoLivro = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!novoLivro.titulo || !novoLivro.autor || !novoLivro.sinopse || !novoLivro.genero) {
      alert('Todos os campos são obrigatórios.');
      return;
    }
    
    try {
      await adicionarLivro(novoLivro.titulo, novoLivro.sinopse, novoLivro.autor, novoLivro.genero);
      setNovoLivro({
        titulo: '',
        autor: '',
        sinopse: '',
        genero: ''
      });
      setMostrarFormularioAdicionar(false);
    } catch (error) {
      console.error('Erro ao adicionar livro:', error);
      alert('Erro ao adicionar livro. Tente novamente.');
    }
  };

  const lidarExclusaoLivro = async (id: string) => {
    if (window.confirm('Tem certeza que deseja excluir este livro?')) {
      try {
        await excluirLivro(id);
      } catch (error) {
        console.error('Erro ao excluir livro:', error);
        alert('Erro ao excluir livro. Tente novamente.');
      }
    }
  };

  const lidarPesquisa = (e: React.FormEvent) => {
    e.preventDefault();
  };
  return <div className="max-w-6xl mx-auto">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-2xl font-bold">Painel Administrativo</h1>
        <button onClick={() => setMostrarFormularioAdicionar(!mostrarFormularioAdicionar)} className="flex items-center space-x-2 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700">
          <PlusIcon className="h-5 w-5" />
          <span>{mostrarFormularioAdicionar ? 'Cancelar' : 'Adicionar Livro'}</span>
        </button>
      </div>
      {mostrarFormularioAdicionar && <div className="bg-white p-6 rounded-lg shadow-md mb-8">
          <h2 className="text-xl font-semibold mb-4">Adicionar Novo Livro</h2>
          <form onSubmit={lidarAdicaoLivro}>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
              <div>
                <label className="block text-gray-700 mb-2" htmlFor="titulo">
                  Título
                </label>
                <input id="titulo" name="titulo" type="text" value={novoLivro.titulo} onChange={lidarMudancaInput} className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Digite o título do livro" />
              </div>
              <div>
                <label className="block text-gray-700 mb-2" htmlFor="autor">
                  Autor
                </label>
                <input id="autor" name="autor" type="text" value={novoLivro.autor} onChange={lidarMudancaInput} className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Digite o nome do autor" />
              </div>
            </div>
            <div className="mb-4">
              <label className="block text-gray-700 mb-2" htmlFor="genero">
                Gênero
              </label>
              <select id="genero" name="genero" value={novoLivro.genero} onChange={lidarMudancaInput} className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option value="">Selecione um gênero</option>
                <option value="Romance">Romance</option>
                <option value="Ficção Científica">Ficção Científica</option>
                <option value="Fantasia">Fantasia</option>
                <option value="Terror">Terror</option>
                <option value="Suspense">Suspense</option>
                <option value="Biografia">Biografia</option>
                <option value="História">História</option>
                <option value="Poesia">Poesia</option>
                <option value="Drama">Drama</option>
                <option value="Aventura">Aventura</option>
                <option value="Fábula">Fábula</option>
                <option value="Outro">Outro</option>
              </select>
            </div>
            <div className="mb-4">
              <label className="block text-gray-700 mb-2" htmlFor="sinopse">
                Sinopse
              </label>
              <textarea id="sinopse" name="sinopse" value={novoLivro.sinopse} onChange={lidarMudancaInput} className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Digite a sinopse do livro" rows={4}></textarea>
            </div>
            <div className="text-right">
              <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700" disabled={loading}>
                {loading ? 'Adicionando...' : 'Adicionar Livro'}
              </button>
            </div>
          </form>
        </div>}
      <div className="bg-white p-6 rounded-lg shadow-md">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-xl font-semibold">Gerenciar Acervo</h2>
          <form onSubmit={lidarPesquisa} className="relative">
            <input type="text" placeholder="Buscar livros..." value={consultaPesquisa} onChange={e => setConsultaPesquisa(e.target.value)} className="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <SearchIcon className="h-5 w-5 text-gray-400" />
            </div>
          </form>
        </div>
        {error && (
          <div className="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded">
            {error}
          </div>
        )}
        {loading ? (
          <div className="text-center py-8">
            <p>Carregando livros...</p>
          </div>
        ) : livrosFiltrados.length > 0 ? <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Livro
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Autor
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Gênero
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Interações
                  </th>
                  <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Ações
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {livrosFiltrados.map(livro => <tr key={livro.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center">
                        <div className="flex-shrink-0 h-10 w-10">
                          <img src={livro.fotoCapaUrl} alt={livro.titulo} className="h-10 w-10 rounded-sm object-cover" onError={(e) => {
                            const target = e.target as HTMLImageElement;
                            target.src = 'https://via.placeholder.com/150x200/gray/white?text=Sem+Capa';
                          }} />
                        </div>
                        <div className="ml-4">
                          <div className="text-sm font-medium text-gray-900">
                            {livro.titulo}
                          </div>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {livro.autor}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800">
                        {livro.genero}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {livro.likes} likes, {livro.avaliacoes?.length || 0}{' '}
                      avaliações
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                      <button onClick={() => lidarExclusaoLivro(livro.id)} className="text-red-600 hover:text-red-900" disabled={loading}>
                        <TrashIcon className="h-5 w-5" />
                      </button>
                    </td>
                  </tr>)}
              </tbody>
            </table>
          </div> : <div className="text-center py-8 text-gray-500">
            {consultaPesquisa ? <p>Nenhum livro encontrado para "{consultaPesquisa}".</p> : <div className="flex flex-col items-center">
                <BookIcon className="h-12 w-12 text-gray-300 mb-2" />
                <p>
                  O acervo está vazio. Adicione livros usando o botão acima.
                </p>
              </div>}
          </div>}
      </div>
    </div>;
};
export default PainelAdministrativo;