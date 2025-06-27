package Errores;

public class E012 extends ErrorLexico {
    public E012(int posicion) {
        super("E012", "Falta comilla de apertura", posicion, "Cadena sin comilla inicial. Agrega una comilla doble antes de la cadena.");
    }
}
