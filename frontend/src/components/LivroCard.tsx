import React from 'react';
import { Link } from 'react-router-dom';
import { HeartIcon, MessageSquareIcon } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import { useLivros } from '../contexts/LivroContext';
import { useLikes } from '../hooks/useLikes';

interface LivroCardProps {
  id: string;
  titulo: string;
  autor: string;
  fotoCapaUrl: string;
  likes: number;
  quantidadeAvaliacoes: number;
}

const CartaoLivro: React.FC<LivroCardProps> = ({
  id,
  titulo,
  autor,
  fotoCapaUrl,
  likes,
  quantidadeAvaliacoes
}) => {
  const { usuarioAtual } = useAuth();
  const { alternarCurtida } = useLivros();
  const { isLiked, likesCount, toggleLike } = useLikes(id, likes, 'livro');

  const lidarCurtida = async (e: React.MouseEvent) => {
    e.preventDefault();
    if (!usuarioAtual) return;
    
    // Atualizar UI imediatamente
    toggleLike();
    
    try {
      // Sincronizar com backend
      await alternarCurtida(id);
    } catch (error) {
      // Reverter mudança local em caso de erro
      toggleLike();
      console.error('Erro ao curtir livro:', error);
    }
  };

  return (
    <Link to={`/livro/${id}`} className="block">
      <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow">
        <div className="aspect-[2/3] overflow-hidden">
          <img 
            src={fotoCapaUrl || 'https://via.placeholder.com/150x200?text=Livro'} 
            alt={titulo} 
            className="w-full h-full object-cover object-center" 
          />
        </div>
        <div className="p-2 sm:p-4">
          <h3 className="font-semibold text-base sm:text-lg truncate">{titulo}</h3>
          <p className="text-gray-600 text-sm">{autor}</p>
          <div className="mt-3 flex items-center justify-between">
            <div className="flex items-center space-x-4">
              <button 
                onClick={lidarCurtida} 
                className={`flex items-center space-x-1 ${isLiked ? 'text-red-500' : 'text-gray-500'} hover:text-red-600`}
                disabled={!usuarioAtual}
              >
                <HeartIcon 
                  className="h-4 w-4" 
                  fill={isLiked ? 'currentColor' : 'none'} 
                />
                <span>{likesCount}</span>
              </button>
              <div className="flex items-center space-x-1 text-gray-500">
                <MessageSquareIcon className="h-4 w-4" />
                <span>{quantidadeAvaliacoes}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </Link>
  );
};

export default CartaoLivro;