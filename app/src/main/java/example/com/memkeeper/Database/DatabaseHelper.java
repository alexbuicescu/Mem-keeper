package example.com.memkeeper.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;

import example.com.memkeeper.POJO.Memory;


/**
 * Created by Alexandru on 03-Apr-15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    protected static SQLiteDatabase mydb;
    public static final String DATABASE_NAME = "memKeeper.db";
    public static final String TABLE_NAME = "memories";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MEMORY_NAME = "memory_name";
    public static final String COLUMN_MEMORY_LOCATION_LATITUDE = "memory_location_latitude";
    public static final String COLUMN_MEMORY_LOCATION_LONGITUDE = "memory_location_longitude";
    public static final String COLUMN_MEMORY_COVER_PHOTO = "memory_cover_photo";
    public static final String COLUMN_MEMORY_PHOTOS_PATHS = "memory_photos_paths";
    public static final String COLUMN_MEMORY_LOCATION_ONE = "memory_location_one";
    public static final String COLUMN_MEMORY_LOCATION_TWO = "memory_location_two";
    public static final String COLUMN_MEMORY_FRIENDS = "memory_friends";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        initDB();
    }

    private void initDB() {
        mydb = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + TABLE_NAME + " (" +
                        COLUMN_ID + " integer primary key, " +
                        COLUMN_MEMORY_NAME + " text, " +
                        COLUMN_MEMORY_LOCATION_LATITUDE + " text, " +
                        COLUMN_MEMORY_LOCATION_LONGITUDE + " text, " +
                        COLUMN_MEMORY_COVER_PHOTO + " text, " +
                        COLUMN_MEMORY_PHOTOS_PATHS + " text, " +
                        COLUMN_MEMORY_FRIENDS + " text, " +
                        COLUMN_MEMORY_LOCATION_ONE + " text, " +
                        COLUMN_MEMORY_LOCATION_TWO + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertMemory(Memory memory) {
        if (!mydb.isOpen()) {
            initDB();
        }
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put(COLUMN_MEMORY_NAME, memory.getName());
            contentValues.put(COLUMN_MEMORY_COVER_PHOTO, memory.getCoverImagePath());

            String photosPaths = "";
            for(String path : memory.getImagesPaths())
            {
                photosPaths += path + "--";
            }
            contentValues.put(COLUMN_MEMORY_PHOTOS_PATHS, photosPaths);

            String longitude = "";
            for(String location : memory.getLongitude())
            {
                longitude += location + "--";
            }
            contentValues.put(COLUMN_MEMORY_LOCATION_LONGITUDE, longitude);

            String latitude = "";
            for(String location : memory.getLatitude())
            {
                latitude += location + "--";
            }
            contentValues.put(COLUMN_MEMORY_LOCATION_LATITUDE, latitude);

            String friends = "";
            for(String friend : memory.getFriends())
            {
                friends += friend + "--";
            }
            contentValues.put(COLUMN_MEMORY_FRIENDS, friends);

            contentValues.put(COLUMN_MEMORY_LOCATION_ONE, memory.getLocationOne());
            contentValues.put(COLUMN_MEMORY_LOCATION_TWO, memory.getLocationTwo());

            mydb.insert(TABLE_NAME, null, contentValues);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }
        return false;
    }

    public ArrayList<Memory> getAllMemories() {
        if (!mydb.isOpen()) {
            initDB();
        }

        ArrayList<Memory> memories = new ArrayList<>();
        try {
            Cursor cursor = mydb.query(TABLE_NAME,
                    null, null, null, null, null, null);
            Integer columnIdIndex = cursor.getColumnIndex(COLUMN_ID);
            Integer columnNameIndex = cursor.getColumnIndex(COLUMN_MEMORY_NAME);
            Integer columnCoverPhoto = cursor.getColumnIndex(COLUMN_MEMORY_COVER_PHOTO);
            Integer columnLocationOne = cursor.getColumnIndex(COLUMN_MEMORY_LOCATION_ONE);
            Integer columnLocationTwo = cursor.getColumnIndex(COLUMN_MEMORY_LOCATION_TWO);
            Integer columnFriends = cursor.getColumnIndex(COLUMN_MEMORY_FRIENDS);
//            Integer columnPhotosPaths = cursor.getColumnIndex(COLUMN_MEMORY_PHOTOS_PATHS);
//            Integer columnLatitude = cursor.getColumnIndex(COLUMN_MEMORY_LOCATION_LATITUDE);
//            Integer columnLongitude = cursor.getColumnIndex(COLUMN_MEMORY_LOCATION_LONGITUDE);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(columnIdIndex);
                    String name = cursor.getString(columnNameIndex);
                    String coverPhoto = cursor.getString(columnCoverPhoto);
                    String locationOne = cursor.getString(columnLocationOne);
                    String locationTwo = cursor.getString(columnLocationTwo);
                    String friends = cursor.getString(columnFriends);
//                    String photosPaths = cursor.getString(columnPhotosPaths);
//                    String latitude = cursor.getString(columnLatitude);
//                    String longitude = cursor.getString(columnLongitude);

                    Memory memory = new Memory();
                    memory.setId(id);
                    memory.setName(name);
                    memory.setCoverImagePath(coverPhoto);
                    memory.setLocationOne(locationOne);
                    memory.setLocationTwo(locationTwo);
                    memory.setFriends(new ArrayList<>(Arrays.asList(friends.split("--"))));
//                    memory.setImagesPaths(new ArrayList<>(Arrays.asList(photosPaths.split("--"))));
//                    memory.setLatitude(new ArrayList<>(Arrays.asList(latitude.split("--"))));
//                    memory.setLongitude(new ArrayList<>(Arrays.asList(longitude.split("--"))));

                    memories.add(memory);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }

        return memories;
    }

    public Memory getMemory(int memoryId) {
        if (!mydb.isOpen()) {
            initDB();
        }

        try {
            Cursor cursor = mydb.query(TABLE_NAME,
                    new String[]{
                            COLUMN_MEMORY_NAME,
                            COLUMN_MEMORY_LOCATION_LATITUDE,
                            COLUMN_MEMORY_LOCATION_LONGITUDE,
                            COLUMN_MEMORY_COVER_PHOTO,
                            COLUMN_MEMORY_PHOTOS_PATHS,
                            COLUMN_MEMORY_FRIENDS,
                            COLUMN_MEMORY_LOCATION_ONE,
                            COLUMN_MEMORY_LOCATION_TWO,
                    },
                    COLUMN_ID + " = ?",
                    new String[]{String.valueOf(memoryId)},
                    null, null, null);

            Integer columnIdIndex = cursor.getColumnIndex(COLUMN_ID);
            Integer columnNameIndex = cursor.getColumnIndex(COLUMN_MEMORY_NAME);
            Integer columnCoverPhoto = cursor.getColumnIndex(COLUMN_MEMORY_COVER_PHOTO);
            Integer columnLocationOne = cursor.getColumnIndex(COLUMN_MEMORY_LOCATION_ONE);
            Integer columnLocationTwo = cursor.getColumnIndex(COLUMN_MEMORY_LOCATION_TWO);
            Integer columnPhotosPaths = cursor.getColumnIndex(COLUMN_MEMORY_PHOTOS_PATHS);
            Integer columnLatitude = cursor.getColumnIndex(COLUMN_MEMORY_LOCATION_LATITUDE);
            Integer columnLongitude = cursor.getColumnIndex(COLUMN_MEMORY_LOCATION_LONGITUDE);
            Integer columnFriends = cursor.getColumnIndex(COLUMN_MEMORY_FRIENDS);

            if (cursor.moveToFirst()) {
                do {

                    int id = cursor.getInt(columnIdIndex);
                    String name = cursor.getString(columnNameIndex);
                    String coverPhoto = cursor.getString(columnCoverPhoto);
                    String locationOne = cursor.getString(columnLocationOne);
                    String locationTwo = cursor.getString(columnLocationTwo);
                    String photosPaths = cursor.getString(columnPhotosPaths);
                    String latitude = cursor.getString(columnLatitude);
                    String longitude = cursor.getString(columnLongitude);
                    String friends = cursor.getString(columnFriends);

                    Memory memory = new Memory();
                    memory.setId(id);
                    memory.setName(name);
                    memory.setCoverImagePath(coverPhoto);
                    memory.setLocationOne(locationOne);
                    memory.setLocationTwo(locationTwo);
                    memory.setImagesPaths(new ArrayList<>(Arrays.asList(photosPaths.split("--"))));
                    memory.setLatitude(new ArrayList<>(Arrays.asList(latitude.split("--"))));
                    memory.setLongitude(new ArrayList<>(Arrays.asList(longitude.split("--"))));
                    memory.setFriends(new ArrayList<>(Arrays.asList(friends.split("--"))));

                    return memory;
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }

        return null;
    }

    private void updateMemory(int memoryId, Memory newMemory) {
        if (!mydb.isOpen()) {
            initDB();
        }
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put(COLUMN_MEMORY_NAME, newMemory.getName());
            contentValues.put(COLUMN_MEMORY_COVER_PHOTO, newMemory.getCoverImagePath());

            String photosPaths = "";
            for(String path : newMemory.getImagesPaths())
            {
                photosPaths += path + "--";
            }
            contentValues.put(COLUMN_MEMORY_PHOTOS_PATHS, photosPaths);

            String longitude = "";
            for(String location : newMemory.getLongitude())
            {
                longitude += location + "--";
            }
            contentValues.put(COLUMN_MEMORY_LOCATION_LONGITUDE, longitude);

            String latitude = "";
            for(String location : newMemory.getLatitude())
            {
                latitude += location + "--";
            }
            contentValues.put(COLUMN_MEMORY_LOCATION_LATITUDE, latitude);

            String friends = "";
            for(String friend : newMemory.getFriends())
            {
                friends += friend + "--";
            }
            contentValues.put(COLUMN_MEMORY_FRIENDS, friends);

            contentValues.put(COLUMN_MEMORY_LOCATION_ONE, newMemory.getLocationOne());
            contentValues.put(COLUMN_MEMORY_LOCATION_TWO, newMemory.getLocationTwo());
            mydb.update(TABLE_NAME, contentValues, COLUMN_ID + " = ? ", new String[]{String.valueOf(memoryId)});

//            mydb.insert(TABLE_NAME, null, contentValues);

//            ContentValues contentValues = new ContentValues();
//            contentValues.put(COLUMN_PRODUCT_QUANTITY, productQuantity);
//            mydb.update(TABLE_NAME, contentValues, COLUMN_ID + " = ? ", new String[]{String.valueOf(memoryId)});
        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }
    }

    public int numberOfRows() {
        if (!mydb.isOpen()) {
            initDB();
        }
        try {
            int numRows = (int) DatabaseUtils.queryNumEntries(mydb, TABLE_NAME);
            return numRows;
        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }
        return 0;
    }

    public Integer deleteMemory(int memoryId) {
        if (!mydb.isOpen()) {
            initDB();
        }
        try {
            return mydb.delete(TABLE_NAME,
                    COLUMN_ID + " = ? ",
                    new String[]{String.valueOf(memoryId)});
        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }
        return -1;
    }

}