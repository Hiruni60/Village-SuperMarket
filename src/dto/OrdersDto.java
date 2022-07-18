package dto;

public class OrdersDto {
    private String OrderID;
    private String CustomerID;

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public OrdersDto(String orderID, String customerID) {
        OrderID = orderID;
        CustomerID = customerID;
    }
}
