package id.sch.smktelkom_mlg.pw.utjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditActivity extends AppCompatActivity {

    private String mJournal_key;

    private DatabaseReference mDatabase;

    private TextView edtVenue, edtVendor, edtcodejob, edtcategory, edtcodecategory, edtactivity, edtdescription, edtsapsomp, edtstart, edtend, edthours, edtunittype, edtremark, edMonth;

    private Button btndelete;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        auth = FirebaseAuth.getInstance();
        mJournal_key = getIntent().getExtras().getString("journalid");
        String user_id = auth.getCurrentUser().getUid();
        Log.d("ini key", "keynya" + mJournal_key);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("journal").child(user_id);


        edtcodejob = findViewById(R.id.edtcodejob1);
        edtcategory = findViewById(R.id.edtcategory1);
        edtcodecategory = findViewById(R.id.edtcodecat1);
        edtactivity = findViewById(R.id.edtactivity1);
        edtdescription = findViewById(R.id.edtdescription1);
        edtsapsomp = findViewById(R.id.edtsapsomp1);
        edMonth = findViewById(R.id.edMonth1);
        edtstart = findViewById(R.id.edtstart1);
        edtend = findViewById(R.id.edtend1);
        edthours = findViewById(R.id.edthours1);
        edtVenue = findViewById(R.id.edtVenue1);
        edtVendor = findViewById(R.id.edtVendor1);
        edtunittype = findViewById(R.id.edtunit1);
        edtremark = findViewById(R.id.edtremark1);

        btndelete = findViewById(R.id.btnDelete1);


        mDatabase.child(mJournal_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String codejob = (String) dataSnapshot.child("codejob").getValue();
                String category = (String) dataSnapshot.child("category").getValue();
                String codecategory = (String) dataSnapshot.child("codecategory").getValue();
                String activity = (String) dataSnapshot.child("activity").getValue();
                String description = (String) dataSnapshot.child("description").getValue();
                String sapsomp = (String) dataSnapshot.child("sapsomp").getValue();
                String month = (String) dataSnapshot.child("month").getValue();
                String startdate = (String) dataSnapshot.child("start").getValue();
                String enddate = (String) dataSnapshot.child("end").getValue();
                String hours = (String) dataSnapshot.child("hours").getValue();
                String venue = (String) dataSnapshot.child("venue").getValue();
                String vendor = (String) dataSnapshot.child("vendor").getValue();
                String unittype = (String) dataSnapshot.child("unittype").getValue();
                String remark = (String) dataSnapshot.child("remark").getValue();

                String journal_uid = (String) dataSnapshot.child("uid").getValue();


                edtcodejob.setText(codejob);
                edtcategory.setText(category);
                edtcodecategory.setText(codecategory);
                edtactivity.setText(activity);
                edtdescription.setText(description);
                edtsapsomp.setText(sapsomp);
                edMonth.setText(month);
                edtstart.setText(startdate);
                edtend.setText(enddate);
                edthours.setText(hours);
                edtVenue.setText(venue);
                edtVendor.setText(vendor);
                edtunittype.setText(unittype);
                edtremark.setText(remark);

                if (auth.getCurrentUser().getUid().equals(journal_uid)) {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(mJournal_key).removeValue();
                Intent intent = new Intent(EditActivity.this, DrawerActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
