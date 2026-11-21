package FrontEnd;

import BackEnd.Trabajo;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;

/**
Barrutieta Nulutagua Juan Alexadner
Alcala Bejarano Jennifer Paola
 */

/**
 * Dibuja la lista circular como un anillo de nodos.
 * Solo recibe una copia de los datos (lista.aLista() y el índice actual);
 * no conoce ni modifica los enlaces reales.
 * Aparece en la paleta de NetBeans como componente personalizado.
 */
public class PanelCircular extends JComponent {

    private static final Color FONDO = new Color(250, 250, 252);
    private static final Color TEXTO = new Color(40, 44, 52);
    private static final Color TEXTO_SUAVE = new Color(110, 116, 128);
    private static final Color ENLACE = new Color(140, 146, 160);
    private static final Color CIERRE = new Color(200, 60, 60);
    private static final Color ACTUAL = new Color(255, 214, 102);
    private static final Color CABEZA = new Color(45, 55, 72);
    private static final Color LIBRO = new Color(52, 110, 200);
    private static final Color REVISTA = new Color(40, 150, 90);
    private static final Color ARTICULO = new Color(220, 120, 30);

    private List<Trabajo> trabajos = Collections.emptyList();
    private int indiceActual = -1;

    public PanelCircular() {
        setPreferredSize(new Dimension(560, 440));
        setMinimumSize(new Dimension(320, 260));
        setOpaque(true);
    }

    /** Recibe la secuencia en orden (una vuelta) y la posición del cursor. */
    public void setDatos(List<? extends Trabajo> datos, int indiceActual) {
        this.trabajos = new ArrayList<>(datos);
        this.indiceActual = indiceActual;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        g.setColor(FONDO);
        g.fillRect(0, 0, w, h);

        int altoLeyenda = 30;
        int areaAlto = h - altoLeyenda;

        if (trabajos.isEmpty()) {
            dibujarVacia(g, w, areaAlto);
        } else {
            dibujarAnillo(g, w, areaAlto);
        }
        dibujarLeyenda(g, w, h - altoLeyenda + 6);
        g.dispose();
    }

    // ---------------------------------------------------------------

    private void dibujarVacia(Graphics2D g, int w, int h) {
        g.setColor(TEXTO_SUAVE);
        g.setFont(getFont().deriveFont(Font.BOLD, 16f));
        centrarTexto(g, "Lista circular vacía", w / 2, h / 2 - 8);
        g.setFont(getFont().deriveFont(Font.PLAIN, 13f));
        centrarTexto(g, "cabeza = null   ·   actual = null   ·   tamaño = 0", w / 2, h / 2 + 16);
    }

    private void dibujarAnillo(Graphics2D g, int w, int h) {
        int n = trabajos.size();
        int anchoNodo = n <= 8 ? 124 : (n <= 14 ? 96 : 72);
        int altoNodo = n <= 8 ? 46 : (n <= 14 ? 40 : 34);

        double cx = w / 2.0;
        double cy = h / 2.0 + 6;
        double radio = Math.max(40, Math.min(w - anchoNodo, h - altoNodo - 40) / 2.0 - 16);

        Point2D.Double[] centros = new Point2D.Double[n];
        for (int i = 0; i < n; i++) {
            double ang = -Math.PI / 2 + 2 * Math.PI * i / n; // empieza arriba, sentido horario
            centros[i] = new Point2D.Double(cx + radio * Math.cos(ang), cy + radio * Math.sin(ang));
        }

        // Texto central
        g.setColor(TEXTO_SUAVE);
        g.setFont(getFont().deriveFont(Font.BOLD, 14f));
        centrarTexto(g, n + (n == 1 ? " nodo" : " nodos"), (int) cx, (int) cy - 6);
        g.setFont(getFont().deriveFont(Font.PLAIN, 12f));
        centrarTexto(g, "siguiente → sentido horario", (int) cx, (int) cy + 14);

        // Enlaces
        if (n == 1) {
            dibujarAutoEnlace(g, centros[0], anchoNodo, altoNodo);
        } else {
            for (int i = 0; i < n; i++) {
                int j = (i + 1) % n;
                boolean esCierre = (j == 0);
                Point2D a = puntoBorde(centros[i], anchoNodo, altoNodo, centros[j]);
                Point2D b = puntoBorde(centros[j], anchoNodo, altoNodo, centros[i]);
                dibujarEnlaceDoble(g, a, b, esCierre);
            }
        }

        // Nodos
        for (int i = 0; i < n; i++) {
            dibujarNodo(g, trabajos.get(i), i, centros[i], anchoNodo, altoNodo);
        }
    }

