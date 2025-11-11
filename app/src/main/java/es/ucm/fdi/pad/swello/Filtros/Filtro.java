package es.ucm.fdi.pad.swello.Filtros;

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
import java.util.List;

import es.ucm.fdi.pad.swello.R;

public class Filtro extends BottomSheetDialogFragment {

    private static final String TAG = "FiltroBottomSheet";

    private RangeSlider sliderWaveHeight, sliderWavePeriod;
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
        chipWaveDir = view.findViewById(R.id.chip_group_wave_dir);
        chipSST = view.findViewById(R.id.chip_group_sst);
        chipNivel = view.findViewById(R.id.chip_group_nivel);
        chipServicios = view.findViewById(R.id.chip_group_servicios);
        btnAplicar = view.findViewById(R.id.btn_aplicar_filtros);

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

        data.direccionOlas = getCheckedChipText(chipWaveDir);
        data.tempAgua = getCheckedChipText(chipSST);
        data.nivelSurfista = getCheckedChipText(chipNivel);
        data.servicios = getCheckedChips(chipServicios);

        Log.d(TAG, "Filtros aplicados: " + data);

        if (listener != null) listener.onFiltroAplicado(data);
        Toast.makeText(getContext(), "Filtros aplicados", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private String getCheckedChipText(ChipGroup group) {
        int id = group.getCheckedChipId();
        if (id == View.NO_ID) return "";
        Chip chip = group.findViewById(id);
        return chip != null ? chip.getText().toString() : "";
    }

    private List<String> getCheckedChips(ChipGroup group) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < group.getChildCount(); i++) {
            Chip chip = (Chip) group.getChildAt(i);
            if (chip.isChecked()) list.add(chip.getText().toString());
        }
        return list;
    }
}
