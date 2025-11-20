package es.ucm.fdi.pad.swello.Filtros;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.ucm.fdi.pad.swello.R;

public class Filtro extends BottomSheetDialogFragment {

    private static final String TAG = "FiltroBottomSheet";

    private RangeSlider sliderWaveHeight, sliderWavePeriod, sliderDistance;
    private ChipGroup chipWaveDir, chipSST, chipNivel, chipServicios;
    private MaterialButton btnAplicar;

    private MaterialButton btnOrdenarDistancia, btnOrdenarNombre, btnOrdenarPopularidad, btnOrdenarValoracion;

    private OnFiltroAplicadoListener listener;

    public interface OnFiltroAplicadoListener {
        void onFiltroAplicado(FiltroData filtros);
    }

    public void setOnFiltroAplicadoListener(OnFiltroAplicadoListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "Creando vista del filtro");

        View view = inflater.inflate(R.layout.component_filtro, container, false);

        sliderWaveHeight = view.findViewById(R.id.slider_wave_height);
        sliderWavePeriod = view.findViewById(R.id.slider_wave_period);
        sliderDistance = view.findViewById(R.id.slider_distancia);

        chipWaveDir = view.findViewById(R.id.chip_group_wave_dir);
        chipSST = view.findViewById(R.id.chip_group_sst);
        chipNivel = view.findViewById(R.id.chip_group_nivel);
        chipServicios = view.findViewById(R.id.chip_group_servicios);

        btnAplicar = view.findViewById(R.id.btn_aplicar_filtros);

        // inicializar botones de ordenación
        btnOrdenarDistancia = view.findViewById(R.id.btn_ordenar_distancia);
        btnOrdenarNombre = view.findViewById(R.id.btn_ordenar_nombre);
        btnOrdenarPopularidad = view.findViewById(R.id.btn_ordenar_popularidad);
        btnOrdenarValoracion = view.findViewById(R.id.btn_ordenar_valoracion);

        // Restaurar estado guardado de filtros
        restaurarEstadoFiltros();

        btnAplicar.setOnClickListener(v -> aplicarFiltros());

        // configurar listeners de ordenación
        btnOrdenarDistancia.setOnClickListener(v -> aplicarOrdenacion("distancia"));
        btnOrdenarNombre.setOnClickListener(v -> aplicarOrdenacion("nombre"));
        btnOrdenarPopularidad.setOnClickListener(v -> aplicarOrdenacion("popularidad"));
        btnOrdenarValoracion.setOnClickListener(v -> aplicarOrdenacion("valoración"));

