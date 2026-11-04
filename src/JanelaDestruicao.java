import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class JanelaDestruicao extends JPanel implements ActionListener {
    private JFrame tela = new JFrame();
    public JButton btnConfirmar = new JButton("DESTRUIR");
    public JButton btnCancelar = new JButton("CANCELAR");
    private int idFrameFundo = 1;
    private ArrayList<Image> fundoFrames = new ArrayList<>();
    private Image btnConfirmarImg;
    private Image btnCancelarImg;
    private ArrayList<Image> btnConfirmarFrames = new ArrayList<>();
     private ArrayList<Image> btnCancelarFrames = new ArrayList<>();
    private int btnConfirmarId = 1;
    private int btnCancelarId = 1;

    public boolean destruir = false;
    public boolean cancelar = false;

    public JanelaDestruicao(){
        tela.setTitle("DESTRuIR?");
        tela.setSize(1920, 1080);
        
        tela.setResizable(true);
        tela.setLocationRelativeTo(null);
        tela.setContentPane(this);
        tela.setLayout(null);
        tela.setVisible(true);
        

        btnConfirmar.setBounds(650,305, 240, 120 );
        btnCancelar.setBounds(650, 415,240,120);

        tela.add(btnConfirmar);
        tela.add(btnCancelar);

        btnConfirmar.setVisible(true);
        
        btnCancelar.setVisible(true);


        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        destruir = true;
        Interface.caminho = "fundo/honra/";
        Interface.totalFrames = 113;
        
      
        new Thread(() -> {
           
            Interface.jarvis.tocarSom("sons/jarvis/honra.wav");
            
          
            try {
                Thread.sleep(3000); 
            } catch (InterruptedException ERR) {
                ERR.printStackTrace();
            }
            
            
            System.exit(0);
        }).start();
    }
});

        btnCancelar.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelar = true;
                tela.dispose();
               
            }
            
        });

        Timer timer = new Timer(18,this);
        timer.start();

        //Thread que carrega o fundo e os frames dos botoes
        new Thread(() ->{
            while(true){
                try{
                    if(fundoFrames.size() < 20){
                        if(idFrameFundo < 600){
                            idFrameFundo ++;
                            BufferedImage img = ImageIO.read(getClass().getResource("fundo/confirmar fundo/(" + idFrameFundo + ").png"));
                            synchronized(fundoFrames){
                                fundoFrames.add(img);
                            }                         
                        }else{
                            idFrameFundo = 1;
                        }
                    }
                    Thread.sleep(18);
                }catch(Exception ERRO){
                    System.err.println("ERRO NA CLASSE JANELADESTRUICAO NA THREAD! CÓDIGO DE ERRO: " + ERRO);
                    ERRO.printStackTrace();
                }
            }
        }).start();    
    }
    @Override 
    public void paintComponent(Graphics g){
        super.paintComponents(g);
        if(fundoFrames.size() >= 3){
            g.drawImage(fundoFrames.get(1), 0, 0, getWidth(), getHeight(), null);
            if(fundoFrames.size() >= 5){
                fundoFrames.remove(0);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            if(btnConfirmarFrames. size() < 20){
                if(btnConfirmarId < 360){
                    btnConfirmarId ++;
                    BufferedImage img = ImageIO.read(getClass().getResource("fundo/btn confirmar/(" + btnConfirmarId + ").png"));
                    btnConfirmarFrames.add(img);
                    btnConfirmarImg = btnConfirmarFrames.get(0);
                }else{
                    btnConfirmarId = 1;
                }
            }

            if(btnConfirmarFrames.size() >= 3){
                btnConfirmar.setIcon(new ImageIcon(btnConfirmarImg));
                if(btnConfirmarFrames.size() >= 5){
                    btnConfirmarFrames.remove(0);
                }
            }


            if(btnCancelarFrames.size() < 20){
                if(btnCancelarId < 480){
                    btnCancelarId ++;
                    BufferedImage img = ImageIO.read(getClass().getResource("fundo/btn cancelar/(" + btnCancelarId + ").png"));
                    btnCancelarFrames.add(img);
                    btnCancelarImg = btnCancelarFrames.get(0);
                }else{
                    btnCancelarId = 1;
                }
            }

            if(btnCancelarFrames.size() >= 3){
                btnCancelar.setIcon(new ImageIcon(btnCancelarImg));
                if(btnCancelarFrames.size() >= 5){
                    btnCancelarFrames.remove(0);
                }
            }


        }catch(Exception ERR){
            System.err.println("ERRO NO SISTEMA DE ANIMAÇÃO DO BOTÃO DE CONFIRMAÇÃO NA CLASSE JANELADESTRUICAO! CÓDIGO DE ERRO: " + ERR);
            ERR.printStackTrace();
        }
        repaint();
    }
}