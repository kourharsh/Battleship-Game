import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;
import java.time.*;
import java.util.UUID;

/**
 * Class to create and initalize players of the game.
 * @author harshkour
 * @since 2019-07-06
 * @version 1.0.1
 */
public class Player {

    enum playerType {
        HUMAN,
        COMPUTER,
    }

    Random ran = new Random();

    public ArrayList<Ships> shipsArr;
    ArrayList<String> inputs;
    ArrayList<String> inputsfirst;
    int hitscount;
    int misscount;
    String playerID;
    ArrayList<String> computerships;
    String name;
	int playerPort;

    public playerType type;
    public boolean gamePlayType , playWithHuman;
    public ArrayList<String> salvaArr;

    public Player() { // Constructor to create Player object
        shipsArr = new ArrayList<>();
        inputsfirst = new ArrayList<>();
        inputs = createInputs();
        computerships = createInputs();
        hitscount = 0;
        misscount = 0;
		playerID =  UUID.randomUUID().toString();
    }


    /**
     * Function to create the ship for the player
     * @param start- starting node of the ship.
     * @param end - ending node of the ship.
     * @return return the object of Ship class.
     */
    public Ships setupShip(String start, String end) { 
        Ships shipFormed = new Ships(start, end);
        return shipFormed;
    }
    
    /**
     * Add ships to the array of player
     * @param s is the Ship added to the object.
     */
    public void updateShipsArr(Ships s) { 
        shipsArr.add(s);
    }

    /**
     * Select random location for the computer'absolutePath random ship placement.
     * @return random string from the grid like "A1" or "B10".
     */
    public String randomhitcomp() { 
        int n = ran.nextInt(inputs.size());
        return inputs.get(n);
    }
    
    /**
     * AI -- Check nearby ship location for computer'absolutePath hit.
     * @param s is the node which is already a hit.
     * @param human player object for human class.
     * @return a string of all the neighbours of the input node.
     * @author harshkour
     */
    public String checkneighbors(String s, Player human) { // 
    	ArrayList<String> neighbor = new ArrayList<String>();
    	String f = "";
    	String s1 = s.substring(0, 1);
    	String s2 = s.substring(1);
    	//string left
    	int index1 = Constants.mapInConstants.get(s1);
    	int index2 = Integer.parseInt(s2);
    	if(index1== 0 && index2 ==1) { //TOPLEFT
    		neighbor.add("D");
    		neighbor.add("R");
    	}else if(index1== 10 && index2 ==1) { //TOPRIGHT
    		neighbor.add("D");
    		neighbor.add("L");
    	}else if(index1 == 1 && index2 ==10) { //BOTTOMLEFT
    		neighbor.add("U");
    		neighbor.add("R");
    	}else if(index1 == 10 && index2 == 10) { //BOTTOMRIGHT
    		neighbor.add("U");
    		neighbor.add("L");
    	}else if(index1 == 0 ) { //leftcol
    		neighbor.add("U");
    		neighbor.add("D");
    		neighbor.add("R");
    	}else if(index1 == 10) { //rightcol
    		neighbor.add("U");
    		neighbor.add("D");
    		neighbor.add("L");
    	}else if(index2 == 1) { //toprow
    		neighbor.add("R");
    		neighbor.add("D");
    		neighbor.add("L");
    	}else if(index2 == 10) { //bottomrow
    		neighbor.add("U");
    		neighbor.add("R");
    		neighbor.add("L");
    	}else {
    		neighbor.add("U");
    		neighbor.add("R");
    		neighbor.add("L");
    		neighbor.add("D");
    	}
    	Iterator i = neighbor.iterator(); 
    	System.out.println("Neighbors with ships are:");
    	while(i.hasNext()) {
    		String s3 = (String)i.next();
    		if(s3.equals("U")) {
    			int in = index2-1;
    			String t = s1 + Integer.toString(in);
    			System.out.println(t);
    			boolean flag = Ships.checkifship(t, human);
    			if(flag && inputs.contains(t)) {
    				f = f.concat(t + " ");
    			}	
    		}else if(s3.equals("D")){
    			int in = index2+1;
    			String t = s1 + Integer.toString(in);
    			System.out.print(t);
    			boolean flag = Ships.checkifship(t, human);
    			if(flag && inputs.contains(t)) {
    				f = f.concat(t + " ");
    			}	
    		}else if(s3.equals("L")) {
    			Character st = Constants.alphabets.charAt(index1-1);
    			String t = Character.toString(st) + index2;
    			System.out.print(t);
    			boolean flag = Ships.checkifship(t, human);
    			if(flag && inputs.contains(t)) {
    				f = f.concat(t + " ");
    			}	
    		}else if(s3.equals("R")) {
    			Character st = Constants.alphabets.charAt(index1+1);
    			String t = Character.toString(st) + index2;
    			System.out.print(t);
    			boolean flag = Ships.checkifship(t, human);
    			if(flag && inputs.contains(t)) {
    				f = f.concat(t + " ");
    			}
    		}//R
    	}//while
    	return f;
    }
    
