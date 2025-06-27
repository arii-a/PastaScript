package Errores;

public class E013 extends ErrorLexico {
    public E013(int posicion) {
        super("E013", "Falta comilla de cierre", posicion, "Cadena sin comilla final. Agrega una comilla doble al final de la cadena.");
    }
}
