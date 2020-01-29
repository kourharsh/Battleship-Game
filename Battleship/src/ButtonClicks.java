import javafx.scene.control.Button;

/**
 * Class to find coordinates of the clicked Button
 * @author harshkour
 * @since 2019-07-06
 * @version 1.0.1
 */
public class ButtonClicks extends Button {
    int coordX;
    int coordY;
    /**
     * Function to set x and y coordinates of the button.
     * @param coordX x-coordinate of the grid.
     * @param coordY y-coordinate of the grid.
     */
    public ButtonClicks(int coordX, int coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
    }

    /**
     * Function to get the x coordinate.
     * @return the x-coordinate of the grid.
     */
    public int getCoordX() {
        return coordX;
    }
    
    /**
     * Function to get the y coordinate.
     * @return the y-coordinate of the grid.
     */
    public int getCoordY() {
        return coordY;
    }
}


