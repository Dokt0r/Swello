package es.ucm.fdi.pad.swello;

public class ItemPlaya {

    private String nombre;         // Nombre de la playa
    private double alturaOla;      // Altura media de la ola (m)
    private String direccionOla;   // Dirección (N, NE, E, etc.)
    private double distancia;      // Distancia (km)
    private String descripcion;    // Descripción opcional

    public ItemPlaya(String nombre, double alturaOla, String direccionOla, double distancia, String descripcion) {
        this.nombre = nombre;
        this.alturaOla = alturaOla;
        this.direccionOla = direccionOla;
        this.distancia = distancia;
        this.descripcion = descripcion;
    }

    // --- Getters ---
    public String getNombre() { return nombre; }
    public double getAlturaOla() { return alturaOla; }
    public String getDireccionOla() { return direccionOla; }
    public double getDistancia() { return distancia; }
    public String getDescripcion() { return descripcion; }

    // --- Setters ---
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setAlturaOla(double alturaOla) { this.alturaOla = alturaOla; }
    public void setDireccionOla(String direccionOla) { this.direccionOla = direccionOla; }
    public void setDistancia(double distancia) { this.distancia = distancia; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return "ItemPlaya{" +
                "nombre='" + nombre + '\'' +
                ", alturaOla=" + alturaOla +
                ", direccionOla='" + direccionOla + '\'' +
                ", distancia=" + distancia +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
