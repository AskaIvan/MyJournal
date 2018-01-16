package id.sch.smktelkom_mlg.pw.utjournal;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {
    private static final String EXCEL_FILE_LOCATION = "/MyFirstExcel.xls";
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference myDataUser, journal;
    private EditText nEditFileName, nEdstart, nEdend;
    private Button nBtnDownload;


    public DownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_download, container, false);

        nEditFileName = rootview.findViewById(R.id.edfilename);
        nEdstart = rootview.findViewById(R.id.edtstart);
        nEdend = rootview.findViewById(R.id.edtend);
        nBtnDownload = rootview.findViewById(R.id.btnDownload);

        auth = FirebaseAuth.getInstance();
        FirebaseUser userid = auth.getCurrentUser();
        String userID = userid.getUid();
        myDataUser = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        journal = FirebaseDatabase.getInstance().getReference().child("journal").child(userID);


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

        nBtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startsave();
            }
        });


        return rootview;
    }

    private void startsave() {
        myDataUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
