package es.ucm.fdi.pad.swello.PlayaAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import es.ucm.fdi.pad.swello.ItemPlaya;
import es.ucm.fdi.pad.swello.R;

public class PlayaAdapter extends RecyclerView.Adapter<PlayaAdapter.ViewHolder> {

    private List<ItemPlaya> items;

    public PlayaAdapter(List<ItemPlaya> items) {
        this.items = items;
    }

    public void updateList(List<ItemPlaya> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_result, parent, false); // 👈 tu layout actualizado
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemPlaya item = items.get(position);

        // Nombre
        holder.textNombrePlaya.setText(item.getNombre());

        // Altura de la ola
        holder.textAlturaOla.setText(String.format("%.1f m", item.getAlturaOla()));

        // Dirección y rotación de la flecha
        String dir = item.getDireccionOla();
        holder.textDireccionOla.setText(dir);
        holder.iconDireccionOla.setRotation(getRotationForDirection(dir));

        // Distancia
        holder.textDistancia.setText(String.format("%.0f km", item.getDistancia()));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    /**
     * Convierte una dirección cardinal (N, NE, E, etc.) en el ángulo de rotación para la flecha.
     */
    private float getRotationForDirection(String direccion) {
        if (direccion == null) return 0f;
        switch (direccion.toUpperCase()) {
            case "N":  return 0f;
            case "NE": return 45f;
            case "E":  return 90f;
            case "SE": return 135f;
            case "S":  return 180f;
            case "SO":
            case "SW": return 225f;
            case "O":
            case "W":  return 270f;
            case "NO":
            case "NW": return 315f;
            default:   return 0f;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView textNombrePlaya, textAlturaOla, textDireccionOla, textDistancia;
        ImageView iconDireccionOla;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;

            textNombrePlaya = itemView.findViewById(R.id.text_nombre_playa);
            textAlturaOla = itemView.findViewById(R.id.text_altura_ola);
            textDireccionOla = itemView.findViewById(R.id.text_direccion_ola);
            textDistancia = itemView.findViewById(R.id.text_distancia);
            iconDireccionOla = itemView.findViewById(R.id.icon_direccion_ola);
        }
    }
}
