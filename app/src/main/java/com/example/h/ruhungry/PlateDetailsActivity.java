package com.example.h.ruhungry;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlateDetailsActivity extends AppCompatActivity {
    private ImageView mPlateView;
    private RecyclerView mPlateList;
    private TextView mDateText;
    private ArrayList<String>mConcepts;
    private String mDate;
    private ArrayList<Food>mFoods;
    public static final String KEY_PLATE_IMAGE="plates_images_views";
    public static final String KEY_PLATE_DATE="plate_date";
    public static final String KEY_PLATE_CONCEPT="plate_concept";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_details);
        /*View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/
        mPlateView= (ImageView)findViewById(R.id.plate_details_imageview);

        Intent originIntent=getIntent();
        byte[] b = originIntent.getByteArrayExtra(KEY_PLATE_IMAGE);



        mPlateView.setImageBitmap(BitmapFactory.decodeByteArray(b,0,b.length));




        mConcepts=originIntent.getStringArrayListExtra(KEY_PLATE_CONCEPT);
        mDate=originIntent.getStringExtra(KEY_PLATE_DATE);
        mFoods=originIntent.getParcelableArrayListExtra("foods");
        mPlateList=(RecyclerView)findViewById(R.id.plate_details_recyclerview);
        mPlateList.setLayoutManager(new LinearLayoutManager(this));
        mPlateList.setAdapter(new PlateDetailsAdapter());

        mDateText=(TextView)findViewById(R.id.plate_date_textview);
        mDateText.setText(mDate);

    }

    private class PlateDetailsAdapter extends RecyclerView.Adapter<PlateDetailsHolder>{
        @Override
        public PlateDetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater =LayoutInflater.from(PlateDetailsActivity.this);
            View view =layoutInflater.inflate(R.layout.plate_details_holder,parent,false);
            return new PlateDetailsHolder(view);
        }

        @Override
        public void onBindViewHolder(PlateDetailsHolder holder, int position) {
            String name=mFoods.get(position).getFoodName();
            name= name.substring(0,1).toUpperCase()+name.substring(1);
            holder.setName(name);
            holder.setCals(mFoods.get(position).getCalories());

        }

        @Override
        public int getItemCount() {
            return mFoods.size();
        }
    }

    private class PlateDetailsHolder extends RecyclerView.ViewHolder{
        private TextView mFoodNameText,mNutritionText;

        public PlateDetailsHolder(View view){
            super(view);
            mFoodNameText=(TextView)view.findViewById(R.id.text_view_food_name);
            mNutritionText=(TextView)view.findViewById(R.id.cals_text_view);
        }

        public void setName(String name){
            mFoodNameText.setText(name);
        }
        public void setCals(String cals){
            mNutritionText.setText("Nutrition: "+cals);
        }



    }
}
