package dao.custom.impl;

import CrudUtil.CrudUtil;
import dao.custom.OrderDetailsDao;
import entity.CustomOrder;
import entity.OrderDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailsDaoImpl implements OrderDetailsDao {

    @Override
    public boolean save(OrderDetails entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO OrderDetails (oid, itemCode, qty, discount, totalPrice) VALUES (?,?,?,?,?)", entity.getOid(), entity.getItemCode(), entity.getQty(), entity.getDiscount(), entity.getTotalPrice());
    }

    @Override
    public boolean exist(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public OrderDetails search(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public ArrayList<OrderDetails> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean update(OrderDetails entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("UPDATE OrderDetails SET qty=?, discount=?, totalPrice=? WHERE itemCode=? AND oid=?",entity.getQty(), entity.getDiscount(), entity.getTotalPrice(),entity.getItemCode(), entity.getOid());
    }

    @Override
    public String genarateId() throws SQLException, ClassNotFoundException {
        return null;
    }

    public boolean deleteCustomOrder(String oid, String itemCode) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("DELETE FROM orderDetails WHERE oid=? AND itemCode=?", oid, itemCode);
    }

    public ArrayList<CustomOrder> generateReport(int code) throws SQLException, ClassNotFoundException {
        ArrayList<CustomOrder> report = new ArrayList<>();
        if(code==0){
            ResultSet rst = CrudUtil.executeQuery("CALL DAILY_REPORT()");
            while(rst.next()){
                report.add(new CustomOrder(rst.getString(1),rst.getDouble(2),rst.getInt(3)));
            }
            return report;
        }
        else if(code==1){
            ResultSet rst = CrudUtil.executeQuery("CALL MONTHLY_REPORT()");
            while(rst.next()){
                report.add(new CustomOrder(rst.getString(1),rst.getDouble(2),rst.getInt(3)));
            }
            return report;
        }
        else if(code==2){
            ResultSet rst = CrudUtil.executeQuery("CALL ANNUAL_REPORT()");
            while(rst.next()){
                report.add(new CustomOrder(rst.getString(1),rst.getDouble(2),rst.getInt(3)));
            }
            return report;
        }
        return null;
    }

    public ArrayList<CustomOrder> getAllOrdersFiltered(String oid) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("select od.itemCode,it.description, od.qty, it.qtyOnHand, od.discount, od.totalPrice from orderDetails od inner join item it on od.itemCode=it.code where od.oid = ?", oid);
        ArrayList<CustomOrder> orderDetails = new ArrayList<>();
        while(rst.next()){
            orderDetails.add(new CustomOrder(rst.getString(1),rst.getString(2),rst.getInt(3),rst.getInt(4),rst.getDouble(5), rst.getDouble(6)));
        }
        return orderDetails;
    }
}
