package id.sch.smktelkom_mlg.pw.utjournal;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.sch.smktelkom_mlg.pw.utjournal.Model.Journal;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {
    private static final String EXCEL_FILE_LOCATION = "/storage/sdcard0/MyFirstExcel.xls";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference myDataUser, ijournal;
    private EditText nEditFileName, nEdstart, nEdend;
    private Button nBtnDownload;
    private ProgressDialog progressDialog;
    private int mYear, mMonth, mDay;
    private List<Journal> mJournalku = new ArrayList<>();
    private Query TqueryJournal, Tdate;


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
        ijournal = FirebaseDatabase.getInstance().getReference().child("journal").child(userID);
        //startDate = FirebaseDatabase.getInstance().getReference().child("journal").child(userID);
        //endDate = FirebaseDatabase.getInstance().getReference().child("journal").child(userID);


        progressDialog = new ProgressDialog(container.getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait while load data.");
        progressDialog.setCanceledOnTouchOutside(false);


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

        nEdstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        updateDisplay();
                    }

                    private void updateDisplay() {
                        nEdstart.setText(
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
        nEdend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        updateDisplay();
                    }

                    private void updateDisplay() {
                        nEdend.setText(
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


        nBtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    String namefile = nEditFileName.getText().toString();
                    String datenyastart = nEdstart.getText().toString();
                    String datenyaend = nEdend.getText().toString();
                    if (checkPermission()) {
                        if (!TextUtils.isEmpty(namefile) && !TextUtils.isEmpty(datenyastart)) {
                            startsave();
                            String namefile1 = nEditFileName.getText().toString();
                            String path = String.valueOf(Environment.getExternalStorageDirectory()) + "/" + namefile1 + ".xls";
                            Toast.makeText(getContext(), "Check it on " + path, Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getContext(), "Can't download data, check your File Name", Toast.LENGTH_LONG).show();
                    } else {
                        requestPermission();
                    }
                }
            }
        });


        return rootview;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getContext(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    private void startsave() {
        progressDialog.show();
        auth = FirebaseAuth.getInstance();
        FirebaseUser userid = auth.getCurrentUser();
        String datestart = nEdstart.getText().toString();
        String dateend = nEdend.getText().toString();

        String userID = userid.getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("journal");
        TqueryJournal = query.orderByChild("uid_start").startAt(userID + "_" + datestart).endAt(userID + "_" + dateend);
        //Tdate = query.orderByChild("start").startAt(datenya);

        TqueryJournal.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int i = 1;
                WritableWorkbook myFirstWbook = null;
                Date currentdate = new Date();
                String namefile = nEditFileName.getText().toString();
                try {

                    myFirstWbook = Workbook.createWorkbook(new File(String.valueOf(Environment.getExternalStorageDirectory()) + "/" + namefile + ".xls"));
                    Log.d("excel", "excel :" + myFirstWbook);

                    // create an Excel sheet
                    WritableSheet excelSheet = myFirstWbook.createSheet("Sheet 1", 0);
                    Label label = new Label(0, 0, "NO");
                    excelSheet.addCell(label);
                    label = new Label(1, 0, "CODE JOB");
                    excelSheet.addCell(label);
                    label = new Label(2, 0, "CATEGORY");
                    excelSheet.addCell(label);
                    label = new Label(3, 0, "CODE CATEGORY");
                    excelSheet.addCell(label);
                    label = new Label(4, 0, "ACTIVITY");
                    excelSheet.addCell(label);
                    label = new Label(5, 0, "DESCRIPTION");
                    excelSheet.addCell(label);
                    label = new Label(6, 0, "SAP SOMP");
                    excelSheet.addCell(label);
                    label = new Label(7, 0, "MONTH");
                    excelSheet.addCell(label);
                    label = new Label(8, 0, "START");
                    excelSheet.addCell(label);
                    label = new Label(9, 0, "END");
                    excelSheet.addCell(label);
                    label = new Label(10, 0, "HRS");
                    excelSheet.addCell(label);
                    label = new Label(11, 0, "VENUE");
                    excelSheet.addCell(label);
                    label = new Label(12, 0, "VENDOR");
                    excelSheet.addCell(label);
                    label = new Label(13, 0, "UNIT TYPE");
                    excelSheet.addCell(label);
                    label = new Label(14, 0, "REMARK");
                    excelSheet.addCell(label);
                    for (DataSnapshot journalSnapshot : dataSnapshot.getChildren()) {
                        //Journal b = new Journal();

                        //mJournalku.add(b);
                        Log.d("isi lihat datanya", "ya data" + mJournalku.size());
                        Journal journal = journalSnapshot.getValue(Journal.class);
                        String mCodejob = journal.getCodejob();
                        String mCatgeory = journal.getCategory();
                        String mCodecategory = journal.getCodecategory();
                        String mActivity = journal.getActivity();
                        String mDescription = journal.getDescription();
                        String mSapsomp = journal.getSapsomp();
                        String mMonth = journal.getMonth();
                        String mStart = journal.getStart();
                        String mEnd = journal.getEnd();
                        String mHours = journal.getHours();
                        String mVenue = journal.getVenue();
                        String mVendor = journal.getVendor();
                        String mUnittype = journal.getUnittype();
                        String mRemark = journal.getRemark();
                        Log.d("isi valuenya", "valuenya" + mActivity);

                        Number number = new Number(0, i, i);
                        excelSheet.addCell(number);
                        label = new Label(1, i, mCodejob);
                        excelSheet.addCell(label);
                        label = new Label(2, i, mCatgeory);
                        excelSheet.addCell(label);
                        label = new Label(3, i, mCodecategory);
                        excelSheet.addCell(label);
                        label = new Label(4, i, mActivity);
                        excelSheet.addCell(label);
                        label = new Label(5, i, mDescription);
                        excelSheet.addCell(label);
                        label = new Label(6, i, mSapsomp);
                        excelSheet.addCell(label);
                        label = new Label(7, i, mMonth);
                        excelSheet.addCell(label);
                        label = new Label(8, i, mStart);
                        excelSheet.addCell(label);
                        label = new Label(9, i, mEnd);
                        excelSheet.addCell(label);
                        label = new Label(10, i, mHours);
                        excelSheet.addCell(label);
                        label = new Label(11, i, mVenue);
                        excelSheet.addCell(label);
                        label = new Label(12, i, mVendor);
                        excelSheet.addCell(label);
                        label = new Label(13, i, mUnittype);
                        excelSheet.addCell(label);
                        label = new Label(14, i, mRemark);
                        excelSheet.addCell(label);

                        i++;
                    }

                    myFirstWbook.write();

                    Log.d("ini write", "sampun" + myFirstWbook);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                } finally {

                    if (myFirstWbook != null) {
                        try {
                            myFirstWbook.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (WriteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(getContext(), "Download Data Complete!", Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
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
