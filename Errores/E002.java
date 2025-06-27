package Errores;

public class E002 extends ErrorSintactico {
    public E002(int posicion) {
        super("E002", "Falta tipo de variable", posicion,
                "Declaración sin tipo previo: agrega un tipo de variable como pasta, sauce o basil.");
    }
}
