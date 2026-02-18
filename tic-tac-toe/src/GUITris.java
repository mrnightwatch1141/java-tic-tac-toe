// Librerie
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUITris implements ActionListener {
    // Attributi
    int larghezzaGriglia  =  600;
    int altezzaGriglia    =  650;
    JFrame      finestra        =  new JFrame();
    Container   contenitore     =  finestra.getContentPane();
    JLabel      lblTesto        =  new JLabel();
    JPanel      pnlTesto        =  new JPanel();
    JPanel      pannelloGriglia =  new JPanel();
    JButton[][] griglia         =  new JButton[3][3];
    
    // Giocatori
    String  giocatoreX = "X";
    String  giocatoreO = "O";
    String  giocatoreAttuale = giocatoreX;
    int     mosse    = 0;
    boolean giocoAttivo = true; // Flag per controllare se il gioco è attivo
    
    // Reset
    JPanel  pannelloReset  =  new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton resetBtn       =  new JButton("Reset");
    
    // Costruttore
    public GUITris() {
        // Proprietà della finestra
        finestra.setTitle("Tris");
        finestra.setVisible(true);
        finestra.setSize(larghezzaGriglia, altezzaGriglia);
        finestra.setLocationRelativeTo(null);
        finestra.setResizable(false);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setLayout(new BorderLayout());
        
        lblTesto.setBackground(Color.WHITE);
        lblTesto.setForeground(Color.BLACK);
        lblTesto.setFont(new Font("Arial", Font.BOLD, 50));
        lblTesto.setHorizontalAlignment(JLabel.CENTER);
        lblTesto.setText("Tris");
        lblTesto.setOpaque(true);
        
        // Layout pannello
        pnlTesto.setLayout(new BorderLayout());
        pnlTesto.add(lblTesto);
        
        // Layout pannello griglia
        pannelloGriglia.setLayout(new GridLayout(3, 3));
        pannelloGriglia.setBackground(Color.WHITE);
        
        int r, c;
        for (r = 0; r < 3; r++) {
            for (c = 0; c < 3; c++) {
                JButton cella = new JButton();
                cella.setBackground(Color.WHITE);
                cella.setForeground(Color.BLACK);
                cella.setFont(new Font("Arial", Font.BOLD, 120));
                cella.setFocusable(false);
                griglia[r][c] = cella;
                pannelloGriglia.add(cella);
                
                cella.addActionListener(this);
            }
        }
        
        pannelloReset.add(resetBtn);
        
        // Inserimento nel contenitore
        contenitore.add(pnlTesto, BorderLayout.NORTH);
        contenitore.add(pannelloGriglia, BorderLayout.CENTER);
        contenitore.add(pannelloReset, BorderLayout.SOUTH);
        
        resetBtn.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent e) {
        // Gestione click su cella
        if (!e.getSource().equals(resetBtn)) {
            JButton cella = (JButton)e.getSource();
            
            // Verifica che il gioco sia attivo e la cella sia vuota
            if (giocoAttivo && cella.getText().isEmpty()) {
                int r, c;
                int x, y;
                boolean trovato = false;
                
                /*
                 * Trovo le coordinate della cella
                 * dove è stato premuto il bottone
                 */
                r = 0;
                x = -1;
                y = -1;
                while (r < 3 && !trovato) {
                    c = 0;
                    while (c < 3 && !trovato) {
                        if (griglia[r][c] == cella) {
                            x = r;
                            y = c;
                            trovato = true;
                        }
                        c++;
                    }
                    r++;
                }
                
                if (trovato) {
                    cella.setText(giocatoreAttuale);
                    cella.setForeground(Color.BLACK);
                    mosse++;
                    
                    if (vincitore(x, y, cella)) {
                        fineGioco();
                    } else if (mosse == 9) {
                        pareggio();
                        fineGioco();
                    } else {
                        cambiaTurno();
                    }
                }
            }
        } else {
            // Gestione reset
            int r, c;
            for (r = 0; r < 3; r++) {
                for (c = 0; c < 3; c++) {
                    griglia[r][c].setText("");
                    griglia[r][c].setBackground(Color.WHITE);
                    griglia[r][c].setForeground(Color.BLACK);
                }
            }
            giocatoreAttuale = giocatoreX;
            mosse = 0;
            giocoAttivo = true; // Riattiva il gioco
            lblTesto.setText("Tris");
        }
    }
    
    void cambiaTurno() {
        if (giocatoreAttuale.equals(giocatoreX)) {
            giocatoreAttuale = giocatoreO;
        } else {
            giocatoreAttuale = giocatoreX;
        }
        lblTesto.setText("È il turno di: " + giocatoreAttuale);
    }
    
    boolean vincitore(int x, int y, JButton cella) {
        int r, c, p, s;
        boolean vittoria = false;
        
        // Righe - Orizzontale
        boolean riga = true;
        for (c = 0; c < 3; c++) {
            if (!griglia[x][c].getText().equals(giocatoreAttuale)) {
                riga = false;
            }
        }
        if (riga) {
            vittoria = true;
            for (c = 0; c < 3; c++) {
                dichiaraVincitore(griglia[x][c]);
            }
        }
        
        // Colonna - Verticale
        boolean colonna = true;
        for (r = 0; r < 3; r++) {
            if (!griglia[r][y].getText().equals(giocatoreAttuale)) {
                colonna = false;
            }
        }
        if (colonna) {
            vittoria = true;
            for (r = 0; r < 3; r++) {
                dichiaraVincitore(griglia[r][y]);
            }
        }
        
        // Diagonale principale
        if (x == y) {
            boolean principale = true;
            for (p = 0; p < 3; p++) {
                if (!griglia[p][p].getText().equals(giocatoreAttuale)) {
                    principale = false;
                }
            }
            if (principale) {
                vittoria = true;
                for (p = 0; p < 3; p++) {
                    dichiaraVincitore(griglia[p][p]);
                }
            }
        }
        
        // Diagonale secondaria
        if (x + y == 2) {
            boolean secondaria = true;
            for (s = 0; s < 3; s++) {
                if (!griglia[s][2 - s].getText().equals(giocatoreAttuale)) {
                    secondaria = false;
                }
            }
            if (secondaria) {
                vittoria = true;
                for (s = 0; s < 3; s++) {
                    dichiaraVincitore(griglia[s][2 - s]);
                }
            }
        }
        return vittoria;
    }
    
    void dichiaraVincitore(JButton cella) {
        lblTesto.setText(giocatoreAttuale + " è il vincitore!");
        cella.setBackground(Color.GRAY);
        cella.setForeground(Color.GREEN);
    }
    
    void pareggio() {
        int r, c;
        lblTesto.setText("Pareggio!");
        for (r = 0; r < 3; r++) {
            for (c = 0; c < 3; c++) {
                griglia[r][c].setBackground(Color.GRAY);
                griglia[r][c].setForeground(Color.ORANGE);
            }
        }
    }
    
    void fineGioco() {
        giocoAttivo = false; // Disattiva il gioco
    }
}