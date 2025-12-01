package br.com.adocao.model;

/**
 * Representa um Administrador do sistema.
 * A classe Admin herda de User, possuindo todos os atributos e métodos
 * de um usuário comum, mas com privilégios adicionais para gerenciar
 * o sistema, como aprovar ou recusar solicitações de adoção.
 *
 * @see User
 */
public class Admin extends User {

    private boolean ativa = false;

    public String getTipoConta(){ return "Admin"; }
    /**
     * Construtor para criar um novo Administrador.
     * Utiliza o construtor da superclasse User.
     *
     * @param nome O nome completo do admin.
     * @param email O email do admin (usado para login).
     * @param cpf O CPF do admin.
     * @param senha A senha de acesso do admin.
     * @param renda A renda mensal do admin (herdado de User).
     */
    public Admin(String nome, String email, String cpf, String senha, float renda, boolean statusConta){
        super(nome, email, cpf, senha, renda, statusConta);
    }

    /**
     * Verifica se a conta do administrador está ativa.
     * @return true se o admin estiver ativo, false caso contrário.
     */
    public boolean isAtiva(){ return ativa; }

    /**
     * Ativa a conta do administrador.
     * (Usado para um admin aprovar outro admin, por exemplo).
     */
    public void aprovar(){
        this.ativa = true;
    }

    /**
     * Desativa a conta do administrador.
     */
    public void reprovar(){
        this.ativa = false;
    }

    /**
     * Aprova uma solicitação de adoção, mudando seu status.
     * O admin precisa estar com a conta 'ativa' para realizar esta ação.
     *
     * @param solicitacao A solicitação a ser aprovada.
     */
    public void aprovarSolicitacaoAdocao(Solicitacao solicitacao){
        if(this.ativa == true){
            solicitacao.setStatus(StatusSolicitacao.APROVADO);
            System.out.println("Adocao aprovada pelo ADM.");
        }
        else{
            System.out.println("ADM não aprovado ainda. Não pode aprovar solicitações.");
        }
    }

    /**
     * Recusa uma solicitação de adoção, mudando seu status.
     * O admin precisa estar com a conta 'ativa' para realizar esta ação.
     *
     * @param solicitacao A solicitação a ser recusada.
     */
    public void recusarSolicitacao(Solicitacao solicitacao){
        if(this.ativa == true){
            solicitacao.setStatus(StatusSolicitacao.REPROVADO);
            System.out.println("Solicitação recusada pelo ADM.");
        }
        else {
            System.out.println("ADM não aprovado ainda. Não pode reprovar solicitações.");
        }
    }
}