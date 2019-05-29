package controller;

import bean.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sample.Main;
import service.*;
import util.AlertUtil;
import util.DateUtil;
import util.Session;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;



public class CommandeController implements Initializable {


    @FXML
    private Label user=new Label();

    @FXML
    private Label statut;

    @FXML
    private ComboBox<Client> client=new ComboBox<>();

    @FXML
    private ComboBox<Electromenager> electromenager=new ComboBox<>();

    @FXML
    private ComboBox<Categorie> categorie=new ComboBox<>();

    @FXML
    private ComboBox<SousCategorie> sousCategorie=new ComboBox<>();

    @FXML
    private TextField qte;

    @FXML
    private TableView<Lc> tableView=new TableView<Lc>();

    @FXML
    private TableColumn<Lc, String> col_ref=new TableColumn<>();

    @FXML
    private TableColumn<Lc, Integer> col_qte=new TableColumn<>();

    @FXML
    private TableColumn<Lc, Double> col_prix=new TableColumn<>();

    @FXML
    private Label montantTotal;
    ////////////////////////////////////////////////////////////////////////////////
    @FXML
    private DatePicker dateCommande;

    @FXML
    private RadioButton isFacture;

    @FXML
    private RadioButton nonFacture;

    @FXML
    private ToggleGroup state;

    @FXML
    private TableView<Commande> tableViewCommande=new TableView<Commande>();

    @FXML
    private TableColumn<Commande, Long> col_com_id=new TableColumn<>();

    @FXML
    private TableColumn<Commande, Date> col_date=new TableColumn<>();

    @FXML
    private TableColumn<Commande, String> col_etat=new TableColumn<>();

    @FXML
    private TableView<Lc> tableViewLC=new TableView<Lc>();




    private Commande commande=new Commande();
    private List<CommandeItem> commandeItems=new ArrayList();
    private List<Lc> lcs=new ArrayList();
    private List<Commande>commandes=new ArrayList<>();

    public ClientService clientService=new ClientService();
    public CategorieService categorieService=new CategorieService();
    public SousCategorieService sousCategorieService=new SousCategorieService();
    public ElectromenagerService electromenagerService=new ElectromenagerService();
    public CommandeService commandeService=new CommandeService();
    public CommandeItemService commandeItemService=new CommandeItemService();


    @FXML
    void findCommande(ActionEvent event) throws SQLException {
        Long idClient;
        Date dateC;
        Integer etat=null;
        idClient=client.getValue()==null?null:client.getValue().getId();
        dateC=dateCommande.getValue()==null?null:java.sql.Date.valueOf(dateCommande.getValue());
        if(isFacture.isSelected()) etat=1;
        else if(nonFacture.isSelected())etat=0;
        System.out.println(dateC);
        commandes=commandeService.findBycriteria(idClient,dateC,etat);
        tableViewCommande.setItems(FXCollections.observableArrayList(commandes));
        System.out.println(tableViewCommande.getItems().size());
        tableViewLC.setItems(null);
    }

    @FXML
    void addLigneCommande(ActionEvent event) {
        System.out.println("a");
        if(electromenager.getValue()!=null  && qte.getText()!=null){
            System.out.println("b");
            if(electromenager.getValue().getQuantite()>new Integer(qte.getText())){
                System.out.println("c");
                addLigneCommandeToList(new CommandeItem(getParams().getQuantite(),electromenager.getValue().getId(),null));
            }
        }
    }

    void addLigneCommandeToList(CommandeItem commandeItem){
        System.out.println(commandeItem.getElectromenagerID()+commandeItem.getQuantite());
        int i=0;
        for (CommandeItem c:commandeItems) {
            System.out.println("e");
            System.out.println(c.getElectromenagerID()+"="+commandeItem.getElectromenagerID());
            if(c.getElectromenagerID()!=commandeItem.getElectromenagerID()) continue;
            if (c.getElectromenagerID()==commandeItem.getElectromenagerID()){
                System.out.println(i);
                i=1;
                break;
            }break;

        }
        if(i==0){
            System.out.println("f");
            commandeItems.add(commandeItem);
            lcs.add(getParams());
            tableView.setItems(FXCollections.observableArrayList(lcs));
            System.out.println(montantTotal.getText());
            System.out.println(new Double(montantTotal.getText()));
            System.out.println(getParams().getPrix());
            System.out.println(new Double(montantTotal.getText())+getParams().getPrix()+"");
            montantTotal.setText(new Double(montantTotal.getText())+getParams().getPrix()+"");
        }else{
            System.out.println("g");
            AlertUtil.showAddError("produit existant dans la commande");
        }
    }



    @FXML
    void findCommandeItems(MouseEvent event) throws SQLException {
        System.out.println("hani");
        if(tableViewCommande.getSelectionModel().getSelectedItem()!=null){
            Long idCommande=tableViewCommande.getSelectionModel().getSelectedItem().getId();
            tableViewLC.setItems(FXCollections.observableArrayList(commandeItemService.findByCommandeID(idCommande)));
        }
    }

    @FXML
    void deleteCommande(ActionEvent event) throws SQLException,Exception {
        try{
            Commande selectedCommande=tableViewCommande.getSelectionModel().getSelectedItem();
            commandeService.deleteCommande(selectedCommande);
            commandeItems.clear();
            commandes.clear();
            tableViewCommande.setItems(null);
            tableViewLC.setItems(null);
            findCommande(event);
        }catch(SQLException e){
            AlertUtil.showDeleteError(e.getMessage());
        }catch(NullPointerException e){
            AlertUtil.showDeleteError("aucune commande n'est selectionnée");
        }

    }


