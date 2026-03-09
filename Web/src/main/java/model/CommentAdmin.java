package model;

import java.time.LocalDate;

public class CommentAdmin {
    private int id;
    private String title;
    private String name;
    private int rating;
    private String content;
    private LocalDate createAt;
    private boolean isActive;

    public CommentAdmin() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "CommentAdmin{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", content='" + content + '\'' +
                ", createAt=" + createAt +
                ", isActive=" + isActive +
                '}';
    }
}
