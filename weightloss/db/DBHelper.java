package com.app.weightloss.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.weightloss.Utils.Support;
import com.app.weightloss.model.Exercise;
import com.app.weightloss.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * SQLite DB Helper for Weightloss
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Weightloss.db";
    // Keep version as 2 or bump to 3 if you've updated the schema again
    private static final int DATABASE_VERSION = 13;

    // ================================
    // TABLE & COLUMN NAMES
    // ================================

    // ADMIN TABLE
    private static final String TABLE_ADMIN = "Admin";
    private static final String COL_ADMIN_ID = "admin_id";
    private static final String COL_ADMIN_USERNAME = "admin_username";
    private static final String COL_ADMIN_EMAIL = "admin_email";
    private static final String COL_ADMIN_PASSWORD = "admin_password";

    // USER TABLE
    private static final String TABLE_USER = "User";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_USERNAME = "username";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_AGE = "age";
    private static final String COL_WEIGHT = "weight";
    private static final String COL_HEIGHT = "height";
    private static final String COL_GOALS = "goals";
    private static final String COL_DIETARY = "dietary_restrictions";
    // Newly added columns
    private static final String COL_GENDER = "gender";
    private static final String COL_ALLERGIES = "allergies";
    private static final String COL_CHRONIC = "chronic";

    // EXERCISE TABLE
    private static final String TABLE_EXERCISE = "Exercise";
    private static final String COL_EXERCISE_ID = "exercise_id";
    private static final String COL_EXERCISE_NAME = "exercise_name";
    private static final String COL_TYPE = "type";
    private static final String COL_DIFFICULTY = "difficulty";
    private static final String COL_DURATION = "duration";
    private static final String COL_REPETITIONS = "repetitions";
    private static final String COL_EXERCISE_DESC = "description_ex";
    private static final String COL_EXERCISE_IMG = "image_ex";
    private static final String COL_EXERCISE_DATE = "date_ex";

    // NUTRITION PLAN TABLE
    private static final String TABLE_NUTRITION_PLAN = "NutritionPlan";
    private static final String COL_NUTRITION_ID = "nutrition_id";
    private static final String COL_MEAL_NAME = "meal_name";
    private static final String COL_MEAL_TYPE = "meal_type";
    private static final String COL_CALORIES = "calories";
    private static final String COL_DIETARY_RESTR = "dietary_restr";
    private static final String COL_NUTRITION_DESC = "description_n";
    private static final String COL_NUTRITION_IMG = "image_nut";


    // BRIDGE TABLE for multiple Exercises per Nutrition Plan
    private static final String TABLE_NUTRITION_EXERCISE_LINK = "NutritionExerciseLink";
    private static final String COL_NLINK_ID = "nlink_id";
    private static final String COL_NUTRITION_ID_FK = "nutrition_id_fk";
    private static final String COL_EXERCISE_ID_FK = "exercise_id_fk";


    // LOG table:
    private static final String TABLE_USER_LOG = "UserLog";
    private static final String COL_LOG_ID = "log_id";
    private static final String COL_USER_ID_FK2 = "user_id_fk2"; // or reuse user_id_fk but let's keep it distinct
    private static final String COL_LOG_DATE = "log_date";
    private static final String COL_LOG_TYPE = "log_type";
    // fields for workout
    private static final String COL_EXERCISE_NAME_LOG = "exercise_name_log";
    // fields for meal
    private static final String COL_MEAL_NAME_LOG = "meal_name_log";
    private static final String COL_MEAL_TYPE_LOG = "meal_type_log";
    private static final String COL_CALORIES_LOG = "calories_log";
    // fields for water
    private static final String COL_WATER_GLASSES = "water_glasses";
    // fields for steps
    private static final String COL_STEPS_COUNT = "steps_count";


    //streak
    private static final String TABLE_STREAK = "streak";
    private static final String COL_STREAK_DATE = "streak_date";
    private static final String COL_IS_COMPLETE = "is_complete";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ================================
    // onCreate
    // ================================
    @Override
    public void onCreate(SQLiteDatabase db) {

        // 1) ADMIN TABLE
        String CREATE_ADMIN_TABLE = "CREATE TABLE " + TABLE_ADMIN + " ("
                + COL_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_ADMIN_USERNAME + " TEXT, "
                + COL_ADMIN_EMAIL + " TEXT, "
                + COL_ADMIN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_ADMIN_TABLE);

        // Seed default admin
        ContentValues cvAdmin = new ContentValues();
        cvAdmin.put(COL_ADMIN_USERNAME, "AdminUser");
        cvAdmin.put(COL_ADMIN_EMAIL, "admin@weightloss.com");
        cvAdmin.put(COL_ADMIN_PASSWORD, "admin123");
        db.insert(TABLE_ADMIN, null, cvAdmin);

        // 2) USER TABLE (with gender, allergies, chronic)
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " ("
                + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USERNAME + " TEXT, "
                + COL_EMAIL + " TEXT, "
                + COL_PASSWORD + " TEXT, "
                + COL_AGE + " INTEGER, "
                + COL_WEIGHT + " REAL, "
                + COL_HEIGHT + " REAL, "
                + COL_GOALS + " TEXT, "
                + COL_DIETARY + " TEXT, "
                + COL_GENDER + " TEXT, "
                + COL_ALLERGIES + " TEXT, "
                + COL_CHRONIC + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);

        // EXERCISE TABLE
        String CREATE_EXERCISE_TABLE = "CREATE TABLE " + TABLE_EXERCISE + " ("
                + COL_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_EXERCISE_NAME + " TEXT, "
                + COL_TYPE + " TEXT, "
                + COL_DIFFICULTY + " TEXT, "
                + COL_DURATION + " INTEGER, "
                + COL_REPETITIONS + " INTEGER, "
                + COL_EXERCISE_DESC + " TEXT, "
                + COL_EXERCISE_DATE + " TEXT, "
                + COL_EXERCISE_IMG + " TEXT)";  // new image col
        db.execSQL(CREATE_EXERCISE_TABLE);

        // NUTRITION TABLE
        String CREATE_NUTRITION_PLAN_TABLE = "CREATE TABLE " + TABLE_NUTRITION_PLAN + " ("
                + COL_NUTRITION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MEAL_NAME + " TEXT, "
                + COL_MEAL_TYPE + " TEXT, "
                + COL_CALORIES + " REAL, "
                + COL_DIETARY_RESTR + " TEXT, "
                + COL_NUTRITION_DESC + " TEXT, "
                + COL_NUTRITION_IMG + " TEXT)"; // new image col
        db.execSQL(CREATE_NUTRITION_PLAN_TABLE);







        // Optionally seed some data
  //      seedExercises(db);
  //      seedNutritionPlans(db);
   //     linkDummyPlans(db);
    }

    // ================================
    // onUpgrade
    // ================================
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NUTRITION_EXERCISE_LINK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NUTRITION_PLAN);
        onCreate(db);
    }

    // new method to get an Exercise by ID
    public Exercise getExerciseById(int exId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_EXERCISE + " WHERE " + COL_EXERCISE_ID + "=?",
                new String[]{String.valueOf(exId)});
        if(c != null && c.moveToFirst()){
            Exercise e = new Exercise();
            e.setExerciseId(c.getInt(c.getColumnIndexOrThrow(COL_EXERCISE_ID)));
            e.setExerciseName(c.getString(c.getColumnIndexOrThrow(COL_EXERCISE_NAME)));
            e.setType(c.getString(c.getColumnIndexOrThrow(COL_TYPE)));
            e.setDifficulty(c.getString(c.getColumnIndexOrThrow(COL_DIFFICULTY)));
            e.setDuration(c.getInt(c.getColumnIndexOrThrow(COL_DURATION)));
            e.setRepetitions(c.getInt(c.getColumnIndexOrThrow(COL_REPETITIONS)));
            e.setDescription(c.getString(c.getColumnIndexOrThrow(COL_EXERCISE_DESC)));
            e.setImageUrl(c.getString(c.getColumnIndexOrThrow(COL_EXERCISE_IMG)));
            c.close();
            return e;
        }
        if(c!=null)c.close();
        return null;
    }


    // ============ seedExercises (with descriptions) =============

    private void seedExercises(SQLiteDatabase db) {
        // 1) Push Ups
        ContentValues cv1 = new ContentValues();
        cv1.put(COL_EXERCISE_NAME, "Push Ups");
        cv1.put(COL_TYPE, "Strength");
        cv1.put(COL_DIFFICULTY, "Easy");
        cv1.put(COL_DURATION, 10);
        cv1.put(COL_REPETITIONS, 15);
        cv1.put(COL_EXERCISE_DESC, "Start in a plank position with your hands on the ground, shoulder-width apart. Lower your chest to the floor, then push back up. Keep your core tight and your body straight.");
        cv1.put(COL_EXERCISE_IMG, "push_ups");
        cv1.put(COL_EXERCISE_DATE, Support.getFormattedCurrentDate());
        db.insert(TABLE_EXERCISE, null, cv1);

        // 2) Squats
        ContentValues cv2 = new ContentValues();
        cv2.put(COL_EXERCISE_NAME, "Squats");
        cv2.put(COL_TYPE, "Strength");
        cv2.put(COL_DIFFICULTY, "Easy");
        cv2.put(COL_DURATION, 10);
        cv2.put(COL_REPETITIONS, 20);
        cv2.put(COL_EXERCISE_DESC, "Stand with your feet shoulder-width apart. Slowly bend your knees, lowering your hips, while keeping your back neutral. Return to standing by pushing through your heels.");
        cv2.put(COL_EXERCISE_IMG, "squats");
        cv1.put(COL_EXERCISE_DATE, Support.getFormattedCurrentDate());
        db.insert(TABLE_EXERCISE, null, cv2);

        // 3) Burpees
        ContentValues cv3 = new ContentValues();
        cv3.put(COL_EXERCISE_NAME, "Burpees");
        cv3.put(COL_TYPE, "Cardio");
        cv3.put(COL_DIFFICULTY, "Medium");
        cv3.put(COL_DURATION, 5);
        cv3.put(COL_REPETITIONS, 10);
        cv3.put(COL_EXERCISE_DESC, "Begin standing, move into a squat, place hands on floor, jump your feet back into a plank, perform a push-up, jump feet forward, and explosively jump up.");
        cv3.put(COL_EXERCISE_IMG, "burpees");
        cv1.put(COL_EXERCISE_DATE, Support.getFormattedCurrentDate());
        db.insert(TABLE_EXERCISE, null, cv3);

        // 4) Plank
        ContentValues cv4 = new ContentValues();
        cv4.put(COL_EXERCISE_NAME, "Plank");
        cv4.put(COL_TYPE, "Core");
        cv4.put(COL_DIFFICULTY, "Medium");
        cv4.put(COL_DURATION, 3);
        cv4.put(COL_REPETITIONS, 1);
        cv4.put(COL_EXERCISE_DESC, "Place your forearms on the ground, elbows under shoulders, and extend your legs behind you. Keep your body in a straight line, engaging your core. Hold this position.");
        cv4.put(COL_EXERCISE_IMG, "plank");
        cv1.put(COL_EXERCISE_DATE, Support.getFormattedCurrentDate());
        db.insert(TABLE_EXERCISE, null, cv4);

        // 5) Jumping Jacks
        ContentValues cv5 = new ContentValues();
        cv5.put(COL_EXERCISE_NAME, "Jumping Jacks");
        cv5.put(COL_TYPE, "Cardio");
        cv5.put(COL_DIFFICULTY, "Easy");
        cv5.put(COL_DURATION, 2);
        cv5.put(COL_REPETITIONS, 30);
        cv5.put(COL_EXERCISE_IMG, "jumping");
        cv5.put(COL_EXERCISE_DESC, "Stand upright with your arms at your sides, then jump while swinging your arms overhead and your feet out. Jump back to the start position.");
        cv5.put(COL_EXERCISE_IMG, "jumping");
        cv1.put(COL_EXERCISE_DATE, Support.getFormattedCurrentDate());
        db.insert(TABLE_EXERCISE, null, cv5);

        // 6) Deadlift
        ContentValues cv6 = new ContentValues();
        cv6.put(COL_EXERCISE_NAME, "Deadlift");
        cv6.put(COL_TYPE, "Strength");
        cv6.put(COL_DIFFICULTY, "Hard");
        cv6.put(COL_DURATION, 10);
        cv6.put(COL_REPETITIONS, 5);
        cv6.put(COL_EXERCISE_IMG, "deadlift");
        cv6.put(COL_EXERCISE_DESC, "Stand with feet under the barbell, hinge at your hips and bend your knees. Grip the bar, engage your core, and lift by extending your hips and knees. Keep your back flat.");
        cv6.put(COL_EXERCISE_IMG, "deadlift");
        cv1.put(COL_EXERCISE_DATE, Support.getFormattedCurrentDate());
        db.insert(TABLE_EXERCISE, null, cv6);

        // 7) Mountain Climbers
        ContentValues cv7 = new ContentValues();
        cv7.put(COL_EXERCISE_NAME, "Mountain Climbers");
        cv7.put(COL_TYPE, "Cardio");
        cv7.put(COL_DIFFICULTY, "Medium");
        cv7.put(COL_DURATION, 4);
        cv7.put(COL_REPETITIONS, 12);
        cv7.put(COL_EXERCISE_DESC, "Start in a plank position, bring one knee to your chest, then switch legs quickly, simulating a running motion. Keep your core braced and back straight.");
        cv7.put(COL_EXERCISE_IMG, "mountain");
        cv1.put(COL_EXERCISE_DATE, Support.getFormattedCurrentDate());
        db.insert(TABLE_EXERCISE, null, cv7);

        // 8) Lunges
        ContentValues cv8 = new ContentValues();
        cv8.put(COL_EXERCISE_NAME, "Lunges");
        cv8.put(COL_TYPE, "Strength");
        cv8.put(COL_DIFFICULTY, "Medium");
        cv8.put(COL_DURATION, 8);
        cv8.put(COL_REPETITIONS, 10);
        cv8.put(COL_EXERCISE_DESC, "Step forward with one leg, bending both knees to lower your hips. Push back up to standing by driving through your front heel. Alternate legs.");
        cv8.put(COL_EXERCISE_IMG, "lunges");
        cv1.put(COL_EXERCISE_DATE, Support.getFormattedCurrentDate());
        db.insert(TABLE_EXERCISE, null, cv8);

        // 9) High Knees
        ContentValues cv9 = new ContentValues();
        cv9.put(COL_EXERCISE_NAME, "High Knees");
        cv9.put(COL_TYPE, "Cardio");
        cv9.put(COL_DIFFICULTY, "Easy");
        cv9.put(COL_DURATION, 2);
        cv9.put(COL_REPETITIONS, 25);
        cv9.put(COL_EXERCISE_DESC, "Run in place while driving your knees up to hip level. Keep your arms pumping and maintain an upright posture.");
        cv9.put(COL_EXERCISE_IMG, "highknees");
        cv1.put(COL_EXERCISE_DATE, Support.getFormattedCurrentDate());
        db.insert(TABLE_EXERCISE, null, cv9);

        // 10) Russian Twists
        ContentValues cv10 = new ContentValues();
        cv10.put(COL_EXERCISE_NAME, "Russian Twists");
        cv10.put(COL_TYPE, "Core");
        cv10.put(COL_DIFFICULTY, "Medium");
        cv10.put(COL_DURATION, 5);
        cv10.put(COL_REPETITIONS, 15);
        cv10.put(COL_EXERCISE_DESC, "Sit on the floor with knees bent, lean back slightly. Twist your torso from side to side, tapping the floor on each side. Engage your core throughout.");
        cv10.put(COL_EXERCISE_IMG, "twists");
        cv1.put(COL_EXERCISE_DATE, Support.getFormattedCurrentDate());
        db.insert(TABLE_EXERCISE, null, cv10);
    }

    /**
     * Insert 9 nutrition plans:
     *  - 3 breakfasts
     *  - 3 lunches
     *  - 3 dinners
     * Each includes a descriptive paragraph in 'description_n'.
     */
    private void seedNutritionPlans(SQLiteDatabase db) {
        // BREAKFAST #1
        ContentValues b1 = new ContentValues();
        b1.put(COL_MEAL_NAME, "Oatmeal & Berries");
        b1.put(COL_MEAL_TYPE, "Breakfast");
        b1.put(COL_CALORIES, 350);
        b1.put(COL_DIETARY_RESTR, "Vegan");
        b1.put(COL_NUTRITION_DESC,
                "A quick and hearty start to your morning. Rolled oats cooked with almond milk, " +
                        "topped with fresh berries for a boost of antioxidants. High in fiber and perfect " +
                        "for sustained energy.");
        b1.put(COL_NUTRITION_IMG, "food_1");
        db.insert(TABLE_NUTRITION_PLAN, null, b1);

        // BREAKFAST #2
        ContentValues b2 = new ContentValues();
        b2.put(COL_MEAL_NAME, "Egg White Omelet");
        b2.put(COL_MEAL_TYPE, "Breakfast");
        b2.put(COL_CALORIES, 400);
        b2.put(COL_DIETARY_RESTR, "None");
        b2.put(COL_NUTRITION_IMG, "food_2");
        b2.put(COL_NUTRITION_DESC,
                "Fluffy egg whites folded with spinach, mushrooms, and a sprinkle of cheese, " +
                        "providing a high-protein start to your day. Low in fat yet delicious and filling.");
        db.insert(TABLE_NUTRITION_PLAN, null, b2);

        // BREAKFAST #3
        ContentValues b3 = new ContentValues();
        b3.put(COL_MEAL_NAME, "Avocado Toast");
        b3.put(COL_MEAL_TYPE, "Breakfast");
        b3.put(COL_CALORIES, 450);
        b3.put(COL_DIETARY_RESTR, "Vegetarian");
        b3.put(COL_NUTRITION_IMG, "food_3");
        b3.put(COL_NUTRITION_DESC,
                "Whole grain toast topped with mashed avocado, a drizzle of olive oil, " +
                        "and a pinch of salt. Packed with healthy fats and fiber to keep you satisfied.");
        db.insert(TABLE_NUTRITION_PLAN, null, b3);

        // LUNCH #1
        ContentValues l1 = new ContentValues();
        l1.put(COL_MEAL_NAME, "Grilled Chicken Salad");
        l1.put(COL_MEAL_TYPE, "Lunch");
        l1.put(COL_CALORIES, 600);
        l1.put(COL_DIETARY_RESTR, "None");
        l1.put(COL_NUTRITION_IMG, "food_4");
        l1.put(COL_NUTRITION_DESC,
                "A lean protein meal of grilled chicken breast on a bed of mixed greens, " +
                        "colorful vegetables, and a light vinaigrette dressing. Ideal for muscle maintenance.");
        db.insert(TABLE_NUTRITION_PLAN, null, l1);

        // LUNCH #2
        ContentValues l2 = new ContentValues();
        l2.put(COL_MEAL_NAME, "Quinoa & Veggies");
        l2.put(COL_MEAL_TYPE, "Lunch");
        l2.put(COL_CALORIES, 550);
        l2.put(COL_DIETARY_RESTR, "Vegan");
        l2.put(COL_NUTRITION_IMG, "food_5");
        l2.put(COL_NUTRITION_DESC,
                "Protein-packed quinoa mixed with sautéed vegetables and herbs. A wholesome, " +
                        "plant-based meal rich in vitamins and minerals.");
        db.insert(TABLE_NUTRITION_PLAN, null, l2);

        // LUNCH #3
        ContentValues l3 = new ContentValues();
        l3.put(COL_MEAL_NAME, "Tuna Wrap");
        l3.put(COL_MEAL_TYPE, "Lunch");
        l3.put(COL_CALORIES, 500);
        l3.put(COL_DIETARY_RESTR, "None");
        l3.put(COL_NUTRITION_IMG, "food_6");
        l3.put(COL_NUTRITION_DESC,
                "A light wrap filled with tuna, lettuce, and tomatoes. Provides lean protein " +
                        "and healthy carbs for steady energy throughout the day.");
        db.insert(TABLE_NUTRITION_PLAN, null, l3);

        // DINNER #1
        ContentValues d1 = new ContentValues();
        d1.put(COL_MEAL_NAME, "Salmon & Broccoli");
        d1.put(COL_MEAL_TYPE, "Dinner");
        d1.put(COL_CALORIES, 700);
        d1.put(COL_DIETARY_RESTR, "None");
        d1.put(COL_NUTRITION_IMG, "food_7");
        d1.put(COL_NUTRITION_DESC,
                "A hearty meal featuring salmon fillet rich in omega-3 fatty acids, " +
                        "paired with steamed broccoli for fiber and essential nutrients.");
        db.insert(TABLE_NUTRITION_PLAN, null, d1);

        // DINNER #2
        ContentValues d2 = new ContentValues();
        d2.put(COL_MEAL_NAME, "Veggie Stir-Fry");
        d2.put(COL_MEAL_TYPE, "Dinner");
        d2.put(COL_CALORIES, 650);
        d2.put(COL_DIETARY_RESTR, "Vegan");
        d2.put(COL_NUTRITION_IMG, "food_8");
        d2.put(COL_NUTRITION_DESC,
                "Colorful medley of bell peppers, carrots, onions, and tofu stir-fried in a light " +
                        "soy sauce. High in vitamins, minerals, and complete plant proteins.");
        db.insert(TABLE_NUTRITION_PLAN, null, d2);

        // DINNER #3
        ContentValues d3 = new ContentValues();
        d3.put(COL_MEAL_NAME, "Cauliflower Rice & Chicken");
        d3.put(COL_MEAL_TYPE, "Dinner");
        d3.put(COL_CALORIES, 600);
        d3.put(COL_DIETARY_RESTR, "None");
        d3.put(COL_NUTRITION_IMG, "food_9");
        d3.put(COL_NUTRITION_DESC,
                "Tender chicken breast served over cauliflower rice, offering a lower-carb alternative. " +
                        "Seasoned with herbs for a flavorful and satisfying meal.");
        db.insert(TABLE_NUTRITION_PLAN, null, d3);
    }


    /**
     * Link each plan with at least one exercise.
     * We'll assume IDs auto-increment in order for these 9 plans:
     *   1..3 (Breakfast), 4..6 (Lunch), 7..9 (Dinner)
     * We'll pick random or varied sets from the 10 exercises (IDs 1..10).
     */
    private void linkDummyPlans(SQLiteDatabase db){
        // Breakfast #1 => planId=1 => exercises #1,2
        linkPlanAndExercise(db, 1, 1);
        linkPlanAndExercise(db, 1, 2);

        // Breakfast #2 => planId=2 => exercises #3,5
        linkPlanAndExercise(db, 2, 3);
        linkPlanAndExercise(db, 2, 5);

        // Breakfast #3 => planId=3 => exercises #4,9
        linkPlanAndExercise(db, 3, 4);
        linkPlanAndExercise(db, 3, 9);

        // Lunch #1 => planId=4 => exercises #2,6
        linkPlanAndExercise(db, 4, 2);
        linkPlanAndExercise(db, 4, 6);

        // Lunch #2 => planId=5 => exercises #1,8
        linkPlanAndExercise(db, 5, 1);
        linkPlanAndExercise(db, 5, 8);

        // Lunch #3 => planId=6 => exercises #3,10
        linkPlanAndExercise(db, 6, 3);
        linkPlanAndExercise(db, 6, 10);

        // Dinner #1 => planId=7 => exercises #6,7
        linkPlanAndExercise(db, 7, 6);
        linkPlanAndExercise(db, 7, 7);

        // Dinner #2 => planId=8 => exercises #4,5,9
        linkPlanAndExercise(db, 8, 4);
        linkPlanAndExercise(db, 8, 5);
        linkPlanAndExercise(db, 8, 9);

        // Dinner #3 => planId=9 => exercises #8,10
        linkPlanAndExercise(db, 9, 8);
        linkPlanAndExercise(db, 9, 10);
    }

    /**
     * Utility method to add row in bridging table (planId -> exerciseId).
     */
    private void linkPlanAndExercise(SQLiteDatabase db, long planId, long exerciseId){
        ContentValues cv = new ContentValues();
        cv.put(COL_NUTRITION_ID_FK, planId);
        cv.put(COL_EXERCISE_ID_FK, exerciseId);
        db.insert(TABLE_NUTRITION_EXERCISE_LINK, null, cv);
    }

    // ================================
    // USER CRUD
    // ================================
    public long insertUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, user.getUsername());
        cv.put(COL_EMAIL, user.getEmail());
        cv.put(COL_PASSWORD, user.getPassword());
        cv.put(COL_AGE, user.getAge());
        cv.put(COL_WEIGHT, user.getWeight());
        cv.put(COL_HEIGHT, user.getHeight());
        cv.put(COL_GOALS, user.getGoals());
        cv.put(COL_DIETARY, user.getDietaryRestrictions());
        cv.put(COL_GENDER, user.getGender());
        cv.put(COL_ALLERGIES, user.getAllergies());
        cv.put(COL_CHRONIC, user.getChronic());

        long result = db.insert(TABLE_USER, null, cv);
        db.close();
        return result;
    }

    public User getUserByEmailPassword(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER
                + " WHERE " + COL_EMAIL + "=? AND " + COL_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        if(cursor != null && cursor.moveToFirst()){
            User user = new User();
            user.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)));
            user.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(COL_AGE)));
            user.setWeight(cursor.getFloat(cursor.getColumnIndexOrThrow(COL_WEIGHT)));
            user.setHeight(cursor.getFloat(cursor.getColumnIndexOrThrow(COL_HEIGHT)));
            user.setGoals(cursor.getString(cursor.getColumnIndexOrThrow(COL_GOALS)));
            user.setDietaryRestrictions(cursor.getString(cursor.getColumnIndexOrThrow(COL_DIETARY)));
            user.setGender(cursor.getString(cursor.getColumnIndexOrThrow(COL_GENDER)));
            user.setAllergies(cursor.getString(cursor.getColumnIndexOrThrow(COL_ALLERGIES)));
            user.setChronic(cursor.getString(cursor.getColumnIndexOrThrow(COL_CHRONIC)));
            cursor.close();
            return user;
        }
        if(cursor != null) cursor.close();
        return null;
    }

    public User getUserById(int userId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COL_USER_ID + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if(cursor != null && cursor.moveToFirst()){
            User user = new User();
            user.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)));
            user.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(COL_AGE)));
            user.setWeight(cursor.getFloat(cursor.getColumnIndexOrThrow(COL_WEIGHT)));
            user.setHeight(cursor.getFloat(cursor.getColumnIndexOrThrow(COL_HEIGHT)));
            user.setGoals(cursor.getString(cursor.getColumnIndexOrThrow(COL_GOALS)));
            user.setDietaryRestrictions(cursor.getString(cursor.getColumnIndexOrThrow(COL_DIETARY)));
            user.setGender(cursor.getString(cursor.getColumnIndexOrThrow(COL_GENDER)));
            user.setAllergies(cursor.getString(cursor.getColumnIndexOrThrow(COL_ALLERGIES)));
            user.setChronic(cursor.getString(cursor.getColumnIndexOrThrow(COL_CHRONIC)));
            cursor.close();
            return user;
        }
        if(cursor != null) cursor.close();
        return null;
    }

    public int updateUserProfile(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_AGE, user.getAge());
        cv.put(COL_WEIGHT, user.getWeight());
        cv.put(COL_HEIGHT, user.getHeight());
        cv.put(COL_GOALS, user.getGoals());
        cv.put(COL_DIETARY, user.getDietaryRestrictions());
        cv.put(COL_GENDER, user.getGender());
        cv.put(COL_ALLERGIES, user.getAllergies());
        cv.put(COL_CHRONIC, user.getChronic());

        int rows = db.update(TABLE_USER, cv, COL_USER_ID + "=?",
                new String[]{String.valueOf(user.getUserId())});
        db.close();
        return rows;
    }

    // ================================
    // EXERCISE
    // ================================
    public long addExercise(Exercise ex){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_EXERCISE_NAME, ex.getExerciseName());
        cv.put(COL_TYPE, ex.getType());
        cv.put(COL_DIFFICULTY, ex.getDifficulty());
        cv.put(COL_DURATION, ex.getDuration());
        cv.put(COL_REPETITIONS, ex.getRepetitions());
        cv.put(COL_EXERCISE_DESC, ex.getDescription());
        cv.put(COL_EXERCISE_IMG, ex.getImageUrl());
        cv.put(COL_EXERCISE_DATE, ex.getDateAdded());
        long result = db.insert(TABLE_EXERCISE, null, cv);
        db.close();
        return result;
    }

    public List<Exercise> getAllExercises(){
        List<Exercise> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_EXERCISE, null);
        if(c.moveToFirst()){
            do {
                Exercise ex = new Exercise();
                ex.setExerciseId(c.getInt(c.getColumnIndexOrThrow(COL_EXERCISE_ID)));
                ex.setExerciseName(c.getString(c.getColumnIndexOrThrow(COL_EXERCISE_NAME)));
                ex.setType(c.getString(c.getColumnIndexOrThrow(COL_TYPE)));
                ex.setDifficulty(c.getString(c.getColumnIndexOrThrow(COL_DIFFICULTY)));
                ex.setDuration(c.getInt(c.getColumnIndexOrThrow(COL_DURATION)));
                ex.setRepetitions(c.getInt(c.getColumnIndexOrThrow(COL_REPETITIONS)));
                ex.setDescription( c.getString( c.getColumnIndexOrThrow( COL_EXERCISE_DESC ) ) );
                ex.setImageUrl( c.getString( c.getColumnIndexOrThrow( COL_EXERCISE_IMG ) ) );
                ex.setDateAdded( c.getString( c.getColumnIndexOrThrow( COL_EXERCISE_DATE ) ) );
                list.add(ex);
            } while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public int removeExercise(int exerciseId){
        SQLiteDatabase db = this.getWritableDatabase();


        int rows = db.delete(TABLE_EXERCISE, COL_EXERCISE_ID + "=?",
                new String[]{String.valueOf(exerciseId)});
        db.close();
        return rows;
    }

    public int updateExercise(Exercise ex){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_EXERCISE_NAME, ex.getExerciseName());
        cv.put(COL_TYPE, ex.getType());
        cv.put(COL_DIFFICULTY, ex.getDifficulty());
        cv.put(COL_DURATION, ex.getDuration());
        cv.put(COL_REPETITIONS, ex.getRepetitions());
        cv.put(COL_EXERCISE_DESC, ex.getDescription());
        cv.put(COL_EXERCISE_IMG, ex.getImageUrl());
        cv.put(COL_EXERCISE_DATE, ex.getDateAdded());

        int rows = db.update(TABLE_EXERCISE, cv, COL_EXERCISE_ID + "=?",
                new String[]{String.valueOf(ex.getExerciseId())});
        db.close();
        return rows;
    }



    // ================================
    // BRIDGE: Plan <-> Exercises
    // ================================
    /**
     * Link a newly created plan to selected exercises (multi-select).
     */
    public void linkNutritionPlanToExercises(long planId, List<Long> exerciseIds){
        SQLiteDatabase db = this.getWritableDatabase();
        for(Long exId : exerciseIds){
            ContentValues cv = new ContentValues();
            cv.put(COL_NUTRITION_ID_FK, planId);
            cv.put(COL_EXERCISE_ID_FK, exId);
            db.insert(TABLE_NUTRITION_EXERCISE_LINK, null, cv);
        }
        db.close();
    }

    /**
     * Get all exercises for a given nutrition plan
     */
    public List<Exercise> getExercisesForNutritionPlan(long planId){
        List<Exercise> exerciseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT E.* FROM " + TABLE_EXERCISE + " E "
                + "JOIN " + TABLE_NUTRITION_EXERCISE_LINK + " L ON E." + COL_EXERCISE_ID + " = L." + COL_EXERCISE_ID_FK
                + " WHERE L." + COL_NUTRITION_ID_FK + "=?";

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(planId)});
        if(c.moveToFirst()){
            do {
                Exercise ex = new Exercise();
                ex.setExerciseId(c.getInt(c.getColumnIndexOrThrow(COL_EXERCISE_ID)));
                ex.setExerciseName(c.getString(c.getColumnIndexOrThrow(COL_EXERCISE_NAME)));
                ex.setType(c.getString(c.getColumnIndexOrThrow(COL_TYPE)));
                ex.setDifficulty(c.getString(c.getColumnIndexOrThrow(COL_DIFFICULTY)));
                ex.setDuration(c.getInt(c.getColumnIndexOrThrow(COL_DURATION)));
                ex.setRepetitions(c.getInt(c.getColumnIndexOrThrow(COL_REPETITIONS)));
                ex.setDescription(c.getString(c.getColumnIndexOrThrow(COL_EXERCISE_DESC)));
                ex.setImageUrl(c.getString(c.getColumnIndexOrThrow(COL_EXERCISE_IMG)));

                exerciseList.add(ex);
            } while(c.moveToNext());
        }
        c.close();
        return exerciseList;
    }




    public boolean isStreakComplete(String dateStr){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + COL_IS_COMPLETE
                        + " FROM " + TABLE_STREAK
                        + " WHERE " + COL_STREAK_DATE + "=?",
                new String[]{dateStr});
        if(c != null && c.moveToFirst()){
            int val = c.getInt(0);
            c.close();
            return (val == 1);
        }
        if(c != null) c.close();
        return false;
    }
    public int getCurrentStreak() {
        // Start from today, go backwards until we find an incomplete day
        // Count consecutive complete days
        int streak = 0;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        while(true) {
            String dateStr = sdf.format(cal.getTime());
            if(! isStreakComplete(dateStr)) {
                break;
            }
            streak++;
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }
        return streak;
    }


}
