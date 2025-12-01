package br.com.adocao;

import br.com.adocao.model.*;
import br.com.adocao.persistence.*;
import br.com.adocao.services.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Classe principal que inicia a aplicação de linha de comando (CLI).
 * Gerencia o fluxo de menus, estado de login (usuário/admin), e
 * interações do usuário com os serviços do sistema.
 * <p>
 * Esta classe utiliza listas em memória para simular um banco de dados.
 */
public class Main {
    // ======================================================== Variáveis Estáticas Globais ==========================================

    /** Scanner global para ler a entrada do usuário em todo o sistema. */
    private static Scanner sc = new Scanner(System.in);

    /** Lista em memória que simula a tabela de usuários comuns. */
    private static List<User> usuarios = new ArrayList<>();

    /** Lista em memória que simula a tabela de administradores. */
    private static List<Admin> admins = new ArrayList<>();

    //** Objeto intermediário que será usado para conectar os dados em memória para o banco de dados. */
    private static AnimalDAO animalDAO = new AnimalDAO();

    //** Objeto intermediário que será usado para conectar os dados em memória para o banco de dados. */
    private static UserDAO userDAO = new UserDAO();

    //** Objeto intermediário que será usado para conectar os dados em memória para o banco de dados. */
    private static SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();

    /** Lista em memória que simula a tabela de animais. (Classe Animal não fornecida, mas presumida) */
    private static List<Animal> animais = new ArrayList<>();

    /** Instância do serviço que gerencia a lógica de negócios das solicitações. */
    private static SolicitacaoService solicitacoes = new SolicitacaoService();

    /** Armazena o usuário que está logado no momento. 'null' se ninguém estiver logado. */
    private static User usuarioLogado = null;

    /** Armazena o admin que está logado no momento. 'null' se nenhum admin estiver logado. */
    private static Admin adminLogado = null;



    // ========================================== MAIN PRINCIPAL ========================================================
    /**
     * Ponto de entrada (entry point) da aplicação.
     *
     * @param args Argumentos da linha de comando (não utilizados neste projeto).
     */
    public static void main(String[] args) {
        // 1. Inicializa o banco de dados - caso ja esteja criado ele apenas inicializa a conexão caso não ele cria o arquivo e as relações.
        DatabaseInitializer.inicializar();

        testarAnimalDAO();

        // 2. Preenche o sistema com dados de exemplo para teste.
        carregarDadosIniciais();
        // 3. Inicia o loop infinito do menu principal.
        menuPrincipal();
        limparBancoAnimais();
    }

    // ========================================== MÉTODOS PREVISÓRIOS ========================================================
    public static void testarAnimalDAO(){
        System.out.println(" ====== TESTE NO BANCO DE DADOS (AnimalDAO) ======");

        try {

            Animal animal = new Animal(434, "trufinha", 4, "vira-lata", "M",
                    "Possui um rabo cortado", "Ainda nao foi vacinado",
                    "-3.0702569,-59.9534721", "Disponível", "grande", 35);

            Admin michael = new Admin("michael", "michael@gmail.com",
                    "21412453412", "1234", 3600.0f, true);
            // Adiciona um usuário padrão
            User user = new User("user", "user@gmail.com",
                    "02423459302", "1234", 15000.0f, true);


            userDAO.inserir(user);
            userDAO.inserir(michael);

            animalDAO.inserir(animal);

            List<Animal> animaisBanco = animalDAO.listarTodos();
            List<User> usuariosBanco = userDAO.listarTodos();

            System.out.println("\nAnimais vindo do BANCO:");
            for (Animal a : animaisBanco) {
                System.out.println(a);
            }

            System.out.println("\nUser vindo do BANCO:");
            for (User u : usuariosBanco) {
                System.out.println(u + u.getTipoConta());
            }

        } catch (Exception e) {
            System.out.println("Erro ao acessar o banco de dados:");
            e.printStackTrace();
        }

    }

