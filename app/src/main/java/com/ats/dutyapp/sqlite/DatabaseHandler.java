package com.ats.dutyapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ats.dutyapp.model.ChatDetail;
import com.ats.dutyapp.model.ChatDisplay;
import com.ats.dutyapp.model.ChatTask;
import com.ats.dutyapp.model.NotificationTemp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 17;

    private static final String DATABASE_NAME = "DutyAppChatDB";

    private static final String TABLE_CHAT_DETAIL = "ChatDetail";
    private static final String TABLE_CHAT_HEADER = "ChatHeader";

    //-----------------TABLE_CHAT_DETAIL--------------------------------

    private static final String ID = "id";
    private static final String CD_ID = "cdId";
    private static final String CD_HID = "cdHId";
    private static final String CD_MSG_TYPE = "cdMsgType";
    private static final String CD_MSG = "cdMsg";
    private static final String CD_DATE = "cdDate";
    private static final String CD_USER_ID = "cdUserId";
    private static final String CD_USER_NAME = "cdUserName";
    private static final String CD_DEL_STATUS = "cdDelStatus";
    private static final String CD_MARK_READ = "cdMarkRead";
    private static final String CD_SYNC_STATUS = "cdSyncStatus";
    private static final String CD_OFFLINE_STATUS = "cdOfflineStatus";

    private static final String CD_REPLY_TO_ID = "cdReplyToId";
    private static final String CD_REPLY_TO_MSG_TYPE = "cdReplyToMsgType";
    private static final String CD_REPLY_TO_MSG = "cdReplyToMsg";
    private static final String CD_REPLY_TO_NAME = "cdReplyToName";


    //-----------------TABLE_CHAT_HEADER--------------------------------

    private static final String H_PKID = "hPkId";
    private static final String H_ID = "hId";
    private static final String H_DATE = "hDate";
    private static final String H_TITLE = "hTitle";
    private static final String H_CREATED_BY = "hCreatedBy";
    private static final String H_ADMIN_IDS = "hAdminIds";
    private static final String H_ASSIGN_IDS = "hAssignIds";
    private static final String H_DESC = "hDesc";
    private static final String H_IMAGE = "hImage";
    private static final String H_STATUS = "hStatus";
    private static final String H_PRIVILAGE = "hPrivilage";
    private static final String H_CLOSE_REQ_ID = "hCloseReqId";
    private static final String H_CLOSE_REQ_NAME = "hCloseReqName";
    private static final String H_LAST_DATE = "hLastDate";
    private static final String H_NEW_DATE = "hNewDate";

    /*String CREATE_TABLE_CHAT_DETAIL = "CREATE TABLE "
            + TABLE_CHAT_DETAIL + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, "
            + CD_ID + " INTEGER, "
            + CD_HID + " INTEGER, "
            + CD_MSG_TYPE + " INTEGER, "
            + CD_MSG + " TEXT, "
            + CD_DATE + " TEXT, "
            + CD_USER_ID + " INTEGER, "
            + CD_USER_NAME + " TEXT, "
            + CD_DEL_STATUS + " INTEGER, "
            + CD_MARK_READ + " INTEGER,"
            + CD_SYNC_STATUS + " INTEGER,"
            + CD_OFFLINE_STATUS + " INTEGER)";*/

    //21-11-2019
    String CREATE_TABLE_CHAT_DETAIL = "CREATE TABLE "
            + TABLE_CHAT_DETAIL + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, "
            + CD_ID + " INTEGER, "
            + CD_HID + " INTEGER, "
            + CD_MSG_TYPE + " INTEGER, "
            + CD_MSG + " TEXT, "
            + CD_DATE + " TEXT, "
            + CD_USER_ID + " INTEGER, "
            + CD_USER_NAME + " TEXT, "
            + CD_DEL_STATUS + " INTEGER, "
            + CD_MARK_READ + " INTEGER,"
            + CD_SYNC_STATUS + " INTEGER,"
            + CD_OFFLINE_STATUS + " INTEGER, "
            + CD_REPLY_TO_ID + " INTEGER, "
            + CD_REPLY_TO_MSG_TYPE + " INTEGER, "
            + CD_REPLY_TO_MSG + " TEXT, "
            + CD_REPLY_TO_NAME + " TEXT)";


    String CREATE_TABLE_CHAT_HEADER = "CREATE TABLE "
            + TABLE_CHAT_HEADER + "("
            + H_PKID + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, "
            + H_ID + " INTEGER, "
            + H_DATE + " TEXT, "
            + H_TITLE + " TEXT, "
            + H_CREATED_BY + " INTEGER, "
            + H_ADMIN_IDS + " TEXT, "
            + H_ASSIGN_IDS + " TEXT, "
            + H_DESC + " TEXT, "
            + H_IMAGE + " TEXT, "
            + H_STATUS + " INTEGER, "
            + H_PRIVILAGE + " INTEGER, "
            + H_CLOSE_REQ_ID + " INTEGER, "
            + H_CLOSE_REQ_NAME + " TEXT,"
            + H_LAST_DATE + " TEXT)";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CHAT_DETAIL);
        db.execSQL(CREATE_TABLE_CHAT_HEADER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_HEADER);
        onCreate(db);
    }


    public void removeAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHAT_DETAIL, null, null);
        db.delete(TABLE_CHAT_HEADER, null, null);
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CHAT_DETAIL);
        db.execSQL("delete from " + TABLE_CHAT_HEADER);
        db.close();
    }


    //--------------------------CHAT DETAIL--------------------------------

   /* public void addChatDetail(ChatDisplay chatDisplay) {

        try {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(CD_ID, chatDisplay.getChatTaskDetailId());
            values.put(CD_HID, chatDisplay.getHeaderId());
            values.put(CD_MSG_TYPE, chatDisplay.getTypeOfText());
            values.put(CD_MSG, chatDisplay.getTextValue());
            values.put(CD_DATE, chatDisplay.getDateTime());
            values.put(CD_USER_ID, chatDisplay.getUserId());
            values.put(CD_USER_NAME, chatDisplay.getUserName());
            values.put(CD_DEL_STATUS, chatDisplay.getDelStatus());
            values.put(CD_MARK_READ, chatDisplay.getMarkAsRead());
            values.put(CD_SYNC_STATUS, chatDisplay.getSyncStatus());
            values.put(CD_OFFLINE_STATUS, chatDisplay.getOfflineStatus());

            db.insert(TABLE_CHAT_DETAIL, null, values);

            Log.e("DB", " ------- addChatDetail---------- NEW ADD");

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    //21-11-2019
    public void addChatDetail(ChatDisplay chatDisplay) {

        try {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(CD_ID, chatDisplay.getChatTaskDetailId());
            values.put(CD_HID, chatDisplay.getHeaderId());
            values.put(CD_MSG_TYPE, chatDisplay.getTypeOfText());
            values.put(CD_MSG, chatDisplay.getTextValue());
            values.put(CD_DATE, chatDisplay.getDateTime());
            values.put(CD_USER_ID, chatDisplay.getUserId());
            values.put(CD_USER_NAME, chatDisplay.getUserName());
            values.put(CD_DEL_STATUS, chatDisplay.getDelStatus());
            values.put(CD_MARK_READ, chatDisplay.getMarkAsRead());
            values.put(CD_SYNC_STATUS, chatDisplay.getSyncStatus());
            values.put(CD_OFFLINE_STATUS, chatDisplay.getOfflineStatus());
            values.put(CD_REPLY_TO_ID, chatDisplay.getReplyToId());
            values.put(CD_REPLY_TO_MSG_TYPE, chatDisplay.getReplyToMsgType());
            values.put(CD_REPLY_TO_MSG, chatDisplay.getReplyToMsg());
            values.put(CD_REPLY_TO_NAME, chatDisplay.getReplyToName());

            db.insert(TABLE_CHAT_DETAIL, null, values);

            Log.e("DB", " ------- addChatDetail1---------- NEW ADD");

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-------------------------

    /*public long addChatDetailReturnId(ChatDisplay chatDisplay) {

        long id = 0;
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(CD_ID, chatDisplay.getChatTaskDetailId());
            values.put(CD_HID, chatDisplay.getHeaderId());
            values.put(CD_MSG_TYPE, chatDisplay.getTypeOfText());
            values.put(CD_MSG, chatDisplay.getTextValue());
            values.put(CD_DATE, chatDisplay.getDateTime());
            values.put(CD_USER_ID, chatDisplay.getUserId());
            values.put(CD_USER_NAME, chatDisplay.getUserName());
            values.put(CD_DEL_STATUS, chatDisplay.getDelStatus());
            values.put(CD_MARK_READ, chatDisplay.getMarkAsRead());
            values.put(CD_SYNC_STATUS, chatDisplay.getSyncStatus());
            values.put(CD_OFFLINE_STATUS, chatDisplay.getOfflineStatus());


            id = db.insert(TABLE_CHAT_DETAIL, null, values);

            Log.e("DB", " ------- addChatDetail---------- NEW ADD");

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }*/

    //21-11-2019
    public long addChatDetailReturnId(ChatDisplay chatDisplay) {

        long id = 0;
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(CD_ID, chatDisplay.getChatTaskDetailId());
            values.put(CD_HID, chatDisplay.getHeaderId());
            values.put(CD_MSG_TYPE, chatDisplay.getTypeOfText());
            values.put(CD_MSG, chatDisplay.getTextValue());
            values.put(CD_DATE, chatDisplay.getDateTime());
            values.put(CD_USER_ID, chatDisplay.getUserId());
            values.put(CD_USER_NAME, chatDisplay.getUserName());
            values.put(CD_DEL_STATUS, chatDisplay.getDelStatus());
            values.put(CD_MARK_READ, chatDisplay.getMarkAsRead());
            values.put(CD_SYNC_STATUS, chatDisplay.getSyncStatus());
            values.put(CD_OFFLINE_STATUS, chatDisplay.getOfflineStatus());
            values.put(CD_REPLY_TO_ID, chatDisplay.getReplyToId());
            values.put(CD_REPLY_TO_MSG_TYPE, chatDisplay.getReplyToMsgType());
            values.put(CD_REPLY_TO_MSG, chatDisplay.getReplyToMsg());
            values.put(CD_REPLY_TO_NAME, chatDisplay.getReplyToName());

            id = db.insert(TABLE_CHAT_DETAIL, null, values);

            Log.e("DB", " ------- addChatDetail---------- NEW ADD");

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }


    //-------------------------

    public ArrayList<ChatDisplay> getAllChatDetail() {
        ArrayList<ChatDisplay> chatList = new ArrayList<>();

        try {

            String query = "SELECT * FROM " + TABLE_CHAT_DETAIL;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    ChatDisplay message = new ChatDisplay();
                    message.setChatTaskDetailId(cursor.getInt(1));
                    message.setHeaderId(cursor.getInt(2));
                    message.setTypeOfText(cursor.getInt(3));
                    message.setTextValue(cursor.getString(4));
                    message.setDateTime(cursor.getString(5));
                    message.setUserId(cursor.getInt(6));
                    message.setUserName(cursor.getString(7));
                    message.setDelStatus(cursor.getInt(8));
                    message.setMarkAsRead(cursor.getInt(9));
                    message.setSyncStatus(cursor.getInt(10));
                    message.setOfflineStatus(cursor.getInt(11));
                    chatList.add(message);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return chatList;
    }

    /*public ArrayList<ChatDisplay> getAllSQLiteChat(int headerId) {
        ArrayList<ChatDisplay> chatList = new ArrayList<>();

        try {


            String query = "SELECT * FROM " + TABLE_CHAT_DETAIL + " WHERE " + CD_HID + "=" + headerId + " ORDER BY " + CD_DATE;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    ChatDisplay chat = new ChatDisplay();
                    chat.setChatTaskDetailId(cursor.getInt(1));
                    chat.setHeaderId(cursor.getInt(2));
                    chat.setTypeOfText(cursor.getInt(3));
                    chat.setTextValue(cursor.getString(4));
                    chat.setDateTime(cursor.getString(5));
                    chat.setUserId(cursor.getInt(6));
                    chat.setUserName(cursor.getString(7));
                    chat.setDelStatus(cursor.getInt(8));
                    chat.setMarkAsRead(cursor.getInt(9));
                    chat.setSyncStatus(cursor.getInt(10));
                    chat.setOfflineStatus(cursor.getInt(11));

                    chatList.add(chat);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return chatList;
    }
*/

    //21-11-2019
    public ArrayList<ChatDisplay> getAllSQLiteChat(int headerId) {
        ArrayList<ChatDisplay> chatList = new ArrayList<>();

        try {
            String query = "SELECT * FROM " + TABLE_CHAT_DETAIL + " WHERE " + CD_HID + "=" + headerId + " AND " + CD_DEL_STATUS + "=1 ORDER BY " + CD_DATE;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    ChatDisplay chat = new ChatDisplay();
                    chat.setChatTaskDetailId(cursor.getInt(1));
                    chat.setHeaderId(cursor.getInt(2));
                    chat.setTypeOfText(cursor.getInt(3));
                    chat.setTextValue(cursor.getString(4));
                    chat.setDateTime(cursor.getString(5));
                    chat.setUserId(cursor.getInt(6));
                    chat.setUserName(cursor.getString(7));
                    chat.setDelStatus(cursor.getInt(8));
                    chat.setMarkAsRead(cursor.getInt(9));
                    chat.setSyncStatus(cursor.getInt(10));
                    chat.setOfflineStatus(cursor.getInt(11));
                    chat.setReplyToId(cursor.getInt(12));
                    chat.setReplyToMsgType(cursor.getInt(13));
                    chat.setReplyToMsg(cursor.getString(14));
                    chat.setReplyToName(cursor.getString(15));

                    chatList.add(chat);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return chatList;
    }




    /*public ArrayList<ChatDisplay> getAllNotSyncTypedChat() {
        ArrayList<ChatDisplay> chatList = new ArrayList<>();


        try {


            String query = "SELECT * FROM " + TABLE_CHAT_DETAIL + " WHERE " + CD_ID + "=0 AND " + CD_OFFLINE_STATUS + "=1";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    ChatDisplay chat = new ChatDisplay();
                    chat.setChatTaskDetailId(cursor.getInt(1));
                    chat.setHeaderId(cursor.getInt(2));
                    chat.setTypeOfText(cursor.getInt(3));
                    chat.setTextValue(cursor.getString(4));
                    chat.setDateTime(cursor.getString(5));
                    chat.setUserId(cursor.getInt(6));
                    chat.setUserName(cursor.getString(7));
                    chat.setDelStatus(cursor.getInt(8));
                    chat.setMarkAsRead(cursor.getInt(9));
                    chat.setSyncStatus(cursor.getInt(10));
                    chat.setOfflineStatus(cursor.getInt(11));

                    chatList.add(chat);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return chatList;
    }*/


    //21-11-2019
    public ArrayList<ChatDisplay> getAllNotSyncTypedChat() {
        ArrayList<ChatDisplay> chatList = new ArrayList<>();


        try {


            String query = "SELECT * FROM " + TABLE_CHAT_DETAIL + " WHERE " + CD_ID + "=0 AND " + CD_OFFLINE_STATUS + "=1";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    ChatDisplay chat = new ChatDisplay();
                    chat.setChatTaskDetailId(cursor.getInt(1));
                    chat.setHeaderId(cursor.getInt(2));
                    chat.setTypeOfText(cursor.getInt(3));
                    chat.setTextValue(cursor.getString(4));
                    chat.setDateTime(cursor.getString(5));
                    chat.setUserId(cursor.getInt(6));
                    chat.setUserName(cursor.getString(7));
                    chat.setDelStatus(cursor.getInt(8));
                    chat.setMarkAsRead(cursor.getInt(9));
                    chat.setSyncStatus(cursor.getInt(10));
                    chat.setOfflineStatus(cursor.getInt(11));
                    chat.setReplyToId(cursor.getInt(12));
                    chat.setReplyToMsgType(cursor.getInt(13));
                    chat.setReplyToMsg(cursor.getString(14));
                    chat.setReplyToName(cursor.getString(15));

                    chatList.add(chat);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return chatList;
    }


    public ChatDisplay getMessage(int id) {
        ChatDisplay msg = new ChatDisplay();
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_CHAT_DETAIL, new String[]{CD_ID,
                            CD_HID, CD_MSG_TYPE, CD_MSG, CD_DATE, CD_USER_ID, CD_USER_NAME, CD_DEL_STATUS, CD_MARK_READ, CD_SYNC_STATUS, CD_OFFLINE_STATUS}, CD_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                msg = new ChatDisplay(Integer.parseInt(cursor.getString(0)));
                cursor.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return msg;
    }

    public int updateChatDetailLastSyncStatus(int id, int status) {
        int res = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(CD_SYNC_STATUS, status);

            Log.e("DB ", "---------updateChatDetailLastSyncStatus------------- id : " + id);

            res = db.update(TABLE_CHAT_DETAIL, values, CD_ID + "=" + id, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;

    }

    public int updateChatDetailMessageRead() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CD_MARK_READ, 1);

        return db.update(TABLE_CHAT_DETAIL, values, CD_MARK_READ + "=0", null);
    }


    public int getChatDetailLastSyncId() {
        int lastSyncId = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT MAX(" + CD_ID + ") FROM " + TABLE_CHAT_DETAIL + " WHERE " + CD_SYNC_STATUS + "=1", null);
        if (cursor != null && cursor.moveToFirst()) {
            lastSyncId = cursor.getInt(0);
            cursor.close();
        }
        return lastSyncId;
    }

    public int getChatDetailIdPresentOrNot(int id) {
        int dbChatId = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + CD_ID + " FROM " + TABLE_CHAT_DETAIL + " WHERE " + CD_ID + "=" + id, null);
        if (cursor != null && cursor.moveToFirst()) {
            dbChatId = cursor.getInt(0);
            cursor.close();
        }
        Log.e("DB : ", "--------getChatDetailIdPresentOrNot -------- " + dbChatId);
        return dbChatId;
    }


    public void deleteChatDetailRecord(int pkId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHAT_DETAIL, ID + "=" + pkId, null);
        Log.e("DB : ", "-----------deleteChatDetailRecord----------- PK = " + pkId);
        db.close();
    }

    public void deleteChatDetailRecordByDetailId(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHAT_DETAIL, CD_ID + "=" + id, null);
        Log.e("DB : ", "-----------deleteChatDetailRecordByDetailId-----------  = " + id);
        db.close();
    }


    public int updateChatDetailReadStatus(int hId, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CD_MARK_READ, status);

        return db.update(TABLE_CHAT_DETAIL, values, CD_HID + "=" + hId + " AND " + CD_MARK_READ + "=0", null);
    }

    /*public int updateChatDetailReadStatus(int hId, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CD_MARK_READ, status);

        return db.update(TABLE_CHAT_DETAIL, values, CD_HID + "=" + hId, null);
    }*/

    public int updateChatDetailOfflineStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CD_OFFLINE_STATUS, status);

        Log.e("DB", "------------ UPDATE CHAT OFFLINE-------------");

        return db.update(TABLE_CHAT_DETAIL, values, CD_ID + "=" + id, null);
    }


    /*public ArrayList<ChatDisplay> getAllChatNotRead(int headerId) {
        ArrayList<ChatDisplay> chatList = new ArrayList<>();


        try {


            String query = "SELECT * FROM " + TABLE_CHAT_DETAIL + " WHERE " + CD_MARK_READ + "=1 AND " + CD_OFFLINE_STATUS + "=0 AND " + CD_SYNC_STATUS + "=1 AND " + CD_HID + "=" + headerId;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    ChatDisplay chat = new ChatDisplay();
                    chat.setChatTaskDetailId(cursor.getInt(1));
                    chat.setHeaderId(cursor.getInt(2));
                    chat.setTypeOfText(cursor.getInt(3));
                    chat.setTextValue(cursor.getString(4));
                    chat.setDateTime(cursor.getString(5));
                    chat.setUserId(cursor.getInt(6));
                    chat.setUserName(cursor.getString(7));
                    chat.setDelStatus(cursor.getInt(8));
                    chat.setMarkAsRead(cursor.getInt(9));
                    chat.setSyncStatus(cursor.getInt(10));
                    chat.setOfflineStatus(cursor.getInt(11));

                    chatList.add(chat);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return chatList;
    }*/

    //21-11-2019
    public ArrayList<ChatDisplay> getAllChatNotRead(int headerId) {
        ArrayList<ChatDisplay> chatList = new ArrayList<>();


        try {


            String query = "SELECT * FROM " + TABLE_CHAT_DETAIL + " WHERE " + CD_MARK_READ + "=1 AND " + CD_OFFLINE_STATUS + "=0 AND " + CD_SYNC_STATUS + "=1 AND " + CD_HID + "=" + headerId;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    ChatDisplay chat = new ChatDisplay();
                    chat.setChatTaskDetailId(cursor.getInt(1));
                    chat.setHeaderId(cursor.getInt(2));
                    chat.setTypeOfText(cursor.getInt(3));
                    chat.setTextValue(cursor.getString(4));
                    chat.setDateTime(cursor.getString(5));
                    chat.setUserId(cursor.getInt(6));
                    chat.setUserName(cursor.getString(7));
                    chat.setDelStatus(cursor.getInt(8));
                    chat.setMarkAsRead(cursor.getInt(9));
                    chat.setSyncStatus(cursor.getInt(10));
                    chat.setOfflineStatus(cursor.getInt(11));
                    chat.setReplyToId(cursor.getInt(12));
                    chat.setReplyToMsgType(cursor.getInt(13));
                    chat.setReplyToMsg(cursor.getString(14));
                    chat.setReplyToName(cursor.getString(15));

                    chatList.add(chat);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return chatList;
    }


    public int updateChatDetailReadStatusByDetailId(int detailId, int status) {
        //Log.e("DB","--------------updateChatDetailReadStatusByDetailId----------- id- "+detailId+"    Status- "+status);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CD_MARK_READ, status);

        return db.update(TABLE_CHAT_DETAIL, values, CD_ID + "=" + detailId, null);
    }


    public int updateChatDetailReadStatusByPKId(int pkId, int status) {
        Log.e("DB", "--------------updateChatDetailReadStatusByPKId----------- id- " + pkId + "    Status- " + status);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CD_MARK_READ, status);

        return db.update(TABLE_CHAT_DETAIL, values, ID + "=" + pkId, null);
    }

    public int updateChatDetailIdAndReadStatusByPKId(int pkId, int detailId, int status) {
        Log.e("DB", "--------------updateChatDetailReadStatusByPKId----------- id- " + pkId + "     detailId- " + detailId + "    Status- " + status);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CD_ID, detailId);
        values.put(CD_MARK_READ, status);

        return db.update(TABLE_CHAT_DETAIL, values, ID + "=" + pkId, null);
    }


    public ArrayList<Integer> getChatDetailIdsByReadStatus1And3(int headerId) {
        ArrayList<Integer> chatList = new ArrayList<>();

        try {

            String query = "SELECT " + CD_ID + " FROM " + TABLE_CHAT_DETAIL + " WHERE " + CD_MARK_READ + " IN(1,2) AND " + CD_HID + "=" + headerId;
            Log.e("DB", "-------getChatDetailIdsByReadStatus1And3-------- " + query);

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    chatList.add(cursor.getInt(0));
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("DB", "-------getChatDetailIdsByReadStatus1And3---op----- " + chatList);

        return chatList;
    }


    public int deleteChatDetailByHeader(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CD_DEL_STATUS, status);

        Log.e("DB", "------------ DELETE CHAT BY HEADER ID-------------");

        return db.update(TABLE_CHAT_DETAIL, values, CD_HID + "=" + id, null);
    }


    public int deleteChatDetailById(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CD_DEL_STATUS, status);

        Log.e("DB", "------------ DELETE CHAT BY ID-------------");

        return db.update(TABLE_CHAT_DETAIL, values, CD_ID + "=" + id, null);
    }


    //--------------------------CHAT HEADER--------------------------------

    public void addChatHeader(ChatTask chatTask) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(H_ID, chatTask.getHeaderId());
            values.put(H_DATE, chatTask.getCreatedDate());
            values.put(H_TITLE, chatTask.getHeaderName());
            values.put(H_CREATED_BY, chatTask.getCreatedUserId());
            values.put(H_ADMIN_IDS, chatTask.getAdminUserIds());
            values.put(H_ASSIGN_IDS, chatTask.getAssignUserIds());
            values.put(H_DESC, chatTask.getTaskDesc());
            values.put(H_IMAGE, chatTask.getImage());
            values.put(H_STATUS, chatTask.getStatus());
            values.put(H_PRIVILAGE, chatTask.getPrivilege());
            values.put(H_CLOSE_REQ_ID, chatTask.getRequestUserId());
            values.put(H_CLOSE_REQ_NAME, chatTask.getRequestUserName());
            values.put(H_LAST_DATE, chatTask.getLastDate());

            db.insert(TABLE_CHAT_HEADER, null, values);
            Log.e("DB : ", "---------addChatHeader-----------" + values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<ChatTask> getAllChatHeader() {
        ArrayList<ChatTask> chatList = new ArrayList<>();

        try {

            //String query = "SELECT * FROM " + TABLE_CHAT_HEADER;
            // String query = "SELECT *,(SELECT COUNT(*) FROM " + TABLE_CHAT_DETAIL + " WHERE " + CD_MARK_READ + "=0 AND " + CD_HID + "=" + H_ID + " ) AS unread FROM " + TABLE_CHAT_HEADER;
            String query = "SELECT h.*,(SELECT COUNT(*) FROM " + TABLE_CHAT_DETAIL + " WHERE " + CD_MARK_READ + "=0 AND " + CD_HID + "=h." + H_ID + " ) AS unread FROM " + TABLE_CHAT_HEADER + " h, " + TABLE_CHAT_DETAIL + " d WHERE h." + H_ID + "=d." + CD_HID + " GROUP BY h." + H_ID + " ORDER BY d." + CD_ID + " DESC";

            // String query = "SELECT h." + H_PKID + ",h." + H_ID + ",h." + H_DATE + ",h." + H_TITLE + ",h." + H_CREATED_BY + ",h." + H_ADMIN_IDS + ",h." + H_ASSIGN_IDS + ",h." + H_DESC + ",h." + H_IMAGE + ",h." + H_STATUS + ",h." + H_PRIVILAGE + " FROM " + TABLE_CHAT_HEADER + " as h," + TABLE_CHAT_DETAIL + " as d WHERE h." + H_ID + "=d." + CD_HID + " ORDER BY d." + CD_ID + " AND d." + CD_HID + " DESC";

            Log.e("DB : ", "-----------getAllChatHeader-------QUERY------------- " + query);

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    ChatTask message = new ChatTask();
                    message.setHeaderId(cursor.getInt(1));
                    message.setCreatedDate(cursor.getString(2));
                    message.setHeaderName(cursor.getString(3));
                    message.setCreatedUserId(cursor.getInt(4));
                    message.setAdminUserIds(cursor.getString(5));
                    message.setAssignUserIds(cursor.getString(6));
                    message.setTaskDesc(cursor.getString(7));
                    message.setImage(cursor.getString(8));
                    message.setStatus(cursor.getInt(9));
                    message.setPrivilege(cursor.getInt(10));
                    message.setRequestUserId(cursor.getInt(11));
                    message.setRequestUserName(cursor.getString(12));
                    message.setLastDate(cursor.getString(13));
                    message.setUnreadCount(cursor.getInt(14));
                    chatList.add(message);
                } while (cursor.moveToNext());
            }
            Log.e("DB : ", "-----------getAllChatHeader-----------" + chatList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return chatList;
    }

    public void removeAllChatHeader() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHAT_HEADER, null, null);
        Log.e("DB : ", "-----------removeAllChatHeader-----------");
        db.close();
    }

    /*public ChatTask getChatHeaderById(int id) {

        Log.e("DB", "------------getChatHeaderById----------- " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHAT_HEADER, new String[]{H_ID,
                        H_DATE, H_TITLE, H_CREATED_BY, H_ADMIN_IDS, H_ASSIGN_IDS, H_DESC, H_IMAGE, H_STATUS, H_CLOSE_REQ_ID, H_CLOSE_REQ_NAME, H_PRIVILAGE}, H_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        ChatTask header = new ChatTask();
        if (cursor != null && cursor.moveToFirst()) {
            header = new ChatTask(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    Integer.parseInt(cursor.getString(3)),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    Integer.parseInt(cursor.getString(8)),
                    Integer.parseInt(cursor.getString(9)),
                    cursor.getString(10),
                    Integer.parseInt(cursor.getString(11))

            );
            cursor.close();
        }

        Log.e("DB : ", "-------- getChatHeaderById----------- " + header);
        return header;
    }*/

    public ChatTask getChatHeaderById(int id) {

        Log.e("DB", "------------getChatHeaderById----------- " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHAT_HEADER, new String[]{H_ID,
                        H_DATE, H_TITLE, H_CREATED_BY, H_ADMIN_IDS, H_ASSIGN_IDS, H_DESC, H_IMAGE, H_STATUS, H_CLOSE_REQ_ID, H_CLOSE_REQ_NAME, H_PRIVILAGE, H_LAST_DATE}, H_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        ChatTask header = new ChatTask();
        if (cursor != null && cursor.moveToFirst()) {
            header = new ChatTask(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    Integer.parseInt(cursor.getString(3)),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    Integer.parseInt(cursor.getString(8)),
                    Integer.parseInt(cursor.getString(9)),
                    cursor.getString(10),
                    Integer.parseInt(cursor.getString(11)),
                    cursor.getString(12)
            );
            cursor.close();
        }

        Log.e("DB : ", "-------- getChatHeaderById----------- " + header);
        return header;
    }


   /* public int updateChatHeaderNewDate(int hId, long time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(H_NEW_DATE, time);

        return db.update(TABLE_CHAT_HEADER, values, H_ID + "=" + hId, null);
    }*/


}
