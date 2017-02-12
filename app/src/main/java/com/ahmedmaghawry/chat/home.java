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
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

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
    String userName;
    Firebase mainFire;
    Firebase userFire;
    Firebase friendsFire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        initializeViews();
        mainFire = new Firebase("https://chat-75842.firebaseio.com/");
        userFire = mainFire.child(userEmail);
        friendsFire = userFire.child("Friends");
        putFriendsOnListView();
        actions();
    }

    private void putFriendsOnListView() {
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
    }

    private void actions() {
        listClick();
        addClick();
    }

    private void addClick() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addedEmail = search.getText().toString().replace(".","_d");
                if (!addedEmail.equals("")) {
                    checkAndAdd(addedEmail);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter the Email", Toast.LENGTH_SHORT).show();
                    search.setText("");
                }
            }
        });
    }

    private void checkAndAdd(final String addedEmail) {
        mainFire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map> emailsMap = dataSnapshot.getValue(Map.class);
                if (emailsMap.containsKey(addedEmail)) {
                    checkAlreadyExsist(addedEmail, (String) emailsMap.get(addedEmail).get("Name"));
                } else {
                    Toast.makeText(getApplicationContext(), "There is no user has this Email", Toast.LENGTH_SHORT).show();
                    search.setText("");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void checkAlreadyExsist(final String addedEmail, final String addedName) {
        friendsFire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> friendsMap = dataSnapshot.getValue(Map.class);
                if (friendsMap.containsKey(addedEmail)) {
                    Toast.makeText(getApplicationContext(),"This user is Already your friend", Toast.LENGTH_SHORT).show();
                } else {
                    addNewFriendToMe(addedEmail, addedName);
                    addNewFriendToHim(addedEmail);
                    putFriendsOnListView();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void addNewFriendToHim(String addedEmail) {
        Firebase newFriend = mainFire.child(addedEmail).child("Friends").child(userEmail);
        Firebase chatBetween = newFriend.child("Chat");
        Firebase nameFriend = newFriend.child("Name");
        Firebase colorFriend = newFriend.child("Color");
        colorFriend.setValue("Black");
        nameFriend.setValue(userName);
        chatBetween.child("Text1").setValue("You Are Now Friend with "+userName);
    }

    private void addNewFriendToMe(String addedEmail,String addedName) {
        Firebase newFriend = friendsFire.child(addedEmail);
        Firebase chatBetween = newFriend.child("Chat");
        Firebase nameFriend = newFriend.child("Name");
        Firebase colorFriend = newFriend.child("Color");
        colorFriend.setValue("Black");
        nameFriend.setValue(addedName);
        chatBetween.child("Text1").setValue("You Are Now Friend with "+addedName);
    }

    private void listClick() {
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
        userName = intent.getStringExtra("Name");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
