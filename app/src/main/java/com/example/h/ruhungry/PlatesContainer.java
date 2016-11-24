package com.example.h.ruhungry;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Url;

/**
 * Created by h on 11/19/2016.
 */

public class PlatesContainer {
    Context mContext;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseDatabase mFireBaseDatabase;
    private StorageReference storageRef;
    private DatabaseReference databaseReference;
    private  SharedPreferences preferences;
    final ArrayList<Plate> mPlates;

    private static final String CLARIFAI_API_KEY="bwrRS6mMNw1o3ZxBK2Ashk4jmySk4TrNzOrzwY8y";
    private static final String CLARIFAI_API_SECRET="D9EAKPCY8FxWkwftESoyXzZambequZwDa_XZN0oq";


    private static final String TAG="PlatesContainer";
    private static PlatesContainer sPlatesContainer;
    public PlatesContainer(Context c){
        mContext=c.getApplicationContext();
        mPlates=new ArrayList<>();
        mFirebaseStorage= FirebaseStorage.getInstance();
        mFireBaseDatabase=FirebaseDatabase.getInstance();
        storageRef= mFirebaseStorage.getReferenceFromUrl("gs://ruhungry-3cda7.appspot.com/");
        databaseReference=mFireBaseDatabase.getReference("https://ruhungry-3cda7.firebaseio.com/".replace('.',','));
        preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
        loadPlateImages();

       // ImageClient imageClient=ServiceGenerator.createImageService(ImageClient.class);
    }
    public static PlatesContainer getPlatesContainer(Context context){
        if(sPlatesContainer==null){
            sPlatesContainer=new PlatesContainer(context);
        }
        return sPlatesContainer;
    }
    /*public File getPhotoFile(Plate m){
        File externalFilesDir=mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFilesDir==null){
            return null;
        }
        return new File(externalFilesDir, m.getImgPath());
    }*/
    public void addPlate(final Bitmap m){
        //StorageReference picRef=storageRef.child(m.getID().toString());
        Bitmap bitmap = m;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        final String byteString= Base64.encodeToString(data,Base64.DEFAULT);
        final UUID uuid=UUID.randomUUID();
        StorageReference picRef=storageRef.child(m.toString());
        UploadTask uploadTask = picRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
               Log.e(TAG,exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d(TAG,"Photo Successfully Uploaded");

                Plate newPlate=new Plate(m,uuid);

                SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
                databaseReference.child(preferences.getString(Constants.LOGIN_KEY,"hems03")).child("Images").child(newPlate.getID().toString()).child("URL").setValue(downloadUrl.toString());




                /*ImageClient imageClient=ServiceGenerator.createImageService(ImageClient.class);
                Call<ResponseBody>call=imageClient.concepts(downloadUrl.toString());
                new FetchConceptsTask().execute(new ConceptTaskParams(call,byteString));*/
            }
        });


        //new FetchPredictsTask().execute(data);

    }
    public void loadPlateImages(){
        /*databaseReference.child(preferences.getString(Constants.LOGIN_KEY,"hems03")).child("Images").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(final DataSnapshot child:dataSnapshot.getChildren()){
                    Target bitmapTarget= new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            mPlates.add(new Plate(bitmap,UUID.fromString(child.getKey().toString())));
                            Log.d(TAG, "Plate Added");
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    };
                    Picasso.with(mContext).load(child.child("URL").getValue().toString()).into(bitmapTarget);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Error when adding plate image");
            }
        });*/
        databaseReference.child(preferences.getString(Constants.LOGIN_KEY,"hems03")).child("Images").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final DataSnapshot child=dataSnapshot;

                Target bitmapTarget= new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mPlates.add(new Plate(bitmap,UUID.fromString(child.getKey())));
                        Log.d(TAG, "New Plate Added");
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };
                Picasso.with(mContext).load(child.child("URL").getValue().toString()).into(bitmapTarget);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;
    }

    public ArrayList<Plate>getPlates(){

        return mPlates;
    }




}
