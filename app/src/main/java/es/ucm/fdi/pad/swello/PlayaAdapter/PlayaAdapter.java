package es.ucm.fdi.pad.swello.PlayaAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import es.ucm.fdi.pad.swello.R;

public class PlayaAdapter extends RecyclerView.Adapter<PlayaAdapter.ViewHolder> {

    private List<ItemPlaya> items;
    private OnPlayaClickListener listener;

    public interface OnPlayaClickListener {
        void onPlayaClick(ItemPlaya playa);
    }

    public void setOnPlayaClickListener(OnPlayaClickListener listener) {
        this.listener = listener;
    }

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
                .inflate(R.layout.item_result, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemPlaya item = items.get(position);

        // --- Nombre más grande ---
        holder.textNombrePlaya.setText(item.getNombre());
        holder.textNombrePlaya.setTextSize(20);

        // --- Olas ---
        holder.textAlturaOla.setText(String.format("%.1f m", item.getAlturaOla()));
        holder.textAlturaOla.setTextSize(16);

        holder.textDireccionOla.setText(item.getDireccionOla());
        holder.textDireccionOla.setTextSize(16);
        holder.iconDireccionOla.setRotation(getRotationForDirection(item.getDireccionOla()));

        // --- Distancia sin ".0" ---
        double d = item.getDistancia();
        String distanciaTxt = (d == Math.floor(d)) ? String.format("%.0f km", d)
                : String.format("%.1f km", d);
        holder.textDistancia.setText(distanciaTxt);
        holder.textDistancia.setTextSize(16);

        // --- Temperatura del agua ---
        double t = item.getTempAgua();
        String tempTxt = (t == Math.floor(t)) ? String.format("%.0f°C", t)
                : String.format("%.1f°C", t);
        holder.tempAgua.setText(tempTxt);
        holder.tempAgua.setTextSize(16);

        // --- Cargar imagen con Glide y esquinas redondeadas ---
        String url = item.getImagenUrl();
        if (url != null && !url.isEmpty()) {
            int radius = 30;
            Glide.with(holder.imageBackground.getContext())
                    .load(url)
                    .centerCrop()
                    .transform(new com.bumptech.glide.load.resource.bitmap.RoundedCorners(radius))
                    .placeholder(R.color.dark_cool_blue)
                    .error(R.color.dark_cool_blue)
                    .into(holder.imageBackground);
        } else {
            holder.imageBackground.setImageResource(R.color.dark_cool_blue);
        }

        holder.bindItem(item);
    }

    @Override
    public int getItemCount() {
        return (items != null) ? items.size() : 0;
    }

    // Rotación según dirección (16 puntos)
    private float getRotationForDirection(String direction) {
        if (direction == null) return 0f;

        switch (direction.toUpperCase()) {
            case "N":   return 0f;
            case "NNE": return 22.5f;
            case "NE":  return 45f;
            case "ENE": return 67.5f;
            case "E":   return 90f;
            case "ESE": return 112.5f;
            case "SE":  return 135f;
            case "SSE": return 157.5f;
            case "S":   return 180f;
            case "SSW": return 202.5f;
            case "SW":  return 225f;
            case "WSW": return 247.5f;
            case "W":   return 270f;
            case "WNW": return 292.5f;
            case "NW":  return 315f;
            case "NNW": return 337.5f;
            default:    return 0f;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textNombrePlaya, textAlturaOla, textDireccionOla, textDistancia, tempAgua;
        ImageView iconDireccionOla, imageBackground;
        private ItemPlaya boundItem;

        ViewHolder(View itemView) {
            super(itemView);

            textNombrePlaya = itemView.findViewById(R.id.text_nombre_playa);
            textAlturaOla = itemView.findViewById(R.id.text_altura_ola);
            textDireccionOla = itemView.findViewById(R.id.text_direccion_ola);
            textDistancia = itemView.findViewById(R.id.text_distancia);
            tempAgua = itemView.findViewById(R.id.text_temp_agua);
            iconDireccionOla = itemView.findViewById(R.id.icon_direccion_ola);
            imageBackground = itemView.findViewById(R.id.image_background);

            itemView.setOnClickListener(this);
        }

        void bindItem(ItemPlaya item) {
            this.boundItem = item;
        }

        @Override
        public void onClick(View v) {
            if (boundItem != null && listener != null) {
                listener.onPlayaClick(boundItem);
            }
        }
    }
}
