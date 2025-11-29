package es.ucm.fdi.pad.swello.PlayaDetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import es.ucm.fdi.pad.swello.R;

public class ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ImagePagerViewHolder> {

    private List<String> imageUrls;

    // Constructor
    public ImagePagerAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImagePagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_image_pager layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_pager, parent, false);
        return new ImagePagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagePagerViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);

        // Load image for ViewPager
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.color.dark_cool_blue)
                .error(R.color.dark_cool_blue)
                .fitCenter()  // Show entire image without cropping
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    // ViewHolder for ViewPager items
    static class ImagePagerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImagePagerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewPager);
        }
    }
}