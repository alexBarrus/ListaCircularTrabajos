# Actividad 4.3 — Lista Circular de Trabajos de Biblioteca

Lista Circular Doblemente Enlazada genérica (`ListaCircularTrabajos<T extends Trabajo>`) para administrar
`Libro`, `Revista` y `Articulo`, con interfaz gráfica Swing diseñada en el GUI Builder de NetBeans.

**Equipo:** Barrutieta Nulutagua Juan Alexadner · Alcala Bejarano Jennifer Paola

## Estructura

| Paquete | Clases |
|---|---|
| `BackEnd` | `Trabajo`, `Libro`, `Revista`, `Articulo`, `Nodo<T>`, `ListaCircularTrabajos<T>` |
| `FrontEnd` | `GUIListaCircular` (+ `.form`), `PanelCircular` (dibujo del anillo) |
| `Pruebas` | `PruebasListaCircular` (pruebas del punto 6 por consola) |

## Ejecutar

1. NetBeans → File → Open Project → esta carpeta.
2. **Clean and Build** (necesario para que el diseñador muestre `PanelCircular`).
3. **Run** abre la ventana. Para las pruebas: clic derecho en `PruebasListaCircular.java` → Run File.

Las posiciones empiezan en 0: insertar acepta `0 ≤ pos ≤ tamaño`, eliminar `0 ≤ pos < tamaño`.
