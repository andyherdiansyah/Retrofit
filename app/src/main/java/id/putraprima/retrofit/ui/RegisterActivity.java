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
import id.putraprima.retrofit.api.models.Envelope;
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

    public void register(){
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Envelope<RegisterResponse>> call = service.doRegister(registerRequest);
        call.enqueue(new Callback<Envelope<RegisterResponse>>() {
            @Override
            public void onResponse(Call<Envelope<RegisterResponse>> call, Response<Envelope<RegisterResponse>> response) {
                if (response.code() !=201 ){
                    Toast.makeText(RegisterActivity.this, "Register Gagal Email Sudah Terdaftar", Toast.LENGTH_SHORT).show();
                } else if(response.code() == 201){
                    Toast.makeText(RegisterActivity.this, "Register Berhasil", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterActivity.this, response.body().getData().getEmail(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterActivity.this, response.body().getData().getName(), Toast.LENGTH_SHORT).show();
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

        boolean check;
        if (name.equals("")) {
            Toast.makeText(this, "Input your name !", Toast.LENGTH_SHORT).show();
            check = false;
        } else if (email.equals("")) {
            Toast.makeText(this, "Input your email !", Toast.LENGTH_SHORT).show();
            check = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Your email is invalid  !", Toast.LENGTH_SHORT).show();
            check = false;
        } else if (password.equals("")) {
            Toast.makeText(this, "Input your password !", Toast.LENGTH_SHORT).show();
            check = false;
        } else if (password.length() < 8) {
            Toast.makeText(this, "Password must be more than 8 characters", Toast.LENGTH_SHORT).show();
            check = false;
        } else if (password_confirmation.equals("")) {
            Toast.makeText(this, "Input your confirm password !", Toast.LENGTH_SHORT).show();
            check = false;
        } else if (!password.equals(password_confirmation)) {
            Toast.makeText(this, "Password and Password Confirm must same !", Toast.LENGTH_SHORT).show();
            check = false;
        } else {
            check = true;
        }

        if (check == true) {
            register();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}