package com.github.vincent_fuchs.custom_build_rules.model;

public class CommentOnTable {
    @Override
    public String toString() {

        return "CommentOnTable{" +
                "comment='" + comment + '\'' +
                ", table='" + table + '\'' +
                '}';
    }

    public CommentOnTable(String comment, String table) {
        this.comment = comment;
        this.table = table;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    private String comment;

    private String table;


}
