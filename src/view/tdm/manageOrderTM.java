package view.tdm;

public class manageOrderTM {
    private String itemCode;
    private String description;
    private Integer orderQty;
    private Integer qtyOnHand;
    private Double Discount;
    private Double totalPrice;

    public manageOrderTM(String itemCode, Integer orderQty) {
        this.itemCode = itemCode;
        this.orderQty = orderQty;
    }

    @Override
    public String toString() {
        return "manageOrderTM{" +
                "itemCode='" + itemCode + '\'' +
                ", description='" + description + '\'' +
                ", orderQty=" + orderQty +
                ", qtyOnHand=" + qtyOnHand +
                ", Discount=" + Discount +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Integer orderQty) {
        this.orderQty = orderQty;
    }

    public Integer getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(Integer qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public Double getDiscount() {
        return Discount;
    }

    public void setDiscount(Double discount) {
        Discount = discount;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public manageOrderTM() {
    }

    public manageOrderTM(String itemCode, String description, Integer orderQty, Integer qtyOnHand, Double discount, Double totalPrice) {
        this.itemCode = itemCode;
        this.description = description;
        this.orderQty = orderQty;
        this.qtyOnHand = qtyOnHand;
        Discount = discount;
        this.totalPrice = totalPrice;
    }
}
