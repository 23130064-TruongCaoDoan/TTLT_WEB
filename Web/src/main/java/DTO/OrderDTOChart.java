package DTO;

public class OrderDTOChart {
    private String label;
    private int total;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrderDTOChart{" +
                "label='" + label + '\'' +
                ", total=" + total +
                '}';
    }
}
