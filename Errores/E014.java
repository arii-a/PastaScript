package Errores;

public class E014 extends ErrorLexico {
    public E014(int posicion) {
        super("E014", "Valor inválido para basil", posicion, "Valor no booleano. Usa valores válidos: cooked, burned.");
    }
}
