package Errores_Lexicos;

public class E003 extends ErrorLexico {
    public E003(int posicion) {
        super("E003", "Nombre inválido de variable/función", posicion,
                "Identificador inválido: usa un nombre que no comience con números ni tenga símbolos inválidos.");
    }
}
