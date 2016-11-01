/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.o5tplen.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.o5tplen.view.PesanDialog;

/**
 *
 * @author Irawan
 */

public class DataCustomer {
private String namaCustomer,alamatCustomer,telpCustomer;
private String pesan;
private int idCustomer;
private Object [][] list;
private final koneksi conn=new koneksi();
private final PesanDialog pesanDialog=new PesanDialog();

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getNamaCustomer() {
        return namaCustomer;
    }

    public void setNamaCustomer(String namaCustomer) {
        this.namaCustomer = namaCustomer;
    }

    public String getAlamatCustomer() {
        return alamatCustomer;
    }

    public void setAlamatCustomer(String alamatCustomer) {
        this.alamatCustomer = alamatCustomer;
    }

    public String getTelpCustomer() {
        return telpCustomer;
    }

    public void setTelpCustomer(String telpCustomer) {
        this.telpCustomer = telpCustomer;
    }

  
    public String getPesan() {
        return pesan;
    }

    public Object[][] getList() {
        return list;
    }

    public void setList(Object[][] list) {
        this.list = list;
    }



    public boolean baca(String namaCustomer){
        boolean adaKesalahan = false;
        Connection connection;
        
        if ((connection = conn.getConnection()) != null){
            Statement sta;
            ResultSet rset;
            
            try {
                String SQLStatemen = "select * from customer where nama='"+namaCustomer+"'";
                sta = connection.createStatement();
                rset = sta.executeQuery(SQLStatemen);
                
                rset.next();
                if (rset.getRow()>0){
                    this.idCustomer=rset.getInt("id");
                    this.namaCustomer = rset.getString("nama");
                    this.alamatCustomer = rset.getString("alamat");
                    this.telpCustomer=rset.getString("telp");
                } else {
                    adaKesalahan = true;
                    pesan = "Nama Customer \""+namaCustomer+"\" tidak ditemukan";
                }
                
                sta.close();
                rset.close();
                connection.close();
            } catch (SQLException ex){
                adaKesalahan = true; 
                pesan = "Tidak dapat membuka tabel customer\n"+ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n"+conn.getPesanKesalahan();
        }
        
        return !adaKesalahan;
    }

    public boolean bacaId(int idCustomer){
        boolean adaKesalahan = false;
        Connection connection;
        
        if ((connection = conn.getConnection()) != null){
            Statement sta;
            ResultSet rset;
            
            try {
                String SQLStatemen = "select * from customer where id='"+idCustomer+"'";
                sta = connection.createStatement();
                rset = sta.executeQuery(SQLStatemen);
                
                rset.next();
                if (rset.getRow()>0){
                    this.idCustomer=rset.getInt("id");
                    this.namaCustomer = rset.getString("nama");
                    this.alamatCustomer = rset.getString("alamat");
                    this.telpCustomer=rset.getString("telp");
                } else {
                    adaKesalahan = true;
                    pesan = "Id Customer \""+idCustomer+"\" tidak ditemukan";
                }
                
                sta.close();
                rset.close();
                connection.close();
            } catch (SQLException ex){
                adaKesalahan = true; 
                pesan = "Tidak dapat membuka tabel customer\n"+ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n"+conn.getPesanKesalahan();
        }
        
        return !adaKesalahan;
    }
    public boolean bacaData(int mulai, int jumlah){
        boolean adaKesalahan = false;
        Connection connection;
        list = new Object[0][0] ;
        
        if ((connection = conn.getConnection()) != null){
            String SQLStatemen;
            Statement sta;
            ResultSet rset;
            
            try { 
                SQLStatemen = "select id,nama,alamat from customer "
                        + " limit  "+mulai+", "+jumlah; 
                sta = connection.createStatement(); 
                rset = sta.executeQuery(SQLStatemen);
                
                rset.next();
                rset.last();
                list = new Object[rset.getRow()][2];
                if (rset.getRow()>0){
                    rset.first();
                    int i=0;
                    do { 
                        list[i] = new Object[]{rset.getInt("id"),rset.getString("nama"), rset.getString("alamat")};
                        i++;
                    } while (rset.next());
                }
                
                sta.close();
                rset.close();
                connection.close();
            } catch (SQLException ex){
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel customer"+ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n"+conn.getPesanKesalahan();
        }
        
        return !adaKesalahan;
    }
  public boolean simpan(){
        boolean adaKesalahan = false;
        Connection connection; 
        
        if ((connection = conn.getConnection()) != null){
            int jumlahSimpan=0;
            boolean simpan = false; 
            
            try {
                String SQLStatemen = "select * from customer where nama='"+namaCustomer+"'";
                Statement sta = connection.createStatement();
                ResultSet rset = sta.executeQuery(SQLStatemen);
                
                rset.next();
                if (rset.getRow()>0){
                    try{
                        simpan = true;
                        SQLStatemen = "update customer set alamat='" + alamatCustomer + 
                                "', telp='" + telpCustomer +"' where nama='" + namaCustomer + "'";
                        System.out.println(SQLStatemen);
                        sta = connection.createStatement();
                        jumlahSimpan = sta.executeUpdate(SQLStatemen); 
                    }catch(SQLException ex){}
                    
                } else {
                    simpan = true;
                    SQLStatemen = "insert into customer(nama, alamat, telp) values ('" +
                            namaCustomer + "','" + alamatCustomer + "','" + telpCustomer + "')"; 
                    sta = connection.createStatement();
                    jumlahSimpan = sta.executeUpdate(SQLStatemen);
                }
                
                if (simpan) {
                    if (jumlahSimpan < 1){
                        adaKesalahan = true; 
                        pesan = "Gagal menyimpan data customer";
                    }
                }
                
                sta.close();
                rset.close();
                connection.close();                
            } catch (SQLException ex){
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel customer\n"+ex;
            }            
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n"+conn.getPesanKesalahan();
        }
        
        return !adaKesalahan;
    }
    public boolean hapus(String namaCustomer){
        boolean adaKesalahan = false;	
        Connection connection; 
        
        if ((connection = conn.getConnection()) != null){
            int jumlahHapus;
            Statement sta;
            
            try {
                String SQLStatemen = "delete from customer where nama='"+namaCustomer+"'";
                sta = connection.createStatement();
                jumlahHapus = sta.executeUpdate(SQLStatemen);
                
                if (jumlahHapus < 1){
                    pesan = "Data customer dengan nama "+namaCustomer+" tidak ditemukan";
                    adaKesalahan = true;
                }
                
                sta.close();
                connection.close();
            } catch (SQLException ex){
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel customer\n"+ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n"+conn.getPesanKesalahan();
        }
        
        return !adaKesalahan;
    }

}
