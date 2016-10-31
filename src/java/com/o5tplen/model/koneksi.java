/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.o5tplen.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Irawan
 */
public class koneksi {
    //com.mysql.
    private static final String dbDriver = "com.mysql.jdbc.Driver";
    private static final String dbName = "jdbc:mysql://localhost:3306/dbaplikasipenjualan";
    private static final String dbUser = "root";
    private static final String dbPassword = "";
    
    private Connection connection;        
    private String pesanKesalahan;

    public String getPesanKesalahan() {
        return pesanKesalahan;
    }
    
    public Connection getConnection(){
        connection = null;        
        pesanKesalahan = "";
        
        try{ 
            Class.forName(dbDriver); 
        } catch (ClassNotFoundException ex){ 
            pesanKesalahan = "JDBC Driver tidak ditemukan atau rusak\n"+ex;
        } 
        
        if (pesanKesalahan.equals("")){ 
            try { 
                connection = DriverManager.getConnection(dbName,dbUser,dbPassword); 
            } catch (SQLException ex) { 
                pesanKesalahan = "Koneksi ke "+dbName+" gagal\n"+ex;
            }
        }
        
        return connection;
    }
}
