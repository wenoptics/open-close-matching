import java.nio.file.Files;
import java.nio.file.Paths;

public class demo {

    public static void main(String[] argv) throws Exception {
        TextValidator.ValidationResult result;

        result = new TextValidator("[({}{})]").run();
        System.out.println("result:" + result);

        result = new TextValidator("[{{})]").run();
        System.out.println("result:" + result);

        result = new TextValidator("[({>)]").run();
        System.out.println("result:" + result);

        result = new TextValidator("[{{(})]").run();
        System.out.println("result:" + result);

        result = new TextValidator("" +
                "void hello() { /*com((({]ment*/" +"\n"+
                "    nice code! /*com((({]ment*/" +"\n"+
                "} /*com((({]ment*/"              +"\n"
        ).run();
        System.out.println("result:" + result);

        result = new TextValidator(""+
                "void hello() { /*com((({]ment"   +"\n"+
                "    nice code! /*com((({]ment*/" +"\n"+
                "/*} comment*/"
        ).run();
        System.out.println("result:" + result);

        // read from file
        String text = demo.readFileAsString(
                "C:\\Users\\wenop\\OneDrive - University of Pittsburgh\\Course\\data-structure\\HW-matching\\txt\\text_1.txt");
        result = new TextValidator(text).run();
        System.out.println("result:" + result);

    }

    public static String readFileAsString(String fileName)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}
