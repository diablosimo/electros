/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Categorie;
import bean.SousCategorie;
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
public class SousCategorieService {
    public List<SousCategorie> findByCategorieID(Long categorieID) throws SQLException {
        List<SousCategorie> sousCategories=new ArrayList();
        Connection connection = null;
        Statement cs = null;
        ResultSet rs=null;
        String query="SELECT * FROM SOUSCATEGORIE WHERE CATEGORIE_ID="+categorieID;
        try {
            connection = Connexion.getConnection();
            cs=connection.createStatement();
            rs=cs.executeQuery(query);
            while(rs.next()){
                sousCategories.add(new SousCategorie(rs.getLong(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getLong(5)));
            }
            return sousCategories;
        }catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            if (cs != null) cs.close();
            if (connection != null) connection.close();
            return sousCategories;
        }
    }
}
