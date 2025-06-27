package Errores;

public class E016 extends ErrorLexico {
    public E016(int posicion, char caracterInesperado) {
        super("E016", "Carácter no válido", posicion, "Carácter inesperado: '" + caracterInesperado + "'. Reportar y eliminar.");
    }
}