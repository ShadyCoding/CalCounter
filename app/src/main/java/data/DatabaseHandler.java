package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Food;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final ArrayList<Food> foodList = new ArrayList<>();

    public DatabaseHandler(Context context){
        super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "+ Constant.TABLE_NAME + "("
                +Constant.KEY_ID + " INTEGER PRIMARY KEY, "+ Constant.FOOD_NAME +
                " TEXT , " + Constant.FOOD_CALORIES_NAME + " INT, " + Constant.DATE_NAME + " LONG);";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_NAME);
        onCreate(db);
    }

    //Get total items
    public int getTotalItems(){
        int totalItems = 0;
        String query = "SELECT * FROM "+ Constant.TABLE_NAME;
        SQLiteDatabase dba = this.getReadableDatabase();
        Cursor cursor = dba.rawQuery(query, null);

        totalItems = cursor.getCount();
        cursor.close();

        return  totalItems;
    }

    //get total calories
    public int totalCalories(){
        int cals = 0;

        SQLiteDatabase dba = this.getReadableDatabase();

        String query = "SELECT SUM ("+ Constant.FOOD_CALORIES_NAME +" )"+
                 "FROM "+ Constant.TABLE_NAME;
         Cursor cursor = dba.rawQuery(query, null);

         if (cursor.moveToFirst()){
             cals = cursor.getInt(0);
         }

         cursor.close();
         dba.close();

        return cals;
    }

    //delete item from food
    public void deleteFood(int id){
        SQLiteDatabase dba = this.getWritableDatabase();
        dba.delete(Constant.TABLE_NAME, Constant.KEY_ID + "=?",
                new String[]{String.valueOf(id)});
    }

    //add food
    public  void  addFood(Food food){
        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constant.FOOD_NAME, food.getFoodName());
        values.put(Constant.FOOD_CALORIES_NAME, food.getCalories());
        values.put(Constant.DATE_NAME, System.currentTimeMillis());

        dba.insert(Constant.TABLE_NAME, null, values);

        Log.d("Saved", "Food added: ");

        dba.close();
    }

    //get all food
    public ArrayList<Food> getFoods(){
        foodList.clear();
        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constant.TABLE_NAME,
                new String[]{Constant.KEY_ID,Constant.FOOD_NAME, Constant.FOOD_CALORIES_NAME,
                Constant.DATE_NAME}, null, null,null, null,
                Constant.DATE_NAME + " DESC ");

        if (cursor.moveToFirst()){
            do {
                Food food = new Food();
                food.setFoodName(cursor.getString(cursor.getColumnIndex(Constant.FOOD_NAME)));
                food.setCalories(cursor.getInt(cursor.getColumnIndex(Constant.FOOD_CALORIES_NAME)));
                food.setFoodId(cursor.getInt(cursor.getColumnIndex(Constant.KEY_ID)));

                DateFormat dateFormat = DateFormat.getDateInstance();
                String date = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constant.DATE_NAME)))
                        .getTime());

                food.setRecordDate(date);

                foodList.add(food);

            }while (cursor.moveToNext());

            cursor.close();
            dba.close();
        }

        return foodList;
    }
}
