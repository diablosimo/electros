package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.Main;
import service.ClientService;
import util.Session;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    private Label user=new Label();

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private TextField email;

    @FXML
    private TextField numTel;

    @FXML
    private TextArea adresse;

    @FXML
    private Label statut;

    ClientService clientService=new ClientService();
    @FXML
    void addClient(ActionEvent event)throws SQLException {
        try {
            clientService.insert(nom.getText(),prenom.getText(),email.getText(),numTel.getText(),adresse.getText());
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void clear(ActionEvent event) {

    }

    @FXML
    void gotoAddClientView(ActionEvent event) throws IOException {
        Main.forward(event, "../view/AddClientView.fxml", this.getClass());

    }

    @FXML
    void gotoAddCommandeView(ActionEvent event) throws IOException {
        Main.forward(event, "../view/AddCommandeView.fxml", this.getClass());

    }
    @FXML
    void gotoDashboard(ActionEvent event) throws IOException {
        Main.forward(event, "../view/dashBoardCommercialView.fxml", this.getClass());

    }

    @FXML
    void gotoSearchCommandeView(ActionEvent event) throws IOException {
        Main.forward(event, "../view/SearchCommandeView.fxml", this.getClass());

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user.setText(Session.getConnectedUser());

    }


    @FXML
    void logout(ActionEvent event) throws IOException {
        Session.delete("connectedUser");
        Main.forward(event, "../view/login.fxml", this.getClass());
    }
}
