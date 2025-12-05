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

public class TelaSolicitacoesResgate extends JDialog{
    private JTable tabelaResgates;
    private JPanel painelBotoes;

    private JButton btnAprovarResgate;
    private JButton btnRemoverSolicitacao;
    private JButton btnMenuPrincipal;

    private static SolicitacaoService solicitacaoService = new SolicitacaoService();

    public TelaSolicitacoesResgate(JFrame parent){
        super(parent, "Solicitações Resgates", true);

        setTitle("Sistema de Resgate - Tela Inicial");
        setSize(750, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // --------------------------------------------------------------------
        // 1. PAINEL SUPERIOR (Título)
        // --------------------------------------------------------------------
        JLabel titulo = new JLabel("🐾 Solicitações de Resgates", SwingConstants.CENTER );
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        // --------------------------------------------------------------------
        // 2. TABELA (Centro)
        // --------------------------------------------------------------------
        String[] colunas = {"ID", "Status", "Local", "Descricao", "Usuário", "Justificativa"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0){
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };


        tabelaResgates = new JTable(modelo);
        tabelaResgates.setRowHeight(25);
        tabelaResgates.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        JScrollPane scroll = new JScrollPane(tabelaResgates);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

        // --------------------------------------------------------------------
        // 3. PAINEL DE BOTÕES (Sul)
        // --------------------------------------------------------------------
        painelBotoes = new JPanel(new FlowLayout());

        btnAprovarResgate = new JButton("Aprovar");
        btnRemoverSolicitacao = new JButton("Remover");
        btnMenuPrincipal = new JButton("Menu Principal");

        add(painelBotoes, BorderLayout.SOUTH);

        tabelaResgates.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabelaResgates.getSelectedRow();
            if (linha == -1 ) return;

            btnRemoverSolicitacao.setEnabled(true);
            btnAprovarResgate.setEnabled(true);
        });
        // --------------------------------------------------------------------
        // Ações Botões
        // --------------------------------------------------------------------
        btnMenuPrincipal.addActionListener(e-> dispose());
        btnAprovarResgate.addActionListener(e -> {
            int linha = tabelaResgates.getSelectedRow();
            if (linha == -1) return;

            int id = (int) tabelaResgates.getValueAt(linha, 0);

            try{
                Integer sucesso = solicitacaoService.aprovarResgate(id);
                if(sucesso == 1){
                    System.out.println("Resgate realizado registrado com sucesso!");

                    System.out.println("Adicione as informações do animal resgatado:");
                    new TelaAdicionarAnimal(parent);
                    dispose();
                }
            } catch  (Exception ex){
                System.out.println("Erro no processo de Aprovar Resgate");
            }
        });

        btnRemoverSolicitacao.addActionListener(e-> {
            int linha = tabelaResgates.getSelectedRow();
            if (linha == -1) return;

            int id = (int) tabelaResgates.getValueAt(linha, 0);
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
            try {
                solicitacaoDAO.deletarSolicitacao(id);
                JOptionPane.showMessageDialog(null, "Solicitação removida com sucesso!\n");
                carregarTabelaSolicitacoesResgates();

            }  catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao deletar o solicitacao!\n" + ex.getMessage());
            }
        });

        carregarTabelaSolicitacoesResgates();
        atualizarInterface();
        setVisible(true);

    }

    private void carregarTabelaSolicitacoesResgates(){
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
        DefaultTableModel modelo = (DefaultTableModel) tabelaResgates.getModel();
        modelo.setRowCount(0); // limpar tabela

        User u = Session.getUsuarioLogado();
        String cpfUsuario =  u.getCpf();

        for (Solicitacao s: solicitacoesSistema){

            UserDAO userDAO = new UserDAO();

            String cpfSolicitante = s.getCpfSolicitante();
            User solicitante = userDAO.getUserCpf(s.getCpfSolicitante());
            String nomeSolicitante = (solicitante != null) ? solicitante.getNome() : "[Usuário Removido]";

            if(u instanceof Admin && (s.getTipoSolicitacao() == TipoSolicitacao.RESGATE)) {
                modelo.addRow(new Object[]{
                        s.getId(),
                        s.getStatus(),
                        s.getLocal(),
                        s.getDescricao(),
                        nomeSolicitante,
                        s.getStatus(),
                        s.getFeedbackAdmin()


                });
            }
            else if( u instanceof User  && (s.getTipoSolicitacao() == TipoSolicitacao.RESGATE)){
                if(cpfSolicitante.equals(cpfUsuario)){
                    modelo.addRow(new Object[]{
                            s.getId(),
                            s.getStatus(),
                            s.getLocal(),
                            s.getDescricao(),
                            nomeSolicitante,
                            s.getStatus(),
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
            painelBotoes.add(btnAprovarResgate);
        }
        else {
            painelBotoes.add(btnMenuPrincipal);
            painelBotoes.add(btnRemoverSolicitacao);
        }
        painelBotoes.revalidate();
        painelBotoes.repaint();

    }




}
