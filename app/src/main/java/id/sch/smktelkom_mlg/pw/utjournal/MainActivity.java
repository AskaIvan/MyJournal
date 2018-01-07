package id.sch.smktelkom_mlg.pw.utjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.sch.smktelkom_mlg.pw.utjournal.Model.User;


public class MainActivity extends AppCompatActivity {

    //firebase db
    FirebaseDatabase database;
    DatabaseReference users1;

    EditText edtusername,edtpassword, edtemail, edtbranch, edtarea, edtnrp;
    Button btnSignUp, btnToLogin;

    AppBarLayout Appbar;
    CollapsingToolbarLayout collTolbar;
    Toolbar toolbar;

    boolean ExpandedAction = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //firebase
        database = FirebaseDatabase.getInstance();
        users1 = database.getReference("Users1");

        edtusername = (EditText) findViewById(R.id.edtusername);
        edtpassword = (EditText) findViewById(R.id.edtpassword);
        edtemail = (EditText) findViewById(R.id.edtemail);
        edtbranch = (EditText) findViewById(R.id.edtbranch);
        edtarea= (EditText) findViewById(R.id.edtarea);
        edtnrp = (EditText) findViewById(R.id.edtnrp);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnToLogin = (Button) findViewById(R.id.btnToLogin);

        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(s);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final User user = new User(edtusername.getText().toString(),
                        edtpassword.getText().toString(),
                        edtemail.getText().toString(),
                        edtbranch.getText().toString(),
                        edtarea.getText().toString(),
                        edtnrp.getText().toString());

                users1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUsername()).exists())
                            Toast.makeText(MainActivity.this, "Username Sudah Ada!", Toast.LENGTH_SHORT).show();
                        else {
                            users1.child(user.getUsername()).setValue(user);
                            Toast.makeText(MainActivity.this, "Registrasi Sukses!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //custom code
                    }
                });
            }
        });




        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
}
