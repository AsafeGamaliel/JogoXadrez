
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Windows
 */
public class TabuleiroDesenhado extends JPanel {
    
    private final int TAMANHO_CASA = 70;
    private final int MARGEM = 25;
    private boolean invertido = false;
    
    private String[][] pecas = {
        {"\u265C", "\u265E", "\u265D", "\u265B", "\u265A", "\u265D", "\u265E", "\u265C"},
        {"\u265F", "\u265F", "\u265F", "\u265F", "\u265F", "\u265F", "\u265F", "\u265F"},
        {"", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "", ""},
        {"\u2659", "\u2659", "\u2659", "\u2659", "\u2659", "\u2659", "\u2659", "\u2659"},
        {"\u2656", "\u2658", "\u2657", "\u2655", "\u2654", "\u2657", "\u2656", "\u2658"}
    };

    public TabuleiroDesenhado() {
        int tamanhoTotal = (TAMANHO_CASA * 8) + (MARGEM * 2);
        java.awt.Dimension dimensao = new java.awt.Dimension(tamanhoTotal, tamanhoTotal);
        
        setPreferredSize(dimensao);
        setMinimumSize(dimensao);
        setMaximumSize(dimensao);
        
        setBackground(new Color(45, 45, 45));
        
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                int colVisual = (e.getX() - MARGEM) / TAMANHO_CASA;
                int linVisual = (e.getY() - MARGEM) / TAMANHO_CASA;
                
                if(colVisual >= 0 && colVisual < 8 && linVisual >= 0 && linVisual < 8) {
                    int linhaReal = invertido ? (7 - linVisual) : linVisual;
                    int colReal = invertido ? (7 - colVisual) : colVisual;
                    
                    System.out.println("Clicou na casa: Linha " + linhaReal + ", Coluna " + colReal);
                }
            }
        });
    }
    
    public void girarTabuleiro() {
        this.invertido = !this.invertido;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        String colunas = "ABCDEFGH";
        
        for(int i = 0; i < 8; i++) {
            int indice = invertido ? (7 - i) : i;
            String letra = String.valueOf(colunas.charAt(indice));
            String numero = String.valueOf(8 - indice);
            
            int pos = MARGEM + (i * TAMANHO_CASA) + (TAMANHO_CASA / 2) - 5;
            g2.drawString(letra, pos, 18);
            g2.drawString(letra, pos, MARGEM + (8 * TAMANHO_CASA) + 18);
            g2.drawString(numero, 8, pos + 5);
            g2.drawString(numero, MARGEM + (8 * TAMANHO_CASA) + 8, pos + 5);
        }
        
        g2.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 55));
        
        for(int linVisual = 0; linVisual < 8; linVisual++) {
            for(int colVisual = 0; colVisual < 8; colVisual++) {
                int x = MARGEM + (colVisual * TAMANHO_CASA);
                int y = MARGEM + (linVisual * TAMANHO_CASA);
                
                if((linVisual + colVisual) % 2 == 0) {
                    g2.setColor(new Color(240, 217, 181));
                } else {
                    g2.setColor(new Color(181,136, 99));
                }
                
                g2.fillRect(x, y, TAMANHO_CASA, TAMANHO_CASA);
                
                int linhaReal = invertido ? (7 - linVisual) : linVisual;
                int colReal = invertido ? (7 - colVisual) : colVisual;
                String peca = pecas[linhaReal][colReal];
                
                if(!peca.isEmpty()) {
                    g2.setColor(Color.BLACK);
                    g2.drawString(peca, x + 8, y + 55);
                }
            }
        }
    }
    
}
