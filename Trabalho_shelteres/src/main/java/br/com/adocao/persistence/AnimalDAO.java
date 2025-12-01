package br.com.adocao.persistence;

import br.com.adocao.model.Animal;

import java.sql.ResultSet;
import java.util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;


public class AnimalDAO {

    public void inserir(Animal animal) {
        String sql = "INSERT INTO Animal(nome, idade, especie, sexo, porte, peso, personalidade, historico, localEncontrado, situacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try( Connection cnn = ConnectionFactory.getConnection();
             PreparedStatement stmt = cnn.prepareStatement(sql)
        ){
            stmt.setString(1, animal.getNome());
            stmt.setInt(2, animal.getIdade());
            stmt.setString(3, animal.getEspecie());
            stmt.setString(4, animal.getSexo());
            stmt.setString(5, animal.getPorte());
            stmt.setFloat(6, animal.getPeso());
            stmt.setString(7, animal.getPersonalidade());
            stmt.setString(8, animal.getHistorico());
            stmt.setString(9, animal.getLocalEncontrado());
            stmt.setString(10, animal.getSituacao());

            stmt.execute();
            System.out.println("Animal inserido com sucesso!");
        } catch (SQLException ex) {
            System.out.println("Erro ao inserir animal: " + ex.getMessage());
        }
    }

    public List<Animal> listarTodos() throws SQLException {
        List<Animal> animais = new ArrayList<>();

        try (Connection cnn = ConnectionFactory.getConnection();
        PreparedStatement stmt = cnn.prepareStatement("SELECT * FROM Animal");
        ResultSet rs = stmt.executeQuery()){
            while(rs.next()){
                Animal a = new Animal(
                        rs.getInt("id_animal"),
                        rs.getString("nome"),
                        rs.getInt("idade"),
                        rs.getString("especie"),
                        rs.getString("sexo"),
                        rs.getString("personalidade"),
                        rs.getString("historico"),
                        rs.getString("localEncontrado"),
                        rs.getString("situacao"),
                        rs.getString("porte"),
                        rs.getFloat("peso")
                );
                animais.add(a);

            }
        }
    return animais;
    }
    public void deletarPorId(Integer id) throws SQLException {
        String sql = "DELETE FROM Animal WHERE id_animal = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void limparTabela() throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("DELETE FROM Animal");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='Animal'");
            stmt.executeUpdate("DELETE FROM AnimalAdotado");
        }
    }

    public void marcarComoAdotado(Integer idAnimal){
        String sql = "UPDATE Animal SET situacao = ? WHERE id_animal = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "Adotado");
            stmt.setInt(2, idAnimal);
            stmt.executeUpdate();

        } catch (SQLException ex){
            System.out.println("Erro ao marcar como adotado: " + ex.getMessage());
        }
    }
    public Animal getAnimalPorId(Integer idAnimal){
        String sql = "SELECT * FROM Animal WHERE id_animal = ?";
        try(Connection conn = ConnectionFactory.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, idAnimal);
            try(
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                Animal a = new Animal(
                        rs.getInt("id_animal"),
                        rs.getString("nome"),
                        rs.getInt("idade"),
                        rs.getString("especie"),
                        rs.getString("sexo"),
                        rs.getString("personalidade"),
                        rs.getString("historico"),
                        rs.getString("localEncontrado"),
                        rs.getString("situacao"),
                        rs.getString("porte"),
                        rs.getFloat("peso")
                );
                return a;
                }
            }catch(SQLException ex){
                System.out.println("Erro ao buscar animal: " + ex.getMessage());
            }
        }catch (Exception e){
            System.out.println("Erro ao obter animal por id: " + e.getMessage());
        }
        return null;
    }

    public void marcarComoDiposnivel(Integer idAnimal){
        String sql = "UPDATE Animal SET situacao = ? WHERE id_animal = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "Diposnivel");
            stmt.setInt(2, idAnimal);
            stmt.executeUpdate();

        } catch (SQLException ex){
            System.out.println("Erro ao marcar como adotado: " + ex.getMessage());
        }
    }

    public void marcarComoIndisponivel(Integer idAnimal){
        String sql = "UPDATE Animal SET situacao = ? WHERE id_animal = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "Indiposnivel");
            stmt.setInt(2, idAnimal);
            stmt.executeUpdate();

        } catch (SQLException ex){
            System.out.println("Erro ao marcar como adotado: " + ex.getMessage());
        }
    }

    public List<Animal> getAdotadosByCpf(String cpf) throws SQLException{
        List<Animal> animais = new ArrayList<>();
        String sql = "SELECT a.* FROM Animal a JOIN AnimalAdotado aa ON a.id_animal = aa.id_animal WHERE aa.cpf = ?";
        try ( Connection cnn = ConnectionFactory.getConnection();
              PreparedStatement stmt = cnn.prepareStatement(sql);){

            stmt.setString(1, cpf);

            try  ( ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    Integer id_animal = rs.getInt("id_animal");
                    String nome = rs.getString("nome");
                    Integer idade = rs.getInt("idade");
                    String especie = rs.getString("especie");
                    String sexo = rs.getString("sexo");
                    String porte = rs.getString("porte");
                    Float peso = rs.getFloat("peso");
                    String personalidade = rs.getString("personalidade");
                    String historico = rs.getString("historico");
                    String localEncontrado = rs.getString("localEncontrado");
                    String situacao = rs.getString("situacao");

                    Animal animal = new Animal(id_animal, nome, idade, especie, sexo, personalidade, historico, localEncontrado, situacao, porte, peso);

                    animais.add(animal);

                }
            }
            return animais;
        }
        catch (SQLException ex){
            System.out.println("Erro ao obter adotados: " + ex.getMessage());
        }
        return animais;
    }

    public void inserirAnimalAdotado (Integer id_animal, String cpfAdotante, String data) {
        String sql = "INSERT INTO AnimalAdotado(id_animal, cpf, data) VALUES (?, ?, ?)";

        try (Connection cnn = ConnectionFactory.getConnection();
             PreparedStatement stmt = cnn.prepareStatement(sql)) {
            stmt.setInt(1, id_animal);
            stmt.setString(2, cpfAdotante);
            stmt.setString(3, data);

            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Erro ao tentar inserir animal na relação de animais adotados." +  e.getMessage());
        }
    }

    public void removerAnimalAdotado (Integer id_animal) {
        String sql = "DELETE FROM AnimalAdotado WHERE id_animal = ?";
        try( Connection cnn = ConnectionFactory.getConnection();
        PreparedStatement stmt = cnn.prepareStatement(sql)){
            stmt.setInt(1, id_animal);
            stmt.execute();
        }catch (Exception e){
            System.out.println("Erro ao remover animal de animaisAdotados");
        }
    }
}
