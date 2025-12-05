package br.com.adocao.view;

import br.com.adocao.model.Animal;
import br.com.adocao.persistence.AnimalDAO;

import javax.swing.*;
import java.awt.*;


public class TelaAdicionarAnimal extends JDialog {
    private final JTextField campoNome;
    private final  JTextField campoIdade;
    private  final JTextField campoEspecie;
    private  final JTextField campoSexo;
    private  final JTextField campoPorte;
    private  final JTextField campoPeso;
    private  final JTextField campoPersonalidade;
    private final JTextField  campoHistorico;
    private final JTextField campoLocalEncontrado;

    public JButton btnFinalizar;
    public JButton btnCancelar;

    public TelaAdicionarAnimal(JFrame parent) {

        // -----------------------------------------
        // Configurações do frame
        // -----------------------------------------
        super(parent, "Adicionar Animal", true);

        setSize(320, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));

        JPanel painel = new JPanel(new GridLayout(10, 2));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 20));


        // -----------------------------------------
        // CAMPOS DE PREENCHIMENTO
        // -----------------------------------------
        painel.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painel.add(campoNome);

        painel.add(new JLabel("Idade:"));
        campoIdade = new JTextField(15);
        painel.add(campoIdade);

        painel.add(new JLabel("Especie:"));
        campoEspecie = new JTextField(15);
        painel.add(campoEspecie);

        painel.add(new JLabel("Sexo:"));
        campoSexo = new JTextField(15);
        painel.add(campoSexo);

        painel.add(new JLabel("Porte:"));
        campoPorte = new JTextField(15);
        painel.add(campoPorte);

        painel.add(new JLabel("Peso:"));
        campoPeso = new JTextField(15);
        painel.add(campoPeso);

        painel.add(new JLabel("Personalidade:"));
        campoPersonalidade = new JTextField(15);
        painel.add(campoPersonalidade);

        painel.add(new JLabel("Historico:"));
        campoHistorico = new JTextField(15);
        painel.add(campoHistorico);

        painel.add(new JLabel("LocalEncontrado:"));
        campoLocalEncontrado = new JTextField(15);
        painel.add(campoLocalEncontrado);


        // -----------------------------------------
        // BOTÕES
        // -----------------------------------------
        JPanel painelBotao1 = new JPanel();
        painelBotao1.setLayout(new BoxLayout(painelBotao1, BoxLayout.X_AXIS));

        btnFinalizar = new JButton("Finalizar");
        painelBotao1.add(btnFinalizar);

        JPanel painelBotao2 = new JPanel();
        btnCancelar = new JButton("Voltar");
        painelBotao2.add(btnCancelar);

        painel.add(painelBotao1);
        painel.add(painelBotao2);

        add(painel, BorderLayout.NORTH);

        // -----------------------------------------
        // AÇÕES
        // -----------------------------------------
        btnCancelar.addActionListener(e-> dispose());

        btnFinalizar.addActionListener(e-> {
            String nome = campoNome.getText();
            String idade = campoIdade.getText();
            String especie = campoEspecie.getText();
            String sexo = campoSexo.getText();
            String porte = campoPorte.getText();
            String  peso = campoPeso.getText();
            String personalidade = campoPersonalidade.getText();

            // ------ Validações --------
            if(vazio(nome) || vazio(especie) || vazio(sexo) || vazio(personalidade) || vazio(porte) || vazio(peso)){
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(!idadeValido(idade)){
                JOptionPane.showMessageDialog(this, "Idade deve ser um numeral.", "Erro", JOptionPane.ERROR_MESSAGE);
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
            if(!pesoValido(peso)){
                JOptionPane.showMessageDialog(this, "Peso deve ser um float entre 0 e 100.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!porteValido(porte)){
                JOptionPane.showMessageDialog(this, "Porte deve ser Pequeno Médio ou Grande.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            adicionarAnimal();
            JOptionPane.showMessageDialog(this, "Animal adicionado com sucesso!");
            dispose();
        });

        setVisible(true);
    }

    public boolean vazio(String s) {
        return s == null || s.trim().isEmpty();
    }
    public boolean idadeValido(String idade) {
        try{
            Integer.parseInt(idade);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public boolean sexoValido(String sexo) {
        boolean sexoValido = sexo.equals("Macho") || sexo.equals("Fêmea");
        return sexoValido;
    }

    public boolean especieValido(String especie) {
        boolean especieValido = especie.equals("Cachorro") || especie.equals("Gato");
        return especieValido;
    }

    public boolean porteValido(String porte) {
        boolean porteValido = porte.equals("Pequeno") || porte.equals("Médio") || porte.equals("Grande");
        return porteValido;
    }

    public boolean pesoValido(String peso) {
        return Float.parseFloat(peso) >= 0 && Float.parseFloat(peso) <= 100;
    }

    private void adicionarAnimal() {
        String nome = campoNome.getText();
        Integer idade = Integer.parseInt(campoIdade.getText());
        String especie = campoEspecie.getText();
        String sexo = campoSexo.getText();
        String porte = campoPorte.getText();
        Float peso = Float.parseFloat(campoPeso.getText());
        String personalidade = campoPersonalidade.getText();
        String historico = campoHistorico.getText();
        String localEncontrado = campoLocalEncontrado.getText();

        Animal animal = new Animal(0, nome, idade, especie, sexo, personalidade, historico, localEncontrado, "Disponível", porte, peso);
        AnimalDAO animalDAO = new AnimalDAO();
        animalDAO.inserir(animal);

        dispose();

    }

}
