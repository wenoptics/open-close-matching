import java.util.HashMap;
import java.util.Stack;

public class TextValidator {

    public class ValidationResult {
        public boolean isGood = false;

        public ValidationResult(boolean isGood) {
            this.isGood = isGood;
        }
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

    private ValidationResult finishWithResult(boolean result) {
        System.out.println("  - result: `" + result + "`");
        return new ValidationResult(result);
    }

    public ValidationResult run() {

        System.out.println("\n---------");
        System.out.println("\n  - text input:");
        String _str = inputString.replaceAll("\n", "\n\t");
        System.out.println("\n\t```\n\t"+_str+"\n\t```\n");

        int cursor = 0;

        while (cursor < inputCharSeq.length) {

            if (equalAt(cursor, "//")) {
                cursor += 2;

                // skip to the line end
                int c = skipTo(cursor, LineBreaks);
                if (c < 0) {
                    // no line break found
                    break;
                }
                // ignore all the contents in between
                cursor += c;
                continue;
            }
            if (equalAt(cursor, "/*")) {
                cursor += 2;

                // skip to the first closing */ */
                int c = skipTo(cursor, new String[]{"*/"});
                if (c < 0) {
                    // no closing */ found
                    // todo: should we consider this as GOOD ?
                    return finishWithResult(true);
                }
                // ignore all the contents in between
                cursor += c;
                continue;
            }
            if (equalAt(cursor, "*/")) {
                // if a */ closing without a /* opening, simply fail it
                return finishWithResult(false);
            }

            // so, we got a char which is not //, /* nor */
            //      validate parenthesis matching using a stack
            if ( ! checkPair( String.valueOf(inputCharSeq[cursor]) )) {
                return finishWithResult(false);
            }

            cursor++;
        }

        // Make sure stack is empty at this point
        return finishWithResult(
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
