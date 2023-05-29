package view;

import controller.GameController;
import model.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class ShowArea extends CellComponent {
         private ImageIcon  imageIcon = new ImageIcon(getClass().getResource("/Image/Select2.png" ));
         private GameController gameController;
         private Point location;
    public ShowArea(CellType gridType, Point location, int size,GameController gameController) {
       super(gridType,location,size);
       this.gameController=gameController;
       this.location=location;
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(imageIcon.getImage(),0, 0, getWidth(), getHeight(), this);
    }
    protected void processMouseEvent(MouseEvent e) {
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
