package br.com.adocao.persistence;

import br.com.adocao.model.Admin;
import br.com.adocao.model.User;
import br.com.adocao.model.Animal;
import br.com.adocao.persistence.AnimalDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public void inserir(User user){
        String sql = "INSERT INTO Usuario(nome, cpf, email, senha, renda, status, tipoConta) VALUES (?,?,?,?,?,?,?)";
        try( Connection cnn = ConnectionFactory.getConnection();
             PreparedStatement stmt = cnn.prepareStatement(sql)
        ){

            stmt.setString(1, user.getNome());
            stmt.setString(2, user.getCpf());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getSenha());
            stmt.setFloat(5, user.getRenda());
            stmt.setBoolean(6, user.getStatusConta());
            stmt.setString(7, user.getTipoConta());

            stmt.execute();
            System.out.println("User inserido com sucesso!");
        }catch (SQLException e){
            System.out.println("Erro ao inserir Usuario: " + e.getMessage());
        }
    }

    public void limparTabela() throws SQLException{
        try( Connection cnn = ConnectionFactory.getConnection();
        Statement stmt = cnn.createStatement()){
            stmt.execute("DELETE FROM Usuario");
        }
    }

    public User getUserEmail(String email) throws SQLException{
        String sql = "SELECT * FROM Usuario WHERE email = ?";
        try ( Connection cnn = ConnectionFactory.getConnection();
        PreparedStatement stmt = cnn.prepareStatement(sql)){
              stmt.setString(1, email);

              try ( ResultSet rs = stmt.executeQuery()){


                  //como email é chave então o resultado é para ser apenas uma
                  String nome = rs.getString("nome");
                  String cpf = rs.getString("cpf");
                  String senha = rs.getString("senha");
                  float renda = rs.getFloat("renda");
                  boolean statusConta = rs.getBoolean("status");
                  String tipoConta = rs.getString("tipoConta");


                  if(!statusConta){
                      return null;
                  }
                  if(tipoConta.equals("Admin")){
                      return new Admin(nome, email, cpf, senha, renda,  statusConta);
                  }
                  else if(tipoConta.equals("User")){
                      return new User(nome, email, cpf, senha, renda, statusConta);
                  }
                  else {
                      return null;
                  }

              }
        }
    }

    public User getUserCpf(String cpf){
        String sql = "SELECT * FROM Usuario WHERE cpf = ?";
        try ( Connection cnn = ConnectionFactory.getConnection();
              PreparedStatement stmt = cnn.prepareStatement(sql)){
            stmt.setString(1, cpf);

            try ( ResultSet rs = stmt.executeQuery()){


                //como email é chave então o resultado é para ser apenas uma
                String nome = rs.getString("nome");
                String senha = rs.getString("senha");
                String email = rs.getString("email");
                float renda = rs.getFloat("renda");
                boolean statusConta = rs.getBoolean("status");
                String tipoConta = rs.getString("tipoConta");


                if(statusConta == false){
                    System.out.println("Conta esta desativada!");
                    return null;
                }
                if(tipoConta.equals("Admin")){
                    return new Admin(nome, cpf, email, senha, renda,  statusConta);
                }
                else if(tipoConta.equals("User")){
                    return new User(nome, cpf, email, senha, renda, statusConta);
                }
                else {
                    System.out.println("Erro ao tentar verificar o tipo da conta");
                    return null;
                }
            }
        } catch(SQLException e){
            System.out.println("Erro tentar resgatar usuário pelo CPF: " + e.getMessage());
        }
        System.out.println("Usuario não encontrado.");
        return null;
    }


    public List<User> listarTodos(){
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";

        try(Connection cnn = ConnectionFactory.getConnection();
            PreparedStatement stmt = cnn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            while(rs.next()){
                String tipo = rs.getString("tipoConta");

                if(tipo.equals("Admin")){
                    Admin user = new Admin(
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getFloat("renda"),
                            rs.getBoolean("status")
                    );
                    users.add(user);
                }
                else if(tipo.equals("User")){
                    User user = new User(
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getFloat("renda"),
                            rs.getBoolean("status")
                    );
                    users.add(user);
                }
                else{ System.out.println("Erro ao listar todos os dados!"); return null;}
            }

        }catch (SQLException e){
            System.out.println("Erro ao listar Usuarios: " + e.getMessage());
        }
        return users;
    }

}
