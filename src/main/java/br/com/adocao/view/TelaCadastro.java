package br.com.adocao.view;

import br.com.adocao.model.User;
import br.com.adocao.persistence.UserDAO;

import javax.swing.*;
import java.awt.*;
public class TelaCadastro extends JDialog {
    private final JTextField campoNome;
    private final JTextField campoCPF;
    private final JTextField campoEmail;
    private final JTextField campoRenda;
    private final JPasswordField campoSenha;
    private final JPasswordField campoConfSenha;
    public final JButton btnCadastrar;
    public final JButton btnVoltar;

    public TelaCadastro(JFrame parent) {
        super(parent, "Tela Cadastro de Usuário", true);

        setSize(320, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painel = new JPanel(new GridLayout(7, 2));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 20));

        painel.add(new JLabel("Nome:"));
        campoNome = new JTextField(15);
        painel.add(campoNome);


        painel.add(new JLabel("CPF:"));
        campoCPF = new JTextField(15);
        painel.add(campoCPF);

        painel.add(new JLabel("Email:"));
        campoEmail = new JTextField(15);
        painel.add(campoEmail);


        painel.add(new JLabel("Renda:"));
        campoRenda = new JTextField(15);
        painel.add(campoRenda);


        painel.add(new JLabel("Senha:"));
        campoSenha = new JPasswordField(15);
        painel.add(campoSenha);

        painel.add(new JLabel("Confirmar Senha:"));
        campoConfSenha = new JPasswordField(15);
        painel.add(campoConfSenha);


        //Botões
        JPanel painelBotao1 = new JPanel();
        painelBotao1.setLayout(new BoxLayout(painelBotao1, BoxLayout.X_AXIS));

        btnCadastrar = new JButton("Cadastrar");
        painelBotao1.add(btnCadastrar);

        JPanel painelBotao2 = new JPanel();
        btnVoltar = new JButton("Voltar");
        painelBotao2.add(btnVoltar);


        painel.add(painelBotao1);
        painel.add(painelBotao2);


        add(painel, BorderLayout.CENTER);
        // -----------------------------------------
        // AÇÕES
        // -----------------------------------------
        btnVoltar.addActionListener(e -> dispose());
        btnCadastrar.addActionListener(e -> {
            String nome = campoNome.getText();
            String cpf = campoCPF.getText();
            String email = campoEmail.getText();
            String renda = campoRenda.getText();
            String s1 = new String(campoSenha.getPassword());
            String s2 = new String(campoConfSenha.getPassword());


            // ----- Validações -----

            if (vazio(nome) || vazio(cpf) || vazio(email) || vazio(renda) || vazio(s1)) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!cpfValido(cpf)) {
                JOptionPane.showMessageDialog(this, "CPF deve conter exatamente 11 números.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!emailValido(email)) {
                JOptionPane.showMessageDialog(this, "E-mail inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!rendaValida(renda)) {
                JOptionPane.showMessageDialog(this, "Renda deve ser um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!senhasIguais(s1, s2)) {
                JOptionPane.showMessageDialog(this, "As senhas não coincidem!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            fazerCadastro();
            // Se passou por tudo, pode cadastrar
            JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!");
            dispose();
        });

        setVisible(true);
    }


    public boolean cpfValido(String cpf) {
        return cpf.matches("\\d{11}");
    }

    public boolean rendaValida(String renda) {
        try {
            Double.parseDouble(renda);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean emailValido(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$");
    }

    public boolean senhasIguais(String s1, String s2) {
        return s1.equals(s2);
    }

    public boolean vazio(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void fazerCadastro() {
        String nome = campoNome.getText();
        String cpf = campoCPF.getText();
        String email = campoEmail.getText();
        float renda = Float.parseFloat(campoRenda.getText());
        String s1 = new String(campoSenha.getPassword());


        User usuario = new User(nome, email, cpf, s1, renda, true);
        UserDAO userDAO = new UserDAO();
        userDAO.inserir(usuario);

        dispose();

    }
}