    private void dibujarNodo(Graphics2D g, Trabajo t, int i, Point2D c, int bw, int bh) {
        int x = (int) Math.round(c.getX() - bw / 2.0);
        int y = (int) Math.round(c.getY() - bh / 2.0);
        boolean esActual = (i == indiceActual);
        boolean esCabeza = (i == 0);

        g.setColor(esActual ? ACTUAL : Color.WHITE);
        g.fillRoundRect(x, y, bw, bh, 12, 12);
        g.setColor(colorTipo(t));
        g.setStroke(new BasicStroke(esCabeza || esActual ? 3f : 2f));
        g.drawRoundRect(x, y, bw, bh, 12, 12);

        g.setColor(TEXTO);
        g.setFont(getFont().deriveFont(Font.BOLD, 11f));
        g.drawString(recortar(g, "[" + i + "] " + t.getTipo(), bw - 12), x + 6, y + 16);
        g.setFont(getFont().deriveFont(Font.PLAIN, 11f));
        g.drawString(recortar(g, t.getTitulo(), bw - 12), x + 6, y + bh - 10);

        // Etiquetas encima del nodo
        String etiqueta = null;
        if (esCabeza && esActual) {
            etiqueta = "CABEZA · ACTUAL";
        } else if (esCabeza) {
            etiqueta = "CABEZA";
        } else if (esActual) {
            etiqueta = "ACTUAL";
        }
        if (etiqueta != null) {
            g.setFont(getFont().deriveFont(Font.BOLD, 10f));
            FontMetrics fm = g.getFontMetrics();
            int tw = fm.stringWidth(etiqueta) + 12;
            int tx = (int) Math.round(c.getX() - tw / 2.0);
            int ty = y - 18;
            g.setColor(esCabeza ? CABEZA : new Color(180, 130, 0));
            g.fillRoundRect(tx, ty, tw, 15, 15, 15);
            g.setColor(Color.WHITE);
            g.drawString(etiqueta, tx + 6, ty + 11);
        }
    }

    private void dibujarEnlaceDoble(Graphics2D g, Point2D a, Point2D b, boolean esCierre) {
        Stroke original = g.getStroke();
        if (esCierre) {
            g.setColor(CIERRE);
            g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                    1f, new float[]{6f, 4f}, 0f));
        } else {
            g.setColor(ENLACE);
            g.setStroke(new BasicStroke(1.6f));
        }
        g.drawLine((int) a.getX(), (int) a.getY(), (int) b.getX(), (int) b.getY());
        g.setStroke(new BasicStroke(1.6f));
        punta(g, a, b); // siguiente
        punta(g, b, a); // anterior
        g.setStroke(original);
    }

    private void dibujarAutoEnlace(Graphics2D g, Point2D c, int bw, int bh) {
        int x = (int) (c.getX() + bw / 2.0 - 18);
        int y = (int) (c.getY() - bh / 2.0 - 34);
        g.setColor(CIERRE);
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1f, new float[]{6f, 4f}, 0f));
        g.drawArc(x, y, 44, 44, -60, 300);
        g.setFont(getFont().deriveFont(Font.PLAIN, 11f));
        g.drawString("se apunta a sí mismo", x + 50, y + 18);
    }

    private void punta(Graphics2D g, Point2D desde, Point2D hacia) {
        double ang = Math.atan2(hacia.getY() - desde.getY(), hacia.getX() - desde.getX());
        double largo = 9;
        double abre = Math.toRadians(24);
        int[] xs = {
            (int) hacia.getX(),
            (int) (hacia.getX() - largo * Math.cos(ang - abre)),
            (int) (hacia.getX() - largo * Math.cos(ang + abre))
        };
        int[] ys = {
            (int) hacia.getY(),
            (int) (hacia.getY() - largo * Math.sin(ang - abre)),
            (int) (hacia.getY() - largo * Math.sin(ang + abre))
        };
        g.fillPolygon(xs, ys, 3);
    }

    /** Punto del borde del rectángulo (centro c) en dirección hacia "destino", con un pequeño margen. */
    private Point2D puntoBorde(Point2D c, int bw, int bh, Point2D destino) {
        double dx = destino.getX() - c.getX();
        double dy = destino.getY() - c.getY();
        if (dx == 0 && dy == 0) {
            return c;
        }
        double sx = dx == 0 ? Double.MAX_VALUE : (bw / 2.0 + 4) / Math.abs(dx);
        double sy = dy == 0 ? Double.MAX_VALUE : (bh / 2.0 + 4) / Math.abs(dy);
        double s = Math.min(sx, sy);
        return new Point2D.Double(c.getX() + dx * s, c.getY() + dy * s);
    }

    private void dibujarLeyenda(Graphics2D g, int w, int y) {
        g.setFont(getFont().deriveFont(Font.PLAIN, 11f));
        String[] textos = {"Libro", "Revista", "Artículo", "Actual", "Enlace de cierre (último ⇄ cabeza)"};
        Color[] colores = {LIBRO, REVISTA, ARTICULO, ACTUAL, CIERRE};
        FontMetrics fm = g.getFontMetrics();
        int total = 0;
        for (String t : textos) {
            total += 16 + fm.stringWidth(t) + 14;
        }
        int x = Math.max(8, (w - total) / 2);
        for (int i = 0; i < textos.length; i++) {
            g.setColor(colores[i]);
            g.fillRoundRect(x, y + 2, 11, 11, 3, 3);
            g.setColor(TEXTO_SUAVE);
            g.drawString(textos[i], x + 16, y + 12);
            x += 16 + fm.stringWidth(textos[i]) + 14;
        }
    }

    private Color colorTipo(Trabajo t) {
        switch (t.getTipo()) {
            case "Libro":
                return LIBRO;
            case "Revista":
                return REVISTA;
            case "Articulo":
                return ARTICULO;
            default:
                return ENLACE;
        }
    }

    private String recortar(Graphics2D g, String texto, int ancho) {
        FontMetrics fm = g.getFontMetrics();
        if (fm.stringWidth(texto) <= ancho) {
            return texto;
        }
        String puntos = "…";
        int fin = texto.length();
        while (fin > 0 && fm.stringWidth(texto.substring(0, fin) + puntos) > ancho) {
            fin--;
        }
        return texto.substring(0, fin) + puntos;
    }

    private void centrarTexto(Graphics2D g, String texto, int cx, int baseY) {
        FontMetrics fm = g.getFontMetrics();
        g.drawString(texto, cx - fm.stringWidth(texto) / 2, baseY);
    }
}
