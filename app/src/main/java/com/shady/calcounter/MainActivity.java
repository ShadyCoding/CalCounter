package com.shady.calcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import data.DatabaseHandler;
import model.Food;

public class MainActivity extends AppCompatActivity {
    private EditText foodName, foodCals;
    private Button submintButton;
    private DatabaseHandler dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dba = new DatabaseHandler(MainActivity.this);

        foodName = findViewById(R.id.foodEditText);
        foodCals = findViewById(R.id.caloriesEditText);
        submintButton = findViewById(R.id.submitButton);

        submintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToDB();
            }
        });
    }

    private void saveDataToDB() {
        Food food = new Food();
        String name = foodName.getText().toString().trim();
        String calsString = foodCals.getText().toString().trim();
        int cals = Integer.parseInt(calsString);

        if (name.equals("") || calsString.equals("")){
            Toast.makeText(getApplicationContext(), "No empy fields allowed", Toast.LENGTH_SHORT).show();
        }else {
            food.setFoodName(name);
            food.setCalories(cals);

            dba.addFood(food);
            dba.close();


            //clear the form

            foodName.setText("");
            foodCals.setText("");

            // take user to new activity
            startActivity(new Intent(MainActivity.this, DisplayActivity.class));
        }

    }
}
