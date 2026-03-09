package model;

import java.time.LocalDate;

public class Comment {
    private int id;
    private int bookId;
    private int userId;
    private int rating;
    private String comment;
    private LocalDate createdAt;
    private String imgComment;
    private boolean isActive;

    public Comment(int id, int bookId, int userId, int rating, String comment, LocalDate createdAt, String imgComment) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.imgComment = imgComment;
    }
    public Comment() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getbookId() {
        return bookId;
    }

    public void setbookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    public String getImgComment() {
        return imgComment;
    }
    public void setImgComment(String imgComment) {
        this.imgComment = imgComment;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
}
