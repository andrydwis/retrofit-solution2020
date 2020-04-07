package id.putraprima.retrofit.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

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

    ArrayList<Recipe> recipe;
    RecipeAdapter adapter;
    ProgressDialog progressDialog;
    ConstraintLayout mRecipeLayout;

    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipe = new ArrayList<>();

        RecyclerView recipeView = findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recipeView.setLayoutManager(layoutManager);

        adapter = new RecipeAdapter(recipe);
        recipeView.setAdapter(adapter);
        mRecipeLayout = findViewById(R.id.recipeLayout);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        page = 1;
        doReload();
    }

    public void doRecipe() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Envelope<List<Recipe>>> call = service.doRecipe();
        call.enqueue(new Callback<Envelope<List<Recipe>>>() {
            @Override
            public void onResponse(Call<Envelope<List<Recipe>>> call, Response<Envelope<List<Recipe>>> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        int id = response.body().getData().get(i).getId();
                        String namaResep = response.body().getData().get(i).getNama_resep();
                        String deskripsi = response.body().getData().get(i).getDeskripsi();
                        String bahan = response.body().getData().get(i).getBahan();
                        String langkahPembuatan = response.body().getData().get(i).getLangkah_pembuatan();
                        String foto = response.body().getData().get(i).getFoto();
                        recipe.add(new Recipe(id, namaResep, deskripsi, bahan, langkahPembuatan, foto));
                        adapter.notifyDataSetChanged();
                    }
                    Snackbar snackbar = Snackbar.make(mRecipeLayout, "Load data sukses", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    //ganti page selanjutnya
                    page++;
                }else {
                    Snackbar snackbar = Snackbar.make(mRecipeLayout, "Load data gagal", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<Envelope<List<Recipe>>> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(mRecipeLayout, "gagal koneksi", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }

    public void doLoadMore() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Envelope<List<Recipe>>> call = service.doLoadMore(page);
        call.enqueue(new Callback<Envelope<List<Recipe>>>() {
            @Override
            public void onResponse(Call<Envelope<List<Recipe>>> call, Response<Envelope<List<Recipe>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getData().size() != 0){
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            int id = response.body().getData().get(i).getId();
                            String namaResep = response.body().getData().get(i).getNama_resep();
                            String deskripsi = response.body().getData().get(i).getDeskripsi();
                            String bahan = response.body().getData().get(i).getBahan();
                            String langkahPembuatan = response.body().getData().get(i).getLangkah_pembuatan();
                            String foto = response.body().getData().get(i).getFoto();
                            recipe.add(new Recipe(id, namaResep, deskripsi, bahan, langkahPembuatan, foto));
                            adapter.notifyDataSetChanged();
                        }
                        Snackbar snackbar = Snackbar.make(mRecipeLayout, "Load data page "+page, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        //ganti page selanjutnya
                        page++;
                    }else{
                        Toast.makeText(RecipeActivity.this, "Page kosong", Toast.LENGTH_SHORT).show();

                        Snackbar snackbar = Snackbar.make(mRecipeLayout, "Load data page "+page, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }else {
                    Snackbar snackbar = Snackbar.make(mRecipeLayout, "Load data gagal", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<Envelope<List<Recipe>>> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(mRecipeLayout, "gagal koneksi", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }

    public void doReload() {
        //clear dulu baru doRecipe biar gak numpuk
        recipe.clear();
        doRecipe();
    }

    public void handleReload(View view) {
        //reset ke page 1 lagi
        page = 1;
        doReload();
    }

    public void handleLoadMore(View view) {
        doLoadMore();
    }
}
