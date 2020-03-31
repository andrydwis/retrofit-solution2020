package id.putraprima.retrofit.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.Envelope;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.UpdateProfileRequest;
import id.putraprima.retrofit.api.models.UpdateProfileResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText mNameProfileUpdateText;
    private EditText mEmailProfileUpdateText;

    private UpdateProfileRequest updateProfileRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        mNameProfileUpdateText = findViewById(R.id.nameProfileUpdateText);
        mEmailProfileUpdateText = findViewById(R.id.emailProfileUpdateText);
    }

    public void doUpdateProfile(){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class, "Bearer " + preference.getString("token", null));

        Call<Envelope<UpdateProfileResponse>> call = service.doUpdateProfile(updateProfileRequest);
        call.enqueue(new Callback<Envelope<UpdateProfileResponse>>() {
            @Override
            public void onResponse(Call<Envelope<UpdateProfileResponse>> call, Response<Envelope<UpdateProfileResponse>> response) {
                if (response.code() == 200){
                    Toast.makeText(UpdateProfileActivity.this, "Update Profile Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(1, intent);
                    finish();
                }else if (response.code() != 200){
                    ApiError error = ErrorUtils.parseError(response);
                    if (error.getError().getName() != null){
                        Toast.makeText(UpdateProfileActivity.this, error.getError().getName().get(0), Toast.LENGTH_SHORT).show();
                    }else if (error.getError().getEmail() != null){
                        Toast.makeText(UpdateProfileActivity.this, error.getError().getEmail().get(0), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Envelope<UpdateProfileResponse>> call, Throwable t) {
                Toast.makeText(UpdateProfileActivity.this, "Error Request", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void handleUpdateProfile(View view) {
        String name = mNameProfileUpdateText.getText().toString();
        String email = mEmailProfileUpdateText.getText().toString();
        updateProfileRequest = new UpdateProfileRequest(name, email);
        doUpdateProfile();
    }
}
