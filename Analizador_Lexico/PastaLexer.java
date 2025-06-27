package Analizador_Lexico;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DFA.DFA;
import DFA.DFADynamic.State;
import Errores.*;


public class PastaLexer {
                private String input;
                private int posicionActual;
                private List<Token> tokens;
                private List<ErrorLexico> errores;
                private List<ErrorSintactico> erroresS;
                private DFA dfa;

                public PastaLexer(String input) {
                    this.input = input.trim();
                    this.posicionActual = 0;
                    this.tokens = new ArrayList<>();
                    this.errores = new ArrayList<>();
                    this.erroresS = new ArrayList<>();
                    this.dfa = new DFA().construirDFA();
                }

                public PastaLexer() {
                    this.tokens = new ArrayList<>();
                    this.errores = new ArrayList<>();
                    this.erroresS = new ArrayList<>();
                    this.dfa = new DFA().construirDFA();
                }

                private ErrorLexico identificarErrorLexico(char caracter, State estadoActual) {
                    switch (estadoActual) {
                        case START:
                            if (!Character.isLetter(caracter)) {
                                ErrorLexico ER = new E001(posicionActual);
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

            case FUNC_PAREN_OPEN:
                if (caracter != 'p' && caracter != 's' && caracter != 'b' && caracter != ')' && caracter != ' '){
                    ErrorLexico ER = new E003(posicionActual);
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
                ErrorLexico ER = new E016(posicionActual, caracter);
                return new ErrorLexico(ER.getIdError(), ER.getTipo(), ER.getPosicion(),
                        ER.getDescripcion());
        }

        return new ErrorLexico("E000", "Error de lógica léxica no especificado", posicionActual,
                "Se produjo un error léxico no cubierto por las reglas específicas. Carácter: '" + caracter + "'");
    }

    private ErrorSintactico identificarErrorSintactico(char caracter, State estadoActual) {
        switch (estadoActual) {
            case START:
                if (!Character.isLetter(caracter)) {
                    ErrorSintactico ES = new E002(posicionActual);
                    return new ErrorSintactico(ES.getIdError(), ES.getTipo(), ES.getPosicion(),
                            ES.getDescripcion());
                }


            case FUNC_NAME_END:
                if (caracter != '('){
                    ErrorSintactico ES = new E004(posicionActual);
                    return new ErrorSintactico(ES.getIdError(), ES.getTipo(), ES.getPosicion(),
                            ES.getDescripcion());
                }
                break;

            case FUNC_ARGS_NAME:
                if(!Character.isLetterOrDigit(caracter) && caracter != ')'){
                    ErrorSintactico ES = new E005(posicionActual);
                    return new ErrorSintactico(ES.getIdError(), ES.getTipo(), ES.getPosicion(),
                            ES.getDescripcion());
                }
                break;

            case FUNC_ARGS_NAME_END:
                if (caracter != ')'){
                    ErrorSintactico ES = new E005(posicionActual);
                    return new ErrorSintactico(ES.getIdError(), ES.getTipo(), ES.getPosicion(),
                            ES.getDescripcion());
                }
                break;

            case FUNC_PAREN_CLOSE:
                if (caracter != '{'){
                    ErrorSintactico ES = new E006(posicionActual);
                    return new ErrorSintactico(ES.getIdError(), ES.getTipo(), ES.getPosicion(),
                            ES.getDescripcion());
                }
                break;

            default:
                if (caracter == '='){
                    ErrorSintactico ES = new E008(posicionActual);
                    return new ErrorSintactico(ES.getIdError(), ES.getTipo(), ES.getPosicion(),
                            ES.getDescripcion());
                }
                else if (estadoActual.toString().endsWith("_ACCEPT") && caracter != '~'){
                    ErrorSintactico ES = new E015(posicionActual);
                    return new ErrorSintactico(ES.getIdError(), ES.getTipo(), ES.getPosicion(),
                            ES.getDescripcion());
                }
                else if (caracter == '}' && !estadoActual.toString().equals("FUNC_BODY_OPEN")){
                    ErrorSintactico ES = new E007(posicionActual);
                    return new ErrorSintactico(ES.getIdError(), ES.getTipo(), ES.getPosicion(),
                            ES.getDescripcion());
                }
        }

        return new ErrorSintactico("E000", "Error de lógica léxica no especificado", posicionActual,
                "Se produjo un error léxico no cubierto por las reglas específicas. Carácter: '" + caracter + "'");
    }

    public List<ErrorLexico> getErrores() {
        return errores;
    }

    public List<ErrorSintactico> getErroresS() {
        return erroresS;
    }

    public List<Token> analizarFull(String input){
        //esto se hace para que un bloque completo de texto lo analice por bloques más pequeño y se optimice el proceso
        List<String> inputsDivididos = dividirInput(input);
        List<Token> tokensFull = new ArrayList<>();

        for (String inputDividido : inputsDivididos) {
            tokensFull.addAll(analizar(inputDividido));
        }

        return tokensFull;
    }

    public List<String> dividirInput(String input) {
        List<String> segmentos = new ArrayList<>();
        int braceCount = 0;
        int segmentStart = 0;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '{') {
                braceCount++;
            } else if (c == '}') {
                braceCount--;
            }

            if ((c == '~' && braceCount == 0) || (c == '}' && braceCount == 0)) {
                String segmento = input.substring(segmentStart, i + 1).trim();
                if (!segmento.isEmpty()) {
                    segmentos.add(segmento);
                }

                while (i + 1 < input.length() && Character.isWhitespace(input.charAt(i + 1))) {
                    i++;
                }
                segmentStart = i + 1;
            }
        }

        if (segmentStart < input.length()) {
            String remainingSegment = input.substring(segmentStart).trim();
            if (!remainingSegment.isEmpty()) {
                segmentos.add(remainingSegment);
            }
        }

        return segmentos;
    }


