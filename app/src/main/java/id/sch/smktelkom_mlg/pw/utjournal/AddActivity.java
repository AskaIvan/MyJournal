package id.sch.smktelkom_mlg.pw.utjournal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    private DatabaseReference journal, myDatabaseUser;
    private FirebaseDatabase database;

    private EditText edtcodejob, edtcategory, edtcodecategory, edtactivity, edtdescription, edtsapsomp, edtstart, edtend, edthours, edtunittype, edtremark;
    private Spinner spinnerMonth, spinnerVenue, spinnerVendor;
    private Button btnadd;
    private int mYear, mMonth, mDay;
    private ProgressDialog mProgress;
    private FirebaseAuth auth;
    private FirebaseUser CUser;
    private FirebaseAuth.AuthStateListener authListener;

    private String mpost_key = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        auth = FirebaseAuth.getInstance();
        CUser = auth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        journal = database.getReference().child("journal");

        myDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users").child(CUser.getUid());


        edtcodejob = findViewById(R.id.edtcodejob);
        edtcategory = findViewById(R.id.edtcategory);
        edtcodecategory = findViewById(R.id.edtcodecat);
        edtactivity = findViewById(R.id.edtactivity);
        edtdescription = findViewById(R.id.edtdescription);
        edtsapsomp = findViewById(R.id.edtsapsomp);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        edtstart = findViewById(R.id.edtstart);
        edtend = findViewById(R.id.edtend);
        edthours = findViewById(R.id.edthours);
        spinnerVenue = findViewById(R.id.spinnerVenue);
        spinnerVendor = findViewById(R.id.spinnerVendor);
        edtunittype = findViewById(R.id.edtunit);
        edtremark = findViewById(R.id.edtremark);
        btnadd = findViewById(R.id.btnAdd);
        auth = FirebaseAuth.getInstance();


        mProgress = new ProgressDialog(this);
        //final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    Intent intent = new Intent(AddActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        };

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setText(null);
                    ((TextView) view.findViewById(android.R.id.text1)).setHint("Select Month");
                    ((TextView) parent.getChildAt(0)).setTextSize(18);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.venue, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVenue.setAdapter(adapter1);
        spinnerVenue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setText(null);
                    ((TextView) view.findViewById(android.R.id.text1)).setHint("Select Venue");
                    ((TextView) parent.getChildAt(0)).setTextSize(18);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.vendor, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVendor.setAdapter(adapter2);
        spinnerVendor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setText(null);
                    ((TextView) view.findViewById(android.R.id.text1)).setHint("Select Vendor");
                    ((TextView) parent.getChildAt(0)).setTextSize(18);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        updateDisplay();
                    }

                    private void updateDisplay() {
                        edtstart.setText(
                                new StringBuilder()
                                        .append(mMonth + 1).append("/")
                                        .append(mDay).append("/")
                                        .append(mYear));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Start Date");
                mDatePicker.show();
            }
        });

        edtend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        updateDisplay();
                    }

                    private void updateDisplay() {
                        edtend.setText(
                                new StringBuilder()
                                        .append(mMonth + 1).append("/")
                                        .append(mDay).append("/")
                                        .append(mYear));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("End Date");
                mDatePicker.show();
            }
        });


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkConnectionAvailable()) {
                    startSaving();
                } else {
                    /*try {
                        AlertDialog alertDialog = new AlertDialog.Builder(AddActivity.this).create();

                        alertDialog.setTitle("Info");
                        alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();

                            }
                        });

                        alertDialog.show();
                    } catch (Exception e) {
                        Log.d("Network", "Show Dialog: " + e.getMessage());
                    }*/
                    checkNetworkConnection();
                }
            }
        });
    }

    public void checkNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        if (isConnected) {
            Log.d("Network", "Connected");
            return true;
        } else {
            checkNetworkConnection();
            Log.d("Network", "Not Connected");
            return false;
        }
    }

    private void startSaving() {
        mProgress.setMessage("Saving ... ");

        final String mCodejob = edtcodejob.getText().toString().trim();
        final String mCatgeory = edtcategory.getText().toString().trim();
        final String mCodecategory = edtcodecategory.getText().toString().trim();
        final String mActivity = edtactivity.getText().toString().trim();
        final String mDescription = edtdescription.getText().toString().trim();
        final String mSapsomp = edtsapsomp.getText().toString().trim();
        final String mMonth = spinnerMonth.getSelectedItem().toString().trim();
        final String mStart = edtstart.getText().toString().trim();
        final String mEnd = edtend.getText().toString().trim();
        final String mHours = edthours.getText().toString().trim();
        final String mVenue = spinnerVenue.getSelectedItem().toString().trim();
        final String mVendor = spinnerVendor.getSelectedItem().toString().trim();
        final String mUnittype = edtunittype.getText().toString().trim();
        final String mRemark = edtremark.getText().toString().trim();

        if (!TextUtils.isEmpty(mCodejob) && !TextUtils.isEmpty(mCatgeory) && !TextUtils.isEmpty(mCodecategory) && !TextUtils.isEmpty(mActivity) && !TextUtils.isEmpty(mDescription) &&
                !TextUtils.isEmpty(mSapsomp) && !TextUtils.isEmpty(mMonth) && spinnerMonth.getSelectedItem() != null && spinnerVenue.getSelectedItem() != null && spinnerVendor.getSelectedItem() != null && !TextUtils.isEmpty(mStart) && !TextUtils.isEmpty(mEnd) && !TextUtils.isEmpty(mHours) && !TextUtils.isEmpty(mVenue) &&
                !TextUtils.isEmpty(mVendor) && !TextUtils.isEmpty(mUnittype) && !TextUtils.isEmpty(mRemark)) {
            mProgress.show();

            final DatabaseReference newJournal = journal.push();

            myDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    newJournal.child("codejob").setValue(mCodejob);
                    newJournal.child("category").setValue(mCatgeory);
                    newJournal.child("codecategory").setValue(mCodecategory);
                    newJournal.child("activity").setValue(mActivity);
                    newJournal.child("description").setValue(mDescription);
                    newJournal.child("sapsomp").setValue(mSapsomp);
                    newJournal.child("month").setValue(mMonth);
                    newJournal.child("start").setValue(mStart);
                    newJournal.child("end").setValue(mEnd);
                    newJournal.child("hours").setValue(mHours);
                    newJournal.child("venue").setValue(mVenue);
                    newJournal.child("vendor").setValue(mVendor);
                    newJournal.child("unittype").setValue(mUnittype);
                    newJournal.child("remark").setValue(mRemark);
                    newJournal.child("uid").setValue(CUser.getUid());
                    newJournal.child("name").setValue(dataSnapshot.child("username").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddActivity.this, "Data Added Successfully!", Toast.LENGTH_SHORT).show();
                                Intent main = new Intent(AddActivity.this, DrawerActivity.class);
                                startActivity(main);
                                finish();
                            }
                        }
                    });

                    mProgress.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(AddActivity.this, "Data Added Failed!",
                    Toast.LENGTH_SHORT).show();
        }

        //private void addJournal(final String codejob, final String category, final String codecategory, final String activity, final String description, final String sapsomp, final String month, final String start, final String end, final String hours, final String venue, final String vendor, final String unittype, final String remark) {
        //  Journal journal = new Journal(codejob, category, codecategory, activity, description, sapsomp, month, start, end, hours, venue, vendor, unittype, remark);

        //journal.child(journals).


    }
}

