package BackEnd;

/**
Barrutieta Nulutagua Juan Alexadner
Alcala Bejarano Jennifer Paola
 */
public class Revista extends Trabajo {

    private int numeroEdicion;

    public Revista(String titulo, String autor, int anioPublicacion, int numeroEdicion) {
        super(titulo, autor, anioPublicacion);
        setNumeroEdicion(numeroEdicion);
    }

    public int getNumeroEdicion() {
        return numeroEdicion;
    }

    public final void setNumeroEdicion(int numeroEdicion) {
        this.numeroEdicion = validarPositivo(numeroEdicion, "El número de edición");
    }

    @Override
    public String getTipo() {
        return "Revista";
    }

    @Override
    public String toString() {
        return "[Revista] " + super.toString() + " | Edición: " + numeroEdicion;
    }
}
