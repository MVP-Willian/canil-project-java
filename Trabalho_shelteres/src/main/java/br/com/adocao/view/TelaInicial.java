package br.com.adocao.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import br.com.adocao.model.User;
import br.com.adocao.model.Admin;
import br.com.adocao.persistence.AnimalDAO;
import br.com.adocao.model.Animal;
import br.com.adocao.persistence.SolicitacaoDAO;
import br.com.adocao.session.Session;
import br.com.adocao.model.Solicitacao;
import br.com.adocao.model.SolicitacaoAdocao;

import java.util.ArrayList;
import java.util.List;

public class TelaInicial extends JFrame {

    private JPanel painelBotoes;

    private JTable tabelaAnimais;
    private JButton btnResgate;
    private JButton btnDetalhes;
    private JButton btnAdotar;
    private JButton btnLogin;
    private JButton btnCadastrar;
    private JButton btnSolicitacoesAdocoes;
    private JButton btnSolicitacoesResgates;
    private JButton btnAdicionarAnimal;
    private JButton btnAnimais;
    private JButton btnAdicionarResgate;
    private JButton btnLogout;
    private JButton btnUsuarios;



    public TelaInicial() {

        setTitle("Sistema de Adoção - Tela Inicial");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --------------------------------------------------------------------
        // 1. PAINEL SUPERIOR (Título)
        // --------------------------------------------------------------------
        JLabel titulo = new JLabel("🐾 Animais Disponíveis para Adoção", SwingConstants.CENTER );
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        // --------------------------------------------------------------------
        // 2. TABELA (Centro)
        // --------------------------------------------------------------------
        String[] colunas = {"ID", "Nome", "Espécie", "Status"};


        // Por enquanto usamos uma tabela sem DAO (mock)
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0){
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabelaAnimais = new JTable(modelo);
        tabelaAnimais.setRowHeight(25);
        tabelaAnimais.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabelaAnimais);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

        // --------------------------------------------------------------------
        // 3. PAINEL DE BOTÕES (Sul)
        // --------------------------------------------------------------------
        painelBotoes = new JPanel(new FlowLayout());

        //Botoes sem login
        btnDetalhes = new JButton("Ver Detalhes");
        btnLogin =  new JButton("Login");
        btnCadastrar = new JButton("Cadastrar");

        //Botoes usuario
        btnAdotar = new JButton("Solicitar Adocão");
        btnResgate = new JButton("Solicitar Resgate");
        btnSolicitacoesAdocoes = new JButton("Solicit.Adocoes");
        btnSolicitacoesResgates = new JButton("Solicit.Resgates");
        btnLogout = new JButton("Logout");


        //Botoes Admin - Remover Solicitacao pode ser de todo mundo
        btnAdicionarAnimal = new JButton("Adicionar Animal");
        btnAnimais = new JButton("Animais");
        btnAdicionarResgate = new JButton("Adicionar Resgate");
        btnUsuarios = new JButton("Usuarios");


        btnAdotar.setEnabled(false);
        btnDetalhes.setEnabled(false);


        add(painelBotoes, BorderLayout.SOUTH);


        // --------------------------------------------------------------------
        // 4. Comando para liberar botões de Adotar e Detalhes
        // --------------------------------------------------------------------

        tabelaAnimais.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabelaAnimais.getSelectedRow();
            if (linha == -1 ) return;

