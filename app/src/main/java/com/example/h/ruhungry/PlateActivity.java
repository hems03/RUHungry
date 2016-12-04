package com.example.h.ruhungry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.azoft.carousellayoutmanager.DefaultChildSelectionListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.UUID;

import retrofit2.Call;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PlateActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private static final String TAG="PlateActivity";
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private GestureDetectorCompat mDetector;
    private Context appContext=this;
    private ImageButton captureButton;
    private RecyclerView plateCarousel;
    private SharedPreferences sharedPreferences;
    private DiningTransitionHelper mDiningTransitionHelper;


    private FirebaseStorage mFirebaseStorage;
    private FirebaseDatabase mFireBaseDatabase;
    private StorageReference storageRef;
    private DatabaseReference databaseReference;
    private SharedPreferences preferences;

    private ArrayList<Plate>mPlates;

    private static final int REQUEST_PHOTO=0;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            show();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }


            return false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        setTitle("Plates");
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        mDetector = new GestureDetectorCompat(this,this);
        mPlates=new ArrayList<>();

        PackageManager packageManager=getPackageManager();
        captureButton=(ImageButton)findViewById(R.id.photo_capture_button);
        final Intent captureImage=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto=captureImage.resolveActivity(packageManager)!=null;
        captureButton.setEnabled(canTakePhoto);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Cam Button Pressed");
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });




        findViewById(R.id.photo_capture_button).setOnTouchListener(mDelayHideTouchListener);



        mFirebaseStorage= FirebaseStorage.getInstance();
        mFireBaseDatabase=FirebaseDatabase.getInstance();
        storageRef= mFirebaseStorage.getReferenceFromUrl("gs://ruhungry-3cda7.appspot.com/");
        databaseReference=mFireBaseDatabase.getReference("https://ruhungry-3cda7.firebaseio.com/".replace('.',','));
        preferences= PreferenceManager.getDefaultSharedPreferences(this);


        plateCarousel = (RecyclerView) findViewById(R.id.list_horizontal);
        loadPlateImages(plateCarousel);

        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        mDiningTransitionHelper=new DiningTransitionHelper(this);
        mDiningTransitionHelper.start();


        if(getIntent().getBooleanExtra(Constants.PREF_CAMERA,false)){
           startActivityForResult(captureImage,REQUEST_PHOTO);

        }
    }



    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.activity_fullscreen,menu);
        return true;
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_refresh:;
                plateCarousel.getAdapter().notifyDataSetChanged();
                break;


        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode){
            case REQUEST_PHOTO:
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                int dim=photo.getWidth();
                double heightPercent=(double)dim/photo.getHeight();
                final int newHeight=(int)(heightPercent*photo.getHeight());
                Bitmap resizedBitmap=Bitmap.createBitmap(photo,0,(int)(.5*photo.getHeight()-newHeight/2),photo.getWidth(),(newHeight));

                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] byteData = baos.toByteArray();
                final String byteString= Base64.encodeToString(byteData,Base64.DEFAULT);
                final UUID uuid=UUID.randomUUID();
                final java.util.Date date=new java.util.Date();
                StorageReference picRef=storageRef.child(date.toString());
                UploadTask uploadTask = picRef.putBytes(byteData);
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
                        final String strDate=new SimpleDateFormat("MM/dd/yyyy\nHH:mm:ss").format(date);
                        final Plate newPlate=new Plate(downloadUrl.toString(),uuid,strDate);
                        mPlates.add(newPlate);
                       final  SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(PlateActivity.this.getApplicationContext());
                        preferences.edit().putString(Constants.LAST_ENTRY_KEY,strDate);
                        databaseReference.child(preferences.getString(Constants.LOGIN_KEY,"hems03")).child("Images").child(newPlate.getID().toString()).child("URL").setValue(downloadUrl.toString());
                        databaseReference.child(preferences.getString(Constants.LOGIN_KEY,"hems03")).child("Images").child(newPlate.getID().toString()).child("Date")
                                .setValue(strDate);

                        databaseReference.child(preferences.getString(Constants.LOGIN_KEY,"hems03")).child("Images").child(newPlate.getID().toString()).child("concept").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue()!=null&&dataSnapshot.getValue().toString().charAt(0)!='['){
                                    Log.d(TAG,dataSnapshot.toString());
                                    String strPlates=dataSnapshot.getValue().toString();
                                    StringTokenizer stringTokenizer=new StringTokenizer(strPlates,",");
                                    final String[] concs=new String[5];
                                    int i=0;
                                    while(stringTokenizer.hasMoreTokens()){
                                        concs[i]=stringTokenizer.nextToken();
                                        i++;
                                    }

                                    final ArrayList<String> checkedConcepts= new ArrayList<String>();
                                    AlertDialog.Builder builder=new AlertDialog.Builder(PlateActivity.this);
                                    builder.setTitle("Select foods");
                                    builder.setMultiChoiceItems(concs, null, new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                            if(b){
                                                checkedConcepts.add(concs[i]);
                                            }else{
                                                if(checkedConcepts.contains(concs[i])){
                                                    checkedConcepts.remove(checkedConcepts.indexOf(concs[i]));
                                                }
                                            }

                                        }
                                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, final int i) {

                                            databaseReference.child(preferences.getString(Constants.LOGIN_KEY,"hems03"))
                                                    .child("Images").child(newPlate.getID().toString()).child("concept").setValue(checkedConcepts.toString());
                                            for (final String concept:checkedConcepts) {
                                                databaseReference.child(preferences.getString(Constants.LOGIN_KEY, "hems03")).child("Images").child(newPlate.getID().toString())
                                                        .child(concept).addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                        Log.d(TAG,dataSnapshot.getKey());
                                                        if (dataSnapshot.getValue() != null) {
                                                            Log.d(TAG, dataSnapshot.getValue().toString());
                                                            String val=dataSnapshot.getValue().toString();

                                                            newPlate.addFood(new Food(concept,val,val,val));
                                                            Log.d(TAG, "Food Added");
                                                        }
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
                                                });
                                            }


                                        }
                                    });

                                    builder.create().show();
                                }else if (dataSnapshot.getValue()!=null&&dataSnapshot.getValue().toString().charAt(0)=='['){

                                    StringTokenizer tokenizer= new StringTokenizer(dataSnapshot.getValue().toString(),"[,");
                                    ArrayList<String>strConcepts= new ArrayList<String>();
                                    while(tokenizer.hasMoreTokens()){
                                        strConcepts.add(tokenizer.nextToken());
                                    }
                                    mPlates.get(mPlates.size()-1).setmConcepts(strConcepts);
                                    plateCarousel.getAdapter().notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                });



        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG,"OnDown Called");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d(TAG,"OnShowPress Called");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG,"OnSingleTapUp called");
        toggle();
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG,"OnScroll Called");

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(TAG,"OnLongPress Called");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG,"OnFling Called");
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private class BooleanObj{
        boolean mVal;
        public BooleanObj(boolean val){
            mVal=val;
        }

        public boolean isVal() {
            return mVal;
        }

        public void setVal(boolean mVal) {
            this.mVal = mVal;
        }
    }

    public void loadPlateImages(final RecyclerView recyclerView){
        final BooleanObj newItems=new BooleanObj(false);
        databaseReference.child(preferences.getString(Constants.LOGIN_KEY,"hems03")).child("Images").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot child:dataSnapshot.getChildren()){
                    DataSnapshot URLChild=child.child("URL");
                    Plate plate=new Plate(URLChild.getValue().toString(), UUID.fromString(child.getKey().toString()),child.child("Date").getValue().toString());
                    String strConcs= child.child("concept").getValue().toString();
                    StringTokenizer stringTokenizer=new StringTokenizer(strConcs,",");
                    ArrayList<String>conceptList=new ArrayList<String>();
                    while(stringTokenizer.hasMoreTokens()){
                        conceptList.add(stringTokenizer.nextToken());
                    }
                    plate.setmConcepts(conceptList);
                    for(final DataSnapshot foodChild:child.child("Foods").getChildren()){
                        plate.addFood(new Food(foodChild.getKey(),foodChild.getValue().toString(),foodChild.getValue().toString(),foodChild.getValue().toString()));
                    }
                    mPlates.add(plate);
                }

                StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                PlateAdapter adapter= new PlateAdapter(PlateActivity.this.getApplicationContext());


                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);



               newItems.setVal(true);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Error when adding plate image");
            }
        });
        databaseReference.child(preferences.getString(Constants.LOGIN_KEY,"hems03")).child("Images").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(!newItems.mVal){
                    return;
                }

                Log.d(TAG,dataSnapshot.toString());
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

        });


    }



    public class PlateAdapter extends RecyclerView.Adapter<PlateHolder>{


        Context mContext;

        public PlateAdapter(Context context){
            mContext=context;

        }

        @Override
        public PlateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater =LayoutInflater.from(appContext);
            View view =layoutInflater.inflate(R.layout.plate_view_holder,parent,false);
            final PlateHolder plateHolder=new PlateHolder(view);

            return plateHolder;

        }

        @Override
        public void onBindViewHolder(final PlateHolder holder, final int position) {

            //Need to fix
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    holder.setBitmap(bitmap);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] b = baos.toByteArray();
                    mPlates.get(position).setPlateBitmap(b);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.with(PlateActivity.this).load(mPlates.get(position).getPlateURL()).into(target);
            holder.setConcepts(mPlates.get(position).getmConcepts().toString().replace("[","").replace("]",""));
            holder.getPlateView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(PlateActivity.this, (View)holder.getPlateView(),"plate_view");

                    Intent intent=new Intent(PlateActivity.this,PlateDetailsActivity.class);
                    intent.putExtra(PlateDetailsActivity.KEY_PLATE_IMAGE,mPlates.get(position).getPlateBitmap());
                    intent.putExtra(PlateDetailsActivity.KEY_PLATE_DATE,mPlates.get(position).getDate());
                    intent.putExtra(PlateDetailsActivity.KEY_PLATE_CONCEPT,mPlates.get(position).getmConcepts());
                    intent.putExtra("foods",mPlates.get(position).getFoods());
                    startActivity(intent, options.toBundle());
                }
            });


        }


        @Override
        public int getItemCount() {
            return mPlates.size(); //have to change
        }
    }

    public class PlateHolder extends RecyclerView.ViewHolder{
        ImageView mPlateView;
        TextView mConceptsTextview;
        public PlateHolder(View view){
            super(view);
            mPlateView=(ImageView)view.findViewById(R.id.plate_img);
            mConceptsTextview=(TextView)view.findViewById(R.id.concept_textview);

        }
        public void setBitmap(Bitmap bitmap){
            mPlateView.setImageBitmap(bitmap);
        }

        public ImageView getPlateView(){
            return mPlateView;
        }

        public void setConcepts(String concepts){
            mConceptsTextview.setText(concepts);
        }
    }

    private class FetchMenuTask extends AsyncTask<Call<List<Menu>>,Void,List<Menu>>{
        @Override
        protected List<Menu> doInBackground(Call<List<Menu>>... params) {
            Call<List<Menu>> menuCall=params[0];
            List<Menu> returnMenu=null;
            try {
               returnMenu= menuCall.execute().body();
            }catch (IOException e){

            }
            return returnMenu;
        }

        @Override
        protected void onPostExecute(List<Menu> menus) {
            super.onPostExecute(menus);
            if(menus!=null) {
                for (Menu m : menus) {
                    Log.i(TAG,m.getLocation_name());
                }
            }
        }
    }

}
