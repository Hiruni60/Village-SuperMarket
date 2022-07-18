package bo.custom;

import bo.SuperBo;
import dto.*;

import java.sql.SQLException;
import java.util.ArrayList;

public interface placeOrderBo extends SuperBo {
    boolean cancelOrder(String oid, ArrayList<CustomOrderDto> orderDTOS) throws SQLException, ClassNotFoundException;

    boolean purchaseOrder(OrdersDto ODto, ArrayList<OrderDetailsDto> ODDto) throws SQLException, ClassNotFoundException;

    CustomerDto searchCustomer(String id) throws SQLException, ClassNotFoundException;

    ItemDto searchItem(String code) throws SQLException, ClassNotFoundException;

    boolean checkItemIsAvailable(String code) throws SQLException, ClassNotFoundException;

    boolean checkCustomerIsAvailable(String id) throws SQLException, ClassNotFoundException;

    String generateNewOrderID() throws SQLException, ClassNotFoundException;

    ArrayList<CustomerDto> getAllCustomers() throws SQLException, ClassNotFoundException;

    ArrayList<ItemDto> getAllItems() throws SQLException, ClassNotFoundException;

    //public ArrayList<CustomOrderDTO> getOrderDetailsFiltered(String oid) throws SQLException, ClassNotFoundException;

    //public boolean cancelOrder(String oid, ArrayList<CustomOrderDTO> orderDTOS) throws SQLException, ClassNotFoundException;

    //public boolean updateOrder(ArrayList<CustomOrderDTO> orderDTOS,ArrayList<CustomOrderDTO> removeOrderDTOS, String id) throws SQLException, ClassNotFoundException;

    //public ArrayList<ReportDTO> generateReport(int code) throws  SQLException, ClassNotFoundException;

    public ArrayList<ReportDto> generateReport(int code) throws  SQLException, ClassNotFoundException;

    public ArrayList<CustomOrderDto> getOrderDetailsFiltered(String oid) throws SQLException, ClassNotFoundException;

    public boolean updateOrder(ArrayList<CustomOrderDto> orderDTOS,ArrayList<CustomOrderDto> removeOrderDTOS, String id) throws SQLException, ClassNotFoundException;
}
