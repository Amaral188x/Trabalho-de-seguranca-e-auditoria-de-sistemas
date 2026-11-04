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
    private ArrayList<Image> btnConfirmarFrames = new ArrayList<>();
    private int btnConfirmarId = 1;

    public JanelaDestruicao(){
        tela.setTitle("DESTRuIR?");
        tela.setSize(200, 200);
        
        tela.setResizable(false);
        tela.setLocation(1100,150);
        tela.setContentPane(this);
        tela.setLayout(null);
        tela.setVisible(true);
        

        btnConfirmar.setBounds(40,10, 100, 50 );
        btnCancelar.setBounds(40, 90,100,50);

        tela.add(btnConfirmar);
        tela.add(btnCancelar);

        btnConfirmar.setVisible(true);
        btnCancelar.setVisible(true);

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
                        Thread.sleep(18);
                    }
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
        }catch(Exception ERR){
            System.err.println("ERRO NO SISTEMA DE ANIMAÇÃO DO BOTÃO DE CONFIRMAÇÃO NA CLASSE JANELADESTRUICAO! CÓDIGO DE ERRO: " + ERR);
            ERR.printStackTrace();
        }
        repaint();
    }
}