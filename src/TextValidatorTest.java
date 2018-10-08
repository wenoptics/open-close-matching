import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TextValidatorTest {

    private TextValidator tv = new TextValidator();

    @Test
    void input() {
        Assertions.assertTrue(tv.input(
                "()").isGood);

        Assertions.assertTrue(tv.input(
                "").isGood);

        Assertions.assertTrue(tv.input(
                "[(({}))]").isGood);

        Assertions.assertTrue(tv.input(
                "[(({}{})]").isGood);

        Assertions.assertTrue(tv.input(
                "[(({}abc{})]").isGood);
        Assertions.assertTrue(tv.input(
                "[(({}abc{a}b)c]").isGood);
    }
}