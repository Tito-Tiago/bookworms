import React from 'react';
import { HeartIcon } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import { useLivros } from '../contexts/LivroContext';

interface LikeButtomProps {
  itemId: string;
  initialLikes: number;
  type: 'avaliacao' | 'resposta';
  onLike: (id: string) => Promise<void>;
}

const LikeButton: React.FC<LikeButtomProps> = ({ itemId, initialLikes, type, onLike }) => {
  const { usuarioAtual } = useAuth();
  const { getLikeState, toggleLikeLocal } = useLivros();
  const { isLiked, likesCount } = getLikeState(itemId, initialLikes, type);

  const handleLike = async () => {
    if (!usuarioAtual) return;
    
    toggleLikeLocal(itemId, type);
    
    try {
      await onLike(itemId);
    } catch (error) {
      toggleLikeLocal(itemId, type);
      console.error(`Erro ao curtir ${type}:`, error);
    }
  };

  return (
    <button 
      onClick={handleLike} 
      className={`flex items-center space-x-1 ${isLiked ? 'text-red-500' : 'text-gray-500'} hover:text-red-600`}
      disabled={!usuarioAtual}
    >
      <HeartIcon 
        className="h-4 w-4" 
        fill={isLiked ? 'currentColor' : 'none'} 
      />
      <span>{likesCount}</span>
    </button>
  );
};

export default LikeButton;
