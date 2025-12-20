package br.com.adocao.model;

/**
 * Representa um animal no sistema de adoção e resgate.
 * Esta classe armazena todas as características físicas, histórico
 * e estado atual (situação) do animal.
 */
public class Animal {
    private Integer id;
    private String nome;
    private int idade;
    private String especie; //gato ou cachorro
    private String sexo;
    private String porte; //pequeno, médio ou grande
    private float peso;
    private String personalidade;
    private String historico;
    private String localEncontrado;
    private String situacao; //solicitAdocao, adotado, recuperacao

    /**
     * Construtor completo para criar um novo objeto Animal.
     *
     * @param id O identificador único do animal.
     * @param nome O nome do animal.
     * @param idade A idade do animal (em anos).
     * @param especie A espécie (ex: "Cachorro", "Gato").
     * @param sexo O sexo do animal (ex: "M", "F").
     * @param personalidade Descrição da personalidade (ex: "Dócil", "Agitado").
     * @param historico Histórico médico ou de vida do animal.
     * @param localEncontrado Onde o animal foi encontrado/resgatado.
     * @param situacao A situação atual (ex: "Disponível", "Adotado", "Solicitado").
     * @param porte O porte do animal (ex: "Pequeno", "Médio", "Grande").
     * @param peso O peso do animal em kg.
     */
    public Animal(Integer id, String nome, int idade, String especie, String sexo, String personalidade, String historico, String localEncontrado, String situacao, String porte, float peso){
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.especie = especie;
        this.sexo = sexo;
        this.porte = porte;
        this.peso = peso;
        this.personalidade = personalidade;
        this.historico = historico;
        this.localEncontrado = localEncontrado;
        this.situacao = situacao;
    }

    // --- Getters ---

    /**
     * Obtém o ID do animal.
     * @return O ID único.
     */
    public int getId() { return id; }

    /**
     * Obtém o nome do animal.
     * @return O nome.
     */
    public String getNome(){ return nome; }

    /**
     * Obtém a idade do animal.
     * @return A idade em anos.
     */
    public int getIdade() { return idade; }

    /**
     * Obtém a espécie do animal.
     * @return A espécie (ex: "Cachorro").
     */
    public String getEspecie(){ return especie; }

    /**
     * Obtém o sexo do animal.
     * @return O sexo (ex: "M", "F").
     */
    public String getSexo(){ return sexo; }

    /**
     * Obtém o porte do animal.
     * @return O porte (ex: "Pequeno", "Médio").
     */
    public String getPorte(){ return porte; }

    /**
     * Obtém o peso do animal.
     * @return O peso em kg.
     */
    public float getPeso(){ return peso; }

    /**
     * Obtém a descrição da personalidade do animal.
     * @return A descrição da personalidade.
     */
    public String getPersonalidade() { return personalidade; }

    /**
     * Obtém o histórico do animal.
     * @return O histórico médico/de vida.
     */
    public String getHistorico(){ return historico; }

    /**
     * Obtém o local onde o animal foi encontrado.
     * @return O local de resgate.
     */
    public String getLocalEncontrado() { return localEncontrado; }

    /**
     * Obtém a situação atual do animal no sistema.
     * @return A situação (ex: "Disponível", "Adotado").
     */
    public String getSituacao() { return situacao; }

    public String getStatus() { return situacao; }

    // --- Setters ---
    // Setters apenas para atributos seguros(que nao dependem de outras classes)

    /**
     * Define o ID do animal.
     * @param id O novo ID.
     */
    public void setId(int id){ this.id = id; }

    /**
     * Define o nome do animal.
     * @param nome O novo nome.
     */
    public void setNome(String nome){ this.nome = nome; }

    /**
     * Define a espécie do animal.
     * @param especie A nova espécie.
     */
    public void setEspecie(String especie){ this.especie = especie; }

    /**
     * Define o sexo do animal.
     * @param sexo O novo sexo.
     */
    public void setSexo(String sexo){ this.sexo = sexo; }

    /**
     * Define o porte do animal.
     * @param porte O novo porte.
     */
    public void setPorte(String porte){ this.porte = porte; }

    /**
     * Define o peso do animal.
     * @param peso O novo peso.
     */
    public void setPeso(float peso){ this.peso = peso; }

    /**
     * Define a idade do animal.
     * @param idade A nova idade.
     */
    public void setIdade(int idade){ this.idade = idade; }

    /**
     * Define a personalidade do animal.
     * @param personalidade A nova descrição de personalidade.
     */
    public void setPersonalidade(String personalidade){ this.personalidade = personalidade; }

    /**
     * Define o histórico do animal.
     * @param historico O novo histórico.
     */
    public void setHistorico(String historico){ this.historico = historico; }

    /**
     * Define o local onde o animal foi encontrado.
     * @param localEncontrado O novo local.
     */
    public void setLocalEncontrado(String localEncontrado){ this.localEncontrado = localEncontrado; }


    public void setSituacao(String situacao){ this.situacao = situacao; }
    /**
     * Sobrescreve o método toString padrão para fornecer uma
     * representação em String mais útil do objeto Animal.
     * Usado por {@code System.out.println(animal)}.
     *
     * @return Uma String formatada com nome, espécie, idade e situação.
     */
    @Override
    public String toString(){
        return id + " - " + nome + " - " + especie + " - " + idade + " anos - " + situacao;
    }
}