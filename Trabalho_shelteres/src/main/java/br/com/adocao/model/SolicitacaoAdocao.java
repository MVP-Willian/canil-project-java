package br.com.adocao.model;

import br.com.adocao.persistence.AnimalDAO;
import br.com.adocao.persistence.UserDAO;

/**
 * Representa uma solicitação específica para a adoção de um animal.
 * Herda de Solicitacao e adiciona o animal que está sendo solicitado.
 *
 * @see Solicitacao
 * @see Animal
 */
public class SolicitacaoAdocao extends Solicitacao {

    private Integer id_animal;

    /**
     * Cria uma nova solicitação de adoção.
     *
     * @param u O usuário que deseja adotar.
     * @param a O animal que está sendo solicitado para adoção.
     */
    public SolicitacaoAdocao( String cpfSolicitante, Integer id_animal){
        super(cpfSolicitante);
        this.id_animal = id_animal;
    }

    /**
     * Obtém o animal associado a esta solicitação de adoção.
     * @return O objeto Animal.
     */
    public Integer getIdAnimal() { return id_animal; }

    /**
     * Gera um resumo formatado da solicitação de adoção.
     * Implementa o metodo abstrato da classe Solicitacao.
     *
     * @return Uma String formatada com detalhes da adoção.
     */
    @Override
    public String resumo() {
        AnimalDAO animalDAO = new AnimalDAO();
        UserDAO userDAO = new UserDAO();
        Animal animal = animalDAO.getAnimalPorId(this.id_animal);
        User user = userDAO.getUserCpf(getSolicitante());

        return "Adoção do animal: " + animal.getNome() +
                " | Usuário: " + user.getNome() +
                " | Status: " + getStatus() +
                " | Justificativa: " + getFeedbackAdmin();
    }
    public TipoSolicitacao getTipoSolicitacao() {
        return TipoSolicitacao.ADOCAO;
    }
}