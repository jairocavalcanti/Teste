CREATE TABLE tarefas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL UNIQUE,
    custo DECIMAL(10, 2) NOT NULL,
    data_limite DATE NOT NULL,
    ordem_apresentacao INT NOT NULL 
);