package BackEnd;

/**
Barrutieta Nulutagua Juan Alexadner
Alcala Bejarano Jennifer Paola
 */

/**
 * Clase base de la jerarquía de la Biblioteca.
 * Libro, Revista y Articulo heredan de aquí y sobrescriben toString().
 */
public class Trabajo {

    private String titulo;
    private String autor;
    private int anioPublicacion;

    public Trabajo(String titulo, String autor, int anioPublicacion) {
        setTitulo(titulo);
        setAutor(autor);
        setAnioPublicacion(anioPublicacion);
    }

    public String getTitulo() {
        return titulo;
    }

    public final void setTitulo(String titulo) {
        this.titulo = validarTexto(titulo, "El título");
    }

    public String getAutor() {
        return autor;
    }

    public final void setAutor(String autor) {
        this.autor = validarTexto(autor, "El autor");
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public final void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = validarPositivo(anioPublicacion, "El año de publicación");
    }

    /** Nombre corto del tipo; cada subclase lo sobrescribe. */
    public String getTipo() {
        return "Trabajo";
    }

    @Override
    public String toString() {
        return "Título: " + titulo
                + " | Autor: " + autor
                + " | Año: " + anioPublicacion;
    }

    // ---------- validaciones compartidas por las subclases ----------

    protected static String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
        return valor.trim();
    }

    protected static int validarPositivo(int valor, String campo) {
        if (valor <= 0) {
            throw new IllegalArgumentException(campo + " debe ser un entero mayor a cero.");
        }
        return valor;
    }
}
