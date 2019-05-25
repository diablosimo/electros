/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Categorie;
import bean.Client;
import sample.Connexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simob
 */
public class ClientService {

    public void insert(String nom,String prenom,String email,String tel,String adresse) throws SQLException {
        Connection connection = null;
        CallableStatement cs = null;
        try {
            connection= Connexion.getConnection();
            cs = connection.prepareCall("{call insertClient(?,?,?,?,?)}");
            cs.setString(1, nom);
            cs.setString(2, prenom);
            cs.setString(3, email);
            cs.setString(4, tel);
            cs.setString(5, adresse);
            cs.execute();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        } finally {
            if (cs != null) cs.close();
            if (connection != null) connection.close();
        }
    }

    public List<Client> findAll() throws SQLException {
        List<Client> clients=new ArrayList();
        Connection connection = null;
        Statement cs = null;
        ResultSet rs=null;
        String query="SELECT * FROM CLIENT";
        try {
            connection = Connexion.getConnection();
            cs=connection.createStatement();
            rs=cs.executeQuery(query);
            while(rs.next()){
                clients.add(new Client(rs.getLong(1),rs.getString(2),
                        rs.getString(3),rs.getString(4),
                        rs.getString(5),rs.getString(6),rs.getInt(7)));
            }
            return clients;
        }catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            if (cs != null) cs.close();
            if (connection != null) connection.close();
            return clients;
        }
    }
}
