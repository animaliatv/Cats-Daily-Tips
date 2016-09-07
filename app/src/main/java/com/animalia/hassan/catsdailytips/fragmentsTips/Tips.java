package com.animalia.hassan.catsdailytips.fragmentsTips;

/**
 * Created by Hassan on 6/6/2016.
 */

public class Tips {

    private int _id;
    private String tipTitle;
    private String tipBody;
    private String favourite;
    private int t_note_id;
    private String tipComment;
    private int rowCount;

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public Tips(int _id, String tipTitle, String tipBody, String favourite, int t_note_id, String tipComment) {
        this._id = _id;
        this.tipTitle = tipTitle;
        this.tipBody = tipBody;
        this.favourite = favourite;
        this.t_note_id = t_note_id;
        this.tipComment = tipComment;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getT_note_id() {
        return t_note_id;
    }

    public void setT_note_id(int t_note_id) {
        this.t_note_id = t_note_id;
    }

    public String getTipBody() {
        return tipBody;
    }

    public void setTipBody(String tipBody) {
        this.tipBody = tipBody;
    }

    public String getTipTitle() {
        return tipTitle;
    }

    public void setTipTitle(String tipTitle) {
        this.tipTitle = tipTitle;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public String getTipComment() {
        return tipComment;
    }

    public void setTipComment(String tipComment) {
        this.tipComment = tipComment;
    }
}
