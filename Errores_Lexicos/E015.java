package Errores_Lexicos;

public class E015 extends ErrorLexico {
    public E015(int posicion) {
        super("E015", "Falta cierre de línea ~", posicion, "Instrucción no finalizada con tilde. Finaliza la instrucción con ~.");
    }
}
