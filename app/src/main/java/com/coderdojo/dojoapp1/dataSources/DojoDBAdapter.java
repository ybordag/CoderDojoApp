package com.coderdojo.dojoapp1.dataSources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;import java.lang.Override;import java.lang.String;

/**
 * Simple dojoapp database access helper class. Defines the basic CRUD operations
 * for the leaderboard participants, and gives the ability to list all participant
 * names as well as retrieve or modify a specific participant.
 */
public class DojoDBAdapter {

    public static final String KEY_NAME = "name";
    public static final String KEY_POSI = "position";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "DojoDBAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     *
     * public abstract class KidsDataSource{
     abstract public void addKid(Kid newKid) throws Exception;
     abstract public void removeKid(String name) throws Exception;
     abstract public ArrayList<Kid> getKids();
     abstract public Kid getKid(String name);
     abstract public void clear() throws Exception;
     abstract public void load() throws Exception;
     abstract public void save() throws Exception;
     abstract public void close() throws Exception;
     abstract public String toString();
     abstract public void fromString(String  input)throws Exception ;

     }

     */
    private static final String DATABASE_CREATE =
            "create table participants (_id integer primary key autoincrement, "
                    + "name text not null, position integer not null);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "participants";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS participants");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public DojoDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the participant database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public DojoDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new participant using the name and position provided. If the participant
     * is successfully created return the new rowId for that participant, otherwise return
     * a -1 to indicate failure.
     *
     * @param name the name of the participant
     * @param posi the position of the participant
     * @return rowId or -1 if failed
     */
    public long createParticipant(String name, int posi) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_POSI, posi);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the participant with the given rowId
     *
     * @param rowId id of participant to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteParticipant(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all participants in the database
     *
     * @return Cursor over all participants
     */
    public Cursor fetchAllParticipants() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_POSI}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the participant that matches the given rowId
     *
     * @param rowId id of participant to retrieve
     * @return Cursor positioned to matching participant, if found
     * @throws SQLException if participant could not be found/retrieved
     */
    public Cursor fetchParticipant(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_NAME, KEY_POSI}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Returns a boolean true/false based on the presence of participant
     *
     * @param sName name of participant to find
     * @return true if found, false if not
     * @throws SQLException if participant could not be found/retrieved
     */
    public boolean findParticipant(String sName) throws SQLException
    {
        boolean bFound = false;
        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE,
                        new String[] {
                                KEY_ROWID,
                                KEY_NAME,
                                KEY_POSI},
                        KEY_NAME + "=?" ,
                        new String[] {sName},
                        null, null, null, null);
        if (mCursor.getCount() > 0) {
            bFound = true;
        }
        return bFound;

    }

    /**
     * Update the participant using the details provided. The participant to be
     * updated is specified using the rowId, and it is altered to use the name
     * and position values passed in
     *
     * @param rowId id of participant to update
     * @param name value to set participant title to
     * @param posi value to set participant body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateParticipant(long rowId, String name, int posi) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_POSI, posi);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}

/*grey out when clicked
 *put lines
 */