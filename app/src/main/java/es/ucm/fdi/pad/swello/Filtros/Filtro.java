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
        View view = inflater.inflate(R.layout.component_filtro, container, false);

        sliderWaveHeight = view.findViewById(R.id.slider_wave_height);
        sliderWavePeriod = view.findViewById(R.id.slider_wave_period);
        sliderDistance = view.findViewById(R.id.slider_distancia);
        chipWaveDir = view.findViewById(R.id.chip_group_wave_dir);
        chipSST = view.findViewById(R.id.chip_group_sst);
        chipNivel = view.findViewById(R.id.chip_group_nivel);
        chipServicios = view.findViewById(R.id.chip_group_servicios);
        btnAplicar = view.findViewById(R.id.btn_aplicar_filtros);

        // Restaurar estado guardado de filtros
        restaurarEstadoFiltros();

        btnAplicar.setOnClickListener(v -> aplicarFiltros());
        return view;
    }

    private void aplicarFiltros() {
        FiltroData data = new FiltroData();

        List<Float> altura = sliderWaveHeight.getValues();
        data.tamanoMinimo = altura.get(0);
        data.tamanoMaximo = altura.get(1);

        List<Float> periodo = sliderWavePeriod.getValues();
        data.periodoMinimo = periodo.get(0);
        data.periodoMaximo = periodo.get(1);

        List<Float> distancia = sliderDistance.getValues();
        data.distanciaMinima = distancia.get(0);
        data.distanciaMaxima = distancia.get(1);

        data.direccionOlas = getCheckedChipText(chipWaveDir);
        data.tempAgua = getCheckedChipText(chipSST);
        data.nivelSurfista = getCheckedChipText(chipNivel);
        data.servicios = getCheckedChips(chipServicios);

        Log.d(TAG, "Filtros aplicados: " + data);

        guardarEstadoFiltros(data); //Guarda los valores

        if (listener != null) listener.onFiltroAplicado(data);
        Toast.makeText(getContext(), "Filtros aplicados", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    // --- Recuperar texto del chip seleccionado ---
    private String getCheckedChipText(ChipGroup group) {
        int id = group.getCheckedChipId();
        if (id == View.NO_ID) return "";
        Chip chip = group.findViewById(id);
        return chip != null ? chip.getText().toString() : "";
    }

    // --- Recuperar textos de varios chips seleccionados ---
    private List<String> getCheckedChips(ChipGroup group) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < group.getChildCount(); i++) {
            Chip chip = (Chip) group.getChildAt(i);
            if (chip.isChecked()) list.add(chip.getText().toString());
        }
        return list;
    }

    // --- Guardar estado de los filtros ---
    private void guardarEstadoFiltros(FiltroData data) {
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
        Log.d(TAG, "Estado de filtros guardado");
    }

    // --- Restaurar estado al abrir el filtro ---
    private void restaurarEstadoFiltros() {

        if (getContext() == null) return;
        SharedPreferences prefs = getContext().getSharedPreferences("filtros_prefs", getContext().MODE_PRIVATE);
        //prefs.edit().clear().apply(); //Limpia datos antiguos para cuando se cambian los filtros

        float distanciaMin = prefs.getFloat("distanciaMin", 0f);
        float distanciaMax = prefs.getFloat("distanciaMax", 200f);
        float tamanoMin = prefs.getFloat("tamanoMin", 0.5f);
        float tamanoMax = prefs.getFloat("tamanoMax", 5f);
        float periodoMin = prefs.getFloat("periodoMin", 5f);
        float periodoMax = prefs.getFloat("periodoMax", 12f);

        sliderDistance.setValues(distanciaMin, distanciaMax);
        sliderWaveHeight.setValues(tamanoMin, tamanoMax);
        sliderWavePeriod.setValues(periodoMin, periodoMax);

        String dir = prefs.getString("direccionOlas", "");
        String temp = prefs.getString("tempAgua", "");
        String nivel = prefs.getString("nivelSurfista", "");
        Set<String> servicios = prefs.getStringSet("servicios", new HashSet<>());

        setCheckedChipByText(chipWaveDir, dir);
        setCheckedChipByText(chipSST, temp);
        setCheckedChipByText(chipNivel, nivel);
        setCheckedChipsByText(chipServicios, new ArrayList<>(servicios));

        Log.d(TAG, "Estado de filtros restaurado");
    }

    // --- Utilidades para marcar chips ---
    private void setCheckedChipByText(ChipGroup group, String text) {
        if (text == null || text.isEmpty()) return;
        for (int i = 0; i < group.getChildCount(); i++) {
            Chip chip = (Chip) group.getChildAt(i);
            chip.setChecked(chip.getText().toString().equals(text));
        }
    }

    private void setCheckedChipsByText(ChipGroup group, List<String> texts) {
        for (int i = 0; i < group.getChildCount(); i++) {
            Chip chip = (Chip) group.getChildAt(i);
            chip.setChecked(texts.contains(chip.getText().toString()));
        }
    }

    public void inicializarValoresPorDefecto() {
        final float distanciaMin = 1f, distanciaMax = 50f;
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

        // Limpiar chips
        clearChips(chipWaveDir);
        clearChips(chipSST);
        clearChips(chipNivel);
        clearChips(chipServicios);
    }


    private void clearChips(ChipGroup group) {
        for (int i = 0; i < group.getChildCount(); i++) {
            Chip chip = (Chip) group.getChildAt(i);
            chip.setChecked(false);
        }
    }
}
