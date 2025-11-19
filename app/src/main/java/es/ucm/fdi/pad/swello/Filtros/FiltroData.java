package es.ucm.fdi.pad.swello.Filtros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FiltroData implements Serializable {
    public float distanciaMinima = 0;
    public float distanciaMaxima = 120;
    public float tamanoMinimo = 0.5f;
    public float tamanoMaximo = 5.0f;
    public float periodoMinimo = 5f;
    public float periodoMaximo = 12f;

    public String direccionOlas = "";
    public String tempAgua = "";
    public String nivelSurfista = "";
    public List<String> servicios = new ArrayList<>();

    @Override
    public String toString() {
        return "FiltroData{" +
                "distancia=" + distanciaMinima + "-" + distanciaMaxima +
                ", tamano=" + tamanoMinimo + "-" + tamanoMaximo +
                ", periodo=" + periodoMinimo + "-" + periodoMaximo +
                ", direccionOlas='" + direccionOlas + '\'' +
                ", tempAgua='" + tempAgua + '\'' +
                ", nivelSurfista='" + nivelSurfista + '\'' +
                ", servicios=" + servicios +
                '}';
    }
}
