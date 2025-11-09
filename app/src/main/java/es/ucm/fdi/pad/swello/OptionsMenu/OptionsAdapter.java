package es.ucm.fdi.pad.swello.OptionsMenu;

import android.view.LayoutInflater;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.ucm.fdi.pad.swello.R;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {

    private List<OptionItem> options;
    private boolean generalExpanded = false; // saber si "General" está abierto

    public OptionsAdapter(List<OptionItem> options) {
        this.options = options;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        OptionItem option = options.get(position);
        holder.textOption.setText(option.getTitle());
        holder.iconOption.setImageResource(option.getIconRes());
        if (option.isSubOption()) {
            holder.itemView.setPadding(80, 0, 0, 0);  // subopción: desplazada
        } else {
            holder.itemView.setPadding(0, 0, 0, 0);   // principal: alineada a la izquierda
        }

        // Por ahora solo mostrar un toast al hacer click
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Toast.makeText(context, "Seleccionaste: " + option.getTitle(), Toast.LENGTH_SHORT).show();

            switch (option.getTitle()){
                case "General":
                    if(!generalExpanded){
                        int insertPosition = position + 1;
                        options.add(insertPosition, new OptionItem("Idioma", R.drawable.ic_language, true));
                        options.add(insertPosition + 1, new OptionItem("Tema", R.drawable.ic_theme, true));
                        options.add(insertPosition + 2, new OptionItem("Notificaciones", R.drawable.ic_notifications, true));
                        notifyItemRangeInserted(insertPosition, 3);
                        generalExpanded = true;
                    } else {
                        options.removeIf(o -> o.getTitle().equals("Idioma")
                                || o.getTitle().equals("Tema")
                                || o.getTitle().equals("Notificaciones"));
                        notifyDataSetChanged();
                        generalExpanded = false;
                    }
            }
        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconOption;
        TextView textOption;

        public ViewHolder(View itemView) {
            super(itemView);
            iconOption = itemView.findViewById(R.id.icon_option);
            textOption = itemView.findViewById(R.id.text_option);
        }
    }
}
