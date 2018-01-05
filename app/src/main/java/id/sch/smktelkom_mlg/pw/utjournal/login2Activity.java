package id.sch.smktelkom_mlg.pw.utjournal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import id.sch.smktelkom_mlg.pw.utjournal.Model.User;

public class login2Activity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference users1;

    EditText edtpassword,edtusername;
    Button btnLogin, btnToSignUp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        database = FirebaseDatabase.getInstance();
        users1 = database.getReference("Users1");

        edtpassword = (EditText) findViewById(R.id.edtpassword);
        edtusername = (EditText) findViewById(R.id.edtusername);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnToSignUp = (Button) findViewById(R.id.btnToSignUp);
        btnToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(s);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn(edtusername.getText().toString(),
                        edtpassword.getText().toString());
            }
        });
    }

    private void logIn(final String username, final String password) {
        users1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(username).exists()){
                    if(!username.isEmpty()){
                        User login = dataSnapshot.child(username).getValue(User.class);
                        if (login.getPassword().equals(password)){
                            Toast.makeText(login2Activity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                            Intent s = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(s);
                        }
                        else {
                            Toast.makeText(login2Activity.this, "Password Salah!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(login2Activity.this, "Pengguna Belum Terdaftar!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
