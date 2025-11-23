package es.ucm.fdi.pad.swello.PlayaAdapter;

public class ItemPlaya {

    private String id;
    private String nombre;
    private double alturaOla;
    private String direccionOla;
    private double distancia;
    private double latitud;
    private double longitud;
    private String imagenUrl;
    private double tempAgua;

    public ItemPlaya(String id, String nombre, double alturaOla, String direccionOla,
                     double distancia, double latitud, double longitud,
                     String imagenUrl, double tempAgua) {
        this.id = id;
        this.nombre = nombre;
        this.alturaOla = alturaOla;
        this.direccionOla = direccionOla;
        this.distancia = distancia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagenUrl = imagenUrl;
        this.tempAgua = tempAgua;
    }

    // --- Getters ---
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getAlturaOla() { return alturaOla; }
    public String getDireccionOla() { return direccionOla; }
    public double getDistancia() { return distancia; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
    public String getImagenUrl() { return imagenUrl; }
    public double getTempAgua() { return tempAgua; }

    // --- Setters ---
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setAlturaOla(double alturaOla) { this.alturaOla = alturaOla; }
    public void setDireccionOla(String direccionOla) { this.direccionOla = direccionOla; }
    public void setDistancia(double distancia) { this.distancia = distancia; }
    public void setLatitud(double latitud) { this.latitud = latitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public void setTempAgua(double tempAgua) { this.tempAgua = tempAgua; }

    @Override
    public String toString() {
        return "ItemPlaya{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", alturaOla=" + alturaOla +
                ", direccionOla='" + direccionOla + '\'' +
                ", distancia=" + distancia +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", tempAgua=" + tempAgua +
                '}';
    }
}
