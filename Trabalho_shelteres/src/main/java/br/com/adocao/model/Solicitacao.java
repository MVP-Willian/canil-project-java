package br.com.adocao.model;

/**
 * Classe abstrata que serve como modelo base para todos os tipos de solicitações.
 * Ela define atributos e comportamentos comuns, como ID, solicitante e status,
 * e um método de cancelamento.
 * <p>
 * Esta classe não pode ser instanciada diretamente.
 *
 * @see SolicitacaoAdocao
 * @see SolicitacaoResgate
 */
public abstract class Solicitacao {

    private static int contador = 1;
    private int idSolicitacao;
    private String cpfSolicitacao;
    private StatusSolicitacao status; //pendente, recusada, cancelada, aprovada
    private String feedbackAdmin;
    /**
     * Construtor base para todas as solicitações.
     * Atribui um ID único incremental e define o status inicial como PENDENTE.
     *
     * @param solicitante O usuário que abriu a solicitação.
     */
    public Solicitacao(String cpfSolicitante){
        this.cpfSolicitacao = cpfSolicitante;
        this.status = StatusSolicitacao.PENDENTE;
        this.feedbackAdmin = "Nenhum comentário";
    }

    // --- Getters e Setters ---

    /**
     * Obtém o ID único da solicitação.
     * @return O ID da solicitação.
     */
    public int getId() { return idSolicitacao; }

    /**
     * Obtém o usuário que criou a solicitação.
     * @return O objeto User do solicitante.
     */
    public String getSolicitante() { return cpfSolicitacao; }

    /**
     * Obtém o status atual da solicitação.
     * @return O enum StatusSolicitacao (PENDENTE, APROVADO, etc.).
     */
    public StatusSolicitacao getStatus() { return status; }

    public String getFeedbackAdmin() { return feedbackAdmin; }
    /**
     * Define o status da solicitação (usado por Admins ou pelo sistema).
     * @param status O novo StatusSolicitacao.
     */
    public void setId(int idSolicitacao) { this.idSolicitacao = idSolicitacao; }

    public void setStatus(StatusSolicitacao status) { this.status = status; }

    public void setFeedbackAdmin(String feedbackAdmin) { this.feedbackAdmin = feedbackAdmin; }


    public Integer getIdAnimal() {
        return null; // padrão
    }

    public String getEspecie() {
        return null;
    }
    public String getSexo() {
        return null;
    }
    public String getLocal() {
        return null;
    }
    public String getDescricao() {
        return null;
    }

    // --- Métodos ---

    /**
     * Permite ao usuário cancelar uma solicitação.
     * A solicitação só pode ser cancelada se ainda estiver PENDENTE.
     */
    public void cancelar(){
        if(status == StatusSolicitacao.PENDENTE){
            status = StatusSolicitacao.CANCELADA;
        }
    }

    /**
     * Método abstrato que força as subclasses a fornecerem um resumo.
     * Deve retornar uma breve descrição formatada da solicitação.
     *
     * @return Uma String com o resumo da solicitação.
     */
    public abstract String resumo();

    public abstract TipoSolicitacao getTipoSolicitacao ();
}