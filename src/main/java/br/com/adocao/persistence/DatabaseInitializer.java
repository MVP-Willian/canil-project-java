package br.com.adocao.persistence;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {
    public static void inicializar() {
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()){


            String sqlUsuario = """
             CREATE TABLE IF NOT EXISTS Usuario(
                nome TEXT NOT NULL,
                cpf TEXT NOT NULL PRIMARY KEY,
                email TEXT NOT NULL UNIQUE,
                senha TEXT NOT NULL,
                renda FLOAT,
                status BOOLEAN NOT NULL,
                tipoConta TEXT NOT NULL
             );
             """;
            String sqlAnimal = """
            CREATE TABLE IF NOT EXISTS Animal (
                id_animal INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                idade   INTEGER,
                especie TEXT NOT NULL,
                sexo TEXT NOT NULL,
                porte TEXT NOT NULL,
                peso FLOAT NOT NULL,
                personalidade TEXT NOT NULL,
                historico TEXT NOT NULL,
                localEncontrado  TEXT NOT NULL,
                situacao TEXT NOT NULL
            );
            """;

            String sqlSolicitacao = """
             CREATE TABLE IF NOT EXISTS Solicitacao(
                id_solicitacao INTEGER PRIMARY KEY AUTOINCREMENT,
                cpf_solicitante TEXT NOT NULL,
                tipo_solicitacao TEXT NOT NULL,
                status_solicitacao TEXT NOT NULL,
                feedBackAdmin TEXT,
             
                FOREIGN KEY (cpf_solicitante) REFERENCES Usuario(cpf)
             );
             """;

            String sqlAnimaisAdotados = """
               CREATE TABLE IF NOT EXISTS AnimalAdotado (
                id_animal INT NOT NULL,
                cpf TEXT NOT NULL,
                data TEXT NOT NULL,
             
                FOREIGN KEY (cpf) REFERENCES Usuario(cpf),
                FOREIGN KEY (id_animal) REFERENCES Animal(id_animal)
             );
             """;

            String sqlSolicitacaoResgate = """
            CREATE TABLE IF NOT EXISTS SolicitacaoResgate (
            id_solicitacao INTEGER NOT NULL,
            especie TEXT,
            sexo TEXT,
            local TEXT,
            descricao TEXT,
            
            FOREIGN KEY (id_solicitacao) REFERENCES Solicitacao(id_solicitacao)
            );
            """;
            String sqlSolicitacaoAdocao = """
            CREATE TABLE IF NOT EXISTS SolicitacaoAdocao (
            id_solicitacao INTEGER NOT NULL,
            id_animal INTEGER NOT NULL,
            
            FOREIGN KEY (id_solicitacao) REFERENCES Solicitacao(id_solicitacao)
            FOREIGN KEY (id_animal) REFERENCES Animal(id_animal)
            );
            """;


            stmt.execute(sqlAnimal);
            stmt.execute(sqlUsuario);
            stmt.execute(sqlSolicitacao);
            stmt.execute(sqlAnimaisAdotados);
            stmt.execute(sqlSolicitacaoResgate);
            stmt.execute(sqlSolicitacaoAdocao);

            System.out.println("LOG: Banco de dados inicializado com sucesso!");

        } catch (SQLException e) {
            System.out.println("X LOG Error: erro ao inicializar o banco de dados.");
            e.printStackTrace();
        }
    }
}
