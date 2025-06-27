package Errores;

public class E017 extends ErrorSemantico {
    public E017(String variable, int posicion) {
        super("E017", "Variable repetida", posicion, "Variable doblemente inicializada: " +
                "Renombre alguna de las variables " + variable);
    }
}
