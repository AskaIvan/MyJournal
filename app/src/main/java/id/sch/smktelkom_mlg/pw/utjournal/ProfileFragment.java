package id.sch.smktelkom_mlg.pw.utjournal;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private TextView dis_name, dis_area, dis_branch, dis_nrp, dis_email;
    private CircleImageView imgProfile;
    private FirebaseAuth auth;
    private DatabaseReference myDataUser;
    private ProgressDialog progressDialog;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        FirebaseUser userid = auth.getCurrentUser();
        String userID = userid.getUid();
        myDataUser = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

        progressDialog = new ProgressDialog(container.getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait while load data.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        imgProfile = rootview.findViewById(R.id.pprofile);
        dis_name = rootview.findViewById(R.id.showuser);
        dis_area = rootview.findViewById(R.id.showarea);
        dis_branch = rootview.findViewById(R.id.showbranch);
        dis_nrp = rootview.findViewById(R.id.shownrp);
        dis_email = rootview.findViewById(R.id.showemail);

        myDataUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String display_name = dataSnapshot.child("username").getValue().toString();
                String display_area = dataSnapshot.child("area").getValue().toString();
                String display_branch = dataSnapshot.child("branch").getValue().toString();
                String display_nrp = dataSnapshot.child("nrp").getValue().toString();
                String display_email = dataSnapshot.child("email").getValue().toString();

                String image = dataSnapshot.child("image").getValue().toString();

                dis_name.setText(display_name);
                dis_area.setText(display_area);
                dis_branch.setText(display_branch);
                dis_nrp.setText(display_nrp);
                dis_email.setText(display_email);

                Picasso.with(getContext()).load(image).placeholder(R.mipmap.ic_launcher).into(imgProfile);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootview;

    }

}
