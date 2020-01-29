import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Saving {

    Player humanPlayer;
    Saving(){
        humanPlayer = new Player();
    }

    public static void saveGame(Player humanPlayer, long elapsedtime, ArrayList<String> hits, ArrayList<String> miss) throws IOException {



        System.out.println("Player name:"+humanPlayer.name);

        for(Ships s : humanPlayer.shipsArr){
            System.out.println("Player ships:"+s.coordinates);
        }
        System.out.println("Player input:"+humanPlayer.inputs);
        System.out.println("Player hits:"+hits);
        System.out.println("Player miss:"+miss);
        System.out.println("Player time:"+elapsedtime);
        File file;

        String sPath = Constants.absolutePath;
        if(humanPlayer.type == Player.playerType.HUMAN){
            new File(sPath+"/gameData").mkdirs();
            file = new File(sPath+"/gameData/human.txt");
        }
        else {
            new File(sPath+"/gameData").mkdirs();
            file = new File(sPath+"/gameData/computer.txt");
        }

        // EXCEPTION: For checking if the File Exist

        PlayerFile obj = new PlayerFile();

        try {
            obj.findByFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }




        if (file.exists())
        {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Write Content
        FileWriter writer = new FileWriter(file);
        writer.append(humanPlayer.name);
        writer.write(Constants.separator);

        for(Ships s : humanPlayer.shipsArr){

            writer.write("-");
            writer.append(String.valueOf(s.coordinates));
        }

        writer.write(Constants.separator);

        writer.append(String.valueOf(humanPlayer.inputs));
        writer.write(Constants.separator);

        writer.append(String.valueOf(hits));
        writer.write(Constants.separator);
        writer.append(String.valueOf(miss));
        writer.write(Constants.separator);
        writer.append(String.valueOf(elapsedtime));

        writer.close();

    }

    public static class PlayerFile{

        public void findByFile(File file) throws FileNotFoundException {

            if (!file.exists()) {
                throw new FileNotFoundException("File does not exist");
            }
        }

    }
}
