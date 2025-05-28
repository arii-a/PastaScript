package Analizador_Lexico;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Token {
    protected String tipo;
    protected String valor;
    protected int posicion;

    public String getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    public int getPosicion() {
        return posicion;
    }

    public String getTokens(){
        return SYMBOLS.getOrDefault(tipo, "TOKEN DESCONOCIDO");
    }

    private static final Map<String, String> SYMBOLS;

    static {
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("pasta", "PALABRA CLAVE");
        tokenMap.put("sauce", "PALABRA CLAVE");
        tokenMap.put("basil", "PALABRA CLAVE");
        tokenMap.put("recipe", "PALABRA CLAVE");
        tokenMap.put("ID", "IDENTIFICADOR");
        tokenMap.put("igual", "OPERADOR DE ASIGNACIÓN");
        tokenMap.put("entero", "NÚMERO ENTERO");
        tokenMap.put("unidad", "UNIDAD DE MEDIDA");
        tokenMap.put("estado_true", "BOOLEANO");
        tokenMap.put("estado_false", "BOOLEANO");
        tokenMap.put("string", "CADENA");
        tokenMap.put("comilla", "COMILLA DOBLE");
        tokenMap.put("end_line", "FIN DE INSTRUCCIÓN"); // para '~'
        tokenMap.put("parentesis_apertura", "PARÉNTESIS DE APERTURA");
        tokenMap.put("parentesis_cierre", "PARÉNTESIS DE CIERRE");
        tokenMap.put("llave_apertura", "LLAVE DE APERTURA");
        tokenMap.put("llave_cierre", "LLAVE DE CIERRE");

        SYMBOLS = Collections.unmodifiableMap(tokenMap);
    }

    public Token(String tipo, String valor, int posicion) {
        this.tipo = tipo;
        this.valor = valor;
        this.posicion = posicion;
    }

    @Override
    public String toString() {
        return String.format("Token(tipo=%s, valor=%s, pos=%d)", tipo, valor, posicion);
    }


}