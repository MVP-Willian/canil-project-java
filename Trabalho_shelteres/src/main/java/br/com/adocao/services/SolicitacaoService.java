package br.com.adocao.services;


import java.sql.SQLException;
import java.util.*;
import br.com.adocao.model.*;
import br.com.adocao.persistence.AnimalDAO;
import br.com.adocao.persistence.SolicitacaoDAO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;



/**
 * Classe de serviço (Service) que gerencia a lógica de negócios
 * relacionada às solicitações.
 * Atua como um repositório central para criar, listar e atualizar
 * o status das solicitações.
 */
public class SolicitacaoService {

    private List<Solicitacao> solicitacoes = new ArrayList<>();

    /**
     * Adiciona uma nova solicitação (de qualquer tipo) ao sistema.
     *
     * @param s A solicitação a ser registrada (pode ser SolicitacaoAdocao
     * ou SolicitacaoResgate).
     */
    public void registrarSolicitacao(Solicitacao s){
        solicitacoes.add(s);
    }

    /**
     * Retorna uma lista de todas as solicitações registradas no sistema.
     *
     * @return Uma lista (List) de objetos Solicitacao.
     */
    public List<Solicitacao> listarSolicitacoes() {
        return solicitacoes;
    }

    /**
     * Filtra e retorna todas as solicitações feitas por um usuário específico.
     *
     * @param u O usuário cujas solicitações devem ser encontradas.
     * @return Uma lista de solicitações filtrada por usuário.
     */
    public List<Solicitacao> listarPorUsuario(User u){
        return solicitacoes.stream()
                .filter(solicitacao -> solicitacao.getSolicitante().equals(u))
                .toList();
    }

    /**
     * Altera o status de uma solicitação para APROVADO, com base no ID.
     *
     * @param id O ID da solicitação a ser aprovada.
     */
    public int aprovarResgate(int id){
        try {
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
            try {
                solicitacaoDAO.atualizarResgateAprovado(id);
                return 1;
            } catch (SQLException ex) {
                return 0;
            }
        }
        catch (Exception e){
            System.out.println("Solicitação " + id + " NÃO encontrada!");
            return 0;
        }
    }

    /**
     * Altera o status de uma solicitação para REPROVADO, com base no ID.
     *
     * @param id O ID da solicitação a ser reprovada.
     */
    public void reprovarAdocao(int id){
        try{
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
            solicitacaoDAO.atualizarAdocaoReprovado(id);
            System.out.println("Descreva a justificativa da reprovação:");

            Scanner sc = new Scanner(System.in);
            String justificativa = sc.nextLine();
            solicitacaoDAO.atualizarFeedBackAdmin(justificativa, id);
        }
        catch (Exception e){
            System.out.println("Erro ao reprovar Solicitação!");
        }
    }

    public void apagarSolicitacao(int id) {
        boolean removido = solicitacoes.removeIf(s -> s.getId() == id);

        if(removido){
            System.out.println("Solicitação removida com sucesso!");
        } else {
            System.out.println("Solicitação não encontrada.");
        }
    }

    public void aprovarAdocao(int id){
        try {
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
            solicitacaoDAO.atualizarAdocaoAprovado(id);
            SolicitacaoAdocao solicitacaoAdocao = solicitacaoDAO.solicitacaoAdocaoId(id);
            AnimalDAO animalDAO = new AnimalDAO();

            Animal animal = animalDAO.getAnimalPorId(solicitacaoAdocao.getIdAnimal());
            animalDAO.marcarComoAdotado(animal.getId());

            LocalDate hoje = LocalDate.now();
            String formatada = hoje.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            animalDAO.inserirAnimalAdotado(animal.getId(), solicitacaoAdocao.getSolicitante(), formatada);

        } catch (Exception e){
            System.out.println("Erro ao tentar aprovar uma solicitação!");
        }
    }
    public int andamentoSolicitacao(int id){
        try {

            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
            SolicitacaoAdocao solicitacaoAdocao = solicitacaoDAO.solicitacaoAdocaoId(id);
            if(solicitacaoAdocao.getStatus() != StatusSolicitacao.APROVADO){
                solicitacaoDAO.atualizarAdocaoEmAndamento(id);
                return 1;
            }
            else{
                System.out.println("Animal já foi adotado");
                return 0;
            }

        } catch (Exception e){
            System.out.println("Erro ao tentar iniciar processo de solicitação!");
        }
        return 0;
    }

    public void pendenteSolicitacao(int id){
        try{
            solicitacoes.stream()
                    .filter(s -> s.getId() == id &&  s instanceof Solicitacao)
                    .findFirst()
                    .ifPresent(s -> s.setStatus(StatusSolicitacao.PENDENTE));
        } catch (Exception e){
            System.out.println("Erro ao tentar mudar solicitação para pendente!");
        }
    }
}