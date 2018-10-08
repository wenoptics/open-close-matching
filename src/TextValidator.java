import java.util.HashMap;
import java.util.Stack;

public class TextValidator {

    public class ValidationResult {
        public boolean isGood = false;

        public ValidationResult(boolean isGood) {
            this.isGood = isGood;
        }
    }

    private class Pairing {
        public char open;
        public char close;
    }

    private static HashMap<String, String> pairingRules;
    private static String[] LineBreaks = {"\n", "\r"};

    private Stack<String> stack;

    private String inputString;
    private char[] inputCharSeq;

    private boolean checkPair(String s) {
        if (pairingRules.containsKey(s)) {
            // this is an opening
            stack.push(pairingRules.get(s));
        } else if (pairingRules.containsValue(s)) {
            // this is a closing
            if (stack.isEmpty()) {
                return false;
            }
            String _pop = stack.pop();
            if (!_pop.equals(s)) {
                return false;
            }
        }
        return true;
    }

    protected boolean equalAt(int start, String test) {
        if (start < 0) {
            throw new IllegalArgumentException("start < 0");
        }
        if (start + test.length() > inputCharSeq.length) {
            return false;
        }
        for (int i = 0; i < test.length(); i++) {
            if (inputCharSeq[start + i] != test.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    protected int skipTo(int start, String[] expectings) {
        int skipped = 0;
        for (skipped=0; skipped + start <= inputCharSeq.length; skipped++) {
            for (String exp: expectings) {
                if (equalAt(start + skipped, exp)) {
                    return skipped+exp.length();
                }
            }
        }
        return -1;
    }

    public ValidationResult run() {

        int cursor = 0;

        while (cursor < inputCharSeq.length) {

            String s = String.valueOf(inputCharSeq[cursor]);

            /*// a state machine to process different scenario
            switch (lastSpecial) {
                case "//":
                    // skip to the line end
                    int c = skipTo(cursor, LineBreaks);
                    if (c < 0) {
                        // no line break found
                        return new ValidationResult(true);
                    }
                    cursor += c;
                    continue;
            }*/

            if (equalAt(cursor, "//")) {
                cursor += 2;

                // skip to the line end
                int c = skipTo(cursor, LineBreaks);
                if (c < 0) {
                    // no line break found
                    break;
                }
                cursor += c;
                continue;
            }

            if (!checkPair(s)) {
                return new ValidationResult(false);
            }

            cursor++;
        }

        // Make sure stack is empty at this point
        return new ValidationResult(
                stack.isEmpty()
        );
    }

    public TextValidator(String input) {
        this.inputString = input;
        inputCharSeq = input.toCharArray();

        stack = new Stack<>();

        pairingRules = new HashMap<>();
        pairingRules.put("(", ")");
        pairingRules.put("[", "]");
        pairingRules.put("{", "}");
        pairingRules.put("<", ">");
    }
}
