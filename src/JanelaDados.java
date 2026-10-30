import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JanelaDados extends JFrame {

    private JTextArea areaTexto = new JTextArea();
    private JScrollPane scroll = new JScrollPane(areaTexto); 

    public JanelaDados() {
        setTitle("Dados Capturados do Arquivo TXT");
        setSize(500, 400);
        setLocationRelativeTo(null); // Centraliza na tela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas esta janela ao clicar no X

        areaTexto.setEditable(true); 
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 14));

        add(scroll);

        // Carrega o conteúdo do arquivo txt
        carregarArquivo("dados.txt"); 
    }

    private void carregarArquivo(String caminhoArquivo) {
        File arquivo = new File(caminhoArquivo);

        if (!arquivo.exists()) {
            areaTexto.setText("O arquivo " + caminhoArquivo + " ainda não foi criado ou está vazio.");
            return;
        }

        StringBuilder conteudo = new StringBuilder();

        try (Scanner leitor = new Scanner(arquivo)) {
            while (leitor.hasNextLine()) {
                conteudo.append(leitor.nextLine()).append("\n");
            }
            areaTexto.setText(conteudo.toString());
        } catch (FileNotFoundException e) {
            areaTexto.setText("Erro ao tentar ler o arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}