INSERT INTO alunos (id, nome_completo, email, username, senha, foto_perfil_url) VALUES
(RANDOM_UUID(), 'Laura Mendes', 'laura.mendes@email.com', 'lauramendes', 'senha123', 'https://pin.it/Cg3X3D8YE'),
(RANDOM_UUID(), 'Pedro Rocha', 'pedro.rocha@email.com', 'pedrorocha', 'senha456', 'https://pin.it/Cg3X3D8YE'),
(RANDOM_UUID(), 'Beatriz Lima', 'beatriz.lima@email.com', 'bialima', 'senha789', 'https://pin.it/Cg3X3D8YE'),
(RANDOM_UUID(), 'Gabriel Faria', 'gabriel.faria@email.com', 'gabrielf', 'senha101', 'https://pin.it/Cg3X3D8YE'),
(RANDOM_UUID(), 'Júlia Azevedo', 'julia.azevedo@email.com', 'juhazevedo', 'senha112', 'https://pin.it/Cg3X3D8YE');

INSERT INTO livros (id, titulo, sinopse, genero, autor, foto_capa_url, likes, soma_avaliacoes, num_avaliacoes) VALUES
(RANDOM_UUID(), 'O Senhor dos Anéis', 'Uma grande aventura em um mundo de fantasia.', 'Fantasia', 'J.R.R. Tolkien', 'https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdn.dc5.ro%2Fimg-prod%2F1728045377-5.jpeg&f=1&nofb=1&ipt=c250928c058af85391c169f0bf5b7f407fc678e3cf9adea25694fb1282bb4aa6', 10, 45, 10),
(RANDOM_UUID(), 'O Hobbit', 'A história de um hobbit que parte em uma aventura inesperada.', 'Fantasia', 'J.R.R. Tolkien', 'https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdn.dc5.ro%2Fimg-prod%2F1728045377-5.jpeg&f=1&nofb=1&ipt=c250928c058af85391c169f0bf5b7f407fc678e3cf9adea25694fb1282bb4aa6', 5, 20, 5),
(RANDOM_UUID(), 'O Silmarillion', 'A história da criação da Terra-média.', 'Fantasia', 'J.R.R. Tolkien', 'https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdn.dc5.ro%2Fimg-prod%2F1728045377-5.jpeg&f=1&nofb=1&ipt=c250928c058af85391c169f0bf5b7f407fc678e3cf9adea25694fb1282bb4aa6', 2, 8, 2);
