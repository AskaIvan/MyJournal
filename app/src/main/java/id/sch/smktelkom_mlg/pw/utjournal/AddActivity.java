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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {

    private DatabaseReference journal, myDatabaseUser;
    private FirebaseDatabase database;

    private EditText edtcodejob, edtcategory, edtcodecategory, edtactivity, edtsapsomp, edtstart, edtend, edthours, edtunittype, edtremark, edMonth;
    private Spinner spinnerVenue, spinnerVendor, spinnerCodeJob;
    private Spinner edtDescription;
    private Button btnadd;
    private int mYear, mMonth, mDay;
    private ProgressDialog mProgress;
    private FirebaseAuth auth;
    private FirebaseUser CUser;
    private FirebaseAuth.AuthStateListener authListener;
    private String mpost_key = null;
    private ArrayList<String> desc;
    private JSONArray data;


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
        //spinnerCodeJob = findViewById(R.id.spinnercodejob);
        edtcategory = findViewById(R.id.edtcategory);
        edtcodecategory = findViewById(R.id.edtcodecat);
        edtactivity = findViewById(R.id.edtactivity);
        edtDescription = findViewById(R.id.edtdescription);
        edtsapsomp = findViewById(R.id.edtsapsomp);
        //edMonth = findViewById(R.id.edMonth);
        edtstart = findViewById(R.id.edtstart);
        edtend = findViewById(R.id.edtend);
        edthours = findViewById(R.id.edthours);
        spinnerVenue = findViewById(R.id.spinnerVenue);
        spinnerVendor = findViewById(R.id.spinnerVendor);
        edtunittype = findViewById(R.id.edtunit);
        edtremark = findViewById(R.id.edtremark);
        btnadd = findViewById(R.id.btnAdd);
        auth = FirebaseAuth.getInstance();

        desc = new ArrayList<String>();
        edtDescription.setOnItemSelectedListener(this);

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

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.venue, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        /*edtDescription.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getData();
                edtcodejob.setText(getKode(position));
                edtcategory.setText(getCategory(position));
                edtcodecategory.setText(getCode(position));
                edtactivity.setText(getActivity(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                edtcodejob.setText("");
                edtcategory.setText("");
                edtcodecategory.setText("");
                edtactivity.setText("");
            }
        });*/
        getData();
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(Config.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            data = j.getJSONArray(Config.JSON_ARRAY);
                            Log.d("asow", "asowlah" + data);

                            //Calling method getStudents to get the students from the JSON Array
                            getDesc(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getDesc(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                desc.add(json.getString(Config.TAG_DESC));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        //ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.txt, desc);
        //edtDescription.setAdapter(adapter3);
        edtDescription.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.txt, desc));
    }

    private String getActivity(int position) {
        String activity = "";
        try {
            //Getting object of given index
            JSONObject json = data.getJSONObject(position);

            //Fetching name from that object
            activity = json.getString(Config.TAG_ACTIVITY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return activity;
    }

    private String getCategory(int position) {
        String category = "";
        try {
            //Getting object of given index
            JSONObject json = data.getJSONObject(position);

            //Fetching name from that object
            category = json.getString(Config.TAG_CATEGORY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return category;
    }

    private String getCode(int position) {
        String code = "";
        try {
            //Getting object of given index
            JSONObject json = data.getJSONObject(position);

            //Fetching name from that object
            code = json.getString(Config.TAG_CODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return code;
    }

    private String getKode(int position) {
        String kode = "";
        try {
            //Getting object of given index
            JSONObject json = data.getJSONObject(position);

            //Fetching name from that object
            kode = json.getString(Config.TAG_KODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return kode;
    }

    /*public ArrayList<String> getDatas(String filename) {
        JSONArray jsonArray = null;
        ArrayList<String> cList = new ArrayList<String>();
        try {
            InputStream inputStream = getResources().getAssets().open(filename);
            int size = inputStream.available();
            byte[] data = new byte[size];
            inputStream.read(data);
            inputStream.close();
            final String json = new String(data, "UTF-8");
            jsonArray = new JSONArray(json);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    cList.add(jsonArray.getJSONObject(i).getString("desc"));
                    //edtcategory.setText(jsonArray.getJSONArray(i).getString(Integer.parseInt("category")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return cList;
    }*/

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
        final String mDescription = edtDescription.getSelectedItem().toString().trim();
        final String mSapsomp = edtsapsomp.getText().toString().trim();
        //final String mMonth = edMonth.getText().toString().trim();
        final String mStart = edtstart.getText().toString().trim();
        final String mEnd = edtend.getText().toString().trim();
        final String mHours = edthours.getText().toString().trim();
        final String mVenue = spinnerVenue.getSelectedItem().toString().trim();
        final String mVendor = spinnerVendor.getSelectedItem().toString().trim();
        final String mUnittype = edtunittype.getText().toString().trim();
        final String mRemark = edtremark.getText().toString().trim();

        final SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("MMM");

        if (!TextUtils.isEmpty(mCodejob) && !TextUtils.isEmpty(mCatgeory) && !TextUtils.isEmpty(mCodecategory) && !TextUtils.isEmpty(mActivity) && !TextUtils.isEmpty(mDescription) &&
                !TextUtils.isEmpty(mSapsomp) && spinnerVenue.getSelectedItem() != null && spinnerVendor.getSelectedItem() != null && !TextUtils.isEmpty(mStart) && !TextUtils.isEmpty(mEnd) && !TextUtils.isEmpty(mHours) && !TextUtils.isEmpty(mVenue) &&
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
                    newJournal.child("month").setValue(dateFormat.format(new Date()));
                    newJournal.child("start").setValue(mStart);
                    newJournal.child("end").setValue(mEnd);
                    newJournal.child("hours").setValue(mHours);
                    newJournal.child("venue").setValue(mVenue);
                    newJournal.child("vendor").setValue(mVendor);
                    newJournal.child("unittype").setValue(mUnittype);
                    newJournal.child("remark").setValue(mRemark);
                    newJournal.child("uid").setValue(CUser.getUid());
                    newJournal.child("uid_start").setValue(CUser.getUid() + "_" + mStart);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        edtcodejob.setText(getKode(position));
        edtcategory.setText(getCategory(position));
        edtcodecategory.setText(getCode(position));
        edtactivity.setText(getActivity(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        edtcodejob.setText("");
        edtcategory.setText("");
        edtcodecategory.setText("");
        edtactivity.setText("");
    }
}

