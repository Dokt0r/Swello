package es.ucm.fdi.pad.swello.PlayaAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import java.util.List;
import es.ucm.fdi.pad.swello.R;
import es.ucm.fdi.pad.swello.Location.UserLocation;

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

        // --- Calcular distancia si la ubicación está disponible ---
        if (UserLocation.isInitialized() && UserLocation.getInstance().isLocationAvailable()) {
            double userLat = UserLocation.getInstance().getLatitud();
            double userLon = UserLocation.getInstance().getLongitud();
            double playaLat = item.getLatitud();
            double playaLon = item.getLongitud();
            double distancia = calcularDistancia(userLat, userLon, playaLat, playaLon);
            item.setDistancia(distancia);
            Log.d("PlayaAdapter", "Distancia calculada para " + item.getNombre() + ": " + distancia + " km");
        }

        holder.textNombrePlaya.setText(item.getNombre());
        holder.textAlturaOla.setText(String.format("%.1f m", item.getAlturaOla()));
        holder.textDireccionOla.setText(item.getDireccionOla());
        holder.textDistancia.setText(String.format("%.1f km", item.getDistancia()));
        holder.tempAgua.setText(String.format("%.1f°C", item.getTempAgua()));

        // --- Cargar imagen con Glide ---
        String url = item.getImagenUrl();
        Log.d("PlayaAdapter", "Cargando imagen para " + item.getNombre() + ": " + url);
        if (url != null && !url.isEmpty()) {
            if (!url.startsWith("http")) {
                url = "http://10.0.2.2:3000/images/" + url;
                Log.d("PlayaAdapter", "URL ajustada a: " + url);
            }

            String finalUrl = url;
            Glide.with(holder.itemView.getContext())
                    .load(url)
                    .centerCrop()
                    .transform(new RoundedCorners(30))
                    .listener(new com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable com.bumptech.glide.load.engine.GlideException e, Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
                            Log.e("PlayaAdapter", "Error cargando imagen: " + finalUrl, e);
                            return false; // deja que Glide maneje el error
                        }

                        @Override
                        public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                            Log.d("PlayaAdapter", "Imagen cargada correctamente: " + finalUrl);
                            return false; // deja que Glide maneje la imagen
                        }
                    })
                    .into(holder.imageBackground);
        } else {
            Log.w("PlayaAdapter", "No hay URL de imagen para " + item.getNombre());
            holder.imageBackground.setBackgroundColor(holder.itemView.getContext().getColor(R.color.dark_cool_blue));
        }

        holder.bindItem(item);
    }


    @Override
    public int getItemCount() {
        return (items != null) ? items.size() : 0;
    }

    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textNombrePlaya, textAlturaOla, textDireccionOla, textDistancia, tempAgua;
        ImageView imageBackground;
        private ItemPlaya boundItem;

        ViewHolder(View itemView) {
            super(itemView);
            textNombrePlaya = itemView.findViewById(R.id.text_nombre_playa);
            textAlturaOla = itemView.findViewById(R.id.text_altura_ola);
            textDireccionOla = itemView.findViewById(R.id.text_direccion_ola);
            textDistancia = itemView.findViewById(R.id.text_distancia);
            tempAgua = itemView.findViewById(R.id.text_temp_agua);
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
