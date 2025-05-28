package Errores_Lexicos;

public class E010 extends ErrorLexico {
    public E010(int posicion) {
        super("E010", "Falta unidad g en pasta", posicion, "No se incluyó unidad de medida. Agrega g al final del valor numérico.");
    }
}