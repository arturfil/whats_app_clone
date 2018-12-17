package com.arturofilio.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.arturofilio.chatapp.Fragments.ChatsFragment;
import com.arturofilio.chatapp.Fragments.UsersFragment;
import com.arturofilio.chatapp.Model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView mProfileImage;
    TextView mUsername;

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference referenceDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mProfileImage = findViewById(R.id.profile_image);
        mUsername = findViewById(R.id.username);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        referenceDB = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseUser.getUid());

        referenceDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mUsername.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    mProfileImage.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(MainActivity.this).load(user.getImageURL()).into(mProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPageAdapter.addFragment(new ChatsFragment(), "Chats");
        viewPageAdapter.addFragment(new UsersFragment(), "Users");

        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return true;
        }

        return false;
    }

}
