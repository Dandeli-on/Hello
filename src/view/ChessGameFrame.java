package view;


import controller.GameController;
import controller.GameMode;
import model.Chessboard;

import java.awt.Color;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGTH;

    private final int ONE_CHESS_SIZE;
    private final int buttonWidth = 150;
    private final int buttonHight = 50;
    private ImagePanel mainPanel;
    private int n = 1;
    private JLabel statusLabel1 = new JLabel();
    State state = new State();
    private ImagePanel startPanel = new ImagePanel(-1);


    private ChessboardComponent chessboardComponent;

    public ChessGameFrame(int width, int height) {
        setTitle("斗兽棋"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.ONE_CHESS_SIZE = (HEIGTH * 4 / 5) / 9;

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);
        addBackgroundImage();
        addChessboard();
        addLabel();
        addHelloButton();
        addState();
        initComponents();
        mainPanel.setVisible(false);
        addStartPanel();

    }

    public void addState() {
        state.setSize(WIDTH, buttonHight-20);
        state.setLocation(0,0);
        state.setFont(new Font("Rockwell", Font.BOLD, 20));
        state.setBorder(null);
        state.setForeground(Color.WHITE);
        state.setBackground(Color.cyan);
        mainPanel.add(state);
    }
    public void Repaint(){
        state.repaint();
    }
    public void addStopButton() {
        JButton StopButton = new JButton("Stop / Continue");
        Mousemotion(StopButton);
        StopButton.setLocation(HEIGTH, HEIGTH / 10 + 130);
        StopButton.setSize(buttonWidth+100, buttonHight);
        StopButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        StopButton.setContentAreaFilled(false);
        StopButton.setBorder(null);
        StopButton.setForeground(Color.WHITE);
        mainPanel.add(StopButton);
        StopButton.addActionListener(e -> {
            System.out.println("Click");
             if (Tool.GameState){
                 Tool.GameState= false;
             }else {
                 Tool.GameState=true;
             }
        });
    }
    public void addSaveButton() {
        JButton SaveButton = new JButton("Save");
        Mousemotion(SaveButton);
        SaveButton.setLocation(HEIGTH, HEIGTH / 10 + 190);
        SaveButton.setSize(buttonWidth, buttonHight);
        SaveButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        SaveButton.setContentAreaFilled(false);
        SaveButton.setBorder(null);
        SaveButton.setForeground(Color.WHITE);
        mainPanel.add(SaveButton);
        SaveButton.addActionListener(e -> {
            System.out.println("Click");
            try {
                chessboardComponent.gameController().save("save",chessboardComponent.gameController().getModel().getGrid());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void addUndoButton() {
        JButton UndoButton = new JButton("Undo");
        mainPanel.add(UndoButton);
        Mousemotion(UndoButton);
        UndoButton.setLocation(HEIGTH, HEIGTH / 10 + 250);
        UndoButton.setSize(buttonWidth, buttonHight);
        UndoButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        UndoButton.setContentAreaFilled(false);
        UndoButton.setBorder(null);
        UndoButton.setForeground(Color.WHITE);
        UndoButton.addActionListener(e -> {
            System.out.println("Click");
            chessboardComponent.gameController().undo(chessboardComponent.gameController().getModel().getGrid());
        });
    }

    public void addSettingButton() {
        JButton SettingButton = new JButton("Setting");
        Mousemotion(SettingButton);
        mainPanel.add(SettingButton);
        SettingButton.setLocation(HEIGTH, HEIGTH / 10 + 310);
        SettingButton.setSize(buttonWidth, buttonHight);
        SettingButton.setContentAreaFilled(false);
        SettingButton.setBorder(null);
        SettingButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        SettingButton.setForeground(Color.WHITE);
        SettingButton.addActionListener(e -> {
            System.out.println("Click");

        });
    }

    public void addRestartButton() {
        JButton StartButton = new JButton("ReStart");
        Mousemotion(StartButton);
        mainPanel.add(StartButton);
        StartButton.setLocation(HEIGTH, HEIGTH / 10 + 370);
        StartButton.setSize(buttonWidth, buttonHight);
        StartButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        StartButton.setContentAreaFilled(false);
        StartButton.setBorder(null);
        StartButton.setForeground(Color.WHITE);
        StartButton.addActionListener(e -> {
            System.out.println("Click");
            Tool.GameState=true;
            Tool.round=1;
            chessboardComponent.gameController().restart();
            n = n + 1;
            if (n > 4) {
                n = 1;
            }
            addBackgroundImage();
            mainPanel.add(chessboardComponent);
            initComponents();
            Tool.Time=30;
            Tool.Player=1;
        });
    }
    public void addLoadButton() {
        JButton LoadButton = new JButton("Load");
        Mousemotion(LoadButton);
        mainPanel.add(LoadButton);
        LoadButton.setLocation(HEIGTH, HEIGTH / 10 + 430);
        LoadButton.setSize(buttonWidth, buttonHight);
        LoadButton.setFont(new Font("haha", Font.BOLD, 20));
        LoadButton.setContentAreaFilled(false);
        LoadButton.setBorder(null);
        LoadButton.setForeground(Color.WHITE);
        LoadButton.addActionListener(e -> {
            System.out.println("Click");
            chessboardComponent.gameController().initialSteps();
            try {
                chessboardComponent.gameController().load(chessboardComponent.gameController().getModel().getGrid(),chessboardComponent.gameController().getModel());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            n = n + 1;
            if (n > 4) {
                n = 1;
            }
            addBackgroundImage();
            mainPanel.add(chessboardComponent);
            initComponents();
            playMusic();
        });
    }

    public void addExitButton() {
        JButton ExitButton = new JButton("Exit");
        Mousemotion(ExitButton);
        mainPanel.add(ExitButton);
        ExitButton.setLocation(HEIGTH, HEIGTH / 10 + 490);
        ExitButton.setSize(buttonWidth, buttonHight);
        ExitButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        ExitButton.setContentAreaFilled(false);
        ExitButton.setBorder(null);
        ExitButton.setForeground(Color.WHITE);
        ExitButton.addActionListener(e -> {
            System.out.println("Click");
            System.exit(0);
        });
    }

    public void addSwitchButton() {
        JButton SwitchButton = new JButton("Switch");
        Mousemotion(SwitchButton);
        mainPanel.add(SwitchButton);
        SwitchButton.setLocation(HEIGTH, HEIGTH / 10 + 550);
        SwitchButton.setSize(buttonWidth, buttonHight);
        SwitchButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        SwitchButton.setContentAreaFilled(false);
        SwitchButton.setBorder(null);
        SwitchButton.setForeground(Color.WHITE);
        SwitchButton.addActionListener(e -> {
            System.out.println(n);
            n = n + 1;
            if (n > 4) {
                n = 1;
            }
            addBackgroundImage();
            mainPanel.add(chessboardComponent);
            initComponents();
            playMusic();
        });
    }


    public void initComponents() {
        addExitButton();
        addLabel();
        addLoadButton();
        addRestartButton();
        addSaveButton();
        addUndoButton();
        addSettingButton();
        addHelloButton();
        addSwitchButton();
        addState();
        addStopButton();
    }

    public void addBackgroundImage() {
        mainPanel = new ImagePanel(n);
        setContentPane(mainPanel);
        mainPanel.setLayout(null);

    }

    public ChessboardComponent getChessboardComponent() {
        return chessboardComponent;
    }

    public void setChessboardComponent(ChessboardComponent chessboardComponent) {
        this.chessboardComponent = chessboardComponent;
    }

    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        chessboardComponent = new ChessboardComponent(ONE_CHESS_SIZE);
        chessboardComponent.setLocation(HEIGTH / 5, HEIGTH / 10);
        mainPanel.add(chessboardComponent);
    }

    /**
     * 在游戏面板中添加标签
     */
    public void addLabel() {
        JLabel statusLabel = new JLabel("Welcome!");
        statusLabel.setLocation(HEIGTH, HEIGTH / 10 + 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(statusLabel);
        statusLabel.setForeground(Color.WHITE);
    }

    /*private void addLabel2() {

        statusLabel1.setLocation(0, 0);
        statusLabel1.setSize(200, 60);
        statusLabel1.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(statusLabel1);

    }

    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    private void addHelloButton() {
        JButton button = new JButton("Hello");
        Mousemotion(button);
        button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "Hello, world!"));
        button.setLocation(HEIGTH, HEIGTH / 10 + 70);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.setContentAreaFilled(false);
        button.setBorder(null);
        mainPanel.add(button);
        button.setForeground(Color.WHITE);

    }
    public void addStartPanel(){
        setContentPane(startPanel);
        startPanel.setLayout(null);
        JLabel statusLabel = new JLabel("Welcome!");
        statusLabel.setLocation(WIDTH/2-100, HEIGTH / 10 + 10);
        statusLabel.setSize(300, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 40));
        add(statusLabel);
        statusLabel.setForeground(Color.WHITE);
        JButton StartButton = new JButton("Start");
        Mousemotion(StartButton);
        StartButton.setLocation(WIDTH/2-165, HEIGTH/10+150);
        StartButton.setSize(300, 100);
        StartButton.setFont(new Font("haha", Font.BOLD, 40));
        StartButton.setContentAreaFilled(false);
        StartButton.setBorder(null);
        StartButton.setForeground(Color.WHITE);
        JButton AIButton = new JButton("AI 1");
        Mousemotion(AIButton);
        AIButton.setLocation(WIDTH/2-165, HEIGTH / 10 +250);
        AIButton.setSize(300, 100);
        AIButton.setFont(new Font("haha", Font.BOLD, 40));
        AIButton.setContentAreaFilled(false);
        AIButton.setBorder(null);
        AIButton.setForeground(Color.WHITE);
        AIButton.addActionListener(e -> {
            System.out.println("Click");
            Tool.GameState=true;
            GameController gameController = new GameController(this.getChessboardComponent(), new Chessboard(), GameMode.AI1);
            Time time = new Time(gameController,this);
            startPanel.setVisible(false);
            setContentPane(mainPanel);
            mainPanel.setVisible(true);
        });
        JButton AIButton2 = new JButton("AI 2");
        Mousemotion(AIButton2);
        AIButton2.setLocation(WIDTH/2-165, HEIGTH / 10 +350);
        AIButton2.setSize(300, 100);
        AIButton2.setFont(new Font("haha", Font.BOLD, 40));
        AIButton2.setContentAreaFilled(false);
        AIButton2.setBorder(null);
        AIButton2.setForeground(Color.WHITE);
        AIButton2.addActionListener(e -> {
            System.out.println("Click");
            Tool.GameState=true;
            GameController gameController = new GameController(this.getChessboardComponent(), new Chessboard(), GameMode.AI2);
            Time time = new Time(gameController,this);
            startPanel.setVisible(false);
            setContentPane(mainPanel);
            mainPanel.setVisible(true);
        });
        JButton AI3Button = new JButton("AI 3");
        Mousemotion(AI3Button);
        AI3Button.setLocation(WIDTH/2-165, HEIGTH / 10 +450);
        AI3Button.setSize(300, 100);
        AI3Button.setFont(new Font("haha", Font.BOLD, 40));
        AI3Button.setContentAreaFilled(false);
        AI3Button.setBorder(null);
        AI3Button.setForeground(Color.WHITE);
        AI3Button.addActionListener(e -> {
            System.out.println("Click");
            Tool.GameState=true;
            GameController gameController = new GameController(this.getChessboardComponent(), new Chessboard(), GameMode.AI3);
            Time time = new Time(gameController,this);
            startPanel.setVisible(false);
            setContentPane(mainPanel);
            mainPanel.setVisible(true);
        });
        startPanel.setVisible(true);
        startPanel.add(StartButton);
        startPanel.add(AIButton);
        startPanel.add(AIButton2);
        startPanel.add(AI3Button);
        StartButton.addActionListener(e -> {
            System.out.println("Click");
            Tool.GameState=true;
           startPanel.setVisible(false);
           setContentPane(mainPanel);
           mainPanel.setVisible(true);
        });
    }

    //    private void addLoadButton() {
//        JButton button = new JButton("Load");
//        button.setLocation(HEIGTH, HEIGTH / 10 + 240);
//        button.setSize(200, 60);
//        button.setFont(new Font("Rockwell", Font.BOLD, 20));
//        add(button);
//
//        button.addActionListener(e -> {
//            System.out.println("Click load");
//            String path = JOptionPane.showInputDialog(this,"Input Path here");
//            gameController.loadGameFromFile(path);
//        });
//    }

    public void Mousemotion(JButton c) {
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                c.setContentAreaFilled(true);
                c.setBackground(Color.darkGray);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                c.setBackground(null);
                c.setContentAreaFilled(false);
            }

        });
    }
    static Clip clip;
    public static void playMusic() {
        try
        {
            //这里面放 绝对路径，音频必须是wav格式，用音频转换软件 把mp3 转成wav格式
            File musicPath = new File("/sound/effct.wav");

            if(musicPath.exists())
            {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-20.0f);//设置音量，范围为 -60.0f 到 6.0f
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            else
            {

            }


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }






}



