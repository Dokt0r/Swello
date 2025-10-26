package es.ucm.fdi.pad.swello.OptionsMenu;

import android.view.LayoutInflater;
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

        // Por ahora solo mostrar un toast al hacer click
        holder.itemView.setOnClickListener(v ->
                Toast.makeText(v.getContext(), "Seleccionaste: " + option.getTitle(), Toast.LENGTH_SHORT).show()
        );
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
