
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Windows
 */
public class LabelCortado extends JLabel {

    public LabelCortado(String texto) {
        super(texto, SwingConstants.CENTER);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(15,50,15,50));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        double w = getWidth();
        double h = getHeight();
        
        Path2D clip = new Path2D.Double();
        clip.moveTo(0, 0);
        clip.lineTo(w, 0);
        clip.lineTo(w - 40.0, h / 2.0);
        clip.lineTo(w, h);
        clip.lineTo(0, h);
        clip.lineTo(40.0, h / 2.0);
        clip.closePath();
        
        g2d.setClip(clip);
        
        g2d.setColor(new Color(255,255,255));
        g2d.fillRect(0,0,(int) w, (int) h);
        
        g2d.setColor(new Color(255, 255, 255));
        g2d.setStroke(new BasicStroke(1f));
        g2d.draw(clip);
        
        g2d.dispose();
        super.paintComponent(g);
    }
    
}
