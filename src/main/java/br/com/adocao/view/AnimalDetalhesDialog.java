package br.com.adocao.view;

import br.com.adocao.model.Animal;

import javax.swing.*;
import java.awt.*;

public class AnimalDetalhesDialog extends JDialog {

    public AnimalDetalhesDialog(JFrame parent, Animal animal) {
        super(parent, "Detalhes do animal", true);

        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));


        JLabel titulo = new JLabel("🐾 Detalhes do Animal", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel painelInfo = new JPanel(new GridLayout(0, 2, 10, 10));
        painelInfo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        painelInfo.add(new JLabel("ID:", SwingConstants.RIGHT));
        painelInfo.add(new JLabel(String.valueOf(animal.getId())));

        painelInfo.add(new JLabel("Nome:", SwingConstants.RIGHT));
        painelInfo.add(new JLabel(animal.getNome()));

        painelInfo.add(new JLabel("Idade:", SwingConstants.RIGHT));
        painelInfo.add(new JLabel(String.valueOf(animal.getIdade())));

        painelInfo.add(new JLabel("Espécie:", SwingConstants.RIGHT));
        painelInfo.add(new JLabel(animal.getEspecie()));

        painelInfo.add(new JLabel("Sexo:", SwingConstants.RIGHT));
        painelInfo.add(new JLabel(animal.getSexo()));

        painelInfo.add(new JLabel("Porte:", SwingConstants.RIGHT));
        painelInfo.add(new JLabel(animal.getPorte()));

        painelInfo.add(new JLabel("Peso:", SwingConstants.RIGHT));
        painelInfo.add(new JLabel(String.valueOf(animal.getPeso())));

        painelInfo.add(new JLabel("Personalidade:", SwingConstants.RIGHT));
        painelInfo.add(new JLabel(String.valueOf(animal.getPersonalidade())));

        painelInfo.add(new JLabel("Histórico:", SwingConstants.RIGHT));
        painelInfo.add(new JLabel(String.valueOf(animal.getHistorico())));

        painelInfo.add(new JLabel("Local Encontrado:", SwingConstants.RIGHT));
        painelInfo.add(new JLabel(animal.getLocalEncontrado()));


        painelInfo.add(new JLabel("Status:", SwingConstants.RIGHT));
        painelInfo.add(new JLabel(animal.getStatus()));


        add(painelInfo, BorderLayout.CENTER);
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());

        JPanel painelBotao = new JPanel(new FlowLayout());
        painelBotao.add(btnFechar);

        add(painelBotao, BorderLayout.SOUTH);
    }

}