    /**
     * Generate Priority list of random computer hits.
     * @param human - Human player'absolutePath object.
     * @param salvacount -  current salva window length
     * @param salvaWindow - salva window length 
     * @return the next random hit by the computer
     * @author harshkour
     */
    public String randomhitcompai(Player human, int salvacount, int salvaWindow) { 
    	int upperbound = 0;
    	if(salvaWindow == 5 || salvaWindow == 4) {
    		upperbound = 2;
    	}else if(salvaWindow == 3 || salvaWindow == 2) {
    		upperbound = 1;
    	}
    	System.out.println("Inputsfirst is: " + inputsfirst);
    	System.out.println("Inputs is: " + inputs);
    	String s = "";
    	if(inputsfirst.size() == 0 || salvacount > salvaWindow - upperbound) {
    		int n = ran.nextInt(inputs.size()); 
    		s = inputs.get(n);
    		inputs.remove(s);
        }else {
        	int n = ran.nextInt(inputsfirst.size()); 
    		s = inputsfirst.get(n);
    		inputsfirst.remove(s);
        }
    	System.out.println("Randomcomphit is" + s);
        boolean flag = Ships.checkifship(s, human);
        if(flag) {
        	String f = checkneighbors(s, human).trim();
        	String[] farr = f.split(" ");
        	System.out.print("Neighbors with ships are: ");
        	for(int j=0; j<farr.length; j++) {
        		System.out.print(farr[j] + " ");
        		if(farr[j].equals("")) {
        			System.out.print("");
        		}else {
        		inputsfirst.add(farr[j].trim());
        		inputs.remove(farr[j]);}
        	}//for
        	System.out.println("Inputsfirst is in: " + inputsfirst);
        	System.out.println("Inputs is in: " + inputs);
        	return s;
        }else {
        	System.out.println("Inputsfirst is: " + inputsfirst);
        	System.out.println("Inputs is: " + inputs);
        	return s;
        }     
    }
    

    /**
     * Check the length of the ship and the valid neighbours.
     * @param s- random string generated to be the part of the ship.
     * @param length- length of the ship.
     * @return "T" if the string absolutePath is valid for ship placement else return "F".
     * @author harshkour
     */
    public String randomshipblocks(String s, int length) { 
        ArrayList<String> temp = new ArrayList<String>();
        String start = "";
        String end = "";
        String flagf = "T";
       // String[] sarr = absolutePath.split("");
        String sarr0 = s.substring(0,1);
        String sarr1 = s.substring(1);
        int row = Integer.parseInt(sarr1);
        Character alph = sarr0.charAt(0);
        System.out.print("sarr0 " + sarr0 );
        int col = Constants.mapInConstants.get(sarr0);
        int j = ran.nextInt(10);
        String block = "";
        if (j >= 5) { //same row
            for (int l = 0; l < length; l++) {
                int col1 = col + l;
                if (col1 > 10) {
                    flagf = "F";
                    break;
                } else {
                    Character k = Constants.alphabets.charAt(col1);
                    block = Character.toString(k) + row;
                    boolean flag = computerships.contains(block);
                    if (!flag) {
                        flagf = "F";
                        break;
                    } else {
                        temp.add(block);
                    }
                }//else
            }//for
            start = s;
            end = block;
        } else { //same col
            for (int l = 0; l < length; l++) {
                int row1 = row + l;
                block = sarr0 + row1;
                boolean flag = computerships.contains(block);
                if (!flag) {
                    flagf = "F";
                    break;
                } else {
                    temp.add(block);
                }
            }
            start = s;
            end = block;
        }
        if (flagf.equals("T")) {
        	System.out.println("\n\nShip is " + temp);
            computerships.removeAll(temp);
            ArrayList<String> Boundary = new ArrayList();
            if(j>=5) { // when ship is placed horizontally	
            	Boundary = clearBoundary(temp,start,end, false);
            	System.out.println("Boundaries are:" +  Boundary);
            	computerships.removeAll(Boundary);	
            }else { //when ship is placed vertically
            	Boundary = clearBoundary(temp,start,end, true);
            	System.out.println("Boundaries are:" + Boundary );
            	computerships.removeAll(Boundary);
            }//else
        }
        String f = flagf.concat(" " + start + " " + end);
        return f;
    }
    
