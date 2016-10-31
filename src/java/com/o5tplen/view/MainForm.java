/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.o5tplen.view;

import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "MainForm", urlPatterns = {"/MainForm"})
public class MainForm extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void tampilkan(String konten, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String menu = "<br><b>Master Data</b><br>"
                + "<a href=DataBarangController>Data Barang</a><br>"
                + "<a href=DataCustomerController>Data Customer</a><br>"
                + "<a href=DataUserController>Data User</a><br><br>"
                + "<b>Transaksi</b><br>"
                + "<a href=PenjualanController>Penjualan</a><br><br>"
                + "<b>Laporan</b><br>"
                + "<a href=LaporanPenjualanController>Penjualan</a><br>"
                + "<a href=StokBarangController>Stok Barang</a><br><br>"
                + "<a href=LoginController>Login</a><br><br>";

        String topMenu = "<nav><ul>"
                +   "<li><a href=.>Home</a></li>"
                +   "<li class='dropdown'><a href=# class='dropbtn'>Master Data</a>"
                +   "<div class='dropdown-content'>"
                +       "<a href=DataBarangController>Data Barang</a>"
                +       "<a href=DataCustomerController>Data Customer</a>"
                +       "<a href=DataUserController>Data User</a>"
                +    "</div>"
                +   "</li>"
                +   "<li class='dropdown'><a href=# class='dropbtn'>Transaksi</a>"
                +       "<div class='dropdown-content'>"
                +       "<a href=PenjualanController>Penjualan</a>"
                +       "</div>"
                +   "</li>"
                +   "<li class='dropdown'><a href=# class='dropbtn'>Laporan</a>"
                +       "<div class='dropdown-content'>"
                +       "<a href=LaporanPenjualanController>Penjualan</a>"
                +       "<a href=StokBarangController>Stok Barang</a>"
                +       "</div>"
                +   "</li>"
                +   "<li><a href=LoginController>Login</a></li>"
                + "<li><a href=Deskripsi>Deskripsi</a></li>"
                +  "</ul>"
                + "</nav>";

        String userName = "";
        if (!session.isNew()) {
            try {
                userName
                        = session.getAttribute("userName").toString();
            } catch (Exception ex) {
            }

            if (!((userName == null) || userName.equals(""))) {
                if (konten.equals("")) {
                    konten = "<br><h1>Selamat Datang</h1><h2>" + userName + "</h2>";
                }

                try {
                    menu = session.getAttribute("menu").toString();
                } catch (Exception ex) {
                }

                try {
                    topMenu = session.getAttribute("topMenu").toString();
                } catch (Exception ex) {
                }
            }
        }
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following
sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link href='style.css' rel='stylesheet' type='text/css' />");
            out.println("<title>Aplikasi Penjualan Barang - Web</title>");
            out.println("</head>");
            out.println("<body bgcolor=\"#808080\">");
            out.println("<center>");
            out.println("<table width=\"80%\" bgcolor=\"#eeeeee\">   ");
            out.println("<tr>");
            out.println("<td colspan=\"2\" align=\"center\">");
            out.println("<br>");
            out.println("<h2 Style=\"margin-bottom:0px; margin-top:0px;\">");
            out.println("Aplikasi Penjualan Barang - Web");
            out.println("</h2>");
            out.println("<h1 Style=\"margin-bottom:0px; margin-top:0px;\">");
            out.println("TOKO 05TPLEN");
            out.println("</h1>");
            out.println("<h4 Style=\"margin-bottom:0px; margin-top:0px;\">");
            out.println("Jl. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten");
            out.println("</h4>");
            out.println("<br>");
            out.println("</td>");
            out.println("</tr>");
            out.println("<tr height=\"400\">");
            out.println("<td width=\"200\" align=\"center\" valign=\"top\" bgcolor=\"#eeffee\"><br>");
            out.println("<div id='menu'>");
            out.println(menu);
            out.println("</div>");
            out.println("</td>");
            out.println("<td align=\"center\" valign=\"top\" bgcolor=\"#ffffff\">");
            out.println(topMenu);
            out.println("<br>");
            out.println(konten);
            out.println("</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td colspan=\"2\" align=\"center\" bgcolor=\"#eeeeff\">");
            out.println("<small>");
            out.println("Copyright &copy; 2016 TOKO 05TPLEN<br>");
            out.println("Jl. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten<br>");
            out.println("</small>");
            out.println("</td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("</center>");
            out.println("</body>");
            out.println("</html>");

        }

    }
}
