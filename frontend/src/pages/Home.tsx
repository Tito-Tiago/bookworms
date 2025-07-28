import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { useLivros } from '../contexts/LivroContext';
import CartaoLivro from '../components/LivroCard';
import { SearchIcon } from 'lucide-react';

const Inicio: React.FC = () => {
  const { livros, loading, error } = useLivros();
  const [consultaPesquisa, setConsultaPesquisa] = useState('');
  const [livrosFiltrados, setLivrosFiltrados] = useState(livros);
  const location = useLocation();

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const consulta = params.get('search') || '';
    setConsultaPesquisa(consulta);
  }, [location.search]);

  useEffect(() => {
    if (consultaPesquisa) {
      const consulta = consultaPesquisa.toLowerCase();
      const filtrados = livros.filter(livro => 
        livro.titulo.toLowerCase().includes(consulta) || 
        livro.autor.toLowerCase().includes(consulta)
      );
      setLivrosFiltrados(filtrados);
    } else {
      const ordenados = [...livros].sort((a, b) => {
        const interacoesA = a.likes + a.avaliacoes.length + 
          a.avaliacoes.reduce((soma, avaliacao) => soma + avaliacao.replies.length, 0);
        const interacoesB = b.likes + b.avaliacoes.length + 
          b.avaliacoes.reduce((soma, avaliacao) => soma + avaliacao.replies.length, 0);
        return interacoesB - interacoesA;
      });
      setLivrosFiltrados(ordenados);
    }
  }, [livros, consultaPesquisa]);

  const lidarPesquisa = (e: React.FormEvent) => {
    e.preventDefault();
  };

  if (loading) {
    return (
      <div className="text-center py-12">
        <p>Carregando livros...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-center py-12 text-red-600">
        <p>Erro ao carregar livros: {error}</p>
      </div>
    );
  }

  return (
    <div className="max-w-6xl mx-auto">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-center mb-2">Bookworms</h1>
        <p className="text-gray-600 text-center mb-6">
          Descubra novos livros e compartilhe suas opiniões com outros leitores.
        </p>
        <form onSubmit={lidarPesquisa} className="max-w-lg mx-auto relative">
          <input 
            type="text" 
            placeholder="Buscar por título ou autor..." 
            value={consultaPesquisa} 
            onChange={e => setConsultaPesquisa(e.target.value)} 
            className="w-full py-2 px-4 pr-10 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" 
          />
          <button type="submit" className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500">
            <SearchIcon className="h-5 w-5" />
          </button>
        </form>
      </div>
      
      {consultaPesquisa && (
        <div className="mb-6">
          <h2 className="text-xl font-semibold mb-2">
            Resultados para "{consultaPesquisa}"
          </h2>
          <p className="text-gray-600">
            {livrosFiltrados.length}{' '}
            {livrosFiltrados.length === 1 ? 'livro encontrado' : 'livros encontrados'}
          </p>
        </div>
      )}
      
      {livrosFiltrados.length > 0 ? (
        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 md:gap-6">
          {livrosFiltrados.map(livro => (
            <CartaoLivro 
              key={livro.id} 
              id={livro.id} 
              titulo={livro.titulo} 
              autor={livro.autor} 
              fotoCapaUrl={livro.fotoCapaUrl} 
              likes={livro.likes} 
              quantidadeAvaliacoes={livro.avaliacoes.length} 
            />
          ))}
        </div>
      ) : (
        <div className="text-center py-12">
          <p className="text-gray-600 mb-2">Nenhum livro encontrado.</p>
          {consultaPesquisa && (
            <p className="text-gray-500">
              Tente buscar por outro título ou autor.
            </p>
          )}
        </div>
      )}
    </div>
  );
};

export default Inicio;