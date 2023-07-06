package com.snd.app.ui.read;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "SND-DEVELOP";
    public static final int DATABASE_VERSION = 1;
    // 이렇게까지 할 필요 없음

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // 데이터베이스가 처음 생성될 때 호출되는 메소드
        // 여기서 테이블을 생성합니다.

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스가 업그레이드되어야 할 때 호출되는 메소드
        // 여기서 테이블을 삭제하고 다시 생성하거나 ALTER TABLE 명령을 사용하여 테이블을 변경할 수 있습니다.
        db.execSQL("DROP TABLE IF EXISTS your_table_name");
        onCreate(db);
    }

    

}
