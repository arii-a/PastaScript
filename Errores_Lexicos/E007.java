package Errores_Lexicos;

public class E007 extends ErrorLexico {
    public E007(int posicion) {
        super("E007", "Falta llave de cierre", posicion,
                "Cuerpo de función sin }. Cierra el cuerpo con }.");
    }
}
