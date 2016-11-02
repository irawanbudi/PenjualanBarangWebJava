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
public class DataUser {

    private String username, password;
    private String pesan;
    private Object[][] list;
    private final koneksi conn = new koneksi();
    private final PesanDialog pesanDialog = new PesanDialog();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean baca(String username) {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = conn.getConnection()) != null) {
            Statement sta;
            ResultSet rset;

            try {
                String SQLStatemen = "select * from user where username='" + username + "'";
                sta = connection.createStatement();
                rset = sta.executeQuery(SQLStatemen);

                rset.next();
                if (rset.getRow() > 0) {
                    this.username = rset.getString("username");
                    this.password = rset.getString("password");
                    // this.telpCustomer=rset.getString("telp");
                } else {
                    adaKesalahan = true;
                    pesan = "Username \"" + username + "\" tidak ditemukan";
                }

                sta.close();
                rset.close();
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel user\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + conn.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

    public boolean bacaData(int mulai, int jumlah) {
        boolean adaKesalahan = false;
        Connection connection;
        list = new Object[0][0];

        if ((connection = conn.getConnection()) != null) {
            String SQLStatemen;
            Statement sta;
            ResultSet rset;

            try {
                SQLStatemen = "select username,password from user limit "
                        + mulai + "," + jumlah;
                sta = connection.createStatement();
                rset = sta.executeQuery(SQLStatemen);

                rset.next();
                rset.last();
                list = new Object[rset.getRow()][2];
                if (rset.getRow() > 0) {
                    rset.first();
                    int i = 0;
                    do {
                        list[i] = new Object[]{rset.getString("username"), rset.getString("password")};
                        i++;
                    } while (rset.next());
                }

                sta.close();
                rset.close();
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel user" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + conn.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

    public boolean simpan() {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = conn.getConnection()) != null) {
            int jumlahSimpan = 0;
            boolean simpan = false;

            try {
                String SQLStatemen = "select * from user where username='" + username + "'";
                Statement sta = connection.createStatement();
                ResultSet rset = sta.executeQuery(SQLStatemen);

                rset.next();
                if (rset.getRow() > 0) {

                    try {
                        simpan = true;
                        SQLStatemen = "update user set password='" + password
                                + "' where username='" + username + "'";
                        System.out.println(SQLStatemen);
                        sta = connection.createStatement();
                        jumlahSimpan = sta.executeUpdate(SQLStatemen);
                    } catch (SQLException ex) {
                    }

                } else {
                    simpan = true;
                    SQLStatemen = "insert into user(username, password) values ('"
                            + username + "','" + password + "')";
                    sta = connection.createStatement();
                    jumlahSimpan = sta.executeUpdate(SQLStatemen);
                }

                if (simpan) {
                    if (jumlahSimpan < 1) {
                        adaKesalahan = true;
                        pesan = "Gagal menyimpan data user";
                    }
                }

                sta.close();
                rset.close();
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel user\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + conn.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

    public boolean hapus(String username) {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = conn.getConnection()) != null) {
            int jumlahHapus;
            Statement sta;

            try {
                String SQLStatemen = "delete from user where username='" + username + "'";
                sta = connection.createStatement();
                jumlahHapus = sta.executeUpdate(SQLStatemen);

                if (jumlahHapus < 1) {
                    pesan = "Data user dengan username " + username + " tidak ditemukan";
                    adaKesalahan = true;
                }

                sta.close();
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel user\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + conn.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

}
