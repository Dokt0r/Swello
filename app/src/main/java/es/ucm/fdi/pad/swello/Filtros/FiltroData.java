package es.ucm.fdi.pad.swello.Filtros;

import java.io.Serializable;

public class FiltroData implements Serializable {
    public float distanciaMinima = 0;
    public float distanciaMaxima = 200;
    public float tamanoMinimo = 0.5f;
    public float tamanoMaximo = 5.0f;

    public String direccionOlas = "";
    public String tempAgua = "";

    public String ordenacion = "";

    @Override
    public String toString() {
        return "FiltroData{" +
                "distancia=" + distanciaMinima + "-" + distanciaMaxima +
                ", tamano=" + tamanoMinimo + "-" + tamanoMaximo +
                ", direccionOlas='" + direccionOlas + '\'' +
                ", tempAgua='" + tempAgua + '\'' +
                ", ordenacion='" + ordenacion + '\'' +
                '}';
    }
}
