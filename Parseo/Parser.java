package Parseo;

import Analizador_Lexico.PastaLexer;
import Analizador_Lexico.Token;
import Analizador_Semantico.AnalizadorSemantico;
import Generacion_Codigo.CodeGenerator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Parser {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    private String inputOriginal;

    static List<String> input;
    static int pos;
    static Stack<String> stack;
    static Map<String, Map<String, List<String>>> tablaParse;
    static List<Token> tokensDelAnalizador;


    public Parser(String inputOriginal) {
        this.inputOriginal = inputOriginal;
    }

    static {
        inicializarTabla();
    }

    public void Parseo() {
        this.inputOriginal = inputOriginal;

        PastaLexer lexer = new PastaLexer();

        //toma los tokens generados por el analizador lexico
        tokensDelAnalizador = lexer.analizarFull(inputOriginal);
        if (!lexer.getErrores().isEmpty()) {
            System.err.println("Errores lexicos encontrados:");
            lexer.getErrores().forEach(System.err::println);
            //en caso de haber errores no se imprimen los tokens
            tokensDelAnalizador = null;
            return;
        }
        if (!lexer.getErroresS().isEmpty()) {
            System.err.println("Errores semanticos encontrados:");
            lexer.getErroresS().forEach(System.err::println);
            tokensDelAnalizador = null;
            return;
        }

        AnalizadorSemantico analizadorSemantico = new AnalizadorSemantico(tokensDelAnalizador);


        input = new ArrayList<>();
        if(tokensDelAnalizador != null) {
            for (Token token : tokensDelAnalizador) {
                input.add(token.getTipo());
            }
        }

        pos = 0;
        stack = new Stack<>();
        inicializarTabla();

        //se incia el programa
        stack.push("$");
        stack.push("programa");

        List<String> stacks = new ArrayList<>();
        List<String> inputs = new ArrayList<>();
        List<String> outputs = new ArrayList<>();

        while (!stack.isEmpty()) {
            String tokenActual = pos < input.size() ? input.get(pos) : "$";

            String tope = stack.peek();

            String stackStr = stackToString();
            String inputStr = inputToString();

            //se busca si matchea con el token actual
            if (tope.equals(tokenActual)) {
                stack.pop();

                pos++;
                stacks.add(stackStr);
                inputs.add(inputStr);
                outputs.add("(match " + tokenActual + ")");
            } else if (isTerminal(tope)) {
                stacks.add(stackStr);
                inputs.add(inputStr);
                //error en caso de no matchear
                outputs.add("Error: token inesperado '" + tokenActual + "'");
                break;
            } else {
                List<String> produccion = getProduccion(tope, tokenActual);
                if (produccion == null) {
                    stacks.add(stackStr);
                    inputs.add(inputStr);
                    //error en caso de que no se pueda continuar por produccion
                    outputs.add("Error: no hay producción para [" + tope + ", " + tokenActual + "]");
                    break;
                } else {
                    stack.pop();
                    //si es epsilon, se quita
                    if (!(produccion.size() == 1 && produccion.get(0).equals("ε"))) {
                        for (int i = produccion.size() - 1; i >= 0; i--) {
                            stack.push(produccion.get(i));
                        }
                    }

                    stacks.add(stackStr);
                    inputs.add(inputStr);
                    outputs.add(tope + " → " + String.join(" ", produccion));
                }
            }
        }

        if (stack.isEmpty() && pos >= input.size()) {
            stacks.add("$");
            inputs.add("$");
            outputs.add(ANSI_GREEN + "ACEPTADO" + ANSI_RESET);
            analizadorSemantico.getError().forEach(System.err::println);
        } else {
            stacks.add("$");
            inputs.add("$");
            outputs.add(ANSI_RED + "RECHAZADO" + ANSI_RESET);
            lexer.getErrores().forEach(System.err::println);
            lexer.getErroresS().forEach(System.err::println);
            analizadorSemantico.getError().forEach(System.err::println);
        }

        imprimirTablaDinamica(stacks, inputs, outputs);
        if (tokensDelAnalizador != null) {
            CodeGenerator codeGenerator = new CodeGenerator(analizadorSemantico.getTablaDeSimbolos());
            String codigoGenerado = codeGenerator.generarDesdeLineas(lexer.dividirInput(inputOriginal));
            System.out.println(codigoGenerado);
            try {
                String archivoJava = "ProgramaGenerado.java";
                Files.write(Paths.get(archivoJava), codigoGenerado.getBytes());
                System.out.println("código java generado y guardado en " + archivoJava);

                Process compilar = Runtime.getRuntime().exec("javac " + archivoJava);
                compilar.waitFor();
                System.out.println("compilación completada.");

                Process ejecutar = Runtime.getRuntime().exec("java ProgramaGenerado");
                BufferedReader reader = new BufferedReader(new InputStreamReader(ejecutar.getInputStream()));
                String linea;
                System.out.println("Salida del programa:");
                while ((linea = reader.readLine()) != null) {
                    System.out.println(linea);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void inicializarTabla() {
        tablaParse = new HashMap<>();

        // Ejemplo de inicialización para 'programa'
        // Basado en taba parser
        Map<String, List<String>> rowPrograma = new HashMap<>();
        rowPrograma.put("pasta", Arrays.asList("sentencia", "$"));
        rowPrograma.put("sauce", Arrays.asList("sentencia", "$"));
        rowPrograma.put("basil", Arrays.asList("sentencia", "$"));
        rowPrograma.put("recipe", Arrays.asList("sentencia", "$"));
        rowPrograma.put("serve", Arrays.asList("sentencia", "$"));
        rowPrograma.put("chef", Arrays.asList("sentencia", "$"));
        rowPrograma.put("$", Arrays.asList("sentencia", "$"));
        tablaParse.put("programa", rowPrograma);

        Map<String, List<String>> rowSentencia = new HashMap<>();
        rowSentencia.put("pasta", Arrays.asList("declaracionVariable", "sentencia"));
        rowSentencia.put("sauce", Arrays.asList("declaracionVariable", "sentencia"));
        rowSentencia.put("basil", Arrays.asList("declaracionVariable", "sentencia"));
        rowSentencia.put("recipe", Arrays.asList("declaracionFunciones", "sentencia"));
        rowSentencia.put("serve", Arrays.asList("sentenciaPrint", "sentencia"));
        rowSentencia.put("chef", Arrays.asList("sentenciaChef"));
        rowSentencia.put("parentesis_cierre", Arrays.asList("ε"));
        rowSentencia.put("llave_cierre", Arrays.asList("ε"));
        rowSentencia.put("$", Arrays.asList("ε"));
        tablaParse.put("sentencia", rowSentencia);

        Map<String, List<String>> rowDeclaracionVar = new HashMap<>();
        rowDeclaracionVar.put("pasta", Arrays.asList("pasta", "ID", "igual", "expresionPasta", "end_line"));
        rowDeclaracionVar.put("sauce", Arrays.asList("sauce", "ID", "igual", "expresionSauce", "end_line"));
        rowDeclaracionVar.put("basil", Arrays.asList("basil", "ID", "igual", "expresionBasil", "end_line"));
        tablaParse.put("declaracionVariable", rowDeclaracionVar);

        Map<String, List<String>> rowDeclaracionFunes = new HashMap<>();
        rowDeclaracionFunes.put("recipe", Arrays.asList("recipe", "tipoRecipe"));
        tablaParse.put("declaracionFunciones", rowDeclaracionFunes);

        Map<String, List<String>> rowTipoFun = new HashMap<>();
        rowTipoFun.put("recipe", Arrays.asList("recipe", "tipoRecipe"));
        tablaParse.put("declaracionFunciones", rowTipoFun);

        Map<String, List<String>> rowTipoRecipe = new HashMap<>();
        rowTipoRecipe.put("main", Arrays.asList("main", "ID", "parentesis_apertura", "parentesis_cierre", "llave_apertura", "listaSentenciasMain", "llave_cierre"));
        rowTipoRecipe.put("ID", Arrays.asList("ID", "parentesis_apertura", "entrada", "parentesis_cierre", "llave_apertura", "cuerpoFuncion", "llave_cierre"));
        tablaParse.put("tipoRecipe", rowTipoRecipe);

        Map<String, List<String>> rowEntrada = new HashMap<>();
        rowEntrada.put("pasta", Arrays.asList("tipoDato", "ID"));
        rowEntrada.put("sauce", Arrays.asList("tipoDato", "ID"));
        rowEntrada.put("basil", Arrays.asList("tipoDato", "ID"));
        rowEntrada.put("parentesis_cierre", Arrays.asList("ε"));
        tablaParse.put("entrada", rowEntrada);

        Map<String, List<String>> rowListaSentenciasMain = new HashMap<>();
        rowListaSentenciasMain.put("pasta", Arrays.asList("sentencia", "listaSentenciasMain"));
        rowListaSentenciasMain.put("sauce", Arrays.asList("sentencia", "listaSentenciasMain"));
        rowListaSentenciasMain.put("basil", Arrays.asList("sentencia", "listaSentenciasMain"));
        rowListaSentenciasMain.put("recipe", Arrays.asList("sentencia", "listaSentenciasMain"));
        rowListaSentenciasMain.put("serve", Arrays.asList("sentencia", "listaSentenciasMain")); // sentencia incluye sentenciaPrint
        rowListaSentenciasMain.put("chef", Arrays.asList("sentencia", "listaSentenciasMain"));   // sentencia incluye sentenciaChef
        rowListaSentenciasMain.put("llave_cierre", Arrays.asList("ε")); // epsilon para cuando la lista está vacía
        tablaParse.put("listaSentenciasMain", rowListaSentenciasMain);

        Map<String, List<String>> rowSentenciaChef = new HashMap<>();
        rowSentenciaChef.put("chef", Arrays.asList("chef", "punto_chef", "chef_func", "parentesis_apertura", "argumentosLlamada", "parentesis_cierre", "end_line"));
        tablaParse.put("sentenciaChef", rowSentenciaChef);

        Map<String, List<String>> rowArgumentosLlamada = new HashMap<>();
        rowArgumentosLlamada.put("chef_args", Arrays.asList("chef_args"));
        rowArgumentosLlamada.put("parentesis_cierre", Arrays.asList("ε"));
        tablaParse.put("argumentosLlamada", rowArgumentosLlamada);

        Map<String, List<String>> rowCierreApertura = new HashMap<>();
        rowCierreApertura.put("parentesis_apertura", Arrays.asList("chef_args", "parentesis_cierre"));
        rowCierreApertura.put("parentesis_cierre", Arrays.asList("ID"));
        tablaParse.put("cierre_apertura", rowCierreApertura);

        Map<String, List<String>> rowIdFuncion = new HashMap<>();
        rowIdFuncion.put("ID", Arrays.asList("ID", "parentesis_apertura", "argumentoEntrada", "parentesis_cierre"));
        tablaParse.put("idFuncion", rowIdFuncion);

        Map<String, List<String>> rowArgumentoEntrada = new HashMap<>();
        rowArgumentoEntrada.put("entero", Arrays.asList("expresionPasta"));
        rowArgumentoEntrada.put("comilla", Arrays.asList("expresionSauce"));
        rowArgumentoEntrada.put("cooked", Arrays.asList("expresionBasil"));
        rowArgumentoEntrada.put("burned", Arrays.asList("expresionBasil"));
        rowArgumentoEntrada.put("parentesis_cierre", Arrays.asList("ε"));
        tablaParse.put("argumentoEntrada", rowArgumentoEntrada);

        Map<String, List<String>> rowTipo = new HashMap<>();
        rowTipo.put("pasta", Arrays.asList("pasta"));
        rowTipo.put("sauce", Arrays.asList("sauce"));
        rowTipo.put("basil", Arrays.asList("basil"));
        tablaParse.put("tipoDato", rowTipo);

        Map<String, List<String>> rowCuerpoFun = new HashMap<>();
        rowCuerpoFun.put("pasta", Arrays.asList("declaracionVariable", "cuerpoFuncion"));
        rowCuerpoFun.put("sauce", Arrays.asList("declaracionVariable", "cuerpoFuncion"));
        rowCuerpoFun.put("basil", Arrays.asList("declaracionVariable", "cuerpoFuncion"));
        rowCuerpoFun.put("serve", Arrays.asList("sentenciaPrint", "cuerpoFuncion"));
        rowCuerpoFun.put("llave_cierre", Arrays.asList("ε"));
        tablaParse.put("cuerpoFuncion", rowCuerpoFun);

        Map<String, List<String>> rowExpresionPasta = new HashMap<>();
        rowExpresionPasta.put("entero", Arrays.asList("entero", "unidad"));
        tablaParse.put("expresionPasta", rowExpresionPasta);


        Map<String, List<String>> rowExpresionSauce = new HashMap<>();
        rowExpresionSauce.put("comilla", Arrays.asList("comilla", "string", "comilla"));
        tablaParse.put("expresionSauce", rowExpresionSauce);

        Map<String, List<String>> rowExpresionBasil = new HashMap<>();
        rowExpresionBasil.put("cooked", Arrays.asList("cooked"));
        rowExpresionBasil.put("burned", Arrays.asList("burned"));
        tablaParse.put("expresionBasil", rowExpresionBasil);

        Map<String, List<String>> rowUnidad = new HashMap<>();
        rowUnidad.put("g", Arrays.asList("g"));
        tablaParse.put("unidad", rowUnidad);

        Map<String, List<String>> rowSentenciaPrint = new HashMap<>();
        rowSentenciaPrint.put("serve", Arrays.asList("serve", "parentesis_apertura", "serve_args", "parentesis_cierre", "end_line"));
        tablaParse.put("sentenciaPrint", rowSentenciaPrint);

    }

    static boolean isTerminal(String symbol) {
        Set<String> terminals = new HashSet<>(Arrays.asList(
                "pasta", "sauce", "basil", "recipe", "ID", "igual", "parentesis_apertura", "parentesis_cierre",
                "llave_apertura", "llave_cierre", "end_line",
                "entero", "\"cadena\"", "cooked", "burned", "serve", "chef", "main", ".", "$", "g"
        ));
        return terminals.contains(symbol);
    }

    static List<String> getProduccion(String noTerminal, String token) {
        Map<String, List<String>> row = tablaParse.get(noTerminal);
        if (row == null) return null;
        return row.get(token);
    }

    static String stackToString() {
        return String.join(" ", stack);
    }

    static String inputToString() {
        StringBuilder sb = new StringBuilder();
        if(tokensDelAnalizador != null) {
            for (int i = pos; i < tokensDelAnalizador.size(); i++) {
                sb.append(tokensDelAnalizador.get(i).getTipo()).append(" ");
            }
        }
        return sb.toString().trim();
    }

    static void imprimirTablaDinamica(List<String> stacks, List<String> inputs, List<String> outputs) {
        int maxStack = "Stack".length();
        int maxInput = "Input".length();
        int maxOutput = "Output".length();

        for (String s : stacks) if (s.length() > maxStack) maxStack = s.length();
        for (String s : inputs) if (s.length() > maxInput) maxInput = s.length();
        for (String s : outputs) {
            String clean = s.replaceAll("\u001B\\[[;\\d]*m", "");
            if (clean.length() > maxOutput) maxOutput = clean.length();
        }

        String formatLeft = String.format("| %%-%ds | %%-%ds | %%-%ds |%n", maxStack, maxInput, maxOutput);
        String linea = "+" + "-".repeat(maxStack + 2) + "+" + "-".repeat(maxInput + 2) + "+" + "-".repeat(maxOutput + 2) + "+";

        System.out.println(linea);
        System.out.printf(formatLeft, "Stack", "Input", "Output");
        System.out.println(linea);

        for (int i = 0; i < stacks.size(); i++) {
            String output = outputs.get(i);
            if (i == stacks.size() - 1 && (output.contains("ACEPTADO") || output.contains("RECHAZADO"))) {
                imprimirFilaFinal(stacks.get(i), inputs.get(i), output, maxOutput, maxInput, maxStack);
            } else {
                System.out.printf(formatLeft, stacks.get(i), inputs.get(i), output);
            }
        }

        System.out.println(linea);
    }

    static void imprimirFilaFinal(String stack, String input, String output, int maxOutput, int maxInput, int maxStack) {
        String cleanOutput = output.replaceAll("\u001B\\[[;\\d]*m", "");
        int paddingTotal = maxOutput - cleanOutput.length();
        int paddingLeft = paddingTotal / 2;
        int paddingRight = paddingTotal - paddingLeft;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paddingLeft; i++) sb.append(' ');
        sb.append(output);
        for (int i = 0; i < paddingRight; i++) sb.append(' ');

        String formatLeft = String.format("| %%-%ds | %%-%ds | %%-%ds |%n", maxStack, maxInput, maxOutput);
        System.out.printf(formatLeft, stack, input, sb.toString());
    }

    public static void main(String[] args) {
        Parser parser = new Parser("recipe f() { pasta n = 5g~ serve(n)~ } recipe main m() { chef.f()~ }");
        parser.Parseo();
    }
}
