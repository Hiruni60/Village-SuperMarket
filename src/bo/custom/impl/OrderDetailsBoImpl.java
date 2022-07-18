package bo.custom.impl;

import CrudUtil.CrudUtil;
import bo.custom.OrderDetailsBo;
import dao.DAOFactory;
import dao.custom.OrderDetailsDao;
import dto.OrderDetailsDto;
import dto.OrdersDto;
import entity.OrderDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailsBoImpl implements OrderDetailsBo {

    OrderDetailsDao orderDetailsDo= (OrderDetailsDao) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAILS);

    @Override
    public ArrayList<OrderDetailsDto> getAllOrderdetails() throws SQLException, ClassNotFoundException {
        ArrayList<OrderDetailsDto>arrayList=new ArrayList<>();
        ArrayList<OrderDetails> all = orderDetailsDo.getAll();
        for (OrderDetails orderDetail : all) {
            arrayList.add(new OrderDetailsDto(orderDetail.getOid(),orderDetail.getItemCode(),orderDetail.getQty(),orderDetail.getDiscount(),orderDetail.getTotalPrice()));
        }
        return arrayList;
    }

    @Override
    public boolean saveOrderdetails(OrderDetailsDto dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean existOrderdetails(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean updateOrderdetails(OrderDetailsDto dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean deleteOrderdetails(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String genarateNewOrderCode() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public OrderDetailsDto search(String id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public ArrayList<OrdersDto> getAllOrdersByCusId(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM orders WHERE customerId=?", id);
        ArrayList<OrdersDto> allOrders=new ArrayList<>();
        while(resultSet.next()){
            OrdersDto order= new OrdersDto(resultSet.getString(1),
                    resultSet.getString(2)
            );

            allOrders.add(order);
        }
        return allOrders;
    }
}
