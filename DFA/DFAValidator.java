package DFA;

public class DFAValidator {
    enum State {
        START, PASTA, SAUCE, BASIL, RECIPE,
        VAR_NAME_PASTA, VAR_NAME_SAUCE, VAR_NAME_BASIL,
        ASSIGN_PASTA, ASSIGN_SAUCE, ASSIGN_BASIL,
        VALUE_PASTA, VALUE_SAUCE, VALUE_BASIL,
        UNIT_PASTA, STRING_SAUCE, END_STRING_SAUCE,
        COOKED_BASIL, BURNED_BASIL,
        ACCEPT, ERROR
    }

    enum Func_State {
        START,
        FUNC_NAME, FUNC_OPEN, FUNC_ARG, FUNC_CLOSE,
        FUNC_BODY_OPEN, FUNC_BODY_CLOSE,
    }

    public boolean accepts(String input) {
        input = input.trim();
        State state = State.START;
        Func_State func_state = null;
        int i = 0;

        while (i < input.length()) {
            char ch = input.charAt(i);

            switch (state) {
                case START:
                    if (input.startsWith("pasta", i)) {
                        state = State.PASTA;
                        //System.out.println(state);
                        i += 5;
                    } else if (input.startsWith("sauce", i)) {
                        state = State.SAUCE;
                        i += 5;
                    } else if (input.startsWith("basil", i)) {
                        state = State.BASIL;
                        i += 5;
                    } else if (input.startsWith("recipe", i)) {
                        state = State.RECIPE;
                        func_state = Func_State.START;
                        i += 6;
                    } else {
                        return false;
                    }
                    break;

                case PASTA:
                case SAUCE:
                case BASIL:
                    i = skipSpaces(input, i);

                    if (i < input.length() && (Character.isLetter(input.charAt(i)))) {
                        while (i < input.length() && (Character.isLetterOrDigit(input.charAt(i)) || input.charAt(i) == '_')) {
                            i++;
                        }
                    }
                    state = (state == State.PASTA) ? State.VAR_NAME_PASTA
                            : (state == State.SAUCE) ? State.VAR_NAME_SAUCE
                            : State.VAR_NAME_BASIL;
                    //System.out.println(state);
                    break;

                case VAR_NAME_PASTA:
                case VAR_NAME_SAUCE:
                case VAR_NAME_BASIL:
                    i = skipSpaces(input, i);
                    if (i < input.length() && input.charAt(i) == '=') {
                        state = (state == State.VAR_NAME_PASTA) ? State.ASSIGN_PASTA
                                : (state == State.VAR_NAME_SAUCE) ? State.ASSIGN_SAUCE
                                : State.ASSIGN_BASIL;
                        //System.out.println(state);
                        i++;
                    } else {
                        return false;
                    }
                    break;

                case ASSIGN_PASTA:
                    i = skipSpaces(input, i);
                    while (i < input.length() && Character.isDigit(input.charAt(i))) {
                        i++;
                    }
                    state = State.VALUE_PASTA;

                    break;

                case VALUE_PASTA:
                    if (input.charAt(i) == 'g') {
                        i++;
                        state = State.UNIT_PASTA;
                        //System.out.println(state);
                    } else {
                        return false;
                    }
                    break;

                case UNIT_PASTA:
                    if (input.charAt(i) == '~') {
                        i++;
                        //System.out.println("Estado antes de ~: " + state + ", índice: " + i);
                        state = State.ACCEPT;
                        //System.out.println("Estado después de ~: " + state + ", índice: " + i);
                        if (i == input.length()) {
                            return true;
                        }
                    } else {
                        return false;
                    }
                    break;

                case ASSIGN_SAUCE:
                    i = skipSpaces(input, i);
                    if (i < input.length() && input.charAt(i) == '\"') {
                        i++;
                        state = State.STRING_SAUCE;
                    } else {
                        return false;
                    }
                    break;

                case STRING_SAUCE:
                    while (i < input.length() && input.charAt(i) != '\"') {
                        i++;
                    }
                    if (i < input.length() && input.charAt(i) == '\"') {
                        i++;
                        state = State.END_STRING_SAUCE;
                    } else {
                        return false;
                    }
                    break;

                case END_STRING_SAUCE:
                    if (input.charAt(i) == '~') {
                        i++;
                        //System.out.println("Estado antes de ~: " + state + ", índice: " + i);
                        state = State.ACCEPT;
                        //System.out.println("Estado después de ~: " + state + ", índice: " + i);
                        if (i == input.length()) {
                            return true;
                        }
                    } else {
                        return false;
                    }
                    break;

                case ASSIGN_BASIL:
                    i = skipSpaces(input, i);
                    if (input.startsWith("cooked", i)) {
                        i += 6;
                        state = State.COOKED_BASIL;
                    } else if (input.startsWith("burned", i)) {
                        i += 6;
                        state = State.BURNED_BASIL;
                    } else {
                        return false;
                    }
                    break;

                case COOKED_BASIL:
                case BURNED_BASIL:
                    if (input.charAt(i) == '~') {
                        i++;
                        //System.out.println("Estado antes de ~: " + state + ", índice: " + i);
                        state = State.ACCEPT;
                        //System.out.println("Estado después de ~: " + state + ", índice: " + i);
                        if (i == input.length()) {
                            return true;
                        }
                    } else {
                        return false;
                    }
                    break;

                case RECIPE:
                    i = skipSpaces(input, i);
                    if (i < input.length() && (Character.isLetter(input.charAt(i)))) {
                        while (i < input.length() && (Character.isLetterOrDigit(input.charAt(i)) || input.charAt(i) == '_')) {
                            i++;
                        }
                    }

                    func_state = Func_State.FUNC_NAME;

                    while (true) {

                        switch (func_state) {
                            case FUNC_NAME:
                                i = skipSpaces(input, i);
                                if (i < input.length() && input.charAt(i) == '(') {
                                    i++;
                                    func_state = Func_State.FUNC_OPEN;
                                } else {
                                    //cambio realizado
                                    return false;
                                }
                            break;

                            case FUNC_OPEN:
                                while (i < input.length() && input.charAt(i) != ')') {
                                    i = skipSpaces(input, i);
                                    String[] types = {"pasta", "sauce", "basil"};
                                    boolean matched = false;
                                    for (String type : types) {
                                        if (input.startsWith(type, i)) {
                                            i += type.length();
                                            matched = true;
                                            break;
                                        }
                                    }
                                    if (!matched) return false;

                                    i = skipSpaces(input, i);

                                    if (i < input.length() && Character.isLetter(input.charAt(i))) {
                                        while (i < input.length() && (Character.isLetterOrDigit(input.charAt(i)) || input.charAt(i) == '_')) {
                                            i++;
                                        }
                                    } else {
                                        return false;
                                    }

                                    i = skipSpaces(input, i);

                                    /*if (i < input.length() && input.charAt(i) == ',') {
                                        i++;
                                        continue;
                                    }
                                    break;*/

                                }

                                if (i < input.length() && input.charAt(i) == ')') {
                                    i++;
                                    func_state = Func_State.FUNC_CLOSE;
                                } else {
                                    return false;
                                }
                            break;

                            case FUNC_CLOSE:
                                i = skipSpaces(input, i);
                                if (i < input.length() && input.charAt(i) == '{') {
                                    i++;
                                    func_state = Func_State.FUNC_BODY_OPEN;
                                } else {
                                    return false;
                                }
                            break;

                            case FUNC_BODY_OPEN:
                                i = skipSpaces(input, i);
                                while (i < input.length()) {
                                    i = skipSpaces(input, i);

                                    if (input.charAt(i) == '}') {
                                        i++;
                                        state = State.ACCEPT;
                                        if (i == input.length()) {
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                }

                                int start = i;
                                if (input.startsWith("pasta ", i) || input.startsWith("sauce ", i) || input.startsWith("basil ", i)) {
                                    StringBuilder tipo = new StringBuilder();
                                    while(i < input.length() && (input.charAt(i) != ' ' || input.charAt(i) != '~')) {
                                        tipo.append(input.charAt(i));
                                        i++;
                                    }
                                    //aquí deseo que si matchea pasta, sauce, basil verifique que lo que le presigue sea un nombre de variable correcto y una inicialización correcta
                                }

                            break;
                        }
                        break;
                    }

                    break;

                case ACCEPT:
                    return i == input.length();

                default:
                    return false;
            }
        }
        return false; // Si el bucle termina y no se alcanzó ACCEPT al final de la entrada
    }

    private int skipSpaces(String input, int index) {
        while (index < input.length() && input.charAt(index) == ' ') index++;
        return index;
    }

    public static void main(String[] args) {
        DFAValidator validator = new DFAValidator();
        String[] tests = {
                "pasta num = 34g~",
                "pasta  num =89g~",
                "pasta 2 = 34g~",
                "pasta _yes = 34g~",
                "pasta yes = 3ag~",
                "sauce letras = \"heey\"~",
                "sauce yay   = \"\"~",
                "basil bool = cooked~",
                "basil bool_2 = burned~",
                "basil    hehe =cooked~",
                "basil oops = lmao7~",
                "recipe function(pasta num){ pasta numero~ pasta numero_2~ }",
                "recipe function(pasta num){ " +
                        "pasta numero~ pasta numero_2~ " +
                "}",
                "recipe function_2(sauce string){ sauce example~ }",
                "recipe functionTres(){ }"
        };

        for (String test : tests) {
            System.out.printf("\"%s\" => %s%n", test, validator.accepts(test) ? "✔ Valid" : "✘ Invalid");
        }
    }
}
