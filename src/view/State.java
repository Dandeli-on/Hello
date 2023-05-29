package view;

import javax.swing.*;
import java.awt.*;

public class State extends JPanel {
    private ImageIcon imageIcon = new ImageIcon(getClass().getResource("/Image/BJ3.png"));
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawString("Time: "+Tool.Time,500,20);
        g.drawString("Round: "+Tool.round,700,20);
        if (Tool.Player==1){
        g.drawString("Blue Player",300,20);}
        else {g.drawString("Red Player",300,20);}
    }
}
