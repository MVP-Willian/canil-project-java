package br.com.adocao.view;

import br.com.adocao.model.*;
import br.com.adocao.persistence.AnimalDAO;
import br.com.adocao.persistence.UserDAO;
import br.com.adocao.persistence.SolicitacaoDAO;
import br.com.adocao.session.Session;
import br.com.adocao.services.SolicitacaoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaSolicitacoesAdocao extends  JDialog{
    private JTable tabelaAdocoes;
    private JPanel painelBotoes;

    private JButton btnRemoverSolicitacao;
    private JButton btnMenuPrincipal;
    private JButton btnAceitarAdocao;
    private JButton btnRecusarAdocao;

    private static SolicitacaoService solicitacaoService = new SolicitacaoService();


    public TelaSolicitacoesAdocao(JFrame parent){
        super(parent, "Solicitações Adoções", true);

        setTitle("Sistema de Adoção - Tela Inicial");
        setSize(750, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));


        // --------------------------------------------------------------------
        // 1. PAINEL SUPERIOR (Título)
        // --------------------------------------------------------------------
        JLabel titulo = new JLabel("🐾 Solicitações de Adoções", SwingConstants.CENTER );
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        // --------------------------------------------------------------------
        // 2. TABELA (Centro)
        // --------------------------------------------------------------------
        String[] colunas = {"ID", "Status", "Animal", "Solicitante", "Justificativa"};

        DefaultTableModel modelo = new DefaultTableModel(colunas, 0){
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabelaAdocoes = new JTable(modelo);
        tabelaAdocoes.setRowHeight(25);
        tabelaAdocoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabelaAdocoes);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

        // --------------------------------------------------------------------
        // 3. PAINEL DE BOTÕES (Sul)
        // --------------------------------------------------------------------
        painelBotoes = new JPanel(new FlowLayout());

        btnRemoverSolicitacao = new JButton("Remover Solicitacao");
        btnMenuPrincipal = new JButton("Menu Principal");
        btnAceitarAdocao = new JButton("Aceitar Adocao");
        btnRecusarAdocao = new JButton("Recusar Adocao");

        add(painelBotoes, BorderLayout.SOUTH);

        tabelaAdocoes.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabelaAdocoes.getSelectedRow();
            if (linha == -1 ) return;

            btnRemoverSolicitacao.setEnabled(true);
            btnAceitarAdocao.setEnabled(true);
            btnRecusarAdocao.setEnabled(true);
        });


        // --------------------------------------------------------------------
        // Ações Botões
        // --------------------------------------------------------------------
        btnMenuPrincipal.addActionListener(e-> dispose());


        btnRemoverSolicitacao.addActionListener(e-> {
            int linha = tabelaAdocoes.getSelectedRow();
            if (linha == -1) return;

            int id = (int) tabelaAdocoes.getValueAt(linha, 0);
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
            try {
                solicitacaoDAO.deletarSolicitacao(id);
                JOptionPane.showMessageDialog(null, "Solicitação removida com sucesso!\n");
                carregarTabelaSolicitacoesAdocoes();

            }  catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao deletar o solicitacao!\n" + ex.getMessage());
            }
        });

        btnAceitarAdocao.addActionListener(e-> {
            int linha = tabelaAdocoes.getSelectedRow();
            if (linha == -1) return;

            int id = (int) tabelaAdocoes.getValueAt(linha, 0);

            try{
                solicitacaoService.aprovarAdocao(id);
                JOptionPane.showMessageDialog(null, "Solicitação Aprovada com sucesso!\n");
                carregarTabelaSolicitacoesAdocoes();
            }catch (Exception ex){
                JOptionPane.showMessageDialog(null, "Erro ao Aceitar o solicitacao!\n" + ex.getMessage());
            }
        });

        btnRecusarAdocao.addActionListener(e-> {
            int linha = tabelaAdocoes.getSelectedRow();
            if (linha == -1) return;

            int id = (int) tabelaAdocoes.getValueAt(linha, 0);

            try{
                SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();

                solicitacaoService.reprovarAdocao(id);
                String input = JOptionPane.showInputDialog(null, "Por favor, digite a justificativa da Reprovação:");
                solicitacaoDAO.atualizarFeedBackAdmin(input, id);

                JOptionPane.showMessageDialog(null, "Solicitação Recusada com sucesso!\n");


                carregarTabelaSolicitacoesAdocoes();

            }catch (Exception ex){
                JOptionPane.showMessageDialog(null, "Erro ao Recusar o solicitacao!\n" + ex.getMessage());
            }
        });

        carregarTabelaSolicitacoesAdocoes();
        atualizarInterface();
        setVisible(true);
    }

    private void carregarTabelaSolicitacoesAdocoes(){
        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
        List<Solicitacao> solicitacoesSistema = new ArrayList<>();

        try {
            solicitacoesSistema = solicitacaoDAO.listarTodasSolicitacoes();
            if(solicitacoesSistema == null || solicitacoesSistema.isEmpty()) {
                System.out.println("Sem solicitações pendentes no sistema");
            }
        } catch (Exception ex){
            System.out.println("Erro ao tentar ver as solicitações do sistema!");
        }
        DefaultTableModel modelo = (DefaultTableModel) tabelaAdocoes.getModel();
        modelo.setRowCount(0); // limpar tabela

        User u = Session.getUsuarioLogado();
        String cpfUsuario =  u.getCpf();

        System.out.println(u);
        for (Solicitacao s: solicitacoesSistema){

            AnimalDAO animalDAO = new AnimalDAO();
            UserDAO userDAO = new UserDAO();
            Animal animal = animalDAO.getAnimalPorId(s.getIdAnimal());
            String email = u.getEmail();
            String cpfSolicitante = s.getCpfSolicitante();
            User solicitante = userDAO.getUserCpf(s.getCpfSolicitante());

            String nomeAnimal = (animal != null) ? animal.getNome() : "[Animal Removido]";
            String nomeSolicitante = (solicitante != null) ? solicitante.getNome() : "[Usuário Removido]";
            if(u instanceof Admin && (s.getTipoSolicitacao() == TipoSolicitacao.ADOCAO)) {
                modelo.addRow(new Object[]{
                        s.getId(),
                        s.getStatus(),
                        nomeAnimal,
                        nomeSolicitante,
                        s.getFeedbackAdmin()

                });
            }
            else if( u instanceof User && (s.getTipoSolicitacao() == TipoSolicitacao.ADOCAO)){
                if(cpfSolicitante.equals(cpfUsuario)){
                    modelo.addRow(new Object[]{
                            s.getId(),
                            s.getStatus(),
                            nomeAnimal,
                            nomeSolicitante,
                            s.getFeedbackAdmin()

                    });
                }
            }
        }

    }

    public void atualizarInterface(){
        if(painelBotoes != null){
            painelBotoes.removeAll();
        }
        User u = Session.getUsuarioLogado();
        if (u instanceof Admin) {
            painelBotoes.add(btnMenuPrincipal);
            painelBotoes.add(btnRemoverSolicitacao);
            painelBotoes.add(btnAceitarAdocao);
            painelBotoes.add(btnRecusarAdocao);
        }
        else {
            painelBotoes.add(btnMenuPrincipal);
            painelBotoes.add(btnRemoverSolicitacao);
        }
        painelBotoes.revalidate();
        painelBotoes.repaint();

    }


}
