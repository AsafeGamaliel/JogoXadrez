
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
    private boolean vezDasBrancas = true;
    
//    controle de seleção e arrasto
    private int linhaSelecionada = -1, colunaSelecionada = -1;
    private boolean arrastando = false;
    private int mouseX = 0, mouseY = 0;
    
    private PainelTabuleiro painelPai;
    
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

    public TabuleiroDesenhado(PainelTabuleiro painelPai) {
        this.painelPai = painelPai;
        
        int tamanhoTotal = (TAMANHO_CASA * 8) + (MARGEM * 2);
        java.awt.Dimension dimensao = new java.awt.Dimension(tamanhoTotal, tamanhoTotal);
        
        setPreferredSize(dimensao);
        setMinimumSize(dimensao);
        setMaximumSize(dimensao);
        
        setBackground(new Color(45, 45, 45));
        
        MouseAdapter mouseHandler = new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                int colunaVisual = (e.getX() - MARGEM) / TAMANHO_CASA;
                int linhaVisual = (e.getY() - MARGEM) / TAMANHO_CASA;
                if(!dentroDoTabuleiro(linhaVisual, colunaVisual)) return;
                
                
                int linhaReal = linhaVisual;
                int colunaReal = colunaVisual;

//                verifica se ja tem peça selecionada e se clicou em outra casa para mover
                if(linhaSelecionada != -1 && (linhaSelecionada != linhaReal || colunaSelecionada != colunaReal)) {
//                    Se clicou em outra peça da mesma cor, apenas troca a seleção
                    if(pecaEhDaVez(pecas[linhaReal][colunaReal])) {
                        linhaSelecionada = linhaReal;
                        colunaSelecionada = colunaReal;
                        arrastando = true;
                        mouseX = e.getX();
                        mouseY = e.getY();
                    } else {
                        executarMovimento(linhaSelecionada, colunaSelecionada, linhaReal, colunaReal);
                        linhaSelecionada = -1;
                        colunaSelecionada = -1;
                    }
                } else if(pecaEhDaVez(pecas[linhaReal][colunaReal])) {
//                    senão, seleciona a peça se ela pertecenter ao jogador da vez
                    linhaSelecionada = linhaReal;
                    colunaSelecionada = colunaReal;
                    arrastando = true;
                    mouseX = e.getX();
                    mouseY = e.getY();
                }
                repaint();
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if(arrastando) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                    repaint(); // Atualiza a peça seguindo o cursor.
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if(arrastando) {
                    arrastando = false;
                    int colunaVisual = (e.getX() - MARGEM) / TAMANHO_CASA;
                    int linhaVisual = (e.getY() - MARGEM) / TAMANHO_CASA;
                    
                    if(dentroDoTabuleiro(linhaVisual, colunaVisual)) {
                        int linhaReal = linhaVisual;
                        int colunaReal = colunaVisual;
                        
//                        Se soltou em uma casa diferente da origem, efetiva o movimento
                        if(linhaReal != linhaSelecionada || colunaReal != colunaSelecionada) {
                            if(!pecaEhDaVez(pecas[linhaReal][colunaReal])) {
                                executarMovimento(linhaSelecionada, colunaSelecionada, linhaReal, colunaReal);
                                linhaSelecionada = -1;
                                colunaSelecionada = -1;
                            }
                        }
                    }
                    repaint();
                }
            }
        };
        
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }
    
    private boolean dentroDoTabuleiro(int linha, int coluna) {
        return linha >= 0 && linha < 8 && coluna >= 0 && coluna < 8;
    }
    
