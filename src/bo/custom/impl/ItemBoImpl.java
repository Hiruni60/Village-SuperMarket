package bo.custom.impl;

import bo.custom.ItemBo;
import dao.DAOFactory;
import dao.custom.ItemDao;
import dto.CustomerDto;
import dto.ItemDto;
import entity.Customer;
import entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;

public class ItemBoImpl implements ItemBo {
    private final ItemDao itemDAO = (ItemDao) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
//    private final ItemDAO itemDAO = new ItemDAOImpl();

    @Override
    public ArrayList<ItemDto> getAllItems() throws SQLException, ClassNotFoundException {
        ArrayList<Item> all = itemDAO.getAll();
        ArrayList<ItemDto> allItems= new ArrayList<>();
        for (Item item : all) {
            allItems.add(new ItemDto(item.getCode(),item.getDescription(),item.getPackSize(),item.getUnitPrice(),item.getDiscount(),item.getQtyOnHand()));
        }
        return allItems;
    }

    @Override
    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException {
        return itemDAO.delete(code);
    }

    @Override
    public boolean saveItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        return itemDAO.save(new Item(dto.getCode(),dto.getDescription(),dto.getPackSize(),dto.getUnitPrice(),dto.getDiscount(),dto.getQtyOnHand()));
    }

    @Override
    public boolean updateItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        return itemDAO.update(new Item(dto.getCode(),dto.getDescription(),dto.getPackSize(),dto.getUnitPrice(),dto.getDiscount(),dto.getQtyOnHand()));
    }

    @Override
    public boolean itemExist(String code) throws SQLException, ClassNotFoundException {
        return itemDAO.exist(code);
    }

    public ItemDto find(String code) throws SQLException, ClassNotFoundException {
        Item item = itemDAO.search(code);
        return new ItemDto(item.getCode(),item.getPackSize(),item.getUnitPrice(),item.getDiscount());
    }

    @Override
    public String generateNewItemCode() throws SQLException, ClassNotFoundException {
        return itemDAO.genarateId();
    }

    public ItemDto search(String s) throws SQLException, ClassNotFoundException {
        Item c = itemDAO.search(s);
        return new ItemDto(c.getCode(),c.getDescription(),c.getPackSize(),c.getUnitPrice(),c.getDiscount(),c.getQtyOnHand());
        //return  CustomerDto.search(s);
    }
}
