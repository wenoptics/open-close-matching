import java.util.HashMap;
import java.util.Stack;

public class TextValidator {

    public static class ValidationResult {
        public boolean isGood = false;
        public int badStart = -1;
        public int badEnd = -1;
        public String message = null;

        public ValidationResult(boolean isGood) {
            this.isGood = isGood;
        }

        @Override
        public String toString() {
            if (isGood) {
                return "<ValidationResult isGood=true";
            }
            return String.format("<ValidationResult isGood=%s, badStart=%d, badEnd=%d message='%s' />",
                    isGood, badStart, badEnd, message);
        }
    }

    public class Positioned <E> {
        public int position = -1;
        public E content = null;

        public Positioned(int position, E content) {
            this.position = position;
            this.content = content;
        }
        public Positioned(E content) {
            this.content = content;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass() != this.getClass()) {
                return false;
            }
            return ((Positioned) obj).content
                    .equals(this.content);
        }
    }


    private static HashMap<String, String> pairingRules;
    private static String[] LineBreaks = {"\n", "\r"};

    private Stack<Positioned> stack;

    private String inputString;
    private char[] inputCharSeq;

    /**
     *  Check pair and return valid or not
     * @param p
     * @param s
     * @return
     */
    private Positioned<Boolean> checkPair(int p, String s) {
        if (pairingRules.containsKey(s)) {
            String pairClosing = pairingRules.get(s);
            if (pairClosing.equals(s)) {
                // quote opening closing are the same
                Positioned check = new Positioned<>(pairClosing);
                if (stack.contains(check)) {
                    // this is a closing
                    var _pop = stack.pop();
                    if ( ! _pop.content.equals(s)) {
                        return new Positioned<>(_pop.position, false);
                    } else {
                        return new Positioned<>(true);
                    }
                }
            }
            // this is an opening
            stack.push(new Positioned<>(p, pairClosing));
        } else if (pairingRules.containsValue(s)) {
            // this is a closing
            if (stack.isEmpty()) {
                return new Positioned<>(false);
            }
            var _pop = stack.pop();
            if ( ! _pop.content.equals(s)) {
                return new Positioned<>(_pop.position, false);
            }
        }
        return new Positioned<>(true);
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
        for (int skipped=0; skipped + start <= inputCharSeq.length; skipped++) {
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
                    ValidationResult vr = new ValidationResult(false);
                    vr.badStart = cursor-2;
                    vr.badEnd = cursor-1;
                    vr.message = "Block comment opening without closing at " + vr.badStart;
                    return vr;
                }
                // ignore all the contents in between
                cursor += c;
                continue;
            }
            if (equalAt(cursor, "*/")) {
                // if a */ closing without a /* opening, simply fail it
                ValidationResult vr = new ValidationResult(false);
                vr.badStart = cursor;
                vr.badEnd = cursor + 1;
                vr.message = "Block comment closing without opening at " + cursor;
                return vr;
            }

            // so, we got a char which is not //, /* nor */
            //      validate parenthesis matching using a stack
            Positioned<Boolean> cp = checkPair(cursor, String.valueOf(inputCharSeq[cursor]));
            if ( false == cp.content ) {
                ValidationResult vr = new ValidationResult(false);
                if (cp.position != -1) {
                    vr.message = String.format("Closing at %d:  %c   not match opening at %d:  %c  ",
                            cursor, inputCharSeq[cursor],
                            cp.position, inputCharSeq[cp.position]);
                } else {
                    vr.message = String.format("No paired for closing at %d:  %c  ", cursor, inputCharSeq[cursor]);
                }
                vr.badStart = cp.position;
                vr.badEnd = cursor;
                return vr;
            }

            cursor++;
        }

        // Make sure stack is empty at this point
        if ( ! stack.isEmpty()) {
            ValidationResult vr = new ValidationResult(false);
            int _p = stack.pop().position;
            vr.message = String.format("No closing for opening at %d:  %c  ", _p, inputCharSeq[_p]);
            vr.badStart = _p;
            return vr;
        }

        return new ValidationResult(true);
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
        pairingRules.put("'", "'");
        pairingRules.put("\"", "\"");
    }
}
