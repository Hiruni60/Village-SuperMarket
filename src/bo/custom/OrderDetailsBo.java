package bo.custom;

import bo.SuperBo;
import dto.OrderDetailsDto;
import dto.OrdersDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDetailsBo extends SuperBo {
    ArrayList<OrderDetailsDto> getAllOrderdetails() throws SQLException, ClassNotFoundException;

    public boolean saveOrderdetails(OrderDetailsDto dto) throws SQLException, ClassNotFoundException;

    public boolean existOrderdetails(String id) throws SQLException, ClassNotFoundException;

    public boolean updateOrderdetails(OrderDetailsDto dto) throws SQLException, ClassNotFoundException;

    public boolean deleteOrderdetails(String id) throws SQLException, ClassNotFoundException;

    public String genarateNewOrderCode() throws SQLException, ClassNotFoundException;

    OrderDetailsDto search(String id)throws SQLException, ClassNotFoundException;

    public ArrayList<OrdersDto> getAllOrdersByCusId(String id)throws SQLException, ClassNotFoundException;
}
