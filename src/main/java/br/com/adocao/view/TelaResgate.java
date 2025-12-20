package br.com.adocao.view;

import br.com.adocao.model.SolicitacaoResgate;
import br.com.adocao.model.User;
import br.com.adocao.persistence.SolicitacaoDAO;
import br.com.adocao.session.Session;

import javax.swing.*;
import java.awt.*;

public class TelaResgate extends JDialog {
    private final JTextField campoEspecie = new JTextField();
    private final JTextField campoSexo = new JTextField();
    private final JTextField campoLocal = new JTextField();
    private final JTextField campoDescricao = new JTextField();
    public final JButton btnFinalizar;
    public final JButton btnVoltar;


    public TelaResgate(JFrame parent) {
        super(parent, "Tela Resgate", true);

        setSize(320, 320);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painel = new JPanel(new GridLayout(5, 2));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 20));

        painel.add(new JLabel("Especie:"));
        painel.add(campoEspecie);

        painel.add(new JLabel("Sexo:"));
        painel.add(campoSexo);

        painel.add(new JLabel("Local:"));
        painel.add(campoLocal);

        painel.add(new JLabel("Descricao:"));
        painel.add(campoDescricao);

        //Botões
        JPanel painelBotao1 = new JPanel();
        painelBotao1.setLayout(new BoxLayout(painelBotao1, BoxLayout.X_AXIS));

        btnFinalizar = new JButton("Finalizar");
        painelBotao1.add(btnFinalizar);

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
        btnFinalizar.addActionListener(e -> {
            String especie = campoEspecie.getText();
            String sexo = campoSexo.getText();
            String local = campoLocal.getText();
            String descricao = campoDescricao.getText();

            // ------ Validações --------
            if(vazio(local)){
                JOptionPane.showMessageDialog(this, "O campo Localização não pode faltar", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!sexoValido(sexo)){
                JOptionPane.showMessageDialog(this, "Sexo deve ser Macho ou Fêmea.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!especieValido(especie)){
                JOptionPane.showMessageDialog(this, "Especie deve ser Cachorro ou Gato.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            User usuario = Session.getUsuarioLogado();
            SolicitacaoResgate solicitacaoResgate = new SolicitacaoResgate(usuario.getCpf(), especie, sexo, local, descricao);
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
            solicitacaoDAO.inserir(solicitacaoResgate);
            JOptionPane.showMessageDialog(this, "Solicitação de Resgate realizado com sucesso!");
            dispose();
        });


        setVisible(true);
    }

    public boolean especieValido(String especie) {
        boolean especieValido = especie.equals("Cachorro") || especie.equals("Gato");
        return especieValido;
    }

    public boolean sexoValido(String sexo) {
        boolean sexoValido = sexo.equals("Macho") || sexo.equals("Fêmea");
        return sexoValido;
    }

    public boolean vazio(String s) {
        return s == null || s.trim().isEmpty();
    }

}
