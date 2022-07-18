package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class welcomeFormController {
    public AnchorPane villageContext;
    public ProgressBar MyProgress;

    public void initialize() throws IOException {
        proces();



    }

    private void proces() throws IOException {




        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {
                for (int i = 0; i <= 100; i++) {
                    updateProgress(i, 100);
                    Thread.sleep(50);
                }
                return null;
            }
        };

        MyProgress.progressProperty().bind(task.progressProperty());

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

        MyProgress.progressProperty().addListener((observable, oldValue, newValue) ->{
            if(oldValue!=newValue){
                int presentage = (int)Math.round((Double)newValue*100);
                //Percentage.setText(presentage+"%");

                //set Your task for this
                //if(persentage==100)lblComplete.setText("Complete");
                if(presentage==100){
                    try {
                        setRUI("mainForm");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });



    }

    private void setRUI(String location) throws IOException {
        Stage stage=(Stage)  villageContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        //stage.setTitle("Welcome Form");
    }
}
