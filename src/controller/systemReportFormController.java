package controller;

import bo.BOFactory;
import bo.custom.ItemBo;
import bo.custom.SystemReportBo;
import bo.custom.placeOrderBo;
import dto.ItemDto;
import dto.OrderDetailsDto;
import dto.ReportDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.tdm.reportTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class systemReportFormController {
    public AnchorPane systemReportPane;
    public ImageView imgBack;
    public TableView <reportTM>tblOrderDetails1;
    public TableColumn colOrderId1;
    public TableColumn colItemCode1;
    public TableColumn colQty1;
    public TableColumn colDiscount1;
    public Label lblDiscount;
    public Label lblQty;
    public Label lblItemCode;
    public Label lblOrderId;
    public Label lblDescription;
    public Label lblPackSize;
    public Label lblQtyOnHand;
    public Label lblUnitPrice;
    public Button btnMostMovable;
    public Button btnLeastMoveble;
    public Label lblTheme;
    public BarChart<String,Number> barChartScale;
    public TextField txtSales;
    public TextField txtTotal;
    public Button btnMonthly;
    public Button btnDaily;
    public Button btnAnnually;

    private final placeOrderBo orderBO = (placeOrderBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PURCHASE_ORDER);

    private static ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    private static ObservableList<XYChart.Series<String,Number>> barChartData = FXCollections.observableArrayList();
    public TextField txtExtra;
    private final ItemBo itemBo= (ItemBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);

    SystemReportBo systemReportBo = (SystemReportBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.SYSTEM_REPORT);

    public void initialize(){
        barChartScale.setData(barChartData);
        barChartData.clear();

        colOrderId1.setCellValueFactory(new PropertyValueFactory<>("oid"));
        colItemCode1.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colQty1.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colDiscount1.setCellValueFactory(new PropertyValueFactory<>("discount"));

        tblOrderDetails1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue !=null){
                lblOrderId.setText(newValue.getOid());
                //txtExtra.setText(newValue.get);
                lblItemCode.setText(newValue.getItemCode());
                lblQty.setText(String.valueOf(newValue.getQty()));
                lblDiscount.setText(String.valueOf(newValue.getDiscount()));
                allItemDetails();
            }
        });

    }

    private void allItemDetails() {
        try {
            ItemDto search = itemBo.search(lblItemCode.getText());
            lblDescription.setText(search.getDescription());
            lblPackSize.setText(search.getPackSize());
            lblQtyOnHand.setText(String.valueOf(search.getQtyOnHand()));
            lblUnitPrice.setText(String.valueOf(search.getUnitPrice()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void generateReport(int n) throws SQLException, ClassNotFoundException {

        int Items = 0;
        double Price = 0.0;

        ArrayList<ReportDto> report =  orderBO.generateReport(n);
        if(!report.isEmpty()){
            pieChartData.clear();
            barChartData.clear();
            XYChart.Series sales = new XYChart.Series<String,Number>();
            sales.setName("sales");
            barChartData.add(sales);
            for (ReportDto dto : report) {
                pieChartData.add(new PieChart.Data(dto.getItemCode(),dto.getTotalItemsSold()));
                barChartData.get(0).getData().add(new XYChart.Data<>(dto.getItemCode(), dto.getTotalEarned()));
                Items+=dto.getTotalItemsSold();
                Price+=dto.getTotalEarned();
            }
        }else new Alert(Alert.AlertType.WARNING,"no records to generate a report!").showAndWait();
        txtTotal.setText(String.valueOf(Items));
        txtSales.setText(String.valueOf(Price));
    }
    public void BackOnMousePressed(MouseEvent event) throws IOException {
        setRUI("AdministratorLoginForm");
    }
    private void setRUI(String location) throws IOException {
        Stage stage=(Stage)  systemReportPane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        //stage.setTitle("Welcome Form");
    }

    public void mostMovebleOnAction(ActionEvent actionEvent) {
        tblOrderDetails1.getItems().clear();
        try{
            ArrayList<OrderDetailsDto> arrayList = systemReportBo.mostMovableItem();
            for (OrderDetailsDto dto:arrayList
            ) {
                tblOrderDetails1.getItems().add(new reportTM(
                        dto.getOrderID(),dto.getItemCode(),dto.getOrderQty(),dto.getDiscount()
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void leastMovebleOnAction(ActionEvent actionEvent) {
        tblOrderDetails1.getItems().clear();
        try{
            ArrayList<OrderDetailsDto> arrayList = systemReportBo.leastMovableItem();
            for (OrderDetailsDto dto:arrayList
            ) {
                tblOrderDetails1.getItems().add(new reportTM(
                        dto.getOrderID(),dto.getItemCode(), dto.getOrderQty(),dto.getDiscount()
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void monthlyOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        generateReport(1);
    }

    public void dailyOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        generateReport(0);
    }

    public void annuallyOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        generateReport(2);
    }
}
