package com.animalia.hassan.catsdailytips.database;

/**
 * Created by Hassan on 6/6/2016.
 */
/*A Class to define the structure of the schema.*/
public class CatTips {
    public CatTips() {
    }

    public static class TipsEntry {

        public static final String TABLE_NAME = "cats";
        public static final String Q_ID = "_id";
        public static final String Q_COLUMN_QUESTION = "question";
        public static final String Q_COLUMN_ANSWER = "answer";
        public static final String Q_COLUMN_FAVOURITE = "favourite";
        public static final String Q_COLUMN_NOTE_ID = "q_note_id";
        public static final String Q_COLUMN_COMMENT = "comment";

    }
}