//    verifica se a peça pertence ao jogador do turno atual
    private boolean pecaEhDaVez(String peca) {
        if(peca.isEmpty()) return false;
        boolean ehBranca = "\u2656\u2658\u2657\u2655\u2654\u2657\u2656\u2658\u2659".contains(peca);
        return vezDasBrancas == ehBranca;
    }
    
    private void executarMovimento(int linhaOriginal, int colunaOriginal, int linhaDestino, int colunaDestino) {
        String pecaMovida = pecas[linhaOriginal][colunaOriginal];
        String pecaCapturada = pecas[linhaDestino][colunaDestino];
        
//        Move a peça na matriz
        pecas[linhaDestino][colunaDestino] = pecaMovida;
        pecas[linhaOriginal][colunaOriginal] = "";
        
//        Formata o texto do movimento
        String colunas = "ABCDEFGH";
        String origemTxt = "" + colunas.charAt(colunaOriginal) + (8 - linhaOriginal);
        String destinoTxt = "" + colunas.charAt(colunaDestino) + (8 - linhaDestino);
        String jogador = vezDasBrancas ? "Brancas" : "Pretas";
        
//        Troca o turno e gira o tabuleiro
        vezDasBrancas = !vezDasBrancas;
        
//        atualiza os textos na tela principal do jogo
        if(painelPai != null) {
            painelPai.atualizarStatusJogo(jogador + ": " + pecaMovida + " " + origemTxt + " -> " + destinoTxt, vezDasBrancas, pecaCapturada);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
//        Coordenadas nas bordas
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        String colunas = "ABCDEFGH";
        
        for(int i = 0; i < 8; i++) {
            int indice = i;
            String letra = String.valueOf(colunas.charAt(indice));
            String numero = String.valueOf(8 - indice);
            
            int pos = MARGEM + (i * TAMANHO_CASA) + (TAMANHO_CASA / 2) - 5;
            g2.drawString(letra, pos, 18);
            g2.drawString(letra, pos, MARGEM + (8 * TAMANHO_CASA) + 18);
            g2.drawString(numero, 8, pos + 5);
            g2.drawString(numero, MARGEM + (8 * TAMANHO_CASA) + 8, pos + 5);
        }
        
//        Casas e peças paradas
        g2.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 55));
        
        for(int linhaVisual = 0; linhaVisual < 8; linhaVisual++) {
            for(int colunaVisual = 0; colunaVisual < 8; colunaVisual++) {
                int x = MARGEM + (colunaVisual * TAMANHO_CASA);
                int y = MARGEM + (linhaVisual * TAMANHO_CASA);
                
                int linhaReal = linhaVisual;
                int colunaReal = colunaVisual;
                
//                Cor da casa (destca em verde se estiver selecionada
                if(linhaReal == linhaSelecionada && colunaReal == colunaSelecionada) {
                    g2.setColor(new Color(130, 180, 90));
                } else if((linhaVisual + colunaVisual) % 2 == 0) {
                    g2.setColor(new Color(240, 217, 181));
                } else {
                    g2.setColor(new Color(181,136, 99));
                }
                
                g2.fillRect(x, y, TAMANHO_CASA, TAMANHO_CASA);
                
//                Desenba a peça (exceto se ela estuver sendo arrastada no momento)
                String peca = pecas[linhaReal][colunaReal];
                boolean estaSendoArrastada = arrastando && (linhaReal == linhaSelecionada && colunaReal == colunaSelecionada);
               
                if(!peca.isEmpty() && !estaSendoArrastada) {
                    g2.setColor(Color.BLACK);
                    g2.drawString(peca, x + 8, y + 55);
                }
            }
        }
        
//        Desenha a peça sendo arrastada por cima de tudo, seguindo o mouse
        if(arrastando && linhaSelecionada != -1) {
            String pecaArrastada = pecas[linhaSelecionada][colunaSelecionada];
            g2.setColor(Color.BLACK);
            g2.drawString(pecaArrastada, mouseX - 22, mouseY + 15);
        }
    }
    
    public void reiniciarTabuleiro() {
        // Recoloca todas as peças nas posições originais
        this.pecas = new String[][] {
            {"\u265C", "\u265E", "\u265D", "\u265B", "\u265A", "\u265D", "\u265E", "\u265C"},
            {"\u265F", "\u265F", "\u265F", "\u265F", "\u265F", "\u265F", "\u265F", "\u265F"},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"\u2659", "\u2659", "\u2659", "\u2659", "\u2659", "\u2659", "\u2659", "\u2659"},
            {"\u2656", "\u2658", "\u2657", "\u2655", "\u2654", "\u2657", "\u2656", "\u2658"}
        };
        
        // Reseta os controles de turno e seleção
        this.vezDasBrancas = true;
        this.linhaSelecionada = -1;
        this.colunaSelecionada = -1;
        this.arrastando = false;
        
        repaint(); // Redesenha o tabuleiro zerado
    }
    
}
