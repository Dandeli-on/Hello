package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import controller.GameController;
import model.ChessboardPoint;

/**
 * This is the equivalent of the Cell class,
 * but this class only cares how to draw Cells on ChessboardComponent
 */

public class CellComponent extends JPanel {
    private Image background;
    private ImageIcon imageIcon;
    private CellType gridType;
    private ImageIcon imageIcon2=new ImageIcon(getClass().getResource("/Image/SelectBack.png" ));
   private int size;
   private GameController gameController ;
   private Point location;
   private boolean selected;
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public boolean isSelected() {
        return selected;
    }

    public CellComponent(CellType gridType, Point location, int size) {
        setLayout(new GridLayout(1, 1));
        setLocation(location);
        setSize(size, size);
        this.size=size;
        this.location=location;
        this.gridType = gridType;
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        if (gridType.equals(CellType.water)) {
            imageIcon = new ImageIcon(getClass().getResource("/Image/HL2.png" ));
            background=imageIcon.getImage();
        } else if (gridType.equals(CellType.land)) {
            imageIcon = new ImageIcon(getClass().getResource("/Image/CP4.png" ));
            background=imageIcon.getImage();
        } else if (gridType.equals(CellType.traps)) {
            imageIcon = new ImageIcon(getClass().getResource("/Image/XJ.jpg" ));
            background=imageIcon.getImage();
        } else if (gridType.equals(CellType.dens)) {
            imageIcon = new ImageIcon(getClass().getResource("/Image/SD2.jpg" ));
            background=imageIcon.getImage();
        }
    }
    public void registerController(GameController gameController) {
        this.gameController = gameController;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.drawImage(background,1,1,getWidth()-1,getHeight()-1,this);
        if (isSelected()) { // Highlights the model if selected.
            super.paintComponent(g);
            g.setColor(Color.yellow);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        if (getChessboardPoint(location).getRow()<=1&&gridType.equals(CellType.traps)|gridType.equals(CellType.dens)){
            g.setColor(Color.RED);
            g.fillOval(32, getHeight()-15, getWidth()-65, getHeight()-65);

        }
        if (getChessboardPoint(location).getRow()>=7&&gridType.equals(CellType.traps)|gridType.equals(CellType.dens)){
            g.setColor(Color.BLUE);
            g.fillOval(32, 10, getWidth()-65, getHeight()-65);
        }
    }
    protected ChessboardPoint getChessboardPoint(Point point) {
        System.out.println("[" + point.y / size + ", " + point.x / size + "] Clicked");
        return new ChessboardPoint(point.y / size, point.x / size);
    }
    protected void processMouseEvent(MouseEvent e) {

        super.processMouseEvent(e);
        if (e.getID()==MouseEvent.MOUSE_ENTERED){
           setSelected(true);
           repaint();
        }
        if (e.getID()==MouseEvent.MOUSE_EXITED){
            setSelected(false);
            repaint();
        }
        if (e.getID() == MouseEvent.MOUSE_PRESSED&&!Tool.isAi) {

            JComponent clickedComponent = (JComponent) getComponentAt(e.getX(), e.getY());
            if (clickedComponent.getComponentCount() == 0&& !(clickedComponent instanceof ChessComponent)) {
                        System.out.println("sss");
                        System.out.println(getChessboardPoint(e.getPoint()));
                gameController.onPlayerClickCell(getChessboardPoint(location), (CellComponent) clickedComponent);

            } else {
                      System.out.println("No");
                gameController.onPlayerClickChessPiece(getChessboardPoint(location), (ChessComponent) clickedComponent);

            }




        }
    }

}
