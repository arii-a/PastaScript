package Errores;

public class E001 extends ErrorLexico {
    public E001(int posicion) {
        super("E001", "Tipo de variable inválido", posicion,
                "Tipo no reconocido: utiliza un tipo de variable válido: pasta, sauce, basil.");
    }
}
