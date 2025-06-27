package Analizador_Semantico;

import java.util.HashMap;
import java.util.Map;

public class Simbolo {
    protected String ambito;
    protected String tipo;
    protected String valor;
    protected boolean entrada;

    public String getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    public String getAmbito() {
        return ambito;
    }

    public boolean isEntrada() {
        return entrada;
    }

    public String toString() {
        return String.format("Simbolo(tipo=%s, valor=%s, ambito=%s, entrada=%s)", tipo, valor, ambito, entrada);
    }

    public Simbolo(String tipo, String valor, String ambito, boolean entrada) {
        this.valor = valor;
        this.tipo = tipo;
        this.ambito = ambito;
        this.entrada = entrada;
    }

    public Simbolo(String tipo, String valor, String ambito) {
        this.valor = valor;
        this.tipo = tipo;
        this.ambito = ambito;
    }

}
