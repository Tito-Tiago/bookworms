import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useLivros } from '../contexts/LivroContext';
import { useAuth } from '../contexts/AuthContext';
import LikeButton from '../components/LikeButton';
import { HeartIcon, StarIcon, TrashIcon, MessageSquareIcon, SendIcon } from 'lucide-react';

const DetalheLivro: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const {
    obterLivro,
    alternarCurtida,
    adicionarAvaliacao,
    adicionarRespostaAvaliacao,
    alternarCurtidaAvaliacao,
    alternarCurtidaResposta,
    excluirLivro,
    carregarLivro,
    loading,
    error,
    getLikeState,
    toggleLikeLocal
  } = useLivros();
  const { usuarioAtual, ehAdmin } = useAuth();

  const [numEstrelas, setNumEstrelas] = useState(0);
  const [comentario, setComentario] = useState('');
  const [comentarioResposta, setComentarioResposta] = useState<{ [key: string]: string }>({});
  const [mostrarFormResposta, setMostrarFormResposta] = useState<{ [key: string]: boolean }>({});

  const livro = obterLivro(id || '');
  const { isLiked: livroLiked, likesCount: livroLikes } = getLikeState(
    livro?.id || '', 
    livro?.likes || 0, 
    'livro'
  );

  useEffect(() => {
    if (id && !livro) {
      carregarLivro(id);
    }
  }, [id, livro, carregarLivro]);

  if (loading) {
    return <div className="text-center py-12">Carregando...</div>;
  }

  if (error) {
    return <div className="text-center py-12 text-red-600">Erro: {error}</div>;
  }

  if (!livro) {
    return (
      <div className="text-center py-12">
        <h2 className="text-2xl font-bold mb-2">Livro não encontrado</h2>
        <p className="text-gray-600 mb-4">
          O livro que você está procurando não existe.
        </p>
        <button 
          onClick={() => navigate('/home')} 
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          Voltar para a página inicial
        </button>
      </div>
    );
  }

  const lidarCurtida = async () => {
    if (!usuarioAtual) {
      navigate('/login');
      return;
    }
    
    // Atualizar UI imediatamente
    toggleLikeLocal(livro.id, 'livro');
    
    try {
      await alternarCurtida(livro.id);
    } catch (error) {
      // Reverter mudança local em caso de erro
      toggleLikeLocal(livro.id, 'livro');
      console.error('Erro ao curtir livro:', error);
    }
  };

  const lidarEnvioAvaliacao = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!usuarioAtual) {
      navigate('/login');
      return;
    }
    if (numEstrelas === 0) {
      alert('Por favor, selecione uma classificação de 1 a 5 estrelas.');
      return;
    }
    
    try {
      await adicionarAvaliacao(livro.id, numEstrelas, comentario);
      setNumEstrelas(0);
      setComentario('');
    } catch (error) {
      console.error('Erro ao adicionar avaliação:', error);
    }
  };

  const lidarEnvioResposta = async (avaliacaoId: string) => {
    if (!usuarioAtual) {
      navigate('/login');
      return;
    }
    if (!comentarioResposta[avaliacaoId]?.trim()) {
      return;
    }
    
    try {
      await adicionarRespostaAvaliacao(avaliacaoId, comentarioResposta[avaliacaoId]);
      setComentarioResposta(prev => ({ ...prev, [avaliacaoId]: '' }));
      setMostrarFormResposta(prev => ({ ...prev, [avaliacaoId]: false }));
    } catch (error) {
      console.error('Erro ao adicionar resposta:', error);
    }
  };

  const lidarExclusaoLivro = async () => {
    if (window.confirm('Tem certeza que deseja excluir este livro?')) {
      try {
        await excluirLivro(livro.id);
        navigate('/home');
      } catch (error) {
        console.error('Erro ao excluir livro:', error);
      }
    }
  };

  const alternarFormResposta = (avaliacaoId: string) => {
    setMostrarFormResposta(prev => ({
      ...prev,
      [avaliacaoId]: !prev[avaliacaoId]
    }));
  };

  return (
    <div className="max-w-4xl mx-auto">
      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <div className="md:flex">
          <div className="md:w-1/3 p-6">
            <img 
              src={livro.fotoCapaUrl || 'https://via.placeholder.com/150x200?text=Livro'} 
              alt={livro.titulo} 
              className="w-full h-auto object-cover rounded-lg shadow" 
            />
            <div className="mt-6 flex justify-between items-center">
              <button 
                onClick={lidarCurtida} 
                className={`flex items-center space-x-1 ${livroLiked ? 'text-red-500' : 'text-gray-500'} hover:text-red-600`}
              >
                <HeartIcon 
                  className="h-6 w-6" 
                  fill={livroLiked ? 'currentColor' : 'none'} 
                />
                <span>{livroLikes}</span>
              </button>
              {ehAdmin && (
                <button 
                  onClick={lidarExclusaoLivro} 
                  className="text-red-500 hover:text-red-700 flex items-center space-x-1"
                >
                  <TrashIcon className="h-5 w-5" />
                  <span>Excluir livro</span>
                </button>
              )}
            </div>
          </div>
          <div className="md:w-2/3 p-6">
            <h1 className="text-3xl font-bold mb-2">{livro.titulo}</h1>
            <p className="text-gray-600 mb-4">{livro.autor}</p>
            <div className="mb-6">
              <h2 className="text-xl font-semibold mb-2">Sinopse</h2>
              <p className="text-gray-700">{livro.sinopse}</p>
            </div>
            <div className="mb-6">
              <h2 className="text-xl font-semibold mb-2">Gênero</h2>
              <span className="bg-blue-100 text-blue-800 px-3 py-1 rounded-full text-sm">
                {livro.genero}
              </span>
            </div>
            <div className="mb-6">
              <h2 className="text-xl font-semibold mb-2">Avaliação média</h2>
              <div className="flex items-center">
                {[1, 2, 3, 4, 5].map(estrela => (
                  <StarIcon 
                    key={estrela} 
                    className={`h-5 w-5 ${estrela <= Math.round(livro.rating) ? 'text-yellow-400' : 'text-gray-300'}`} 
                    fill={estrela <= Math.round(livro.rating) ? 'currentColor' : 'none'} 
                  />
                ))}
                <span className="ml-2 text-gray-600">({livro.rating.toFixed(1)})</span>
              </div>
            </div>
          </div>
        </div>
        
        <div className="border-t border-gray-200 p-6">
          <h2 className="text-xl font-semibold mb-6">Avaliações</h2>
          
          {usuarioAtual && (
            <form onSubmit={lidarEnvioAvaliacao} className="mb-8 bg-gray-50 p-4 rounded-lg">
              <h3 className="font-medium mb-4">Deixe sua avaliação</h3>
              <div className="mb-4">
                <div className="flex items-center">
                  {[1, 2, 3, 4, 5].map(estrela => (
                    <button 
                      key={estrela} 
                      type="button" 
                      onClick={() => setNumEstrelas(estrela)} 
                      className="focus:outline-none"
                    >
                      <StarIcon 
                        className={`h-8 w-8 ${estrela <= numEstrelas ? 'text-yellow-400' : 'text-gray-300'}`} 
                        fill={estrela <= numEstrelas ? 'currentColor' : 'none'} 
                      />
                    </button>
                  ))}
                </div>
              </div>
              <div className="mb-4">
                <textarea
                  value={comentario}
                  onChange={e => setComentario(e.target.value)}
                  className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="Escreva um comentário (opcional)"
                  rows={3}
                />
              </div>
              <button 
                type="submit" 
                className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
              >
                Enviar avaliação
              </button>
            </form>
          )}
          
          {livro.avaliacoes.length > 0 ? (
            <div className="space-y-6">
              {livro.avaliacoes.map(avaliacao => (
                <div key={avaliacao.id} className="bg-white border border-gray-200 rounded-lg p-4">
                  <div className="flex justify-between items-start mb-2">
                    <div>
                      <div className="font-medium">{avaliacao.aluno.nomeCompleto}</div>
                      <div className="flex items-center mt-1">
                        {[1, 2, 3, 4, 5].map(estrela => (
                          <StarIcon 
                            key={estrela} 
                            className={`h-4 w-4 ${estrela <= avaliacao.numEstrelas ? 'text-yellow-400' : 'text-gray-300'}`} 
                            fill={estrela <= avaliacao.numEstrelas ? 'currentColor' : 'none'} 
                          />
                        ))}
                      </div>
                    </div>
                  </div>
                  
                  {avaliacao.comentario && (
                    <p className="text-gray-700 mb-3">{avaliacao.comentario}</p>
                  )}
                  
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-4">
                      <LikeButton
                        itemId={avaliacao.id}
                        initialLikes={avaliacao.likes}
                        type="avaliacao"
                        onLike={alternarCurtidaAvaliacao}
                      />
                      <button 
                        onClick={() => alternarFormResposta(avaliacao.id)} 
                        className="flex items-center space-x-1 text-gray-500 hover:text-gray-700"
                      >
                        <MessageSquareIcon className="h-4 w-4" />
                        <span>{avaliacao.replies.length}</span>
                      </button>
                    </div>
                  </div>
                  
                  {/* Respostas */}
                  {avaliacao.replies.length > 0 && (
                    <div className="mt-3 pl-4 border-l-2 border-gray-100 space-y-3">
                      {avaliacao.replies.map(resposta => (
                        <div key={resposta.id} className="bg-gray-50 p-3 rounded">
                          <div className="flex justify-between items-start mb-2">
                            <div className="font-medium">{resposta.aluno.nomeCompleto}</div>
                          </div>
                          <p className="text-gray-700 mb-2">{resposta.comentario}</p>
                          <LikeButton
                            itemId={resposta.id}
                            initialLikes={resposta.likes}
                            type="resposta"
                            onLike={alternarCurtidaResposta}
                          />
                        </div>
                      ))}
                    </div>
                  )}
                  
                  {/* Formulário de resposta */}
                  {mostrarFormResposta[avaliacao.id] && usuarioAtual && (
                    <div className="mt-3 flex">
                      <input
                        type="text"
                        value={comentarioResposta[avaliacao.id] || ''}
                        onChange={e => setComentarioResposta({
                          ...comentarioResposta,
                          [avaliacao.id]: e.target.value
                        })}
                        className="flex-1 p-2 border border-gray-300 rounded-l-lg focus:outline-none focus:ring-1 focus:ring-blue-500"
                        placeholder="Escreva uma resposta..."
                      />
                      <button 
                        onClick={() => lidarEnvioResposta(avaliacao.id)} 
                        className="bg-blue-600 text-white p-2 rounded-r-lg hover:bg-blue-700"
                      >
                        <SendIcon className="h-5 w-5" />
                      </button>
                    </div>
                  )}
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-6 text-gray-500">
              Nenhuma avaliação ainda. Seja o primeiro a avaliar este livro!
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default DetalheLivro;