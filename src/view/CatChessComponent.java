package view;

import model.PlayerColor;

import javax.swing.*;
import java.awt.*;

public class CatChessComponent extends ChessComponent {
    private PlayerColor owner;

    private ImageIcon imageIcon;

    public CatChessComponent(PlayerColor owner, int size) {
        super(owner,size);
        imageIcon = new ImageIcon(getClass().getResource("/Image/C.png" ));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the GIF image
        if  (imageIcon!= null) {
            g.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
        }

    }
}

