package BackEnd;

/**
Barrutieta Nulutagua Juan Alexadner
Alcala Bejarano Jennifer Paola
 */
public class Libro extends Trabajo {

    private int numeroPaginas;

    public Libro(String titulo, String autor, int anioPublicacion, int numeroPaginas) {
        super(titulo, autor, anioPublicacion);
        setNumeroPaginas(numeroPaginas);
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public final void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = validarPositivo(numeroPaginas, "El número de páginas");
    }

    @Override
    public String getTipo() {
        return "Libro";
    }

    @Override
    public String toString() {
        return "[Libro] " + super.toString() + " | Páginas: " + numeroPaginas;
    }
}
