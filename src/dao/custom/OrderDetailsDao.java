package dao.custom;

import dao.CrudDao;
import entity.CustomOrder;
import entity.OrderDetails;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDetailsDao extends CrudDao<OrderDetails,String> {
    public ArrayList<CustomOrder> generateReport(int code) throws SQLException, ClassNotFoundException;
    public boolean deleteCustomOrder(String oid, String itemCode) throws SQLException, ClassNotFoundException;
}
