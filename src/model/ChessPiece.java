package model;


public class ChessPiece {
    private PlayerColor owner;

    private String name;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    private int rank;

    public ChessPiece(PlayerColor owner, String name, int rank) {
        this.owner = owner;
        this.name = name;
        this.rank = rank;
    }

    public boolean canCapture(ChessPiece target) {
        if(this.getOwner()==target.getOwner()){
         return false;
        }
        if (target.name.equals("Elephant") && this.name.equals("Rat")){
            return true;
        }
        if (target.name.equals("Rat") && this.name.equals("Elephant")){
            return false;
        }
        if (this.rank >= target.rank) {
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public PlayerColor getOwner() {
        return owner;
    }
    public String toString(){
        String res;
        String Color;
  /*  if(this.getOwner().equals(PlayerColor.RED)){
        Color = "RED";
    }else{
        Color = "BLUE";
    }*/
        res = this.name;
        return res;
    }
}
