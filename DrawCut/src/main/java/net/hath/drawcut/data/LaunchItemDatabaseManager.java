package net.hath.drawcut.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.gesture.Gesture;
import android.os.Parcel;
import android.util.Log;
import net.hath.drawcut.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class LaunchItemDatabaseManager extends SQLiteOpenHelper {

    private static final String TAG = "LaunchItemDatabaseManager";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "launchitemdb";
    private static final String TABLE_LAUNCHITEM = "LaunchItems";
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PACKAGE = "package";
    private static final String KEY_GESTURE = "gesture";
    private Context context;

    public LaunchItemDatabaseManager(Context context){
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public LaunchItemDatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GESTURE_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s BLOB)",
                TABLE_LAUNCHITEM, KEY_ID, KEY_NAME, KEY_PACKAGE, KEY_GESTURE);
        db.execSQL(CREATE_GESTURE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LAUNCHITEM);
        onCreate(db);
    }

    public void putLaunchItem(LaunchItem gi) {
        String name = gi.getName();
        String packageName = gi.getApplicationItem().getPackageName();
        Gesture gesture = gi.getGesture();
        Parcel p = Parcel.obtain();
        gesture.writeToParcel(p, 0);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        if (containsLaunchItem(db, gi)) {
            Log.d(TAG, "Database already contains this item: " + gi.getName());
            return;
        }

        values.put(KEY_ID, gi.getId());
        values.put(KEY_NAME, name);
        values.put(KEY_PACKAGE, packageName);
        values.put(KEY_GESTURE, p.createByteArray());

        db.insert(TABLE_LAUNCHITEM, null, values);
        db.close();
    }

    public void putLaunchItems(List<LaunchItem> list){
        SQLiteDatabase db = getWritableDatabase();

        for(LaunchItem gi:list){
            String name = gi.getName();
            String packageName = gi.getApplicationItem().getPackageName();
            Gesture gesture = gi.getGesture();
            Parcel p = Parcel.obtain();
            gesture.writeToParcel(p, 0);

            ContentValues values = new ContentValues();

            if (containsLaunchItem(db, gi)) {
                Log.d(TAG, "Database already contains this item: " + gi.getName());
                continue;
            }

            values.put(KEY_ID, gi.getId());
            values.put(KEY_NAME, name);
            values.put(KEY_PACKAGE, packageName);
            values.put(KEY_GESTURE, p.createByteArray());

            db.insert(TABLE_LAUNCHITEM, null, values);
        }
        db.close();
    }

    public boolean containsLaunchItem(SQLiteDatabase db, LaunchItem gi) {
        String query = String.format("SELECT * FROM %s WHERE %s = %d",
                TABLE_LAUNCHITEM, KEY_ID, gi.getId());
        Cursor cursor = db.rawQuery(query, null);
        Boolean ret = cursor.moveToFirst();
        return ret;
    }

    public List<LaunchItem> getLaunchItems() {
        List<LaunchItem> list = new ArrayList<LaunchItem>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_LAUNCHITEM;
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            return list;
        }
        do {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String packageName = cursor.getString(2);

            byte[] gestureBArray = cursor.getBlob(3);
            Parcel parcel = Parcel.obtain();
            parcel.writeByteArray(gestureBArray);
            Gesture g = Gesture.CREATOR.createFromParcel(parcel);

            ApplicationItem ai = ApplicationItem.createFromPackageName(context, packageName);

            LaunchItem li = new LaunchItem(name, g, ai);
            li.setGestureImage(Utils.loadBitmapFromFile(context, ""+li.getId()));
            list.add(li);
        } while (cursor.moveToNext());

        db.close();

        return list;
    }
}
