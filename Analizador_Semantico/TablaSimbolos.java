package Analizador_Semantico;

import Errores.E017;
import Errores.ErrorSemantico;

import java.util.ArrayList;
import java.util.List;

public class TablaSimbolos {
    private List<Simbolo> simbolos = new ArrayList<>();

    public void agregarSimbolo(Simbolo simbolo) {
        simbolos.add(simbolo);
    }

    public Simbolo buscar(String nombre, String ambito) {
        for (Simbolo s : simbolos) {
            if (s.getValor().equals(nombre) &&
                    s.getAmbito().equals(ambito)) {
                return s;
            }
        }
        return null;
    }

    public List<Simbolo> getSimbolos() {
        return simbolos;
    }
}
