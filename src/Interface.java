import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Interface principal do sistema.
 * Estados: iniciando -> login -> autenticando -> carregando menu -> menu.
 * A tecla 0 (ou 3 erros no login) ativa a auto-destruição.
 */
public class Interface extends JPanel implements MouseMotionListener,MouseListener,KeyListener {
    private JFrame tela = new JFrame();
    private ArrayList<Image> fundo = new ArrayList<>();
    private int idFrame = 1;
    private static String caminho;
    private static int totalFrames = 845;
    private static Som jarvis = new Som();
    public static boolean ligarServidor = false;

    // Estados da interface
    private static boolean neutro = false;
    private boolean iniciando = true;
    private boolean autoDestruicao = false;
    private boolean carregarMenu = false;
    private boolean mostrarMenu = false;
    private boolean autenticando = false;  // som do usuário está tocando
    private boolean falhaLogin = false;    // som de tentativa errada está tocando
    private boolean mostrarLogin = false;
    private boolean podeTocarSom = true;
    private boolean botaoAdicionado = false;
    private int tentativas = 3;

    private JLabel lblusuario = new JLabel("USUÁRIO:");
    private JLabel lblSenha = new JLabel("SENHA:");
    private JTextField campoUsuario = new JTextField();
    private JPasswordField campoSenha = new JPasswordField();
    private JButton btnVerDados = new JButton("VER DADOS");
    private JLabel linkTitulo = new JLabel("LINK PHISHING:");
    private JTextField campoLink = new JTextField();

    // Usuários cadastrados (frames = quantidade de PNGs na pasta do fundo)
    private String maneUsuario = "emanuel";
    private String maneSenha = "007";
    private String maneSom = "sons/jarvis/emanuel.wav";
    private String maneFundo = "fundo/emanuel/";
    private int maneFrames = 140;

    private String luisUsuario = "luis";
    private String luisSenha = "1233";
    private String luisSom = "sons/jarvis/luis.wav";
    private String luisFundo = "fundo/luis/";
    private int luisFrames = 128;

    private String amaralUsuario = "amaral";
    private String amaralSenha = "402893";
    private String amaralSom = "sons/jarvis/amaral.wav";
    private String amaralFundo = "fundo/amaral/";
    private int amaralFrames = 129;

    public Interface(){
        // Janela
        tela.setSize(1920, 1080);
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tela.setResizable(false);
        tela.setLocationRelativeTo(null);
        tela.setTitle("PHISHING 2000");

        setLayout(null);
        tela.add(this);
        tela.setVisible(true);

        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

        // Componentes de login
        lblusuario.setBounds(30, 30, 100, 30);
        lblusuario.setForeground(java.awt.Color.WHITE);
        lblSenha.setBounds(30, 100, 100, 30);
        lblSenha.setForeground(java.awt.Color.WHITE);
        campoUsuario.setBounds(90, 40, 500, 25);
        campoSenha.setBounds(90, 105, 500, 25);
        btnVerDados.setBounds(90, 150, 150, 30);

        linkTitulo.setBounds(30,295, 150,30);
        linkTitulo.setForeground(java.awt.Color.WHITE);
        linkTitulo.setVisible(false);
        add(linkTitulo);

        String urlPhishing = "http://localhost:8080/";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                if (ni.isUp() && !ni.isLoopback() && ni.getDisplayName().toLowerCase().contains("wi-fi")) {
                    Enumeration<InetAddress> addresses = ni.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress addr = addresses.nextElement();
                        String ip = addr.getHostAddress();
                        if (ip.indexOf(':') < 0) {
                            urlPhishing = "http://" + ip + ":8080/";
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao obter IP do Wi-Fi: " + e.getMessage());
        }

        campoLink.setBounds(30,300,320,35);
        campoLink.setText(urlPhishing);
        campoLink.setEditable(false);
        campoLink.setVisible(false);
        campoLink.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 12));
        add(campoLink);

        campoLink.setForeground(java.awt.Color.GREEN);
        campoLink.setBackground(java.awt.Color.DARK_GRAY);

        lblusuario.setVisible(false);
        lblSenha.setVisible(false);
        campoUsuario.setVisible(false);
        campoSenha.setVisible(false);
        btnVerDados.setVisible(false);

        

        add(lblusuario);
        add(lblSenha);
        add(campoUsuario);
        add(campoSenha);
        add(btnVerDados);

        ImageIO.setUseCache(false);


        // Abrir a janela de dados pelo botão
        btnVerDados.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JanelaDados janela = new JanelaDados();
                janela.setVisible(true);
                neutro = false;
                jarvis.tocarSom("sons/jarvis/mostrando dados.wav");
                caminho = "fundo/mostrando dados/";
                totalFrames = 57;

