package controller;


import listener.GameListener;
import model.Chessboard;
import model.ChessboardPoint;
import model.Constant;
import model.PlayerColor;
import view.*;
import model.Step;
import model.ChessPiece;
import model.Cell;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and onPlayerClickChessPiece()]
 *
*/
public class GameController implements GameListener {

   private CellComponent cellComponent;
    private Chessboard model ;

    public ChessboardComponent getView() {
        return view;
    }

    public void setView(ChessboardComponent view) {
        this.view = view;
    }

    private ChessboardComponent view;

    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    private PlayerColor currentPlayer;
    private AI ai;

    public ArrayList<Step> getSteps() {
        return Steps;
    }

    ArrayList<Step> Steps = new ArrayList<>(0);


    public ChessboardPoint getSelectedPoint() {
        return selectedPoint;
    }

    public void setSelectedPoint(ChessboardPoint selectedPoint) {
        this.selectedPoint = selectedPoint;
    }

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;
    public GameController(ChessboardComponent view, Chessboard model,GameMode gameMode) {
        this.view = view;
        this.model = model;

        this.currentPlayer = PlayerColor.BLUE;
        registerCellComponent();
        view.registerController(this);
        initialize();
        view.initiateChessComponent(model);
        view.repaint();
        if (gameMode==GameMode.AI1){
             ai = new AI(GameMode.AI1,model);
        }
        if (gameMode==GameMode.AI2){
            ai=new AI(GameMode.AI2,model);
        }
        if (gameMode==GameMode.AI3){
            ai=new AI(GameMode.AI3,model);
        }
        model.registerGame(this);
    }
    private PlayerColor battlewin(){
        PlayerColor Winner = currentPlayer;
        System.out.println("Winner is " + Winner);
        return  Winner;
    }
    private void initialize() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {

            }
        }
    }

    // after a valid move swap the player
    public void swapColor() {
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
    }

 /*   private boolean win() {
        boolean bluewin = true;
        for(int i = 0;i<9;i++){
            for(int j = 0;j<7;j++){
                if(){}
            }
        }
        return false;
    } */


    // click an empty cell
    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
        System.out.println(point);
        if (selectedPoint != null && model.isValidMove(selectedPoint, point)) {
            model.inOrOutTraps(selectedPoint,point);
            String stepname = "Step" + Tool.round;
            Steps.add(new Step(stepname, selectedPoint, point, getChessPieceAt(selectedPoint), getChessPieceAt(point),currentPlayer));
            if(model.enterDens(point)){
                   battlewin();
                   //开一个胜利窗口，退出游戏，或者重新开始
                SubWindow subWindow = new SubWindow(500,500,battlewin(),this);
                subWindow.setVisible(true);
            }
            for (int i =0;i<9;i++){
                for (int j =0;j<7;j++){
                    ChessboardPoint everyPoint=new ChessboardPoint(i,j);
                    if (model.isValidMove(selectedPoint,everyPoint)){
                       view.HideArea(everyPoint);
                       view.repaint();
                    }
                    if (model.isValidCapture(selectedPoint, everyPoint)) {
                        view.getChessComponentAtGrid(everyPoint).setBeSelected(false);
                        view.getChessComponentAtGrid(everyPoint).repaint();
                    }
                }
            }
            model.moveChessPiece(selectedPoint, point);
            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
            selectedPoint = null;
            Tool.round++;
            swapColor();
            if (PlayerColor.BLUE==currentPlayer){
                Tool.Player=1;
            }else {
                Tool.Player = 2;
            }
           UseAItoMove();
            view.repaint();


        }

    }
    private void DieOutWin() {
        PlayerColor Winner = currentPlayer;
        System.out.println("Winner is " + Winner);
    }
    // click a cell with a chess
    @Override
    public void onPlayerClickChessPiece(ChessboardPoint point, ChessComponent component) {
        if (selectedPoint == null) {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                selectedPoint = point;

                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 7; j++) {
                        ChessboardPoint everyPoint = new ChessboardPoint(i, j);
                        if (model.isValidMove(selectedPoint, everyPoint)) {
                            view.showArea(everyPoint);
                            view.repaint();
                        }component.setSelected(true);
                        if (model.isValidCapture(selectedPoint, everyPoint)) {
                            view.getChessComponentAtGrid(everyPoint).setBeSelected(true);
                            view.getChessComponentAtGrid(everyPoint).repaint();
                        }
                    }
                }
                component.repaint();
            }
        } else if (selectedPoint.equals(point)) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 7; j++) {
                    ChessboardPoint everyPoint = new ChessboardPoint(i, j);
                    if (model.isValidMove(selectedPoint, everyPoint)) {
                        view.HideArea(everyPoint);
                        view.repaint();
                    }
                    if (model.isValidCapture(selectedPoint, everyPoint)) {
                        view.getChessComponentAtGrid(everyPoint).setBeSelected(false);
                        view.getChessComponentAtGrid(everyPoint).repaint();
                    }
                }
            }
            selectedPoint = null;
            component.setSelected(false);
            component.repaint();
        } else {
            if (!model.isValidCapture(selectedPoint, point)) {

                component.setSelected(false);
                view.repaint();
            } else {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 7; j++) {
                        ChessboardPoint everyPoint = new ChessboardPoint(i, j);
                        if (model.isValidMove(selectedPoint, everyPoint)) {
                            view.HideArea(everyPoint);
                            view.repaint();
                        }
                        if (model.isValidCapture(selectedPoint, everyPoint)) {
                            view.getChessComponentAtGrid(everyPoint).setBeSelected(false);
                            view.getChessComponentAtGrid(everyPoint).repaint();
                        }
                    }
                }
                String stepname = "Step" + Tool.round;
                Steps.add(new Step(stepname, selectedPoint, point, getChessPieceAt(selectedPoint), getChessPieceAt(point),currentPlayer));
                model.captureChessPiece(selectedPoint, point);
                view.removeChessComponentAtGrid(point);
                view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));

                selectedPoint = null;
                if (currentPlayer.equals(PlayerColor.BLUE)) {
                    if (model.isRedDieOut()) {
                        DieOutWin();
                        SubWindow subWindow = new SubWindow(200, 200, PlayerColor.BLUE,this);
                        subWindow.setVisible(true);
                    }
                } else if (currentPlayer.equals(PlayerColor.RED)) {
                    if (model.isBlueDieOut()) {
                        DieOutWin();
                        SubWindow subWindow = new SubWindow(200, 200, PlayerColor.RED,this);
                        subWindow.setVisible(true);
                    }
                }
                model.checktraps();
                Tool.round++;
                swapColor();
                if (PlayerColor.BLUE==currentPlayer){
                    Tool.Player =1;
                }else {
                    Tool.Player = 2;
                }
                  UseAItoMove();
                view.repaint();
            }
        }
    }

    public void  UseAItoMove(){
        if (currentPlayer==PlayerColor.RED&&ai!=null){
           Step step=ai.generateMoveAI1(currentPlayer);
            Tool.isAi=true;
            if (ai.getGameMode()==GameMode.AI1){

            step = ai.generateMoveAI1(currentPlayer);
            }
            if (ai.getGameMode()==GameMode.AI2){
                step=ai.generateMoveAI3(currentPlayer);
            }
            if (ai.getGameMode()==GameMode.AI3){
                step=ai.generateMoveAI2(currentPlayer);
            }
           selectedPoint = step.getSrc();
           if (step.getDestPiece() ==null){
               model.inOrOutTraps(selectedPoint,step.getDest());
               String stepname = "Step" + Tool.round;
               Steps.add(new Step(stepname, selectedPoint, step.getDest(), getChessPieceAt(selectedPoint), getChessPieceAt(step.getDest()),currentPlayer));
               /*Timer time1 = new Timer(2000,e->{
               model.moveChessPiece(selectedPoint,step.getDest());
               view.setChessComponentAtGrid(step.getDest(), view.removeChessComponentAtGrid(selectedPoint));
               });
               time1.setRepeats(false);
               time1.start();*/
               model.moveChessPiece(selectedPoint,step.getDest());
               view.setChessComponentAtGrid(step.getDest(), view.removeChessComponentAtGrid(selectedPoint));
               if(model.enterDens(step.getDest())){
                   battlewin();
                   //开一个胜利窗口，退出游戏，或者重新开始
                   SubWindow subWindow = new SubWindow(500,500,battlewin(),this);
                   subWindow.setVisible(true);
               }
               selectedPoint = null;
               Tool.round++;
               swapColor();
               if (PlayerColor.BLUE==currentPlayer){
                   Tool.Player=1;
               }else {
                   Tool.Player = 2;
               }
               view.repaint();
           }else {
               String stepname = "Step" + Tool.round;
               Steps.add(new Step(stepname, selectedPoint, step.getDest(), getChessPieceAt(selectedPoint), getChessPieceAt(step.getDest()),currentPlayer));
               model.captureChessPiece(selectedPoint, step.getDest());
               view.removeChessComponentAtGrid(step.getDest());
               view.setChessComponentAtGrid(step.getDest(), view.removeChessComponentAtGrid(selectedPoint));
               selectedPoint = null;
               if (currentPlayer.equals(PlayerColor.BLUE)) {
                   if (model.isRedDieOut()) {
                       DieOutWin();
                       SubWindow subWindow = new SubWindow(500, 500, PlayerColor.BLUE,this);
                       subWindow.setVisible(true);
                   }
               } else if (currentPlayer.equals(PlayerColor.RED)) {
                   if (model.isBlueDieOut()) {
                       DieOutWin();
                       SubWindow subWindow = new SubWindow(500, 500, PlayerColor.RED,this);
                       subWindow.setVisible(true);
                   }
               }
               model.checktraps();
               Tool.round++;
               swapColor();
               if (PlayerColor.BLUE==currentPlayer){
                   Tool.Player =1;
               }else {
                   Tool.Player = 2;
               }
               view.repaint();
           }
            Tool.isAi=false;
        }
    }
    public void  UseAItoMove2(Step step2){
            Step step = step2;
            selectedPoint = step.getSrc();
            String stepname = "Step" + Tool.round;
            Steps.add(new Step(stepname, selectedPoint, step.getDest(), getChessPieceAt(selectedPoint), getChessPieceAt(step.getDest()),currentPlayer));
            if (step.getDestPiece() ==null){
                model.inOrOutTraps(selectedPoint,step.getDest());
               /*Timer time1 = new Timer(2000,e->{
               model.moveChessPiece(selectedPoint,step.getDest());
               view.setChessComponentAtGrid(step.getDest(), view.removeChessComponentAtGrid(selectedPoint));
               });
               time1.setRepeats(false);
               time1.start();*/
                model.moveChessPiece(selectedPoint,step.getDest());
                if(model.enterDens(step.getDest())){
                    battlewin();
                    //开一个胜利窗口，退出游戏，或者重新开始
                    SubWindow subWindow = new SubWindow(500,500,battlewin(),this);
                    subWindow.setVisible(true);
                }
                selectedPoint = null;
                Tool.round++;
            }else {
                model.captureChessPiece(selectedPoint, step.getDest());
                selectedPoint = null;
                if (currentPlayer.equals(PlayerColor.BLUE)) {
                    if (model.isRedDieOut()) {
                        DieOutWin();
                        SubWindow subWindow = new SubWindow(500, 500, PlayerColor.BLUE,this);
                        subWindow.setVisible(true);
                    }
                } else if (currentPlayer.equals(PlayerColor.RED)) {
                    if (model.isBlueDieOut()) {
                        DieOutWin();
                        SubWindow subWindow = new SubWindow(500, 500, PlayerColor.RED,this);
                        subWindow.setVisible(true);
                    }
                }
                model.checktraps();

            }
        }



        public void restart(){
            currentPlayer =PlayerColor.BLUE;
           if (selectedPoint!=null){
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 7; j++) {
                    ChessboardPoint everyPoint = new ChessboardPoint(i, j);
                    if (model.isValidMove(selectedPoint, everyPoint)) {
                        view.HideArea(everyPoint);
                        view.repaint();
                    }
                    if (model.isValidCapture(selectedPoint, everyPoint)) {
                        view.getChessComponentAtGrid(everyPoint).setBeSelected(false);
                        view.getChessComponentAtGrid(everyPoint).repaint();
                    }
                }
            }
           }
            selectedPoint = null;
            view.clearAllComponent();
            model.initPieces();
            view.initiateGridComponents();
            registerCellComponent();
            view.initiateChessComponent(model);
            view.repaint();
        }
        // TODO: Implement capture function
    public Chessboard getModel(){
        return  model;
    }
    private void registerCellComponent(){
          for (int i =0;i<9;i++){
              for (int j = 0;j<7;j++){
                  ChessboardPoint chessboardPoint = new ChessboardPoint(i,j);
                  view.getGridComponentAt(chessboardPoint).registerController(this);
              }
          }

    }

    /*private void CheckEveryPoint(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                ChessboardPoint everyPoint = new ChessboardPoint(i, j);
                if (model.isValidMove(selectedPoint, everyPoint)) {
                    view.HideArea(everyPoint);
                    view.repaint();
                }
                if (model.isValidCapture(selectedPoint, everyPoint)) {
                    view.getChessComponentAtGrid(everyPoint).setBeSelected(false);
                    view.getChessComponentAtGrid(everyPoint).repaint();
                }
            }
        }
    }*/
    public ChessPiece getChessPieceAt(ChessboardPoint point) {
        return getGridAt(point).getPiece();
    }

    public Cell getGridAt(ChessboardPoint point) {
        Cell[][] grid = getModel().getGrid();
        return grid[point.getRow()][point.getCol()];
    }
    public void save(String filename, Cell[][] model) throws IOException {
        try {
            FileWriter fileWriter = new FileWriter(filename);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            int row = model.length;
            int column = model[0].length;

            bufferedWriter.write(row + "," + column + "     " + "\n");


            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    if (model[i][j].getPiece() == null) {
                        //  bufferedWriter.write(" ");
                        bufferedWriter.write("        ");
                        //     bufferedWriter.write(" ");
                        bufferedWriter.write("        ");
                        continue;
                    }
                    if (model[i][j].getPiece().toString().equals("Elephant")) {
                        bufferedWriter.write("Elephant");
                    } else if (model[i][j].getPiece().toString().equals("Cat")) {
                        bufferedWriter.write("Cat     ");
                    } else if (model[i][j].getPiece().toString().equals("Dog")) {
                        bufferedWriter.write("Dog     ");
                    } else if (model[i][j].getPiece().toString().equals("Lion")) {
                        bufferedWriter.write("Lion    ");
                    } else if (model[i][j].getPiece().toString().equals("Tiger")) {
                        bufferedWriter.write("Tiger   ");
                    } else if (model[i][j].getPiece().toString().equals("Leopard")) {
                        bufferedWriter.write("Leopard ");
                    } else if (model[i][j].getPiece().toString().equals("Wolf")) {
                        bufferedWriter.write("Wolf    ");
                    } else if (model[i][j].getPiece().toString().equals("Rat")) {
                        bufferedWriter.write("Rat     ");
                    }

                    /*else {
                        bufferedWriter.write(model[i][j].getPiece().toString() + "\t");
                        if (model[i][j].getPiece().toString().equals("Cat") || model[i][j].getPiece().toString().equals("Dog") || model[i][j].getPiece().toString().equals("Rat")) {
                            bufferedWriter.write("\t");
                        }
                    }*/
                    if (model[i][j].getPiece().getOwner().equals(PlayerColor.BLUE)) {
                        bufferedWriter.write("BLUE    ");
                    } else {
                        bufferedWriter.write("RED     ");
                    }
                }
                bufferedWriter.write("\n");
            }
            bufferedWriter.write("\n");

            bufferedWriter.write(currentPlayer.toString());
            bufferedWriter.write(" ");
            bufferedWriter.write("\n");

            bufferedWriter.write("当前回合数为：" + Tool.round);
            bufferedWriter.write("\n");

            if (Steps.size()==0) {
                bufferedWriter.write("No moves.");
            } else {
                for (int i = 0; i < Steps.size(); i++) {
                    bufferedWriter.write(Steps.get(i).toString());
                    bufferedWriter.write("\n");
                }
            }
            bufferedWriter.close();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void load( Cell[][] model, Chessboard board) throws IOException {
        FileSystemView sss = FileSystemView.getFileSystemView();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(sss.getHomeDirectory());
        fileChooser.setDialogTitle("请选择要加载的文件");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        String path = null;
        int res = fileChooser.showOpenDialog(null);
        if(JFileChooser.APPROVE_OPTION==res){
            path = fileChooser.getSelectedFile().getPath();
        }
        Path path2 = Paths.get(path);
        String data = Files.readString(path2);
        selectedPoint = null;
        String mimetype = Files.probeContentType(path2);
//    System.out.println(mimetype);
        if(mimetype!=null&&!mimetype.equals("text/plain")){
            System.out.println("寄了");
            WrongWindow wrongWindow = new WrongWindow(500,500,"文件类型错误");
            wrongWindow.setVisible(true);
            return;
        }
        //   component.setSelected(false);

        //检查地图大小
        if (Integer.parseInt(data.substring(0, 1)) != 9) {
            //弹窗一下 地图大小错误
            //终止程序
            WrongWindow wrongWindow = new WrongWindow(500,500,"地图大小错误");
            wrongWindow.setVisible(true);
            System.out.println("寄了");
            return;
        }
        //    int newRow = Integer.parseInt(data.substring(0));

        if (Integer.parseInt(data.substring(2, 3)) != 7) {
            //弹窗一下 地图大小错误
            //终止程序
            WrongWindow wrongWindow = new WrongWindow(500,500,"地图大小错误");
            wrongWindow.setVisible(true);
            System.out.println("寄了");
            return;
        }
        // int newCol = Integer.parseInt(data.substring(2));
        //开始往model里面传入棋子


        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                model[i][j].removePiece();

            }
        }
        for (int i = 0; i < 7; i++) {
            if (data.substring(9 + i * 16, 17 + i * 16).equals("        ")) {
                continue;
            }
            if (data.substring(9 + i * 16, 17 + i * 16).equals("Lion    ") && data.substring(17 + i * 16, 25 + i * 16).equals("RED     ")) {
                model[0][i].setPiece(new ChessPiece(PlayerColor.RED, "Lion", 7));
            } else if (data.substring(9 + i * 16, 17 + i * 16).equals("Lion    ") && data.substring(17 + i * 16, 25 + i * 16).equals("BLUE    ")) {
                model[0][i].setPiece(new ChessPiece(PlayerColor.BLUE, "Lion", 7));
            }
           else if (data.substring(9 + i * 16, 17 + i * 16).equals("Elephant") && data.substring(17 + i * 16, 25 + i * 16).equals("RED     ")) {
                model[0][i].setPiece(new ChessPiece(PlayerColor.RED, "Elephant", 8));
            } else if (data.substring(9 + i * 16, 17 + i * 16).equals("Elephant") && data.substring(17 + i * 16, 25 + i * 16).equals("BLUE    ")) {
                model[0][i].setPiece(new ChessPiece(PlayerColor.BLUE, "Elephant", 8));
            }
            else  if (data.substring(9 + i * 16, 17 + i * 16).equals("Tiger   ") && data.substring(17 + i * 16, 25 + i * 16).equals("RED     ")) {
                model[0][i].setPiece(new ChessPiece(PlayerColor.RED, "Tiger", 6));
            } else if (data.substring(9 + i * 16, 17 + i * 16).equals("Tiger   ") && data.substring(17 + i * 16, 25 + i * 16).equals("BLUE    ")) {
                model[0][i].setPiece(new ChessPiece(PlayerColor.BLUE, "Tiger", 6));
            }
            else   if (data.substring(9 + i * 16, 17 + i * 16).equals("Leopard ") && data.substring(17 + i * 16, 25 + i * 16).equals("RED     ")) {
                model[0][i].setPiece(new ChessPiece(PlayerColor.RED, "Leopard", 5));
            } else if (data.substring(9 + i * 16, 17 + i * 16).equals("Leopard ") && data.substring(17 + i * 16, 25 + i * 16).equals("BLUE    ")) {
                model[0][i].setPiece(new ChessPiece(PlayerColor.BLUE, "Leopard", 5));
            }
            else   if (data.substring(9 + i * 16, 17 + i * 16).equals("Wolf    ") && data.substring(17 + i * 16, 25 + i * 16).equals("RED     ")) {
                model[0][i].setPiece(new ChessPiece(PlayerColor.RED, "Wolf", 4));
            } else if (data.substring(9 + i * 16, 17 + i * 16).equals("Wolf    ") && data.substring(17 + i * 16, 25 + i * 16).equals("BLUE    ")) {
                model[0][i].setPiece(new ChessPiece(PlayerColor.BLUE, "Wolf", 4));
            }
            else   if (data.substring(9 + i * 16, 17 + i * 16).equals("Dog     ") && data.substring(17 + i * 16, 25 + i * 16).equals("RED     ")) {
                model[0][i].setPiece(new ChessPiece(PlayerColor.RED, "Dog", 3));
            } else if (data.substring(9 + i * 16, 17 + i * 16).equals("Dog     ") && data.substring(17 + i * 16, 25 + i * 16).equals("BLUE    ")) {
                model[0][i].setPiece(new ChessPiece(PlayerColor.BLUE, "Dog", 3));
            }
            else   if (data.substring(9 + i * 16, 17 + i * 16).equals("Cat     ") && data.substring(17 + i * 16, 25 + i * 16).equals("RED     ")) {
                model[0][i].setPiece(new ChessPiece(PlayerColor.RED, "Cat", 2));
            } else if (data.substring(9 + i * 16, 17 + i * 16).equals("Cat     ") && data.substring(17 + i * 16, 25 + i * 16).equals("BLUE    ")) {
                model[0][i].setPiece(new ChessPiece(PlayerColor.BLUE, "Cat", 2));
            }
            else   if (data.substring(9 + i * 16, 17 + i * 16).equals("Rat     ") && data.substring(17 + i * 16, 25 + i * 16).equals("RED     ")) {
                model[0][i].setPiece(new ChessPiece(PlayerColor.RED, "Rat", 1));
            } else if (data.substring(9 + i * 16, 17 + i * 16).equals("Rat     ") && data.substring(17 + i * 16, 25 + i * 16).equals("BLUE    ")) {
                model[0][i].setPiece(new ChessPiece(PlayerColor.BLUE, "Rat", 1));
            }  else {
                //弹窗报错，错误棋子类型
                //终止程序
                WrongWindow wrongWindow = new WrongWindow(500,500,"棋子类型错啦");
                wrongWindow.setVisible(true);
                return;
            }    }
        for (int i = 1; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("        ")) {
                    continue;
                }
                if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("Lion    ") && data.substring((113 * i) + (j * 16) + 17, (113 * i) + (j * 16) + 25).equals("RED     ")) {
                    model[i][j].setPiece(new ChessPiece(PlayerColor.RED, "Lion", 7));
                } else if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("Lion    ") && data.substring((113 * i) + (j * 16) + 17, (113 * i) + (j * 16) + 25).equals("BLUE    ")) {
                    model[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "Lion", 7));
                }
                else    if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("Elephant") && data.substring((113 * i) + (j * 16) + 17, (113 * i) + (j * 16) + 25).equals("RED     ")) {
                    model[i][j].setPiece(new ChessPiece(PlayerColor.RED, "Elephant", 8));
                } else if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("Elephant") && data.substring((113 * i) + (j * 16) + 17, (113 * i) + (j * 16) + 25).equals("BLUE    ")) {
                    model[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "Elephant", 8));
                }
                else   if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("Tiger   ") && data.substring((113 * i) + (j * 16) + 17, (113 * i) + (j * 16) + 25).equals("RED     ")) {
                    model[i][j].setPiece(new ChessPiece(PlayerColor.RED, "Tiger", 6));
                } else if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("Tiger   ") && data.substring((113 * i) + (j * 16) + 17, (113 * i) + (j * 16) + 25).equals("BLUE    ")) {
                    model[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "Tiger", 6));
                }
                else   if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("Leopard ") && data.substring((113 * i) + (j * 16) + 17, (113 * i) + (j * 16) + 25).equals("RED     ")) {
                    model[i][j].setPiece(new ChessPiece(PlayerColor.RED, "Leopard", 5));
                } else if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("Leopard ") && data.substring((113 * i) + (j * 16) + 17, (113 * i) + (j * 16) + 25).equals("BLUE    ")) {
                    model[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "Leopard", 5));
                }
                else   if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("Wolf    ") && data.substring((113 * i) + (j * 16) + 17, (113 * i) + (j * 16) + 25).equals("RED     ")) {
                    model[i][j].setPiece(new ChessPiece(PlayerColor.RED, "Wolf", 4));
                } else if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("Wolf    ") && data.substring((113 * i) + (j * 16) + 17, (113 * i) + (j * 16) + 25).equals("BLUE    ")) {
                    model[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "Wolf", 4));
                }
                else   if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("Dog     ") && data.substring((113 * i) + (j * 16) + 17, (113 * i) + (j * 16) + 25).equals("RED     ")) {
                    model[i][j].setPiece(new ChessPiece(PlayerColor.RED, "Dog", 3));
                } else if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("Dog     ") && data.substring((113 * i) + (j * 16) + 17, (113 * i) + (j * 16) + 25).equals("BLUE    ")) {
                    model[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "Dog", 3));
                }
                else   if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("Cat     ") && data.substring((113 * i) + (j * 16) + 17, (113 * i) + (j * 16) + 25).equals("RED     ")) {
                    model[i][j].setPiece(new ChessPiece(PlayerColor.RED, "Cat", 2));
                } else if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("Cat     ") && data.substring((113 * i) + (j * 16) + 17, (113 * i) + (j * 16) + 25).equals("BLUE    ")) {
                    model[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "Cat", 2));
                }
                else   if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("Rat     ") && data.substring((113 * i) + (j * 16) + 17, (113 * i) + (j * 16) + 25).equals("RED     ")) {
                    model[i][j].setPiece(new ChessPiece(PlayerColor.RED, "Rat", 1));
                } else if (data.substring((113 * i) + (j * 16) + 9, (113 * i) + (j * 16) + 17).equals("Rat     ") && data.substring((113 * i) + (j * 16) + 17, (113 * i) + (j * 16) + 25).equals("BLUE    ")) {
                    model[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "Rat", 1));
                }
                else {
                    //弹窗报错，错误棋子类型
                    //终止程序
                    WrongWindow wrongWindow = new WrongWindow(500,500,"棋子类型错啦");
                    wrongWindow.setVisible(true);
                    return;
                }
            }
        }

        if (currentPlayer == PlayerColor.BLUE) {
            if (data.substring(1027, 1031).equals("BLUE")) {

            } else {
                swapColor();
            }
        }
        if (currentPlayer == PlayerColor.RED) {
            if (data.substring(1027, 1031).equals("BLUE")) {
                swapColor();
            } else {
            }
        }
        for (int a = 3; a < 6; a++) {
            for (int b = 1; b < 3; b++) {
                if ((model[a][b].getPiece() != null) && (!model[a][b].getPiece().getName().equals("Rat"))) {
                    //弹窗报错 河里进脏东西了
                    //终止或返回
                    WrongWindow wrongWindow = new WrongWindow(500,500,"河里进脏东西了");
                    wrongWindow.setVisible(true);
                    System.out.println("寄了");
                    return;
                }
            }
        }
        for (int a = 3; a < 6; a++) {
            for (int b = 4; b < 6; b++) {
                if ((model[a][b].getPiece() != null) && (!model[a][b].getPiece().getName().equals("Rat"))) {
                    //弹窗报错 河里进脏东西了
                    WrongWindow wrongWindow = new WrongWindow(500,500,"河里进脏东西了");
                    wrongWindow.setVisible(true);
                    System.out.println("寄了");
                    return;
                }
            }
        }


        //检查棋子是否重复了
        int[][] Piecemap = new int[2][8];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                Piecemap[i][j] = 0;
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                if (model[i][j].getPiece() == null) {
                    continue;
                }
                if (model[i][j].getPiece().getName().equals("Elephant") && model[i][j].getPiece().getOwner() == PlayerColor.BLUE) {
                    Piecemap[0][0]++;
                }
                if (model[i][j].getPiece().getName().equals("Lion") && model[i][j].getPiece().getOwner() == PlayerColor.BLUE) {
                    Piecemap[0][1]++;
                }
                if (model[i][j].getPiece().getName().equals("Tiger") && model[i][j].getPiece().getOwner() == PlayerColor.BLUE) {
                    Piecemap[0][2]++;
                }
                if (model[i][j].getPiece().getName().equals("Leopard") && model[i][j].getPiece().getOwner() == PlayerColor.BLUE) {
                    Piecemap[0][3]++;
                }
                if (model[i][j].getPiece().getName().equals("Wolf") && model[i][j].getPiece().getOwner() == PlayerColor.BLUE) {
                    Piecemap[0][4]++;
                }
                if (model[i][j].getPiece().getName().equals("Dog") && model[i][j].getPiece().getOwner() == PlayerColor.BLUE) {
                    Piecemap[0][5]++;
                }
                if (model[i][j].getPiece().getName().equals("Cat") && model[i][j].getPiece().getOwner() == PlayerColor.BLUE) {
                    Piecemap[0][6]++;
                }
                if (model[i][j].getPiece().getName().equals("Rat") && model[i][j].getPiece().getOwner() == PlayerColor.BLUE) {
                    Piecemap[0][7]++;
                }
                if (model[i][j].getPiece().getName().equals("Elephant") && model[i][j].getPiece().getOwner() == PlayerColor.RED) {
                    Piecemap[1][0]++;
                }
                if (model[i][j].getPiece().getName().equals("Lion") && model[i][j].getPiece().getOwner() == PlayerColor.RED) {
                    Piecemap[1][1]++;
                }
                if (model[i][j].getPiece().getName().equals("Tiger") && model[i][j].getPiece().getOwner() == PlayerColor.RED) {
                    Piecemap[1][2]++;
                }
                if (model[i][j].getPiece().getName().equals("Leopard") && model[i][j].getPiece().getOwner() == PlayerColor.RED) {
                    Piecemap[1][3]++;
                }
                if (model[i][j].getPiece().getName().equals("Wolf") && model[i][j].getPiece().getOwner() == PlayerColor.RED) {
                    Piecemap[1][4]++;
                }
                if (model[i][j].getPiece().getName().equals("Dog") && model[i][j].getPiece().getOwner() == PlayerColor.RED) {
                    Piecemap[1][5]++;
                }
                if (model[i][j].getPiece().getName().equals("Cat") && model[i][j].getPiece().getOwner() == PlayerColor.RED) {
                    Piecemap[1][6]++;
                }
                if (model[i][j].getPiece().getName().equals("Rat") && model[i][j].getPiece().getOwner() == PlayerColor.RED) {
                    Piecemap[1][7]++;
                }

            }

        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                if (Piecemap[i][j] != 0 && Piecemap[i][j] != 1) {
                    System.out.println("寄了");
                    //弹窗报错，棋子重复了
                    WrongWindow wrongWindow = new WrongWindow(500,500,"棋子重复了");
                    wrongWindow.setVisible(true);
                    //终止程序
                    return;
                }
            }
        }
        if(data.charAt(1027)<=48){
            WrongWindow wrongWindow = new WrongWindow(500,500,"没有下回合下棋方信息");
            wrongWindow.setVisible(true);
            System.out.println("寄了");
            return;
        }
        int Jieshu = 0;
        for(int i = 1040;i<1050;i++){
            if(data.charAt(i)<=48){
                Jieshu = i;
                break;
            }
        }
        String temp = data.substring(1040, Jieshu);
        Tool.round = Integer.parseInt(temp);
        board.checktraps();
        initialize();
        view.initiateChessComponent(board);
        view.repaint();
    }
    public void undo(Cell[][] grid) {
        if (Steps.size() == 0) {
            System.out.println("没下棋呢");
        } else if (Steps.size() != 0) {
            if (Steps.get(Steps.size() - 1).getDestPiece() == null) {
                Step temp = Steps.get(Steps.size() - 1);
                model.moveChessPiece(temp.getDest(), temp.getSrc());
                view.setChessComponentAtGrid(temp.getSrc(), view.removeChessComponentAtGrid(temp.getDest()));
                //   selectedPoint = null;
                view.repaint();
                swapColor();
                if (PlayerColor.BLUE==currentPlayer){
                    Tool.Player =1;
                }else {
                    Tool.Player = 2;
                }
                Tool.round--;
                Steps.remove(Steps.size()-1);}else {
                if(Steps.get(Steps.size() - 1).getDestPiece() != null){
                    Step temp1 = Steps.get(Steps.size() - 1);
                    model.moveChessPiece(temp1.getDest(), temp1.getSrc());
                    view.setChessComponentAtGrid(temp1.getSrc(), view.removeChessComponentAtGrid(temp1.getDest()));
                    grid[temp1.getDest().getRow()][temp1.getDest().getCol()].setPiece(temp1.getDestPiece());
                    model.checktraps();
                    initialize();
                    view.initiateChessComponent(model);
                    view.repaint();
                    swapColor();
                    if (PlayerColor.BLUE==currentPlayer){
                        Tool.Player =1;
                    }else {
                        Tool.Player = 2;
                    }
                    Tool.round--;
                    Steps.remove(Steps.size()-1);

                }
            }
        }
    }
    public void undo2(Cell[][] grid) {
        if (Steps.size() == 0) {
            System.out.println("没下棋呢");
        } else if (Steps.size() != 0) {
            if (Steps.get(Steps.size() - 1).getDestPiece() == null) {
                Step temp = Steps.get(Steps.size() - 1);
                model.moveChessPiece(temp.getDest(), temp.getSrc());
                //   selectedPoint = null;
                if (PlayerColor.BLUE==currentPlayer){
                    Tool.Player =1;
                }else {
                    Tool.Player = 2;
                }
                Tool.round--;
                Steps.remove(Steps.size()-1);}else {
                if(Steps.get(Steps.size() - 1).getDestPiece() != null){
                    Step temp1 = Steps.get(Steps.size() - 1);
                    model.moveChessPiece(temp1.getDest(), temp1.getSrc());
                    grid[temp1.getDest().getRow()][temp1.getDest().getCol()].setPiece(temp1.getDestPiece());
                    model.checktraps();
                    initialize();
                    if (PlayerColor.BLUE==currentPlayer){
                        Tool.Player =1;
                    }else {
                        Tool.Player = 2;
                    }
                    Tool.round--;
                    Steps.remove(Steps.size()-1);

                }
            }
        }
    }
    public void initialSteps(){
        ArrayList<Step> Steps = new ArrayList<>(0);
    }

}

