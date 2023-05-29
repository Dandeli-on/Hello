package view;

import model.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChessComponent extends JComponent {
    protected PlayerColor owner;

    public boolean selected;
    public boolean beSelected;

    private ImageIcon imageIcon1 =new ImageIcon(getClass().getResource("/Image/Select.png" ));
    private ImageIcon imageIcon2 =new ImageIcon(getClass().getResource("/Image/Select2.png" ));


    public boolean isSelected() {
        return selected;
    }
   public void setBeSelected(boolean beSelected){
        this.beSelected=beSelected;
   }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public boolean isBeSelected() {
        return beSelected;
    }

    public ChessComponent(PlayerColor owner, int size) {
        this.owner = owner;
        this.selected = false;

        setSize(size/2, size/2);
        setLocation(0,0);
        setVisible(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 如果棋子是蓝方的，就画一个蓝色的环
        if (owner == PlayerColor.BLUE) {
            g.setColor(Color.BLUE);
            g.drawOval(0, 0, getWidth(), getHeight());
        }
        // 如果棋子是红方的，就画一个红色的环
        else if (owner == PlayerColor.RED) {
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth(), getHeight());
        }
        if (isSelected()) { // Highlights the model if selected.
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(imageIcon1.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
        if (isBeSelected()) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(imageIcon1.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }


}



