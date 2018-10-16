import java.nio.file.Files;
import java.nio.file.Paths;

public class Helper {
    public static String readFileAsString(String fileName)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}
