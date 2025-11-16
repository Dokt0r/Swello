package es.ucm.fdi.pad.swello.PlayaAdapter;

public class ItemPlaya {

    private String id;
    private String nombre;
    private double alturaOla;
    private String direccionOla;
    private double distancia;

    public ItemPlaya(String id, String nombre, double alturaOla, String direccionOla, double distancia) {
        this.id = id;
        this.nombre = nombre;
        this.alturaOla = alturaOla;
        this.direccionOla = direccionOla;
        this.distancia = distancia;
    }

    // --- Getters ---
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getAlturaOla() { return alturaOla; }
    public String getDireccionOla() { return direccionOla; }
    public double getDistancia() { return distancia; }

    // --- Setters ---
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setAlturaOla(double alturaOla) { this.alturaOla = alturaOla; }
    public void setDireccionOla(String direccionOla) { this.direccionOla = direccionOla; }
    public void setDistancia(double distancia) { this.distancia = distancia; }

    @Override
    public String toString() {
        return "ItemPlaya{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", alturaOla=" + alturaOla +
                ", direccionOla='" + direccionOla + '\'' +
                ", distancia=" + distancia +
                '}';
    }
}
