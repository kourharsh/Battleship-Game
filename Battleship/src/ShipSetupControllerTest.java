import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Test;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;

public class ShipSetupControllerTest {
	 shipSetupController test = new shipSetupController();
	    Player p1 = new Player();
	    Player p2 = new Player();
	    Button btnok;
	    RadioButton rb;
	    GridPane gridPane;

	    //check for availability
	    @Test public void checkForAvailability()
	    {

	        assertEquals(false,test.checkAvailability(3,7,5,true));
	    }

	    //check for availability 
	    @Test public void checkForAvailability1()
	    {

	        assertEquals(true,test.checkAvailability(3,1,5,true));
	    }

	    @Test public void checkForAvailability2()
	    {

	        assertNotEquals(false,test.checkAvailability(4,5,6,true));
	    }

	    @Test public void checkForAvailability3()
	    {

	        assertNotEquals(false,test.checkAvailability(6,5,2,false));
	    }
	    

}
