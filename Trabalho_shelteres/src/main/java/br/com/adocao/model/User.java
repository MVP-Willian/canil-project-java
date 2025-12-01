package br.com.adocao.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Representa um usuário (adotante) no sistema.
 * Esta classe armazena informações pessoais e gerencia as ações
 * que um usuário comum pode realizar, como editar seu perfil e
 * gerenciar sua conta.
 */
public class User {

    //Atributos
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private float renda;
    private boolean statusConta = false;

    /**
     * Construtor para criar um novo usuário.
     * Ao ser criado, o status da conta é definido como ativo (true)
     * e a lista de animais adotados é inicializada.
     *
     * @param nome O nome completo do usuário.
     * @param email O email do usuário (usado para login).
     * @param cpf O CPF do usuário.
     * @param senha A senha de acesso do usuário.
     * @param renda A renda mensal do usuário.
     */
    public User(String nome, String email, String cpf, String senha, float renda, boolean statusConta) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.renda = renda;
        this.statusConta = statusConta;
    }

    // --- Getters e Setters ---

    /**
     * Obtém o nome do usuário.
     * @return O nome do usuário.
     */
    public String getNome(){ return nome; }

    /**
     * Define o nome do usuário.
     * @param nome O novo nome do usuário.
     */
    public void setNome(String nome){ this.nome = nome; }

    /**
     * Obtém o CPF do usuário.
     * @return O CPF do usuário.
     */
    public String getCpf() { return cpf; }

    /**
     * Define o CPF do usuário.
     * @param cpf O novo CPF do usuário.
     */
    public void setCpf(String cpf){ this.cpf = cpf; }

    /**
     * Obtém a renda do usuário.
     * @return A renda do usuário.
     */
    public float getRenda() { return renda; }

    /**
     * Define a renda do usuário.
     * @param renda A nova renda do usuário.
     */
    public void setRenda(float renda){ this.renda = renda; }

    /**
     * Obtém a senha do usuário.
     * @return A senha (hash ou texto) do usuário.
     */
    public String getSenha(){ return senha; }

    /**
     * Obtém o email do usuário.
     * @return O email do usuário.
     */
    public String getEmail(){ return email; }

    /**
     * Define o email do usuário.
     * @param email O novo email do usuário.
     */
    public void setEmail(String email){ this.email = email; }

    /**
     * Obtém a lista de animais adotados pelo usuário.
     * @return Uma lista de objetos Animal.
     */
    public Boolean getStatusConta(){ return this.statusConta; }


    public void setStatusConta(boolean statusConta){ this.statusConta = statusConta; }


    public String getTipoConta(){ return "User"; }
    // --- MÉTODOS ---

    /**
     * Permite ao usuário editar suas informações de perfil.
     * A alteração só é permitida se a senha atual fornecida for correta.
     *
     * @param senhaAtual A senha atual do usuário, para verificação.
     * @param nome O novo nome a ser atualizado.
     * @param cpf O novo CPF a ser atualizado.
     * @param renda A nova renda a ser atualizada.
     * @param email O novo email a ser atualizado.
     */
    public void editarPerfil(String senhaAtual, String nome, String cpf, float renda, String email){
        if(this.senha.equals(senhaAtual)){
            this.nome = nome;
            this.cpf = cpf;
            this.renda = renda;
            this.email = email;
        }
    }

    /**
     * Altera a senha do usuário.
     * A alteração só é permitida se a senha atual fornecida for correta.
     *
     * @param senhaAtual A senha atual do usuário, para verificação.
     * @param senhaNova A nova senha que substituirá a atual.
     */
    public void mudarSenha(String senhaAtual, String senhaNova){
        if (this.senha.equals(senhaAtual)) {
            this.senha = senhaNova;
        }
    }

    /**
     * Desativa a conta do usuário.
     * Altera o status da conta para 'false'.
     */
    public void apagarConta(){
        this.statusConta = false;
    }

    @Override
    public String toString(){
        return nome + " - " +  cpf + " - " + email + " - " + senha + " - ";
    }
}