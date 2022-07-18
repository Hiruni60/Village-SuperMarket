package bo.custom.impl;

import bo.custom.placeOrderBo;
import dao.custom.impl.ItemDaoImpl;
import dao.custom.impl.OrderDetailsDaoImpl;
import dao.custom.impl.OrdersDaoImpl;
import db.DbConnection;
import dto.*;
import entity.CustomOrder;
import entity.Item;
import entity.OrderDetails;
import entity.Orders;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class placeOrderBoImpl implements placeOrderBo {
    private final OrdersDaoImpl orderDAO = new OrdersDaoImpl();
    private final OrderDetailsDaoImpl orderDetailsDAO = new OrderDetailsDaoImpl();
    private final ItemDaoImpl itemDAO = new ItemDaoImpl();

    @Override
    public boolean cancelOrder(String oid, ArrayList<CustomOrderDto> orderDTOS) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        boolean save;
        // updating qtyOnHand in items table
        for (CustomOrderDto dto : orderDTOS) {
            Item item = itemDAO.search(dto.getItemCode());
            item.setQtyOnHand(item.getQtyOnHand()+dto.getOrderQty());
            save = itemDAO.update(item);
            if(!save){
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
        }
        save = orderDAO.delete(oid);
        if(!save){
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }
        connection.commit();
        connection.setAutoCommit(true);
        return true;
    }

    @Override
    public boolean purchaseOrder(OrdersDto orderDTO, ArrayList<OrderDetailsDto> orderDetailDTO) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        boolean isOrderAdded = orderDAO.save(new Orders(orderDTO.getOrderID(),orderDTO.getCustomerID()));

        if(!isOrderAdded){
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }
        System.out.println("saved");
        for (OrderDetailsDto dto : orderDetailDTO) {
            boolean isOderDetailsSaved = orderDetailsDAO.save(new OrderDetails(dto.getOrderID(),dto.getItemCode(),dto.getOrderQty(),dto.getDiscount(),dto.getTotalPrice()));
            if (!isOderDetailsSaved){
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
            System.out.println("details saved");
            //Search & Update Item
            Item item = itemDAO.search(dto.getItemCode());
            if(item==null){
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
            item.setQtyOnHand(item.getQtyOnHand() - dto.getOrderQty());

            //update item
            boolean update = itemDAO.update(new Item(item.getCode(), item.getDescription(),  item.getPackSize(), item.getUnitPrice(), item.getDiscount(), item.getQtyOnHand()));

            if (!update) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
            System.out.println("item updated and details saved");

        }
        connection.commit();
        connection.setAutoCommit(true);
        return true;
    }

    @Override
    public CustomerDto searchCustomer(String id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean updateOrder(ArrayList<CustomOrderDto> orderDTOS,ArrayList<CustomOrderDto> removeOrderDTOS, String id) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        for (CustomOrderDto dto : orderDTOS) {
            if(!orderDetailsDAO.update(new OrderDetails(id,dto.getItemCode(),dto.getOrderQty(),dto.getDiscount(),dto.getTotalPrice()))){
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
        }
        for (CustomOrderDto dto : removeOrderDTOS) {
            if(!orderDetailsDAO.deleteCustomOrder(id,dto.getItemCode())){
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
        }
        connection.commit();
        connection.setAutoCommit(true);
        return true;
    }


    @Override
    public ItemDto searchItem(String code) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean checkItemIsAvailable(String code) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean checkCustomerIsAvailable(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewOrderID() throws SQLException, ClassNotFoundException {
        return orderDAO.genarateId();
    }

    @Override
    public ArrayList<CustomerDto> getAllCustomers() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public ArrayList<ItemDto> getAllItems() throws SQLException, ClassNotFoundException {
        return null;
    }

    public ArrayList<ReportDto> generateReport(int code) throws  SQLException, ClassNotFoundException{
        ArrayList<CustomOrder> rpt = orderDetailsDAO.generateReport(code);
        ArrayList<ReportDto> report = new ArrayList<>();
        if(rpt!=null){
            for (CustomOrder customOrder : rpt) {
                report.add(new ReportDto(customOrder.getItemCode(),customOrder.getTotalPrice(),customOrder.getOrderQty()));
            }
            return report;
        }
        return null;
    }

    public ArrayList<CustomOrderDto> getOrderDetailsFiltered(String oid) throws SQLException, ClassNotFoundException{
        ArrayList<CustomOrder> orderDetails = orderDetailsDAO.getAllOrdersFiltered(oid);
        ArrayList<CustomOrderDto> dtOs = new ArrayList<>();
        for (CustomOrder cod : orderDetails) {
            dtOs.add(new CustomOrderDto(cod.getItemCode(),cod.getDescription(),cod.getOrderQty(),cod.getQtyOnHand(),cod.getDiscount(),cod.getTotalPrice()));
        }
        return dtOs;
    }
}
