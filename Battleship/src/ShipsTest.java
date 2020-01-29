import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;

import org.junit.Test;

public class ShipsTest {

	Player p1 = new Player();
    Ships s = new Ships("A1", "A5");
    char[] arr = {'a', 'b', 'c'};
    ArrayList<String> hits = new ArrayList<>();
	@Test
	public void checkForHit() {
        p1.shipsArr.add(s);

        assertEquals(true, Ships.checkhit("A2", p1));
    }
 
	//if it is false
    @Test
    public void checkForHit2() {
        p1.shipsArr.add(s);

        assertEquals(false, Ships.checkhit("A6", p1));
    }

    @Test
    public void checkForHit3() {
        p1.shipsArr.add(s);
        assertNotEquals(true, Ships.checkhit("K4", p1));
    }



    //function for computer to check if ship is positioned or not
    // if it is true
    @Test
    public void checkForshipPosition() {
        p1.shipsArr.add(s);
        for (Ships s : p1.shipsArr) {
        }
        assertEquals(true, Ships.checkifship("A1", p1));
    }

    // if it is false
    @Test
    public void checkForshipPosition2() {
        p1.shipsArr.add(s);
        for (Ships s : p1.shipsArr) {
        }
        assertEquals(false, Ships.checkifship("A6", p1));
    }

    @Test
    public void checkForshipPosition3() {
        p1.shipsArr.add(s);
        for (Ships s : p1.shipsArr) {
        }
        assertNotEquals(true, Ships.checkifship("B4", p1));
    }
    
    @Test
    public void checkForSalvaHits()
    {
    	hits.add("A4");
    	hits.add("F8");
    	assertEquals("!F8,",Ships.checkHitSalva(hits, p1));
    }
    
    //if there are zero hits
    @Test
    public void checkForSalvaHits1()
    {
    	assertEquals("!",Ships.checkHitSalva(hits, p1));
    }
    
    @Test
    public void checkForSalvaHits2()
    {
    	hits.add("G4");
    	hits.add("K7");
    	hits.add("D5");
    	hits.add("E8");
    	assertEquals("!E8,",Ships.checkHitSalva(hits, p1));
    }
}
