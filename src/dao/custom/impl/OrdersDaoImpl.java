package dao.custom.impl;

import CrudUtil.CrudUtil;
import dao.custom.OrdersDao;
import entity.Orders;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersDaoImpl implements OrdersDao {
    @Override
    public boolean save(Orders entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO `Orders` (oid, date, customerID) VALUES (?,NOW(),?)", entity.getOid(), entity.getCustomerID());
    }

    @Override
    public boolean exist(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String oid) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("DELETE FROM `Orders` WHERE oid=?", oid);
    }

    @Override
    public Orders search(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public ArrayList<Orders> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean update(Orders dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String genarateId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT oid FROM `Orders` ORDER BY oid DESC LIMIT 1;");
        return rst.next() ? String.format("OID-%03d", (Integer.parseInt(rst.getString("oid").replace("OID-", "")) + 1)) : "OID-001";
    }
}
