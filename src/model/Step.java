package model;

public class Step {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChessboardPoint getSrc() {
        return src;
    }

    public void setSrc(ChessboardPoint src) {
        this.src = src;
    }

    public ChessboardPoint getDest() {
        return dest;
    }

    public void setDest(ChessboardPoint dest) {
        this.dest = dest;
    }

    public ChessPiece getSrcPiece() {
        return srcPiece;
    }

    public void setSrcPiece(ChessPiece srcPiece) {
        this.srcPiece = srcPiece;
    }

    public ChessPiece getDestPiece() {
        return destPiece;
    }

    public void setDestPiece(ChessPiece destPiece) {
        this.destPiece = destPiece;
    }

    private String name;
    private ChessboardPoint src;
    private ChessboardPoint dest;

    private ChessPiece srcPiece;

    private ChessPiece destPiece;

    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
    }

    private transient int Value;

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    private PlayerColor playerColor;


    public Step(String name, ChessboardPoint src, ChessboardPoint dest, ChessPiece srcPiece, ChessPiece destPiece,PlayerColor playerColor) {
        this.name = name;
        this.src = src;
        this.dest = dest;
        this.srcPiece = srcPiece;
        this.destPiece = destPiece;
        this.playerColor=playerColor;
    }

    public String toString() {
        String res;
        if (this.destPiece != null) {
            res = name + srcPiece.toString() + src.toString() + destPiece.toString() + dest.toString();
        } else {
            res = name + srcPiece.toString() + src.toString() + "null" + dest.toString();

        }
        return res;
    }
}
