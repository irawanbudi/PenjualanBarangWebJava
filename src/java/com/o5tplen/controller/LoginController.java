/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.o5tplen.controller;

import com.o5tplen.model.Enkripsi;
import com.o5tplen.model.DataUser;
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
@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

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
        String userName = "";

        try {
            userName = session.getAttribute("userName").toString();
        } catch (Exception ex) {
        }

        if (((userName == null) || userName.equals(""))) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            String konten = "<br><form action=LoginController method=post>"
                    + "<table>"
                    + "<tr>"
                    + "<td>Username</td><td><input type=text name=username></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Password</td><td><input type=password name=password></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td colspan=2 align=center><input type=submit value=Login><td>"
                    + "</tr>"
                    + "</table>"
                    + "</form>";
            String pesan = "";

            if (username == null) {
            } else if (username.equals("")) {
                pesan = "<br><br><font style='color:red'>Username harus diisi</font>";
            } else {
                DataUser dataUser = new DataUser();
                Enkripsi enkripsi = new Enkripsi();

                pesan = "<br><br><font style='color:red'>Username atau password salah</font>";

                if (dataUser.baca(username)) {
                    String passwordEncrypted = "";

                    try {
                        passwordEncrypted = enkripsi.hashMD5(password);
                    } catch (Exception ex) {
                    }

                    if (passwordEncrypted.equals(dataUser.getPassword())) {
                        pesan = "";
                        session.setAttribute("userName", dataUser.getUsername().equals("") ? "No Name" : dataUser.getUsername());
                        String menu = "<br><b>Master Data</b><br>"
                                + "<a href=DataBarangController>Data Barang</a><br>"
                                + "<a href=DataCustomerController>Data Customer</a><br>"
                                + "<a href=DataUserController>Data User</a><br><br>"
                                + "<b>Transaksi</b><br>"
                                + "<a href=PenjualanController>Penjualan</a><br><br>"
                                + "<b>Laporan</b><br>"
                                + "<a href=LaporanPenjualanController>Penjualan</a><br>"
                                + "<a href=StokBarangController>Stok Barang</a><br><br>"
                                + "<a href=LogoutController>Logout</a><br><br>";
                        session.setAttribute("menu", menu);

                        String topMenu = "<nav><ul>"
                                + "<li><a href=.>Home</a></li>"
                                + "<li class='dropdown'><a href=# class='dropbtn'>Master Data</a>"
                                + "<div class='dropdown-content'>"
                                + "<a href=DataBarangController>Data Barang</a>"
                                + "<a href=DataCustomerController>Data Customer</a>"
                                + "<a href=DataUserController>Data User</a>"
                                + "</div>"
                                + "</li>"
                                + "<li class='dropdown'><a href=# class='dropbtn'>Transaksi</a>"
                                + "<div class='dropdown-content'>"
                                + "<a href=PenjualanController>Penjualan</a>"
                                + "</div>"
                                + "</li>"
                                + "<li class='dropdown'><a href=# class='dropbtn'>Laporan</a>"
                                + "<div class='dropdown-content'>"
                                + "<a href=LaporanPenjualanController>Penjualan</a>"
                                + "<a href=StokBarangController>Stok Barang</a>"
                                + "</div>"
                                + "</li>"
                                + "<li><a href=LogoutController>Logout</a></li>"
                                + "<li><a href=Deskripsi>Deskripsi</a></li>"
                                + "</ul>"
                                + "</nav>";
                        session.setAttribute("topMenu", topMenu);

                        session.setMaxInactiveInterval(15 * 60); // 15 x 60 detik = 15 menit
                        konten = "";
                    }
                } else if (!dataUser.getPesan().substring(0, 3).equals("username")) {
                    pesan = "<br><br><font style='color:red'>" + dataUser.getPesan().replace("\n", "<br>") + "</font>";
                }
            }

            new MainForm().tampilkan(konten + pesan, request, response);
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