                if(!jarvis.estaTocando()){
                    neutro = true;
                    caminho = "fundo/neutro/";
                    totalFrames = 844;
                }
            }
        });

        // Enter nos campos faz o login
        ActionListener acaoLogin = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!mostrarLogin){
                    return;
                }

                String usuarioDigitado = campoUsuario.getText();
                String senhaDigitada = new String(campoSenha.getPassword());

                if(usuarioDigitado.equals(maneUsuario) && senhaDigitada.equals(maneSenha)){
                    jarvis.tocarSom(maneSom);
                    caminho = maneFundo;
                    totalFrames = maneFrames;
                }
                else if(usuarioDigitado.equals(luisUsuario) && senhaDigitada.equals(luisSenha)){
                    jarvis.tocarSom(luisSom);
                    caminho = luisFundo;
                    totalFrames = luisFrames;
                }
                else if(usuarioDigitado.equals(amaralUsuario) && senhaDigitada.equals(amaralSenha)){
                    jarvis.tocarSom(amaralSom);
                    caminho = amaralFundo;
                    totalFrames = amaralFrames;
                }
                else {
                    tentativas--;

                    if(tentativas == 2){
                        jarvis.tocarSom("sons/jarvis/2 tentativas.wav");
                        caminho = "fundo/restam 2 tentativas/";
                        totalFrames = 131;
                        falhaLogin = true;
                    }
                    else if(tentativas == 1){
                        jarvis.tocarSom("sons/jarvis/1 tentativa.wav");
                        caminho = "fundo/resta 1 tentativa/";
                        totalFrames = 120;
                        falhaLogin = true;
                    }
                    else if(tentativas <= 0){
                        jarvis.tocarSom("sons/jarvis/destruicao.wav");
                        caminho = "fundo/auto destruicao/";
                        totalFrames = 220;
                        autoDestruicao = true;
                        podeTocarSom = false; // o som já foi tocado aqui
                        mostrarLogin = false;
                        esconderCampos();
                    }
                }

                if(!autoDestruicao){
                    if(!falhaLogin){
                        // Login aceito: esconde os campos e depois carrega o menu
                        mostrarLogin = false;
                        esconderCampos();
                        autenticando = true;
                    }
                    neutro = false;
                    idFrame = 1;
                }
            }
        };

        campoUsuario.addActionListener(acaoLogin);
        campoSenha.addActionListener(acaoLogin);

        // Inicia a thread que carrega os frames e controla os estados
        Thread carregarFundo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // ===== ESTADO: INICIANDO =====
                        if(iniciando){
                            neutro = false;
                            caminho = "fundo/iniciando sistema/";
                            totalFrames = 47;

                            if(podeTocarSom){
                                jarvis.tocarSom("sons/jarvis/iniciando sistema.wav");
                                podeTocarSom = false;
                            }
                        }

                        // Quando a animação de início termina, mostra o login
                        if(!iniciando && !neutro && !autenticando && !falhaLogin && !autoDestruicao && !carregarMenu && !mostrarMenu){
                            if(!mostrarLogin){
                                neutro = true;
                                podeTocarSom = true;
                                totalFrames = 845;
                                caminho = "fundo/neutro/";
                                mostrarLogin = true;
                                lblusuario.setVisible(true);
                                lblSenha.setVisible(true);
                                campoUsuario.setVisible(true);
                                campoSenha.setVisible(true);
                                campoUsuario.requestFocusInWindow();
                            }
                        }

                        // ===== ESTADO: CARREGANDO MENU =====
                        if(carregarMenu){
                            if(!jarvis.estaTocando()){
                                neutro = false;
                                caminho = "fundo/carregando interface/";
                                totalFrames = 138;

                                if(podeTocarSom){
                                    jarvis.tocarSom("sons/jarvis/carregando interface.wav");
                                    podeTocarSom = false;
                                    
                                }
                            }
                        }

                        // ===== ESTADO: MENU =====
                        if(mostrarMenu){
                            neutro = false;
                            caminho = "fundo/pronto/";
                            totalFrames = 57;

                            if(podeTocarSom){
                                jarvis.tocarSom("sons/jarvis/pronto.wav");
                                podeTocarSom = false;
                                ligarServidor = true;
                            }

                            if(!botaoAdicionado){
                                botaoAdicionado = true;
                                btnVerDados.setVisible(true);
                                campoLink.setVisible(true);
                            }
                            if(!jarvis.estaTocando()){
                                neutro = true;
                                caminho = "fundo/neutro/";
                                totalFrames = 844;
                            }
                        }

                        // ===== ESTADO: AUTO-DESTRUICAO (tecla 0) =====
                        if(autoDestruicao && !iniciando && podeTocarSom){
                            caminho = "fundo/auto destruicao/";
                            totalFrames = 220;
                            jarvis.tocarSom("sons/jarvis/destruicao.wav");
                            podeTocarSom = false;
                            if(!jarvis.estaTocando()){
                                neutro = true;
                                caminho = "fundo/neutro/";
                                totalFrames = 844;
                            }
                        }

                        

                        // ===== AVANÇO DOS FRAMES =====
                        // Com som: sincroniza com o áudio. Sem som: avança pelo frame rate.
                        if(jarvis.estaTocando()){
                            long tempoAtualMs = jarvis.getPosicaoMilisegundos();
                            idFrame = (int) (tempoAtualMs / 33.33) + 1;
                        }
                        else {
                            if(neutro || mostrarMenu || iniciando){
                                idFrame++;
                            }
                        }

                        // ===== SOM DO USUÁRIO ACABOU -> CARREGA O MENU =====
                        if(autenticando && !jarvis.estaTocando() && idFrame >= totalFrames - 1){
                            autenticando = false;
                            carregarMenu = true;
                            podeTocarSom = true;
                            idFrame = 1;
                        }

                        // ===== FIM DA ANIMAÇÃO DE INICIO (passa pro login) =====
                        if(iniciando && !jarvis.estaTocando() && idFrame >= totalFrames - 1){
                            iniciando = false;
                        }

                        // ===== SOM DE TENTATIVA ERRADA ACABOU -> VOLTA AO LOGIN =====
                        if(falhaLogin && !jarvis.estaTocando()){
                            falhaLogin = false;
                            neutro = true;
                            podeTocarSom = true;
                            totalFrames = 845;
                            caminho = "fundo/neutro/";
                            idFrame = 1;
                            mostrarLogin = true;
                            lblusuario.setVisible(true);
                            lblSenha.setVisible(true);
                            campoUsuario.setVisible(true);
                            campoSenha.setVisible(true);
                            campoUsuario.setText("");
                            campoSenha.setText("");
                            campoUsuario.requestFocusInWindow();
                        }

                        // ===== AUTO-DESTRUICAO TERMINOU -> VOLTA AO LOGIN =====
                        if(autoDestruicao && !jarvis.estaTocando() && podeTocarSom == false && idFrame >= totalFrames - 1){
                            autoDestruicao = false;
                            neutro = true;
                            podeTocarSom = true;
                            totalFrames = 845;
                            caminho = "fundo/neutro/";
                            idFrame = 1;
                            mostrarLogin = true;
                           
                        }

                        // ===== MENU CARREGADO -> MOSTRA O BOTAO =====
                        if(carregarMenu && !jarvis.estaTocando() && podeTocarSom == false && idFrame >= totalFrames - 1){
                            carregarMenu = false;
                            
                            mostrarMenu = true;
                            
                            podeTocarSom = true;
                            idFrame = 1;
                        }

                        // ===== LIMITES DOS FRAMES =====
                        if(idFrame >= totalFrames || idFrame <= 0){
                            if(neutro || mostrarMenu){
                                idFrame = 1; // Loop normal
                            }
                            else if(iniciando){
                                idFrame = 1; // Loop do início (espera o som acabar)
                            }
                            else {
                                idFrame = totalFrames - 1; // Congela no último frame
                            }
                        }

                        // ===== CARREGA O FRAME ATUAL =====
                        if(fundo.size() < 20 && caminho != null){
                            if(idFrame <= 0 || idFrame >= totalFrames){
                                idFrame = 1;
                            }

                            BufferedImage frameTemp = ImageIO.read(getClass().getResource(caminho + "(" + idFrame + ").png"));

                            synchronized(fundo) {
                                fundo.add(frameTemp);
                            }
                        }

                        repaint();
                        Thread.sleep(18);

                    } catch(Exception ERRO) {
                        System.err.println("ERRO AO CARREGAR FUNDO! CÓDIGO DE ERRO: " + ERRO);
                    }
                }
            }
        });

        carregarFundo.setDaemon(true);
        carregarFundo.start();
    }

    // Esconde os campos de login
    private void esconderCampos(){
        lblusuario.setVisible(false);
        lblSenha.setVisible(false);
        campoUsuario.setVisible(false);
        campoSenha.setVisible(false);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        if(fundo.size() >= 3){
            g.drawImage(fundo.get(1),0, 0, getWidth(), getHeight(),null);
            if(fundo.size() >= 5){
                fundo.remove(0);
            }
        }
    }


    //MOUSE

    @Override
    public void mouseDragged(MouseEvent e) {
       
    }

    @Override
    public void mouseMoved(MouseEvent e) {
       
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
        

    @Override
    public void mousePressed(MouseEvent e) {
       
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      
    }

    @Override
    public void mouseExited(MouseEvent e) {
       
    }

    //TECLADO (easter egg: tecla 0 = auto-destruição)
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_0){
            System.out.println("pressionado");

            neutro = false;
            iniciando = false;
            carregarMenu = false;
            mostrarMenu = false;
            autenticando = false;
            falhaLogin = false;

            autoDestruicao = true;
            podeTocarSom = true; // deixa a thread tocar o som da destruição
            esconderCampos();
            mostrarLogin = false;
            idFrame = 1;

            synchronized(fundo){
                fundo.clear();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
       
    }

    public static void notificarLoginRecebido(){
        neutro = false;
        caminho = "fundo/dados capturados/";
        jarvis.tocarSom("sons/jarvis/dados capturados.wav");
        totalFrames = 80;
        
    }

    
}
