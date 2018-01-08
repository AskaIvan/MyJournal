package id.sch.smktelkom_mlg.pw.utjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    boolean isOpen = false;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private ProgressBar progressBar;
    private FloatingActionButton fab_plus, fab_logout, fab_create;
    private Animation fabOpen, fabClose, fabRClockwise, fabRantiClockwise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab_plus = findViewById(R.id.fab_plus);
        fab_logout = findViewById(R.id.fab_logout);
        fab_create = findViewById(R.id.fab_create);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabRClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        fabRantiClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        auth = FirebaseAuth.getInstance();
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    Intent intent = new Intent(HomeActivity.this, login2Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        };
        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    fab_create.startAnimation(fabClose);
                    fab_logout.startAnimation(fabClose);
                    fab_plus.startAnimation(fabRantiClockwise);
                    fab_create.setClickable(false);
                    fab_logout.setClickable(false);
                    isOpen = false;
                } else {
                    fab_create.startAnimation(fabOpen);
                    fab_logout.startAnimation(fabOpen);
                    fab_plus.startAnimation(fabRClockwise);
                    fab_create.setClickable(true);
                    fab_logout.setClickable(true);
                    isOpen = true;
                }
            }
        });
        fab_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    public void signOut() {
        auth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
