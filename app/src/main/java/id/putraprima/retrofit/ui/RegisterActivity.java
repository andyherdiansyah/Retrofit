package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
    RegisterRequest registerRequest;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editName;
    private EditText editPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editName = findViewById(R.id.editName);
        editPasswordConfirm = findViewById(R.id.editConfirmPassword);
    }

    public void register() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Envelope<RegisterResponse>> call = service.doRegister(registerRequest);
        call.enqueue(new Callback<Envelope<RegisterResponse>>() {
            @Override
            public void onResponse(Call<Envelope<RegisterResponse>> call, Response<Envelope<RegisterResponse>> response) {
                if (response.isSuccessful()){

                    Toast.makeText(RegisterActivity.this, "Register Successfull", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    ApiError error = ErrorUtils.parseError(response);
                    if(error.getError().getName()!= null){
                        Toast.makeText(RegisterActivity.this, error.getError().getName().get(0), Toast.LENGTH_SHORT).show();
                    }else if (error.getError().getEmail()!=null){
                        Toast.makeText(RegisterActivity.this, error.getError().getEmail().get(0), Toast.LENGTH_SHORT).show();
                    } else  if(error.getError().getPassword()!= null){
                        Toast.makeText(RegisterActivity.this, error.getError().getPassword().get(0), Toast.LENGTH_SHORT).show();
                    }else if (error.getError().getConfirmationPassword()!=null){
                        Toast.makeText(RegisterActivity.this, error.getError().getConfirmationPassword().get(0), Toast.LENGTH_SHORT).show();
                    }else if (error.getError().getPassword()!=null){
                        for (int k = 0 ; k < error.getError().getPassword().size(); k++) {
                            Toast.makeText(RegisterActivity.this, error.getError().getPassword().get(k), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Envelope<RegisterResponse>> call, Throwable t) {
            }
        });

    }

    public void handleRegister(View view) {
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String password_confirmation = editPasswordConfirm.getText().toString();
        registerRequest = new RegisterRequest(name, email, password, password_confirmation);
        register();
        }
    }