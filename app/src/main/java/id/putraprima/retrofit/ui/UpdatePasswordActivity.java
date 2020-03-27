package id.putraprima.retrofit.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.Envelope;
import id.putraprima.retrofit.api.models.UpdatePasswordRequest;
import id.putraprima.retrofit.api.models.UpdatePasswordResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText mPasswordPasswordUpdateText;
    private EditText mEmailPasswordUpdateText;

    private UpdatePasswordRequest updatePasswordRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        mPasswordPasswordUpdateText = findViewById(R.id.passwordPasswordUpdateText);
        mEmailPasswordUpdateText = findViewById(R.id.emailPasswordUpdateText);
    }

    public void doUpdatePassword(){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class, "Bearer " + preference.getString("token", null));

        Call<Envelope<UpdatePasswordResponse>> call = service.doUpdatePassword(updatePasswordRequest);
        call.enqueue(new Callback<Envelope<UpdatePasswordResponse>>() {
            @Override
            public void onResponse(Call<Envelope<UpdatePasswordResponse>> call, Response<Envelope<UpdatePasswordResponse>> response) {
                if (response.code() == 200){
                    Toast.makeText(UpdatePasswordActivity.this, "Update Password Success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UpdatePasswordActivity.this, "Update Password Failed", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent();
                setResult(2, intent);
                finish();
            }

            @Override
            public void onFailure(Call<Envelope<UpdatePasswordResponse>> call, Throwable t) {
                Toast.makeText(UpdatePasswordActivity.this, "Error Request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleUpdatePassword(View view) {
        String password = mPasswordPasswordUpdateText.getText().toString();
        String password_confirm = mPasswordPasswordUpdateText.getText().toString();
        updatePasswordRequest = new UpdatePasswordRequest(password, password_confirm);

        boolean check;
        if (password.equals("")) {
            Toast.makeText(this, "Password is Empty!", Toast.LENGTH_SHORT).show();
            check = false;
        } else if (password_confirm.equals("")) {
            Toast.makeText(this, "Password Confirmation is Empty!", Toast.LENGTH_SHORT).show();
            check = false;
        } else if (password.length() < 8) {
            Toast.makeText(this, "Password limit 8", Toast.LENGTH_SHORT).show();
            check = false;
        } else if (!password_confirm.equals(password)) {
            Toast.makeText(this, "Confirm Password not Same!", Toast.LENGTH_SHORT).show();
            check = false;
        } else {
            check = true;
        }
        Toast.makeText(this, "New Password : "+password, Toast.LENGTH_SHORT).show();
        if (check == true) {
            doUpdatePassword();
        }
    }
}
