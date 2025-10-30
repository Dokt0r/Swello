package es.ucm.fdi.pad.swello.Filtros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FiltroData implements Serializable {

    public List<String> tiposOla = new ArrayList<>();
    public float tamanoMinimo = 0.5f;
    public float tamanoMaximo = 5.0f;
    public String direccionViento = "";
    public String nivelSurfista = "";
    public List<String> servicios = new ArrayList<>();

    @Override
    public String toString() {
        return "FiltroData{" +
                "tiposOla=" + tiposOla +
                ", tamano=" + tamanoMinimo + " - " + tamanoMaximo +
                ", direccionViento='" + direccionViento + '\'' +
                '}';
    }
}
