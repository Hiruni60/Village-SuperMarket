package view.tdm;


import javafx.scene.control.Button;

public class cartTM {
    private String itemCode;
    private Double unitPrice;
    private Integer totQty;
    private Double discount;
    private Double totPrice;
    private Button delete;

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    @Override
    public String toString() {
        return "cartTM{" +
                "itemCode='" + itemCode + '\'' +
                ", unitPrice=" + unitPrice +
                ", totQty=" + totQty +
                ", discount=" + discount +
                ", totPrice=" + totPrice +
                '}';
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getTotQty() {
        return totQty;
    }

    public void setTotQty(Integer totQty) {
        this.totQty = totQty;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTotPrice() {
        return totPrice;
    }

    public void setTotPrice(Double totPrice) {
        this.totPrice = totPrice;
    }


    public cartTM() {
    }

    public cartTM(String itemCode, Double unitPrice, Integer totQty, Double discount, Double totPrice) {
        this.itemCode = itemCode;
        this.unitPrice = unitPrice;
        this.totQty = totQty;
        this.discount = discount;
        this.totPrice = totPrice;
        this.delete = new Button("delete");
    }
}
