import java.io.File;

public class FileNotFoundException extends Exception {

    public FileNotFoundException(File file) {
        super(String.valueOf(file));
    }
}
