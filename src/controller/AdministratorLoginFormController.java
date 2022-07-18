package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdministratorLoginFormController {
    public AnchorPane mainPane;
    public ImageView imgHome;
    public ImageView imgManageItem;
    public ImageView imgSystemReport;

    public void HomeOnMousePressed(MouseEvent event) throws IOException {
        setRUI("mainForm");
    }
    private void setRUI(String location) throws IOException {
        Stage stage=(Stage)  mainPane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        //stage.setTitle("Welcome Form");
    }
    public void initialize(URL url, ResourceBundle rb) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), mainPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    public void navigate(MouseEvent event) throws IOException {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            Parent mainPane = null;

            switch (icon.getId()) {
                case "imgSystemReport":
                    mainPane= FXMLLoader.load(this.getClass().getResource("../view/systemReportForm.fxml"));
                    //setRUI("AdministratorLoginForm");
                    break;
                case "imgManageItem":
                    mainPane = FXMLLoader.load(this.getClass().getResource("../view/manageItemForm.fxml"));
                    break;

            }

            if (mainPane != null) {
                Scene subScene = new Scene(mainPane);
                Stage primaryStage = (Stage) this.mainPane.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();

            }
        }
    }

    public void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            switch (icon.getId()) {
                case "imgAdministrator":
                    // lblMenu.setText("Administrator");
                    // lblDescription.setText("Click to manage items and view system reports");
                    break;
                case "imgCashier":
                    // lblMenu.setText("Cashier");
                    // lblDescription.setText("Click to manage orders and place customer order");
                    break;
               /* case "imgOrder":
                    lblMenu.setText("Place Orders");
                    lblDescription.setText("Click here if you want to place a new order");
                    break;
                case "imgViewOrders":
                    lblMenu.setText("Search Orders");
                    lblDescription.setText("Click if you want to search orders");
                    break;*/
            }

            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.CORNFLOWERBLUE);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);
        }
    }

    public void playMouseExitAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);
            //lblMenu.setText("Welcome");
            //lblDescription.setText("Please select one of above who are you....");
        }
    }
}
