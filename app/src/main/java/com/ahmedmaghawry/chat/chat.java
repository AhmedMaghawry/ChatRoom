package com.ahmedmaghawry.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

/**
 * Created by Ahmed Maghawry on 2/11/2017.
 */
public class chat extends Activity {

    EditText chatArea;
    Button sendButton;
    Firebase mainFire;
    Firebase currentFire;
    Firebase sendToFire;
    String currentUser;
    String sendToUser;
    static String userName;
    static int counter;
    ListView list;
    Firebase chatCurrent;
    Firebase chatSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);
        Intent data = getIntent();
        currentUser = data.getStringExtra("Sender");
        sendToUser = data.getStringExtra("Receiver");
        chatArea = (EditText) findViewById(R.id.text);
        sendButton = (Button) findViewById(R.id.send);
        list = (ListView) findViewById(R.id.listView2);
        mainFire = new Firebase("@string/fireBaseName");
        currentFire = mainFire.child(currentUser);
        sendToFire = mainFire.child(sendToUser);
        chatCurrent = currentFire.child("Friends").child(sendToUser).child("Chat");
        chatSend = sendToFire.child("Friends").child(currentUser).child("Chat");
        initializeCounter();
        initializeName();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chatContent = chatArea.getText().toString();
                if(chatContent != null) {
                    send(chatContent);
                }
            }
        });
    }

    private void send(String chatContent) {
        counter++;
        //add at the sender console
        Firebase newMessage = chatCurrent.child("Text"+counter);
        newMessage.setValue(userName + " : " + chatContent);
        //add at the send to user console
        Firebase newMessageRecevier = chatSend.child("Text"+counter);
        newMessageRecevier.setValue(userName + " : " + chatContent);
    }

    private void initializeCounter() {
        chatCurrent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                counter = (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void initializeName() {
        currentFire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> namee = dataSnapshot.getValue(Map.class);
                userName = namee.get("Name");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
