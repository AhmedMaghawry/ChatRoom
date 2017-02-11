package com.ahmedmaghawry.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Ahmed Maghawry on 2/10/2017.
 */
public class home extends Activity {

    EditText search;
    Button add;
    ListView listView;
    ArrayList<String> friends;
    ArrayAdapter<String> adapter;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        initializeViews();
        Firebase mainFire = new Firebase("@string/fireBaseName");
        Firebase userFire = mainFire.child(userEmail);
        Firebase friendsFire = userFire.child("Friends");
        friendsFire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    friends.add(x.getKey());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent chatIt = new Intent(home.this,chat.class);
                chatIt.putExtra("Sender",userEmail);
                chatIt.putExtra("Receiver",friends.get(position));
                startActivity(chatIt);
                finish();
            }
        });
    }

    private void initializeViews() {
        search = (EditText) findViewById(R.id.searchBox);
        add = (Button) findViewById(R.id.add);
        listView = (ListView) findViewById(R.id.listView);
        friends = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, friends);
        listView.setAdapter(adapter);
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("Email");
    }
}
