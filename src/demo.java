public class demo {

    public static void main(String[] argv) {
        TextValidator.ValidationResult result;

        result = new TextValidator("[({}{})]").run();
        System.out.println("result:" + result.isGood);

        result = new TextValidator("[{{})]").run();
        System.out.println("result:" + result.isGood);

        result = new TextValidator("" +
                "void hello() { /*com((({]ment*/" +"\n"+
                "    nice code! /*com((({]ment*/" +"\n"+
                "} /*com((({]ment*/"              +"\n"
        ).run();
        System.out.println("result:" + result.isGood);

        result = new TextValidator(""+
                "void hello() { /*com((({]ment"   +"\n"+
                "    nice code! /*com((({]ment*/" +"\n"+
                "/*} comment*/"
        ).run();
        System.out.println("result:" + result.isGood);
    }
}
