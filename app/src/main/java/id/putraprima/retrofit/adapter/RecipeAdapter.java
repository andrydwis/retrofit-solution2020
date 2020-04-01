package id.putraprima.retrofit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.models.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    ArrayList<Recipe> items;

    public RecipeAdapter(ArrayList<Recipe> items) {
        this.items = items;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe item = items.get(position);
        holder.idText.setText(Integer.toString(item.getId()));
        holder.namaResepText.setText(item.getNama_resep());
        holder.deskripsiText.setText(item.getDeskripsi());
        holder.bahanText.setText(item.getBahan());
        holder.langkahPembuatan.setText(item.getLangkah_pembuatan());
        String url = "https://mobile.putraprima.id/uploads/"+item.getFoto();
        Picasso.get().load(url).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView idText, namaResepText, deskripsiText, bahanText, langkahPembuatan;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idText = itemView.findViewById(R.id.idText);
            namaResepText = itemView.findViewById(R.id.namaResepText);
            deskripsiText = itemView.findViewById(R.id.deskripsiText);
            bahanText = itemView.findViewById(R.id.bahanText);
            langkahPembuatan = itemView.findViewById(R.id.langkahPembuatanText);
            imageView = itemView.findViewById(R.id.imageView2);
        }
    }
}
