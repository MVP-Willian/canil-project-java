package br.com.adocao.persistence;

import br.com.adocao.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class SolicitacaoDAO {
    public void inserir(Solicitacao solicitacao) {
        String sql = "INSERT INTO Solicitacao(cpf_solicitante, tipo_solicitacao, status_solicitacao, feedBackAdmin) VALUES(?,?,?,?)";

        try ( Connection cnn = ConnectionFactory.getConnection();
            PreparedStatement stmt = cnn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, solicitacao.getSolicitante());
            stmt.setString(2, solicitacao.getTipoSolicitacao().name());
            stmt.setString(3, solicitacao.getStatus().name());
            stmt.setString(4, solicitacao.getFeedbackAdmin());

            stmt.execute();

            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            int idSolicitacao = rs.getInt(1);


            System.out.println("Solicitacao inserida com sucesso! ID: " + idSolicitacao);

            if(solicitacao.getTipoSolicitacao()  == TipoSolicitacao.ADOCAO){
                String sqlAdocao = "INSERT INTO SolicitacaoAdocao(id_solicitacao, id_animal) VALUES(?, ?) ";

                try (PreparedStatement stmtAdocao = cnn.prepareStatement(sqlAdocao)) {
                    stmtAdocao.setInt(1, idSolicitacao);
                    stmtAdocao.setInt(2, solicitacao.getIdAnimal());
                    stmtAdocao.executeUpdate();
                }

                System.out.println("SolicitacaoAdocao inserida!");
            }
            else if(solicitacao.getTipoSolicitacao() == TipoSolicitacao.RESGATE){
                String sqlResgate = "INSERT INTO SolicitacaoResgate(id_solicitacao, especie, sexo, local, descricao) VALUES(?, ?, ?, ?, ?) ";

                try (PreparedStatement stmtResgate = cnn.prepareStatement(sqlResgate)) {
                    stmtResgate.setInt(1, idSolicitacao);
                    stmtResgate.setString(2, solicitacao.getEspecie());
                    stmtResgate.setString(3, solicitacao.getSexo());
                    stmtResgate.setString(4, solicitacao.getLocal());
                    stmtResgate.setString(5, solicitacao.getDescricao());
                    stmtResgate.executeUpdate();
                }

                System.out.println("SolicitacaoResgate inserida!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inserir solicitacao: " + e.getMessage());
        }
    }

    public void limparTabela() throws SQLException {
        try ( Connection cnn = ConnectionFactory.getConnection();
        Statement stmt = cnn.createStatement()){
            stmt.execute("DELETE FROM Solicitacao");
            stmt.execute("DELETE FROM SolicitacaoAdocao");
            stmt.execute("DELETE FROM SolicitacaoResgate");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='Solicitacao'");

        }
    }
    public List<Solicitacao> listarSolicitacoesAdocaoCpf(String cpfSolicitacao) throws SQLException {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql = "SELECT s.*, s1.id_animal FROM Solicitacao s JOIN SolicitacaoAdocao s1 ON s.id_solicitacao=s1.id_solicitacao WHERE s.cpf_solicitante = ? ";
        try( Connection cnn = ConnectionFactory.getConnection();
        PreparedStatement stmt = cnn.prepareStatement(sql);){
            stmt.setString(1, cpfSolicitacao);
            try (ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    Integer id_solicitacao = rs.getInt("id_solicitacao");
                    StatusSolicitacao status_solicitacao = StatusSolicitacao.valueOf(rs.getString("status_solicitacao"));
                    Integer id_animal = rs.getInt("id_animal");
                    String feedback_admin = rs.getString("feedBackAdmin");

                    SolicitacaoAdocao solicitacaoAdocao= new SolicitacaoAdocao(cpfSolicitacao, id_animal);
                    solicitacaoAdocao.setId(id_solicitacao);
                    solicitacaoAdocao.setStatus(status_solicitacao);
                    solicitacaoAdocao.setFeedbackAdmin(feedback_admin);

                    solicitacoes.add(solicitacaoAdocao);
                }
            }
            return solicitacoes;
        }
        catch( Exception e ){
            System.out.println("Erro ao listar Adocoes: " + e.getMessage() );
            return null;
        }
    }

    public List<Solicitacao> listarSolicitacoesResgateCpf(String cpfSolicitacao) throws SQLException {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql = "SELECT s.*, s1.* FROM Solicitacao s JOIN SolicitacaoResgate s1 ON s.id_solicitacao=s1.id_solicitacao WHERE s.cpf_solicitante = ? ";
        try( Connection cnn = ConnectionFactory.getConnection();
             PreparedStatement stmt = cnn.prepareStatement(sql);){
            stmt.setString(1, cpfSolicitacao);
            try (ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    Integer id_solicitacao = rs.getInt("id_solicitacao");
                    StatusSolicitacao status_solicitacao = StatusSolicitacao.valueOf(rs.getString("status_solicitacao"));;
                    String especie = rs.getString("especie");
                    String sexo = rs.getString("sexo");
                    String local = rs.getString("local");
                    String descricao = rs.getString("descricao");
                    String feedback_admin = rs.getString("feedBackAdmin");

                    SolicitacaoResgate solicitacaoResgate= new SolicitacaoResgate(cpfSolicitacao, especie, sexo, local, descricao);
                    solicitacaoResgate.setId(id_solicitacao);
                    solicitacaoResgate.setStatus(status_solicitacao);
                    solicitacaoResgate.setFeedbackAdmin(feedback_admin);

                    solicitacoes.add(solicitacaoResgate);
                }
            }
            return solicitacoes;
        } catch( Exception e ){
            System.out.println("Erro ao listar Resgates: " + e.getMessage() );
            return null;
        }
    }

    public List<Solicitacao> listarTodasSolicitacoesCpf(String cpf) throws SQLException {
        List<Solicitacao> solicitacoes = new ArrayList<>();

        solicitacoes.addAll(listarSolicitacoesAdocaoCpf(cpf));
        solicitacoes.addAll(listarSolicitacoesResgateCpf(cpf));

        return solicitacoes;
    }

    public void deletarSolicitacao(int id) throws SQLException {
        String sqlAdocao = "DELETE FROM SolicitacaoAdocao WHERE id_solicitacao = ?";
        String sqlResgate = "DELETE FROM SolicitacaoResgate WHERE id_solicitacao = ?";
        String sqlSolicitacao = "DELETE FROM Solicitacao WHERE id_solicitacao = ?";

        try (Connection cnn = ConnectionFactory.getConnection()) {

            // 1. Apaga registros filhos
            try (PreparedStatement stmt = cnn.prepareStatement(sqlAdocao)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = cnn.prepareStatement(sqlResgate)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            // 2. Apaga o registro pai
            try (PreparedStatement stmt = cnn.prepareStatement(sqlSolicitacao)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        }
    }
    public List<Solicitacao> listarSolicitacoesAdocao() throws SQLException {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql = "SELECT s.*, s1.id_animal FROM Solicitacao s JOIN SolicitacaoAdocao s1 ON s.id_solicitacao=s1.id_solicitacao";
        try( Connection cnn = ConnectionFactory.getConnection();
             PreparedStatement stmt = cnn.prepareStatement(sql);){
            try (ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    Integer id_solicitacao = rs.getInt("id_solicitacao");
                    String cpf_solicitante = rs.getString("cpf_solicitante");
                    StatusSolicitacao status_solicitacao = StatusSolicitacao.valueOf(rs.getString("status_solicitacao"));;
                    Integer id_animal = rs.getInt("id_animal");
                    String feedback_admin = rs.getString("feedBackAdmin");

                    SolicitacaoAdocao solicitacaoAdocao= new SolicitacaoAdocao(cpf_solicitante, id_animal);
                    solicitacaoAdocao.setId(id_solicitacao);
                    solicitacaoAdocao.setStatus(status_solicitacao);
                    solicitacaoAdocao.setFeedbackAdmin(feedback_admin);

                    solicitacoes.add(solicitacaoAdocao);
                }
            }
            return solicitacoes;
        }
        catch( Exception e ){
            System.out.println("Erro ao listar Adocoes: " + e.getMessage() );
            return null;
        }
    }
    public List<Solicitacao> listarSolicitacoesResgate() throws SQLException {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql = "SELECT s.*, s1.* FROM Solicitacao s JOIN SolicitacaoResgate s1 ON s.id_solicitacao=s1.id_solicitacao";
        try( Connection cnn = ConnectionFactory.getConnection();
             PreparedStatement stmt = cnn.prepareStatement(sql);){
            try (ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    String cpf_solicitante = rs.getString("cpf_solicitante");
                    Integer id_solicitacao = rs.getInt("id_solicitacao");
                    StatusSolicitacao status_solicitacao = StatusSolicitacao.valueOf(rs.getString("status_solicitacao"));;
                    String especie = rs.getString("especie");
                    String sexo = rs.getString("sexo");
                    String local = rs.getString("local");
                    String descricao = rs.getString("descricao");
                    String feedback_admin = rs.getString("feedBackAdmin");

                    SolicitacaoResgate solicitacaoResgate= new SolicitacaoResgate(cpf_solicitante, especie, sexo, local, descricao);
                    solicitacaoResgate.setId(id_solicitacao);
                    solicitacaoResgate.setStatus(status_solicitacao);
                    solicitacaoResgate.setFeedbackAdmin(feedback_admin);

                    solicitacoes.add(solicitacaoResgate);
                }
            }
            return solicitacoes;
        } catch( Exception e ){
            System.out.println("Erro ao listar Resgates: " + e.getMessage() );
            return null;
        }
    }


    public List<Solicitacao> listarTodasSolicitacoes() throws SQLException {
        List<Solicitacao> solicitacoes = new ArrayList<>();

        solicitacoes.addAll(listarSolicitacoesAdocao());
        solicitacoes.addAll(listarSolicitacoesResgate());

        return solicitacoes;
    }

    public void atualizarAdocaoAprovado(int id_solicitacao) throws SQLException {
        String sqlAtualizarStatus = "UPDATE Solicitacao SET status_solicitacao=? WHERE id_solicitacao=?";
        try(Connection cnn = ConnectionFactory.getConnection();
        PreparedStatement stmt = cnn.prepareStatement(sqlAtualizarStatus);){
            stmt.setString(1, StatusSolicitacao.APROVADO.toString());
            stmt.setInt(2, id_solicitacao);

            stmt.executeUpdate();
        }
    }

    public void atualizarResgateAprovado(int id_solicitacao) throws SQLException {
        String sqlAtualizarStatus = "UPDATE Solicitacao SET status_solicitacao=? WHERE id_solicitacao=?";
        try(Connection cnn = ConnectionFactory.getConnection();
            PreparedStatement stmt = cnn.prepareStatement(sqlAtualizarStatus);){
            stmt.setString(1, StatusSolicitacao.APROVADO.toString());
            stmt.setInt(2, id_solicitacao);

            stmt.executeUpdate();
        }

    }
    public void atualizarAdocaoEmAndamento(int id_solicitacao) throws SQLException {
        String sqlAtualizarStatus = "UPDATE Solicitacao SET status_solicitacao=? WHERE id_solicitacao=?";
        try(Connection cnn = ConnectionFactory.getConnection();
            PreparedStatement stmt = cnn.prepareStatement(sqlAtualizarStatus);){
            stmt.setString(1, StatusSolicitacao.EM_ANDAMENTO.toString());
            stmt.setInt(2, id_solicitacao);

            stmt.executeUpdate();
        }
    }
    public void atualizarAdocaoReprovado(int id_solicitacao) throws SQLException {
        String sqlAtualizarStatus = "UPDATE Solicitacao SET status_solicitacao=? WHERE id_solicitacao=?";
        try(Connection cnn = ConnectionFactory.getConnection();
            PreparedStatement stmt = cnn.prepareStatement(sqlAtualizarStatus);){
            stmt.setString(1, StatusSolicitacao.REPROVADO.toString());
            stmt.setInt(2, id_solicitacao);

            stmt.executeUpdate();
        }
    }
    public void atualizarFeedBackAdmin(String feedBackAdmin, int id_solicitacao) throws SQLException {
        String sqlAtualizarStatus = "UPDATE Solicitacao SET feedBackAdmin=? WHERE id_solicitacao=?";
        try(Connection cnn = ConnectionFactory.getConnection();
            PreparedStatement stmt = cnn.prepareStatement(sqlAtualizarStatus);){
            stmt.setString(1, feedBackAdmin);
            stmt.setInt(2, id_solicitacao);

            stmt.executeUpdate();
        }
    }


    public SolicitacaoAdocao solicitacaoAdocaoId(int id_solicitacao){
        String sqlBuscarSolicitacao = "SELECT s.*, s1.id_animal FROM Solicitacao s JOIN SolicitacaoAdocao s1 ON s.id_solicitacao=s1.id_solicitacao WHERE s.id_solicitacao=?";
        try(Connection cnn = ConnectionFactory.getConnection();
        PreparedStatement stmt = cnn.prepareStatement(sqlBuscarSolicitacao);){
            stmt.setInt(1, id_solicitacao);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                StatusSolicitacao statusSolicitacao = StatusSolicitacao.valueOf(rs.getString("status_solicitacao"));
                Integer id_animal = rs.getInt("id_animal");
                String feedback_admin = rs.getString("feedBackAdmin");
                String cpf_solicitante = rs.getString("cpf_solicitante");

                SolicitacaoAdocao solicitacaoAdocao = new SolicitacaoAdocao(cpf_solicitante, id_animal);
                solicitacaoAdocao.setId(id_solicitacao);
                solicitacaoAdocao.setStatus(statusSolicitacao);
                solicitacaoAdocao.setFeedbackAdmin(feedback_admin);

                return solicitacaoAdocao;

            }
        }catch( Exception e ){
            System.out.println("Erro ao tentar buscar solicitacao por ID" + e.getMessage() );
        }
        return null;
    }

    public void apagarSolicitacaoByCpf(String cpf_solicitante){
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sqlAdocao = "DELET FROM SolicitacaoAdocao WHERE id_solicitacao=?";
        String sqlResgate = "DELET FROM SolicitacaoResgate WHERE id_solicitacao=?";
        String sqlSolicitacao = "DELETE FROM Solicitacao WHERE id_solicitacao=?";

        try {
            solicitacoes = this.listarSolicitacoesAdocaoCpf(cpf_solicitante);
        }catch(SQLException e){
            System.out.println("Erro ao tentar buscar solicitacao por Cpf" + e.getMessage() );
        }
        for(Solicitacao solicitacao : solicitacoes){
            int id = solicitacao.getId();

            // SOLICITAÇÕES ADOCOES
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sqlAdocao);){
                ps.setInt(1, id);
                ps.executeUpdate();
            }catch(SQLException e){
                System.out.println("Erro ao deletar Solicitação de adocao: " + e.getMessage());
            }

            // SOLICITAÇÕES RESGATE
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sqlResgate);){
                ps.setInt(1, id);
                ps.executeUpdate();
            }catch(SQLException e){
                System.out.println("Erro ao deletar Solicitação de Resgate: " + e.getMessage());
            }

            // SOLICITACOES PRINCIPAl
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sqlSolicitacao);){
                ps.setInt(1, id);
                ps.executeUpdate();
            }catch(SQLException e){
                System.out.println("Erro ao deletar Solicitação: " + e.getMessage());
            }
        }
        System.out.println("Solicitacões apagadas com sucesso!");
    }

}
