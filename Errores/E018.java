package Errores;

public class E018 extends ErrorSemantico {
    public E018(int posicion, String valor) {
            super("E018", "Variable no declarada", posicion, "Imprimiendo variable no inicializada: Inicie la variable " + valor);
    }
}
