package model;


public class CommentView {
    private int id;
    private  String name;
    private  int rating;
    private  String content;
    private String createAt;
    private String imgComment;
    private boolean active;

    public CommentView(String name, int rating, String content, String createAt, String imgComment) {
        this.name = name;
        this.rating = rating;
        this.content = content;
        this.createAt = createAt;
        this.imgComment = imgComment;
    }
    public CommentView() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
    public String getImgComment() {
        return imgComment;
    }
    public void setImgComment(String imgComment) {
        this.imgComment = imgComment;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CommentView{" +
                "name='" + name + '\'' +
                ", rating=" + rating +
                ", content='" + content + '\'' +
                ", createAt=" + createAt + active+
                '}';
    }
}
