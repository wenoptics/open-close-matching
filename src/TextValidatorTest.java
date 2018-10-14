import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TextValidatorTest {

    private static final String NEWLINE = "\n";

    @Test
    void test_1() {
        Assertions.assertTrue(new TextValidator(
                "()").run().isGood);
        Assertions.assertTrue(new TextValidator(
                "").run().isGood);
        Assertions.assertTrue(new TextValidator(
                "[(({}))]").run().isGood);

        Assertions.assertTrue(new TextValidator(
                "[(({}{}))]").run().isGood);
        Assertions.assertFalse(new TextValidator(
                "[(({}{})]").run().isGood);

        Assertions.assertFalse(new TextValidator(
                "[(({}abc{})]").run().isGood);
        Assertions.assertTrue(new TextValidator(
                "[(({}abc{}))]").run().isGood);

        Assertions.assertTrue(new TextValidator(
                "[(({}abc{a}b))c]").run().isGood);
        Assertions.assertFalse(new TextValidator(
                "[(({}abc{a}b)c]").run().isGood);
    }

    @Test
    void test_2() {
        Assertions.assertFalse(new TextValidator(
                ")").run().isGood);
        Assertions.assertFalse(new TextValidator(
                "(").run().isGood);
        Assertions.assertFalse(new TextValidator(
                "{{").run().isGood);
        Assertions.assertFalse(new TextValidator(
                "{)").run().isGood);
        Assertions.assertFalse(new TextValidator(
                "abc)").run().isGood);
    }

    @Test
    void test_3() {
        Assertions.assertTrue(new TextValidator(
                "[(({}ab\n\nc{a}b)\n)c]").run().isGood);
        Assertions.assertFalse(new TextValidator(
                "\n[(({}abc{a}b\n)c]\n").run().isGood);

        Assertions.assertTrue(new TextValidator(
                "\n\n").run().isGood);
        Assertions.assertFalse(new TextValidator(
                "\n)").run().isGood);
        Assertions.assertFalse(new TextValidator(
                "\n(").run().isGood);
    }

    @Test
    void test_4() {
        Assertions.assertTrue(
                new TextValidator("" +
                        "{" +               NEWLINE +
                        "   void hi() {}" + NEWLINE +
                        "}" +               NEWLINE +
                        "").run().isGood);

        Assertions.assertTrue(
                new TextValidator("" +
                        "{ // abc" +           NEWLINE +
                        "   void hello() {}" + NEWLINE +
                        "}" +                  NEWLINE +
                        "").run().isGood);

        Assertions.assertTrue(
                new TextValidator("" +
                        "{ // (()){{}}[][]" +  NEWLINE +
                        "   void hello() {}" + NEWLINE +
                        "} // ()()(){{}}" +    NEWLINE +
                        "").run().isGood);

        Assertions.assertTrue(
                new TextValidator("" +
                        "{ // (" +             NEWLINE +
                        "   void hello() {}" + NEWLINE +
                        "} // (" +             NEWLINE +
                        "").run().isGood);

        Assertions.assertTrue(
                new TextValidator("" +
                        "{ // (]{]" +          NEWLINE +
                        "   void hello() {}" + NEWLINE +
                        "} // (}}" +           NEWLINE +
                        "").run().isGood);

        Assertions.assertFalse(
                new TextValidator("" +
                        "{ // (]{]").run().isGood);

        Assertions.assertTrue(
                new TextValidator("" +
                        "// { // (]{]").run().isGood);
    }

    @Test
    void test_5() {


        Assertions.assertEquals(true, new TextValidator(""+
                "void hello() {"      + NEWLINE +
                "    nice code! /**/" + NEWLINE +
                "}"                   + NEWLINE
        ).run().isGood);

        Assertions.assertEquals(true, new TextValidator(""+
                "void hello() {"             + NEWLINE +
                "    nice code! /*comment*/" + NEWLINE +
                "}"
        ).run().isGood);

        Assertions.assertEquals(true, new TextValidator(""+
                "void hello() {"                +NEWLINE +
                "    nice code! /*com(((ment*/" +NEWLINE +
                "}"
        ).run().isGood);

        Assertions.assertEquals(true, new TextValidator(""+
                "void hello() { /*com((({]ment*/" +NEWLINE +
                "    nice code! /*com((({]ment*/" +NEWLINE +
                "} /*com((({]ment*/"
        ).run().isGood);

        Assertions.assertEquals(true, new TextValidator(""+
                "/*com((({]ment*/").run().isGood);

        Assertions.assertEquals(true, new TextValidator(""+
                "/**/").run().isGood);

        Assertions.assertEquals(true, new TextValidator(""+
                "/*//*/").run().isGood);

        Assertions.assertEquals(true, new TextValidator(""+
                "/*//////////////*/").run().isGood);

        Assertions.assertEquals(true, new TextValidator(""+
                "/*cot*/").run().isGood);

        Assertions.assertEquals(true, new TextValidator(""+
                "/*com((({]*/").run().isGood);

        Assertions.assertEquals(true, new TextValidator(""+
                "/*((({]ment*/").run().isGood);

    }

    @Test
    void test_6() {
        Assertions.assertEquals(false, new TextValidator(""+
                "void hello(/*) { com((({]ment*/" +NEWLINE +
                "    nice code! /*com((({]ment*/" +NEWLINE +
                "} /*com((({]ment*/"
        ).run().isGood);

        Assertions.assertEquals(false, new TextValidator(""+
                "void hello(/*) { com((({]ment"   +NEWLINE +
                "    nice code! /*com((({]ment*/" +NEWLINE +
                "} /*com((({]ment*/"
        ).run().isGood);

        Assertions.assertEquals(false, new TextValidator(""+
                "void hello() { /*com((({]ment"   +NEWLINE +
                "    nice code! /*com((({]ment*/" +NEWLINE +
                "/*} com((({]ment*/"
        ).run().isGood);

        Assertions.assertEquals(false, new TextValidator(""+
                "*/"
        ).run().isGood);

        Assertions.assertEquals(false, new TextValidator(""+
                "void hello() { com((({]ment"   +NEWLINE +
                "    nice code! com((({]ment*/" +NEWLINE +
                "/*} com((({]ment*/"
        ).run().isGood);
    }

    @Test
    void test_7() {
        Assertions.assertEquals(true, new TextValidator(
                " abc '()'abc  "
        ).run().isGood);

        Assertions.assertEquals(false, new TextValidator(
                " abc '() abc  "
        ).run().isGood);

        Assertions.assertEquals(false, new TextValidator(
                " abc'(')abc  "
        ).run().isGood);

        Assertions.assertEquals(false, new TextValidator(
                " abc'('abc  "
        ).run().isGood);

        Assertions.assertEquals(true, new TextValidator(
                " abc'a' '' '' ' ' ''''  "
        ).run().isGood);
    }
}