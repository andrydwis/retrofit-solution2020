package id.putraprima.retrofit.ui;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.Envelope;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.RegisterRequest;
import id.putraprima.retrofit.api.models.RegisterResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    public RegisterRequest registerRequest;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mPasswordConfirm;
    private EditText mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mPasswordConfirm = findViewById(R.id.password_confirm);
        mEmail = findViewById(R.id.email);
    }

    public void register() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Envelope<RegisterResponse>> call = service.doRegister(registerRequest);
        call.enqueue(new Callback<Envelope<RegisterResponse>>() {
            @Override
            public void onResponse(Call<Envelope<RegisterResponse>> call, Response<Envelope<RegisterResponse>> response) {
               if (response.code() == 201){
                   Toast.makeText(RegisterActivity.this, "Register Successfull", Toast.LENGTH_SHORT).show();
               }else if (response.code() != 201){
                   ApiError error = ErrorUtils.parseError(response);
                   if (error.getError().getName() != null){
                       Toast.makeText(RegisterActivity.this, error.getError().getName().get(0), Toast.LENGTH_SHORT).show();
                   }else if (error.getError().getEmail() !=null){
                       Toast.makeText(RegisterActivity.this, error.getError().getEmail().get(0), Toast.LENGTH_SHORT).show();
                   }else if (error.getError().getPassword() != null){
                       for (int i = 0;i < error.getError().getPassword().size();i++){
                           Toast.makeText(RegisterActivity.this, error.getError().getPassword().get(i), Toast.LENGTH_SHORT).show();
                       }
                   }
               }
            }

            @Override
            public void onFailure(Call<Envelope<RegisterResponse>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error Request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handlerRegisterProcess(View view) {
        String name = mUsername.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String password_confirm = mPasswordConfirm.getText().toString();
        registerRequest = new RegisterRequest(name, email, password, password_confirm);
        register();
    }
}
