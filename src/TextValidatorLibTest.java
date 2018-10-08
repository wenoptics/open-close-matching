import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextValidatorLibTest {

    @Test
    void skipTo() {
        Assertions.assertEquals(0, new TextValidator(
                "").skipTo(0, new String[]{""}));

        Assertions.assertEquals(2, new TextValidator(
                "qwertyui").skipTo(1, new String[]{"we"}));

        Assertions.assertEquals(4, new TextValidator(
                "qwertyui").skipTo(2, new String[]{"ty"}));

        Assertions.assertEquals(0, new TextValidator(
                "qwertyui").skipTo(2, new String[]{""}));

        Assertions.assertEquals(8, new TextValidator(
                "qwertyui").skipTo(0, new String[]{"qwertyui"}));

        Assertions.assertEquals(5, new TextValidator(
                "qwer//tyui\nabc").skipTo(6, new String[]{"\n", "\r"}));

        Assertions.assertEquals(-1, new TextValidator(
                "qwertyui").skipTo(2, new String[]{"qwertyui"}));
    }

    @Test
    void equalAt() {
        Assertions.assertEquals(true, new TextValidator(
                "").equalAt(0, ""));

        Assertions.assertEquals(true, new TextValidator(
                "a").equalAt(0, "a"));

        Assertions.assertEquals(true, new TextValidator(
                "ab").equalAt(0, "a"));

        Assertions.assertEquals(false, new TextValidator(
                "ab").equalAt(1, "a"));

        Assertions.assertEquals(true, new TextValidator(
                "ab").equalAt(1, "b"));

        Assertions.assertEquals(false, new TextValidator(
                "ab").equalAt(2, "b"));

        Assertions.assertEquals(false, new TextValidator(
                "qwertyui").equalAt(0, "ui"));

        Assertions.assertEquals(true, new TextValidator(
                "qwertyui").equalAt(6, "ui"));

        Assertions.assertEquals(false, new TextValidator(
                "qwertyui").equalAt(10, "ui"));
    }
}