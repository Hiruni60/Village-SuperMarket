package view.tdm;

public class reportTM {
     private String oid;
     private String itemCode;
     private int qty;
     private double discount;
    private double totalPrice;

    public reportTM(String oid, String itemCode, int qty, double discount) {
        this.oid = oid;
        this.itemCode = itemCode;
        this.qty = qty;
        this.discount = discount;
    }

    public reportTM() {
    }

    @Override
    public String toString() {
        return "reportTM{" +
                "oid='" + oid + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", qty=" + qty +
                ", discount=" + discount +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public reportTM(String oid, String itemCode, int qty, double discount, double totalPrice) {
        this.oid = oid;
        this.itemCode = itemCode;
        this.qty = qty;
        this.discount = discount;
        this.totalPrice = totalPrice;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
