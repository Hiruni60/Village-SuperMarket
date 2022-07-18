package controller;

import bo.custom.ItemBo;
import bo.custom.impl.ItemBoImpl;
import dto.CustomerDto;
import dto.ItemDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.tdm.CustomerTM;
import view.tdm.ItemTM;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class manageItemFormController {
    public ImageView imgBack;
    public AnchorPane manageItemPane;

    public TextField txtDescription;
    public TextField txtPackSize;
    public TextField txtQtyOnHand;
    public TextField txtUnitPrice;
    public TextField txtItemDiscount;
    public Button btnSave;
    public Button btnAddNew;
    public TextField txtUnitPrice1;
    public TextField txtItemDiscount1;
    public TextField txtQtyOnHand1;
    public TextField txtDescription1;
    public TextField txtPackSize1;
    public Button btnUpdate;
    public Button btnDelete;
    public ComboBox cmbCode;
    public TableView tblManageItem;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colPackSize;
    public TableColumn colUnitPrice;
    public TableColumn colDiscount;
    public TableColumn colQtyOnHand;
    public Label lblCode;


    ItemBo itemBo = new ItemBoImpl();
    public void initialize() throws SQLException, ClassNotFoundException {
        try {
            loadItems();
            loadAllItem();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        lblCode.setText(itemBo.generateNewItemCode());
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

    }
    public void BackOnMousePressed(MouseEvent event) throws IOException {
        setRUI("AdministratorLoginForm");
    }
    private void setRUI(String location) throws IOException {
        Stage stage=(Stage)  manageItemPane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        //stage.setTitle("Welcome Form");
    }

    public void saveOnAction(ActionEvent actionEvent) {
        String code = lblCode.getText();
        String description = txtDescription.getText();
        String packSize = txtPackSize.getText();
        Double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        Double discount = Double.parseDouble(txtItemDiscount.getText());
        Integer qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        try {
            if(itemBo.saveItem(new ItemDto(code, description, packSize, unitPrice, discount, qtyOnHand))) {
                new Alert(Alert.AlertType.CONFIRMATION, "SAVE!").show();
                lblCode.setText(itemBo.generateNewItemCode());
                loadItems();
                loadAllItem();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, "Something Went Wrong!").show();
        }
        clear();

    }

    public void addnewOnAction(ActionEvent actionEvent) {
        clear();

    }

    public void updateOnAction(ActionEvent actionEvent) {
        try {
            if(itemBo.updateItem(new ItemDto(String.valueOf(cmbCode.getValue()),txtDescription1.getText(), txtPackSize1.getText(),
                    Double.parseDouble(txtUnitPrice1.getText()), Double.parseDouble(txtItemDiscount1.getText()), Integer.parseInt(txtQtyOnHand1.getText())))) {
                new Alert(Alert.AlertType.CONFIRMATION, "UPDATED!").show();
                loadItems();
                //loadAllCustomer();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, "Something Went Wrong!").show();
        }
        clearU();
    }

    public void deleteOnAction(ActionEvent actionEvent) {
        try {
            if(itemBo.deleteItem(String.valueOf(cmbCode.getValue()))) {
                new Alert(Alert.AlertType.CONFIRMATION, "DELETED!").show();
                loadItems();
                loadAllItem();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, "Something Went Wrong!").show();
        }
        clearU();
    }
    public void clear(){
        txtDescription.clear();
        txtPackSize.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        txtItemDiscount.clear();
    }
    public void clearU(){
        cmbCode.setValue(null);
        txtDescription1.clear();
        txtPackSize1.clear();
        txtQtyOnHand1.clear();
        txtUnitPrice1.clear();
        txtItemDiscount1.clear();
    }

    public void cmbCodeOnAction(ActionEvent actionEvent) {
        try {
            if(cmbCode.getValue()!=null) {
                ItemDto search = itemBo.search(String.valueOf(cmbCode.getValue()));

                txtDescription1.setText(search.getDescription());
                txtPackSize1.setText(search.getPackSize());
                txtQtyOnHand1.setText(String.valueOf(search.getQtyOnHand()));
                txtUnitPrice1.setText(String.valueOf(search.getUnitPrice()));
                txtItemDiscount1.setText(String.valueOf(search.getDiscount()));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void loadAllItem() throws SQLException, ClassNotFoundException {
        cmbCode.getItems().clear();
        ArrayList<ItemDto> arrayList = itemBo.getAllItems();
        for (ItemDto dto : arrayList
        ) {
            cmbCode.getItems().add(dto.getCode());
        }
    }
    private void loadItems() throws SQLException, ClassNotFoundException {
        tblManageItem.getItems().clear();
        ArrayList<ItemDto> arrayList = itemBo.getAllItems();
        for (ItemDto dto : arrayList) {
            tblManageItem.getItems().add(new ItemTM(dto.getCode(),dto.getDescription(), dto.getPackSize(),dto.getUnitPrice(),dto.getDiscount(),dto.getQtyOnHand()));
        }
    }

    private Object validate() {
        //Pattern idPattern = Pattern.compile("^(E00-)[0-9]{3,5}$");
        Pattern desPartern = Pattern.compile("^[A-ZA-z]{3,20}$");
        Pattern qtyOnHandPatern= Pattern.compile("^[1-9]{1,20}$");
       // Pattern unitPricePatern= Pattern.compile("^[0-9]{3,20}$");
        // Pattern postalCodePatern = Pattern.compile("^[0-9]{5,9}$");


        if (!desPartern.matcher(txtDescription.getText()).matches()) {
            addError(txtDescription);
            return txtDescription;
        } else {
            removeError(txtDescription);
        }
          if(!qtyOnHandPatern.matcher(txtQtyOnHand.getText()).matches()){
                addError(txtQtyOnHand);
                return txtQtyOnHand;
            }
            else{
                removeError(txtQtyOnHand);

            }


        return true;
    }

    private void addError(TextField txtField) {
        if (txtField.getText().length() > 0) {
            txtField.setStyle("-fx-border-color: red");

        }
        btnSave.setDisable(true);
    }

    private void removeError(TextField txtField) {
        txtField.setStyle("-fx-border-color: green");

        btnSave.setDisable(false);

    }

    private Object validateU() {
        //Pattern idPattern = Pattern.compile("^(E00-)[0-9]{3,5}$");
        Pattern desUPartern = Pattern.compile("^[A-ZA-z]{3,20}$");
        Pattern qtyOnHandUPatern= Pattern.compile("^[1-9]{1,20}$");
        // Pattern unitPricePatern= Pattern.compile("^[0-9]{3,20}$");
        // Pattern postalCodePatern = Pattern.compile("^[0-9]{5,9}$");


        if (!desUPartern.matcher(txtDescription1.getText()).matches()) {
            addUError(txtDescription1);
            return txtDescription1;
        } else {
            removeError(txtDescription1);
            if(!qtyOnHandUPatern.matcher(txtQtyOnHand1.getText()).matches()){
                addUError(txtQtyOnHand1);
                return txtQtyOnHand1;
            }
            else{
                removeUError(txtQtyOnHand1);

            }
        }

        return true;
    }

    private void addUError(TextField txtField) {
        if (txtField.getText().length() > 0) {
            txtField.setStyle("-fx-border-color: red");

        }
        btnUpdate.setDisable(true);
    }

    private void removeUError(TextField txtField) {
        txtField.setStyle("-fx-border-color: green");

        btnUpdate.setDisable(false);

    }


    public void textFields_Key_Released(KeyEvent keyEvent) {
        validate();
       validateU();
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = validate();
            //if the response is a text field
            //that means there is a error
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();// if there is a error just focus it
            } else if (response instanceof Boolean) {

            }

        }
    }
}
