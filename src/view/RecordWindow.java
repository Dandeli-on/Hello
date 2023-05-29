package view;

import model.PlayerColor;

import javax.swing.*;
import java.awt.*;

public class RecordWindow extends JFrame {
    private final int WIDTH;
    private final int HEIGTH;
    private String string;
    public RecordWindow(int width, int height,String string,int n) {
        setTitle("Win!");
        this.WIDTH = width;
        this.HEIGTH = height;
         this.string=string;
        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);
        addLabel(string,n);
    }
    public void addLabel(String string1,int n) {
        JLabel statusLabel = new JLabel(string1);
        statusLabel.setLocation(10, 10+15*n);
        statusLabel.setSize(300, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(statusLabel);
    }
}