            btnDetalhes.setEnabled(true);
            btnAdotar.setEnabled(true);
        });

        // --------------------------------------------------------------------
        // 5. Comando para abrir tela de Detalhes de animal específico selecionado
        // --------------------------------------------------------------------

        btnDetalhes.addActionListener(e -> {
            int linha = tabelaAnimais.getSelectedRow();
            if (linha == -1) return;

            int id = (int) tabelaAnimais.getValueAt(linha, 0);

            AnimalDAO dao = new AnimalDAO();
            Animal animal = dao.getAnimalPorId(id);

            AnimalDetalhesDialog dialog = new AnimalDetalhesDialog(this, animal);
            dialog.setVisible(true);

        });


        // --------------------------------------------------------------------
        // 6. Comando para abrir tela de login quando selecionada
        // --------------------------------------------------------------------

        btnLogin.addActionListener(e -> {
            new TelaLogin(this);
            atualizarInterface();
        });


        // --------------------------------------------------------------------
        // 6. Comando para abrir tela de Cadastro quando selecionada
        // --------------------------------------------------------------------
        btnCadastrar.addActionListener(e -> new TelaCadastro(this));



        // -----------------------------------------
        // 7. Comando para Adicionar Animal
        // -----------------------------------------
        btnAdicionarAnimal.addActionListener(e -> {new TelaAdicionarAnimal(this);
        carregarTabelaAnimais();});


        // -----------------------------------------
        // 7. Comando para Solicitar Adoção
        // -----------------------------------------
        btnAdotar.addActionListener(e-> {
            if(Session.getUsuarioLogado() == null) {
                JOptionPane.showMessageDialog(null, "Primeiramente faça o login");
                return;
            }
            int linha = tabelaAnimais.getSelectedRow();
            if (linha == -1) return;
            int id = (int) tabelaAnimais.getValueAt(linha, 0);

            AnimalDAO dao = new AnimalDAO();
            Animal animal = dao.getAnimalPorId(id);
            User usuario = Session.getUsuarioLogado();

            SolicitacaoAdocao solicitacaoAdocao = new SolicitacaoAdocao(usuario.getCpf(), animal.getId());
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
            solicitacaoDAO.inserir(solicitacaoAdocao);

            JOptionPane.showMessageDialog(null, "Solicitação de Adoção enviada com sucesso!");
        });

        // -----------------------------------------
        // 8. Comando para Solicitar Resgate
        // -----------------------------------------
        btnResgate.addActionListener(e -> {
            if(Session.getUsuarioLogado() == null) {
                JOptionPane.showMessageDialog(null, "Primeiramente faça o login");
                return;
            }
            TelaResgate telaResgate = new TelaResgate(this);
        });

        // -----------------------------------------
        // 9. Comando para Adicionar Resgate
        // -----------------------------------------
        btnAdicionarResgate.addActionListener(e -> {
            if(Session.getUsuarioLogado() == null) {
                JOptionPane.showMessageDialog(null, "Primeiramente faça o login");
                return;
            }
            TelaResgate telaResgate = new TelaResgate(this);
        });

        // -----------------------------------------
        // 10. Comando para Adicionar Resgate
        // -----------------------------------------
        btnLogout.addActionListener(e -> {
            Session.logout();
            atualizarInterface();
            JOptionPane.showMessageDialog(null, "Logout com sucesso!");

        });

        // -----------------------------------------
        // 10. Comando para Remover Animal
        // -----------------------------------------
        btnAnimais.addActionListener(e -> new TelaAnimais(this));


        // -----------------------------------------
        // 11. Comando ver solicitações de Adoções
        // -----------------------------------------
        btnSolicitacoesAdocoes.addActionListener(e -> {new TelaSolicitacoesAdocao(this);
        carregarTabelaAnimais();});


        // -----------------------------------------
        // 12. Comando ver solicitações de Resgates
        // -----------------------------------------
        btnSolicitacoesResgates.addActionListener(e -> {new TelaSolicitacoesResgate(this);
            carregarTabelaAnimais();});

        // -----------------------------------------
        // 13. Comando ver Controle de usuários
        // -----------------------------------------
        btnUsuarios.addActionListener(e -> new TelaControleUsuarios(this));
        carregarTabelaAnimais();
        atualizarInterface();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaInicial().setVisible(true));
    }

    private void carregarTabelaAnimais() {
        AnimalDAO dao = new AnimalDAO();
        List<Animal> animais = new ArrayList<>();
        try {
            animais = dao.listarTodos();
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Erro ao carregar os Animais");
        }


        DefaultTableModel modelo = (DefaultTableModel) tabelaAnimais.getModel();
        modelo.setRowCount(0); // limpar tabela

        for (Animal a : animais) {
            modelo.addRow(new Object[]{
                    a.getId(),
                    a.getNome(),
                    a.getEspecie(),
                    a.getStatus()
            });
        }
    }

    private void atualizarInterface() {
        if(painelBotoes != null){
            painelBotoes.removeAll();
        }

        User u = Session.getUsuarioLogado();
        if (u == null) {
            painelBotoes.add(btnDetalhes);
            painelBotoes.add(btnAdotar);
            painelBotoes.add(btnResgate);
            painelBotoes.add(btnLogin);
            painelBotoes.add(btnCadastrar);
        }
        else if(u instanceof Admin){
            // Adicionar Aceitar/remover adocao - adicionar/concluido resgate - Cancelarsolicitacao - Menuprincipal
            painelBotoes.add(btnSolicitacoesAdocoes);
            painelBotoes.add(btnSolicitacoesResgates);
            painelBotoes.add(btnAdicionarAnimal);
            painelBotoes.add(btnAnimais);
            painelBotoes.add(btnAdicionarResgate);
            painelBotoes.add(btnUsuarios);
            painelBotoes.add(btnLogout);
        }
        else {
            painelBotoes.add(btnSolicitacoesAdocoes);
            painelBotoes.add(btnSolicitacoesResgates);
            painelBotoes.add(btnAdotar);
            painelBotoes.add(btnResgate);
            painelBotoes.add(btnLogout);
        }
        painelBotoes.revalidate();
        painelBotoes.repaint();
    }
}
