package bo.custom;

import bo.SuperBo;
import dto.CustomerDto;
import dto.ItemDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemBo extends SuperBo {
    ArrayList<ItemDto> getAllItems() throws SQLException, ClassNotFoundException;

    boolean deleteItem(String code) throws SQLException, ClassNotFoundException;

    boolean saveItem(ItemDto dto) throws SQLException, ClassNotFoundException;

    boolean updateItem(ItemDto dto) throws SQLException, ClassNotFoundException;

    boolean itemExist(String code) throws SQLException, ClassNotFoundException;

    String generateNewItemCode() throws SQLException, ClassNotFoundException;

    public ItemDto search(String s) throws SQLException, ClassNotFoundException;

    public ItemDto find(String code) throws SQLException, ClassNotFoundException;
}
