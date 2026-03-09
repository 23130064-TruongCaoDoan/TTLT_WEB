package model;

public class RatingStartView {
    private int rating;
    private int total;
    private double percent;

    public RatingStartView(double percent, int rating, int total) {
        this.percent = percent;
        this.rating = rating;
        this.total = total;
    }

    public RatingStartView() {
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return
                "rating=" + rating +
                ", total=" + total +
                ", percent=" + percent;
    }
}
