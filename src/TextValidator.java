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

    static HashMap<String, String> pairingRules;

    Stack<String> stack;

    void init() {
        stack.clear();

    }

    public ValidationResult input(String str) {
        init();

        for (char c : str.toCharArray()) {
            String s = String.valueOf(c);
            if (pairingRules.containsKey(s)) {
                // this is an opening
                stack.push(pairingRules.get(s));
            } else if (pairingRules.containsValue(s)) {
                // this is a closing
                if (stack.isEmpty()) {
                    return new ValidationResult(false);
                }
                String _pop = stack.pop();
                if ( ! _pop.equals(s)) {
                    return new ValidationResult(false);
                }
            }
        }

        // Make sure stack is empty at this point
        return new ValidationResult(
                stack.isEmpty()
        );
    }

    public TextValidator() {
        pairingRules = new HashMap<>();
        pairingRules.put("(", ")");
        pairingRules.put("[", "]");
        pairingRules.put("{", "}");
        pairingRules.put("<", ">");

        stack = new Stack<>();
    }
}
