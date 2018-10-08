public class TextValidator {

    public class ValidationResult {
        public ValidationResult(boolean isGood) {
            this.isGood = isGood;
        }
        public boolean isGood = false;
    }


    public ValidationResult input(String str) {
        // todo
        return new ValidationResult(false);
    }
}
