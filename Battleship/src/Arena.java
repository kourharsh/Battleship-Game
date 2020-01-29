
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import javafx.scene.text.Text;

/**
 * Arena.java deals with the game arena, it records the actions on UI and performs the computation.
 * @author iknoor
 * @since 2019-07-06
 * @version 1.0.1
 */
public class Arena extends Application {

	static Player humanPlayer;
    static Player computer;
    static long startTime;
    static long finishTime;
    static String selectedAddress;
    static GridPane playerRefGrid;
    public static  Button hitBtn;
    public static ComboBox inputComboBox;  
    static int call = 1;
    static int index = 0;
    static GridPane salvaGrid;
    static GridPane compRefGrid;
    static GridPane playerGrid;
    static GridPane compGrid;
    static int salvaWindow = 5;
    static long elapsedtime = 0;
    static int oppShipsLeft = 5;
    static ArrayList<ArrayList<String>> oppShips = new ArrayList<ArrayList<String>>();
    static boolean winner = false;
    static private int seconds = 0;
    static private int minutes = 0;
    static private int hours = 0;
    static boolean timerstop = false;
    static Arena a1;
    static Stage stage1;
    static Udp u1;
    
    private final Text text = new Text((hours < 10 ? "0" : "")+Integer.toString(hours)+":"+ (minutes < 10 ? "0" : "")+Integer.toString(minutes)+":"+(seconds < 10 ? "0" : "")+Integer.toString(seconds));
    Saving save1 = new Saving();
    ArrayList<String> hitsHuman = new ArrayList<String>();
    ArrayList<String> missHuman = new ArrayList<String>();
    ArrayList<String> hitsComputer = new ArrayList<String>();
    ArrayList<String> missComputer = new ArrayList<String>();
    SplitPane split_pane2;

    
    
    /**
     * Function to increment the timer.
     * @author harshkour
     */
    private void incrementCount() {
    	if(!timerstop) {
    		seconds++;
    		if(seconds > 59) {
    			seconds = 0;
    			minutes = minutes + 1;
    		}
    		if(minutes > 59) {
    			minutes = 0;
    			hours = hours + 1;
    		}
    	}
        text.setText((hours < 10 ? "0" : "")+Integer.toString(hours)+":"+ (minutes < 10 ? "0" : "")+Integer.toString(minutes)+":"+(seconds < 10 ? "0" : "")+Integer.toString(seconds));
    }
    
    /**
     * Function to send the Ships
     */
	public void sendShips() {
		String sendships = "Z";
		
		for(Ships ship : humanPlayer.shipsArr) {
			String s = ",";
			Iterator<String> it = ship.coordinates.iterator();
			while(it.hasNext()) {
				s += it.next().trim()+":";
			}
			sendships = sendships+s.substring(0,s.length()-1);
		}
		System.out.println("\n\nSending Ships-----  " + sendships);
		Udp.sendMessage(humanPlayer.playerPort, sendships);
	}
    
    
    public void start(Stage stage) { 
    	stage1 = stage;
    	a1 = Arena.this;
    	u1.arena = a1;
    	u1.human = humanPlayer;

    	System.out.println("Human Ships in Arena"+u1.human.shipsArr);
        startTime = System.currentTimeMillis();
        if(humanPlayer.playWithHuman) {
        	timerstop = true;
        }
        

        System.out.println(elapsedtime);
        hours = (int)elapsedtime/3600;
        minutes = (int)(elapsedtime - hours * 3600) / 60;
        seconds = (int)(elapsedtime - hours * 3600) - minutes * 60;


        System.out.println(hitsHuman);
        System.out.println(missHuman);
        System.out.println(hitsComputer);
        System.out.println(missComputer);


        try {

            // set title for the stage
            stage.setTitle("LET'S PLAY");
            HBox hbox = new HBox(70);
            hbox.setTranslateX(200);
            hbox.setTranslateY(20);
            Label right = new Label(humanPlayer.name.toUpperCase());
            right.setFont(new Font("Arial", 30));

            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    System.out.println("button pressed");
                }
            };

            // create split pane 1

            SplitPane split_pane1 = new SplitPane();
            split_pane1.setOrientation(Orientation.VERTICAL);
            split_pane1.setPrefSize(500, 500);
            playerGrid = createGrid(Constants.row + 1, Constants.col + 1, false);
            playerRefGrid = createGrid(Constants.row + 1, Constants.col + 1, false);


            compGrid = createGrid(Constants.row + 1, Constants.col + 1, false);
            compRefGrid = createGrid(Constants.row + 1, Constants.col + 1, false);
            split_pane1.getItems().addAll(playerRefGrid, playerGrid, right);
            text.resize(150, 40);
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
            text.setTranslateX(-130);
            hbox.getChildren().add(text);
            hbox.getChildren().add(split_pane1);


            if (!humanPlayer.playWithHuman) {
                // create split pane 2
            	
            split_pane2 = new SplitPane();
            split_pane2.setPrefSize(500, 500);
            split_pane2.setOrientation(Orientation.VERTICAL);
                Label left = new Label(computer.name.toUpperCase());
                left.setFont(new Font("Arial", 30));
                left.setPrefHeight(50);
                split_pane2.getItems().addAll(compRefGrid, compGrid, left);
            }
            
            inputComboBox = new ComboBox();
            inputComboBox.setPromptText("Select Location");
            inputComboBox.setStyle("-fx-border-color: #000000 ; -fx-border-width: 3px;");
            inputComboBox.setStyle("-fx-border-color: #000000 ; -fx-background-color: #CD853F;");
            System.out.println(humanPlayer.inputs);
            inputComboBox.getItems().addAll(
                    humanPlayer.inputs
            );

