package id.sch.smktelkom_mlg.pw.utjournal;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import id.sch.smktelkom_mlg.pw.utjournal.Model.Journal;

public class AddActivity extends AppCompatActivity {

    //Firebase
    FirebaseDatabase database;
    DatabaseReference journals;

    EditText edtcodejob, edtcategory, edtcodecategory, edtactivity, edtdescription, edtsapsomp, edtstart, edtend, edthours, edtunittype, edtremark;
    Spinner spinnerMonth, spinnerVenue, spinnerVendor;
    Button btnadd;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        database =FirebaseDatabase.getInstance();
        journals = database.getReference("Journal");

        edtcodejob = (EditText) findViewById(R.id.edtcodejob);
        edtcategory = (EditText) findViewById(R.id.edtcategory);
        edtcodecategory = (EditText) findViewById(R.id.edtcodecat);
        edtactivity = (EditText) findViewById(R.id.edtactivity);
        edtdescription = (EditText) findViewById(R.id.edtdescription);
        edtsapsomp = (EditText) findViewById(R.id.edtsapsomp);
        spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);
        edtstart = (EditText) findViewById(R.id.edtstart);
        edtend = (EditText) findViewById(R.id.edtend);
        edthours = (EditText) findViewById(R.id.edthours);
        spinnerVenue = (Spinner) findViewById(R.id.spinnerVenue);
        spinnerVendor = (Spinner) findViewById(R.id.spinnerVendor);
        edtunittype = (EditText) findViewById(R.id.edtunit);
        edtremark = (EditText) findViewById(R.id.edtremark);
        btnadd = (Button) findViewById(R.id.btnAdd);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.month, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.venue, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVenue.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.vendor, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVendor.setAdapter(adapter2);

        edtstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear= mcurrentDate.get(Calendar.YEAR);
                mMonth=mcurrentDate.get(Calendar.MONTH);
                mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

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
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Start Date");
                mDatePicker.show();
            }
        });

        edtend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear= mcurrentDate.get(Calendar.YEAR);
                mMonth=mcurrentDate.get(Calendar.MONTH);
                mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

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
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("End Date");
                mDatePicker.show();
            }
        });


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Journal journal = new Journal(edtcodejob.getText().toString(),
                        edtcategory.getText().toString(),
                        edtcodecategory.getText().toString(),
                        edtactivity.getText().toString(),
                        edtdescription.getText().toString(),
                        edtsapsomp.getText().toString(),
                        spinnerMonth.getSelectedItem().toString(),
                        edtstart.getText().toString(),
                        edtend.getText().toString(),
                        edthours.getText().toString(),
                        spinnerVenue.getSelectedItem().toString(),
                        spinnerVendor.getSelectedItem().toString(),
                        edtunittype.getText().toString(),
                        edtremark.getText().toString());

                journals.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        journals.child(journal.getCodejob()).setValue(journal);
                        Toast.makeText(AddActivity.this,"Journal Added!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    //private void addJournal(final String codejob, final String category, final String codecategory, final String activity, final String description, final String sapsomp, final String month, final String start, final String end, final String hours, final String venue, final String vendor, final String unittype, final String remark) {
      //  Journal journal = new Journal(codejob, category, codecategory, activity, description, sapsomp, month, start, end, hours, venue, vendor, unittype, remark);

        //journal.child(journals).


}

