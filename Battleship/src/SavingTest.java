import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class SavingTest {
	Saving test = new Saving();
	Player humanPlayer = new Player();
	Player computerPlayer = new Player();
	Player p1 = new Player();
	Player p2 = new Player();
	Ships s = new Ships("A1","A5");
	Ships m = new Ships("B5","B9");
	ArrayList<String> hits = new ArrayList<>();
	ArrayList<String> miss = new ArrayList<>();
	
	@Test
		public void savingTestforHumanPlayer() throws IOException {
		p1.name = "kajal";
		p1.shipsArr.add(s);
		hits.add("A2");
		hits.add("B5");
		miss.add("C2");
		miss.add("F6");
		test.saveGame(p1,70000, hits, miss);
		}
	
	@Test
	public void savingTestforComputerPlayer() throws IOException {
		p2.name = "sim";
		p2.shipsArr.add(m);
		hits.add("C3");
		hits.add("D8");
		miss.add("F4");
		miss.add("G3");
		test.saveGame(p2,8000, hits, miss);
		}
	
	@Test
	public void savingTestforPlayer1() throws IOException {
		p1.name = "joy";
		p1.shipsArr.add(s);
		hits.add("F1");
		hits.add("K5");
		miss.add("H2");
		miss.add("C6");
		test.saveGame(p1,9000, hits, miss);
		}
	
	@Test
	public void savingTestforPlayer2() throws IOException {
		p1.name = "fun";
		p1.shipsArr.add(m);
		hits.add("C2");
		hits.add("E7");
		miss.add("G3");
		miss.add("K9");
		test.saveGame(p1,58000, hits, miss);
		}
	
	@Test
	public void savingTestforhumanPlayer() throws IOException {
		p1.name = "fun";
		p1.shipsArr.add(m);
		hits.add("F9");
		hits.add("E7");
		miss.add("G3");
		miss.add("K4");
		test.saveGame(p2,4000, hits, miss);
		}
}
