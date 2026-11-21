package BackEnd;

/**
Barrutieta Nulutagua Juan Alexadner
Alcala Bejarano Jennifer Paola
 */
public class Articulo extends Trabajo {

    private String nombreConferencia;

    public Articulo(String titulo, String autor, int anioPublicacion, String nombreConferencia) {
        super(titulo, autor, anioPublicacion);
        setNombreConferencia(nombreConferencia);
    }

    public String getNombreConferencia() {
        return nombreConferencia;
    }

    public final void setNombreConferencia(String nombreConferencia) {
        this.nombreConferencia = validarTexto(nombreConferencia, "El nombre de la conferencia");
    }

    @Override
    public String getTipo() {
        return "Articulo";
    }

    @Override
    public String toString() {
        return "[Artículo] " + super.toString() + " | Conferencia: " + nombreConferencia;
    }
}