            hitBtn = new Button();
            hitBtn.setText("Hit");
            hitBtn.setStyle("-fx-border-color: #000000 ; -fx-border-width: 3px;");
            hitBtn.setStyle("-fx-border-color: #000000; -fx-background-color: #CD853F");
            if (humanPlayer.gamePlayType) {
                hitBtn.setDisable(true);   
            }
            
            if(humanPlayer.playWithHuman) {
            	hitBtn.setText("Start");
            }
            
            // To check if it is a HIT / MISS by any player
            for (Ships p : humanPlayer.shipsArr) {
                ArrayList<String> got = p.coordinates;
                for (int i = 0; i < got.size(); i++) {
                    String s0 = got.get(i).substring(0, 1);
                    String s1 = got.get(i).substring(1);

                    int x = Constants.mapInConstants.get(s0);    //c
                    int y = Integer.parseInt(s1) - 1;    //r
                    Button b = (Button) getNodeFromGridPane(playerGrid, x + 1, y);
                    b.setStyle("-fx-background-color:" + p.hexColor);
                }
            }


            if (!humanPlayer.playWithHuman){
                showHideComputerShip(true, compGrid); // show hide Computer ships
        }
             

            VBox vbox = new VBox();
            vbox.getChildren().add(inputComboBox);    
            
