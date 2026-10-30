import javax.sound.sampled.*;
import java.net.URL;

public class Som {

    private Clip clip;
    private AudioInputStream audioStream;
    private volatile boolean tocando = false;   // som realmente em reprodução
    private volatile boolean carregado = false; // som foi pedido, mas ainda não começou

    public void tocarSom(String caminhoArquivo) {
        if (caminhoArquivo == null || caminhoArquivo.trim().isEmpty()) {
            System.err.println("O caminho do áudio não pode estar vazio!");
            return;
        }

        try {
            // Libera o som anterior, caso ainda exista.
            liberarRecursos();

            URL arquivo = getClass().getResource(caminhoArquivo);

            if (arquivo == null) {
                System.err.println("Arquivo de áudio não encontrado: " + caminhoArquivo);
                return;
            }

            audioStream = AudioSystem.getAudioInputStream(arquivo);

            clip = AudioSystem.getClip();
            // Marca quando o som começa e quando termina.
            clip.addLineListener(new javax.sound.sampled.LineListener() {
                @Override
                public void update(LineEvent evento) {
                    if (evento.getType() == LineEvent.Type.START) {
                        tocando = true;
                    } else if (evento.getType() == LineEvent.Type.STOP) {
                        tocando = false;
                        carregado = false;
                    }
                }
            });
            
            clip.open(audioStream);
            clip.setFramePosition(0);
            clip.start();

            // Importante: o som é considerado "em andamento" a partir daqui,
            // mesmo antes do evento START chegar (evita pular estados).
            carregado = true;

        } catch (Exception e) {
            System.err.println("Erro ao carregar o som: " + e.getMessage());
            liberarRecursos();
        }
    }

    public boolean estaTocando() {
        // Enquanto houver um som carregado, considera como "em andamento",
        // mesmo nos instantes antes do evento START chegar.
        if (clip != null && carregado) {
            return true;
        }

        return tocando;
    }

    public long getPosicaoMilisegundos() {
        if (clip != null && tocando) {
            return clip.getMicrosecondPosition() / 1000;
        }

        return 0;
    }

    private void liberarRecursos() {
        if (clip != null) {
            try {
                clip.stop();
                clip.close();
            } catch (Exception e) {
                // Ignora: o clip já pode estar fechado pelo LineListener.
            }
            clip = null;
        }

        tocando = false;
        carregado = false;

        if (audioStream != null) {
            try {
                audioStream.close();
            } catch (Exception e) {
                System.err.println("Erro ao fechar o áudio: " + e.getMessage());
            }

            audioStream = null;
        }
    }
}