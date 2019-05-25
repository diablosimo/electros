/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Commande;
import bean.CommandeItem;
import bean.Electromenager;
import oracle.sql.NUMBER;
import sample.Connexion;
import util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author simob
 */
public class CommandeService {

    public void insert(Long idClient,List<CommandeItem> list) throws SQLException {
        Connection connection = null;
        CallableStatement cs = null;
        CommandeItemService commandeItemService=new CommandeItemService();
        Long idCommande=null;
        try {
            connection= Connexion.getConnection();
            cs = connection.prepareCall("{?=call insertCommande(?)}");
            cs.registerOutParameter(1,Types.NUMERIC);
            cs.setLong(2,idClient);
            cs.execute();
            idCommande=cs.getLong(1);
            commandeItemService.insert(list,idCommande);
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            if (cs != null) cs.close();
            if (connection != null) connection.close();
        }
    }

    public List<Commande> findBycriteria(Long idClient, Date dateCommande,Integer etat) throws SQLException {
        String query="SELECT * FROM Commande WHERE 1=1";
        if(idClient!=null){
            query+= " AND client_ID="+idClient;
        }
        if (dateCommande!=null){
            query+=" AND dateCommande>'"+ DateUtil.formateDate("dd/MM/yyyy",dateCommande)+"'";
        }if(etat!=null){
            query+=" AND etat="+etat;
        }
        System.out.println(query);
        Connection connection = null;
        Statement cs = null;
        ResultSet rs=null;
        Commande commande=null;
        List<Commande> list=new ArrayList<>();
        try {
            connection = Connexion.getConnection();
            cs=connection.createStatement();
            rs=cs.executeQuery(query);
            while(rs.next()){
                commande=new Commande(rs.getLong("ID"),rs.getDate("dateCommande"),
                        rs.getInt("etat"),   rs.getLong("client_ID"));
                list.add(commande);
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
}
