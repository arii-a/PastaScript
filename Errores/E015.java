package Errores;

public class E015 extends ErrorSintactico {
    public E015(int posicion) {
        super("E015", "Falta cierre de línea ~", posicion, "Instrucción no finalizada con tilde. Finaliza la instrucción con ~.");
    }
}
