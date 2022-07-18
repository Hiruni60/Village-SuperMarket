package controller;

import bo.BOFactory;
import bo.SuperBo;
import bo.custom.impl.CustomerBoImpl;
import bo.custom.impl.ItemBoImpl;
import bo.custom.impl.placeOrderBoImpl;
import dto.CustomerDto;
import dto.ItemDto;
import dto.OrderDetailsDto;
import dto.OrdersDto;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import view.tdm.cartTM;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class placeOrderFormController {

    public ImageView imgBack;
    public AnchorPane placeOrderPane;
    public Label txtTime;
    public ComboBox cmbCusId;

    public TextField txtDiscount;
    public ComboBox cmbItemCode;

    public TextField txtQty;

    public TextField txtUnitPrice;
    public Text txtOId;
    public Text txtDate;

    public Button btnAddToCart;
    public TableView tblPlaceorder;
    public TableColumn colItemCode;
    public TableColumn colQty;
    public TableColumn colOption;
    public Button btnPlaceOrder;
    public Button btnPrintBill;
    public Label lblDescription;
    public Label lblPackSize;
    public Label lblQtyOnHand;
    public Label lblUnitPrice;
    public Label lblDiscount;
    public Label lblCustomerName;
    public Label lblCusAddress;
    public TextField txtTotalPackSize;
    public TableColumn colPackSize;
    public TableColumn colDiscount;
    public TableColumn colTotalPrice;
    public TableColumn colUnitPrice;


    private final CustomerBoImpl customerBo = (CustomerBoImpl) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);
    private final placeOrderBoImpl placeOrderBo = (placeOrderBoImpl) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PURCHASE_ORDER);
    private final ItemBoImpl itemBo = new ItemBoImpl();
    public TextField txtFinalTotal;


    ArrayList<ItemDto> itemsToChoose = null;

    private ObservableList<cartTM> obList = FXCollections.observableArrayList();


    public void initialize() throws SQLException, ClassNotFoundException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        txtDate.setText(dtf.format(now));

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        final Runnable runnable = new Runnable() {
            public void run() {
                DateTimeFormatter dtft = DateTimeFormatter.ofPattern(" HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                Platform.runLater(()->{txtTime.setText(dtft.format(now));});
            }
        };
        scheduler.scheduleAtFixedRate(runnable, 0, 1, SECONDS);

        loadAllCusIds();
        loadItemIds();
        txtOId.setText(placeOrderBo.generateNewOrderID());

        itemsToChoose = itemBo.getAllItems();

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("totQty"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totPrice"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("delete"));

        tblPlaceorder.setItems(obList);

       /*colOption.setCellValueFactory(param -> {
            Button btnDelete = new Button("Delete");
            btnDelete.setOnAction(event -> {
                tblPlaceorder.getItems().remove(param.getValue());
                tblPlaceorder.getSelectionModel().clearSelection();
                //calculateTotal();
               // enableOrDisablePlaceOrderButton();
            });

            return new ReadOnlyObjectWrapper<>(btnDelete);
        });*/

    }


    public void BackOnMousePressed(MouseEvent event) throws IOException {
        setRUI("CashierLoginForm");
    }
    private void setRUI(String location) throws IOException {
       Stage stage=(Stage)  placeOrderPane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        //stage.setTitle("Welcome Form");
    }

    public void addtocartOnAction(ActionEvent actionEvent) {
        if((Integer.parseInt(lblQtyOnHand.getText())>=Integer.parseInt(txtQty.getText())) & (Integer.parseInt(txtQty.getText())>0) &
                (Double.parseDouble(lblDiscount.getText())>=Double.parseDouble(txtDiscount.getText())) & (Double.parseDouble(txtDiscount.getText()) >0) ) {
            try{
                Double unitPrice = Double.parseDouble(lblUnitPrice.getText());
                int qty = Integer.parseInt(txtQty.getText());
                Double discount = Double.parseDouble(txtDiscount.getText());
                Double total = (unitPrice*qty)-(discount*qty);

                cartTM tm = new cartTM(cmbItemCode.getValue().toString(),Double.parseDouble(lblUnitPrice.getText()),qty,discount,total);

                tm.getDelete().setOnAction(a->{
                      obList.remove(tm);
                     calculateFinalTotal();
                });

                boolean found = false;
                for (cartTM tm1: obList) {
                    // if item exist in cart
                    if(tm1.getItemCode().equals(tm.getItemCode())){
                        found=true;
                        if(!tm.getDiscount().equals(tm1.getDiscount())){
                            new Alert(Alert.AlertType.WARNING,"please check the discount!").showAndWait();
                            return;
                        }
                        else {
                            tm1.setTotQty(tm.getTotQty()+tm1.getTotQty());
                            tm1.setTotPrice(tm.getTotPrice()+tm1.getTotPrice());
                            tblPlaceorder.refresh();
                            break;
                        }
                    }
                }

                if(!found){
                    obList.add(tm);
                }

                // reducing items
                for (ItemDto itemDto: itemsToChoose) {
                    if(itemDto.getCode().equals(tm.getItemCode())){
                        int reduced = itemDto.getQtyOnHand()-tm.getTotQty();
                        itemDto.setQtyOnHand(reduced);
                        lblQtyOnHand.setText(String.valueOf(reduced));
                    }
                }

            }catch (Throwable t){
                throw t;
            }
        }else new Alert(Alert.AlertType.WARNING,"non-computable values!").show();
        calculateFinalTotal();
        txtDiscount.clear();
        txtQty.clear();
        lblDescription.setText("");
        lblPackSize.setText("");
        lblQtyOnHand.setText("");
        lblUnitPrice.setText("");
        lblDiscount.setText("");
        //cmbItemCode.getItems().clear();

    }




    public void placeOrderOnAction(ActionEvent actionEvent){
        if (!obList.isEmpty() & cmbCusId.getSelectionModel().getSelectedIndex() != -1) {
            String oId = txtOId.getText();
            String cId = cmbCusId.getValue().toString();
            OrdersDto orderDTO = new OrdersDto(oId, cId);
            ArrayList<OrderDetailsDto> dtoLst = new ArrayList<>();
            for (cartTM tdm : obList) {
                dtoLst.add(new OrderDetailsDto(oId, tdm.getItemCode(), tdm.getTotQty(), tdm.getDiscount(), tdm.getTotPrice()));
            }
            try {
                if (placeOrderBo.purchaseOrder(orderDTO, dtoLst)) {
                    new Alert(Alert.AlertType.INFORMATION, "order saved successfully!").show();
                    txtOId.setText(placeOrderBo.generateNewOrderID());
                    obList.clear();
                    txtFinalTotal.setText("");
                } else {
                    new Alert(Alert.AlertType.WARNING, "interrupted").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "order cannot be saved \n error : " + e.getMessage());
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "please check whether\n-> Customer is selected\n-> If the cart is empty").show();
        }

       // cmbCusId.getItems().clear();
        lblCustomerName.setText("");
        lblCusAddress.setText("");
        //txtOId.setText(placeOrderBo.generateNewOrderID());

    }

    public void printbillOnAction(ActionEvent actionEvent) {

    }

    private void loadAllCusIds() throws SQLException, ClassNotFoundException {
        ArrayList<CustomerDto> dtos = customerBo.getAllCustomer();
        for (CustomerDto dto : dtos) {
            cmbCusId.getItems().add(dto.getId());
        }
    }



    private void loadItemIds() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDto> dtos = itemBo.getAllItems();
        for (ItemDto dto : dtos) {
            cmbItemCode.getItems().add(dto.getCode());
        }
    }

    public void cmbItemCodeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ItemDto itemDto = itemBo.search(cmbItemCode.getValue().toString());
       /* if(itemDto!=null) {
            ItemDto search = null;
            for (ItemDto dto : itemsToChoose) {
                if(dto.getCode().equals(cmbItemCode.getValue().toString())){
                    search = dto;
                }
            }*/
            lblDescription.setText(itemDto.getDescription());
            lblDiscount.setText(itemDto.getDiscount().toString());
            lblPackSize.setText(itemDto.getPackSize());
            lblQtyOnHand.setText(itemDto.getQtyOnHand().toString());
            lblUnitPrice.setText(itemDto.getUnitPrice().toString());
       // }
    }

    public void cmbCusIdOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        CustomerDto dto = customerBo.search(cmbCusId.getValue().toString());
        lblCustomerName.setText(dto.getName());
        lblCusAddress.setText(dto.getAddress());
    }

   public void calculateFinalTotal() {
       BigDecimal total = new BigDecimal(0);
       for (cartTM tm : obList)
           total = total.add(BigDecimal.valueOf(tm.getTotPrice()));
       {

       }
       txtFinalTotal.setText(String.valueOf(total));
   }



}
