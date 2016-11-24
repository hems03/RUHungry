package com.example.h.ruhungry;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by h on 11/17/2016.
 */

public class SignInActivity extends FragmentActivity {
    private static final String TAG="SignInActivity";
    private GoogleApiClient mGoogleApiClient;
    private Button mLoginButton;
    private EditText mUserEditText;
    private EditText mPassEditText;
    private MenuItem mProgressIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        final SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    4
            );
        }
        mProgressIcon=(MenuItem)findViewById(R.id.menu_item_progress_bar);
        mUserEditText=(EditText)findViewById(R.id.input_user);
        mPassEditText=(EditText)findViewById(R.id.input_password);
        mLoginButton=(Button)findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sharedPreferences.getBoolean(Constants.GEOFENCE_TOGGLE_KEY,false)){
                     DiningTransitionHelper transitionHelper= new DiningTransitionHelper(SignInActivity.this.getApplicationContext());
                        transitionHelper.start();
                    sharedPreferences.edit().putBoolean(Constants.GEOFENCE_TOGGLE_KEY,true);
                }

                final String login=mUserEditText.getText().toString();
                final String pass=mPassEditText.getText().toString();

                FirebaseDatabase mFireBaseDatabase=FirebaseDatabase.getInstance();
                final DatabaseReference signInRef=mFireBaseDatabase.getReference("https://ruhungry-3cda7.firebaseio.com/".replace('.',','));


                signInRef.child(login).child("PASS").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getValue()==null){
                            Toast toast = Toast.makeText(SignInActivity.this.getApplicationContext(),"Adding New User",Toast.LENGTH_LONG);
                            toast.show();
                             Log.d("SignInActivity","Making new user");
                            signInRef.child(login).child("PASS").setValue(pass);
                            sharedPreferences.edit().putString(Constants.LOGIN_KEY,login).apply();
                            startActivity(new Intent(SignInActivity.this.getApplicationContext(),PlateActivity.class));
                        }else if(dataSnapshot.getValue().equals(pass)){
                            Toast toast = Toast.makeText(SignInActivity.this.getApplicationContext(),"Login Successful",Toast.LENGTH_LONG);
                            toast.show();
                            Log.d("SignInActivity","Login successful");
                            sharedPreferences.edit().putString(Constants.LOGIN_KEY,login).apply();
                            startActivity(new Intent(SignInActivity.this.getApplicationContext(),PlateActivity.class));
                        }else{
                            Toast toast = Toast.makeText(SignInActivity.this.getApplicationContext(),"Login Unsuccessful. Try Again",Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_in_activity,menu);

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
