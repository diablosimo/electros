package controller;

import bean.Categorie;
import bean.Electromenager;
import bean.Fournisseur;
import bean.SousCategorie;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Main;
import service.CategorieService;
import service.ElectromenagerService;
import service.FournisseurService;
import service.SousCategorieService;
import util.Session;

import javax.print.attribute.standard.DialogTypeSelection;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class dashboardStockManagerController implements Initializable {

    @FXML
    private Label user=new Label();

    @FXML
    private TextField reference;

    @FXML
    private TextField libelle;

    @FXML
    private TextField pVente;

    @FXML
    private TextField pAchat;

    @FXML
    private TextField qte;

    @FXML
    private TextArea description;

    @FXML
    private ImageView img;

    @FXML
    private ComboBox<Fournisseur> fournisseur=new ComboBox<Fournisseur>();

    @FXML
    private ComboBox<Categorie> categorie=new ComboBox<Categorie>();

    @FXML
    private ComboBox<SousCategorie> sousCategorie=new ComboBox<SousCategorie>();

    @FXML
    private Label statut=new Label();

    @FXML
    private TextField pVenteMin;

    @FXML
    private TextField pVenteMax;

    @FXML
    private TextField pAchatMin;

    @FXML
    private TextField pAchatMax;
    @FXML
    private TextField qteMin;
    @FXML
    private TextField qteMax;

    ////////////////////////////////////////////////////////////
    @FXML
    private TableView<Electromenager > tableView=new TableView<Electromenager>();
    @FXML
    private TableColumn<Electromenager, String> col_ref=new TableColumn<>();
    @FXML
    private TableColumn<Electromenager, String> col_lib=new TableColumn<>();
    @FXML
    private TableColumn<Electromenager, String> col_pv=new TableColumn<>();
    @FXML
    private TableColumn<Electromenager, String> col_pa=new TableColumn<>();
    @FXML
    private TableColumn<Electromenager, String> col_qte=new TableColumn<>();
    @FXML
    private TableColumn<Electromenager, String> col_desc=new TableColumn<>();
    private List<Electromenager> list=new ArrayList();



    public void initTable(){
        col_ref.setCellValueFactory(new PropertyValueFactory<>("reference"));
        col_lib.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        col_pv.setCellValueFactory(new PropertyValueFactory<>("prixVente"));
        col_pa.setCellValueFactory(new PropertyValueFactory<>("prixAchat"));
        col_qte.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        col_desc.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    ///////////////////////////////////////////////////////


    private CategorieService categorieService=new CategorieService();
    private SousCategorieService sousCategorieService= new SousCategorieService();
    private FournisseurService fournisseurService=new FournisseurService();
    private ElectromenagerService electromenagerService=new ElectromenagerService();


    public Electromenager getParams(){
        return new Electromenager(reference.getText(),libelle.getText(),description.getText(),new Double(pVente.getText()),new Double(pAchat.getText()),new Integer(qte.getText()),"X");
    }

    @FXML
    void addElectromenager(ActionEvent event) throws SQLException,Exception {
        try {
            electromenagerService.insert(getParams(), sousCategorie.getValue().getNom(), fournisseur.getValue().getNom());
            statut.setText("success");
        }catch (Exception e){
            statut.setText("error");
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void clear(ActionEvent event) {

    }
    @FXML
    void findByCriteria(ActionEvent event) throws SQLException {
        Long idCat,idF;
        if (sousCategorie.getValue() == null) {
            idCat=null;
        }else idCat=sousCategorie.getValue().getId();
        if(fournisseur.getValue()==null){
            idF=null;
        }else idF=fournisseur.getValue().getId();

        list=electromenagerService.findByCriteria(reference.getText(),libelle.getText(),description.getText(),
                new Double(zeroIfEmpty(pVenteMin.getText())),new Double(zeroIfEmpty(pVenteMax.getText())),
                new Double(zeroIfEmpty(pAchatMin.getText())),new Double(zeroIfEmpty(pAchatMax.getText())),new Integer(zeroIfEmpty(qteMin.getText())),
                new Integer(zeroIfEmpty(qteMax.getText())),-1,idCat,idF);
        tableView.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    void loadSousCategorie(ActionEvent event) throws SQLException {
        System.out.println(sousCategorieService.findByCategorieID(categorie.getSelectionModel().getSelectedItem().getId()));
        sousCategorie.setItems(FXCollections.observableArrayList(sousCategorieService.findByCategorieID(categorie.getSelectionModel().getSelectedItem().getId())));
        System.out.println(sousCategorie.getItems().size());
    }

    @FXML
    void loadCategorie() throws SQLException {
        categorie.setItems(FXCollections.observableArrayList(categorieService.findAll()));
    }

    @FXML
    void loadFournisseur() throws SQLException {
        fournisseur.setItems(FXCollections.observableArrayList(fournisseurService.findAll()));
    }



    @FXML
    public void updateElectromenager(ActionEvent event) throws SQLException {
       try {
           Electromenager electromenager=getParams();
           electromenager.setFournisseurID(fournisseur.getValue().getId());
           electromenager.setSousCategorieId(sousCategorie.getValue().getId());
           electromenagerService.update(electromenager);
           statut.setText("success");
       }catch(Exception e){
            System.out.println(e.getMessage());
            statut.setText("error");
        }
    }

    @FXML
    void uploadImage(ActionEvent event) {

    }


//////////////////////////////////////:

    @FXML
    void gotoAddElectromenagerView(ActionEvent event) throws IOException {
        Main.forward(event, "../view/addElectromenagerView.fxml", this.getClass());
    }

    @FXML
    void gotoDashboard(ActionEvent event) throws IOException {
        Main.forward(event, "../view/dashboardStockManagerView.fxml", this.getClass());

    }

    @FXML
    void gotoListReptureView(ActionEvent event) throws IOException, SQLException {
        tableView.setItems(FXCollections.observableArrayList(electromenagerService.findEnAlerte()));
        Main.forward(event, "../view/ListReptureView.fxml", this.getClass());

    }

    @FXML
    void gotoSearchElectromenagerView(ActionEvent event) throws IOException {
        Main.forward(event, "../view/SearchElectromenagerView.fxml", this.getClass());

    }

    ////////////////////////////////////////:

    public TextField getReference() {
        return reference;
    }

    public void setReference(TextField reference) {
        this.reference = reference;
    }

    public TextField getLibelle() {
        return libelle;
    }

    public void setLibelle(TextField libelle) {
        this.libelle = libelle;
    }

    public TextField getpVente() {
        return pVente;
    }

    public void setpVente(TextField pVente) {
        this.pVente = pVente;
    }

    public TextField getpAchat() {
        return pAchat;
    }

    public void setpAchat(TextField pAchat) {
        this.pAchat = pAchat;
    }

    public TextField getQte() {
        return qte;
    }

    public void setQte(TextField qte) {
        this.qte = qte;
    }

    public TextArea getDescription() {
        return description;
    }

    public void setDescription(TextArea description) {
        this.description = description;
    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public ComboBox<?> getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(ComboBox<Fournisseur> fournisseur) {
        this.fournisseur = fournisseur;
    }

    public ComboBox<?> getCategorie() {
        return categorie;
    }

    public void setCategorie(ComboBox<Categorie> categorie) {
        this.categorie = categorie;
    }

    public Label getUser() {
        return user;
    }

    public void setUser(Label user) {
        this.user = user;
    }

    public ComboBox<?> getSousCategorie() {
        return sousCategorie;
    }

    public void setSousCategorie(ComboBox<SousCategorie> sousCategorie) {
        this.sousCategorie = sousCategorie;
    }

    public Label getStatut() {
        return statut;
    }

    public void setStatut(Label statut) {
        this.statut = statut;
    }

    public TableView<Electromenager> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<Electromenager> tableView) {
        this.tableView = tableView;
    }

    public TableColumn<Electromenager, String> getCol_ref() {
        return col_ref;
    }

    public void setCol_ref(TableColumn<Electromenager, String> col_ref) {
        this.col_ref = col_ref;
    }

    public TableColumn<Electromenager, String> getCol_lib() {
        return col_lib;
    }

    public void setCol_lib(TableColumn<Electromenager, String> col_lib) {
        this.col_lib = col_lib;
    }

    public TableColumn<Electromenager, String> getCol_pv() {
        return col_pv;
    }

    public void setCol_pv(TableColumn<Electromenager, String> col_pv) {
        this.col_pv = col_pv;
    }

    public TableColumn<Electromenager, String> getCol_pa() {
        return col_pa;
    }

    public void setCol_pa(TableColumn<Electromenager, String> col_pa) {
        this.col_pa = col_pa;
    }

    public TableColumn<Electromenager, String> getCol_qte() {
        return col_qte;
    }

    public void setCol_qte(TableColumn<Electromenager, String> col_qte) {
        this.col_qte = col_qte;
    }

    public TableColumn<Electromenager, String> getCol_desc() {
        return col_desc;
    }

    public void setCol_desc(TableColumn<Electromenager, String> col_desc) {
        this.col_desc = col_desc;
    }

    public List<Electromenager> getList() {
        return list;
    }

    public void setList(List<Electromenager> list) {
        this.list = list;
    }
private String zeroIfEmpty(String s){
        if (s.isEmpty() ||s==null){
            return "0";
        }else
            return s;
}
    public TextField getpVenteMin() {

        return pVenteMin;
    }

    public void setpVenteMin(TextField pVenteMin) {
        this.pVenteMin = pVenteMin;
    }

    public TextField getpVenteMax() {
        return pVenteMax;
    }

    public void setpVenteMax(TextField pVenteMax) {
        this.pVenteMax = pVenteMax;
    }

    public TextField getpAchatMin() {
        return pAchatMin;
    }

    public void setpAchatMin(TextField pAchatMin) {
        this.pAchatMin = pAchatMin;
    }

    public TextField getpAchatMax() {
        return pAchatMax;
    }

    public void setpAchatMax(TextField pAchatMax) {
        this.pAchatMax = pAchatMax;
    }

    public TextField getQteMin() {
        return qteMin;
    }

    public void setQteMin(TextField qteMin) {
        this.qteMin = qteMin;
    }

    public TextField getQteMax() {
        return qteMax;
    }

    public void setQteMax(TextField qteMax) {
        this.qteMax = qteMax;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user.setText(Session.getConnectedUser());
        try {
            loadCategorie();
            loadFournisseur();
            initTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Session.delete("connectedUser");
        Main.forward(event, "../view/login.fxml", this.getClass());
    }
}
