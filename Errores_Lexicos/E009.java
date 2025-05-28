package Errores_Lexicos;

public class E009 extends ErrorLexico {
    public E009(int posicion) {
        super("E009", "Valor inválido para pasta", posicion, "Valor no entero. El valor debe ser un número entero.");
    }
}
