package sample;

import bean.Electromenager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.ElectromenagerService;


import java.io.IOException;
import java.sql.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void forward(ActionEvent actionEvent, String pageName, Class myClass) throws IOException {
        Parent parent = FXMLLoader.load(myClass.getResource(pageName));
        Scene scene = new Scene(parent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(scene);
        app_stage.show();
    }


    public static void main(String[] args) throws SQLException {
        launch(args);
       /* ElectromenagerService electromenagerService= new ElectromenagerService();
        Electromenager electromenager= new Electromenager("FMW250CR2GBK","FRANKEA","CAPACITE : 25, FINITION : NOIR, ENCASTRABLE : OUI", 3200d,2800d,20,"x");
        electromenager.setSousCategorieId(1L);
        electromenager.setFournisseurID(1L);

        String fournisseur="FRANKE";
        String categorie="Micro-ondes";
        //electromenagerService.insert(electromenager,categorie,fournisseur);
        electromenagerService.update(electromenager);
       */ System.out.println("hi");

    }
}
