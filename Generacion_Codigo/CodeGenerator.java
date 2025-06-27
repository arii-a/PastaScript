package Generacion_Codigo;

import Analizador_Semantico.Simbolo;


import java.util.List;

public class CodeGenerator {

    private List<Simbolo> simbolos;
    private StringBuilder codigo = new StringBuilder();
    private boolean dentroDeMain = false;
    private int indent = 0;

    public CodeGenerator(List<Simbolo> simbolos) {
        this.simbolos = simbolos;
    }

    public String generarDesdeLineas(List<String> lineas) {
        System.out.println(simbolos);
        codigo.append("public class ProgramaGenerado {\n");
        indent++;

        for (String linea : lineas) {
            procesarLinea(linea.trim());
        }

        indent--;
        codigo.append("}\n");
        return codigo.toString();
    }

    private void procesarLinea(String linea) {
        if (linea.startsWith("recipe")) {
            boolean esMain = linea.startsWith("recipe main");
            String nombre = "";

            if(esMain) {
                nombre = extraerEntre(linea, "main", "(").trim();
            } else {
                nombre = extraerEntre(linea, "recipe", "(").trim();
            }

            String parametros = extraerEntre(linea, "(", ")").trim();

            String tipoParam = "";
            String nombreParam = "";

            if (!parametros.isEmpty()) {
                String[] tipoNombre = parametros.split(" ");
                tipoParam = convertirTipo(tipoNombre[0]);
                nombreParam = tipoNombre[1];
            }

            if (esMain) {
                appendLinea("public static void main(String[] args) {");
                dentroDeMain = true;
            } else {
                appendLinea("public static void " + nombre + "(" + tipoParam + " " + nombreParam + ") {");
            }
            indent++;

            // Si contiene bloque dentro, extraerlo
            if (linea.contains("{") && linea.contains("}")) {
                String dentroLlaves = extraerEntre(linea, "{", "}").trim();
                procesarBloqueInterno(dentroLlaves);
                indent--;
                appendLinea("}");
                if (esMain) dentroDeMain = false;
            }
        } else if (linea.startsWith("pasta") || linea.startsWith("sauce") || linea.startsWith("basil")) {
            String[] partes = linea.replace("~", "").split("=");
            String[] tipoNombre = partes[0].trim().split(" ");
            String tipoJava = convertirTipo(tipoNombre[0]);
            String nombre = tipoNombre[1];
            String valor = limpiarLiteral(partes[1].trim());
            String modifier = estaEnAmbitoGlobal(String.valueOf(nombre.charAt(0))) ? "static " : "";
            appendLinea(modifier + tipoJava + " " + nombre + " = " + valor + ";");
        }
    }

    private void procesarBloqueInterno(String bloque) {
        String[] instrucciones = bloque.split("~");
        for (String instr : instrucciones) {
            instr = instr.trim();
            if (instr.startsWith("serve(")) {
                String expr = extraerEntre(instr, "serve(", ")").trim();
                appendLinea("System.out.println(" + expr + ");");
            } else if (instr.startsWith("chef.")) {
                String llamada = instr.substring("chef.".length()).replace("~", "").trim();
                appendLinea((dentroDeMain ? "" : "this.") + llamada + ";");
            } else if (instr.startsWith("pasta") || instr.startsWith("sauce") || instr.startsWith("basil")) {
                String[] partes = instr.replace("~", "").split("=");
                String[] tipoNombre = partes[0].trim().split(" ");
                String tipoJava = convertirTipo(tipoNombre[0]);
                String nombre = tipoNombre[1];
                String valor = limpiarLiteral(partes[1].trim());
                String modifier = estaEnAmbitoGlobal(String.valueOf(nombre.charAt(0))) ? "static " : "";
                appendLinea(modifier + tipoJava + " " + nombre + " = " + valor + ";");
            }
        }
    }

    private boolean estaEnAmbitoGlobal(String nombre) {
        for (Simbolo s : simbolos) {
            if (s.getValor().equals(nombre)) {
                System.out.println("simbolo analisis: " + s + ", " + s.getAmbito());
                return s.getAmbito().equals("global");
            }
        }
        return false;
    }

    private String convertirTipo(String tipo) {
        return switch (tipo) {
            case "pasta" -> "int";
            case "sauce" -> "String";
            case "basil" -> "boolean";
            default -> "Object";
        };
    }

    private String limpiarLiteral(String valor) {
        if (valor.equals("cooked")) return  "true";
        if (valor.equals("burned")) return "false";
        if (valor.endsWith("g")) return valor.replace("g", "");
        return valor;
    }

    private void appendLinea(String linea) {
        for (int i = 0; i < indent; i++) codigo.append("    ");
        codigo.append(linea).append("\n");
    }

    private String extraerEntre(String linea, String ini, String fin) {
        int i = linea.indexOf(ini) + ini.length();
        int f = linea.indexOf(fin);
        return linea.substring(i, f);
    }

}