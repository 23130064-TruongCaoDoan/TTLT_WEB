package model;

public class AdminBookRateView {
    private String title;
    private double rating;

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "AdminBookRateView{" +
                "title='" + title + '\'' +
                ", rating=" + rating +
                '}';
    }
}
