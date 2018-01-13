package id.sch.smktelkom_mlg.pw.utjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import id.sch.smktelkom_mlg.pw.utjournal.Model.Journal;

public class HomeActivity extends AppCompatActivity {
    boolean isOpen = false;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private ProgressBar progressBar;
    private FloatingActionButton fab_plus, fab_logout, fab_create, fab_editacc;
    private Animation fabOpen, fabClose, fabRClockwise, fabRantiClockwise;
    private RecyclerView recyclerviewku;
    private DatabaseReference myDatabase;
    private FirebaseUser CUser;
    private Query query;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab_plus = findViewById(R.id.fab_plus);
        fab_logout = findViewById(R.id.fab_logout);
        fab_create = findViewById(R.id.fab_create);
        fab_editacc = findViewById(R.id.fab_editacc);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabRClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        fabRantiClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        recyclerviewku = findViewById(R.id.journal_list);
        recyclerviewku.setHasFixedSize(true);
        recyclerviewku.setLayoutManager(new LinearLayoutManager(this));
        myDatabase = FirebaseDatabase.getInstance().getReference().child("journal");

        query = FirebaseDatabase.getInstance()
                .getReference()
                .child("journal");


        auth = FirebaseAuth.getInstance();
        //get current user
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
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
                    fab_editacc.startAnimation(fabClose);
                    fab_plus.startAnimation(fabRantiClockwise);
                    fab_create.setClickable(false);
                    fab_logout.setClickable(false);
                    isOpen = false;
                } else {
                    fab_create.startAnimation(fabOpen);
                    fab_logout.startAnimation(fabOpen);
                    fab_editacc.startAnimation(fabOpen);
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
        fab_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    public void signOut() {
        auth.signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
        FirebaseRecyclerOptions<Journal> options =
                new FirebaseRecyclerOptions.Builder<Journal>()
                        .setQuery(query, Journal.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Journal, JournalViewHolder>(options) {
            @Override
            public JournalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.journal_row, parent, false);

                return new JournalViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(JournalViewHolder holder, int position, Journal model) {
                holder.setActivity(model.getActivity());
                holder.setStart(model.getStart());
                holder.setEnd(model.getEnd());
            }

        };
        recyclerviewku.setAdapter(adapter);
    }

    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    public static class JournalViewHolder extends RecyclerView.ViewHolder {
        TextView title, mulai, akhir;

        public JournalViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.journal_title);
            mulai = itemView.findViewById(R.id.journal_timestart);
            akhir = itemView.findViewById(R.id.journal_timeend);
        }

        public void setActivity(String activity) {
            title.setText(activity);
        }

        public void setStart(String start) {
            mulai.setText(start);
        }

        public void setEnd(String end) {
            akhir.setText(end);
        }
    }
}