    public boolean shipAvailable(ArrayList<String> ship) {
    	Iterator<String> it = ship.iterator();
    	boolean flag = false;
    	while(it.hasNext()) {
    		if (computerships.contains(it.next())){
    			flag = true;
    		}else {
    			flag = false;
    			break;
    		}
    	}//while
    	return flag;
    }
    
    public ArrayList<String> clearBoundary(ArrayList<String> temp, String start, String end, boolean isvertical) {
    	ArrayList<String> Boundary = new ArrayList();
        String start0 = start.substring(0,1);
        String start1 = start.substring(1);
        String end0 = end.substring(0,1);
    	String end1 = end.substring(1);
    	Iterator<String> it = temp.iterator();
    	int colend = Constants.mapInConstants.get(end0);
    	int colstart = Constants.mapInConstants.get(start0);
        if(!isvertical) { // when ship is placed horizontally	
        	
        	if(colend != 10) { //remove ship end+1 block
        		String remover = Constants.indexToAlpha.get(Integer.toString((colend+2)))+end1;
        		System.out.print("\nremovedr :" + remover);
        		Boundary.add(remover);
        		if(Integer.parseInt(end1)!=10) {
        			String removerd = Constants.indexToAlpha.get(Integer.toString((colend+2)))+ (Integer.parseInt(end1)+1) ;
        			System.out.print("\nremovedrd :" + removerd);
        			Boundary.add(removerd);
        			}
        		if(Integer.parseInt(end1)!=1) {
        			String removeru = Constants.indexToAlpha.get(Integer.toString((colend+2)))+ (Integer.parseInt(end1)-1) ;
        			System.out.print("\nremovedru :" + removeru);
        			Boundary.add(removeru);
        			}
        	}
        	
        	if(colstart!=0) {//remove ship start-1 block
        		String removel = Constants.indexToAlpha.get(Integer.toString(colstart)) + start1;
        		System.out.print("\nremovedl :" + removel);
        		Boundary.add(removel);
        		if(Integer.parseInt(start1)!=10) {
        			String removeld = Constants.indexToAlpha.get(Integer.toString(colstart)) + (Integer.parseInt(start1)+1);
        			System.out.print("\nremovedld :" + removeld);
        			Boundary.add(removeld);
        			}
        		if(Integer.parseInt(start1)!=1) {
        			String removelu = Constants.indexToAlpha.get(Integer.toString(colstart)) + (Integer.parseInt(start1)-1);
        			System.out.print("\nremovedlu :" + removelu);
        			Boundary.add(removelu);
        			}
        	}       	
        	while(it.hasNext()) { //remove ship'absolutePath top and bottom row
        		String blockship = it.next();
        		String blockship0 = blockship.substring(0,1);
        		String blockship1 = blockship.substring(1);
        		if(Integer.parseInt(blockship1)!=1) {
        			String removet = blockship0 + (Integer.parseInt(blockship1)-1);
        			System.out.print("\nremovedt :" + removet);
        			Boundary.add(removet);
        			}
        		if(Integer.parseInt(blockship1)!=10) {
        			String removed = blockship0 + (Integer.parseInt(blockship1)+1);
        			System.out.print("\nremovedd :" + removed);
        			Boundary.add(removed);
        			}
        	} 	
        }else { //when ship is placed vertically
        	int endb = Integer.parseInt(end1);
        	int startb = Integer.parseInt(start1);
        	if(endb!= 10) {
        		String removed =  end0 + (endb+1);
        		System.out.print("\nremovedd :" + removed);
        		Boundary.add(removed);
        		if(colend!=0) {
        			String removedl =  Constants.indexToAlpha.get(Integer.toString(colend)) + (endb+1);
        			System.out.print("\nremovedl :" + removedl);
        			Boundary.add(removedl);
        			}
        		if(colend!=10) {
        		String removedr =  Constants.indexToAlpha.get(Integer.toString(colend+2)) + (endb+1);
        		System.out.print("\nremovedr :" + removedr);
        		Boundary.add(removedr);
        		}
        		
        	}
        	if(startb != 1) {
        		//int startt = startb-1;
        		String removet =  start0 + (startb-1);
        		System.out.print("\nremovedt :" + removet);
        		Boundary.add(removet);
        		if(colstart!=0) {
        			String removetl =  Constants.indexToAlpha.get(Integer.toString(colstart)) + (startb-1);
        			System.out.print("\nremovetl :" + removetl);
        			Boundary.add(removetl);
        			}
        		if(colstart!=10) {
        			String removetr =  Constants.indexToAlpha.get(Integer.toString(colstart+2)) + (startb-1);
        			System.out.print("\nremovetr :" + removetr);
        			Boundary.add(removetr);
        			}
        	}
        	while(it.hasNext()) {
        		String blockship = it.next();
        		String blockship0 = blockship.substring(0,1);
        		String blockship1 = blockship.substring(1);
        		int colblockship = Constants.mapInConstants.get(blockship0);
        		if(colblockship!=0) {
        			String removel = Constants.indexToAlpha.get(Integer.toString((colblockship))) + blockship1;
        			System.out.print("\nremovedl :" + removel);
        			Boundary.add(removel);
        			}
        		if(colblockship!=10) {
        			String remover = Constants.indexToAlpha.get(Integer.toString((colblockship+2))) + blockship1;
        			System.out.print("\nremovedd :" + remover);
        			Boundary.add(remover);
        			}
        	}//while
        }//else
        return Boundary;
    }
    
