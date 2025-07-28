import { useState, useEffect } from 'react';

interface LikeState {
  isLiked: boolean;
  likesCount: number;
}

export const useLikes = (itemId: string, initialLikes: number, type: 'livro' | 'avaliacao' | 'resposta') => {
  const [likeState, setLikeState] = useState<LikeState>({
    isLiked: false,
    likesCount: initialLikes
  });

  // Verificar se o item foi curtido pelo usuário atual no localStorage
  useEffect(() => {
    const likedItems = localStorage.getItem(`liked_${type}s`);
    if (likedItems) {
      const parsedLikes = JSON.parse(likedItems);
      const isLiked = parsedLikes.includes(itemId);
      setLikeState(prev => ({
        ...prev,
        isLiked
      }));
    }
  }, [itemId, type]);

  const toggleLike = () => {
    const likedItems = localStorage.getItem(`liked_${type}s`);
    const parsedLikes = likedItems ? JSON.parse(likedItems) : [];
    
    let newLikes: string[];
    let newIsLiked: boolean;
    let newCount: number;

    if (parsedLikes.includes(itemId)) {
      // Remove like
      newLikes = parsedLikes.filter((id: string) => id !== itemId);
      newIsLiked = false;
      newCount = Math.max(0, likeState.likesCount - 1);
    } else {
      // Add like
      newLikes = [...parsedLikes, itemId];
      newIsLiked = true;
      newCount = likeState.likesCount + 1;
    }

    localStorage.setItem(`liked_${type}s`, JSON.stringify(newLikes));
    setLikeState({
      isLiked: newIsLiked,
      likesCount: newCount
    });
  };

  return {
    ...likeState,
    toggleLike
  };
};
