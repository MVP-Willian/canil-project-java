package br.com.adocao.view;

import br.com.adocao.model.User;
import br.com.adocao.persistence.UserDAO;
import br.com.adocao.session.Session;

import javax.swing.*;
import java.awt.*;


public class TelaLogin extends JDialog{
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton btnEntrar, btnCancelar;


    public TelaLogin(JFrame parent) {
        super(parent, "Tela Login", true);


        setSize(400, 220);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // -----------------------------------------
        // TÍTULO
        // -----------------------------------------

        JLabel titulo = new JLabel("Login do Usuário", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        // -----------------------------------------
        // FORMULÁRIO
        // -----------------------------------------

        JPanel painelForm = new JPanel(new GridLayout(2, 2, 10, 10));
        painelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(painelForm, BorderLayout.CENTER);

        JLabel labelEmail = new JLabel("Email:");
        painelForm.add(labelEmail);
        campoEmail = new JTextField();
        campoEmail.setPreferredSize(new Dimension(50, 28));
        painelForm.add(campoEmail);

        JLabel labelSenha = new JLabel("Senha:");
        painelForm.add(labelSenha);
        campoSenha = new JPasswordField();
        campoSenha.setPreferredSize(new Dimension(50, 28));
        painelForm.add(campoSenha);


        // -----------------------------------------
        // BOTÕES
        // -----------------------------------------

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnEntrar = new JButton("Entrar");
        btnCancelar = new JButton("Cancelar");

        painelBotoes.add(btnEntrar);
        painelBotoes.add(btnCancelar);

        add(painelBotoes, BorderLayout.SOUTH);

        // -----------------------------------------
        // AÇÕES
        // -----------------------------------------
        btnCancelar.addActionListener(e -> dispose());


        btnEntrar.addActionListener(e -> fazerLogin());

        setVisible(true);

    }

    private void fazerLogin() {
        String email = campoEmail.getText();
        String senha = campoSenha.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        User usuario = userDAO.login(email, senha);

        if (usuario != null) {
            Session.setUsuarioLogado(usuario);
            JOptionPane.showMessageDialog(this, "Login efetuado com sucesso!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Email ou senha incorretos!");
        }

        dispose();

    }

}
