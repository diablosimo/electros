/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Categorie;
import bean.Fournisseur;
import sample.Connexion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simob
 */
public class FournisseurService {
    public List<Fournisseur> findAll() throws SQLException {
        List<Fournisseur> fournisseurs=new ArrayList();
        Connection connection = null;
        Statement cs = null;
        ResultSet rs=null;
        String query="SELECT * FROM FOURNISSEUR";
        try {
            connection = Connexion.getConnection();
            cs=connection.createStatement();
            rs=cs.executeQuery(query);
            while(rs.next()){
                fournisseurs.add(new Fournisseur(rs.getLong(1),rs.getString(2),rs.getString(3),rs.getString(4)));
            }
            return fournisseurs;
        }catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            if (cs != null) cs.close();
            if (connection != null) connection.close();
            return fournisseurs;
        }
    }
}
