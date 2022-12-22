package com.example.gamecenter.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DB_Handle extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "GameCenter";

    private static final String TABLE_GAME = "Game";
    private static final String KEY_ID_TABLE_GAME = "ID";
    private static final String KEY_GAME_NAME_TABLE_GAME = "GameName";

    private static final String TABLE_MODE_GAME = "ModeGame";
    private static final String KEY_ID_TABLE_MODE_GAME = "ID";
    private static final String KEY_MODE_NAME_TABLE_MODE_GAME = "Mode_Name";
    private static final String KEY_ID_GAME_TABLE_MODE_GAME = "ID_Game";
    private static final String KEY_TIMES_TABLE_MODE_GAME = "Times";
    private static final String KEY_TRIES_TABLE_MODE_GAME = "Tries";


    public DB_Handle(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("DEBUG DB", "DB_Handle: Contructor");
//        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DEBUG DB", "onCreate: is running...");
        String CREATE_GAME_TABLE = "CREATE TABLE " + TABLE_GAME + "("
                + KEY_ID_TABLE_GAME + " INTEGER PRIMARY KEY," + KEY_GAME_NAME_TABLE_GAME + " TEXT" + ")";
        String CREATE_MODE_GAME_TABLE = "CREATE TABLE " + TABLE_MODE_GAME + "("
                + KEY_ID_TABLE_MODE_GAME + " INTEGER PRIMARY KEY,"
                + KEY_ID_GAME_TABLE_MODE_GAME + " INTEGER,"
                + KEY_MODE_NAME_TABLE_MODE_GAME + " TEXT, "
                + KEY_TRIES_TABLE_MODE_GAME + " INTEGER, "
                + KEY_TIMES_TABLE_MODE_GAME + " INTEGER" + ")";

        db.execSQL(CREATE_GAME_TABLE);
        db.execSQL(CREATE_MODE_GAME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d("DEBUG DB", "onUpgrade: is running...");
    }

    // Query NOT need return data
    public void QueryStatement(String sql){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
    }

    public void addNewGame(Game game){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GAME_NAME_TABLE_GAME, game.getGameName()); // Contact Name

        db.insert(TABLE_GAME, null, values);
        db.close();
        Log.d("DEBUG DB", "addNewGame: SUCCESS");
    }

    public void addNewScore(ScoreGame game){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_GAME_TABLE_MODE_GAME, game.getGameID());
        values.put(KEY_MODE_NAME_TABLE_MODE_GAME, game.getGameMode());
        values.put(KEY_TRIES_TABLE_MODE_GAME, game.getTries());
        values.put(KEY_TIMES_TABLE_MODE_GAME, game.getTime());

        db.insert(TABLE_MODE_GAME, null, values);
        db.close();
        Log.d("DEBUG DB", "addNewScore: SUCCESS");
    }

    public List<Game> getListGame(){
        List<Game> listGame = new ArrayList<Game>();
        String selectQuery = "SELECT * FROM " + TABLE_GAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Game game = new Game();
                game.setID(Integer.parseInt(cursor.getString(0)));
                game.setGameName(cursor.getString(1));

                // Adding contact to list
                listGame.add(game);
            } while (cursor.moveToNext());
        }
        return listGame;
    }

    public List<ScoreGame> getScoreByGameID(int gameID){
        List<ScoreGame> listScore = new ArrayList<ScoreGame>();
        String selectQuery = "SELECT * FROM " + TABLE_MODE_GAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ScoreGame gameList = new ScoreGame();
                gameList.setID(Integer.parseInt(cursor.getString(0)));
                gameList.setGameID(Integer.parseInt(cursor.getString(1)));
                gameList.setGameMode(cursor.getString(2));
                gameList.setTries(Integer.parseInt(cursor.getString(3)));
                gameList.setTime(Integer.parseInt(cursor.getString(4)));

                if(gameID == gameList.getGameID()){
                    listScore.add(gameList);
                }
            } while (cursor.moveToNext());
        }
        return listScore;
    }

    public List<ScoreGame> getScoreOfModeGameByID(int gameID, String modeGame){
        String selectQuery = "SELECT * FROM " + TABLE_MODE_GAME;
        List<ScoreGame> result = new ArrayList<ScoreGame>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ScoreGame gameList = new ScoreGame();
                gameList.setID(Integer.parseInt(cursor.getString(0)));
                gameList.setGameID(Integer.parseInt(cursor.getString(1)));
                gameList.setGameMode(cursor.getString(2));
                gameList.setTries(Integer.parseInt(cursor.getString(3)));
                gameList.setTime(Integer.parseInt(cursor.getString(4)));

                if(gameID == gameList.getGameID() && modeGame.contains(gameList.getGameMode())){
                    result.add(gameList);
                }
            } while (cursor.moveToNext());
        }
        return result;
    }


    public void deleteScore(int IDScore){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_MODE_GAME + " WHERE " + KEY_ID_TABLE_MODE_GAME + " = " + IDScore;
        db.execSQL(sql);
        Log.d("DEBUG DB", "deleteScore: SUCCESSFUL");
    }

    public ScoreGame getHighestTries(List<ScoreGame> games){
        int max = games.get(0).getTries();
        ScoreGame result = games.get(0);
        for(ScoreGame game: games){
            if(game.getTries() > max){
                max = game.getTries();
                result = game;
            }
        }
        return result;
    }

}
