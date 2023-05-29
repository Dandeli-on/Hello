package view;

import controller.GameController;
import model.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WrongWindow extends JFrame{
    private final int WIDTH;
    private final int HEIGTH;
    private final int buttonWidth=150;
    private final int buttonHight=50;
    private String string;
    private ImagePanel mainPanel = new ImagePanel(-1);
    public WrongWindow(int width, int height, String string) {
        setTitle("错了错了");
        this.WIDTH = width;
        this.HEIGTH = height;
        setSize(WIDTH, HEIGTH);
        this.string=string;
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);
        addLabel(string);
        setContentPane(mainPanel);
        mainPanel.setLayout(null);
        addExitButton();
    }
    public void addExitButton(){
        JButton ExitButton=new JButton("Exit");
        Mousemotion(ExitButton);
        mainPanel.add(ExitButton);
        ExitButton.setLocation(HEIGTH/10, HEIGTH / 10 );
        ExitButton.setSize(buttonWidth,buttonHight);
        ExitButton.setFont(new Font("Rockwell",Font.BOLD,20));
        ExitButton.setContentAreaFilled(false);
        ExitButton.setBorder(null);
        ExitButton.setForeground(Color.MAGENTA);
        ExitButton.addActionListener(e -> {
            System.out.println("Click");
            dispose();
        });
    }

    private void addLabel(String string) {
        JLabel statusLabel = new JLabel(string);
        statusLabel.setLocation(HEIGTH/10, HEIGTH / 10+100);
        statusLabel.setSize(buttonWidth+100,buttonHight);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 40));
        statusLabel.setForeground(Color.WHITE);
        mainPanel.add(statusLabel);
    }
    public void Mousemotion(JButton c){
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                c.setContentAreaFilled(true);
                c.setBackground(Color.yellow);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                c.setBackground(null);
                c.setContentAreaFilled(false);
            }

        });
    }
}

