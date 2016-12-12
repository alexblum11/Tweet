package com.example.alexblum.tweet;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class PostTweetActivity extends Activity implements View.OnClickListener{

    private Button buttonTweet;
    private Button buttonClear;
    private EditText editTextTweet;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_tweet);

        buttonTweet = (Button) findViewById(R.id.buttonTweet);
        buttonClear = (Button) findViewById(R.id.buttonClear);
        editTextTweet = (EditText) findViewById(R.id.editTextTweet);

        buttonClear.setOnClickListener(this);
        buttonTweet.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(PostTweetActivity.this, "User signed in: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(PostTweetActivity.this, "Nobody logged in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostTweetActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                // ...
            }
        };
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (v==buttonClear) {
            editTextTweet.setText("");
        } else if (v==buttonTweet) {
            String message = editTextTweet.getText().toString();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String ts = dateFormat.format(new Date()); // Finds current time UTC+-0
            Tweet tweet = new Tweet(message,user.getEmail(),ts);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference dataTweets = database.getReference("tweets");
            DatabaseReference dataNewTweet = dataTweets.push();
            dataNewTweet.setValue(tweet);
            Toast.makeText(this, "Your tweet was posted.", Toast.LENGTH_SHORT).show();
            editTextTweet.setText("");
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intentRead = new Intent(PostTweetActivity.this, ReadTweetActivity.class);

        if(mAuth.getCurrentUser() != null) {
            if (item.getItemId() == R.id.logout) {
                mAuth.signOut();
            } else if (item.getItemId() == R.id.post) {
                Toast.makeText(this, "You are on the Post Tweet Page already",Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.read) {
                startActivity(intentRead);
            }
        } else {
            Toast.makeText(this, "Nobody logged in", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }


}
