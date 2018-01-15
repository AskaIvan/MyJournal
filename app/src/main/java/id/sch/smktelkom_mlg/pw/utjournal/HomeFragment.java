package id.sch.smktelkom_mlg.pw.utjournal;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import id.sch.smktelkom_mlg.pw.utjournal.Model.Journal;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private FloatingActionButton fab_create;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private ProgressDialog progressDialog;
    private DatabaseReference myDataUser;
    private RecyclerView recyclerviewku;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        fab_create = rootview.findViewById(R.id.fab_create);

        auth = FirebaseAuth.getInstance();
        FirebaseUser userid = auth.getCurrentUser();
        String userID = userid.getUid();
        myDataUser = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

        recyclerviewku = rootview.findViewById(R.id.journal_list);
        //recyclerviewku.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerviewku.setLayoutManager(linearLayoutManager);

        progressDialog = new ProgressDialog(container.getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait while load data.");
        progressDialog.setCanceledOnTouchOutside(false);
        //progressDialog.show();

        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };


        fab_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });

        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
        Query query = FirebaseDatabase.getInstance().getReference().child("journal");
        FirebaseRecyclerOptions<Journal> options =
                new FirebaseRecyclerOptions.Builder<Journal>()
                        .setQuery(query, Journal.class)
                        .build();
        Log.d("kkk", "ini sampek option" + options);

        FirebaseRecyclerAdapter<Journal, JournalViewHolder> adapter = new FirebaseRecyclerAdapter<Journal, JournalViewHolder>(options) {
            @Override
            public JournalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.journal_row, parent, false);
                Log.d("kkk", "ini sampek view" + view);
                return new JournalViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(JournalViewHolder holder, int position, Journal model) {
                Log.d("kkk", "model:" + model.toString());

                holder.setActivity(model.getActivity());
                holder.setStart(model.getStart());
                holder.setEnd(model.getEnd());
            }

        };
        recyclerviewku.setAdapter(adapter);
        adapter.startListening();

    }

    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
        Query query = FirebaseDatabase.getInstance().getReference().child("journal");
        FirebaseRecyclerOptions<Journal> options =
                new FirebaseRecyclerOptions.Builder<Journal>()
                        .setQuery(query, Journal.class)
                        .build();
        Log.d("kkk", "ini sampek option" + options);

        FirebaseRecyclerAdapter<Journal, JournalViewHolder> adapter = new FirebaseRecyclerAdapter<Journal, JournalViewHolder>(options) {
            @Override
            public JournalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.journal_row, parent, false);
                Log.d("kkk", "ini sampek view" + view);
                return new JournalViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(JournalViewHolder holder, int position, Journal model) {
                Log.d("kkk", "model:" + model.toString());

                holder.setActivity(model.getActivity());
                holder.setStart(model.getStart());
                holder.setEnd(model.getEnd());
            }

        };
        recyclerviewku.setAdapter(adapter);
        adapter.startListening();
    }

    private class JournalViewHolder extends RecyclerView.ViewHolder {

        public JournalViewHolder(View itemView) {
            super(itemView);
        }

        public void setActivity(String activity) {
            TextView title = itemView.findViewById(R.id.journal_title);
            title.setText(activity);
        }

        public void setStart(String start) {
            TextView mulai = itemView.findViewById(R.id.journal_timestart);
            mulai.setText(start);
        }

        public void setEnd(String end) {
            TextView akhir = itemView.findViewById(R.id.journal_timeend);
            akhir.setText(end);
        }
    }

}
