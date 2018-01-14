package id.sch.smktelkom_mlg.pw.utjournal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import id.sch.smktelkom_mlg.pw.utjournal.Model.Journal;

public class HomeActivity extends AppCompatActivity {
    boolean isOpen = false;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private ProgressDialog progressDialog;
    private FloatingActionButton fab_plus, fab_logout, fab_create, fab_editacc;
    private Animation fabOpen, fabClose, fabRClockwise, fabRantiClockwise;
    private RecyclerView recyclerviewku;
    private DatabaseReference myDatabase, myDataUser;
    private FirebaseUser CUser;
    private TextView dis_name, dis_area, dis_branch, dis_nrp;
    private CircleImageView imgProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab_plus = findViewById(R.id.fab_plus);
        fab_logout = findViewById(R.id.fab_logout);
        fab_create = findViewById(R.id.fab_create);
        fab_editacc = findViewById(R.id.fab_editacc);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabRClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        fabRantiClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);


        auth = FirebaseAuth.getInstance();
        FirebaseUser userid = auth.getCurrentUser();
        String userID = userid.getUid();

        imgProfile = findViewById(R.id.pprofile);
        dis_name = findViewById(R.id.showuser);
        dis_area = findViewById(R.id.showarea);
        dis_branch = findViewById(R.id.showbranch);
        dis_nrp = findViewById(R.id.shownrp);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait while load data.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        recyclerviewku = findViewById(R.id.journal_list);
        recyclerviewku.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerviewku.setLayoutManager(linearLayoutManager);
        myDatabase = FirebaseDatabase.getInstance().getReference().child("journal");
        myDataUser = FirebaseDatabase.getInstance().getReference().child("users").child(userID);


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

        myDataUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String display_name = dataSnapshot.child("username").getValue().toString();
                String display_area = dataSnapshot.child("area").getValue().toString();
                String display_branch = dataSnapshot.child("branch").getValue().toString();
                String display_nrp = dataSnapshot.child("nrp").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                dis_name.setText(display_name);
                dis_area.setText(display_area);
                dis_branch.setText(display_branch);
                dis_nrp.setText(display_nrp);

                Picasso.with(HomeActivity.this).load(image).placeholder(R.mipmap.ic_launcher).into(imgProfile);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("journal");
        FirebaseRecyclerOptions<Journal> options =
                new FirebaseRecyclerOptions.Builder<Journal>()
                        .setQuery(myDatabase, Journal.class)
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

    public class JournalViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public JournalViewHolder(View itemView) {
            super(itemView);
            itemView = mView;
        }

        public void setActivity(String activity) {
            TextView title = mView.findViewById(R.id.journal_title);
            title.setText(activity);
        }

        public void setStart(String start) {
            TextView mulai = mView.findViewById(R.id.journal_timestart);
            mulai.setText(start);
        }

        public void setEnd(String end) {
            TextView akhir = mView.findViewById(R.id.journal_timeend);
            akhir.setText(end);
        }
    }
}
