package Errores;

public class E004 extends ErrorSintactico {
    public E004(int posicion) {
        super("E004", "Falta paréntesis de apertura", posicion,
                "Función sin ( después del nombre: grega ( después del nombre de la función.");
    }
}
