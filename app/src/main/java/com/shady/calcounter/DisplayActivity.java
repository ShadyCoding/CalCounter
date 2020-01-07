package com.shady.calcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import data.CustomListViewAdapter;
import data.DatabaseHandler;
import model.Food;
import util.Utils;

public class DisplayActivity extends AppCompatActivity {

    private DatabaseHandler dba;
    private ArrayList<Food> dbFoods = new ArrayList<>();
    private CustomListViewAdapter foodAdapter;
    private ListView mListView;

    private Food myFood;
    private TextView totalCals, totalFoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        mListView = findViewById(R.id.list);
        totalFoods = findViewById(R.id.totalAmountTV);
        totalCals = findViewById(R.id.totalItemTV);

        refreshData();
    }

    private void refreshData() {
        dbFoods.clear();

        dba = new DatabaseHandler(getApplicationContext());

        ArrayList<Food> foodsFromDB = dba.getFoods();

        int calsValue = dba.totalCalories();
        int totalItems = dba.getTotalItems();

        String formattedValues = Utils.formatNumber(calsValue);
        String formattedItems = Utils.formatNumber(totalItems);

        totalCals.setText("Total Calories: "+ formattedValues);
        totalFoods.setText("Total Foods: "+ formattedItems);

        for (int i = 0; i <foodsFromDB.size() ; i++) {

            String name = foodsFromDB.get(i).getFoodName();
            String dateText = foodsFromDB.get(i).getRecordDate();
            int cals = foodsFromDB.get(i).getCalories();
            int foodId = foodsFromDB.get(i).getFoodId();

            Log.v("Food IDs: ", String.valueOf(foodId));

            myFood = new Food();
            myFood.setFoodName(name);
            myFood.setCalories(cals);
            myFood.setRecordDate(dateText);
            myFood.setFoodId(foodId);

            dbFoods.add(myFood);
        }
        dba.close();

        //setup adapter
        foodAdapter = new CustomListViewAdapter(DisplayActivity.this, R.layout.list_row, dbFoods);
        mListView.setAdapter(foodAdapter);
        foodAdapter.notifyDataSetChanged();

    }
}