    public List<Token> analizar(String input) {
        this.input = input;
        this.tokens.clear();
        this.errores.clear();
        this.erroresS.clear();
        this.posicionActual = 0;

        this.dfa = new DFA().construirDFA();

        dfa.setCurrentState(State.START);

            while (posicionActual < this.input.length()) {
                boolean transicionValida = false;
                List<DFA.Transition> transiciones = dfa.getTransitions(dfa.getCurrentState());

                //analiza por transiciones
                for (DFA.Transition transicion : transiciones) {
                    if (transicion.condition.matches(this.input, posicionActual)) {
                        if (transicion.tokenType != null) {
                            //agrega a la lista de tokens
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
                    ErrorLexico error = identificarErrorLexico(this.input.charAt(posicionActual), dfa.getCurrentState());
                    ErrorSintactico errorS = identificarErrorSintactico(this.input.charAt(posicionActual), dfa.getCurrentState());

                    errores.add(error);
                    erroresS.add(errorS);
                    //throw new RuntimeException(error.toString());
                    return tokens;
                }
            }

            if (!dfa.isAcceptState()) {
                ErrorSintactico error = new E015(posicionActual);
                erroresS.add(error);
                //throw new RuntimeException(error.toString());
                return tokens;
            }

            if (tokens.isEmpty() && !input.isEmpty() && errores.isEmpty()) {
                System.out.println("No se encontraron tokens válidos en la entrada.");
            }

            return tokens;
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
        String[] inputs = {"pasta n = 5g~", "recipe f(pasta n) { serve(n)~ }"};
        String put = "recipe f() { int num = 8g~ serve(num)~ }";

        //inicialmente se divide para analizar por bloques
        PastaLexer lexy = new PastaLexer();
        System.out.println(lexy.dividirInput(put));
        List<Token> tokens = lexy.analizarFull(put);
        printSymbolTable(tokens);

        //se imprimen tanto tokens como errores
        System.out.println("Tokens generados:");
        tokens.forEach(System.out::println);

        if (!lexy.getErrores().isEmpty()) {
            System.err.println("Errores lexicos encontrados:");
            lexy.getErrores().forEach(System.err::println);
        }
        if (!lexy.getErroresS().isEmpty()) {
            System.err.println("Errores semanticos encontrados:");
            lexy.getErroresS().forEach(System.err::println);
        }
    }
}
