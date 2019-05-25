/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Electromenager;
import sample.Connexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simob
 */
public class ElectromenagerService {

    public void insert(Electromenager electromenager,String categorie, String fournisseur) throws SQLException {
        Connection connection = null;
        CallableStatement cs = null;
        try {
            connection= Connexion.getConnection();
            cs = connection.prepareCall("{call insertElectromenager(?,?,?,?,?,?,?,?,?)}");
            cs.setString(1, electromenager.getReference());
            cs.setString(2, electromenager.getLibelle());
            cs.setString(3, electromenager.getDescription());
            cs.setDouble(4, electromenager.getPrixVente());
            cs.setDouble(5, electromenager.getPrixAchat());
            cs.setInt(6, electromenager.getQuantite());
            cs.setString(7, electromenager.getImage());
            cs.setString(8,categorie);
            cs.setString(9, fournisseur);
            cs.execute();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        } finally {
            if (cs != null) cs.close();
            if (connection != null) connection.close();
        }
    }


    public void update(Electromenager electromenager) throws SQLException {
        Connection connection = null;
        CallableStatement cs = null;
        try {
            connection= Connexion.getConnection();
            cs = connection.prepareCall("{call updateElectromenager(?,?,?,?,?,?,?,?,?)}");
            cs.setString(1, electromenager.getReference());
            cs.setString(2, electromenager.getLibelle());
            cs.setString(3, electromenager.getDescription());
            cs.setDouble(4, electromenager.getPrixVente());
            cs.setDouble(5, electromenager.getPrixAchat());
            cs.setInt(6, electromenager.getQuantite());
            cs.setString(7, electromenager.getImage());
            cs.setLong(8,electromenager.getSousCategorieId());
            cs.setLong(9, electromenager.getFournisseurID());
            cs.execute();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            if (cs != null) cs.close();
            if (connection != null) connection.close();
        }
    }

    public List<Electromenager> findByCriteria(String reference,String libelle,String description,
                                               Double prixVenteMin,Double prixVenteMax,
                                               Double prixAchatMin,Double prixAchatMax,int quantiteMin,int quantiteMax,
                                               int existe,Long sousCategorieId,Long fournisseurID) throws SQLException {
        String query="SELECT * FROM Electromenager WHERE 1=1";
        if(reference!=null && !reference.isEmpty())query+=" AND reference='"+reference+"'";
        if(libelle!=null && !libelle.isEmpty()) query+=" AND libelle='"+libelle+"'";
        if(description!=null && !description.isEmpty()) query+=" AND description='%"+description+"%'";
        if(prixVenteMin!=null && prixVenteMax!=null && prixVenteMin!=0 && prixVenteMax!=0) query+=" AND prix_vente>="+prixVenteMin+" AND prix_vente<="+prixVenteMax;
        if(prixAchatMin!=null && prixAchatMax!=null && prixAchatMin!=0 && prixAchatMax!=0) query+=" AND prix_achat>="+prixAchatMin+" AND prix_achat<="+prixAchatMax;
        if(quantiteMax>0 && quantiteMin>0) query+=" AND quantite>="+quantiteMin+" AND quantite<="+quantiteMax;
        if(sousCategorieId!=null) query+=" AND sousCategorie_id="+sousCategorieId;
        if(fournisseurID!=null) query+=" AND fournisseur_id="+fournisseurID;
        if(existe>=0)query+=" AND existe="+existe;
        System.out.println(query);
        Connection connection = null;
        Statement cs = null;
        ResultSet rs=null;
        Electromenager electromenager=null;
        List<Electromenager> list=new ArrayList<>();
        try {
            connection = Connexion.getConnection();
            cs=connection.createStatement();
            rs=cs.executeQuery(query);
            while(rs.next()){
                electromenager=new Electromenager(rs.getLong("ID"),rs.getString("REFERENCE"),
                        rs.getString("LIBELLE"),   rs.getString("DESCRIPTION"),
                        rs.getDouble("PRIX_VENTE"), rs.getDouble("PRIX_ACHAT"),
                        rs.getInt("QUANTITE"),rs.getInt("EXISTE"), rs.getString("IMAGE"),
                        rs.getLong("SOUSCATEGORIE_ID"),rs.getLong("FOURNISSEUR_ID"));
                 list.add(electromenager);
            }
            return list;
        }catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            if (cs != null) cs.close();
            if (connection != null) connection.close();
            return list;
        }
    }



    public  List<Electromenager> findEnAlerte() throws SQLException {
        String query="SELECT E.REFERENCE,E.LIBELLE,E.DESCRIPTION,E.PRIX_VENTE,E.PRIX_ACHAT,E.QUANTITE " +
                "FROM Electromenager E,SOUSCATEGORIE S WHERE E.sousCategorie_id=S.id AND E.quantite<=S.Seuilalert";

        Connection connection = null;
        Statement cs = null;
        ResultSet rs=null;
        Electromenager electromenager=null;
        List<Electromenager> list=new ArrayList<>();
        try {
            connection = Connexion.getConnection();
            cs = connection.createStatement();
            rs = cs.executeQuery(query);
            while(rs.next()){
                electromenager=new Electromenager(rs.getString("REFERENCE"), rs.getString("LIBELLE"),
                        rs.getString("DESCRIPTION"), rs.getDouble("PRIX_VENTE"),
                        rs.getDouble("PRIX_ACHAT"),rs.getInt("QUANTITE"));
                list.add(electromenager);
            }

            return list;
        }catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            if (cs != null) cs.close();
            if (connection != null) connection.close();
            return list;
        }
    }

    public List<Electromenager> findBySousCategorieID(Long id) throws SQLException {
        List<Electromenager> electromenagers=new ArrayList();
        Connection connection = null;
        Statement cs = null;
        ResultSet rs=null;
        String query="SELECT E.* FROM ELECTROMENAGER E,SOUSCATEGORIE S WHERE E.sousCategorie_id=S.id AND E.quantite>S.Seuilalert AND SOUSCATEGORIE_ID="+id;
        try {
            connection = Connexion.getConnection();
            cs=connection.createStatement();
            rs=cs.executeQuery(query);
            while(rs.next()){
                electromenagers.add(new Electromenager(rs.getLong("id"),rs.getString("REFERENCE"), rs.getString("LIBELLE"),
                        rs.getDouble("PRIX_VENTE"),rs.getInt("QUANTITE")));
            }
            return electromenagers;
        }catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            if (cs != null) cs.close();
            if (connection != null) connection.close();
            return electromenagers;
        }
    }
}

