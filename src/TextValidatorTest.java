import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TextValidatorTest {

    private TextValidator tv = new TextValidator();

    @Test
    void test_1() {
        Assertions.assertTrue(tv.input(
                "()").isGood);
        Assertions.assertTrue(tv.input(
                "").isGood);
        Assertions.assertTrue(tv.input(
                "[(({}))]").isGood);

        Assertions.assertTrue(tv.input(
                "[(({}{}))]").isGood);
        Assertions.assertFalse(tv.input(
                "[(({}{})]").isGood);

        Assertions.assertFalse(tv.input(
                "[(({}abc{})]").isGood);
        Assertions.assertTrue(tv.input(
                "[(({}abc{}))]").isGood);

        Assertions.assertTrue(tv.input(
                "[(({}abc{a}b))c]").isGood);
        Assertions.assertFalse(tv.input(
                "[(({}abc{a}b)c]").isGood);
    }

    @Test
    void test_2() {
        Assertions.assertFalse(tv.input(
                ")").isGood);
        Assertions.assertFalse(tv.input(
                "(").isGood);
        Assertions.assertFalse(tv.input(
                "{{").isGood);
        Assertions.assertFalse(tv.input(
                "{)").isGood);
        Assertions.assertFalse(tv.input(
                "abc)").isGood);
    }
}