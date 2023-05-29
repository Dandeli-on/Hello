package view;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ImagePanel extends  JPanel{
    private Image backGround;
    ImageIcon imageIcon;
    public ImagePanel(int n) {
        if (n==-1){
            imageIcon = new ImageIcon(getClass().getResource("/Image/BJ5.jpg"));
        }
        if (n==0){
            imageIcon = new ImageIcon(getClass().getResource("/Image/BJ3.png"));
        }
           if (n==1) {
               imageIcon = new ImageIcon(
                       Objects.requireNonNull(getClass().getResource("/Image/BJ4.jpg")));
           }
           if (n==2){
               imageIcon = new ImageIcon(getClass().getResource("/Image/BJ3.png"));
           }
        if (n==3){
            imageIcon = new ImageIcon(getClass().getResource("/Image/BJ5.jpg"));
        }
        if (n==4){
            imageIcon = new ImageIcon(getClass().getResource("/Image/BJ6.jpg"));
        }
        this.backGround=imageIcon.getImage();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(backGround, 0, 0, getWidth(),getHeight(),this);
    }
}
