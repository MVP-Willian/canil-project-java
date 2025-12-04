package br.com.adocao.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import br.com.adocao.model.User;
import br.com.adocao.model.Admin;
import br.com.adocao.persistence.AnimalDAO;
import br.com.adocao.model.Animal;
import br.com.adocao.session.Session;

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
    private JButton btnSolicitacoes;
    private JButton btnAdicionarAnimal;
    private JButton btnRemoverAnimal;
    private JButton btnAdicionarResgate;
    private JButton btnLogout;
    private JButton btnAceitarAdocao;
    private JButton btnRecusarAdocao;
    private JButton btnResgateConcluido;
    private JButton btnRemoverSolicitacao;
    private JButton btnMenuPrincipal;
    private JButton btnUsuarios;



    public TelaInicial() {

        setTitle("Sistema de Adoção - Tela Inicial");
        setSize(800, 500);
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
        btnSolicitacoes = new JButton("Solicitações");
        btnRemoverSolicitacao = new JButton("Remover Solicitacao");
        btnMenuPrincipal = new JButton("MenuPrincipal");
        btnLogout = new JButton("Logout");


        //Botoes Admin - Remover Solicitacao pode ser de todo mundo
        btnAdicionarAnimal = new JButton("Adicionar Animal");
        btnRemoverAnimal = new JButton("Remover Animal");
        btnAceitarAdocao = new JButton("Aceitar Adocao");
        btnRecusarAdocao = new JButton("Recusar Adocao");
        btnAdicionarResgate = new JButton("Adicionar Resgate");
        btnResgateConcluido = new JButton("Resgate Concluido");
        btnUsuarios = new JButton("Usuarios");


        btnAdotar.setEnabled(false);
        btnRemoverSolicitacao.setEnabled(false);
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

        carregarTabela();
        atualizarInterface();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaInicial().setVisible(true));
    }

    private void carregarTabela() {
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
            painelBotoes.add(btnSolicitacoes);
            painelBotoes.add(btnAdicionarAnimal);
            painelBotoes.add(btnRemoverAnimal);
            painelBotoes.add(btnAdicionarResgate);
            painelBotoes.add(btnUsuarios);
            painelBotoes.add(btnLogout);
        }
        else {
            painelBotoes.add(btnSolicitacoes);
            painelBotoes.add(btnAdotar);
            painelBotoes.add(btnResgate);
            painelBotoes.add(btnRemoverSolicitacao);
            painelBotoes.add(btnLogout);
        }
        painelBotoes.revalidate();
        painelBotoes.repaint();
    }
}
