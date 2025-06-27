package Errores;

public class E006 extends ErrorSintactico {
    public E006(int posicion) {
        super("E006", "Falta llave de apertura", posicion,
                "Cuerpo de función sin {. Agrega { para abrir el cuerpo de la función.");
    }
}
