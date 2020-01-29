import java.io.*;
import java.io.FileNotFoundException;
import java.util.Optional;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * Initial controller to start the game and choose game play options.
 * @author mohit
 * @since 2019-07-06
 * @version 1.0.1
 */
public class initiateController extends Application {

    Player humanPlayer;
    Player computer;
    Udp u1 = new Udp();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException, FileNotFoundException {
        primaryStage.setTitle("Window to Choose Players");
        FileInputStream input = new FileInputStream("battleship.jpg");
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        imageView.setTranslateX(-150); 
        imageView.setTranslateY(10);
        imageView.setFitHeight(540);
        imageView.setFitWidth(720);
        imageView.setPreserveRatio(true);



        
        Button btn2 = Constants.createButton(344,450,250,70, "Play with Another Player");
        Button btn1 = Constants.createButton(600,350,250,70, "Play with Computer");

        btn1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                humanPlayer = new Player();
                computer = new Player();
                humanPlayer.type = Player.playerType.HUMAN;
                computer.name = "COMPUTER";
                computer.type = Player.playerType.COMPUTER;
                humanPlayer.playWithHuman = false;


                String filePath = Constants.absolutePath +"/gameData";
                File f = new File(filePath); //Change Path

                // EXCEPTION: For checking if the File Exist

                PlayerFile obj2 = new PlayerFile();

                try {
                    obj2.findByFile(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (f.exists()) {
                    if(f.isDirectory()){
                        if(f.list().length>0) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Select");
                            alert.setHeaderText(Constants.LOAD_ALERT);
                            ButtonType yes = new ButtonType("Yes");
                            ButtonType no = new ButtonType("No");

                            // Remove default ButtonTypes
                            alert.getButtonTypes().clear();
                            alert.getButtonTypes().addAll(yes, no);
                            Optional<ButtonType> option = alert.showAndWait();

                            if (option.get() == yes) {
                                loadGame(primaryStage, filePath);
                            } else if (option.get() == no) {
                                startNewGame(primaryStage);
                            }
                        }
                        else{
                            startNewGame(primaryStage);
                        }
                    }
                }
                else{
                    startNewGame(primaryStage);
                }

            }
        });


        btn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                humanPlayer = new Player();
                humanPlayer.type = Player.playerType.HUMAN;


                TextInputDialog dialog = new TextInputDialog("Enter your name");

                dialog.setHeaderText("Enter your name:");
                dialog.setContentText("Name:");

                Optional<String> result = dialog.showAndWait();

                result.ifPresent(name -> {

                    if (name.trim().length() > 0 && !name.equals("Enter your name")) {
                        humanPlayer.name = name.trim();


                        // EXCEPTION: For checking the name of the player

                        PlayerNameCheck obj = new PlayerNameCheck();

                        try {
                            obj.findByName(name);
                        } catch (NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        humanPlayer.playWithHuman = true;
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Select");
                        alert.setHeaderText(Constants.SALVA_ALERT);
                        ButtonType yes = new ButtonType("Yes");
                        ButtonType no = new ButtonType("No");

                        // Remove default ButtonTypes
                        alert.getButtonTypes().clear();
                        alert.getButtonTypes().addAll(yes, no);
                        Optional<ButtonType> option = alert.showAndWait();

                        if (option.get() == yes) {
                            humanPlayer.initiateSalva();
                        } else if (option.get() == no) {
                            humanPlayer.gamePlayType = false;
                        }

                        humanPlayer.playerPort = 6666; //change port
                        try {
                        	u1.startServer(5555);
                        	
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        shipSetupController fx2 = new shipSetupController();
                        fx2.humanPlayer = humanPlayer;
                        fx2.u1 = u1;

                        try {
                            fx2.start(primaryStage);
                        }
                        catch (Exception e){
                            System.out.println(e);
                        }


                    } else {
                        Constants.showAlert(Constants.NAME_ALERT);
                    }
                });

            }
        });


        FlowPane flow = new FlowPane();
        btn1.setLayoutX(350);
        btn1.setLayoutY(350);

        flow.setHgap(5);
        flow.getChildren().addAll(btn1, btn2, imageView);
        flow.setStyle("-fx-background-color: Grey");
        primaryStage.setScene(new Scene(flow, 1000, 800));
        primaryStage.setMaximized(true);
        primaryStage.show();

    }

        public void loadGame(Stage primaryStage, String path) {

            //player ships

            // The name of the file to open.

            // This will reference one line at a time
            String line = null;


            String humanData[] = {};
            String compData[] = {};
            
            System.out.println(Constants.filePath);
            System.out.println(Constants.f_human);
            System.out.println(Constants.f_Computer);
            {

                try {
                    // FileReader reads text files in the default encoding.
                    FileReader fileReaderHuman =
                            new FileReader(Constants.f_human);

                    // Always wrap FileReader in BufferedReader.
                    BufferedReader bufferedReader =
                            new BufferedReader(fileReaderHuman);

                    while ((line = bufferedReader.readLine()) != null) {
                        humanData = line.split(Constants.separator);
                    }
                    FileReader fileReaderComputer = new FileReader(Constants.f_Computer);
                    BufferedReader bufferedReaderComputer =
                            new BufferedReader(fileReaderComputer);
                    while ((line = bufferedReaderComputer.readLine()) != null) {
                        compData = line.split(Constants.separator);
                    }

                    // Always close files.
                    bufferedReader.close();

                    for (String d : humanData) {
                        System.out.println(d);
                    }

                    humanPlayer.name = humanData[0];
                    computer.name = compData[0];
                    String[] ships = humanData[1].split("-");
                    for (String d : ships) {
                        if(!d.equals("")) {
                            String ships_Val = d.substring(1, d.length() - 1);
                            String endCoor = ships_Val.substring(ships_Val.length() - 2);
                            if(ships_Val.length()>2){
                                endCoor= ships_Val.substring(ships_Val.length() - 3).trim();
                            }
                            Ships loadShip = new Ships(ships_Val.substring(0, 2), endCoor);
                            humanPlayer.shipsArr.add(loadShip);
                        }
                    }


                    String h_inputs = humanData[2].substring(1,humanData[2].length()-1);

                    humanPlayer.inputs.clear();
                    for (String d : h_inputs.split(",")) {
                        humanPlayer.inputs.add(d.trim());
                    }



                    String[] cships = compData[1].split("-");
                    for (String d : cships) {
                        if(!d.equals("")) {
                            String ships_Val = d.substring(1, d.length() - 1);
                                                       String endCoor = ships_Val.substring(ships_Val.length() - 2);
                            if(ships_Val.length()>2){
                                endCoor= ships_Val.substring(ships_Val.length() - 3).trim();
                            }
                            Ships loadShip = new Ships(ships_Val.substring(0, 2), endCoor);
                            computer.shipsArr.add(loadShip);
                        }
                    }

                    String c_inputs = compData[2].substring(1,compData[2].length()-1);
                    computer.inputs.clear();
                    for (String d : c_inputs.split(",")) {
                        computer.inputs.add(d.trim());
                    }


                    Arena fx2 = new Arena();
                    fx2.humanPlayer = humanPlayer;
                    fx2.computer = computer;


                    String hits = humanData[3].substring(1,humanData[3].length()-1);

                    for (String d : hits.split(",")) {
                        if(d.length()>0) {
                            fx2.hitsHuman.add(d.trim());
                        }
                    }

                    String miss = humanData[4].substring(1,humanData[4].length()-1);
                    for (String d : miss.split(",")) {
                        if(d.length()>0) {
                            fx2.missHuman.add(d.trim());
                        }
                    }


                    String Chits = compData[3].substring(1,compData[3].length()-1);

                    for (String d : Chits.split(",")) {
                        if(d.length()>0) {
                            fx2.hitsComputer.add(d.trim());
                        }
                    }

                    String Cmiss = compData[4].substring(1,compData[4].length()-1);
                    for (String d : Cmiss.split(",")) {
                        if(d.length()>0) {
                            fx2.missComputer.add(d.trim());
                        }
                    }

                    fx2.elapsedtime = Long.parseLong(humanData[5])/1000;
                    try {
                        fx2.start(primaryStage);
                    } catch (Exception e) {
                        System.out.println(e);
                    }




                } catch (FileNotFoundException ex) {
                    System.out.println(
                            "Unable to open file '");
                } catch (IOException ex) {
                    System.out.println(
                            "Error reading file ");
                    // Or we could just do this:
                    // ex.printStackTrace();
                }



            }
        }

        public void startNewGame(Stage primaryStage){
        computer.computerRandomShip();

        TextInputDialog dialog = new TextInputDialog("Enter your name");

        dialog.setHeaderText("Enter your name:");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {

            if (name.trim().length() > 0 && !name.equals("Enter your name")) {
                humanPlayer.name = name.trim();

                // EXCEPTION: For checking the name of the players

                PlayerNameCheck obj = new PlayerNameCheck();

                try {
                    obj.findByName(name);
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Select");
                alert.setHeaderText(Constants.SALVA_ALERT);
                ButtonType yes = new ButtonType("Yes");
                ButtonType no = new ButtonType("No");

                // Remove default ButtonTypes
                alert.getButtonTypes().clear();
                alert.getButtonTypes().addAll(yes, no);
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == yes) {
                    humanPlayer.initiateSalva();
                } else if (option.get() == no) {
                    humanPlayer.gamePlayType = false;
                }


                shipSetupController fx2 = new shipSetupController();
                fx2.humanPlayer = humanPlayer;
                fx2.computer = computer;

                try {
                    fx2.start(primaryStage);
                }
                catch (Exception e){
                    System.out.println(e);
                }


            } else {
                Constants.showAlert(Constants.NAME_ALERT);
            }
        });
    }

    public class PlayerNameCheck {
        public void findByName(String name) throws NameNotFoundException {


            if (!isStringOnlyAlphabet(name)) {
                throw new NameNotFoundException("Please Enter a Valid Name");
            }
        }

        public boolean isStringOnlyAlphabet(String str)
        {
            return ((str != null)
                    && (!str.equals(""))
                    && (str.chars().allMatch(Character::isLetter)));
        }
    }
    
    public static class PlayerFile{

        public void findByFile(File file) throws FileNotFoundException {

            if (!file.exists()) {
                throw new FileNotFoundException("File does not exist");
            }
        }

    }
}
