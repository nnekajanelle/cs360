package com.app.weightloss.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.app.weightloss.R;
import com.app.weightloss.db.DBHelper;
import com.app.weightloss.model.User;


public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;

    private TextView btnSignUp;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DBHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Prefill admin credentials (optional)
    //    etEmail.setText("test@gmail.com");
    //    etPassword.setText("abcd1234");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter email & password", Toast.LENGTH_SHORT).show();
                    return;
                }


                // 1) Check if normal User
                User user = dbHelper.getUserByEmailPassword(email, password);
                if(user != null){

                    if (user.getAge() != 0){
                        // Go to user flow
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra("USER_ID", user.getUserId());
                        startActivity(i);
                        finish();
                    }else{
                        // Go to user flow
                        Intent i = new Intent(LoginActivity.this, OnboardingActivity.class);
                        i.putExtra("USER_ID", user.getUserId());
                        startActivity(i);
                        finish();
                    }

                } else {
                    // 3) Invalid
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // SignUp for normal user
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }
}
