package com.example.alexblum.tweet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ReadTweetActivity extends Activity implements View.OnClickListener{

    private Button buttonUpdate;

    private TextView Tweet1;
    private TextView Tweet2;
    private TextView Tweet3;
    private TextView Tweet4;
    private TextView Tweet5;
    private Integer i;
    private String val;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_tweet);

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        Tweet1 = (TextView) findViewById(R.id.Tweet1);
        Tweet2 = (TextView) findViewById(R.id.Tweet2);
        Tweet3 = (TextView) findViewById(R.id.Tweet3);
        Tweet4 = (TextView) findViewById(R.id.Tweet4);
        Tweet5 = (TextView) findViewById(R.id.Tweet5);

        buttonUpdate.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(ReadTweetActivity.this, "User signed in: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(ReadTweetActivity.this, "Nobody logged in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ReadTweetActivity.this, MainActivity.class);
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

    @Override
    public void onClick(View v) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataTweets = database.getReference();
        i=0;
        dataTweets.child("tweets").orderByKey().limitToLast(5).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                i=i+1;
                Tweet tweet = dataSnapshot.getValue(Tweet.class);
                if (i==1) {
                    Tweet1.setText("Time: "+tweet.timestamp+" - User: "+tweet.user+"\n"+tweet.message);
                } else if (i==2) {
                    Tweet2.setText("Time: "+tweet.timestamp+" - User: "+tweet.user+"\n"+tweet.message);
                } else if (i==3) {
                    Tweet3.setText("Time: "+tweet.timestamp+" - User: "+tweet.user+"\n"+tweet.message);
                } else if (i==4) {
                    Tweet4.setText("Time: "+tweet.timestamp+" - User: "+tweet.user+"\n"+tweet.message);
                } else if (i==5) {
                    Tweet5.setText("Time: "+tweet.timestamp+" - User: "+tweet.user+"\n"+tweet.message);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intentPost = new Intent(ReadTweetActivity.this, PostTweetActivity.class);

        if(mAuth.getCurrentUser() != null) {
            if (item.getItemId() == R.id.logout) {
                mAuth.signOut();
            } else if (item.getItemId() == R.id.read) {
                Toast.makeText(this, "You are on the Read Tweet Page already",Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.post) {
                startActivity(intentPost);
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
