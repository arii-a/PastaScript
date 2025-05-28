package DFA;

import java.util.function.Predicate;

public class DFADynamic {
    public enum State {
        START,
        SAUCE, VAR_NAME_SAUCE, VAR_NAME_SAUCE_END, ASSIGN_SAUCE, SAUCE_OPEN_MARKS, SAUCE_STRING, END_STRING_SAUCE, SAUCE_ACCEPT,
        PASTA, VAR_NAME_PASTA, VAR_NAME_PASTA_END, ASSIGN_PASTA, VALUE_PASTA, UNIT_PASTA, PASTA_ACCEPT,
        BASIL, VAR_NAME_BASIL, VAR_NAME_BASIL_END, ASIGN_BASIL, COOKED_BASIL, BURNED_BASIL, BASIL_ACCEPT,
        RECIPE, FUNC_NAME, FUNC_NAME_END, FUNC_PAREN_OPEN, FUNC_ARGS, FUNC_ARGS_NAME, FUNC_ARGS_NAME_END, FUNC_PAREN_CLOSE,
        FUNC_BODY_OPEN, FUNC_ACCEPT,
    }

    public interface Condition {
        boolean matches(String input, int index);
    }

    static class Literal implements Condition {
        private final String literal;
        public Literal(String literal) {
            this.literal = literal;
        }
        public boolean matches(String input, int index) {
            return input.startsWith(literal, index);
        }
    }

    static class CharCondition implements Condition {
        private final Predicate<Character> predicate;
        public CharCondition(Predicate<Character> predicate) {
            this.predicate = predicate;
        }
        public boolean matches(String input, int index) {
            return index < input.length() && predicate.test(input.charAt(index));
        }
    }

    static class Whitespace implements Condition {
        public boolean matches(String input, int index) {
            return index < input.length() && Character.isWhitespace(input.charAt(index));
        }
    }
}
