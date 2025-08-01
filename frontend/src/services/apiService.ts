const API_BASE_URL = 'http://localhost:8080';

interface ApiResponse<T> {
  data: T;
  message: string;
  timestamp: string;
}

class ApiService {
  private baseURL = API_BASE_URL;

  private async request<T>(url: string, options: RequestInit = {}): Promise<T> {
    const token = localStorage.getItem('token');
    
    const config: RequestInit = {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
        ...options.headers,
      },
    };

    try {
      const response = await fetch(`${this.baseURL}${url}`, config);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const result: ApiResponse<T> = await response.json();
      return result.data;
    } catch (error) {
      console.error('API request failed:', error);
      throw error;
    }
  }

  // Livros
  async getLivros() {
    return this.request<any[]>('/livros');
  }

  async getLivroById(id: string) {
    return this.request<any>(`/livros/${id}`);
  }

  async createLivro(livro: any) {
    return this.request<any>('/livros/cadastro', {
      method: 'POST',
      body: JSON.stringify(livro),
    });
  }

  async deleteLivro(id: string) {
    return this.request<{ mensagem: string }>(`/livros/${id}`, {
      method: 'DELETE',
    });
  }

  async likeLivro(id: string) {
    return this.request<any>(`/livros/${id}/like`, {
      method: 'POST',
    });
  }

  async unlikeLivro(id: string) {
    return this.request<any>(`/livros/${id}/like`, {
      method: 'DELETE',
    });
  }

  // Avaliações
  async createAvaliacao(avaliacao: any) {
    return this.request<any>('/avaliacoes', {
      method: 'POST',
      body: JSON.stringify(avaliacao),
    });
  }

  async likeAvaliacao(id: string) {
    return this.request<any>(`/avaliacoes/${id}/like`, {
      method: 'POST',
    });
  }

  async unlikeAvaliacao(id: string) {
    return this.request<any>(`/avaliacoes/${id}/like`, {
      method: 'DELETE',
    });
  }

  // Respostas
  async createResposta(resposta: any) {
    return this.request<any>('/avaliacoes/respostas', {
      method: 'POST',
      body: JSON.stringify(resposta),
    });
  }

  async likeResposta(id: string) {
    return this.request<any>(`/avaliacoes/respostas/${id}/like`, {
      method: 'POST',
    });
  }

  async unlikeResposta(id: string) {
    return this.request<any>(`/avaliacoes/respostas/${id}/like`, {
      method: 'DELETE',
    });
  }

  // Autenticação
  async loginAluno(login: string, senha: string) {
    const response = await fetch(`${this.baseURL}/auth/login/aluno`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ login, senha }),
    });
    
    if (!response.ok) {
      const errorData = await response.json();
      throw errorData;
    }
    
    return response.json();
  }

  async loginBibliotecario(username: string, password: string) {
    const response = await fetch(`${this.baseURL}/auth/login/bibliotecario`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    });
    
    if (!response.ok) {
      const errorData = await response.json();
      throw errorData;
    }
    
    return response.json();
  }

  async registerAluno(nomeCompleto: string, username: string, email: string, senha: string) {
    const response = await fetch(`${this.baseURL}/alunos/cadastro`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ nomeCompleto, email, username, senha }),
    });
    
    if (!response.ok) {
      const errorData = await response.json();
      throw errorData;
    }
    
    return response.json();
  }

  async updateAluno(id: string, data: any) {
    return this.request<any>(`/alunos/${id}`, {
      method: 'PATCH',
      body: JSON.stringify(data),
    });
  }

  async deleteAluno(id: string) {
    return this.request<any>(`/alunos/${id}`, {
      method: 'DELETE',
    });
  }
}

export const apiService = new ApiService();