    private static void limparBancoAnimais() {
        System.out.println("Tem certeza que deseja APAGAR TODOS os animais do banco? (s/n)");
        String op = sc.nextLine();

        if(op.equalsIgnoreCase("s")) {
            try {
                animalDAO.limparTabela();
                solicitacaoDAO.limparTabela();
                System.out.println("✅ Banco de animais limpo com sucesso!");
            } catch (Exception e) {
                System.out.println("❌ Erro ao limpar banco: " + e.getMessage());
            }
        } else {
            System.out.println("Operação cancelada.");
        }
    }


    /**
     * Popula as listas em memória com dados de exemplo (mock data).
     * Isso permite testar a aplicação sem um banco de dados real.
     */
    private static void carregarDadosIniciais() {
        // Adiciona um admin padrão
        admins.add(new Admin("Admin1", "user@gmail.com",
                "21412453412", "1234", 3600.0f, true));
        // Adiciona um usuário padrão
        usuarios.add(new User("Michael", "michael@gmail.com",
                "02423459302", "1234", 15000.0f, true));
        // Adiciona animais de exemplo
        animais.add(new Animal(434, "trufinha", 4, "vira-lata", "M",
                "Possui um rabo cortado", "Ainda nao foi vacinado",
                "-3.0702569,-59.9534721", "Disponível", "grande", 35));
        animais.add(new Animal(521, "leoleo", 4, "chouchou", "F",
                "Cachorro não é dócil", "Orelha está machucada",
                "-3.0702569,-59.9534721", "Solicitado", "pequeno", 35));
    }

    /**
     * Exibe o menu principal e gerencia o loop de interação com o usuário.
     * O menu exibido se adapta dinamicamente com base no estado de login
     * (usuário comum, admin, ou deslogado).
     */





    // ======================================================== fluxo main principal ========================================================
    private static void menuPrincipal(){
        // Loop infinito para manter o menu ativo
        while(true){
            System.out.println("\n=============== Sistema de Adoção e Resgate =====================");
            System.out.println("Animais para adoção:");

            AnimalDAO animalDAO = new AnimalDAO();
            try {
                animais = animalDAO.listarTodos();
            } catch (Exception e) {
                System.out.println("Erro ao acessar o banco de dados para ver os animais salvos:");
            }

            // Usando Stream API para filtrar e exibir animais "Disponíveis"
            animais.stream()
                    .filter(a -> a.getSituacao().equalsIgnoreCase("Disponível"))
                    .forEach(System.out::println); // Imprime o toString() de cada animal


            System.out.println("\nOpções:");

            // Bloco 1: Define qual menu exibir com base no login
            if(usuarioLogado == null && adminLogado == null) {
                // Menu para usuário DESLOGADO
                System.out.println("1. Quero adotar um animal.");
                System.out.println("2. Quero registrar um resgate");
                System.out.println("3. Login");
                System.out.println("4. Cadastrar conta");
                System.out.println("5. Sair");
            } else if(adminLogado != null) { // Verifica se é um admin (presumindo que adminLogado é um Admin)
                // Menu para ADMIN LOGADO
                System.out.println("1. Verificar todas as solicitações.");
                System.out.println("2. Verificar todos os animais");
                System.out.println("3. Adicionar um animal.");
                System.out.println("4. Adicionar um resgate.");
                System.out.println("5. Remover um animal.");
                System.out.println("6. Logout");
                System.out.println("7. Sair");

            } else {
                // Menu para USUÁRIO COMUM LOGADO
                System.out.println("1. Quero adotar um animal.");
                System.out.println("2. Quero registrar um resgate.");
                System.out.println("3. Ver minhas solicitações");
                System.out.println("4. Cancelar solicitação.");
                System.out.println("5. Logout");
                System.out.println("6. Sair");
            }

            System.out.println("Escolha: ");
            int opcao = sc.nextInt();
            sc.nextLine(); // Consome a quebra de linha (Enter) pendente do nextInt()
            System.out.println("============================================================");

            // Bloco 2: Processa a escolha do usuário
            if(usuarioLogado == null && adminLogado == null) {
                // Ações para usuário DESLOGADO
                switch (opcao) {
                    case 1 -> fluxoAdocao();
                    case 2 -> fluxoResgate();
                    case 3 -> login();
                    case 4 -> cadastrarUsuario();
                    case 5 -> { System.out.println("Saindo..."); return; } // 'return' sai do método e encerra o loop
                    default -> System.out.println("Opção inválida!");
                }
            } else if(adminLogado != null) {
                // Ações para ADMIN LOGADO
                switch (opcao) {
                    // TODO: Implementar métodos de admin
                    case 1 -> verificarSolicitacoes(); //verificarSolicitacoes();
                    case 2 -> verificarAnimal(); //verificarAnimais();
                    case 3 -> adicionarAnimal();
                    case 4 -> fluxoResgate(); // Admin também pode registrar resgate
                    case 5 -> removerAnimal();
                    case 6 -> { adminLogado = null; System.out.println("Logout realizado."); }
                    case 7 -> { System.out.println("Saindo..."); return; }
                    default -> System.out.println("Opção inválida!");
                }
            } else {
                // Ações para USUÁRIO COMUM LOGADO
                switch (opcao) {
                    case 1 -> fluxoAdocao();
                    case 2 -> fluxoResgate();
                    case 3 -> verSolicitacoes();
                    case 4 -> fluxoCancelarSolicitacao();
                    case 5 -> { usuarioLogado = null; System.out.println("Logout realizado."); }
                    case 6 -> { System.out.println("Saindo..."); return; }
                    default -> System.out.println("Opção inválida!");
                }
            }
        }
    }


