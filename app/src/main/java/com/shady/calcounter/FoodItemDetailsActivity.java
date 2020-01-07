package com.shady.calcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import data.DatabaseHandler;
import model.Food;

public class FoodItemDetailsActivity extends AppCompatActivity {
    private TextView foodName, calories, dateTaken;
    private Button shareButton;
    private int foodID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_details_activity);

        foodName = findViewById(R.id.detsFoodName);
        calories = findViewById(R.id.detsCaloriesValues);
        dateTaken = findViewById(R.id.detsDateText);

        Food food = (Food) getIntent().getSerializableExtra("userObj");
        foodName.setText(food.getFoodName());
        calories.setText(String.valueOf(food.getCalories()));
        dateTaken.setText(food.getRecordDate());

        foodID = food.getFoodId();

        calories.setTextSize(34.9f);
        calories.setTextColor(Color.RED);

        shareButton = findViewById(R.id.detsShareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCals();
            }
        });
    }

    public void shareCals() {
        StringBuilder dataString = new StringBuilder();
        String name = foodName.getText().toString();
        String cals = calories.getText().toString();
        String date = dateTaken.getText().toString();

        dataString.append(" Food: " + name + "\n");
        dataString.append(" Calories: " + cals + "\n");
        dataString.append(" Eaten on: " + date + "\n");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_SUBJECT, "My caloric intake");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
        intent.putExtra(Intent.EXTRA_TEXT, dataString.toString());

        try {
            startActivity(Intent.createChooser(intent, "Send Email..."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Please install email client before send email",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.deleteItem) {

            AlertDialog.Builder alert = new AlertDialog.Builder(FoodItemDetailsActivity.this);
            alert.setTitle("Delete?");
            alert.setMessage("Are you sure you want to delete this item?");
            alert.setNegativeButton("No", null);
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    DatabaseHandler dba = new DatabaseHandler(getApplicationContext());
                    dba.deleteFood(foodID);

                    Toast.makeText(getApplicationContext(), "Food Item Deleted!", Toast.LENGTH_LONG)
                            .show();

                    startActivity(new Intent(FoodItemDetailsActivity.this, DisplayActivity.class));


                    //remove this activity from activity stack
                    FoodItemDetailsActivity.this.finish();


                }
            });
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
