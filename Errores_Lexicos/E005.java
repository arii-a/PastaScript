package Errores_Lexicos;

public class E005 extends ErrorLexico {
    public E005(int posicion) {
        super("E005", "Falta paréntesis de cierre", posicion,
                "Falta ) al cerrar lista de parámetros: cierra los parámetros con ).");
    }
}