    /**
     * Check overlapping of ships for player
     * @param newShip -  check if the new ship created is a valid ship or not.
     * @return true if it a valid ship else return false.
     */
    public boolean checkOverlap(ArrayList<String> newShip) { 
        for (Ships s : shipsArr) {
            for (String i : s.coordinates) {
                for (String g : newShip) {
                    if (i.equals(g)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * Function to generate random block from the play area grid.
     * @return the random block generated in the form of String.
     */
    public String randomblock() { 
        int n = ran.nextInt(10) ;
        System.out.println("n is :" + n);
        Character alpha = Constants.alphabets.charAt(ran.nextInt(Constants.alphabets.length()));
        String s = Character.toString(alpha) + n;
        return s;
    }
    
    /**
     * Create Random ships for the computer for length 2/3/3/4/5.
     */
    public void computerRandomShip() { //
        int[] len = {2, 3, 3, 4, 5};
        int length = 0;
        for (int i = 0; i < len.length; i++) {
            length = len[i];
            System.out.println("length is :" + length);
            boolean flag = true;
            while (flag) {
                String s = randomblock();
                System.out.println("random block is :" + s);
                String[] arr = randomshipblocks(s, length).split(" ");
                if (arr[0].equals("T")) {
                    Ships shipcomp = setupShip(arr[1], arr[2]);
                    shipsArr.add(shipcomp);
                    flag = false;
                    break;
                }//if
            }//while
        }//for
    }
    
    /**
     * Create whole set of input locations for player.
     * @return the arrayList with all the available inputs for the human player.
     */
    public ArrayList<String> createInputs() { // 
        ArrayList<String> temp = new ArrayList<String>();
        String[] col = Constants.alphabets.split("");
        int[] rows = new int[Constants.row];
        for (int i = 1; i <= rows.length; i++) {
            rows[i - 1] = i;
        }
        for (int i = 0; i < col.length; i++) {//11
            for (int j = 0; j < rows.length; j++) {//10
                temp.add(col[i] + rows[j]);
            }
        }
        return temp;
    }//createInputs
    
    /**
     * Function to initiate Salva Variation for the human player.
     */
    public void initiateSalva(){
            salvaArr = new ArrayList<>();
            gamePlayType = true;
    }
    
    /**
     * Update human player input drop down
     * @param s The input which is already used by the player
     * @param drop - Inputs of the player.
     */
    public void updateDropdown(String s, ArrayList<String> drop) { 
        drop.remove(s);
    }


    public void createConnection(int port) throws IOException {
		Udp.startServer(port);
	}

/*
* public static void main(String args[]) throws InvalidName, AdapterInactive, ServantNotActive, WrongPolicy, org.omg.CosNaming.NamingContextPackage.InvalidName, NotFound, CannotProceed, SecurityException, IOException {
		Udp.startServer();

		}*/


}
