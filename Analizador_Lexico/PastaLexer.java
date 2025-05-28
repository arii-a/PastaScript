package Analizador_Lexico;

import java.util.*;

import DFA.DFA;
import DFA.DFADynamic.State;
import Errores_Lexicos.*;


public class PastaLexer {
    private String input;
    private int posicionActual;
    private List<Token> tokens;
    private List<ErrorLexico> errores;
    private DFA dfa;

    public PastaLexer(String input) {
        this.input = input.trim();
        this.posicionActual = 0;
        this.tokens = new ArrayList<>();
        this.errores = new ArrayList<>();
        this.dfa = new DFA().construirDFA();
    }

    public PastaLexer() {
        this.tokens = new ArrayList<>();
        this.errores = new ArrayList<>();
        this.dfa = new DFA().construirDFA();
    }


    private ErrorLexico identificarError(char caracter, State estadoActual) {
        switch (estadoActual) {
            case START:
                if (!Character.isLetter(caracter)) {
                    ErrorLexico ER = new E001(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                } else {
                    ErrorLexico ER = new E002(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }

            case PASTA:
            case SAUCE:
            case BASIL:
            case RECIPE:
            case FUNC_ARGS:
                if (!Character.isLetter(caracter)) {
                    ErrorLexico ER = new E003(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }
                break;


            case VAR_NAME_PASTA:
            case VAR_NAME_SAUCE:
            case VAR_NAME_BASIL:
            case FUNC_NAME:
                if (!Character.isLetterOrDigit(caracter)) {
                    ErrorLexico ER = new E003(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                        ER.getDescripcion());
                }
                break;

            case ASSIGN_PASTA:
                if (!Character.isDigit(caracter) && !Character.isWhitespace(caracter)){
                    ErrorLexico ER = new E009(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }
                break;

            case VALUE_PASTA:
                if (caracter != 'g'){
                    ErrorLexico ER = new E010(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }
                break;

            case ASSIGN_SAUCE:
                if (caracter != '"'){
                    ErrorLexico ER = new E012(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }
                break;

            case SAUCE_STRING:
                if (caracter == '~' || caracter == '\n'){
                    ErrorLexico ER = new E013(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }
                break;

            case ASIGN_BASIL:
                if (caracter != 'c' && caracter != 'b'){
                    ErrorLexico ER = new E014(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }
                break;

            case FUNC_NAME_END:
                if (caracter != '('){
                    ErrorLexico ER = new E004(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }
                break;

            case FUNC_PAREN_OPEN:
                if (caracter != 'p' && caracter != 's' && caracter != 'b' && caracter != ')' && caracter != ' '){
                    ErrorLexico ER = new E003(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }
                break;

            case FUNC_ARGS_NAME:
                if(!Character.isLetterOrDigit(caracter) && caracter != ')'){
                    ErrorLexico ER = new E005(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }
                break;

            case FUNC_ARGS_NAME_END:
                if (caracter != ')'){
                    ErrorLexico ER = new E005(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }
                break;

            case FUNC_PAREN_CLOSE:
                if (caracter != '{'){
                    ErrorLexico ER = new E006(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }
                break;

            case FUNC_BODY_OPEN:
                if (caracter != 'p' && caracter != 's' && caracter != 'b' && caracter != '}' && caracter != ' '){
                    ErrorLexico ER = new E001(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }
                break;

            default:
                if (caracter == '='){
                    ErrorLexico ER = new E008(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }
                else if (estadoActual.toString().endsWith("_ACCEPT") && caracter != '~'){
                    ErrorLexico ER = new E015(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }
                else if (caracter == '}' && !estadoActual.toString().equals("FUNC_BODY_OPEN")){
                    ErrorLexico ER = new E007(posicionActual);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }
                else{
                    ErrorLexico ER = new E016(posicionActual, caracter);
                    return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                            ER.getDescripcion());
                }
        }

        return new ErrorLexico("E000", "Error de lógica léxica no especificado", posicionActual,
                "Se produjo un error léxico no cubierto por las reglas específicas. Carácter: '" + caracter + "'");
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public List<ErrorLexico> getErrores() {
        return errores;
    }

    public List<Token> analizar(String input) {
        this.input = input.trim();
        this.tokens.clear();
        this.errores.clear();
        this.posicionActual = 0;
        this.dfa = new DFA().construirDFA();

        dfa.setCurrentState(State.START);

            while (posicionActual < this.input.length()) {
                boolean transicionValida = false;
                List<DFA.Transition> transiciones = dfa.getTransitions(dfa.getCurrentState());
                //System.out.println(transiciones);
                //transiciones.sort((t1, t2) -> Integer.compare(t2.consume, t1.consume));

                for (DFA.Transition transicion : transiciones) {
                    if (transicion.condition.matches(this.input, posicionActual)) {
                        if (transicion.tokenType != null) {
                            String valor = this.input.substring(posicionActual, posicionActual + transicion.consume);
                            tokens.add(new Token(transicion.tokenType, valor, posicionActual));
                        }

                        posicionActual += transicion.consume;
                        dfa.setCurrentState(transicion.nextState);
                        transicionValida = true;
                        break;
                    }
                }

                if (!transicionValida) {
                    ErrorLexico error = identificarError(this.input.charAt(posicionActual), dfa.getCurrentState());
                    errores.add(error);
                    //throw new RuntimeException(error.toString());
                    return tokens;
                }
            }

            if (!dfa.isAcceptState()) {
                ErrorLexico error = new E015(posicionActual);
                errores.add(error);
                //throw new RuntimeException(error.toString());
                return tokens;
            }

            if (tokens.isEmpty() && !input.isEmpty() && errores.isEmpty()) {
                System.out.println("No se encontraron tokens válidos en la entrada.");
            }

            return tokens;

        /*} catch (Exception e) {
            if (errores.isEmpty()) {
                ErrorLexico error = new ErrorLexico("E000", "Error desconocido", posicionActual,
                        e.getMessage());
                errores.add(error);
            }
            throw new RuntimeException();
        }*/
    }

    public static void printSymbolTable(List<Token> tokens) {
        if (tokens.isEmpty()) {
            System.out.println("No se generaron tokens.");
            return;
        }

        String header = String.format("%-25s %-30s %-20s %30s",
                "TOKEN", "TIPO DE TOKEN", "VALOR", "POSICIÓN");
        System.out.println(header);
        System.out.println(String.join("", Collections.nCopies(header.length(), "-")));

        HashMap<String, Set<Integer>> posiciones = new HashMap<>();
        for (Token token : tokens) {
            posiciones
                    .computeIfAbsent(token.getValor(), k -> new TreeSet<>())
                    .add(token.getPosicion());
        }

        HashMap<Token, String> tablaReducida = new HashMap<>();
        for (Token token : tokens) {
            if(!tablaReducida.containsValue(token.getValor()))
                tablaReducida.put(token, token.getValor());
        }

        for (Token token : tablaReducida.keySet()) {
            String tokenRow = String.format("%-25s %-30s %-20s %-30s",
                    token.getTipo(),
                    token.getTokens(),
                    "\"" + token.getValor() + "\"",
                    posiciones.get(token.getValor()));
            System.out.println(tokenRow);
        }
        System.out.println();

    }

    public static void main(String[] args) {
        String input = "recipe h(sauce s) { sauce s = \"h\"~ }";
        PastaLexer lexer = new PastaLexer(input);

        List<Token> tokens = lexer.analizar(input);
        printSymbolTable(tokens);

        System.out.println("Tokens generados:");
        tokens.forEach(System.out::println);
        if (!lexer.getErrores().isEmpty()) {
            System.err.println("Errores encontrados:");
            lexer.getErrores().forEach(System.err::println);
        }
    }
}
