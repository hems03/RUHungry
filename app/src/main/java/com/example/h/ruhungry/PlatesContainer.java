package com.example.h.ruhungry;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;

/**
 * Created by h on 11/19/2016.
 */

public class PlatesContainer {
    Context mContext;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference storageRef;
    private ArrayList<Plate>mPlates;
    private static final String CLARIFAI_API_KEY="bwrRS6mMNw1o3ZxBK2Ashk4jmySk4TrNzOrzwY8y";
    private static final String CLARIFAI_API_SECRET="D9EAKPCY8FxWkwftESoyXzZambequZwDa_XZN0oq";

    final ClarifaiClient mClarifaiClient;
    private static final String TAG="PlatesContainer";
    private static PlatesContainer sPlatesContainer;
    public PlatesContainer(Context c){
        mContext=c.getApplicationContext();
        mPlates=new ArrayList<>();
        mFirebaseStorage= FirebaseStorage.getInstance();
        storageRef= mFirebaseStorage.getReferenceFromUrl("gs://ruhungry-3cda7.appspot.com");
        mClarifaiClient = new ClarifaiBuilder(CLARIFAI_API_KEY, CLARIFAI_API_SECRET).buildSync();
    }
    public static PlatesContainer getPlatesContainer(Context context){
        if(sPlatesContainer==null){
            sPlatesContainer=new PlatesContainer(context);
        }
        return sPlatesContainer;
    }
    public File getPhotoFile(Plate m){
        File externalFilesDir=mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFilesDir==null){
            return null;
        }
        return new File(externalFilesDir, m.getImgPath());
    }
    public void addPlate(Plate m){

        StorageReference picRef=storageRef.child(m.getID().toString());
        Bitmap bitmap = m.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = picRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                Log.d(TAG,"Photo Successfully Uploaded");
            }
        });
        new FetchPredictsTask().execute(data);

        mPlates.add(m);
    }
    public ArrayList<Plate>getPlates(){
        return mPlates;
    }

    private class FetchPredictsTask extends AsyncTask<byte[],Void,ClarifaiResponse>{
        @Override
        protected ClarifaiResponse doInBackground(byte[]... params) {
            ClarifaiResponse clarifaiResponse=mClarifaiClient.getDefaultModels().foodModel().predict()
                    .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(params[0])))
                    .executeSync();
            Log.i(TAG,clarifaiResponse.get().toString());
            return clarifaiResponse;
        }
    }

}