    // ======================================================== FLUXOS QUE COMPÕEM A MAIN PRINCIPAL ========================================================


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ FLUXOS USUÁRIO COMUM ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Controla o fluxo de usuário para registrar uma nova solicitação de adoção.
     * Se o usuário não estiver logado, chama {@link #autenticarUsuario()} primeiro.
     * Lista os animais disponíveis e registra a solicitação.
     */
    private static void fluxoAdocao(){
        // Passo 1: Verificar se o usuário está logado
        if(usuarioLogado == null){
            autenticarUsuario(); // Tenta logar ou cadastrar

            // Se mesmo após a tentativa, o usuário ainda for nulo (ex: cancelou), aborta o fluxo.
            if(usuarioLogado == null){
                System.out.println("Não foi possível logar ou registrar o usuário! Voltando ao menu.");
                return;
            }
        }

        // Passo 2: Listar animais disponíveis para adoção
        System.out.println("\nEscolha o animal pelo índice:");
        for(int i = 0; i < animais.size(); i++){
            // Mostra apenas os animais "Disponíveis"
            if(animais.get(i).getSituacao().equalsIgnoreCase("Disponível")){
                System.out.println(i + " - " + animais.get(i).getNome());
            }
        }

        // Passo 3: Ler a escolha e criar a solicitação
        int indice = sc.nextInt();
        sc.nextLine(); // Consome o \n

        // Valida se o índice está dentro dos limites da lista
        if(indice >= 0 && indice < animais.size()){
            Animal escolhido = animais.get(indice);

            // Cria a solicitação específica de adoção
            SolicitacaoAdocao solicitacaoAdocao = new SolicitacaoAdocao(usuarioLogado.getCpf(), escolhido.getId());

            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
            solicitacaoDAO.inserir(solicitacaoAdocao);

            // Registra a solicitação no serviço
            solicitacoes.registrarSolicitacao(solicitacaoAdocao);
            System.out.println("Solicitação registrada com sucesso!");
        } else{
            System.out.println("Índice inválido!");
        }
    }

    /**
     * Controla o fluxo de usuário para registrar uma nova solicitação de resgate.
     * Se o usuário não estiver logado, força a autenticação.
     * Coleta dados do animal a ser resgatado e registra a solicitação.
     */
    private static void fluxoResgate(){
        // Passo 1: Verificar login
        if(usuarioLogado == null){
            autenticarUsuario();
            // Se a autenticação falhar, aborta.
            if(usuarioLogado == null){
                System.out.println("Login/Cadastro necessário. Voltando ao menu.");
                return;
            }
        }

        // Passo 2: Coletar dados do resgate
        System.out.print("Espécie (ex: Cachorro, Gato): ");
        String especie = sc.nextLine();
        System.out.print("Sexo (ex: Macho, Fêmea, Não sei): ");
        String sexo = sc.nextLine();
        System.out.print("Local (ex: Rua, bairro, ponto de ref.): ");
        String local = sc.nextLine();
        System.out.print("Descrição (estado do animal, etc.): ");
        String descricao = sc.nextLine();

        // Passo 3: Criar e registrar a solicitação de resgate
        SolicitacaoResgate solicitacaoResgate = new SolicitacaoResgate(usuarioLogado.getCpf(), especie, sexo, local, descricao);
        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
        solicitacaoDAO.inserir(solicitacaoResgate);

        solicitacoes.registrarSolicitacao(solicitacaoResgate);

        System.out.println("Solicitação de resgate registrada!");
    }

    /**
     * Exibe um sub-menu para o usuário escolher entre fazer login ou criar conta.
     * É chamado por fluxos (como adotar ou resgatar) que exigem um usuário logado.
     */
    private static void autenticarUsuario(){
        System.out.println("\nVocê precisa estar logado para esta ação.");
        System.out.println("1. Fazer login");
        System.out.println("2. Criar conta");
        System.out.println("3. Voltar ao Menu Principal");

        int op = sc.nextInt();
        sc.nextLine(); // Consome o \n

        switch (op){
            case 1 -> login();
            case 2 -> cadastrarUsuario();
            case 3 -> {
                System.out.println("Voltando ao menu...");
                return; // Retorna ao fluxo que o chamou (que por sua vez retornará ao menu)
            }
            default -> System.out.println("Opção inválida!");
        }
    }

    /**
     * Solicita email e senha e tenta autenticar um usuário.
     * Verifica primeiro na lista de usuários comuns e, se falhar, na lista de admins.
     * Define a variável de sessão ({@code usuarioLogado} ou {@code adminLogado}) apropriada.
     */
    private static void login(){
        User usuarioNaoLogado = null;
        System.out.print("Email: ");
        String email = sc.nextLine();
        try {
            usuarioNaoLogado = userDAO.getUserEmail(email);
        } catch (Exception e) {
            System.out.println("Usuário não encontrado, ou problema com banco!");
            return;
        }
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        if(senha.equals(usuarioNaoLogado.getSenha())){
            if(usuarioNaoLogado instanceof Admin)
            {
                adminLogado = (Admin) usuarioNaoLogado;
                System.out.println("\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Bem-vindo Admin, " + adminLogado.getNome() + " ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            }

            else if( usuarioNaoLogado instanceof  User)
            {
                usuarioLogado =  usuarioNaoLogado;
                System.out.println("\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Bem-vindo, " + usuarioLogado.getNome() + " ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            }
            else { System.out.println("Conta inexistente ou tipo desconhecido."); }
        }
        else
        {
            System.out.println("Senha incorreta!");
        }
    }

    /**
     * Solicita os dados (nome, cpf, email, etc.) e cria uma nova conta de usuário.
     * O novo usuário é adicionado à lista {@code usuarios} e automaticamente
     * logado no sistema (definindo {@code usuarioLogado}).
     */
    private static void cadastrarUsuario(){
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("CPF: ");
        String cpf = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();
        System.out.print("Renda: ");
        String rendaStr = sc.nextLine();

        // Converte a renda de String para float
        float renda = Float.parseFloat(rendaStr);


        // Cria o novo usuário e o define como logado
        User user = new User(nome, email, cpf, senha, renda, true);

        userDAO.inserir(user);
        usuarioLogado = user;
        System.out.println("Usuario cadastrado e logado com sucesso!");
    }

    /**
     * Exibe todas as solicitações (com seu resumo) feitas pelo
     * usuário atualmente logado ({@code usuarioLogado}).
     * Utiliza o método polimórfico {@link Solicitacao#resumo()} para
     * exibir os detalhes de cada tipo de solicitação.
     */
    public static void verSolicitacoes(){
        // Busca no serviço apenas as solicitações do usuário logado
        List<Solicitacao> userSolicitacoes = new ArrayList<>();

        try {
            userSolicitacoes = solicitacaoDAO.listarTodasSolicitacoesCpf(usuarioLogado.getCpf());
        } catch (Exception e) {
            System.out.println("Erro ao listar solicitacoes!");
        }

        // BUGFIX: Verifica se a lista está nula OU vazia
        if(userSolicitacoes == null || userSolicitacoes.isEmpty()){
            System.out.println("Você ainda não fez nenhuma solicitação");
        } else{
            System.out.println("\n=== Minhas Solicitacoes ===");
            // Itera sobre a lista de solicitações genéricas
            for(Solicitacao s: userSolicitacoes){
                // Chama o método polimórfico resumo()
                // O Java decide em tempo de execução qual 'resumo()' chamar
                // (o de SolicitacaoAdocao ou o de SolicitacaoResgate)
                System.out.println("ID: " + s.getId() +
                        " | Status: " + s.getStatus() +
                        " | " + s.resumo());
            }
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Fluxos somente do Admin  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // private static void verificarSolicitacoes() { ... }
    // private static void adicionarAdmin() { ... }

    //criar atributo para raça do animal
    private static void adicionarAnimal() {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Idade:");
        Integer idade = Integer.parseInt(sc.nextLine());
        System.out.print("Especie:");
        String especie = sc.nextLine();
        System.out.print("Sexo:");
        String sexo = sc.nextLine();
        System.out.print("Porte:");
        String porte = sc.nextLine();
        System.out.print("Peso:");
        float peso = Float.parseFloat(sc.nextLine());
        System.out.print("Personalidade:");
        String personalidade = sc.nextLine();
        System.out.print("Historico:");
        String historico = sc.nextLine();
        System.out.print("localEncontrado:");
        String localEncontrado = sc.nextLine();
        System.out.print("Situacao:");
        String situacao = sc.nextLine();

        Animal animal = new Animal(65, nome, idade, especie, sexo, personalidade, historico, localEncontrado, situacao, porte, peso);

        animais.add(animal);
        animalDAO.inserir(animal);
        System.out.println("Animal adicionado ao banco de dados com sucesso!");
    }

    private static void removerAnimal() {
        try {
            List<Animal> animaisBanco = animalDAO.listarTodos();

            System.out.println("\nAnimais vindo do BANCO:");
            for (Animal a : animaisBanco) {
                System.out.println(a);
            }
        } catch (Exception e) {
            System.out.println("Erro ao tentar acessa o banco de dados!");
            e.printStackTrace();
        }

        try {
            System.out.print("Nome: ");
            String nome = sc.nextLine();
            System.out.print("ID:");
            Integer ID = Integer.parseInt(sc.nextLine());

            animalDAO.deletarPorId(ID);
            System.out.println("Animal removido com sucesso!");

            List<Animal> animaisBanco = animalDAO.listarTodos();

            System.out.println("\nAnimais atualizados vindo do BANCO:");
            for (Animal a : animaisBanco) {
                System.out.println(a);
            }


        } catch (Exception e) {
            System.out.println("Erro ao tentar remover animal do banco de dados!");
            e.printStackTrace();
        }

    }

    private static void verificarAnimal() {

        try{
            List<Animal> animaisBanco = animalDAO.listarTodos();
            for(Animal a : animaisBanco){
                System.out.println(a);
            }

        } catch (Exception e) {
            System.out.println("Erro ao tentar acessa o banco de dados!");
            e.printStackTrace();
        }
    }

    // ==================== FLUXO DE VERIFICACOES DE SOLICITACOES DO ADMIN ====================

    private static void verificarSolicitacoes() {
        try {
            List<Solicitacao> solicitacoesSistema = solicitacaoDAO.listarTodasSolicitacoes();
            if(solicitacoesSistema == null || solicitacoesSistema.isEmpty()){
                System.out.println("Sem solicitações pendentes no sistema");
            } else{
                System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  Solicitações do sistema ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                // Itera sobre a lista de solicitações genéricas
                for(Solicitacao s: solicitacoesSistema){
                    // Chama o método polimórfico resumo()
                    // O Java decide em tempo de execução qual 'resumo()' chamar
                    // (o de SolicitacaoAdocao ou o de SolicitacaoResgate)
                    System.out.println("ID: " + s.getId() +
                            " | Status: " + s.getStatus() +
                            " | " + s.resumo());
                }
            }
        } catch ( Exception e ) {
            System.out.println("Erro ao tentar ver as solicitações do sistema!");
        }

        try {
            System.out.println("\nOpções:");
            System.out.println("1. Aceitar adoção.");
            System.out.println("2. Começar processo de adoção.");
            System.out.println("3. Recusar adoção");
            System.out.println("4. Marcar resgate como concluído");
            System.out.println("5. Apagar solicitação");
            System.out.println("6. Ver novamente solicitações");
            System.out.println("7. Voltar para o MENU PRINCIPAL");

            System.out.println("Escolha uma opção: ");
            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> fluxoAceitarAdocao();
                case 2 -> fluxoAndamentoAdocao();
                case 3 -> fluxoRecusarAdocao();
                case 4 -> fluxoAprovarResgate();
                case 5 -> fluxoCancelarSolicitacao();
                case 6 -> verificarSolicitacoes();
                case 7 -> { System.out.println("Voltando para MENU PRINCIPAL..."); }
            }
        }
        catch ( Exception e ) {
            System.out.println("Erro ao tentar executar o fluxo de solicitacoes!");
        }
    }

    private static void fluxoAceitarAdocao() {
        try{
            System.out.println("Id da adoção que você quer aceitar:");
            int idAdocao = sc.nextInt();
            sc.nextLine();
            solicitacoes.aprovarAdocao(idAdocao);

        }
        catch ( Exception e ) {
            System.out.println(" Erro no processo de Aceitar uma solicitação! ");
        }
    }

    private static void fluxoRecusarAdocao() {
        try{
            System.out.println("Id da adoção que você deseja recusar:");
            int idAdocao = sc.nextInt();
            sc.nextLine();
            solicitacoes.reprovarAdocao(idAdocao);

            System.out.println("Solicitação de adoção reprovada com sucesso!");
        }catch (Exception e ){
            System.out.println("Erro no processo de Recusar solicitação");
        }
    }
     private static void fluxoAndamentoAdocao() {
        try{
            System.out.println("Id da adoção que você deseja iniciar:");
            int idAdocao = sc.nextInt();
            sc.nextLine();

            int sucesso = solicitacoes.andamentoSolicitacao(idAdocao);
            if(sucesso == 1){
                System.out.println("Processo de adoção iniciado com sucesso!");
            }
            else{
                System.out.println("Falha no processo de adoção");
            }


        }catch (Exception e ){
            System.out.println("Erro no andamento de Iniciar processo de adoção");
        }
    }

    private static void fluxoAprovarResgate() {
        try{
            System.out.println("Id do Resgate que você deseja marcar como Feito:");
            int idResgate = sc.nextInt();
            sc.nextLine();

            Integer sucesso = solicitacoes.aprovarResgate(idResgate);
            if(sucesso == 1){
                System.out.println("Resgate realizado registrado com sucesso!");

                System.out.println("Adicione as informações do animal resgatado:");
                adicionarAnimal();//tratar a entrada dos dados de tal forma que não deixe entradas vazias
            }
            System.out.println("Erro no processo de Aprovar Resgate");
        }
        catch ( Exception e ){
            System.out.println("Erro no processo de Aprovar Resgate");
        }
    }

    private static void fluxoCancelarSolicitacao(){
        try{
            verSolicitacoes();
            System.out.println("Id da solicitação que você deseja cancelar:");
            int idSolicitacao = sc.nextInt();
            sc.nextLine();

            solicitacaoDAO.deletarSolicitacao(idSolicitacao);
            System.out.println("Solicitação cancelada com sucesso!");
        }
        catch ( Exception e ){
            System.out.println("Erro ao tentar cancelar solicitação!");
        }
    }
}
