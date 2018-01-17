package id.sch.smktelkom_mlg.pw.utjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private ImageView imageView;
    private TextView nUser, eUser;
    private DatabaseReference myDataUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        auth = FirebaseAuth.getInstance();
        FirebaseUser userid = auth.getCurrentUser();
        String userID = userid.getUid();
        myDataUser = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    Intent intent = new Intent(DrawerActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        changePage(R.id.nav_home);
        navigationView.setCheckedItem(R.id.nav_home);

        myDataUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NavigationView navigationView = findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                nUser = headerView.findViewById(R.id.nameUser);
                eUser = headerView.findViewById(R.id.emailUser);
                imageView = headerView.findViewById(R.id.imageView);


                String display_name = dataSnapshot.child("username").getValue().toString();
                String display_email = dataSnapshot.child("email").getValue().toString();
                Log.d("kkk", "ini usergambar" + display_name);

                String image = dataSnapshot.child("image").getValue().toString();

                nUser.setText(display_name);
                eUser.setText(display_email);

                Picasso.with(DrawerActivity.this).load(image).placeholder(R.mipmap.ic_launcher).into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        changePage(id);


        return true;
    }

    private void changePage(int id) {
        android.support.v4.app.Fragment fragment = null;

        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            setTitle("Home");
        } else if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
            setTitle("Profile");

        } else if (id == R.id.nav_download) {
            fragment = new DownloadFragment();
            setTitle("Download");

        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commitNow();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    private void logOut() {
        auth.signOut();
        Intent intent = new Intent(DrawerActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
