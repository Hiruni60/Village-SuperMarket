package dao;

import dao.custom.impl.CustomerDaoImpl;
import dao.custom.impl.ItemDaoImpl;
import dao.custom.impl.OrderDetailsDaoImpl;
import dao.custom.impl.OrdersDaoImpl;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    //singleton
    public static DAOFactory getDaoFactory() {
        if (daoFactory == null) {
            daoFactory = new DAOFactory();
        }
        return daoFactory;
    }

    //public final static integer values
    public enum DAOTypes {
        CUSTOMER , ITEM  ,ORDER, ORDERDETAILS // QUERYDAO
    }

    //method for hide the object creation logic and return what client wants
    public SuperDAO getDAO(DAOTypes types) {
        switch (types) {
            case CUSTOMER:
                return new CustomerDaoImpl();
                //SuperDAO superDAO=new CustomerDAOImpl();
            case ITEM:
                return new ItemDaoImpl(); //SuperDAO superDAO=new ItemDAOImpl();
            case ORDER:
                return  new OrdersDaoImpl(); //SuperDAO superDAO = new OrderDAOImpl();
            case ORDERDETAILS:
                return new OrderDetailsDaoImpl(); //SuperDAO superDAO = new OrderDetailsDAOImpl();
            /*case QUERYDAO:
                return new QueryDAOImpl(); //SuperDAO superDAO = new QueryDAOImpl();*/
            default:
                return null;
        }
    }
}