        return view;
    }

    private void aplicarOrdenacion(String tipoOrdenacion) {
        if (listener != null) {
            FiltroData data = new FiltroData();
            data.ordenacion = tipoOrdenacion;
            listener.onFiltroAplicado(data);
            Toast.makeText(getContext(), "Ordenado por " + tipoOrdenacion, Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    private void aplicarFiltros() {
        Log.d(TAG, "Aplicando filtros");

        FiltroData data = new FiltroData();

        List<Float> altura = sliderWaveHeight.getValues();
        data.tamanoMinimo = altura.get(0);
        data.tamanoMaximo = altura.get(1);
        Log.d(TAG, "Altura seleccionada: min=" + data.tamanoMinimo + " max=" + data.tamanoMaximo);

        List<Float> periodo = sliderWavePeriod.getValues();
        data.periodoMinimo = periodo.get(0);
        data.periodoMaximo = periodo.get(1);
        Log.d(TAG, "Periodo seleccionado: min=" + data.periodoMinimo + " max=" + data.periodoMaximo);

        List<Float> distancia = sliderDistance.getValues();
        data.distanciaMinima = distancia.get(0);
        data.distanciaMaxima = distancia.get(1);
        Log.d(TAG, "Distancia seleccionada: min=" + data.distanciaMinima + " max=" + data.distanciaMaxima);

        data.direccionOlas = getCheckedChipText(chipWaveDir);
        Log.d(TAG, "Direccion olas seleccionada: " + data.direccionOlas);

        data.tempAgua = getCheckedChipText(chipSST);
        Log.d(TAG, "Temperatura del agua seleccionada: " + data.tempAgua);

        data.nivelSurfista = getCheckedChipText(chipNivel);
        Log.d(TAG, "Nivel de surfista seleccionado: " + data.nivelSurfista);

        data.servicios = getCheckedChips(chipServicios);
        Log.d(TAG, "Servicios seleccionados: " + data.servicios);

        guardarEstadoFiltros(data);

        if (listener != null) {
            Log.d(TAG, "Enviando filtros al listener: " + data);
            listener.onFiltroAplicado(data);
        }

        Toast.makeText(getContext(), "Filtros aplicados", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private String getCheckedChipText(ChipGroup group) {
        int id = group.getCheckedChipId();
        if (id == View.NO_ID) return "";
        Chip chip = group.findViewById(id);
        if (chip != null) Log.d(TAG, "Chip seleccionado: " + chip.getText());
        return chip != null ? chip.getText().toString() : "";
    }

    private List<String> getCheckedChips(ChipGroup group) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < group.getChildCount(); i++) {
            Chip chip = (Chip) group.getChildAt(i);
            if (chip.isChecked()) {
                Log.d(TAG, "Chip multiple seleccionado: " + chip.getText());
                list.add(chip.getText().toString());
            }
        }
        return list;
    }

    private void guardarEstadoFiltros(FiltroData data) {
        Log.d(TAG, "Guardando estado de filtros: " + data);

        if (getContext() == null) return;
        SharedPreferences prefs = getContext().getSharedPreferences("filtros_prefs", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putFloat("distanciaMin", data.distanciaMinima);
        editor.putFloat("distanciaMax", data.distanciaMaxima);
        editor.putFloat("tamanoMin", data.tamanoMinimo);
        editor.putFloat("tamanoMax", data.tamanoMaximo);
        editor.putFloat("periodoMin", data.periodoMinimo);
        editor.putFloat("periodoMax", data.periodoMaximo);

        editor.putString("direccionOlas", data.direccionOlas);
        editor.putString("tempAgua", data.tempAgua);
        editor.putString("nivelSurfista", data.nivelSurfista);
        editor.putStringSet("servicios", new HashSet<>(data.servicios));

        editor.apply();
    }

    private void restaurarEstadoFiltros() {
        Log.d(TAG, "Restaurando estado de filtros");

        if (getContext() == null) return;
        SharedPreferences prefs = getContext().getSharedPreferences("filtros_prefs", getContext().MODE_PRIVATE);

        float distanciaMin = prefs.getFloat("distanciaMin", 0f);
        float distanciaMax = prefs.getFloat("distanciaMax", 120f);
        float tamanoMin = prefs.getFloat("tamanoMin", 0.5f);
        float tamanoMax = prefs.getFloat("tamanoMax", 5f);
        float periodoMin = prefs.getFloat("periodoMin", 5f);
        float periodoMax = prefs.getFloat("periodoMax", 12f);

        sliderDistance.setValues(distanciaMin, distanciaMax);
        sliderWaveHeight.setValues(tamanoMin, tamanoMax);
        sliderWavePeriod.setValues(periodoMin, periodoMax);

        Log.d(TAG, "Distancia restaurada: min=" + distanciaMin + " max=" + distanciaMax);
        Log.d(TAG, "Altura restaurada: min=" + tamanoMin + " max=" + tamanoMax);
        Log.d(TAG, "Periodo restaurado: min=" + periodoMin + " max=" + periodoMax);

        String dir = prefs.getString("direccionOlas", "");
        String temp = prefs.getString("tempAgua", "");
        String nivel = prefs.getString("nivelSurfista", "");
        Set<String> servicios = prefs.getStringSet("servicios", new HashSet<>());

        Log.d(TAG, "Direccion olas restaurada: " + dir);
        Log.d(TAG, "Temperatura restaurada: " + temp);
        Log.d(TAG, "Nivel restaurado: " + nivel);
        Log.d(TAG, "Servicios restaurados: " + servicios);

        setCheckedChipByText(chipWaveDir, dir);
        setCheckedChipByText(chipSST, temp);
        setCheckedChipByText(chipNivel, nivel);
        setCheckedChipsByText(chipServicios, new ArrayList<>(servicios));
    }

    private void setCheckedChipByText(ChipGroup group, String text) {
        if (text == null || text.isEmpty()) return;

        Log.d(TAG, "Buscando chip para marcar: " + text);

        for (int i = 0; i < group.getChildCount(); i++) {
            Chip chip = (Chip) group.getChildAt(i);
            if (chip.getText().toString().equals(text)) {
                Log.d(TAG, "Marcando chip: " + chip.getText());
                chip.setChecked(true);
            }
        }
    }

    private void setCheckedChipsByText(ChipGroup group, List<String> texts) {
        for (int i = 0; i < group.getChildCount(); i++) {
            Chip chip = (Chip) group.getChildAt(i);
            chip.setChecked(texts.contains(chip.getText().toString()));

            if (chip.isChecked()) {
                Log.d(TAG, "Marcando chip multiple: " + chip.getText());
            }
        }
    }

    public void inicializarValoresPorDefecto() {
        Log.d(TAG, "Inicializando valores por defecto");

        final float distanciaMin = 0f, distanciaMax = 120f;
        final float alturaMin = 0.5f, alturaMax = 5f;
        final float periodoMin = 5f, periodoMax = 12f;

        if (getContext() == null) return;
        SharedPreferences prefs = getContext().getSharedPreferences("filtros_prefs", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putFloat("distanciaMin", distanciaMin);
        editor.putFloat("distanciaMax", distanciaMax);
        editor.putFloat("tamanoMin", alturaMin);
        editor.putFloat("tamanoMax", alturaMax);
        editor.putFloat("periodoMin", periodoMin);
        editor.putFloat("periodoMax", periodoMax);

        Log.d(TAG, "Chips limpiados");

        clearChips(chipWaveDir);
        clearChips(chipSST);
        clearChips(chipNivel);
        clearChips(chipServicios);

        editor.apply();
    }

    private void clearChips(ChipGroup group) {
        for (int i = 0; i < group.getChildCount(); i++) {
            Chip chip = (Chip) group.getChildAt(i);
            chip.setChecked(false);
            Log.d(TAG, "Chip desmarcado: " + chip.getText());
        }
    }
}
