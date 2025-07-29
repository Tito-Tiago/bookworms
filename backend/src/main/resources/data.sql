-- Inserindo 10 usuários
INSERT INTO alunos (id, nome_completo, email, username, senha, foto_perfil_url) VALUES
(RANDOM_UUID(), 'Laura Mendes', 'laura.mendes@email.com', 'lauramendes', 'senha123', 'https://images.unsplash.com/photo-1494790108755-2616b612b5bb?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3'),
(RANDOM_UUID(), 'Pedro Rocha', 'pedro.rocha@email.com', 'pedrorocha', 'senha456', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3'),
(RANDOM_UUID(), 'Beatriz Lima', 'beatriz.lima@email.com', 'bialima', 'senha789', 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?q=80&w=1770&auto=format&fit=crop&ixlib=rb-4.0.3'),
(RANDOM_UUID(), 'Gabriel Faria', 'gabriel.faria@email.com', 'gabrielf', 'senha101', 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?q=80&w=1770&auto=format&fit=crop&ixlib=rb-4.0.3'),
(RANDOM_UUID(), 'Júlia Azevedo', 'julia.azevedo@email.com', 'juhazevedo', 'senha112', 'https://images.unsplash.com/photo-1544725176-7c40e5a71c5e?q=80&w=1769&auto=format&fit=crop&ixlib=rb-4.0.3'),
(RANDOM_UUID(), 'Roberto Silva', 'roberto.silva@email.com', 'robertosilva', 'senha113', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3'),
(RANDOM_UUID(), 'Ana Carolina', 'ana.carolina@email.com', 'anacarol', 'senha114', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=1964&auto=format&fit=crop&ixlib=rb-4.0.3'),
(RANDOM_UUID(), 'Carlos Eduardo', 'carlos.eduardo@email.com', 'carlosedu', 'senha115', 'https://images.unsplash.com/photo-1507591064344-4c6ce005b128?q=80&w=1770&auto=format&fit=crop&ixlib=rb-4.0.3'),
(RANDOM_UUID(), 'Mariana Costa', 'mariana.costa@email.com', 'maricosta', 'senha116', 'https://images.unsplash.com/photo-1517841905240-472988babdf9?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3'),
(RANDOM_UUID(), 'Felipe Santos', 'felipe.santos@email.com', 'felipesantos', 'senha117', 'https://images.unsplash.com/photo-1560250097-0b93528c311a?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3');

-- Inserindo 10 livros com diferentes gêneros e autores
INSERT INTO livros (id, titulo, sinopse, genero, autor, foto_capa_url, likes, soma_avaliacoes, num_avaliacoes) VALUES
(RANDOM_UUID(), 'O Senhor dos Anéis', 'Uma épica jornada pela Terra-média, onde um jovem hobbit deve destruir um anel para salvar o mundo.', 'Fantasia', 'J.R.R. Tolkien', 'https://m.media-amazon.com/images/I/71jLBXtWJWL._AC_UF1000,1000_QL80_.jpg', 85, 432, 96),
(RANDOM_UUID(), 'Dom Casmurro', 'A história de Bentinho e sua obsessão com o suposto adultério de Capitu.', 'Romance', 'Machado de Assis', 'https://m.media-amazon.com/images/I/71+3VbxhazL._AC_UF1000,1000_QL80_.jpg', 67, 318, 72),
(RANDOM_UUID(), 'O Alquimista', 'A jornada de Santiago em busca de seu tesouro pessoal e realização dos sonhos.', 'Ficção', 'Paulo Coelho', 'https://m.media-amazon.com/images/I/51Z0nLAfLmL._AC_UF1000,1000_QL80_.jpg', 92, 456, 114),
(RANDOM_UUID(), '1984', 'Uma distopia sobre vigilância, controle mental e autoritarismo em uma sociedade totalitária.', 'Ficção Científica', 'George Orwell', 'https://m.media-amazon.com/images/I/71rpa1-kyvL._AC_UF1000,1000_QL80_.jpg', 78, 389, 89),
(RANDOM_UUID(), 'Cem Anos de Solidão', 'A saga da família Buendía na cidade imaginária de Macondo.', 'Realismo Mágico', 'Gabriel García Márquez', 'https://m.media-amazon.com/images/I/81MI+d+LoNL._AC_UF1000,1000_QL80_.jpg', 56, 267, 61),
(RANDOM_UUID(), 'O Hobbit', 'A aventura de Bilbo Bolseiro com treze anões em busca do tesouro do dragão Smaug.', 'Fantasia', 'J.R.R. Tolkien', 'https://m.media-amazon.com/images/I/712cDO7d73L._AC_UF1000,1000_QL80_.jpg', 71, 348, 81),
(RANDOM_UUID(), 'Orgulho e Preconceito', 'A história de amor entre Elizabeth Bennet e Mr. Darcy na Inglaterra do século XIX.', 'Romance', 'Jane Austen', 'https.m.media-amazon.com/images/I/71Q1tPupKjL._AC_UF1000,1000_QL80_.jpg', 89, 425, 98),
(RANDOM_UUID(), 'O Código Da Vinci', 'Um thriller sobre símbolos religiosos e sociedades secretas.', 'Suspense', 'Dan Brown', 'https://m.media-amazon.com/images/I/815WORuYMML._AC_UF1000,1000_QL80_.jpg', 63, 294, 68),
(RANDOM_UUID(), 'A Garota no Trem', 'Um thriller psicológico sobre memórias fragmentadas e um assassinato misterioso.', 'Suspense', 'Paula Hawkins', 'https://m.media-amazon.com/images/I/81gTRv2HXrL._AC_UF1000,1000_QL80_.jpg', 45, 201, 47),
(RANDOM_UUID(), 'O Nome do Vento', 'A primeira parte da história de Kvothe, um lendário herói e músico.', 'Fantasia', 'Patrick Rothfuss', 'https://m.media-amazon.com/images/I/81bCB31+vOL._AC_UF1000,1000_QL80_.jpg', 74, 362, 84);

-- Inserindo avaliações distribuídas entre usuários e livros
INSERT INTO avaliacoes (id, num_estrelas, comentario, aluno_id, livro_id, likes) VALUES
-- Avaliações para O Senhor dos Anéis
(RANDOM_UUID(), 5, 'Uma obra-prima da fantasia! Tolkien criou um mundo incrível.', (SELECT id FROM alunos WHERE username = 'lauramendes'), (SELECT id FROM livros WHERE titulo = 'O Senhor dos Anéis'), 0),
(RANDOM_UUID(), 4, 'Muito bom, mas um pouco longo para meu gosto.', (SELECT id FROM alunos WHERE username = 'pedrorocha'), (SELECT id FROM livros WHERE titulo = 'O Senhor dos Anéis'), 0),
(RANDOM_UUID(), 5, 'Simplesmente perfeito! Recomendo para todos.', (SELECT id FROM alunos WHERE username = 'bialima'), (SELECT id FROM livros WHERE titulo = 'O Senhor dos Anéis'), 0),
(RANDOM_UUID(), 4, 'Excelente desenvolvimento dos personagens.', (SELECT id FROM alunos WHERE username = 'gabrielf'), (SELECT id FROM livros WHERE titulo = 'O Senhor dos Anéis'), 0);

INSERT INTO avaliacoes (id, num_estrelas, comentario, aluno_id, livro_id, likes) VALUES
-- Avaliações para Dom Casmurro
(RANDOM_UUID(), 4, 'Machado de Assis é genial! A ambiguidade é fascinante.', (SELECT id FROM alunos WHERE username = 'juhazevedo'), (SELECT id FROM livros WHERE titulo = 'Dom Casmurro'), 0),
(RANDOM_UUID(), 3, 'Interessante, mas achei um pouco pesado.', (SELECT id FROM alunos WHERE username = 'robertosilva'), (SELECT id FROM livros WHERE titulo = 'Dom Casmurro'), 0),
(RANDOM_UUID(), 5, 'Um clássico que todo brasileiro deveria ler.', (SELECT id FROM alunos WHERE username = 'anacarol'), (SELECT id FROM livros WHERE titulo = 'Dom Casmurro'), 0);

INSERT INTO avaliacoes (id, num_estrelas, comentario, aluno_id, livro_id, likes) VALUES
-- Avaliações para O Alquimista
(RANDOM_UUID(), 5, 'Inspirador! Mudou minha perspectiva sobre a vida.', (SELECT id FROM alunos WHERE username = 'carlosedu'), (SELECT id FROM livros WHERE titulo = 'O Alquimista'), 0),
(RANDOM_UUID(), 4, 'Uma leitura leve e motivacional.', (SELECT id FROM alunos WHERE username = 'maricosta'), (SELECT id FROM livros WHERE titulo = 'O Alquimista'), 0),
(RANDOM_UUID(), 3, 'Bom, mas não tão revolucionário quanto esperava.', (SELECT id FROM alunos WHERE username = 'felipesantos'), (SELECT id FROM livros WHERE titulo = 'O Alquimista'), 0),
(RANDOM_UUID(), 5, 'Paulo Coelho sempre acerta no coração.', (SELECT id FROM alunos WHERE username = 'lauramendes'), (SELECT id FROM livros WHERE titulo = 'O Alquimista'), 0);

INSERT INTO avaliacoes (id, num_estrelas, comentario, aluno_id, livro_id, likes) VALUES
-- Avaliações para 1984
(RANDOM_UUID(), 5, 'Assustadoramente atual. Orwell foi um visionário.', (SELECT id FROM alunos WHERE username = 'pedrorocha'), (SELECT id FROM livros WHERE titulo = '1984'), 0),
(RANDOM_UUID(), 4, 'Muito bem escrito, mas depressivo.', (SELECT id FROM alunos WHERE username = 'bialima'), (SELECT id FROM livros WHERE titulo = '1984'), 0),
(RANDOM_UUID(), 5, 'Leitura obrigatória para entender o mundo atual.', (SELECT id FROM alunos WHERE username = 'gabrielf'), (SELECT id FROM livros WHERE titulo = '1984'), 0);

INSERT INTO avaliacoes (id, num_estrelas, comentario, aluno_id, livro_id, likes) VALUES
-- Avaliações para Cem Anos de Solidão
(RANDOM_UUID(), 4, 'García Márquez é um mestre do realismo mágico.', (SELECT id FROM alunos WHERE username = 'juhazevedo'), (SELECT id FROM livros WHERE titulo = 'Cem Anos de Solidão'), 0),
(RANDOM_UUID(), 3, 'Confuso no início, mas vale a pena persistir.', (SELECT id FROM alunos WHERE username = 'robertosilva'), (SELECT id FROM livros WHERE titulo = 'Cem Anos de Solidão'), 0),
(RANDOM_UUID(), 5, 'Uma obra-prima da literatura latino-americana.', (SELECT id FROM alunos WHERE username = 'anacarol'), (SELECT id FROM livros WHERE titulo = 'Cem Anos de Solidão'), 0);

INSERT INTO avaliacoes (id, num_estrelas, comentario, aluno_id, livro_id, likes) VALUES
-- Avaliações para O Hobbit
(RANDOM_UUID(), 4, 'Mais acessível que O Senhor dos Anéis.', (SELECT id FROM alunos WHERE username = 'carlosedu'), (SELECT id FROM livros WHERE titulo = 'O Hobbit'), 0),
(RANDOM_UUID(), 5, 'Perfeito para quem está começando na fantasia.', (SELECT id FROM alunos WHERE username = 'maricosta'), (SELECT id FROM livros WHERE titulo = 'O Hobbit'), 0),
(RANDOM_UUID(), 4, 'Bilbo é um personagem cativante.', (SELECT id FROM alunos WHERE username = 'felipesantos'), (SELECT id FROM livros WHERE titulo = 'O Hobbit'), 0);

INSERT INTO avaliacoes (id, num_estrelas, comentario, aluno_id, livro_id, likes) VALUES
-- Avaliações para Orgulho e Preconceito
(RANDOM_UUID(), 5, 'Jane Austen é incomparável! Romance perfeito.', (SELECT id FROM alunos WHERE username = 'lauramendes'), (SELECT id FROM livros WHERE titulo = 'Orgulho e Preconceito'), 0),
(RANDOM_UUID(), 4, 'Elizabeth Bennet é uma protagonista incrível.', (SELECT id FROM alunos WHERE username = 'pedrorocha'), (SELECT id FROM livros WHERE titulo = 'Orgulho e Preconceito'), 0),
(RANDOM_UUID(), 5, 'Um dos melhores romances da literatura inglesa.', (SELECT id FROM alunos WHERE username = 'bialima'), (SELECT id FROM livros WHERE titulo = 'Orgulho e Preconceito'), 0);

INSERT INTO avaliacoes (id, num_estrelas, comentario, aluno_id, livro_id, likes) VALUES
-- Avaliações para O Código Da Vinci
(RANDOM_UUID(), 3, 'Entretenimento puro, mas sem muita profundidade.', (SELECT id FROM alunos WHERE username = 'gabrielf'), (SELECT id FROM livros WHERE titulo = 'O Código Da Vinci'), 0),
(RANDOM_UUID(), 4, 'Thriller envolvente, difícil de largar.', (SELECT id FROM alunos WHERE username = 'juhazevedo'), (SELECT id FROM livros WHERE titulo = 'O Código Da Vinci'), 0),
(RANDOM_UUID(), 4, 'Dan Brown sabe como criar suspense.', (SELECT id FROM alunos WHERE username = 'robertosilva'), (SELECT id FROM livros WHERE titulo = 'O Código Da Vinci'), 0);

INSERT INTO avaliacoes (id, num_estrelas, comentario, aluno_id, livro_id, likes) VALUES
-- Avaliações para A Garota no Trem
(RANDOM_UUID(), 4, 'Thriller psicológico bem construído.', (SELECT id FROM alunos WHERE username = 'anacarol'), (SELECT id FROM livros WHERE titulo = 'A Garota no Trem'), 0),
(RANDOM_UUID(), 3, 'Interessante, mas previsível em alguns momentos.', (SELECT id FROM alunos WHERE username = 'carlosedu'), (SELECT id FROM livros WHERE titulo = 'A Garota no Trem'), 0);

INSERT INTO avaliacoes (id, num_estrelas, comentario, aluno_id, livro_id, likes) VALUES
-- Avaliações para O Nome do Vento
(RANDOM_UUID(), 5, 'Rothfuss é um mestre da narrativa fantástica.', (SELECT id FROM alunos WHERE username = 'maricosta'), (SELECT id FROM livros WHERE titulo = 'O Nome do Vento'), 0),
(RANDOM_UUID(), 4, 'Kvothe é um protagonista fascinante.', (SELECT id FROM alunos WHERE username = 'felipesantos'), (SELECT id FROM livros WHERE titulo = 'O Nome do Vento'), 0),
(RANDOM_UUID(), 5, 'Mal posso esperar pelo próximo livro da série.', (SELECT id FROM alunos WHERE username = 'lauramendes'), (SELECT id FROM livros WHERE titulo = 'O Nome do Vento'), 0);