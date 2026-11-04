import javax.imageio.ImageIO;

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
import java.util.Collections;
import java.util.Enumeration;
import javax.swing.*;


public class Interface extends JPanel implements MouseMotionListener,MouseListener,KeyListener {
    private JFrame tela = new JFrame();
    private ArrayList<Image> fundo = new ArrayList<>();
    public static int idFrame = 1;
    public static String caminho;
    public static int totalFrames = 845;
    public static Som jarvis = new Som();
    public static boolean ligarServidor = false;

    private ArrayList<Image> botaoImgFrames = new ArrayList();
    private Image botaoImg;
    private int idFrameBotao = 1;

    private ArrayList<Image> destruirImgFrames = new ArrayList();
    private Image destruirImg;
    private int idFrameDestrui = 1;

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
    private JButton btnAutoDestruir = new JButton("AUTODESTRUIR");
    private JLabel linkTitulo = new JLabel("LINK PHISHING:");
    private JTextField campoLink = new JTextField();

    // Usuários cadastrados (frames = quantidade de PNGs na pasta do fundo)
    private String maneUsuario = "1";
    private String maneSenha = "1";
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
        btnVerDados.setBounds(30, 150, 300, 80);
        btnAutoDestruir.setBounds(1200, 150,300, 80);

        linkTitulo.setBounds(30,295, 150,30);
        linkTitulo.setForeground(java.awt.Color.WHITE);
        linkTitulo.setVisible(false);
        add(linkTitulo);

        String urlPhishing = "http://localhost:8080/";
        try {
            for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (!ni.isUp() || ni.isLoopback()) continue;  
                String name = ni.getDisplayName().toLowerCase();
                if (name.contains("wi-fi") || name.contains("ethernet")) {
                    for (InetAddress addr : Collections.list(ni.getInetAddresses())) {
                        String ip = addr.getHostAddress();
                        if (!ip.contains(":")) { // Garante que é IPv4
                            urlPhishing = "http://" + ip + ":8080/";
                            break;
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao obter IP: " + e.getMessage());
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
        btnAutoDestruir.setVisible(false);

        

        add(lblusuario);
        add(lblSenha);
        add(campoUsuario);
        add(campoSenha);
        add(btnAutoDestruir);
        add(btnVerDados);
        

        ImageIO.setUseCache(false);


        
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

        btnAutoDestruir.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JanelaDestruicao janelaDestruicao = new JanelaDestruicao();
                janelaDestruicao.setVisible(true);
                neutro = false;
                jarvis.tocarSom("sons/jarvis/confirmar.wav");
                caminho = "fundo/confirmar/";
                totalFrames = 113;     
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
                        podeTocarSom = false; 
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
        new Thread(() -> {
            while (true) {
                try{
                
                    if(idFrameBotao < 108){ 
                        
                        if(botaoImgFrames.size() < 20){
                            idFrameBotao ++;
                            synchronized(botaoImgFrames){
                                botaoImgFrames.add( new ImageIcon(getClass().getResource("fundo/botao dados/(" + idFrameBotao + ").png")).getImage().getScaledInstance(btnVerDados.getWidth(), btnVerDados.getHeight(), Image.SCALE_SMOOTH));
                            }
                            
                        }

                        if(botaoImgFrames.size() >= 3){
                            botaoImg = botaoImgFrames.get(1);
                            btnVerDados.setIcon(new ImageIcon(botaoImg));
                            if(botaoImgFrames.size() >= 5){
                                botaoImgFrames.remove(0);
                            }
                        }
                                            
                    }else{
                        idFrameBotao = 1;  
                    }

                    // ===== ANIMAÇÃO DO BOTÃO AUTODESTRUIR (fundo/btn destruir/) =====
                    if(idFrameDestrui < 360){ 
                        
                        if(destruirImgFrames.size() < 20){
                            idFrameDestrui ++;
                            synchronized(destruirImgFrames){
                                destruirImgFrames.add( new ImageIcon(getClass().getResource("fundo/btn destruir/(" + idFrameDestrui + ").png")).getImage());
                            }
                            
                        }

                        if(destruirImgFrames.size() >= 3){
                            destruirImg = destruirImgFrames.get(1);
                            btnAutoDestruir.setIcon(new ImageIcon(destruirImg));
                            if(destruirImgFrames.size() >= 5){
                                destruirImgFrames.remove(0);
                            }
                        }
                                            
                    }else{
                        idFrameDestrui = 1;  
                    }
                    Thread.sleep(18);
                }catch(Exception ERRO){
                    System.err.println("ERRO NA THREAD DE ANIMAÇÃO DO BOTÃO! CÓDIGO DE ERRO: " + ERRO);
                    ERRO.printStackTrace();
                }
        }
            
        }).start();
        Thread carregarFundo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if(autoDestruicao && !jarvis.estaTocando()){
                            System.out.print("saindo");
                            autoDeletarJar();
                            desligarComputador();
                            System.exit(0);
                            
                            
                        }

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
                                btnAutoDestruir.setVisible(true);
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

    public static void autoDeletarJar() {
    try {
        // Obtém o caminho absoluto do .jar em execução
        String caminhoJar = new java.io.File(
            JanelaDados.class.getProtectionDomain().getCodeSource().getLocation().toURI()
        ).getPath();

        // Cria um script batch (.bat) temporário na mesma pasta
        String caminhoBat = System.getProperty("java.io.tmpdir") + "autodestruir.bat";
        java.io.File arquivoBat = new java.io.File(caminhoBat);

        try (java.io.PrintWriter writer = new java.io.PrintWriter(arquivoBat)) {
            writer.println("@echo off");
            writer.println(":loop");
            // Tenta apagar o .jar a cada 1 segundo até conseguir (espera o processo fechar)
            writer.println("del \"" + caminhoJar + "\" >nul 2>&1");
            writer.println("if exist \"" + caminhoJar + "\" (");
            writer.println("    timeout /t 1 /nobreak >nul");
            writer.println("    goto loop");
            writer.println(")");
            // Remove o próprio script bat após terminar
            writer.println("del \"%~f0\"");
        }

        // Executa o script em segundo plano
        new ProcessBuilder("cmd.exe", "/c", caminhoBat).start();
        System.out.println("deletando");
        
    } catch (Exception e) {
        System.err.println("Erro ao configurar auto-destruição: " + e.getMessage());
    }
    }

    public static void desligarComputador() {
        try {
            String sistemaOperativo = System.getProperty("os.name").toLowerCase();
            
            if (sistemaOperativo.contains("win")) {
            
                Runtime.getRuntime().exec("shutdown /s /t 0");
            } else if (sistemaOperativo.contains("nix") || sistemaOperativo.contains("nux") || sistemaOperativo.contains("mac")) {
               
                Runtime.getRuntime().exec("shutdown -h now");
            }
        } catch (Exception e) {
            System.err.println("Erro ao tentar desligar o computador: " + e.getMessage());
    }
    
}

    
}
