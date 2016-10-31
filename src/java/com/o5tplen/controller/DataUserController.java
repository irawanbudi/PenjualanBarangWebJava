/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.o5tplen.controller;

import com.o5tplen.model.DataUser;
import com.o5tplen.model.Enkripsi;
import com.o5tplen.view.MainForm;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Irawan
 */
@WebServlet(name = "DataUserController", urlPatterns = {"/DataUserController"})
public class DataUserController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        DataUser dataUser = new DataUser();
        Enkripsi enkripsi=new Enkripsi();
        String userName = "";

        String tombol = request.getParameter("tombol");
        String namaUser = request.getParameter("nama");
        String passwordUser = request.getParameter("password");
        String mulaiParameter = request.getParameter("mulai");
        String jumlahParameter = request.getParameter("jumlah");
        String namaUserDipilih = request.getParameter("namaUserDipilih");

        if (tombol == null) {
            tombol = "";
        }
        if (namaUser == null) {
            namaUser = "";
        }
        if (passwordUser == null) {
            passwordUser = "";
        }
        if (namaUserDipilih == null) {
            namaUserDipilih = "";
        }

        int mulai = 0, jumlah = 10;

        try {
            mulai = Integer.parseInt(mulaiParameter);
        } catch (NumberFormatException ex) {
        }

        try {
            jumlah = Integer.parseInt(jumlahParameter);
        } catch (NumberFormatException ex) {
        }

        String keterangan = "<br>";

        try {
            userName = session.getAttribute("userName").toString();
        } catch (Exception ex) {
        }

        if (!((userName == null) || userName.equals(""))) {
            if (tombol.equals("Simpan")) {
                if (!namaUser.equals("")) {
                    dataUser.setUsername(namaUser);
                    String passwordEcrypted = "";
                    try {
                        passwordEcrypted = enkripsi.hashMD5(passwordUser);
                    } catch (Exception ex){}
                    dataUser.setPassword(passwordEcrypted);

                    if (dataUser.simpan()) {
                        namaUser = "";
                        passwordUser = "";
                        keterangan = "Sudah tersimpan";
                    } else {
                        keterangan = "Gagal menyimpan:\n" + dataUser.getPesan();
                    }
                } else {
                    keterangan = "Gagal menyimpan, username tidak boleh kosong";
                }
            } else if (tombol.equals("Hapus")) {
                if (!namaUser.equals("")) {
                    if (dataUser.hapus(namaUser)) {
                        namaUser = "";
                        passwordUser = "";
                         keterangan = "Data sudah dihapus";
                    } else {
                        keterangan = "Username tersebut tidak ada, atau ada kesalahan:\n" + dataUser.getPesan();
                    }
                } else {
                    keterangan = "Username masih kosong";
                }
            } else if (tombol.equals("Cari")) {
                if (!namaUser.equals("")) {
                    if (dataUser.baca(namaUser)) {
                        namaUser = dataUser.getUsername();
                        passwordUser = dataUser.getPassword();
                        keterangan = "<br>";
                    } else {
                        keterangan = "Username tersebut tidak ada";
                    }
                } else {
                    keterangan = "Username masih kosong";
                }
            } else if (tombol.equals("Pilih")) {
                namaUser = namaUserDipilih;
                passwordUser = "";
                if (!namaUserDipilih.equals("")) {
                    if (dataUser.baca(namaUserDipilih)) {
                        namaUser = dataUser.getUsername();
                        passwordUser = dataUser.getPassword();
                        keterangan = "<br>";
                    } else {
                        keterangan = "Username tersebut tidak ada";
                    }
                } else {
                    keterangan = "Tidak ada yang dipilih";
                }
            }

            String kontenLihat = "";
            if (tombol.equals("Lihat") || tombol.equals("Sebelumnya") || tombol.equals("Berikutnya") || tombol.equals("Tampilkan")) {
                kontenLihat += "<tr>";
                kontenLihat += "<td colspan='2' align='center'>";
                kontenLihat += "<table>";

                if (tombol.equals("Sebelumnya")) {
                    mulai -= jumlah;
                    if (mulai < 0) {
                        mulai = 0;
                    }
                }

                if (tombol.equals("Berikutnya")) {
                    mulai += jumlah;
                }

                Object[][] listDataUser = null;
                if (dataUser.bacaData(mulai, jumlah)) {
                    listDataUser = dataUser.getList();
                } else {
                    keterangan = dataUser.getPesan();
                }

                if (listDataUser != null) {
                    for (int i = 0; i < listDataUser.length; i++) {
                        kontenLihat += "<tr>";
                        kontenLihat += "<td>";
                        if (i == 0) {
                            kontenLihat += "<input type='radio' checked name='namaUserDipilih' value='" + listDataUser[i][0].toString() + "'>";
                        } else {
                            kontenLihat += "<input type='radio' name='namaUserDipilih' value='" + listDataUser[i][0].toString() + "'>";
                        }
                        kontenLihat += "</td>";
                        kontenLihat += "<td>";
                        kontenLihat += listDataUser[i][0].toString();
                        kontenLihat += "</td>";
                        kontenLihat += "</tr>";
                    }
                }

                kontenLihat += "</table>";
                kontenLihat += "</td>";
                kontenLihat += "</tr>";

                kontenLihat += "<tr>";
                kontenLihat += "<td colspan='2' align='center'>";
                kontenLihat += "<table>";
                kontenLihat += "<tr>";
                kontenLihat += "<td align='center'><input type='submit' name='tombol' value='Sebelumnya' style='width: 100px'></td>";
                kontenLihat += "<td align='center'><input type='submit' name='tombol' value='Pilih' style='width: 60px'></td>";
                kontenLihat += "<td align='center'><input type='submit' name='tombol' value='Berikutnya' style='width: 100px'></td>";
                kontenLihat += "</tr>";
                kontenLihat += "<tr>";
                kontenLihat += "<td align='center'>Mulai <input type='text' name='mulai' value=" + mulai + " style='width: 40px'></td>";
                kontenLihat += "<td>Jumlah";
                kontenLihat += "<select name='jumlah'>";

                for (int i = 1; i <= 10; i++) {
                    if (jumlah == (i * 10)) {
                        kontenLihat += "<option selected value=" + i * 10 + ">" + i * 10 + "</option>";
                    } else {
                        kontenLihat += "<option value=" + i * 10 + ">" + i * 10 + "</option>";
                    }
                }

                kontenLihat += "</select>";
                kontenLihat += "</td>";
                kontenLihat += "<td align='center'><input type='submit' name='tombol' value='Tampilkan' style='width: 90px'></td>";
                kontenLihat += "</tr>";
                kontenLihat += "</table>";
                kontenLihat += "</td>";
                kontenLihat += "</tr>";
            }

            String konten = "<h2>Master Data User</h2>";
            konten += "<form action='DataUserController' method='post'>";
            konten += "<table>";
            konten += "<tr>";
            konten += "<td align='right'>Username</td>";
            konten += "<td align='left'><input type='text'  value='" + namaUser + "' name='nama' maxlength='30' size='15'><input type='submit' name='tombol' value='Cari'></td>";
            konten += "</tr>";
            konten += "<tr>";
            konten += "<td align='right'>Password</td>";
            konten += "<td align='left'><input type='password'   name='password' value='"+passwordUser+"' size='15'></td>";
            konten += "</tr>";
            konten += "<tr>";
            konten += "<td colspan='2'><b>" + keterangan.replaceAll("\n", "<br>").replaceAll(";", ",") + "</b></td>";
            konten += "</tr>";

            konten += "<tr>";
            konten += "<td colspan='2' align='center'>";
            konten += "<table>";
            konten += "<tr>";
            konten += "<td align='center'><input type='submit' name='tombol' value='Simpan' style='width: 100px'></td>";
            konten += "<td align='center'><input type='submit' name='tombol' value='Hapus' style='width: 100px'></td>";
            konten += "<td align='center'><input type='submit' name='tombol' value='Lihat' style='width: 100px'></td>";
            konten += "</tr>";
            konten += "</table>";
            konten += "</td>";
            konten += "</tr>";

            konten += "<tr>";
            konten += "<td colspan='2' align='center'><br>";
            konten += "</td>";
            konten += "</tr>";
            konten += kontenLihat;
            konten += "</table>";
            konten += "</form>";

            new MainForm().tampilkan(konten, request, response);
        } else {
            response.sendRedirect(".");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
