/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Commande;
import bean.CommandeItem;
import bean.Lc;
import sample.Connexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author simob
 */
public class CommandeItemService {
    public void insert(List<CommandeItem> items,Long idCommande) throws SQLException {
        Connection connection = null;
        CallableStatement cs = null;
        try {
            connection= Connexion.getConnection();
            cs = connection.prepareCall("{call insertCommandeItem(?,?,?)}");
            for (CommandeItem i:items) {
                cs.setInt(1, i.getQuantite());
                System.out.println(idCommande);
                cs.setLong(2, idCommande);
                System.out.println( i.getElectromenagerID());
                cs.setLong(3, i.getElectromenagerID());
                cs.execute();
            }
        } catch (Exception e) {
            System.err.println("Got an exception in insert commandeItem! ");
            System.err.println(e.getMessage());
        } finally {
            if (cs != null) cs.close();
            if (connection != null) connection.close();
        }
    }

    public List<Lc> findByCommandeID(Long idCommande) throws SQLException {
        String query="SELECT e.REFERENCE,ci.QUANTITE FROM COMMANDEITEM ci,ELECTROMENAGER e WHERE ci.ELECTROMENAGER_ID=e.ID AND COMMANDE_ID="+idCommande;
        System.out.println(query);
        Connection connection = null;
        Statement cs = null;
        ResultSet rs=null;
        Lc lc=null;
        List<Lc> list=new ArrayList<>();
        try {
            connection = Connexion.getConnection();
            cs=connection.createStatement();
            rs=cs.executeQuery(query);
            while(rs.next()){
                lc=new Lc(rs.getString(1),rs.getInt(2),0d);

                list.add(lc);
            }
            System.out.println("hani f commandeItemService: "+list.toString());
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
}
