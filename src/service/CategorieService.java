/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Categorie;
import bean.Electromenager;
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
public class CategorieService {
    public List<Categorie> findAll() throws SQLException {
        List<Categorie> categories=new ArrayList();
        Connection connection = null;
        Statement cs = null;
        ResultSet rs=null;
        String query="SELECT * FROM CATEGORIE";
        try {
            connection = Connexion.getConnection();
            cs=connection.createStatement();
            rs=cs.executeQuery(query);
            while(rs.next()){
                categories.add(new Categorie(rs.getLong(1),rs.getString(2),rs.getString(3)));
            }
            return categories;
        }catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            if (cs != null) cs.close();
            if (connection != null) connection.close();
            return categories;
        }
    }
}
