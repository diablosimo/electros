package controller;

import bean.Utilisateur;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sample.Main;
import service.UtilisateurService;
import util.Session;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UtilisateurController implements Initializable {

    @FXML
    private Label user=new Label();

    @FXML
    private TextField login;

    @FXML
    private TextField nom;

    @FXML
    private ComboBox<String> statut=new ComboBox<>();

    @FXML
    private TextField prenom;

    @FXML
    private PasswordField  password;

    @FXML
    private RadioButton tous;

    @FXML
    private RadioButton nonActif;

    @FXML
    private ToggleGroup actif;

    @FXML
    private TableView<Utilisateur> tableView=new TableView<Utilisateur>();

    @FXML
    private TableColumn<Utilisateur, String> col_nom=new TableColumn<>();

    @FXML
    private TableColumn<Utilisateur, String> col_prenom=new TableColumn<>();

    @FXML
    private TableColumn<Utilisateur, String> col_login=new TableColumn<>();

    @FXML
    private TableColumn<Utilisateur, String> col_statut=new TableColumn<>();

    Utilisateur utilisateur=new Utilisateur();
    Utilisateur connectedUser=new Utilisateur();
    List<Utilisateur> utilisateurs=new ArrayList<>();
    UtilisateurService utilisateurService=new UtilisateurService();
    Object[] cnx = new Object[]{0, null};

    @FXML
    void seConnecter(ActionEvent event) throws SQLException, IOException {
        Utilisateur utilisateur=new Utilisateur(login.getText(),password.getText());
        cnx=utilisateurService.seConnecter(utilisateur);
        connectedUser=(Utilisateur) cnx[1];
        System.out.println(connectedUser);
        Session.createAtrribute(connectedUser,"connectedUser");
        switch((int)cnx[0]){
            case 1: gotoDashboard(event);break;
            case 2: gotoComDashboard(event);break;
            case 3: gotoCaissierDashboard(event);break;
            case 4: gotoStockDashboard(event);break;
        }
    }

    @FXML
    void addUser(ActionEvent event) throws SQLException {
        utilisateurService.insert(nom.getText(),prenom.getText(),login.getText(),password.getText(),statut.getValue());
    }

    @FXML
    void clear(ActionEvent event) {

    }

    @FXML
    void delete(ActionEvent event) throws SQLException {
        if(utilisateur.getId()!=null)
        utilisateurService.delete(utilisateur.getId());
        tableView.getItems().removeAll(utilisateur);
    }

    @FXML
    void modifyUser(ActionEvent event) throws SQLException {
        utilisateurService.update(nom.getText(),prenom.getText(),login.getText(),password.getText(),statut.getValue());
    }

    @FXML
    void search(ActionEvent event) throws SQLException {
        int x=0;
        if (tous.isSelected())x=1;
        else if(nonActif.isSelected())x=0;
        utilisateurs=utilisateurService.findByCriteria(nom.getText(),prenom.getText(),login.getText(),statut.getValue(),x);
        tableView.setItems(FXCollections.observableArrayList(utilisateurs));
    }


    @FXML
    void getSelected(MouseEvent event) throws SQLException {
        if(tableView.getSelectionModel().getSelectedItem()!=null){
             utilisateur =tableView.getSelectionModel().getSelectedItem();
            nom.setText(utilisateur.getNom());
            prenom.setText(utilisateur.getPrenom());
            login.setText(utilisateur.getLogin());
            statut.setValue(utilisateur.getStatut());
        }
    }

    @FXML
    void gotoDashboard(ActionEvent event) throws IOException {
        Main.forward(event, "../view/dashBoardAdmin.fxml", this.getClass());

    }

    @FXML
    void gotoManageUser(ActionEvent event) throws IOException {
        Main.forward(event, "../view/manageUser.fxml", this.getClass());

    }

    @FXML
    void gotoComDashboard(ActionEvent event) throws IOException {
        Main.forward(event, "../view/dashBoardCommercialView.fxml", this.getClass());
    }
// A AJOUTE
    @FXML
    void gotoCaissierDashboard(ActionEvent event) throws IOException {
        Main.forward(event, "../view/dashBoardCaissierView.fxml", this.getClass());
    }

    @FXML
    void gotoStockDashboard(ActionEvent event) throws IOException {
        Main.forward(event, "../view/dashboardStockManagerView.fxml", this.getClass());
    }



    @FXML
    public void initTable(){
        col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        col_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        col_login.setCellValueFactory(new PropertyValueFactory<>("login"));
        col_statut.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    public void initStatut(){
        statut.setItems(FXCollections.observableArrayList(new String[]{"administrateur","agent commercial","caissier","responsable stock"}));
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user.setText(Session.getConnectedUser());
        initTable();
        initStatut();
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Session.delete("connectedUser");
        Main.forward(event, "../view/login.fxml", this.getClass());
    }
}
