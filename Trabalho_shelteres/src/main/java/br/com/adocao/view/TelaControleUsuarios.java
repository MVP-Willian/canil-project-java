package br.com.adocao.view;

import br.com.adocao.persistence.UserDAO;
import br.com.adocao.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class TelaControleUsuarios extends JDialog {

    private JTable tabelaUsuarios;
    private JPanel painelBotoes;

    private JButton btnRemoverUsuario;
    private JButton btnMenuPrincipal;

    public TelaControleUsuarios(JFrame parent) {
        super(parent, "Controle Usuários", true);

        setTitle("Sistema de canil - Tela Inicial");
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));


        // --------------------------------------------------------------------
        // 1. PAINEL SUPERIOR (Título)
        // --------------------------------------------------------------------
        JLabel titulo = new JLabel("🚻 Usuários cadastrados no Canil", SwingConstants.CENTER );
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        // --------------------------------------------------------------------
        // 2. TABELA (Centro)
        // --------------------------------------------------------------------
        String[] colunas = {"Nome", "CPF", "E-mail", "Renda"};

        DefaultTableModel modelo = new DefaultTableModel(colunas, 0){
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabelaUsuarios = new JTable(modelo);
        tabelaUsuarios.setRowHeight(25);
        tabelaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabelaUsuarios);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

        // --------------------------------------------------------------------
        // 3. PAINEL DE BOTÕES (Sul)
        // --------------------------------------------------------------------
        painelBotoes = new JPanel(new FlowLayout());

        btnMenuPrincipal = new JButton("Menu Principal");
        btnRemoverUsuario = new JButton("Remover Usuário");

        btnRemoverUsuario.setEnabled(false);

        painelBotoes.add(btnMenuPrincipal);
        painelBotoes.add(btnRemoverUsuario);
        add(painelBotoes, BorderLayout.SOUTH);

        // --------------------------------------------------------------------
        // Ações Botões
        // --------------------------------------------------------------------
        tabelaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabelaUsuarios.getSelectedRow();
            if (linha == -1 ) return;

            btnRemoverUsuario.setEnabled(true);
        });

        btnMenuPrincipal.addActionListener(e -> dispose());

        btnRemoverUsuario.addActionListener(e -> {
            int linha = tabelaUsuarios.getSelectedRow();
            if (linha == -1) return;

            String cpf =  (String) tabelaUsuarios.getValueAt(linha, 1);

            UserDAO userDAO = new UserDAO();
            try{
                userDAO.deletarUsuario(cpf);
                JOptionPane.showMessageDialog(null, "Animal Removido com sucesso!");
                carregarTabelaUsuarios();
            }catch(Exception ex){
                JOptionPane.showMessageDialog(parent, ex.getMessage(), "Erro ao deletar Usuários", JOptionPane.ERROR_MESSAGE);
            }

        });

        carregarTabelaUsuarios();
        setVisible(true);
    }

    private void carregarTabelaUsuarios() {
        UserDAO userDAO = new UserDAO();
        List<User> usuarios = userDAO.listarTodos();

        DefaultTableModel modelo = (DefaultTableModel) tabelaUsuarios.getModel();
        modelo.setRowCount(0);

        for (User usuario : usuarios) {
            modelo.addRow(new Object[]{
               usuario.getNome(),
               usuario.getCpf(),
               usuario.getEmail(),
               usuario.getRenda()
            });
        }
    }

}
