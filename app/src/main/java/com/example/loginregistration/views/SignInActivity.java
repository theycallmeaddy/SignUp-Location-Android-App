package com.example.loginregistration.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginregistration.R;
import com.example.loginregistration.database.UserDao;
import com.example.loginregistration.database.UserDatabase;
import com.example.loginregistration.models.User;

public class SignInActivity extends AppCompatActivity {

    EditText email,password;
    Button login;
    TextView signUp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signUp = (TextView) this.findViewById(R.id.signUpButton);
        email = findViewById(R.id.emailET);
        password = findViewById(R.id.passwordET);
        login = findViewById(R.id.signInButton);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailIdText = email.getText().toString();
                String passwordText = password.getText().toString();
                if(emailIdText.isEmpty()||passwordText.isEmpty())
                {
                    Toast.makeText(SignInActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else{

                    //Database Initialization
                    UserDatabase userDatabase = UserDatabase.userDB(getApplicationContext());
                    UserDao userDao = userDatabase.userDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            User user = userDao.findUser(emailIdText,passwordText);
                            if(user == null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SignInActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                //calling login method
                                String name = user.getName();
                                String email = user.getEmail();
                                String phone = user.getPhoneNo();
                                Intent intent = new Intent(SignInActivity.this,ProfileActivity.class);
                                intent.putExtra("name",name);
                                intent.putExtra("phone",phone);
                                intent.putExtra("email",email);

                                startActivity(intent);
                            }
                        }
                    }).start();
                }


            }
        });
    }

}