    void loadClient() throws SQLException {
        client.setItems(FXCollections.observableArrayList(clientService.findAll()));
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
    void loadElectromenager(ActionEvent event) throws SQLException {
        electromenager.setItems(FXCollections.observableArrayList(electromenagerService.findBySousCategorieID(sousCategorie.getSelectionModel().getSelectedItem().getId())));
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

    @FXML
     void saveCommande(ActionEvent event) throws SQLException {
        if(commandeItems!=null && !commandeItems.isEmpty()&& client!=null && client.getValue()!=null){
            for(CommandeItem i:commandeItems){
                System.out.println(i.getElectromenagerID()+" "+i.getQuantite());
            }
            try{
                commandeService.insert(client.getValue().getId(),commandeItems);
                AlertUtil.showAddAlert();
            }catch (Exception e){
                AlertUtil.showAddError(e.getMessage());
                System.out.println(e.getMessage());
            }

        }
    }
    @FXML
    void clear1(ActionEvent event){
        tableView.setItems(null);
        lcs=new ArrayList();
        commandeItems=new ArrayList();

    }

    public Lc getParams(){
       return new Lc(electromenager.getValue().getReference(),new Integer(qte.getText()),new Double(electromenager.getValue().getPrixVente()*new Integer(qte.getText())));
    }

    @FXML
    public void initTable(){
        col_ref.setCellValueFactory(new PropertyValueFactory<>("reference"));
        col_qte.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        col_prix.setCellValueFactory(new PropertyValueFactory<>("prix"));

        col_etat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        col_com_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user.setText(Session.getConnectedUser());
        try {
            loadClient();
            loadCategorie();
            initTable();
            montantTotal=new Label();
            montantTotal.setText("0");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Label getUser() {
        return user;
    }

    public void setUser(Label user) {
        this.user = user;
    }

    public Label getStatut() {
        return statut;
    }

    public void setStatut(Label statut) {
        this.statut = statut;
    }

    public ComboBox<Client> getClient() {
        return client;
    }

    public void setClient(ComboBox<Client> client) {
        this.client = client;
    }

    public ComboBox<Electromenager> getElectromenager() {
        return electromenager;
    }

    public void setElectromenager(ComboBox<Electromenager> electromenager) {
        this.electromenager = electromenager;
    }

    public ComboBox<Categorie> getCategorie() {
        return categorie;
    }

    public void setCategorie(ComboBox<Categorie> categorie) {
        this.categorie = categorie;
    }

    public ComboBox<SousCategorie> getSousCategorie() {
        return sousCategorie;
    }

    public void setSousCategorie(ComboBox<SousCategorie> sousCategorie) {
        this.sousCategorie = sousCategorie;
    }

    public TextField getQte() {
        return qte;
    }

    public void setQte(TextField qte) {
        this.qte = qte;
    }

    public TableView<Lc> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<Lc> tableView) {
        this.tableView = tableView;
    }

    public TableColumn<Lc, String> getCol_ref() {
        return col_ref;
    }

    public void setCol_ref(TableColumn<Lc, String> col_ref) {
        this.col_ref = col_ref;
    }

    public TableColumn<Lc, Integer> getCol_qte() {
        return col_qte;
    }

    public void setCol_qte(TableColumn<Lc, Integer> col_qte) {
        this.col_qte = col_qte;
    }

    public TableColumn<Lc, Double> getCol_prix() {
        return col_prix;
    }

    public void setCol_prix(TableColumn<Lc, Double> col_prix) {
        this.col_prix = col_prix;
    }

    public Label getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Label montantTotal) {
        this.montantTotal = montantTotal;
    }

    public List<CommandeItem> getCommandeItems() {
        return commandeItems;
    }

    public void setCommandeItems(List<CommandeItem> commandeItems) {
        this.commandeItems = commandeItems;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public DatePicker getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(DatePicker dateCommande) {
        this.dateCommande = dateCommande;
    }

    public RadioButton getIsFacture() {
        return isFacture;
    }

    public void setIsFacture(RadioButton isFacture) {
        this.isFacture = isFacture;
    }

    public RadioButton getNonFacture() {
        return nonFacture;
    }

    public void setNonFacture(RadioButton nonFacture) {
        this.nonFacture = nonFacture;
    }

    public ToggleGroup getState() {
        return state;
    }

    public void setState(ToggleGroup state) {
        this.state = state;
    }

    public TableView<Commande> getTableViewCommande() {
        return tableViewCommande;
    }

    public void setTableViewCommande(TableView<Commande> tableViewCommande) {
        this.tableViewCommande = tableViewCommande;
    }

    public TableColumn<Commande, Long> getCol_com_id() {
        return col_com_id;
    }

    public void setCol_com_id(TableColumn<Commande, Long> col_com_id) {
        this.col_com_id = col_com_id;
    }

    public TableColumn<Commande, Date> getCol_date() {
        return col_date;
    }

    public void setCol_date(TableColumn<Commande, Date> col_date) {
        this.col_date = col_date;
    }

    public TableColumn<Commande, String> getCol_etat() {
        return col_etat;
    }

    public void setCol_etat(TableColumn<Commande, String> col_etat) {
        this.col_etat = col_etat;
    }

    public TableView<Lc> getTableViewLC() {
        return tableViewLC;
    }

    public void setTableViewLC(TableView<Lc> tableViewLC) {
        this.tableViewLC = tableViewLC;
    }


    @FXML
    void logout(ActionEvent event) throws IOException {
        Session.delete("connectedUser");
        Main.forward(event, "../view/login.fxml", this.getClass());
    }
}
