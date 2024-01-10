package keerthika.com.mx.todoapp.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import keerthika.com.mx.todoapp.model.Nota;

public class DbHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 5; // Increment the version when schema changes

    private static final String DB_NAME = "notas_app";
    private static final String TABLE_NOTAS = "notas";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_STATUS = "status";
    private static final String KEY_PRIORITY="priority";
    private static final String KEY_CATEGORY="category";
    private static final String KEY_DATE="date";





    public DbHandler(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NOTAS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT, "
                + KEY_DESCRIPTION + " TEXT, "
                + KEY_STATUS + " TEXT, " + KEY_PRIORITY + " TEXT, " +  KEY_CATEGORY + " TEXT, " +KEY_DATE + " TEXT )";



        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTAS);
        // Create tables again
        onCreate(db);
    }


    /**
     *
     * @param description
     */
    public void insertNotas(String name, String description,String priority, String category,String formattedDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_DESCRIPTION, description);
        contentValues.put(KEY_STATUS, "New");
        contentValues.put(KEY_PRIORITY, priority);
        contentValues.put(KEY_CATEGORY, category);
        contentValues.put(KEY_DATE, formattedDate);


        long newRow = db.insert(TABLE_NOTAS, null, contentValues);
        db.close();
    }


    public ArrayList<Nota> getAllNotas() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Nota> notasList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_NOTAS + " ORDER BY " + KEY_ID + " DESC";
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            Nota nota = new Nota(
                    cursor.getString(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(KEY_STATUS)),cursor.getString(cursor.getColumnIndex(KEY_PRIORITY)),cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)),cursor.getString(cursor.getColumnIndex(KEY_DATE))
            );

            notasList.add(nota);
        }


        return notasList;
    }
    // Get distinct categories from the database
    public List<String> getDistinctCategories() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT category FROM " + TABLE_NOTAS, null);

        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return categories;
    }

    // Get tasks filtered by category
    public List<Nota> getNotasByCategory(String category) {
        List<Nota> notas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {category};
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTAS + " WHERE " + KEY_CATEGORY + "=?", selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                Nota nota = new Nota();
                nota.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                nota.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                nota.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                nota.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                nota.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                nota.setPriority(cursor.getString(cursor.getColumnIndex(KEY_PRIORITY)));
                // Add additional lines for other columns like status, priority, date, etc.
                notas.add(nota);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notas;
    }




    public void deleteNota(String nota_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTAS, KEY_ID+" = ?", new String[]{nota_id});
        db.close();
    }

    public int deleteAllNotas(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete (TABLE_NOTAS, String.valueOf (1), null);
    }


    public int updateNota(String nota_id, String name, String description, String status, String priority,String category,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_DESCRIPTION, description);
        contentValues.put(KEY_STATUS, status);
        contentValues.put(KEY_PRIORITY, priority);
        contentValues.put(KEY_CATEGORY,category);
        contentValues.put(KEY_DATE,date);


        return db.update(TABLE_NOTAS, contentValues, KEY_ID + " = ?", new String[]{nota_id});
    }


    public int updateStatus(String status, String nota_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        return db.update(TABLE_NOTAS, contentValues, KEY_ID+" = ?",new String[]{nota_id});
    }
    public int updatePriority(String priority, String nota_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PRIORITY, priority);
        return db.update(TABLE_NOTAS, contentValues, KEY_ID+" = ?",new String[]{nota_id});
    }






}
