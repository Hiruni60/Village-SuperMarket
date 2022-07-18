package controller;

import bo.custom.CustomerBo;
import bo.custom.impl.CustomerBoImpl;
import dto.CustomerDto;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.tdm.CustomerTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class manageCustomerFormController {
    public ImageView imhBack;
    public AnchorPane manageCusPane;
    public Button btnSave;
    public ComboBox cmbCusId;
    public Button btnUpdate;
    public TableView tblLoadCustomer;
    public TableColumn colCusId;
    public TableColumn colCusTitle;
    public TableColumn colCusName;
    public TableColumn colAddress;
    public TableColumn colCity;
    public TableColumn colProvince;
    public TableColumn colPostalcode;




    public TextField txtCusName;
    public TextField txtAddress;
    public TextField txtCity;
    public TextField txtProvince;
    public TextField txtPostalCode;
    public TextField txtPostalCode1;
    public TextField txtProvince1;
    public TextField txtCity1;
    public TextField txtAddress1;
    public TextField txtCusName1;

    public Button btnDelete;
    public ComboBox cmbCusTitle;
    public ComboBox cmbCusTitle1;
    public Label lblCusId;

    CustomerBo customerBo = new CustomerBoImpl();
    //CustomerDao customerDao = new CustomerDaoImpl();

    public void initialize() throws SQLException, ClassNotFoundException {
        cmbCusTitle.getItems().add("Miss");
        cmbCusTitle.getItems().add("Mrs");
        cmbCusTitle.getItems().add("Mr");
        cmbCusTitle1.getItems().add("Miss");
        cmbCusTitle1.getItems().add("Mrs");
        cmbCusTitle1.getItems().add("Mr");
        try {
            loadCustomers();
            loadAllCustomer();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        lblCusId.setText(customerBo.genarateNewCustomerId());
        colCusId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCusTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
        colPostalcode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
    }


    public void BackOnMousePressed(MouseEvent event) throws IOException {
        setRUI("CashierLoginForm");
    }

    private void setRUI(String location) throws IOException {
        Stage stage = (Stage) manageCusPane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"))));
        //stage.setTitle("Welcome Form");
    }

    public void saveOnAction(ActionEvent actionEvent) {
        String id = lblCusId.getText();
        String title = String.valueOf(cmbCusTitle.getValue());
        String name = txtCusName.getText();
        String address = txtAddress.getText();
        String city = txtCity.getText();
        String province = txtProvince.getText();
        String postalCode = txtPostalCode.getText();

        try {
           if(customerBo.saveCustomer(new CustomerDto(id, title, name, address, city, province, postalCode))) {
               new Alert(Alert.AlertType.CONFIRMATION, "SAVE!").show();
               lblCusId.setText(customerBo.genarateNewCustomerId());
               loadCustomers();
               loadAllCustomer();
           }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, "Something Went Wrong!").show();
        }
        clear();
    }

    public void updateOnAction(ActionEvent actionEvent) {
        try {
            if(customerBo.updateCustomer(new CustomerDto(String.valueOf(cmbCusId.getValue()), String.valueOf(cmbCusTitle1.getValue()), txtCusName1.getText(),
                    txtAddress1.getText(), txtCity1.getText(), txtProvince1.getText(), txtPostalCode1.getText()))) {
                new Alert(Alert.AlertType.CONFIRMATION, "UPDATED!").show();
                loadCustomers();
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

    public void cmbCusIdOnAction(ActionEvent actionEvent) {

        try {
            if(cmbCusId.getValue()!=null) {
                CustomerDto search = customerBo.search(String.valueOf(cmbCusId.getValue()));

                cmbCusTitle1.setValue(search.getTitle());
                txtCusName1.setText(search.getName());
                txtAddress1.setText(search.getAddress());
                txtCity1.setText(search.getCity());
                txtProvince1.setText(search.getProvince());
                txtPostalCode1.setText(search.getPostalCode());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // customerBo.search(cmbUCustId.getItems());
    }

    public void loadAllCustomer() throws SQLException, ClassNotFoundException {
        cmbCusId.getItems().clear();
        ArrayList<CustomerDto> arrayList = customerBo.getAllCustomer();
        for (CustomerDto dto : arrayList
        ) {
            cmbCusId.getItems().add(dto.getId());
        }
    }
    private void loadCustomers() throws SQLException, ClassNotFoundException {
        tblLoadCustomer.getItems().clear();
        ArrayList<CustomerDto> arrayList = customerBo.getAllCustomer();
        for (CustomerDto dto : arrayList) {
            tblLoadCustomer.getItems().add(new CustomerTM(dto.getId(), dto.getTitle(), dto.getName(), dto.getAddress(), dto.getCity(), dto.getProvince(),
                    dto.getPostalCode()));
        }
    }

    public void deleteOnAction(ActionEvent actionEvent) {
        try {
           if(customerBo.deleteCustomer(String.valueOf(cmbCusId.getValue()))) {
               new Alert(Alert.AlertType.CONFIRMATION, "DELETED!").show();
               loadCustomers();
               loadAllCustomer();
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
        cmbCusTitle.setValue(null);
        txtCusName.clear();
        txtAddress.clear();
        txtCity.clear();
        txtProvince.clear();
        txtPostalCode.clear();
    }
    public void clearU(){
        cmbCusId.setValue(null);
        cmbCusTitle1.setValue(null);
        txtCusName1.clear();
        txtAddress1.clear();
        txtCity1.clear();
        txtProvince1.clear();
        txtPostalCode1.clear();

    }

    private Object validate() {
        //Pattern idPattern = Pattern.compile("^(E00-)[0-9]{3,5}$");
        Pattern namePartern = Pattern.compile("^[A-Z][A-z]{3,20}$");
        Pattern cityPatern= Pattern.compile("^[A-Z][A-z]{3,20}$");
        Pattern provincePatern= Pattern.compile("^[A-Z][A-z]{3,20}$");
       // Pattern postalCodePatern = Pattern.compile("^[0-9]{5,9}$");


        if (!namePartern.matcher(txtCusName.getText()).matches()) {
            addError(txtCusName);
            return txtCusName;
        } else {
            removeError(txtCusName);
            if(!cityPatern.matcher(txtCity.getText()).matches()){
                addError(txtCity);
                return txtCity;
            }
            else{
                removeError(txtCity);
                if(!provincePatern.matcher(txtProvince.getText()).matches()){
                    addError(txtProvince);
                    return  txtProvince;
                }
                else{
                    removeError(txtProvince);
                    if(txtPostalCode.getText().matches("^[0-9]{8}$")){
                        addError(txtPostalCode);
                        return txtPostalCode;
                    }else{
                        removeError(txtPostalCode);
                    }
                }
            }
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
        Pattern nameUPartern = Pattern.compile("^[A-Z][A-z]{3,20}$");
        Pattern cityUPatern= Pattern.compile("^[A-Z][A-z ]{3,20}$");
        Pattern provinceUPatern= Pattern.compile("^[A-Z][A-z]{3,20}$");
        // Pattern postalCodePatern = Pattern.compile("^[0-9]{5,9}$");


        if (!nameUPartern.matcher(txtCusName1.getText()).matches()) {
            addUError(txtCusName1);
            return txtCusName1;
        } else {
            removeError(txtCusName1);
            if(!cityUPatern.matcher(txtCity1.getText()).matches()){
                addUError(txtCity1);
                return txtCity1;
            }
            else{
                removeError(txtCity1);
                if(!provinceUPatern.matcher(txtProvince1.getText()).matches()){
                    addUError(txtProvince1);
                    return  txtProvince1;
                }
                else{
                    removeError(txtProvince1);
                    if(txtPostalCode1.getText().matches("^[0-9]{8}$")){
                        addUError(txtPostalCode1);
                        return txtPostalCode1;
                    }else{
                        removeUError(txtPostalCode1);
                    }
                }
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
