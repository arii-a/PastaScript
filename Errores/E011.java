package Errores;

public class E011 extends ErrorLexico {
    public E011(int posicion) {
        super("E011", "Valor inválido para sauce", posicion, "Valor no es una cadena. Usa comillas dobles para definir una cadena.");
    }
}