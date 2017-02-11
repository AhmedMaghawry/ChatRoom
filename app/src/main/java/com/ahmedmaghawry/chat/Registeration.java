package com.ahmedmaghawry.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
public class Registeration extends Activity {

    EditText name;
    EditText email;
    EditText password;
    EditText rePassword;
    Button cancel;
    Button confirm;
    TextView errorMessage;
    Intent goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeration_page);
        initializeViews();
        action();
    }

    private void action() {
        confirmBT();
        cancelBT();
    }

    private void confirmBT() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                    if (checkPassword()) {
                        checkEmail();
                    } else {
                        error("Password");
                    }
                } else {
                    error("Empty");
                }
            }
        });
    }

    private void checkEmail() {
        final Firebase firebase = new Firebase("@string/fireBaseName");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map> allUsersMap = dataSnapshot.getValue(Map.class);
                if (allUsersMap.containsKey(email.getText().toString())) {
                    error("Email");
                } else {
                    Firebase newUser = firebase.child((email.getText().toString()).replace(".","_d"));
                    Firebase nameFire = newUser.child("Name");
                    nameFire.setValue(name.getText().toString());
                    Firebase passFire = newUser.child("Password");
                    passFire.setValue(password.getText().toString());
                    startActivity(goBack);
                    finish();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private boolean checkPassword() {
        if ((password.getText().toString()).equals(rePassword.getText().toString()))
            return true;
        else
            return false;
    }

    private boolean checkFields() {
        if (!(name.getText().toString()).equals(""))
            if (!(email.getText().toString()).equals(""))
                if(!(password.getText().toString()).equals(""))
                    if(!(rePassword.getText().toString()).equals(""))
                        return true;
        return false;
    }

    private void cancelBT() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goBack);
                finish();
            }
        });
    }

    private void initializeViews() {
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        rePassword = (EditText) findViewById(R.id.repassword);
        cancel = (Button) findViewById(R.id.cancel);
        confirm = (Button) findViewById(R.id.confirm);
        errorMessage = (TextView) findViewById(R.id.errorMessage);
        goBack = new Intent(Registeration.this, Login.class);
    }

    private void error(String err) {
        if (err.equals("Email")) {
            errorMessage.setText("This Email is already exsist");
        } else if (err.equals("Password")) {
            errorMessage.setText("The confirm password isn't match");
        } else if (err.equals("Empty")) {
            errorMessage.setText("There are some Fields empty");
        }
        clearAllFields();
    }

    private void clearAllFields() {
        name.setText("");
        email.setText("");
        password.setText("");
        rePassword.setText("");
    }
}
