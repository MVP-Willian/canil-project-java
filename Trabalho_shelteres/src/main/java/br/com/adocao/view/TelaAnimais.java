package br.com.adocao.view;


import br.com.adocao.model.Animal;
import br.com.adocao.persistence.AnimalDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaAnimais extends JDialog {
    private JTable tabelaAnimais;
    private JPanel painelBotoes;


    private JButton btnDetalhes;
    private JButton btnRemoverAninal;


    public TelaAnimais(JFrame parent) {
        super(parent, "Controle Animais", true);

        setTitle("Sistema de canil - Tela Inicial");
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // --------------------------------------------------------------------
        // 1. PAINEL SUPERIOR (Título)
        // --------------------------------------------------------------------
        JLabel titulo = new JLabel("🐾 Animais cadastrados no Canil", SwingConstants.CENTER );
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        // --------------------------------------------------------------------
        // 2. TABELA (Centro)
        // --------------------------------------------------------------------
        String[] colunas = {"ID", "Nome", "Espécie", "Status"};


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

        btnDetalhes = new JButton("Detalhes");
        btnRemoverAninal = new JButton("Remover Animal");

        btnDetalhes.setEnabled(false);
        btnRemoverAninal.setEnabled(false);

        painelBotoes.add(btnDetalhes);
        painelBotoes.add(btnRemoverAninal);
        add(painelBotoes, BorderLayout.SOUTH);


        // --------------------------------------------------------------------
        // Ações Botões
        // --------------------------------------------------------------------
        tabelaAnimais.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabelaAnimais.getSelectedRow();
            if (linha == -1 ) return;

            btnDetalhes.setEnabled(true);
            btnRemoverAninal.setEnabled(true);
        });

        btnDetalhes.addActionListener(e -> {
            int linha = tabelaAnimais.getSelectedRow();
            if (linha == -1) return;

            int id = (int) tabelaAnimais.getValueAt(linha, 0);

            AnimalDAO animalDAO = new AnimalDAO();
            Animal animal = animalDAO.getAnimalPorId(id);

            AnimalDetalhesDialog dialog = new AnimalDetalhesDialog(parent, animal);
            dialog.setVisible(true);

        });

        btnRemoverAninal.addActionListener(e -> {
            int linha = tabelaAnimais.getSelectedRow();
            if (linha == -1) return;

            int id = (int) tabelaAnimais.getValueAt(linha, 0);

            AnimalDAO animalDAO = new AnimalDAO();
            try {
                animalDAO.deletarPorId(id);
                JOptionPane.showMessageDialog(null, "Animal Removido com sucesso!");
                carregarTabelaAnimais();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, ex.getMessage(), "Erro ao deletar", JOptionPane.ERROR_MESSAGE);
            }
        });

        carregarTabelaAnimais();
        setVisible(true);
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

}
