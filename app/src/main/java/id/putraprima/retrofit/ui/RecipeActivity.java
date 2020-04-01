package id.putraprima.retrofit.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.adapter.RecipeAdapter;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.Envelope;
import id.putraprima.retrofit.api.models.Recipe;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeActivity extends AppCompatActivity {

    final List<Recipe> recipe = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        doLoad();
    }

    public void doRecipe(){
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Envelope<List<Recipe>>> call = service.doRecipe();
        call.enqueue(new Callback<Envelope<List<Recipe>>>() {
            @Override
            public void onResponse(Call<Envelope<List<Recipe>>> call, Response<Envelope<List<Recipe>>> response) {
                for (int i=0; i < response.body().getData().size();i++){
                    int id = response.body().getData().get(i).getId();
                    String namaResep = response.body().getData().get(i).getNama_resep();
                    String deskripsi = response.body().getData().get(i).getDeskripsi();
                    String bahan = response.body().getData().get(i).getBahan();
                    String langkahPembuatan = response.body().getData().get(i).getLangkah_pembuatan();
                    String foto = response.body().getData().get(i).getFoto();
                    recipe.add(new Recipe(id, namaResep,deskripsi, bahan, langkahPembuatan, foto));

                }
            }

            @Override
            public void onFailure(Call<Envelope<List<Recipe>>> call, Throwable t) {

            }
        });
//
    }

    public void doLoad(){
        RecyclerView recipeView = findViewById(R.id.recycleView);

        RecipeAdapter adapter = new RecipeAdapter(recipe);
        recipeView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recipeView.setLayoutManager(layoutManager);
    }

    public void handleLoad(View view) {
        doRecipe();
        doLoad();
    }
}
