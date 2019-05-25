/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Commande;
import bean.Electromenager;
import bean.Lc;
import bean.Utilisateur;
import sample.Connexion;
import util.HashageUtil;
import util.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simob
 */
public class UtilisateurService {
    public Object[] seConnecter(Utilisateur utilisateur) throws SQLException {
        if(utilisateur==null) return new Object[]{-1,null};
        else if (utilisateur.getLogin()==null) return new Object[]{-2,null};
        else if (utilisateur.getPassword()==null) return new Object[]{-3,null};
        else{
            int statut=0;
            Utilisateur bdUser=findByLogin(utilisateur.getLogin());
            if(bdUser==null) return new Object[]{-4,null};
            else if (!HashageUtil.sha256(utilisateur.getPassword()).equals(bdUser.getPassword())) return new Object[]{-5,null};
            else {
                bdUser.setPassword(null);
                switch (bdUser.getStatut()){
                    case "administrateur":statut=1;break;
                    case "agent commercial":statut=2;break;
                    case "caissier":statut=3;break;
                    case "responsable stock":statut=4;break;
                }
                return new Object[]{statut,bdUser};
            }
        }
    }

    public Utilisateur findByLogin(String login) throws SQLException {
        String query="SELECT * FROM UTILISATEUR WHERE LOGIN='"+login+"'";
        System.out.println(query);
        Connection connection = null;
        Statement cs = null;
        ResultSet rs=null;
        Utilisateur utilisateur=null;
        try {
            connection = Connexion.getConnection();
            cs=connection.createStatement();
            rs=cs.executeQuery(query);
            while(rs.next()){
                utilisateur=new Utilisateur(rs.getLong("ID"), rs.getString("NOM"), rs.getString("PRENOM"), rs.getString("LOGIN"), rs.getString("PASSWORD"), rs.getString("STATUT"));
            }
            return utilisateur;
        }catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            if (cs != null) cs.close();
            if (connection != null) connection.close();
            return utilisateur;
        }

    }

    public void insert(String nom,String prenom,String login,String password,String statut) throws SQLException {
        Connection connection = null;
        CallableStatement cs = null;
        try {
            connection= Connexion.getConnection();
            cs = connection.prepareCall("{call insertUtilisateur(?,?,?,?,?)}");
            cs.setString(1, nom);
            cs.setString(2, prenom);
            cs.setString(3, login);
            cs.setString(4, HashageUtil.sha256(password));
            cs.setString(5, statut);
            cs.execute();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        } finally {
            if (cs != null) cs.close();
            if (connection != null) connection.close();
        }
    }

    public void update(String nom,String prenom,String login,String password,String statut) throws SQLException {
        Connection connection = null;
        CallableStatement cs = null;
        try {
            connection= Connexion.getConnection();
            cs = connection.prepareCall("{call updateUtilisateur(?,?,?,?,?)}");
            cs.setString(1, nom);
            cs.setString(2, prenom);
            cs.setString(3, login);
            cs.setString(4, HashageUtil.sha256(password));
            cs.setString(5, statut);
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

    public List<Utilisateur> findByCriteria(String nom,String prenom,String login,String statut,int actif) throws SQLException {
        String query;
        if(actif==0)query="SELECT * FROM UTILISATEUR WHERE ID NOT IN (SELECT e.id FROM UTILISATEUR e,FACTURE f WHERE e.ID=f.UTILISATEUR_ID)";
        else query="SELECT * FROM UTILISATEUR WHERE 1=1";
        if(nom!=null && !nom.isEmpty()) query+=" AND NOM='"+nom+"'";
        if(prenom!=null && !prenom.isEmpty()) query+=" AND prenom='"+prenom+"'";
        if(login!=null && !login.isEmpty()) query+=" AND login='"+login+"'";
        if(statut!=null && !statut.isEmpty()) query+=" AND statut='"+statut+"'";

        System.out.println(query);
        Connection connection = null;
        Statement cs = null;
        ResultSet rs=null;
        Utilisateur utilisateur=null;
        List<Utilisateur> list=new ArrayList<>();
        try {
            connection = Connexion.getConnection();
            cs=connection.createStatement();
            rs=cs.executeQuery(query);
            while(rs.next()){
                utilisateur=new Utilisateur(rs.getLong("ID"),rs.getString("NOM"),
                        rs.getString("PRENOM"), rs.getString("LOGIN"),
                        null,rs.getString("STATUT"));
                list.add(utilisateur);
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

    public void delete(Long id) throws SQLException {
        Connection connection = null;
        CallableStatement cs = null;
        try {
            connection= Connexion.getConnection();
            cs = connection.prepareCall("{call deleteUtilisateur(?)}");
            cs.setLong(1, id);
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



}
