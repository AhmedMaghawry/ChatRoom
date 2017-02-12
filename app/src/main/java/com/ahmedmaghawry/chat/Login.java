package com.ahmedmaghawry.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

/**
 * Created by Ahmed Maghawry on 2/10/2017.
 */
public class Login extends Activity{

    EditText emailET;
    EditText passwordET;
    Button loginBT;
    Button regBT;
    TextView error;
    static String email;
    static String password;
    static boolean res = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        initializeViews();
        actions();
    }

    private void actions() {
        loginAction();
        regAction();
    }

    private void regAction() {
        regBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent enterReg = new Intent(Login.this, Registeration.class);
                startActivity(enterReg);
                finish();
            }
        });
    }

    private void loginAction() {
        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEmail = emailET.getText().toString();
                email = enteredEmail.replace(".", "_d");
                password = passwordET.getText().toString();
                checkExistance();
            }
        });
    }

    private void checkExistance() {
        Firebase firebase = new Firebase("https://chat-75842.firebaseio.com/");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map> allUsersMap = dataSnapshot.getValue(Map.class);
                if (allUsersMap.containsKey(email)) {
                    if(checkPassword(allUsersMap)) {
                        Intent enterHome = new Intent(Login.this, home.class);
                        enterHome.putExtra("Email",email);
                        enterHome.putExtra("Name",(String)allUsersMap.get(email).get("Name"));
                        startActivity(enterHome);
                        finish();
                    } else {
                        emailET.setText("");
                        passwordET.setText("");
                        error.setText("Invalid Input");
                    }
                } else {
                    emailET.setText("");
                    passwordET.setText("");
                    error.setText("Invalid Input");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private boolean checkPassword(Map<String, Map> allUsersMap) {
        Map<String,Object> passwordMap = allUsersMap.get(email);
        if(passwordMap.get("Password").equals(this.password)) {
            return true;
        } else {
            return false;
        }
    }

    private void initializeViews() {
        emailET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        loginBT = (Button) findViewById(R.id.loginBT);
        regBT = (Button) findViewById(R.id.regiserBT);
        error = (TextView) findViewById(R.id.error);
    }
}
