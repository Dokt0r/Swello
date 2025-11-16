package es.ucm.fdi.pad.swello.PlayaAdapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import es.ucm.fdi.pad.swello.R;

public class PlayaAdapter extends RecyclerView.Adapter<PlayaAdapter.ViewHolder> {

    private static final String TAG = "PlayaAdapter";
    private List<ItemPlaya> items;
    private OnPlayaClickListener listener;

    public interface OnPlayaClickListener {
        void onPlayaClick(ItemPlaya playa);
    }

    public void setOnPlayaClickListener(OnPlayaClickListener listener) {
        this.listener = listener;
        Log.d(TAG, "Listener registrado en adapter: " + (listener != null));
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

        holder.textNombrePlaya.setText(item.getNombre());
        holder.textAlturaOla.setText(String.format("%.1f m", item.getAlturaOla()));
        holder.textDireccionOla.setText(item.getDireccionOla());
        holder.iconDireccionOla.setRotation(getRotationForDirection(item.getDireccionOla()));
        holder.textDistancia.setText(String.format("%.0f km", item.getDistancia()));

        holder.bindItem(item);

        // --- Cargar imagen con Glide y esquinas redondeadas ---
        String url = item.getImagenUrl();
        if (url != null && !url.isEmpty()) {
            int radius = 30; // radio en píxeles, puedes ajustarlo
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
    }



    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MaterialCardView cardView;
        TextView textNombrePlaya, textAlturaOla, textDireccionOla, textDistancia;
        ImageView iconDireccionOla, imageBackground;
        private ItemPlaya boundItem;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            textNombrePlaya = itemView.findViewById(R.id.text_nombre_playa);
            textAlturaOla = itemView.findViewById(R.id.text_altura_ola);
            textDireccionOla = itemView.findViewById(R.id.text_direccion_ola);
            textDistancia = itemView.findViewById(R.id.text_distancia);
            iconDireccionOla = itemView.findViewById(R.id.icon_direccion_ola);
            imageBackground = itemView.findViewById(R.id.image_background);

            itemView.setClickable(true);
            itemView.setFocusable(true);
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
