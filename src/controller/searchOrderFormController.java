package controller;

import bo.BOFactory;
import bo.custom.CustomerBo;
import bo.custom.ItemBo;
import bo.custom.OrderDetailsBo;
import bo.custom.impl.placeOrderBoImpl;
import bo.custom.placeOrderBo;
import dto.CustomOrderDto;
import dto.CustomerDto;
import dto.ItemDto;
import dto.OrdersDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.tdm.manageOrderTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class searchOrderFormController {
    public ImageView imgBack;
    public AnchorPane searchOrderPane;
    public TableView<manageOrderTM> tblOrderDetails;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colQty;
    public TableColumn colQtyOnHand;
    public TableColumn colDiscount;
    public TableColumn colTotal;
    public ComboBox cmbOrId;

    public Button btnUpdate;
    public Button btnRemoveItem;

    public Button btnRemoveOrder;
    public Button btnUpdate1;
    public TextField txtQty1;
    public TextField txtDiscount1;
    public TextField txtTotal1;
    public ComboBox cmbCusId;

    placeOrderBo placeorderBO = (placeOrderBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PURCHASE_ORDER);
    OrderDetailsBo orderDetailsBo = (OrderDetailsBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDERDETAILS);
    CustomerBo customerBo = (CustomerBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);
    ItemBo itemBo= (ItemBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);

    public ObservableList<manageOrderTM> itemList = FXCollections.observableArrayList();

    public ObservableList<manageOrderTM> removedItemList = FXCollections.observableArrayList();

    public void initialize() throws SQLException, ClassNotFoundException {
        loadAllCustomers();

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("orderQty"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        tblOrderDetails.setItems(itemList);

    }

    public void BackOnMousePressed(MouseEvent event) throws IOException {
        setRUI("CashierLoginForm");
    }
    private void setRUI(String location) throws IOException {
        Stage stage=(Stage)  searchOrderPane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        //stage.setTitle("Welcome Form");
    }

    public void loadAllCustomers() throws SQLException, ClassNotFoundException {
        cmbCusId.getSelectionModel().clearSelection();
        cmbCusId.getItems().clear();
        ArrayList<CustomerDto> arrayList = customerBo.getAllCustomer();
        for (CustomerDto dto : arrayList
        ) {
            cmbCusId.getItems().add(dto.getId());
        }
    }

    public void updateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        manageOrderTM tdm =  tblOrderDetails.getSelectionModel().getSelectedItem();
        Integer qty = 0;
        Double dis = 0.0;
        if(tdm!=null) {
            try {
                qty = Integer.parseInt(txtQty1.getText());
                dis = Double.parseDouble(txtDiscount1.getText());
            } catch (Throwable f) {
                if (f instanceof NumberFormatException) {
                    new Alert(Alert.AlertType.WARNING, "Please enter a valid amount").showAndWait();
                } else {
                    new Alert(Alert.AlertType.WARNING, f.getMessage()).showAndWait();
                }
                return;
            }
            Double maxDiscount = itemBo.find(tdm.getItemCode()).getDiscount();
            if (dis > maxDiscount | dis < 0) {
                new Alert(Alert.AlertType.WARNING, "Discount has exceeded maximum or invalid!").showAndWait();
                return;
            }
            if (qty > tdm.getQtyOnHand() | qty < 0) {
                new Alert(Alert.AlertType.WARNING, "Quantity has exceeded maximum or uncountable!").showAndWait();
                return;
            }
            for (manageOrderTM item : itemList) {
                if (item.getItemCode().equals(tdm.getItemCode())) {
                    if (qty < item.getOrderQty()) {
                        item.setQtyOnHand(item.getQtyOnHand() + (item.getOrderQty() - qty));
                    } else {
                        item.setQtyOnHand(item.getQtyOnHand() - (qty - item.getOrderQty()));
                    }
                    item.setOrderQty(qty);
                    // updating the total price
                    ItemDto dto = itemBo.find(tdm.getItemCode());
                    // u-price
                    System.out.println(dto.getUnitPrice());
                    item.setTotalPrice((qty * dto.getUnitPrice()) - (dis * qty));
                    item.setDiscount(dis);
                    new Alert(Alert.AlertType.INFORMATION, "updated!").show();
                    break;
                }
            }
            tblOrderDetails.refresh();
        }else new Alert(Alert.AlertType.WARNING,"Please select an item!").show();

        txtDiscount1.clear();
        txtQty1.clear();

    }

    public void removeitemOnAction(ActionEvent actionEvent) {
        manageOrderTM tdm = (manageOrderTM) tblOrderDetails.getSelectionModel().getSelectedItem();
        if(tdm!=null) {
            itemList.remove(tdm);
            removedItemList.add(tdm);
        } else new Alert(Alert.AlertType.WARNING,"Please select an item").show();
    }

    public void removeOrderOnAction(ActionEvent actionEvent) {
        String oid = cmbOrId.getValue().toString();
        if(oid!=null) {
            ArrayList<CustomOrderDto> orderDTOS = new ArrayList<>();
            for (manageOrderTM tdm : itemList) {
                orderDTOS.add(new CustomOrderDto(tdm.getItemCode(), tdm.getOrderQty()));
            }
            try {
                if (placeorderBO.cancelOrder(oid, orderDTOS)) {
                    new Alert(Alert.AlertType.INFORMATION, "order deleted successfully").show();
                    itemList.clear();
                    removedItemList.clear();
                    cmbOrId.getItems().remove(cmbOrId.getSelectionModel().getSelectedItem());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else new Alert(Alert.AlertType.WARNING,"Select an order first!").show();
    }

    public void updateOrderOnAction(ActionEvent actionEvent) {
        String oId = cmbOrId.getValue().toString();
        if(oId!=null) {
            ArrayList<CustomOrderDto> orderDTOS = new ArrayList<>();
            ArrayList<CustomOrderDto> removeOrderDTOS = new ArrayList<>();
            if (!itemList.isEmpty()) {
                for (manageOrderTM tdm : itemList) {
                    orderDTOS.add(new CustomOrderDto(tdm.getItemCode(), tdm.getOrderQty(), tdm.getQtyOnHand(), tdm.getDiscount(), tdm.getTotalPrice()));
                }
            }
            if (!removedItemList.isEmpty()) {
                for (manageOrderTM rmvTDM : removedItemList) {
                    removeOrderDTOS.add(new CustomOrderDto(rmvTDM.getItemCode(), rmvTDM.getOrderQty(), rmvTDM.getQtyOnHand(), rmvTDM.getDiscount(), rmvTDM.getTotalPrice()));
                }
            }
            try {
                if (placeorderBO.updateOrder(orderDTOS, removeOrderDTOS, oId)) {
                    new Alert(Alert.AlertType.INFORMATION, "updated!").show();
                    itemList.clear();
                    removedItemList.clear();
                    cmbOrId.getSelectionModel().clearSelection();
                } else {
                    new Alert(Alert.AlertType.ERROR, "not updated properly! try again....").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.WARNING, "interrupted\nerror : " + e.getMessage()).show();
            }
        }else new Alert(Alert.AlertType.WARNING,"Select an order first!").show();
    }

    public void cusIdOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        cmbOrId.getItems().clear();
        ArrayList <OrdersDto> allOrder = orderDetailsBo.getAllOrdersByCusId(String.valueOf(cmbCusId.getValue()));
        for (OrdersDto order:allOrder
        ) {
            cmbOrId.getItems().add(order.getOrderID());
        }
    }

    public void orderIdOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(!itemList.isEmpty()){
            itemList.clear();
        }
        if(!removedItemList.isEmpty()){
            removedItemList.clear();
        }
        if(!cmbOrId.getSelectionModel().isEmpty()) {
            for (CustomOrderDto dto : placeorderBO.getOrderDetailsFiltered(cmbOrId.getValue().toString())) {
                itemList.add(new manageOrderTM(dto.getItemCode(), dto.getDescription(), dto.getOrderQty(), dto.getQtyOnHand(), dto.getDiscount(), dto.getTotalPrice()));
            }
        }
    }
}
