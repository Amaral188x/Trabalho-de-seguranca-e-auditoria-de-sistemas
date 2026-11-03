import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class JanelaDestruicao extends JFrame implements ActionListener {
    public JButton btnConfirmar = new JButton("DESTRUIR");
    public JButton btnCancelar = new JButton("CANCELAR");

    public JanelaDestruicao(){
        setTitle("DESTRuIR?");
        setSize(200, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setLocation(1100,150);
        setLayout(null);
        setVisible(true);

        btnConfirmar.setBounds(40,10, 100, 50 );
        btnCancelar.setBounds(40, 90,100,50);

        add(btnConfirmar);
        add(btnCancelar);

        btnConfirmar.setVisible(true);
        btnCancelar.setVisible(true);
        
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
}