            // long running operation runs on different thread
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    Runnable updater = new Runnable() {

                        @Override
                        public void run() {
                            incrementCount();
                        }
                    };
                    while (true) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                        }
                        Platform.runLater(updater);
                    }
                }

            });
            thread.setDaemon(true);
            thread.start();

            if(humanPlayer.playWithHuman && call == 1){
            	hitBtn.setText("Start");
            	inputComboBox.setDisable(true);
            	hitBtn.setDisable(false);}
            

            if(humanPlayer.gamePlayType) {
                salvaGrid = createGrid(1, salvaWindow, true);
                vbox.getChildren().add(salvaGrid);
                	inputComboBox.valueProperty().addListener(new ChangeListener<String>() {
                		@Override
                		public void changed(ObservableValue ov, String t, String t1) {
                			if(t1 == null && humanPlayer.salvaArr.size() < salvaWindow){
                        	
                				hitBtn.setDisable(true);
                			}
                			else{
                				hitBtn.setDisable(false);
                			}
                		}
                	});
              //  }

                if(humanPlayer.salvaArr.size()<salvaWindow){
                	System.out.println("here when salvarr < window---1");
                	if(call != 1) {
                    hitBtn.setText("OK");
                    hitBtn.setDisable(true);}

                    hitBtn.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                        	if(humanPlayer.playWithHuman){
                        		if(call == 1) {
                        			timerstop = false;
                        			hitBtn.setText("OK");
                        			inputComboBox.setDisable(false);
                        			hitBtn.setDisable(true);
                        			call=0;
                        			sendShips();                			
                        		}else if(winner) {
                        			hitBtn.setDisable(true);
                        			finishTime = System.currentTimeMillis();
                        			elapsedtime = elapsedtime*1000 + (finishTime - startTime);
                        			System.out.println("hours: " + hours);
                    				System.out.println("minutes: " + minutes);
                    				System.out.println("seconds: " + seconds);
                    				int newtime = (seconds*1000)+ (minutes*60000) + (hours*60*60000);
                    				System.out.println("newtime: " + newtime);
                        			String score  = calcScore(newtime, humanPlayer, false);
                        			System.out.println("\n\nYou lost the game!");
                                    timerstop = true;
                        			Constants.showAlert("Sorry You lost the game!"+ "\n Miss = "+ humanPlayer.misscount +
                        					"\t Hit = "+ humanPlayer.hitscount + "\nYour score is " + score );                        					                        					                        			
                        		}else { //1
                        			if(humanPlayer.salvaArr.size() < salvaWindow){
                                    	System.out.println("human play - here when salvarr < window---2");
                                          updateSalvaGRid(salvaGrid,inputComboBox.getValue().toString());
                                    } else{ //2
                                        hitBtn.setDisable(true);
                                        inputComboBox.setDisable(true);
                                        timerstop = true;	
                                    	System.out.println("human play - here when salvarr < window----else");
                                    	boolean flag3 = false;
                                    	Iterator<String> it = humanPlayer.salvaArr.iterator();
                                    	String s = "";
                            			while(it.hasNext()) {
                            				String m = it.next().trim();
                            				s += ","+m;
                            				humanPlayer.updateDropdown(m, humanPlayer.inputs);
                            				}
                            			System.out.println("send hits====" + s);
                            			u1.sendMessage(humanPlayer.playerPort,"S"+s);
                            			clearSalvaAfterHit(salvaGrid);
                            			hitBtn.setText("OK");
                            			index=0;
                            			int shipcount  = oppShipsLeft; //check if any ship of opp human player is down
                            			System.out.println("\n \n opp human player ship count is " + shipcount);
                            			if(shipcount < salvaWindow) {
                            				salvaWindow = shipcount;
                            				System.out.println("\n \n Window size updated to " + salvaWindow);                   	
                            				//update UI
                            				salvaGrid = createGrid(1, salvaWindow, true);
                            				vbox.getChildren().remove(1);
                            				vbox.getChildren().add(1,salvaGrid);
                            			}                                   	                                   	
                                    }//else2                       			       				
                        		}//else1
                        	}//playwithHuman
                        	else {
                        		if(humanPlayer.salvaArr.size() < salvaWindow){
                        			System.out.println("here when salvarr < window---2");
                        			updateSalvaGRid(salvaGrid,inputComboBox.getValue().toString());
                        		}
                        		else{
                        			hitBtn.setDisable(true);

                        			System.out.println("here when salvarr < window----else");
                        			boolean flag3 = false;
                        			Iterator<String> it = humanPlayer.salvaArr.iterator();

                            	//if(!humanPlayer.playWithHuman){                           				
                                	while(it.hasNext()) {
                                		String s = it.next();
                                		humanPlayer.updateDropdown(s, humanPlayer.inputs);
                                		boolean flag = Ships.colorButton(playerRefGrid, compGrid, s, Arena.this, computer);
                                		if(flag) {
                                			hitsHuman.add(s);
                                			humanPlayer.hitscount++;
                            				playSound();

                                		}else {
                                			missHuman.add(s);
                                			humanPlayer.misscount++;
                                		}
                                	}//while 
                                	
                                	clearSalvaAfterHit(salvaGrid);
                                    hitBtn.setText("OK");
                                    index=0;
                                    boolean flag2 = checkWinner(computer, humanPlayer);
                                    if (!flag2) {
                                    	for(int i = 0;i<salvaWindow;i++) {
                            				String s = computer.randomhitcompai(humanPlayer, i+1 , salvaWindow);
                                			System.out.println("computerhit is == " + s);
                                			boolean flag1 = Ships.colorButton(playerGrid, compRefGrid, s, Arena.this, humanPlayer);
                                            if (flag1) {
                                                hitsComputer.add(s);
                                				playSound();

                                            } else {
                                                missComputer.add(s);
                                            }
                                			flag3 = checkWinner(humanPlayer, computer);
                                    		if(flag3) {

                                    			timerstop = true;
                                                finishTime = System.currentTimeMillis();
                                                elapsedtime = elapsedtime*1000 + (finishTime - startTime);
                                        		String score  = calcScore(elapsedtime, humanPlayer,false);
                                    			Constants.showAlert(computer.name + " won the game!!!" + "\nYour score is " + score);
                                    			break;
                                    			}
                                    		
                            			}//for
                                    }//not flag2
                                    else { //Human Player won the game!
                                		finishTime = System.currentTimeMillis();
                                        elapsedtime = elapsedtime*1000 + (finishTime - startTime);
                                		System.out.println("elspsed time is : " + elapsedtime + " finishtime is :"+ finishTime + " start time is: "+startTime);
                                		String score  = calcScore(elapsedtime, humanPlayer, false);
                                		timerstop = true;
                                		Constants.showAlert(humanPlayer.name + " won the game!!!" + "\nYour score is " + score);
                                	}
                                    
                                    if (flag2 || flag3) { 		
                                		Alert alert = new Alert(AlertType.CONFIRMATION);
                                		alert.setTitle("Select");
                                		alert.setHeaderText("Do you wish to continue?");
                                		ButtonType yes = new ButtonType("Yes");
                                		ButtonType no = new ButtonType("No");

                                    // Remove default ButtonTypes
                                		alert.getButtonTypes().clear();
                                		alert.getButtonTypes().addAll(yes, no);
                                		Optional<ButtonType> option = alert.showAndWait();

                                		if (option.get() == yes) {
                                			initiateController fx2 = new initiateController();
                                			try {
                                				fx2.start(stage);
                                			} catch (FileNotFoundException e) {
                                				e.printStackTrace();
                                			}
                                		} else if (option.get() == no) {
                                			Platform.exit();
                                		}
                                	}// if any player has won the game
                                    
                                    //No player won the game after the hits
                                    int shipcount  = computer.shipsArr.size(); //check if any ship of computer is down
                                    System.out.println("\n \n computer ship count is " + shipcount);
                                    if(shipcount < salvaWindow) {
                                    	salvaWindow = shipcount;
                                    	System.out.println("\n \n Window size updated to " + salvaWindow);                   	
                                    	//update UI
                                        salvaGrid = createGrid(1, salvaWindow, true);
                                        vbox.getChildren().remove(1);
                                        vbox.getChildren().add(1,salvaGrid);
                                    } 
                    			}                   
                            }//else
                        }
                    });
                }
            }
            else {            	
            	hitBtn.setOnAction(new EventHandler<ActionEvent>() {
                	@Override
                	public void handle(ActionEvent event) {
                		if(call == 1 && humanPlayer.playWithHuman) {
                			System.out.println("In call == 1");
                			timerstop = false;
                			inputComboBox.setDisable(false);
                			call=0;
                    		hitBtn.setText("Hit");
                    		hitBtn.setDisable(true);
                			sendShips();
                			System.out.println("\n\ncall is-------" + call);
                    		
                		}else if(winner && humanPlayer.playWithHuman) {
                			System.out.println("In call winner");
                			hitBtn.setDisable(true);
                			finishTime = System.currentTimeMillis();
                			elapsedtime = elapsedtime*1000 + (finishTime - startTime);
                			System.out.println("hours: " + hours);
            				System.out.println("minutes: " + minutes);
            				System.out.println("seconds: " + seconds);
            				int newtime = (seconds*1000)+ (minutes*60000) + (hours*60*60000);
            				System.out.println("newtime: " + newtime);
                			String score  = calcScore(newtime, humanPlayer, false);
                			System.out.println("\n\nYou lost the game!");
                            timerstop = true;
                			//Constants.showAlert("Sorry You lost the game! \n"+ "Your score is " + score);
                			Constants.showAlert("Sorry You lost the game!"+ "\n Miss = "+ humanPlayer.misscount +
                					"\t Hit = "+ humanPlayer.hitscount + "\nYour score is " + score );
                		}
                		else {
                			System.out.println("In call else");
                			boolean flag3 = false;
                			try {
                				selectedAddress = (String) inputComboBox.getValue();
                			}
                			catch(Exception e){
                				System.out.print("");
                			} 
                			if(selectedAddress!= null){
                				timerstop = true;
                				System.out.println("Human Player hit is == " + selectedAddress);
                				humanPlayer.updateDropdown(selectedAddress, humanPlayer.inputs);
                				inputComboBox.getItems().remove(selectedAddress);
                				inputComboBox.setPromptText("Select Location");
                                if(!humanPlayer.playWithHuman) {        
                                    boolean flag = Ships.colorButton(playerRefGrid, compGrid, selectedAddress, Arena.this, computer);
                                    String message = "Wohoo!! Its a hit!!";
                                    if (!flag) {
                                    	message = "Bohoo!! You missed it!!";
                                    	humanPlayer.misscount++;
                                    	missHuman.add(selectedAddress);

                                    }else {
                                    	humanPlayer.hitscount++;
                                    	playSound();
                                    	hitsHuman.add(selectedAddress);
                					}
                                    Constants.showAlert(message);                                    
                                	boolean flag2 = checkWinner(computer, humanPlayer);
                                	if (!flag2) {
                                			String s = computer.randomhitcompai(humanPlayer, 0, 0);
                                			System.out.println("computerhit is == " + s);
                                			boolean flag1 = Ships.colorButton(playerGrid, compRefGrid, s, Arena.this, humanPlayer);
                                			String messageComp;
                                			if (flag1) {
                                				messageComp = "It was a hit by Computer at " + s;
                                				playSound();
                                				hitsComputer.add(s);
                                			} else {
                                				messageComp = "Wohoo!! Computer missed the shot and hit you at " + s;
                                				missComputer.add(s);
                                			}
                                			Constants.showAlert(messageComp);                               		
                                		flag3 = checkWinner(humanPlayer, computer);
                                		if(flag3) {
//                                            save1.humanPlayer = humanPlayer;
                                			timerstop = true;
                                			finishTime = System.currentTimeMillis();
                                            elapsedtime = elapsedtime*1000 + (finishTime - startTime);
                                    		String score  = calcScore(elapsedtime, humanPlayer, false);
                                			Constants.showAlert(computer.name + " won the game!!!" + "\nYour score is " + score);}
                                	}else {
                                		finishTime = System.currentTimeMillis();
                                        elapsedtime = elapsedtime*1000 + (finishTime - startTime);
                                		String score  = calcScore(elapsedtime, humanPlayer, false);
//                                        save1.humanPlayer = humanPlayer;
                                		timerstop = true;
                                		Constants.showAlert(humanPlayer.name + " won the game!!!" + "\nYour score is " + score);
                                	}                                                                 	
                                   	if (flag2 || flag3) { 		
                                		Alert alert = new Alert(AlertType.CONFIRMATION);
                                		alert.setTitle("Select");
                                		alert.setHeaderText(Constants.CONTINUE_ALERT);
                                		ButtonType yes = new ButtonType("Yes");
                                		ButtonType no = new ButtonType("No");

                                    // Remove default ButtonTypes
                                		alert.getButtonTypes().clear();
                                		alert.getButtonTypes().addAll(yes, no);
                                		Optional<ButtonType> option = alert.showAndWait();

                                		if (option.get() == yes) {
                                			initiateController fx2 = new initiateController();
                                			try {
                                				fx2.start(stage);
                                			} catch (FileNotFoundException e) {
                                				e.printStackTrace();
                                			}
                                		} else if (option.get() == no) {
                                			Platform.exit();
                                		}
                                	}
                                }else {
                                	/*if(call == 1) {
                                		sendShips();
                                		call =0;
                                		}*/
                                	hitBtn.setDisable(true); 
                                	inputComboBox.setDisable(true);
                                    u1.sendMessage(humanPlayer.playerPort, "H," + selectedAddress);
                                    
                                }
                			 }else {
                                 	Constants.showAlert(Constants.HIT_ALERT);
                             }
                		}//call= 1
                			                
                    }
                });
            }                                 
                
            vbox.setSpacing(10);
            hbox.getChildren().add(vbox);
            hbox.getChildren().add(hitBtn);
            vbox.setPrefWidth(250);



            hbox.setSpacing(10);
            if (!humanPlayer.playWithHuman) {
                hbox.getChildren().add(split_pane2);
            }

            // Creating scene
            Scene scene = new Scene(new Group(hbox), 1000, 800);
            scene.setFill(Color.GRAY);
            stage.setScene(scene);
            stage.setWidth(1000);
            stage.setHeight(800);

            stage.setOnCloseRequest(e2 -> {
            	if(!humanPlayer.playWithHuman) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Select");
                alert.setHeaderText(Constants.SAVE_ALERT);
                ButtonType yes = new ButtonType("Yes");
                ButtonType no = new ButtonType("No");

                // Remove default ButtonTypes
                alert.getButtonTypes().clear();
                alert.getButtonTypes().addAll(yes, no);
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == yes) {
                //call saving
                    try {
                        System.out.println(System.currentTimeMillis() - startTime);

                        Saving.saveGame(humanPlayer, elapsedtime*1000+ (System.currentTimeMillis() - startTime), hitsHuman, missHuman);
                        Saving.saveGame(computer, 0, hitsComputer, missComputer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (option.get() == no) {
                    Platform.exit();
                }
            	}
            	else {
                    Platform.exit();
            	}
            });
            updateGridFromLoad(playerGrid,hitsComputer,true);
            updateGridFromLoad(playerGrid,missComputer,false);
            updateGridFromLoad(playerRefGrid,hitsHuman,true);
            updateGridFromLoad(playerRefGrid,missHuman,false);
            updateGridFromLoad(compGrid,hitsHuman,true);
            updateGridFromLoad(compGrid,missHuman,false);

            updateGridFromLoad(compRefGrid,hitsComputer,true);
            updateGridFromLoad(compRefGrid,missComputer,false);
            stage.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    } 
    
    /**
     * Function to perform actions based on the UDP message received
     * @param received message received from the UDP
     * @return a String reply to be sent through UDP sockets
     */
    public static String postHit(String received) 
    {
    	String check = received.substring(0, 1).trim();
		String msg = received.substring(2).trim();
		System.out.println("msg is:" + msg);
    	if(check.equals("H")) { //for Single Hit    		
    		hitBtn.setDisable(false);
    		inputComboBox.setDisable(false);
    		timerstop = false;
    		boolean flag = Ships.colorButtonHuman(playerGrid, msg.trim(), a1, humanPlayer);
    		String s = "R,";
    		System.out.println(flag);
    		if(flag) {
    				System.out.println("y-h");
    				s += "Y,"+msg.trim();	
    				playSound();
                } else {
                	System.out.println("n-h");
                	s += "N,"+msg.trim();
                }
    		System.out.println("s is ---- " + s);
    		return s;
    	}//H
    	else if(check.equals("R")) { //for Single response	
       		msg = received.substring(2,3).trim();
       		System.out.println(msg);
       		String msg1 = received.substring(4).trim();	
        	if(msg.equals("Y")) {
        			String messageComp = "It is a hit";
        			Constants.showAlert(messageComp);
        			System.out.println(messageComp);
        			Ships.colorRefHuman(playerRefGrid, msg1,  a1, humanPlayer, true) ;
        			humanPlayer.hitscount++;
        			int Oppshipcount = oppShips.size();
        			removeOppShip(msg1); 
        			System.out.println("\n\n Opposite Ships size is -- "+ oppShips.size());
    				if(oppShips.size() == 0) {
    						finishTime = System.currentTimeMillis();
    						elapsedtime = elapsedtime*1000 + (finishTime - startTime);
    						System.out.println("hours: " + hours);
    						System.out.println("minutes: " + minutes);
    						System.out.println("seconds: " + seconds);
    						int newtime = (seconds*1000)+ (minutes*60000) + (hours*60*60000);
    						System.out.println("newtime: " + newtime);
    						String score  = calcScore(newtime, humanPlayer, true);
                            timerstop = true;
                            hitBtn.setDisable(true);  
                            inputComboBox.setDisable(true);
    						u1.sendMessage(humanPlayer.playerPort, "W,"+humanPlayer.name);
    						Constants.showAlert(humanPlayer.name + " won the game!!!" + 
                            		"\n Miss = "+ humanPlayer.misscount + "\t Hit = "+ humanPlayer.hitscount +
                            		"\nYour score is " + score); 
    	        			System.out.println(messageComp);
    				}
    				else if(oppShips.size() < 5){
    						if(oppShipsLeft < Oppshipcount) {
    						Constants.showAlert(oppShipsLeft + " ship(s) left!");}
    				}
                }
                 else {	
                	String messageComp = "It is a miss";
                	Constants.showAlert(messageComp);
                	System.out.println(messageComp);
                	Ships.colorRefHuman(playerRefGrid, msg1,  a1, humanPlayer, false) ;
                	humanPlayer.misscount++;
                } 
        		String m = "P";	
        		return m;
    	}//R
    	else if(check.equals("W")) { //for Winner 
            winner = true;
            timerstop = true;
            inputComboBox.setDisable(true);
            hitBtn.setDisable(false);
          //  hitBtn.setText("Score")
            
    		//Constants.showAlert("Sorry You lost the game! \n"+ "Your score is " + score);
    		System.out.println("\n\nYou lost the game!3");
    		String m = "L,--";
    		return m;
    	}else if(check.equals("S")) { //for Salva Hits
    		hitBtn.setDisable(false);
    		inputComboBox.setDisable(false);
    		timerstop = false;
    		String[] arr = msg.split(",");
    		String s = "T";
    		for(int i = 0; i<arr.length; i++) {
    			boolean flag = Ships.colorButtonHuman(playerGrid, arr[i].trim(), a1, humanPlayer);
    			if(flag) {
    				s+=",Y-"+arr[i].trim();
    				playSound();

    			}else {
    				s+=",N-"+arr[i].trim();
    			}
    		}//for	
    		return s;
    	}else if(check.equals("T")){ //for Salva Response
    		String[] arr = msg.split(",");
    		int Oppshipcount = oppShips.size();
    		for(int i = 0; i<arr.length; i++) {
    			String resp = arr[i].trim().substring(0,1);
    			String hit = arr[i].trim().substring(2);
    			if(resp.equals("Y")) {
    				Ships.colorRefHuman(playerRefGrid, hit,  a1, humanPlayer, true) ;
    				humanPlayer.hitscount++;
        			removeOppShip(hit);
        			System.out.println("\n\n Opposite Ships size is -- "+ oppShips.size());
                }else {   	
                	Ships.colorRefHuman(playerRefGrid, hit,  a1, humanPlayer, false) ;
                	humanPlayer.misscount++;
                }
    		}
			if(oppShips.size() == 0) {
				finishTime = System.currentTimeMillis();
				elapsedtime = elapsedtime*1000 + (finishTime - startTime);
				System.out.println("hours: " + hours);
				System.out.println("minutes: " + minutes);
				System.out.println("seconds: " + seconds);
				int newtime = (seconds*1000)+ (minutes*60000) + (hours*60*60000);
				System.out.println("newtime: " + newtime);
				String score  = calcScore(newtime, humanPlayer, true);
				//score = Integer.toString(Integer.parseInt(score) + 100);
				Constants.showAlert(humanPlayer.name + " won the game!!!" + 
                		"\n Miss = "+ humanPlayer.misscount + "\t Hit = "+ humanPlayer.hitscount +
                		"\nYour score is " + score);
                timerstop = true;                            
                hitBtn.setDisable(true);
                inputComboBox.setDisable(true);   
				u1.sendMessage(humanPlayer.playerPort, "W,"+humanPlayer.name);
			}
			else if(oppShips.size() < 5){
				if(oppShipsLeft < Oppshipcount) {
				Constants.showAlert(oppShipsLeft + " ship(s) left!");}
			}		
    		String m = "P";
    		return m;
    	}
    	else if(check.equals("Z")) {
    		System.out.println("Inside z");
    		String[] arr = msg.split(",");
    		for(int i=0; i<arr.length;i++) {
    			System.out.println(arr[i]);
    			String[] arr1 = arr[i].split(":");
    			ArrayList<String> ship = new ArrayList<String>();
    			for(int j=0; j<arr1.length; j++) {
    				System.out.println(arr1[j]);
    				ship.add(arr1[j]);
    			}
    			oppShips.add(ship);
    		}
    		System.out.println("\n\nOpposite Ships are --- " + oppShips );
    		hitBtn.setDisable(false);
    		String m = "X,--";
    		return m;
    	}else if(check.equals("X")) {
    		System.out.println("\n\nShips sent!!!");	 
    		String m = "P";
    		return m;
    	}else if(check.equals("L")) {    		
    		System.out.println("\n\nWinner received!!!");
    		 timerstop = true;                            
             hitBtn.setDisable(true);
             inputComboBox.setDisable(true);
    		String m = "P";
    		return m;
    	}
    	else {
    		String m = "P";
    		return m;
    	}
    }
    
    /**
     * Remove Ship co-ordinate if it is a hit from the local copy of the ship	
     * @param s is the Ship co-ordinate
     */
    public static void removeOppShip(String s) {
    	System.out.println("\n\n1. Opposite Ships are --- " + oppShips );
    	Iterator<ArrayList<String>> it1 = oppShips.iterator();
    	boolean flag = true;
    	while(it1.hasNext() && flag) {
    		ArrayList<String> Ship = it1.next();
    		Iterator<String> it2 = Ship.iterator();
    		if(Ship.contains(s)) {
    			Ship.remove(s);
    			if(Ship.size() == 0) {
    				oppShips.remove(Ship);
    				oppShipsLeft = oppShipsLeft - 1;
    				System.out.println("oppShipsLeft ---" + oppShipsLeft);
    				flag = false;
    				break;
    			}// if- removeOppShips
    		}//if-Ship contains
    	}//while
    	System.out.println("\n\n2. Opposite Ships are --- " + oppShips );
    }
    
    
    /**
     * Function to fetch each node from the grid.
     * @param gridPane Grid for which node value needs to be returned.
     * @param col column value for the grid.
     * @param row row value on the grid.
     * @return the node of the grid.
     * @author mohit
     */
    public Node getNodeFromGridPane(GridPane gridPane, int col, int row) { 
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }


    // Main Method
    public static void main(String args[]) {
        // launch the application
        launch(args);
    }
    
    /**
     * Function to Show/Hide Computer Ships on the Play Arena
     * @param show true if ships needs to be visible on the Arena else false.
     * @param cGrid Input grid on which ships for the computer needs to be shown.
     * @author munish
     */
    public void showHideComputerShip(Boolean show , GridPane cGrid) { 
        if (show) {
            {
                for (Ships p : computer.shipsArr) {

                    ArrayList<String> got = p.coordinates;

                    for (int i = 0; i < got.size(); i++) {
                        String s0 = got.get(i).substring(0, 1);
                        String s1 = got.get(i).substring(1);
                        int x = Constants.mapInConstants.get(s0);    //c
                        int y = Integer.parseInt(s1);    //r
                        Button b = (Button) getNodeFromGridPane(cGrid, x + 1, y - 1);
                        b.setStyle("-fx-background-color:" + p.hexColor);
                    }
                }
            }
        }
    }

    /**
     * Function to Create Grid to setup the ships by the Human Player
     * @param rows no. of rows of the grid
     * @param col no. of columns for the grid
     * @param isSalva flag to update for Salva inputs
     * @return Gridpane
     * @author iknoor
     */
    public static GridPane createGrid(int rows, int col, Boolean isSalva) {
        GridPane gridPane = new GridPane();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <  col; j++) {
                if(!isSalva){
                    if (j == 0 && i != Constants.row) {
                    String buttonname = "button" + i + j;
                    Button button = new Button(Integer.toString(i + 1));
                    button.setPrefSize(40, 15);
                    button.setStyle("-fx-border-color: #000000 ; -fx-border-width: 2px;");
                    button.setStyle("-fx-border-color: #000000; -fx-background-color: #FFD700");
                    button.setDisable(true);
                    gridPane.add(button, j, i);
                } else if (i == Constants.row && j != 0) {

                    if (j == 1) {

                        Button button = new Button("A");
                        button.setDisable(true);
                        button.setPrefSize(40, 15);
                        button.setStyle("-fx-border-color: #000000 ; -fx-border-width: 2px;");
                        button.setStyle("-fx-border-color: #000000; -fx-background-color: #FFD700");
                        gridPane.add(button, j, i);
                    } else if (j == 2) {
                        Button button = new Button("B");
                        button.setDisable(true);
                        button.setPrefSize(40, 15);
                        button.setStyle("-fx-border-color: #000000 ; -fx-border-width: 2px;");
                        button.setStyle("-fx-border-color: #000000; -fx-background-color: #FFD700");
                        gridPane.add(button, j, i);
                    } else if (j == 3) {
                        Button button = new Button("C");
                        button.setDisable(true);
                        button.setPrefSize(40, 15);
                        button.setStyle("-fx-border-color: #000000 ; -fx-border-width: 2px;");
                        button.setStyle("-fx-border-color: #000000; -fx-background-color: #FFD700");
                        gridPane.add(button, j, i);
                    } else if (j == 4) {
                        Button button = new Button("D");
                        button.setDisable(true);
                        button.setPrefSize(40, 15);
                        button.setStyle("-fx-border-color: #000000 ; -fx-border-width: 2px;");
                        button.setStyle("-fx-border-color: #000000; -fx-background-color: #FFD700");
                        gridPane.add(button, j, i);
                    } else if (j == 5) {
                        Button button = new Button("E");
                        button.setDisable(true);
                        button.setPrefSize(40, 15);
                        button.setStyle("-fx-border-color: #000000 ; -fx-border-width: 2px;");
                        button.setStyle("-fx-border-color: #000000; -fx-background-color: #FFD700");
                        gridPane.add(button, j, i);
                    } else if (j == 6) {
                        Button button = new Button("F");
                        button.setDisable(true);
                        button.setPrefSize(40, 15);
                        button.setStyle("-fx-border-color: #000000 ; -fx-border-width: 2px;");
                        button.setStyle("-fx-border-color: #000000; -fx-background-color: #FFD700");
                        gridPane.add(button, j, i);
                    } else if (j == 7) {
                        Button button = new Button("G");
                        button.setDisable(true);
                        button.setPrefSize(40, 15);
                        button.setStyle("-fx-border-color: #000000 ; -fx-border-width: 2px;");
                        button.setStyle("-fx-border-color: #000000; -fx-background-color: #FFD700");
                        gridPane.add(button, j, i);
                    } else if (j == 8) {
                        Button button = new Button("H");
                        button.setDisable(true);
                        button.setPrefSize(40, 15);
                        button.setStyle("-fx-border-color: #000000 ; -fx-border-width: 2px;");
                        button.setStyle("-fx-border-color: #000000; -fx-background-color: #FFD700");
                        gridPane.add(button, j, i);
                    } else if (j == 9) {
                        Button button = new Button("I");
                        button.setDisable(true);
                        button.setPrefSize(40, 15);
                        button.setStyle("-fx-border-color: #000000 ; -fx-border-width: 2px;");
                        button.setStyle("-fx-border-color: #000000; -fx-background-color: #FFD700");
                        gridPane.add(button, j, i);
                    } else if (j == 10) {
                        Button button = new Button("J");
                        button.setDisable(true);
                        button.setPrefSize(40, 15);
                        button.setStyle("-fx-border-color: #000000 ; -fx-border-width: 2px;");
                        button.setStyle("-fx-border-color: #000000; -fx-background-color: #FFD700");
                        gridPane.add(button, j, i);

                    } else if (j == 11) {
                        Button button = new Button("K");
                        button.setDisable(true);
                        button.setPrefSize(40, 15);
                        button.setStyle("-fx-border-color: #000000 ; -fx-border-width: 2px;");
                        button.setStyle("-fx-border-color: #000000; -fx-background-color: #FFD700");
                        gridPane.add(button, j, i);
                    }

                    }else {

                        Button button = new Button("-");
                        button.setStyle("-fx-border-color: #000000 ; -fx-border-width: 2px;");
                        button.setStyle("-fx-border-color: #000000; -fx-background-color: #FFFFFF");
                        button.setDisable(true);
                        button.setPrefSize(40, 15);

                        gridPane.add(button, j, i);
                    }
                    }
                    else {
                        Button button = new Button("-");
                        button.setStyle("-fx-border-color: #000000 ; -fx-border-width: 2px;");
                        button.setStyle("-fx-border-color: #000000; -fx-background-color: #FFFFFF");
                        button.setDisable(true);
                        button.setPrefSize(100, 15);
                        button.setOnAction(new EventHandler<ActionEvent>() {
                           @Override
                           public void handle(ActionEvent event) {
                               Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                               alert.setTitle("Select");
                               alert.setHeaderText("Do you wish to remove this in Salva entry?");
                               ButtonType yes = new ButtonType("Yes");
                               ButtonType no = new ButtonType("No");

                               // Remove default ButtonTypes
                               alert.getButtonTypes().clear();
                               alert.getButtonTypes().addAll(yes, no);
                               Optional<ButtonType> option = alert.showAndWait();

                               if (option.get() == yes) {
                                   System.out.println(button.getText());

                                   index = humanPlayer.salvaArr.indexOf(button.getText());
                                   humanPlayer.salvaArr.remove(button.getText());

                                   button.setDisable(true);
                                   humanPlayer.inputs.add(button.getText());
                                   inputComboBox.getItems().add(button.getText());
                                   Collections.sort(inputComboBox.getItems());
                                   button.setText("-");
                                   inputComboBox.setPromptText("Select Location");
                                   hitBtn.setText("OK");

                               }
                           }
                           });

                        gridPane.add(button, j, i);
                }
            }//inner for
        }//outer for
        if(rows>1) {
            gridPane.setPrefSize(500, 500);
        }
        else{
            gridPane.setPrefSize(col*100, 20);
        }
        return gridPane;
    }


    /**
     * Funcion to update the Salva grid to add values to the grid
     * @author munish
     * @param g Grid to be updated
     * @param inp the value selected for the hit
     */
    public void updateSalvaGRid(GridPane g, String inp){
    	System.out.println("yooo===="+inp);
        if (humanPlayer.salvaArr.size() < salvaWindow && inp != null) {

            humanPlayer.salvaArr.add(index,inp);
            System.out.println(humanPlayer.salvaArr);


            Button b = (Button) getNodeFromGridPane(g, index, 0);
            b.setText(inp);
            b.setDisable(false);

            humanPlayer.updateDropdown(inp, humanPlayer.inputs);
            System.out.println(inp);

            inputComboBox.getItems().remove(inp);
            inputComboBox.setPromptText("Select Location");
            index ++ ;

            hitBtn.setText(humanPlayer.salvaArr.size() == salvaWindow ? "Hit" : "OK");
        }
        else {
        	hitBtn.setDisable(true);
        }
    }

    /**
     * Function to update clear the salva grid
     * @param g Grid to be cleared
     * @author munish
     */
    public void clearSalvaAfterHit(GridPane g){
        for(int i = 0 ; i < salvaWindow; i++){
            Button b = (Button) getNodeFromGridPane(g, i, 0);
            b.setText("-");
            b.setDisable(true);
        }
        humanPlayer.salvaArr.clear();
    }
    
    /**
     * Function to check the winner of the game after every hit by player/computer.
     * @param p1 Player who has been hit.
     * @param p2 Player who did hit.
     * @return true if there is a winner of the game i.e. all ships are down else return false.
     */
    public static boolean checkWinner(Player p1, Player p2) { 
        System.out.println("Ships array size is " + p1.shipsArr.size());
        System.out.println("Ships array is " + p1.shipsArr);
        if (p1.shipsArr.size() == 0) {

            Constants.f_human.delete();
            Constants.f_Computer.delete();
            return true;
        } else {
            return false;
        }
    }//checkWinner
    
    /**
     * Function to calculate score for each game play.
     * @param elapsedtime - time taken by player for the entire game play
     * @param human human player object
     * @return the calculated score in the form of a String.
     */
    public static String calcScore(long elapsedtime, Player human, boolean flag) {
		// EXCEPTION: For checking the final time of the game
		long time = elapsedtime;
		Arena.CheckGameTime obj = new Arena.CheckGameTime();

		try {
			obj.findByTime(time);
		} catch (TimeException e) {
			e.printStackTrace();
		}

    	System.out.println("elapsed time: " + elapsedtime);
    	double minutes = (double)elapsedtime/60000; 
    	System.out.println("Time taken by player is " + Double.toString(minutes)); 
    	double scorecalc = ((1/minutes)*100) + (human.hitscount * 10) - (human.misscount * 1);
    	if(flag) {
    		scorecalc += 100;
    	}
		// EXCEPTION: For checking the final score of the game
		double score =scorecalc;
		Arena.CheckGameScore obj1 = new Arena.CheckGameScore();
		try {
			obj1.findByScore(score);
		} catch (RunTimeException e) {
			e.printStackTrace();
		}

		DecimalFormat d = new DecimalFormat("#.###");
    	System.out.println("score is "+ d.format(scorecalc));
    	return d.format(scorecalc);
    }

    /**
     * Update Grid after Re-Loading of the game
     * @param grid Grid of UI that needs to be updated
     * @param data Previous game data stored
     * @param isHit true when there is a Hit  else it is false
     */
    public void updateGridFromLoad(GridPane grid, ArrayList<String> data, Boolean isHit) {
        if(!data.isEmpty()) {
            for (String d : data) {
                String s1 = d.substring(0, 1);
                String s2 = d.substring(1);
                int x = Constants.mapInConstants.get(s1);    //c
                int y = Integer.parseInt(s2);    //r
                Button bActual = (Button) getNodeFromGridPane(grid, x + 1, y - 1);
                Button bReference = (Button) getNodeFromGridPane(grid, x + 1, y - 1);
            if (isHit) {
                bActual.setStyle("-fx-background-color: Red");
                bReference.setStyle("-fx-background-color: Red");
            } else {
                bActual.setStyle("-fx-background-color: Black;");
                bReference.setStyle("-fx-background-color: Black");
            }
            }
        }
    }
    
    /**
     * Function to check the game Time
     * @author harshkour
     *
     */
	public static class CheckGameTime {
		public void findByTime(long time) throws TimeException {

			if (time <= 0) {
				throw new TimeException("Invalid Game Time");
			}
		}
	}
	
	/**
	 * Function to check Game score
	 * @author harshkour
	 *
	 */
	public static class CheckGameScore{

		public void findByScore(double score) throws RunTimeException {

			if (score < 0) {
				throw new RunTimeException("Invalid Game Score");
			}
		}

	}
	
	/**
	 * Function to play sound
	 */
	public static void playSound() {
		String bip = "hit.mp3";
		Media hit = new Media(new File(bip).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(hit);
		mediaPlayer.play();
	}


}//Arena