import React, { useEffect, useState, createContext, useContext } from 'react';
import { useAuth } from './AuthContext';
import { apiService } from '../services/apiService';

interface Usuario {
  id: string;
  nomeCompleto: string;
  email: string;
  username: string;
  fotoPerfilUrl: string;
}

interface Livro {
  id: string;
  titulo: string;
  sinopse: string;
  autor: string;
  genero: string;
  fotoCapaUrl: string;
  likes: number;
  rating: number;
  avaliacoes: Avaliacao[];
}

interface Avaliacao {
  id: string;
  aluno: Usuario;
  numEstrelas: number;
  comentario: string;
  likes: number;
  replies: Resposta[];
}

interface Resposta {
  id: string;
  aluno: Usuario;
  comentario: string;
  likes: number;
}

interface LivroContextType {
  livros: Livro[];
  loading: boolean;
  error: string | null;
  adicionarLivro: (titulo: string, sinopse: string, autor: string, genero: string) => Promise<void>;
  excluirLivro: (id: string) => Promise<void>;
  obterLivro: (id: string) => Livro | undefined;
  buscarLivros: (consulta: string) => Livro[];
  alternarCurtida: (livroId: string) => Promise<void>;
  adicionarAvaliacao: (livroId: string, numEstrelas: number, comentario: string) => Promise<void>;
  adicionarRespostaAvaliacao: (avaliacaoId: string, comentario: string) => Promise<void>;
  alternarCurtidaAvaliacao: (avaliacaoId: string) => Promise<void>;
  alternarCurtidaResposta: (respostaId: string) => Promise<void>;
  carregarLivros: () => Promise<void>;
  carregarLivro: (id: string) => Promise<void>;
}

const LivroContext = createContext<LivroContextType | undefined>(undefined);

export const useLivros = () => {
  const context = useContext(LivroContext);
  if (context === undefined) {
    throw new Error('useLivros deve ser usado dentro de um LivroProvider');
  }
  return context;
};

export const LivroProvider: React.FC<{
  children: React.ReactNode;
}> = ({ children }) => {
  const [livros, setLivros] = useState<Livro[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const { usuarioAtual } = useAuth();

  const carregarLivros = async () => {
    setLoading(true);
    setError(null);
    try {
      const livrosData = await apiService.getLivros();
      setLivros(livrosData);
    } catch (err) {
      setError('Erro ao carregar livros');
      console.error('Erro ao carregar livros:', err);
    } finally {
      setLoading(false);
    }
  };

  const carregarLivro = async (id: string) => {
    setLoading(true);
    setError(null);
    try {
      const livroData = await apiService.getLivroById(id);
      setLivros(prev => {
        const index = prev.findIndex(l => l.id === id);
        if (index >= 0) {
          const newLivros = [...prev];
          newLivros[index] = livroData;
          return newLivros;
        } else {
          return [...prev, livroData];
        }
      });
    } catch (err) {
      setError('Erro ao carregar livro');
      console.error('Erro ao carregar livro:', err);
    } finally {
      setLoading(false);
    }
  };

  // Carregar livros na inicialização
  useEffect(() => {
    carregarLivros();
  }, []);

  const adicionarLivro = async (titulo: string, sinopse: string, autor: string, genero: string) => {
    try {
      const novoLivro = await apiService.createLivro({
        titulo,
        sinopse,
        autor,
        genero
      });
      setLivros(prev => [...prev, novoLivro]);
    } catch (err) {
      setError('Erro ao adicionar livro');
      console.error('Erro ao adicionar livro:', err);
      throw err;
    }
  };

  const excluirLivro = async (id: string) => {
    try {
      await apiService.deleteLivro(id);
      setLivros(prev => prev.filter(livro => livro.id !== id));
    } catch (err) {
      setError('Erro ao excluir livro');
      console.error('Erro ao excluir livro:', err);
      throw err;
    }
  };

  const obterLivro = (id: string) => {
    return livros.find(livro => livro.id === id);
  };

  const buscarLivros = (consulta: string) => {
    consulta = consulta.toLowerCase();
    return livros.filter(livro => 
      livro.titulo.toLowerCase().includes(consulta) || 
      livro.autor.toLowerCase().includes(consulta)
    );
  };

  const alternarCurtida = async (livroId: string) => {
    if (!usuarioAtual) return;
    
    try {
      const livroAtualizado = await apiService.likeLivro(livroId);
      setLivros(prev => prev.map(livro => 
        livro.id === livroId ? livroAtualizado : livro
      ));
    } catch (err) {
      setError('Erro ao curtir livro');
      console.error('Erro ao curtir livro:', err);
      throw err;
    }
  };

  const adicionarAvaliacao = async (livroId: string, numEstrelas: number, comentario: string) => {
    if (!usuarioAtual) return;
    
    try {
      await apiService.createAvaliacao({
        livroId,
        numEstrelas,
        comentario
      });
      // Recarregar o livro para pegar a avaliação atualizada
      await carregarLivro(livroId);
    } catch (err) {
      setError('Erro ao adicionar avaliação');
      console.error('Erro ao adicionar avaliação:', err);
      throw err;
    }
  };

  const adicionarRespostaAvaliacao = async (avaliacaoId: string, comentario: string) => {
    if (!usuarioAtual) return;
    
    try {
      await apiService.createResposta({
        avaliacaoId,
        comentario
      });
      // Recarregar todos os livros para atualizar as respostas
      await carregarLivros();
    } catch (err) {
      setError('Erro ao adicionar resposta');
      console.error('Erro ao adicionar resposta:', err);
      throw err;
    }
  };

  const alternarCurtidaAvaliacao = async (avaliacaoId: string) => {
    if (!usuarioAtual) return;
    
    try {
      await apiService.likeAvaliacao(avaliacaoId);
      // Recarregar todos os livros para atualizar as curtidas
      await carregarLivros();
    } catch (err) {
      setError('Erro ao curtir avaliação');
      console.error('Erro ao curtir avaliação:', err);
      throw err;
    }
  };

  const alternarCurtidaResposta = async (respostaId: string) => {
    if (!usuarioAtual) return;
    
    try {
      await apiService.likeResposta(respostaId);
      // Recarregar todos os livros para atualizar as curtidas
      await carregarLivros();
    } catch (err) {
      setError('Erro ao curtir resposta');
      console.error('Erro ao curtir resposta:', err);
      throw err;
    }
  };

  const valor = {
    livros,
    loading,
    error,
    adicionarLivro,
    excluirLivro,
    obterLivro,
    buscarLivros,
    alternarCurtida,
    adicionarAvaliacao,
    adicionarRespostaAvaliacao,
    alternarCurtidaAvaliacao,
    alternarCurtidaResposta,
    carregarLivros,
    carregarLivro
  };

  return <LivroContext.Provider value={valor}>{children}</LivroContext.Provider>;
};