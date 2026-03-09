package model;

import java.time.LocalDate;

public class Voucher {
    private int id;
    private String code;
    private String description;
    private int conditionPrice;
    private String conditionBook;
    private String conditionPublisher;
    private String start_date;
    private String end_date;
    private int usage_limit;
    private double valuee;
    private String type;



    public Voucher() {
    }

    public Voucher(int id, String code, String description, int conditionPrice, String conditionBook, String conditionPublisher, String start_date, String end_date, int usage_limit, double valuee, String type) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.conditionPrice = conditionPrice;
        this.conditionBook = conditionBook;
        this.conditionPublisher = conditionPublisher;
        this.start_date = start_date;
        this.end_date = end_date;
        this.usage_limit = usage_limit;
        this.valuee = valuee;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getConditionPrice() {
        return conditionPrice;
    }

    public void setConditionPrice(int conditionPrice) {
        this.conditionPrice = conditionPrice;
    }

    public String getConditionBook() {
        return conditionBook;
    }

    public void setConditionBook(String conditionBook) {
        this.conditionBook = conditionBook;
    }

    public String getConditionPublisher() {
        return conditionPublisher;
    }

    public void setConditionPublisher(String conditionPublisher) {
        this.conditionPublisher = conditionPublisher;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getUsage_limit() {
        return usage_limit;
    }

    public void setUsage_limit(int usage_limit) {
        this.usage_limit = usage_limit;
    }

    public double getValuee() {
        return valuee;
    }

    public void setValuee(double valuee) {
        this.valuee = valuee;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartDateFormatted() {
        if (start_date == null || start_date.isEmpty()) return "";
        return start_date.substring(8, 10) + "/" +
                start_date.substring(5, 7) + "/" +
                start_date.substring(0, 4);
    }

    public String getEndDateFormatted() {
        if (end_date == null || end_date.isEmpty()) return "";
        return end_date.substring(8, 10) + "/" +
                end_date.substring(5, 7) + "/" +
                end_date.substring(0, 4);
    }

    public boolean isActive() {
        if (start_date == null || end_date == null) return false;

        LocalDate today = LocalDate.now();
        LocalDate start = LocalDate.parse(start_date);
        LocalDate end = LocalDate.parse(end_date);

        return !today.isBefore(start) && !today.isAfter(end);
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", conditionPrice=" + conditionPrice +
                ", conditionBook='" + conditionBook + '\'' +
                ", conditionPublisher='" + conditionPublisher + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", usage_limit=" + usage_limit +
                ", valuee=" + valuee +
                ", type='" + type + '\'' +
                '}';
    }
}
