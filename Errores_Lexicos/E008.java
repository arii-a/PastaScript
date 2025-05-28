package Errores_Lexicos;

public class E008 extends ErrorLexico {
    public E008(int posicion) {
        super("E008", "Falta operador de asignación", posicion, "No se usó = para asignar. Usa = para asignar valores a variables.");
    }
}
