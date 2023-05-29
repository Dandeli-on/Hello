package model;
import controller.GameController;
import view.CellType;
import view.Tool;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


import static model.Constant.CHESSBOARD_COL_SIZE;
import static model.Constant.CHESSBOARD_ROW_SIZE;

/**
 * This class store the real chess information.
 * The Chessboard has 9*7 cells, and each cell has a position for chess
 */
public class Chessboard {
    public Cell[][] grid;
    int val = 0;
    public GameController gameController;

    public Chessboard() {
        this.grid = new Cell[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];//19X19

        initGrid();
        initPieces();
        initTypesOwners();
    }

    private void initGrid() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j] = new Cell();
            }
        }

    }

    private void initTypesOwners() {
        for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j].setType(CellType.land);
            }
        }
        grid[3][1].setType(CellType.water);
        grid[3][2].setType(CellType.water);
        grid[4][1].setType(CellType.water);
        grid[4][2].setType(CellType.water);
        grid[5][1].setType(CellType.water);
        grid[5][2].setType(CellType.water);
        grid[3][4].setType(CellType.water);
        grid[3][5].setType(CellType.water);
        grid[4][4].setType(CellType.water);
        grid[4][5].setType(CellType.water);
        grid[5][4].setType(CellType.water);
        grid[5][5].setType(CellType.water);
        grid[0][2].setType(CellType.traps);
        grid[0][2].setOwner(PlayerColor.RED);
        grid[0][4].setType(CellType.traps);
        grid[0][4].setOwner(PlayerColor.RED);
        grid[1][3].setType(CellType.traps);
        grid[1][3].setOwner(PlayerColor.RED);
        grid[7][3].setType(CellType.traps);
        grid[7][3].setOwner(PlayerColor.BLUE);
        grid[8][2].setType(CellType.traps);
        grid[8][2].setOwner(PlayerColor.BLUE);
        grid[8][4].setType(CellType.traps);
        grid[8][4].setOwner(PlayerColor.BLUE);
        grid[0][3].setType(CellType.dens);
        grid[0][3].setOwner(PlayerColor.RED);
        grid[8][3].setType(CellType.dens);
        grid[8][3].setOwner(PlayerColor.BLUE);

    }

    public void initPieces() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                grid[i][j].removePiece();
            }
        }
        grid[6][0].setPiece(new ChessPiece(PlayerColor.BLUE, "Elephant", 8));
        grid[2][6].setPiece(new ChessPiece(PlayerColor.RED, "Elephant", 8));
        grid[8][6].setPiece(new ChessPiece(PlayerColor.BLUE, "Lion", 7));
        grid[0][0].setPiece(new ChessPiece(PlayerColor.RED, "Lion", 7));
        grid[8][0].setPiece(new ChessPiece(PlayerColor.BLUE, "Tiger", 6));
        grid[0][6].setPiece(new ChessPiece(PlayerColor.RED, "Tiger", 6));
        grid[6][4].setPiece(new ChessPiece(PlayerColor.BLUE, "Leopard", 5));
        grid[2][2].setPiece(new ChessPiece(PlayerColor.RED, "Leopard", 5));
        grid[6][2].setPiece(new ChessPiece(PlayerColor.BLUE, "Wolf", 4));
        grid[2][4].setPiece(new ChessPiece(PlayerColor.RED, "Wolf", 4));
        grid[7][5].setPiece(new ChessPiece(PlayerColor.BLUE, "Dog", 3));
        grid[1][1].setPiece(new ChessPiece(PlayerColor.RED, "Dog", 3));
        grid[7][1].setPiece(new ChessPiece(PlayerColor.BLUE, "Cat", 2));
        grid[1][5].setPiece(new ChessPiece(PlayerColor.RED, "Cat", 2));
        grid[6][6].setPiece(new ChessPiece(PlayerColor.BLUE, "Rat", 1));
        grid[2][0].setPiece(new ChessPiece(PlayerColor.RED, "Rat", 1));
    }

    private ChessPiece getChessPieceAt(ChessboardPoint point) {
        return getGridAt(point).getPiece();
    }

    public Cell getGridAt(ChessboardPoint point) {
        return grid[point.getRow()][point.getCol()];
    }

    private int calculateDistance(ChessboardPoint src, ChessboardPoint dest) {
        return Math.abs(src.getRow() - dest.getRow()) + Math.abs(src.getCol() - dest.getCol());
    }

    private ChessPiece removeChessPiece(ChessboardPoint point) {
        ChessPiece chessPiece = getChessPieceAt(point);
        getGridAt(point).removePiece();
        return chessPiece;
    }

    private void setChessPiece(ChessboardPoint point, ChessPiece chessPiece) {
        getGridAt(point).setPiece(chessPiece);
    }

    public void moveChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (!isValidMove(src, dest)) {
            throw new IllegalArgumentException("Illegal chess move!");
        }
        setChessPiece(dest, removeChessPiece(src));
    }

    public void captureChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (!isValidCapture(src, dest)) {
            throw new IllegalArgumentException("Illegal chess capture!");
        }
        removeChessPiece(dest);
        setChessPiece(dest, removeChessPiece(src));
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public PlayerColor getChessPieceOwner(ChessboardPoint point) {
        return getGridAt(point).getPiece().getOwner();
    }

    public boolean isValidMove(ChessboardPoint src, ChessboardPoint dest) {
        if (getChessPieceAt(src) == null || getChessPieceAt(dest) != null) {
            return false;
        }
        if (getGridAt(dest).getType().equals(CellType.water) && getChessPieceAt(src).getName().equals("Rat")) {
            if (calculateDistance(src, dest) == 1) {
                return true;
            } else return false;
        }
        if (getGridAt(dest).getType().equals(CellType.water)) {
            if (!getChessPieceAt(src).getName().equals("Rat")) {
                return false;
            }
        }
        if ((calculateDistance(src, dest) != 1) && (getChessPieceAt(src).getName().equals("Lion") | getChessPieceAt(src).getName().equals("Tiger"))) {
            if ((src.getCol() - dest.getCol() != 0) && (src.getRow() - dest.getRow() != 0)) {
                return false;
            }
            if (src.getRow() == dest.getRow()) {
                if ((src.getCol() - dest.getCol()) < 0) {
                    for (int i = src.getCol() + 1; i < dest.getCol(); i++) {
                        if ((grid[src.getRow()][i].getType() != CellType.water) || (grid[src.getRow()][i].getPiece() != null)) {
                            return false;
                        }
                    }
                    return true;
                }
                if ((src.getCol() - dest.getCol()) > 0) {
                    for (int i = dest.getCol() + 1; i < src.getCol(); i++) {
                        if ((grid[src.getRow()][i].getType() != CellType.water) || (grid[src.getRow()][i].getPiece() != null)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            if (src.getCol() == dest.getCol()) {
                if (src.getRow() - dest.getRow() < 0) {
                    for (int i = src.getRow() + 1; i < dest.getRow(); i++) {
                        if ((grid[i][src.getCol()].getType() != CellType.water) || (grid[i][src.getCol()].getPiece() != null)) {
                            return false;
                        }
                    }
                    return true;
                }
                if (src.getRow() - dest.getRow() > 0) {
                    for (int i = dest.getRow() + 1; i < src.getRow(); i++) {
                        if ((grid[i][src.getCol()].getType() != CellType.water) || (grid[i][src.getCol()].getPiece() != null)) {
                            return false;
                        }
                    }
                    return true;
                }

            }

        }
        if (getGridAt(dest).getType().equals(CellType.dens) && calculateDistance(src, dest) == 1) {
            if (getChessPieceAt(src).getOwner() != getGridAt(dest).getOwner()) {
                return true;
            }
            return false;
        }
        return calculateDistance(src, dest) == 1;
    }


    public boolean isValidCapture(ChessboardPoint src, ChessboardPoint dest) {
        ChessPiece chessPiecesrc = getChessPieceAt(src);
        ChessPiece chessPiecedest = getChessPieceAt(dest);
        if (chessPiecesrc == null || chessPiecedest == null) {
            return false;
        } else {
            if (chessPiecesrc.getOwner() == chessPiecedest.getOwner()) {
                return false;
            } else {
                if (getGridAt(dest).getType() == CellType.water) {
                    return false;
                }
                if (getGridAt(src).getType() == CellType.water) {
                    return false;
                }
                if ((calculateDistance(src, dest) != 1) && (getChessPieceAt(src).getName().equals("Lion") | getChessPieceAt(src).getName().equals("Tiger"))) {
                    if ((src.getCol() - dest.getCol() != 0) && (src.getRow() - dest.getRow() != 0)) {
                        return false;
                    }
                    if (src.getRow() == dest.getRow()) {
                        if ((src.getCol() - dest.getCol()) < 0) {
                            for (int i = src.getCol() + 1; i < dest.getCol(); i++) {
                                if ((grid[src.getRow()][i].getType() != CellType.water) || (grid[src.getRow()][i].getPiece() != null)) {
                                    return false;
                                }
                            }
                            boolean res;
                            res = chessPiecesrc.canCapture(chessPiecedest);
                            return res;
                        }
                        if ((src.getCol() - dest.getCol()) > 0) {
                            for (int i = dest.getCol() + 1; i < src.getCol(); i++) {
                                if ((grid[src.getRow()][i].getType() != CellType.water) || (grid[src.getRow()][i].getPiece() != null)) {
                                    return false;
                                }
                            }
                            boolean res;
                            res = chessPiecesrc.canCapture(chessPiecedest);
                            return res;
                        }
                    }
                    if (src.getCol() == dest.getCol()) {
                        if (src.getRow() - dest.getRow() < 0) {
                            for (int i = src.getRow() + 1; i < dest.getRow(); i++) {
                                if ((grid[i][src.getCol()].getType() != CellType.water) || (grid[i][src.getCol()].getPiece() != null)) {
                                    return false;
                                }
                            }
                            boolean res;
                            res = chessPiecesrc.canCapture(chessPiecedest);
                            return res;
                        }
                        if (src.getRow() - dest.getRow() > 0) {
                            for (int i = dest.getRow() + 1; i < src.getRow(); i++) {
                                if ((grid[i][src.getCol()].getType() != CellType.water) || (grid[i][src.getCol()].getPiece() != null)) {
                                    return false;
                                }
                            }
                            boolean res;
                            res = chessPiecesrc.canCapture(chessPiecedest);
                            return res;
                        }
                    }
                }
                boolean res2;
                res2 = (calculateDistance(src, dest) == 1) && (chessPiecesrc.canCapture(chessPiecedest));
                return res2;
            }
        }
    }

    //判断是否或离开进入陷阱
    public void inOrOutTraps(ChessboardPoint srcPoint, ChessboardPoint destPoint) {
        if (getGridAt(destPoint).getType() == CellType.traps && getChessPieceAt(srcPoint).getOwner() != getGridAt(destPoint).getOwner()) {
            getTrapped(srcPoint);
        } else if (getGridAt(srcPoint).getType() == CellType.traps && getChessPieceAt(srcPoint).getOwner() != getGridAt(srcPoint).getOwner()) {
            exitTrap(srcPoint);
        }
    }

    public void checktraps() {
        if (grid[0][2].getPiece() != null && grid[0][2].getPiece().getOwner() != grid[0][2].getOwner()) {
            grid[0][2].getPiece().setRank(0);
        }
        if (grid[0][4].getPiece() != null && grid[0][4].getPiece().getOwner() != grid[0][4].getOwner()) {
            grid[0][4].getPiece().setRank(0);
        }
        if (grid[1][3].getPiece() != null && grid[1][3].getPiece().getOwner() != grid[1][3].getOwner()) {
            grid[1][3].getPiece().setRank(0);
        }
        if (grid[7][3].getPiece() != null && grid[7][3].getPiece().getOwner() != grid[7][3].getOwner()) {
            grid[7][3].getPiece().setRank(0);
        }
        if (grid[8][2].getPiece() != null && grid[8][2].getPiece().getOwner() != grid[8][2].getOwner()) {
            grid[8][2].getPiece().setRank(0);
        }
        if (grid[8][4].getPiece() != null && grid[8][4].getPiece().getOwner() != grid[8][4].getOwner()) {
            grid[8][4].getPiece().setRank(0);
        }
    }

    //判断是否进入地方兽穴
    public boolean enterDens(ChessboardPoint destPoint) {
        if (getGridAt(destPoint).getType() == CellType.dens) {
            return true;
        }
        return false;
    }

    //被困住，rank降为0
    public void getTrapped(ChessboardPoint point) {
        getGridAt(point).getPiece().setRank(0);
    }

    public void exitTrap(ChessboardPoint point) {
        if (getGridAt(point).getPiece().getName().equals("Elephant")) {
            getGridAt(point).getPiece().setRank(8);
        }
        if (getGridAt(point).getPiece().getName().equals("Lion")) {
            getGridAt(point).getPiece().setRank(7);
        }
        if (getGridAt(point).getPiece().getName().equals("Tiger")) {
            getGridAt(point).getPiece().setRank(6);
        }
        if (getGridAt(point).getPiece().getName().equals("Leopard")) {
            getGridAt(point).getPiece().setRank(5);
        }
        if (getGridAt(point).getPiece().getName().equals("Wolf")) {
            getGridAt(point).getPiece().setRank(4);
        }
        if (getGridAt(point).getPiece().getName().equals("Dog")) {
            getGridAt(point).getPiece().setRank(3);
        }
        if (getGridAt(point).getPiece().getName().equals("Cat")) {
            getGridAt(point).getPiece().setRank(2);
        }
        if (getGridAt(point).getPiece().getName().equals("Rat")) {
            getGridAt(point).getPiece().setRank(1);
        }
    }

    public boolean isBlueDieOut() {
        boolean res = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                if (grid[i][j].getPiece() == null) {
                    continue;
                }
                if (grid[i][j].getPiece().getOwner().equals(PlayerColor.BLUE)) {
                    res = false;
                    break;
                }
            }
        }
        return res;
    }

    public boolean isRedDieOut() {
        boolean res = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                if (grid[i][j].getPiece() == null) {
                    continue;
                }
                if (grid[i][j].getPiece().getOwner().equals(PlayerColor.RED)) {
                    res = false;
                    break;
                }
            }
        }
        return res;
    }

    public List<ChessboardPoint> getValidPoints(PlayerColor playerColor) {
        /*List<ChessboardPoint> validPoints = new ArrayList<>();
        for (int i = 9; i < 9; i++) {
            for (int j = 7; j < 7; j++) {
                ChessboardPoint chessboardPoint = new ChessboardPoint(i, j);
                if (getChessPieceAt(chessboardPoint).getOwner() == playerColor && getChessPieceAt(chessboardPoint) != null) {
                    validPoints.add(chessboardPoint);
                }
            }
        }
        return validPoints;*/
        List<ChessboardPoint> availablePoints = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                ChessboardPoint point = new ChessboardPoint(i, j);
                if (getChessPieceAt(point) != null && getChessPieceAt(point).getOwner() == playerColor) {
                    availablePoints.add(point);
                }
            }
        }
        return availablePoints;
    }

    public List<ChessboardPoint> getValidMoves(ChessboardPoint point) {
        List<ChessboardPoint> validMovePoints = new ArrayList<>();
        if (gameController.getSelectedPoint()!=null){
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 7; j++) {
                    ChessboardPoint everyPoint = new ChessboardPoint(i, j);
                    if (isValidMove(gameController.getSelectedPoint(), everyPoint)) {
                        gameController.getView().HideArea(everyPoint);
                        gameController.getView().repaint();
                    }
                    if (isValidCapture(gameController.getSelectedPoint(), everyPoint)) {
                        gameController.getView().getChessComponentAtGrid(everyPoint).setBeSelected(false);
                        gameController.getView().getChessComponentAtGrid(everyPoint).repaint();
                    }
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                ChessboardPoint chessboardPoint = new ChessboardPoint(i, j);
                if (isValidMove(point, chessboardPoint)|isValidCapture(point, chessboardPoint)) {
                    validMovePoints.add(chessboardPoint);
                }
            }
        }
        return validMovePoints;
    }

    public List<Step> getValidStep(PlayerColor playerColor) {
        List<Step> validSteps = new ArrayList<>();
        List<ChessboardPoint> validMovePoints;
        List<ChessboardPoint> validPoints = getValidPoints(playerColor);
        for (int i = 0; i < validPoints.size(); i++) {
            validMovePoints = getValidMoves(validPoints.get(i));
            for (int j = 0; j < validMovePoints.size(); j++) {
                Step step = new Step("Step" + Tool.round, validPoints.get(i), validMovePoints.get(j), getChessPieceAt(validPoints.get(i)), getChessPieceAt(validMovePoints.get(j)), playerColor);
                validSteps.add(step);

            }
        }
        return validSteps;
    }
    public void registerGame(GameController gameController){
        this.gameController=gameController;
    }

    public Step getFinalstep() {
        return finalstep;
    }

    public void setFinalstep(Step finalstep) {
        this.finalstep = finalstep;
    }

    public Step finalstep;
    public Step palyerStep;
    public Step bestStep(PlayerColor playerColor){
        List<Step> validSteps=getValidStep(PlayerColor.RED);
        Max(2,playerColor);
        return validSteps.get(Record);
    }
    int Record=0;
    public int Max(int depth, PlayerColor playerColor){
        int best = -10000000;
        if (depth<=0){
            if (playerColor==PlayerColor.RED){
                int a =Value(PlayerColor.RED);
               return a;
            }else {
                int a =Value(PlayerColor.BLUE);
                return a;
            }
        }
        List<Step> validSteps = getValidStep(playerColor);
        for (int i =0;i<validSteps.size();i++){
              gameController.UseAItoMove2(validSteps.get(i));
            if (playerColor==PlayerColor.RED){
            val = -Max(depth-1,PlayerColor.BLUE);
            }else {
                val = -Max(depth-1,PlayerColor.RED);
            }
            gameController.undo2(grid);
           if (val>best){
               best=val;
               if (depth==2){
               Record=i;
                    }
               }
           }
        return best;
           }


    /*private int Evaluate(PlayerColor playerColor){
        List<Step> validSteps = getValidStep(playerColor);

    }

     */
    public List<Step> valueSteps(PlayerColor playerColor) {
        List<Step> validSteps = getValidStep(playerColor);
        for (int i = 0; i < validSteps.size(); i++) {
            if (playerColor == PlayerColor.RED) {
                validSteps.get(i).setValue(30 - Math.abs(validSteps.get(i).getSrc().getCol() - 3) - Math.abs(validSteps.get(i).getSrc().getRow() - 8));
                if (Tool.round <= 10) {
                    validSteps.get(i).setValue(validSteps.get(i).getValue() + (int) (10 - validSteps.get(i).getSrcPiece().getRank())/2);
                }
                if (Tool.round > 10) {
                    validSteps.get(i).setValue(validSteps.get(i).getValue() + validSteps.get(i).getSrcPiece().getRank());
                }
                validSteps.get(i).setValue(validSteps.get(i).getValue() + (validSteps.get(i).getDest().getRow() - validSteps.get(i).getSrc().getRow()) +  (Math.abs(validSteps.get(i).getSrc().getCol() - 3) - Math.abs(validSteps.get(i).getDest().getCol() - 3)));
                if (validSteps.get(i).getDest().getRow()==8&&validSteps.get(i).getDest().getCol()==3){
                    validSteps.get(i).setValue(10000);
                }
                if (getGridAt(validSteps.get(i).getDest()).getType()==CellType.traps&&validSteps.get(i).getDest().getRow()>6){
                   ChessboardPoint chessboardPoint1 = new ChessboardPoint(validSteps.get(i).getDest().getRow()-1,validSteps.get(i).getDest().getCol());
                    ChessboardPoint chessboardPoint2 = new ChessboardPoint(validSteps.get(i).getDest().getRow(),validSteps.get(i).getDest().getCol()+1);
                    ChessboardPoint chessboardPoint3 = new ChessboardPoint(validSteps.get(i).getDest().getRow(),validSteps.get(i).getDest().getCol()-1);
                   List<ChessboardPoint> chessboardPoints = new ArrayList<>();
                   chessboardPoints.add(chessboardPoint1);
                   chessboardPoints.add(chessboardPoint2);
                   chessboardPoints.add(chessboardPoint3);
                   for (int j =0;i<chessboardPoints.size();i++){
                       if (getChessPieceAt(chessboardPoints.get(i))!=null&&getChessPieceAt(chessboardPoints.get(i)).getOwner()!=playerColor){
                           validSteps.get(i).setValue(validSteps.get(i).getValue()-100);
                       }
                   }
                   if (validSteps.get(i).getValue()>0){
                       validSteps.get(i).setValue(10000);
                   }
                }
            }else {
                validSteps.get(i).setValue(40 - Math.abs(validSteps.get(i).getSrc().getCol() - 3) - Math.abs(validSteps.get(i).getSrc().getCol() - 3));
                if (Tool.round <= 10) {
                    validSteps.get(i).setValue(validSteps.get(i).getValue() + (int) (10 - validSteps.get(i).getSrcPiece().getRank())/2);
                }
                if (Tool.round > 10) {
                    validSteps.get(i).setValue(validSteps.get(i).getValue() + validSteps.get(i).getSrcPiece().getRank()/2);
                }
                validSteps.get(i).setValue(validSteps.get(i).getValue() + 2 * (validSteps.get(i).getSrc().getRow() - validSteps.get(i).getDest().getRow()) + 2 * (Math.abs(validSteps.get(i).getSrc().getCol() - 3) - Math.abs(validSteps.get(i).getDest().getCol() - 3)));
                if (validSteps.get(i).getDest().getRow()==0&&validSteps.get(i).getDest().getCol()==3){
                    validSteps.get(i).setValue(10000);
                }
                if (getGridAt(validSteps.get(i).getDest()).getType()==CellType.traps&&validSteps.get(i).getDest().getRow()>6){
                    ChessboardPoint chessboardPoint1 = new ChessboardPoint(validSteps.get(i).getDest().getRow()+1,validSteps.get(i).getDest().getCol());
                    ChessboardPoint chessboardPoint2 = new ChessboardPoint(validSteps.get(i).getDest().getRow(),validSteps.get(i).getDest().getCol()+1);
                    ChessboardPoint chessboardPoint3 = new ChessboardPoint(validSteps.get(i).getDest().getRow(),validSteps.get(i).getDest().getCol()-1);
                    List<ChessboardPoint> chessboardPoints = new ArrayList<>();
                    chessboardPoints.add(chessboardPoint1);
                    chessboardPoints.add(chessboardPoint2);
                    chessboardPoints.add(chessboardPoint3);
                    for (int j =0;j<chessboardPoints.size();j++){
                        if (getChessPieceAt(chessboardPoints.get(j))!=null&&getChessPieceAt(chessboardPoints.get(j)).getOwner()!=playerColor){
                            validSteps.get(i).setValue(validSteps.get(i).getValue()-100);
                        }
                    }
                    if (validSteps.get(i).getValue()>0){
                        validSteps.get(i).setValue(10000);
                    }
                }
            }
            if (validSteps.get(i).getSrcPiece().getName().equals("Rat")&&getGridAt(validSteps.get(i).getDest()).getType()==CellType.water ){
                validSteps.get(i).setValue(validSteps.get(i).getValue()+1);
            }
              if (validSteps.get(i).getDestPiece()!=null){
                  validSteps.get(i).setValue(validSteps.get(i).getValue()+validSteps.get(i).getDestPiece().getRank()*validSteps.get(i).getDestPiece().getRank()+100);
                  if (validSteps.get(i).getDestPiece().getRank()<=4&&validSteps.get(i).getDestPiece().getRank()>0){
                      validSteps.get(i).setValue(validSteps.get(i).getValue()+8-validSteps.get(i).getDestPiece().getRank());
                  }
                    if (validSteps.get(i).getDestPiece().getRank()<=0){
                        validSteps.get(i).setValue(1000);
                    }
              }

        }
        return validSteps;
    }
    public int Value(PlayerColor playerColor) {
               int value = 0;
               int badValue=0;
               List<ChessboardPoint> chessboardPointsAI = getValidPoints(playerColor);
               for (int i = 0; i<chessboardPointsAI.size();i++){
                   ChessboardPoint chessboardPoint = chessboardPointsAI.get(i);
                   value=value+getChessPieceAt(chessboardPoint).getRank()*100;
                   if (getGridAt(chessboardPoint).getType()==CellType.dens){
                       value=value+1000000;
                   }
                   if (getGridAt(chessboardPoint).getType()==CellType.traps&&chessboardPoint.getRow()>6){
                       ChessboardPoint chessboardPoint1 = new ChessboardPoint(chessboardPoint.getRow()-1,chessboardPoint.getCol());
                       ChessboardPoint chessboardPoint2 = new ChessboardPoint(chessboardPoint.getRow(),chessboardPoint.getCol()+1);
                       ChessboardPoint chessboardPoint3 = new ChessboardPoint(chessboardPoint.getRow(),chessboardPoint.getCol()-1);
                       List<ChessboardPoint> chessboardPoints = new ArrayList<>();
                       chessboardPoints.add(chessboardPoint1);
                       chessboardPoints.add(chessboardPoint2);
                       chessboardPoints.add(chessboardPoint3);
                       int k =0;
                       for (int j =0;i<chessboardPoints.size();i++){
                           if (getChessPieceAt(chessboardPoints.get(i))!=null&&getChessPieceAt(chessboardPoints.get(i)).getOwner()!=playerColor){
                               value=value-10000;
                               k++;
                           }
                       }
                      if (k==0){
                          value=value+100000;
                      }
                   }
                   value=300 - Math.abs(chessboardPoint.getCol() - 3)*10 - Math.abs(chessboardPoint.getRow() - 8)*10;
               }
               List<ChessboardPoint> chessboardPointsPlayer = getValidPoints(PlayerColor.BLUE);
                for(int i = 0;i<chessboardPointsPlayer.size();i++){
                    badValue=badValue+getChessPieceAt(chessboardPointsPlayer.get(i)).getRank()*100;
                }
               return value-badValue;
    }/*int value=0;
        List<Step> validSteps = getValidStep(playerColor);
        for (int i = 0; i < validSteps.size(); i++) {
            if (playerColor == PlayerColor.RED) {
               value=value+30 - Math.abs(validSteps.get(i).getSrc().getCol() - 3) - Math.abs(validSteps.get(i).getSrc().getRow() - 8);
                if (Tool.round <= 10) {
                   value=value+validSteps.get(i).getValue() + (int) (10 - validSteps.get(i).getSrcPiece().getRank())/2;
                }
                if (Tool.round > 10) {
                  value=value + validSteps.get(i).getSrcPiece().getRank();
                }
                value=value + (validSteps.get(i).getDest().getRow() - validSteps.get(i).getSrc().getRow()) +  (Math.abs(validSteps.get(i).getSrc().getCol() - 3) - Math.abs(validSteps.get(i).getDest().getCol() - 3));
                if (validSteps.get(i).getDest().getRow()==8&&validSteps.get(i).getDest().getCol()==3){
                    validSteps.get(i).setValue(10000);
                }
                if (getGridAt(validSteps.get(i).getDest()).getType()==CellType.traps&&validSteps.get(i).getDest().getRow()>6){
                    ChessboardPoint chessboardPoint1 = new ChessboardPoint(validSteps.get(i).getDest().getRow()-1,validSteps.get(i).getDest().getCol());
                    ChessboardPoint chessboardPoint2 = new ChessboardPoint(validSteps.get(i).getDest().getRow(),validSteps.get(i).getDest().getCol()+1);
                    ChessboardPoint chessboardPoint3 = new ChessboardPoint(validSteps.get(i).getDest().getRow(),validSteps.get(i).getDest().getCol()-1);
                    List<ChessboardPoint> chessboardPoints = new ArrayList<>();
                    chessboardPoints.add(chessboardPoint1);
                    chessboardPoints.add(chessboardPoint2);
                    chessboardPoints.add(chessboardPoint3);
                    for (int j =0;i<chessboardPoints.size();i++){
                        if (getChessPieceAt(chessboardPoints.get(i))!=null&&getChessPieceAt(chessboardPoints.get(i)).getOwner()!=playerColor){
                            value=value-1000;
                        }
                    }
                    if (validSteps.get(i).getValue()>0){
                       value=value+10000;
                    }
                }
            }else {
                value=value+40 - Math.abs(validSteps.get(i).getSrc().getCol() - 3) - Math.abs(validSteps.get(i).getSrc().getCol() - 3);
                if (Tool.round <= 10) {
                    value=value + (int) (10 - validSteps.get(i).getSrcPiece().getRank())/2;
                }
                if (Tool.round > 10) {
                   value=value + validSteps.get(i).getSrcPiece().getRank()/2;
                }
                value=value + 2 * (validSteps.get(i).getSrc().getRow() - validSteps.get(i).getDest().getRow()) + 2 * (Math.abs(validSteps.get(i).getSrc().getCol() - 3) - Math.abs(validSteps.get(i).getDest().getCol() - 3));
                if (validSteps.get(i).getDest().getRow()==0&&validSteps.get(i).getDest().getCol()==3){
                    value=value+10000;
                }
                if (getGridAt(validSteps.get(i).getDest()).getType()==CellType.traps&&validSteps.get(i).getDest().getRow()>6){
                    ChessboardPoint chessboardPoint1 = new ChessboardPoint(validSteps.get(i).getDest().getRow()+1,validSteps.get(i).getDest().getCol());
                    ChessboardPoint chessboardPoint2 = new ChessboardPoint(validSteps.get(i).getDest().getRow(),validSteps.get(i).getDest().getCol()+1);
                    ChessboardPoint chessboardPoint3 = new ChessboardPoint(validSteps.get(i).getDest().getRow(),validSteps.get(i).getDest().getCol()-1);
                    List<ChessboardPoint> chessboardPoints = new ArrayList<>();
                    chessboardPoints.add(chessboardPoint1);
                    chessboardPoints.add(chessboardPoint2);
                    chessboardPoints.add(chessboardPoint3);
                    for (int j =0;j<chessboardPoints.size();j++){
                        if (getChessPieceAt(chessboardPoints.get(j))!=null&&getChessPieceAt(chessboardPoints.get(j)).getOwner()!=playerColor){
                           value=value-1000;
                        }
                    }
                    if (validSteps.get(i).getValue()>0){
                       value=value+10000;
                    }
                }
            }
            if (validSteps.get(i).getSrcPiece().getName().equals("Rat")&&getGridAt(validSteps.get(i).getDest()).getType()==CellType.water ){
                value=value+3;
            }
            if (validSteps.get(i).getDestPiece()!=null){
                value=value+validSteps.get(i).getDestPiece().getRank()*validSteps.get(i).getDestPiece().getRank()+100;
                if (validSteps.get(i).getDestPiece().getRank()<=4&&validSteps.get(i).getDestPiece().getRank()>0){
                    value=value+8-validSteps.get(i).getDestPiece().getRank();
                }
                if (validSteps.get(i).getDestPiece().getRank()<=0){
                    value=value+1000;
                }
            }

        }
        return value;
    }
   /* public int Value(Step step,PlayerColor playerColor) {
        if (playerColor == PlayerColor.RED) {
            step.setValue(30 - Math.abs(step.getSrc().getCol() - 3) - Math.abs(step.getSrc().getRow() - 8));
            if (Tool.round <= 10) {
                step.setValue(step.getValue() + (int) (10 - step.getSrcPiece().getRank()) / 2);
            }
            if (Tool.round > 10) {
                step.setValue(step.getValue() + step.getSrcPiece().getRank());
            }
            step.setValue(step.getValue() + (step.getDest().getRow() - step.getSrc().getRow()) + (Math.abs(step.getSrc().getCol() - 3) - Math.abs(step.getDest().getCol() - 3)));
            if (step.getDest().getRow() == 8 && step.getDest().getCol() == 3) {
                step.setValue(10000);
            }
            if (getGridAt(step.getDest()).getType() == CellType.traps && step.getDest().getRow() > 6) {
                ChessboardPoint chessboardPoint1 = new ChessboardPoint(step.getDest().getRow() - 1, step.getDest().getCol());
                ChessboardPoint chessboardPoint2 = new ChessboardPoint(step.getDest().getRow(), step.getDest().getCol() + 1);
                ChessboardPoint chessboardPoint3 = new ChessboardPoint(step.getDest().getRow(), step.getDest().getCol() - 1);
                List<ChessboardPoint> chessboardPoints = new ArrayList<>();
                chessboardPoints.add(chessboardPoint1);
                chessboardPoints.add(chessboardPoint2);
                chessboardPoints.add(chessboardPoint3);
                for (int j = 0; j < chessboardPoints.size(); j++) {
                    if (getChessPieceAt(chessboardPoints.get(j)) != null && getChessPieceAt(chessboardPoints.get(j)).getOwner() != playerColor) {
                        step.setValue(step.getValue() - 100);
                    }
                }
                if (step.getValue() > 0) {
                    step.setValue(10000);
                }
            }
        } else {
            step.setValue(30 - Math.abs(step.getSrc().getCol() - 3) - Math.abs(step.getSrc().getCol() - 3));
            if (Tool.round <= 10) {
                step.setValue(step.getValue() + (int) (10 - step.getSrcPiece().getRank()) / 2);
            }
            if (Tool.round > 10) {
                step.setValue(step.getValue() + step.getSrcPiece().getRank() / 2);
            }
            step.setValue(step.getValue() + 2 * (step.getSrc().getRow() - step.getDest().getRow()) + 2 * (Math.abs(step.getSrc().getCol() - 3) - Math.abs(step.getDest().getCol() - 3)));
            if (step.getDest().getRow() == 0 && step.getDest().getCol() == 3) {
                step.setValue(10000);
            }
            if (getGridAt(step.getDest()).getType() == CellType.traps && step.getDest().getRow() > 6) {
                ChessboardPoint chessboardPoint1 = new ChessboardPoint(step.getDest().getRow() + 1, step.getDest().getCol());
                ChessboardPoint chessboardPoint2 = new ChessboardPoint(step.getDest().getRow(), step.getDest().getCol() + 1);
                ChessboardPoint chessboardPoint3 = new ChessboardPoint(step.getDest().getRow(), step.getDest().getCol() - 1);
                List<ChessboardPoint> chessboardPoints = new ArrayList<>();
                chessboardPoints.add(chessboardPoint1);
                chessboardPoints.add(chessboardPoint2);
                chessboardPoints.add(chessboardPoint3);
                for (int j = 0; j < chessboardPoints.size(); j++) {
                    if (getChessPieceAt(chessboardPoints.get(j)) != null && getChessPieceAt(chessboardPoints.get(j)).getOwner() != playerColor) {
                        step.setValue(step.getValue() - 100);
                    }
                }
                if (step.getValue() > 0) {
                    step.setValue(10000);
                }
            }
        }
        if (step.getSrcPiece().getName().equals("Rat") && getGridAt(step.getDest()).getType() == CellType.water) {
            step.setValue(step.getValue() + 1);
        }
        if (step.getDestPiece() != null) {
            step.setValue(step.getValue() + step.getDestPiece().getRank() * step.getDestPiece().getRank() + 20);
            if (step.getDestPiece().getRank() <= 4 && step.getDestPiece().getRank() > 0) {
                step.setValue(step.getValue() + 8 - step.getDestPiece().getRank());
            }
            if (step.getDestPiece().getRank() <= 0) {
                step.setValue(1000);
            }
        }
       return step.getValue();
    }*/

}
