package Analizador_Semantico;

import Analizador_Lexico.Token;
import Errores.E017;
import Errores.E018;
import Errores.ErrorSemantico;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalizadorSemantico {
    static List<String> input = new ArrayList<>();
    static TablaSimbolos tablaSimbolos = new TablaSimbolos();
    List<Simbolo> tablaDeSimbolos = new ArrayList<>();
    private List<ErrorSemantico> error = new ArrayList<>();

    public AnalizadorSemantico(List<Token> tokens) {
        //recibe los tokens del lexer
        crearTabla(tokens);

        //maneja simbolos para manejar los ambitos
        tablaDeSimbolos = tablaSimbolos.getSimbolos();

        Map<String, List<Simbolo>> agrupados = tablaDeSimbolos.stream()
                .collect(Collectors.groupingBy(s -> s.getAmbito() + ":" + s.getValor() + ":" + s.getTipo()));

        for (Map.Entry<String, List<Simbolo>> entrada : agrupados.entrySet()) {
            List<Simbolo> simbolos = entrada.getValue();
            String[] partes = entrada.getKey().split(":");
            String valor = partes[1];
            String tipo = partes[2];

            if (simbolos.size() > 1) {
                for (Token token : tokens) {
                    if(token.getValor().equals(valor) && !tipo.equals("serve")) {
                        ErrorSemantico errorS = new E017(valor, token.getPosicion());
                        error.add(errorS);
                    }
                }
            }

            boolean declarado = true;
            int pos = 0;

            for (Token token : tokens) {
                if (tipo.equals("serve")) {
                    declarado = tablaDeSimbolos.stream()
                            .anyMatch(simbolo -> simbolo.getValor().equals(valor) && !simbolo.getTipo().equals("serve"));

                    if (!declarado) {
                        pos = token.getPosicion();
                    }
                }
            }

            if (!declarado) {
                ErrorSemantico errorS = new E018(pos, valor);
                error.add(errorS);
            }
        }
    }

    private void crearTabla(List<Token> tokens) {
        int currentTokenIndex = 0;
        input.clear();

        if(tokens != null) {
            for (Token token : tokens) {
                input.add(token.getTipo());
            }
        }

        while (currentTokenIndex < input.size()) {
            String tokenTipo = input.get(currentTokenIndex);

            if (tokenTipo.equals("recipe")) {
                currentTokenIndex++;
                String ambito = "";

                if(currentTokenIndex < input.size() && input.get(currentTokenIndex).equals("main")) {
                    currentTokenIndex++;
                    if (currentTokenIndex < input.size() && input.get(currentTokenIndex).equals("ID")) {
                        String valor = tokens.get(currentTokenIndex).getValor();
                        ambito = valor;
                        tablaSimbolos.agregarSimbolo(new Simbolo("main", valor, "global"));
                        currentTokenIndex++;
                    }

                } else if (currentTokenIndex < input.size() && input.get(currentTokenIndex).equals("ID")) {
                    String valor = tokens.get(currentTokenIndex).getValor();
                    ambito = valor;
                    tablaSimbolos.agregarSimbolo(new Simbolo("recipe", valor, "global"));
                    currentTokenIndex++;

                }
                if (currentTokenIndex + 3 < input.size()
                            && (input.get(currentTokenIndex).equals("parentesis_apertura"))
                            && ((input.get(currentTokenIndex + 3).equals("parentesis_cierre"))
                            || (input.get(currentTokenIndex + 1).equals("parentesis_cierre")))) {

                    if (!input.get(currentTokenIndex + 1).equals("parentesis_cierre")) {
                        String tipoParam = input.get(currentTokenIndex + 1);
                        String nombreParam = tokens.get(currentTokenIndex + 2).getValor();
                        tablaSimbolos.agregarSimbolo(new Simbolo(tipoParam, nombreParam, ambito, true));
                        currentTokenIndex += 5;
                    } else {
                        currentTokenIndex += 3;
                    }

                    while(!input.get(currentTokenIndex).equals("llave_cierre")) {

                        switch (input.get(currentTokenIndex)) {
                            case "pasta", "sauce", "basil" -> {
                                String tipoVar = input.get(currentTokenIndex);
                                currentTokenIndex++;
                                if (currentTokenIndex < input.size() && input.get(currentTokenIndex).equals("ID")) {
                                    String valorVar = tokens.get(currentTokenIndex).getValor();
                                    tablaSimbolos.agregarSimbolo(new Simbolo(tipoVar, valorVar, ambito));
                                }
                                currentTokenIndex++;
                            }
                            case "serve" -> {
                                if (currentTokenIndex + 1 < input.size()
                                        && (input.get(currentTokenIndex + 1).equals("parentesis_apertura"))) {
                                    String tipo = input.get(currentTokenIndex);
                                    currentTokenIndex += 2;

                                    String valVar = tokens.get(currentTokenIndex).getValor();
                                    tablaSimbolos.agregarSimbolo(new Simbolo(tipo, valVar, ambito));
                                }
                                currentTokenIndex++;
                            }
                            case "chef" -> {
                                if (currentTokenIndex + 2 < input.size()
                                        && (input.get(currentTokenIndex + 1).equals("punto_chef"))
                                        && (input.get(currentTokenIndex + 2).matches("chef_func"))) {
                                    String tipo = input.get(currentTokenIndex);
                                    String valVar = tokens.get(currentTokenIndex + 2).getValor();
                                    currentTokenIndex += 2;

                                    tablaSimbolos.agregarSimbolo(new Simbolo(tipo, valVar, ambito));

                                    if (currentTokenIndex + 1 < input.size()
                                            && (input.get(currentTokenIndex + 1).equals("parentesis_apertura"))
                                            && (input.get(currentTokenIndex + 2).equals("chef_args"))) {
                                        currentTokenIndex += 2;

                                        String tipoChef = input.get(currentTokenIndex);
                                        String valVarChef = tokens.get(currentTokenIndex).getValor();

                                        tablaSimbolos.agregarSimbolo(new Simbolo(tipoChef, valVarChef, ambito, true));
                                    }
                                }
                                currentTokenIndex++;
                            }
                        }
                        currentTokenIndex++;
                    }
                }
            }


            else if (tokenTipo.equals("pasta") || tokenTipo.equals("sauce") || tokenTipo.equals("basil")) {
                currentTokenIndex++; // match tipoDato

                if (currentTokenIndex < input.size() && input.get(currentTokenIndex).equals("ID")) {
                    String nombreVar = tokens.get(currentTokenIndex).getValor();
                    tablaSimbolos.agregarSimbolo(new Simbolo(tokenTipo, nombreVar, "global")); // asumir 'global' si no estamos dentro de una función
                    currentTokenIndex++;
                }
            }

            else {
                currentTokenIndex++;
            }
        }
    }

    public static TablaSimbolos getTablaSimbolos() {
        return tablaSimbolos;
    }

    public List<Simbolo> getTablaDeSimbolos() {
        return tablaSimbolos.getSimbolos();
    }

    public List<ErrorSemantico> getError() {
        return error;
    }
}
