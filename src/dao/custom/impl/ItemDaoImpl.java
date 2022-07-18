package dao.custom.impl;

import CrudUtil.CrudUtil;
import dao.custom.ItemDao;
import entity.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDaoImpl implements ItemDao{

    public ArrayList<Item> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Item");
        ArrayList<Item> allItems = new ArrayList<>();
        while (rst.next()) {
            allItems.add(new Item(rst.getString(1), rst.getString(2), rst.getString(3), rst.getDouble(4), rst.getDouble(5), rst.getInt(6)));
        }
        return allItems;
    }

    public boolean delete(String code) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("DELETE FROM Item WHERE code=?", code);
    }

    public boolean save(Item entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO Item (code, description, packSize, unitPrice, discount, qtyOnHand) VALUES (?,?,?,?,?,?)", entity.getCode(), entity.getDescription(), entity.getPackSize(), entity.getUnitPrice(), entity.getDiscount(), entity.getQtyOnHand());
    }

    public boolean update(Item entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("UPDATE Item SET description=?, packSize=?, unitPrice=?, discount=?, qtyOnHand=? WHERE code=?", entity.getDescription(),  entity.getPackSize(), entity.getUnitPrice(), entity.getDiscount(), entity.getQtyOnHand(), entity.getCode());
    }

    public Item search(String code) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Item WHERE code=?", code);
        if (rst.next()) {
            return new Item(rst.getString(1), rst.getString(2), rst.getString(3), rst.getDouble(4), rst.getDouble(5), rst.getInt(6));
        }
        return null;
    }

    public boolean exist(String code) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeQuery("SELECT code FROM Item WHERE code=?", code).next();
    }

    public String genarateId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT code FROM Item ORDER BY code DESC LIMIT 1;");
        if (rst.next()) {
            String id = rst.getString("code");
            int newItemId = Integer.parseInt(id.replace("I00-", "")) + 1;
            return String.format("I00-%03d", newItemId);
        } else {
            return "I00-001";
        }
    }


}
