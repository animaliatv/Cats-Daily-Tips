package com.animalia.hassan.catsdailytips.fragmentsQues;

/**
 * Created by Hassan on 6/6/2016.
 */

public class Ques {

    private int _id;
    private String question;
    private String answer;
    private String favourite;
    private int q_note_id;
    private String comment;
    private int rowCount;

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public Ques(int _id, String question, String answer, String favourite, int q_note_id, String comment) {
        this._id = _id;
        this.question = question;
        this.answer = answer;
        this.favourite = favourite;
        this.q_note_id = q_note_id;
        this.comment = comment;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getQ_note_id() {
        return q_note_id;
    }

    public void setQ_note_id(int q_note_id) {
        this.q_note_id = q_note_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
