package es.ucm.fdi.pad.swello.PlayaAdapter;

import androidx.annotation.Nullable;
import android.util.Log;
import java.util.Arrays;

public class ItemPlaya {
    private String id;
    private String nombre;
    private String descripcion;
    private double valoracion;
    private double latitud;
    private double longitud;
    private double alturaOla;
    private double periodoOla;
    private String direccionOla;
    private double tempAgua;
    private double oceanCurrentVelocity;
    private String oceanCurrentDirection;
    private double valoracionMedia;
    private String[] imagenes;
    private double distancia;
    private String imagenPrincipal;

    public ItemPlaya(String id, String nombre, String descripcion, double valoracion,
                     double latitud, double longitud, double alturaOla, double periodoOla,
                     String direccionOla, double tempAgua, double oceanCurrentVelocity,
                     String oceanCurrentDirection, double valoracionMedia,
                     @Nullable String[] imagenes, double distancia, String imagenPrincipal) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.valoracion = valoracion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.alturaOla = alturaOla;
        this.periodoOla = periodoOla;
        this.direccionOla = direccionOla;
        this.tempAgua = tempAgua;
        this.oceanCurrentVelocity = oceanCurrentVelocity;
        this.oceanCurrentDirection = oceanCurrentDirection;
        this.valoracionMedia = valoracionMedia;

        // Aseguramos que siempre haya un array no nulo
        this.imagenes = (imagenes != null) ? imagenes : new String[0];
        this.distancia = distancia;
        this.imagenPrincipal = imagenPrincipal;
    }

    // GETTERS
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getValoracion() { return valoracion; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
    public double getAlturaOla() { return alturaOla; }
    public double getPeriodoOla() { return periodoOla; }
    public String getDireccionOla() { return direccionOla; }
    public double getTempAgua() { return tempAgua; }
    public double getOceanCurrentVelocity() { return oceanCurrentVelocity; }
    public String getOceanCurrentDirection() { return oceanCurrentDirection; }
    public double getValoracionMedia() { return valoracionMedia; }
    public String[] getImagenes() { return imagenes; }
    public double getDistancia() { return distancia; }
    public String getImagenPrincipal() { return imagenPrincipal; }

    public void setDistancia(double distancia) { this.distancia = distancia; }

    /**
     * Devuelve la URL de la imagen principal del ItemPlaya.
     * Prioriza el primer elemento de 'imagenes', si no existe devuelve 'imagenPrincipal'.
     */
    public String getImagenUrl() {
        String url = null;

        if (imagenes != null && imagenes.length > 0 && imagenes[0] != null && !imagenes[0].isEmpty()) {
            url = imagenes[0];
        } else if (imagenPrincipal != null && !imagenPrincipal.isEmpty()) {
            url = imagenPrincipal;
        }

        Log.d("ItemPlaya", "getImagenUrl() para " + nombre + " = " + url + " | imagenes=" + Arrays.toString(imagenes) + " | imagenPrincipal=" + imagenPrincipal);
        return url;
    }
}
