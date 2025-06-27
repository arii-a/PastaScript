package DFA;

import java.util.function.Predicate;

public class DFADynamic {
    public enum State {
        START,
        SAUCE, VAR_NAME_SAUCE, VAR_NAME_SAUCE_END, ASSIGN_SAUCE, SAUCE_OPEN_MARKS, SAUCE_STRING, END_STRING_SAUCE, SAUCE_ACCEPT,
        PASTA, VAR_NAME_PASTA, VAR_NAME_PASTA_END, ASSIGN_PASTA, VALUE_PASTA, UNIT_PASTA, PASTA_ACCEPT,
        BASIL, VAR_NAME_BASIL, VAR_NAME_BASIL_END, ASIGN_BASIL, COOKED_BASIL, BURNED_BASIL, BASIL_ACCEPT,
        RECIPE, FUNC_NAME, FUNC_NAME_END, FUNC_PAREN_OPEN, FUNC_ARGS, FUNC_ARGS_NAME, FUNC_ARGS_NAME_END, FUNC_PAREN_CLOSE,
        FUNC_BODY_OPEN,
        SERVE, SERVE_PAREN, SERVE_ARGS, SERVE_PAREN_CLOSE, SERVE_CLOSE,
        FUNC_ACCEPT,
        MAIN, MAIN_NAME, MAIN_NAME_END, MAIN_PAREN_OPEN, MAIN_PAREN_CLOSE,
        MAIN_BODY_OPEN, MAIN_CHEF, MAIN_CHEF_DOT, MAIN_CHEF_FUNC, MAIN_CHEF_PAREN,
        MAIN_CHEF_ARGS,
        MAIN_CHEF_PAREN_CLOSE, MAIN_CHEF_END, MAIN_ACCEPT
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
