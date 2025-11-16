package es.ucm.fdi.pad.swello.OptionsMenu;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import es.ucm.fdi.pad.swello.MainActivity;
import es.ucm.fdi.pad.swello.R;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {
    private Context context;
    private List<OptionItem> options;
    private boolean generalExpanded = false;
    private boolean InformacionExpanded = false;

    public OptionsAdapter(Context context, List<OptionItem> options) {
        this.context = context;
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
            holder.itemView.setPadding(80, 0, 0, 0);
        } else {
            holder.itemView.setPadding(0, 0, 0, 0);
        }

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Toast.makeText(context, v.getContext().getString(R.string.seleccionaste) + option.getTitle(), Toast.LENGTH_SHORT).show();

            switch (option.getTitle()){
                case "General":
                    toggleGeneral(position);
                    break;
                case "Informacion":
                    toggleInformacion(position);
                    break;
                case "Information":
                    toggleInformacion(position);
                    break;
                case "Aviso legal":
                    showLegalFile(context, "aviso_legal.txt");
                    break;
                case "Legal notice":
                    showLegalFile(context, "aviso_legal.txt");
                    break;

                case "Idioma":
                    showLanguageDialog(context);
                    break;
                case "Language":
                    showLanguageDialog(context);
                    break;
                case "Contacto":
                    showContactDialog(context);
                    break;
                case "Contact":
                    showContactDialog(context);

                default:
                    Toast.makeText(context, v.getContext().getString(R.string.pulsaste) + option.getTitle(), Toast.LENGTH_SHORT).show();
                    break;

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

    private void toggleGeneral(int position) {
        String idioma = context.getString(R.string.opcion_idioma);
        String contacto = context.getString(R.string.opcion_contacto);
        if (!generalExpanded) {
            int insertPosition = position + 1;
            options.add(insertPosition, new OptionItem(idioma, R.drawable.ic_language, true));
            options.add(insertPosition, new OptionItem(contacto, R.drawable.ic_contact, true));
            notifyItemRangeInserted(insertPosition, 2);
            generalExpanded = true;
        } else {
            options.removeIf(o -> o.getTitle().equals(idioma)
                    || o.getTitle().equals((contacto)));
            notifyDataSetChanged();
            generalExpanded = false;
        }
    }

    private void toggleInformacion(int position){
        String aviso_legal = context.getString(R.string.opcion_av_legal);
        String term_cond = context.getString(R.string.opcion_term_cond);

        if(!InformacionExpanded){
            int insertPosition = position + 1;
            options.add(insertPosition, new OptionItem(aviso_legal, R.drawable.ic_av_legal, true));
            options.add(insertPosition, new OptionItem(term_cond, R.drawable.ic_term_cond, true));
            notifyItemRangeInserted(insertPosition, 2);
            InformacionExpanded= true;
        }
        else{
            options.removeIf(o -> o.getTitle().equals(aviso_legal) || o.getTitle().equals(term_cond));
            notifyDataSetChanged();
            InformacionExpanded = false;
        }
    }

    private void showLegalFile(Context context, String fileName) {
        StringBuilder text = new StringBuilder();

        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }

            reader.close();
            is.close();
        } catch (Exception e) {
            text.append("Error loading file.");
        }

        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.opcion_av_legal))
                .setMessage(text.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    private void showLanguageDialog(Context context) {
        final String[] idiomas = {context.getString(R.string.Espanol), context.getString(R.string.Ingles)};
        int checkedItem = Locale.getDefault().getLanguage().equals("es") ? 0 : 1;

        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.selecciona_idioma))
                .setSingleChoiceItems(idiomas, checkedItem, (dialog, which) -> {
                    String langCode = (which == 0) ? "es" : "en";
                    setLocale(context, langCode);
                    dialog.dismiss();
                })
                .show();
    }

    private void showContactDialog(Context context){
        final String contacto = context.getString(R.string.contacto);

        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.Titulo))
                .setMessage(context.getString(R.string.contacto))
                .show();

    }

    private void setLocale(Context context, String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        // Guardar idioma para mantenerlo tras reinicio
        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        prefs.edit().putString("language", langCode).apply();

        // Reiniciar la app desde la MainActivity
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).finish();
        }
    }



}

