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
                        "{ // abc" +
                        "   void hello() {}" +
                        "}" +
                        "").run().isGood);

        Assertions.assertTrue(
                new TextValidator("" +
                        "{ // (()){{}}[][]" +
                        "   void hello() {}" +
                        "} // ()()(){{}}" +
                        "").run().isGood);

        Assertions.assertTrue(
                new TextValidator("" +
                        "{ // (" +
                        "   void hello() {}" +
                        "} // (" +
                        "").run().isGood);

        Assertions.assertTrue(
                new TextValidator("" +
                        "{ // (]{]" +
                        "   void hello() {}" +
                        "} // (}}" +
                        "").run().isGood);
    }
}