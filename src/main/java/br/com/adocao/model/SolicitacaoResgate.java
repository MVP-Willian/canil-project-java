package br.com.adocao.model;

import br.com.adocao.persistence.UserDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Representa uma solicitação específica para o resgate de um animal.
 * Herda de Solicitacao e adiciona informações sobre o animal
 * a ser resgatado (espécie, local, descrição).
 *
 * @see Solicitacao
 */
public class SolicitacaoResgate extends Solicitacao {

    private String especie;
    private String sexo;
    private String local;
    private String descricao;

    /**
     * Cria uma solicitação de resgate.
     *
     * @param user O usuário que está reportando o animal.
     * @param especie A espécie do animal (ex: "Cachorro", "Gato").
     * @param sexo O sexo do animal (ex: "Macho", "Fêmea", "Não sabe").
     * @param local A localização onde o animal foi visto.
     * @param descricao Uma descrição da situação ou condição do animal.
     */
    public SolicitacaoResgate(String cpfSolicitante, String especie, String sexo,
                              String local, String descricao){
        super(cpfSolicitante);
        this.especie = especie;
        this.sexo = sexo;
        this.local = local;
        this.descricao = descricao;
    }


    //getters and setters
    public String getEspecie() {
        return especie;
    }
    public void setEspecie(String especie) {
        this.especie = especie;
    }
    public String getSexo() {
        return sexo;
    }
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    public String getLocal() {
        return local;
    }
    public void setLocal(String local) {
        this.local = local;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    /**
     * Gera um resumo formatado da solicitação de resgate.
     * Implementa o metodo abstrato da classe Solicitacao.
     *
     * @return Uma String formatada com detalhes do resgate.
     */
    public String getCpfSolicitante() {
        return getSolicitante();
    }
    @Override
    public String resumo(){
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserCpf(getSolicitante());

        return "Resgate em: " + local +
                " | Descricao: " + descricao +
                " | Usuário: " + user.getNome() +
                " | Status: " + getStatus() +
                " | Justificativa: " + getFeedbackAdmin();
    }
    public TipoSolicitacao getTipoSolicitacao(){
        return TipoSolicitacao.RESGATE;
    }
}