package id.sch.smktelkom_mlg.pw.utjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login2Activity extends AppCompatActivity {

    FirebaseDatabase database;
    ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference users1;

    private EditText edtpassword, edtemail;
    private Button btnLogin, btnToSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(login2Activity.this, HomeActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login2);

        database = FirebaseDatabase.getInstance();
        users1 = database.getReference().child("users");

        edtpassword = findViewById(R.id.edtpassword);
        edtemail = findViewById(R.id.edtemail);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        btnToSignUp = findViewById(R.id.btnToSignUp);

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
                //logIn(edtusername.getText().toString(),
                //       edtpassword.getText().toString());
                String email = edtemail.getText().toString().trim();
                String password = edtpassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(login2Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(login2Activity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                } else {
                                    checkUserExist();
                                    Intent intent = new Intent(login2Activity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    private void checkUserExist() {
        final String user_id = auth.getCurrentUser().getUid();
        users1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)) {
                    Intent intent = new Intent(login2Activity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(login2Activity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*private void logIn(final String username, final String password) {
        users1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(username).exists()) {
                    if (!username.isEmpty()) {
                        User login = dataSnapshot.child(username).getValue(User.class);
                        if (login.getPassword().equals(password)) {
                            Toast.makeText(login2Activity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                            Intent s = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(s);
                        } else {
                            Toast.makeText(login2Activity.this, "Password Salah!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(login2Activity.this, "Pengguna Belum Terdaftar!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/
}