import controller.GameController;
import controller.GameMode;
import model.Chessboard;
import view.ChessGameFrame;
import view.Time;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;


public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            ChessGameFrame mainFrame = new ChessGameFrame(1100, 810);
            GameController gameController = new GameController(mainFrame.getChessboardComponent(), new Chessboard(), GameMode.LOPlayer);
            Time time = new Time(gameController,mainFrame);
            mainFrame.setVisible(true);
            time.start();
        }
        );
        Clip bgm;

        {
            try {
                bgm = AudioSystem.getClip();
                InputStream is = Main.class.getClassLoader().getResourceAsStream("sound/background.wav");
                BufferedInputStream iss = new BufferedInputStream(is);
                AudioInputStream ais = AudioSystem.getAudioInputStream(iss);
                bgm.open(ais);
                bgm.start();
                bgm.loop(Clip.LOOP_CONTINUOUSLY);
                while (1==1){

                }
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

