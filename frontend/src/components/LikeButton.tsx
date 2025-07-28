import React from 'react';
import { HeartIcon } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import { useLikes } from '../hooks/useLikes';

interface LikeButtomProps {
  itemId: string;
  initialLikes: number;
  type: 'avaliacao' | 'resposta';
  onLike: (id: string) => Promise<void>;
}

const LikeButton: React.FC<LikeButtomProps> = ({ itemId, initialLikes, type, onLike }) => {
  const { usuarioAtual } = useAuth();
  const { isLiked, likesCount, toggleLike } = useLikes(itemId, initialLikes, type);

  const handleLike = async () => {
    if (!usuarioAtual) return;
    
    // Atualizar UI imediatamente
    toggleLike();
    
    try {
      await onLike(itemId);
    } catch (error) {
      // Reverter mudança local em caso de erro
      toggleLike();
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
