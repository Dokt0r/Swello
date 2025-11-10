package es.ucm.fdi.pad.swello.Filtros;

import android.content.Context;
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
import com.google.android.material.slider.Slider;

import es.ucm.fdi.pad.swello.R;

public class Filtro extends BottomSheetDialogFragment {

    private static final String TAG = "FiltroBottomSheet";

    private ChipGroup chipGroupOlas, chipGroupViento, chipGroupNivel, chipGroupServicios;
    private Slider sliderTamano;
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

        try {
            chipGroupOlas = view.findViewById(R.id.chip_group_olas);
            chipGroupViento = view.findViewById(R.id.chip_group_viento);
            sliderTamano = view.findViewById(R.id.slider_tamano_ola);
            btnAplicar = view.findViewById(R.id.btn_aplicar_filtros);

            btnAplicar.setOnClickListener(v -> aplicarFiltros());
        } catch (Exception e) {
            Log.e(TAG, "Error inicializando filtros", e);
        }

        return view;
    }

    private void aplicarFiltros() {
        try {
            FiltroData data = new FiltroData();

            // Tipos de ola
            for (int i = 0; i < chipGroupOlas.getChildCount(); i++) {
                Chip chip = (Chip) chipGroupOlas.getChildAt(i);
                if (chip.isChecked()) {
                    data.tiposOla.add(chip.getText().toString());
                }
            }

            // Tamaño de ola (usamos el valor actual del slider)
            data.tamanoMinimo = 0.5f; // valor base
            data.tamanoMaximo = sliderTamano.getValue();

            // Dirección del viento
            for (int i = 0; i < chipGroupViento.getChildCount(); i++) {
                Chip chip = (Chip) chipGroupViento.getChildAt(i);
                if (chip.isChecked()) {
                    data.direccionViento = chip.getText().toString();
                    break;
                }
            }

            // Nivel del surfista
            for (int i = 0; i < chipGroupNivel.getChildCount(); i++) {
                Chip chip = (Chip) chipGroupNivel.getChildAt(i);
                if (chip.isChecked()) {
                    data.nivelSurfista = chip.getText().toString();
                    break;
                }
            }

            // Servicios
            for (int i = 0; i < chipGroupServicios.getChildCount(); i++) {
                Chip chip = (Chip) chipGroupServicios.getChildAt(i);
                if (chip.isChecked()) {
                    data.servicios.add(chip.getText().toString());
                }
            }

            Log.d(TAG, "Filtros aplicados: " + data);

            if (listener != null) {
                listener.onFiltroAplicado(data);
            } else {
                Log.w(TAG, "Listener no asignado");
            }

            Toast.makeText(requireContext(), getString(R.string.filtros_aplicados), Toast.LENGTH_SHORT).show();
            dismiss();

        } catch (Exception e) {
            Log.e(TAG, "Error al aplicar filtros", e);
        }
    }
}
