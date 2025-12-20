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

}