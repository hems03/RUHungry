package com.example.h.ruhungry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.azoft.carousellayoutmanager.DefaultChildSelectionListener;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
            hide();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        setTitle("Plates");
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        mDetector = new GestureDetectorCompat(this,this);

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

        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);

        plateCarousel = (RecyclerView) findViewById(R.id.list_horizontal);
        initCarousel(plateCarousel, layoutManager,new PlateAdapter(this) );
        findViewById(R.id.photo_capture_button).setOnTouchListener(mDelayHideTouchListener);

        FoodClient foodClient=ServiceGenerator.createMenuService(FoodClient.class);
        Call<List<Menu>> menuCall=foodClient.foodMenu();
        new FetchMenuTask().execute(menuCall);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_PHOTO:
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                PlatesContainer.getPlatesContainer(this).addPlate(photo);
                plateCarousel.getAdapter().notifyDataSetChanged();
                /*initCarousel(plateCarousel,
                        new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL),
                        new PlateAdapter());*/


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

    //RecyclerView Stuff
    private void initCarousel(RecyclerView recyclerView, CarouselLayoutManager layoutManager, final PlateAdapter adapter){
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        recyclerView.setLayoutManager(layoutManager);
        // we expect only fixed sized item for now
        recyclerView.setHasFixedSize(true);
        // sample adapter with random data
        recyclerView.setAdapter(adapter);
        // enable center post scrolling
        recyclerView.addOnScrollListener(new CenterScrollListener());
        // enable center post touching on item and item click listener
        DefaultChildSelectionListener.initCenterItemListener(new DefaultChildSelectionListener.OnCenterItemClickListener() {
            @Override
            public void onCenterItemClicked(@NonNull final RecyclerView recyclerView, @NonNull final CarouselLayoutManager carouselLayoutManager, @NonNull final View v) {
                final int position = recyclerView.getChildLayoutPosition(v);
                final String msg = String.format(Locale.US, "Item %1$d was clicked", position);
                Toast.makeText(PlateActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        }, recyclerView, layoutManager);

        layoutManager.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {

            @Override
            public void onCenterItemChanged(final int adapterPosition) {
                if (CarouselLayoutManager.INVALID_POSITION != adapterPosition) {
                    final int value = adapterPosition;
/*
                    adapter.mPosition[adapterPosition] = (value % 10) + (value / 10 + 1) * 10;
                    adapter.notifyItemChanged(adapterPosition);
*/
                }
            }
        });
    }

    private class PlateAdapter extends RecyclerView.Adapter<PlateHolder>{
        private PlatesContainer mPlates;

        Context mContext;

        public PlateAdapter(Context context){
            mPlates=PlatesContainer.getPlatesContainer(context);


        }

        @Override
        public PlateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater =LayoutInflater.from(appContext);
            View view =layoutInflater.inflate(R.layout.plate_view_holder,parent,false);
            return new PlateHolder(view);

        }

        @Override
        public void onBindViewHolder(PlateHolder holder, int position) {
            ImageView imageView=(ImageView)holder.itemView.findViewById(R.id.plate_img);
            //File mPhotoFile=mPlates.getPhotoFile(mPlates.getPlates().get(position));
            imageView.setImageBitmap(mPlates.getPlates().get(position).getBitmap());
        }


        @Override
        public int getItemCount() {
            return mPlates.getPlates().size(); //have to change
        }
    }

    private class PlateHolder extends RecyclerView.ViewHolder{
        public PlateHolder(View view){
            super(view);
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
