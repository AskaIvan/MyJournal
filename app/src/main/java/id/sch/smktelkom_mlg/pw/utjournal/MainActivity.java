package id.sch.smktelkom_mlg.pw.utjournal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class MainActivity extends AppCompatActivity {

    private static final int GALERY_REQUEST = 1;
    boolean ExpandedAction = true;
    private DatabaseReference users1;
    //firebase db
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    //private ProgressBar progressBar;
    private EditText edtusername, edtpassword, edtemail, edtbranch, edtarea, edtnrp;
    private Button btnSignUp, btnToLogin;
    private AppBarLayout Appbar;
    private CollapsingToolbarLayout collTolbar;
    private Toolbar toolbar;
    private ImageButton imgBtn;
    private Uri imgUri = null;
    private StorageReference mStorageImage;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //firebase
        auth = FirebaseAuth.getInstance();
        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_images");
        database = FirebaseDatabase.getInstance();
        users1 = database.getReference().child("users");
        //users1 = database.getReferenceFromUrl("https://myjournal-3c25f.firebaseio.com/users/");

        edtusername = findViewById(R.id.edtusername);
        edtpassword = findViewById(R.id.edtpassword);
        edtemail = findViewById(R.id.edtemail);
        edtbranch = findViewById(R.id.edtbranch);
        edtarea = findViewById(R.id.edtarea);
        edtnrp = findViewById(R.id.edtnrp);
        //progressBar = findViewById(R.id.progressBar);
        mProgress = new ProgressDialog(this);

        imgBtn = findViewById(R.id.imgprofile);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnToLogin = findViewById(R.id.btnToLogin);

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent galeryIntent = new Intent();
                //galeryIntent.setAction(Intent.ACTION_GET_CONTENT);
                //galeryIntent.setType("image/*");
                //startActivityForResult(galeryIntent, GALERY_REQUEST);
                Intent galeryIntent;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    galeryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                } else {
                    galeryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    galeryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                galeryIntent.setType("image/*");
                if (galeryIntent.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(galeryIntent, GALERY_REQUEST);
            }
        });


        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtemail.getText().toString().trim();
                String password = edtpassword.getText().toString().trim();
                final String email1 = edtemail.getText().toString().trim();
                final String password1 = edtpassword.getText().toString().trim();
                final String username = edtusername.getText().toString().trim();
                final String nrp = edtnrp.getText().toString().trim();
                final String branch = edtbranch.getText().toString().trim();
                final String area = edtarea.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                //final User user = new User(email, password, username, nrp, branch, area);

                if (imgUri == null) {
                    Toast.makeText(getApplicationContext(), "Include your photo", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!email.matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), "Invalid email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Please Enter Username!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please Enter Password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(nrp)) {
                    Toast.makeText(getApplicationContext(), "Please Enter NRP!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(branch)) {
                    Toast.makeText(getApplicationContext(), "Please Enter Branch!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(area)) {
                    Toast.makeText(getApplicationContext(), "Please Enter Area!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password is too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isNetworkConnectionAvailable()) {
                    //progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(MainActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                    //progressBar.setVisibility(View.GONE);
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (task.isSuccessful()) {
                                        final String user_id = auth.getCurrentUser().getUid();

                                        mProgress.setMessage("Registering ...");
                                        mProgress.show();

                                        StorageReference filepath = mStorageImage.child(imgUri.getLastPathSegment());
                                        filepath.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                String downloadUri = taskSnapshot.getDownloadUrl().toString();


                                                DatabaseReference current_user_db = users1.child(user_id);
                                                current_user_db.child("email").setValue(email1);
                                                current_user_db.child("username").setValue(username);
                                                current_user_db.child("password").setValue(password1);
                                                current_user_db.child("nrp").setValue(nrp);
                                                current_user_db.child("branch").setValue(branch);
                                                current_user_db.child("area").setValue(area);
                                                current_user_db.child("image").setValue(downloadUri);

                                                mProgress.dismiss();

                                                Intent main = new Intent(MainActivity.this, DrawerActivity.class);
                                                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(main);
                                                finish();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(MainActivity.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    checkNetworkConnection();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imgUri = result.getUri();
                imgBtn.setImageURI(imgUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
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

    //@Override
    //protected void onResume() {
    //   super.onResume();
    //progressBar.setVisibility(View.GONE);
    //}
}
