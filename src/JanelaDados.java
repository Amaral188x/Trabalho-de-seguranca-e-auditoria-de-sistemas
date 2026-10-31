import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class JanelaDados extends JFrame implements ActionListener{

    private JTextArea areaTexto = new JTextArea();
    private JScrollPane scroll = new JScrollPane(areaTexto); 
    public static ArrayList<Image> imgFundo = new ArrayList<>();
    private int indice;

    public JanelaDados() {
        setTitle("Dados Capturados do Arquivo TXT");
        setSize(500, 400);
        setLocation(10,500);// Centraliza na tela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        

        Timer timer = new Timer(25, this);
        timer.start();

        JPanel painelFundo = new JPanel(){


            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if(imgFundo != null){
                    g.drawImage(imgFundo.get(indice), 0, 0, getWidth(), getHeight(), null);
                }else {
                    g.setColor(new Color(30, 30, 30));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                repaint();
            }

        };
        

        
       
        painelFundo.setLayout(new BorderLayout());
        

        areaTexto.setEditable(false); 
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaTexto.setOpaque(false);
        areaTexto.setForeground(Color.GREEN);
        scroll.setOpaque(false);

        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelFundo.add(scroll, BorderLayout.CENTER);
        setContentPane(painelFundo);

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

    @Override
    public void actionPerformed(ActionEvent e) {
       if(!imgFundo.isEmpty()){
            if(indice < imgFundo.size() - 1){
                indice ++;
            }else{
                indice = 0;
            }
       }
    }
    
}