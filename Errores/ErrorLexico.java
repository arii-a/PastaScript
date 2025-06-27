package Errores;

public class ErrorLexico {
    protected String idError;
    protected String tipo;

    public void setIdError(String idError) {
        this.idError = idError;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    protected int posicion;
    protected String descripcion;

    public String getIdError() {
        return idError;
    }

    public String getTipo() {
        return tipo;
    }

    public int getPosicion() {
        return posicion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public ErrorLexico(String idError, String tipo, int posicion, String descripcion) {
        this.idError = idError;
        this.tipo = tipo;
        this.posicion = posicion;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return String.format("Error(id=%s, tipo=%s, pos=%d, desc=%s)", idError, tipo, posicion, descripcion);
    }
}

