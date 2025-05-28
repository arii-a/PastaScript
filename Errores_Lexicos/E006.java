package Errores_Lexicos;

public class E006 extends ErrorLexico {
    public E006(int posicion) {
        super("E006", "Falta llave de apertura", posicion,
                "Cuerpo de función sin {. Agrega { para abrir el cuerpo de la función.");
    }
}
