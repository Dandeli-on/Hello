package view;

import controller.GameController;
import model.PlayerColor;


    public class Time extends Thread {

        private GameController gameController;
        private int oldPlayer = 1;
        private ChessGameFrame chessGameFrame;

        public Time(GameController gameController, ChessGameFrame chessGameFrame) {
            super();
            this.chessGameFrame = chessGameFrame;
            this.gameController = gameController;
        }

        @Override
        public void run() {
            if (Tool.Time > 0) {
                    try {
                        Thread.sleep(1000);
                        Tool.Time = Tool.Time - 1;
                        if (oldPlayer != Tool.Player) {
                            Tool.Time = 30;
                            oldPlayer = Tool.Player;
                        }
                        chessGameFrame.Repaint();
                        if (!Tool.GameState){
                            Tool.Time=Tool.Time+1;
                        }
                        run();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    if (gameController.getCurrentPlayer() == PlayerColor.BLUE) {
                        gameController.setCurrentPlayer(PlayerColor.RED);
                        Tool.Player = 2;
                        Tool.round++;
                    } else {
                        gameController.setCurrentPlayer(PlayerColor.BLUE);
                        Tool.Player = 1;
                        Tool.round++;
                    }
                    Tool.Time = 30;
                    chessGameFrame.Repaint();
                    run();
                }
            }
        }

