/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.o5tplen.controller;

import com.o5tplen.model.DataBarang;
import com.o5tplen.view.MainForm;
import java.io.IOException;
import java.io.OutputStream;
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
@WebServlet(name = "StokBarangController", urlPatterns = {"/StokBarangController"})
public class StokBarangController extends HttpServlet {

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
        DataBarang dataBarang = new DataBarang();
        String[][] formatTypeData = {{"PDF (Portable Document Format)", "pdf", "application/pdf"},
        {"XLSX (Microsoft Excel)", "xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
        {"XLS (Microsoft Excel 97-2003)", "xls", "application/vnd.ms-excel"},
        {"DOCX (Microsoft Word)", "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
        {"ODT (OpenDocument Text)", "odt", "application/vnd.oasis.opendocument.text"},
        {"RTF (Rich Text Format)", "rtf", "text/rtf"}};
        String formatType = request.getParameter("formatType");
        String userName = "";

        String tombol = request.getParameter("tombol");
        String mulaiParameter = request.getParameter("mulai");
        String jumlahParameter = request.getParameter("jumlah");
        int noType = 0;

        for (int i = 0; i < formatTypeData.length; i++) {
            if (formatTypeData[i][0].equals(formatType)) {
                noType = i;
            }
        }

        if (tombol == null) {
            tombol = "";
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
            if (tombol.equals("Cetak")) {

                if (dataBarang.cetakLaporanStokBarang(formatTypeData[noType][1], getServletConfig().getServletContext().getRealPath("reports/stokbarang.jrxml"))) {
                    byte[] pdfasbytes = dataBarang.getPdfasbytes();
                    try (OutputStream outStream = response.getOutputStream()) {
                        response.setHeader("Content-Disposition", "inline; filename=LaporanPenjualan." + formatTypeData[noType][1]);
                        response.setContentType(formatTypeData[noType][2]);

                        response.setContentLength(pdfasbytes.length);
                        outStream.write(pdfasbytes, 0, pdfasbytes.length);

                        outStream.flush();
                        outStream.close();
                    }
                } else {
                    keterangan += dataBarang.getPesan();
                }
            }
            String isi = "";
            String kontenLihat = "<table>";
            kontenLihat += "<tr>";
            kontenLihat += "<td align='center'><input type='submit' name='tombol' value='Sebelumnya' style='width: 100px'></td>";
            kontenLihat += "<td align='center'></td>";
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
            kontenLihat += "<tr>";
            kontenLihat += "<td align='right'>Format Laporan</td>";
            kontenLihat += "<td colspan='2'>";
            kontenLihat += "<select name='formatType'>";
            for (String[] formatLaporan : formatTypeData) {
                if (formatLaporan[0].equals(formatType)) {
                    kontenLihat += "<option selected value='" + formatLaporan[0] + "'>" + formatLaporan[0] + "</option>";
                } else {
                    kontenLihat += "<option value='" + formatLaporan[0] + "'>" + formatLaporan[0] + "</option>";
                }
            }
            kontenLihat += "</select><br>";

            kontenLihat += "</td>";
            kontenLihat += "</tr>";
            kontenLihat += "<tr>";
            kontenLihat += "<td colspan='3' align='center'><input type='submit' name='tombol' value='Cetak' style='width: 100px'></td>";
            kontenLihat += "</tr>";
            kontenLihat += "</table>";

            if (tombol.equals("Lihat") || tombol.equals("Sebelumnya") || tombol.equals("Berikutnya") || tombol.equals("Tampilkan")
                    || tombol.equals("") || tombol.equals("Cetak")) {
                if (tombol.equals("Sebelumnya")) {
                    mulai -= jumlah;
                    if (mulai < 0) {
                        mulai = 0;
                    }
                }

                if (tombol.equals("Berikutnya")) {
                    mulai += jumlah;
                }

                Object[][] listDataBarang = null;
                if (dataBarang.bacaData(mulai, jumlah)) {
                    listDataBarang = dataBarang.getList();
                } else {
                    keterangan = dataBarang.getPesan();
                }

                if (listDataBarang != null) {
                    for (int i = 0; i < listDataBarang.length; i++) {
                        isi += "<tr>";
                        isi += "<td>" + (i + 1) + "</td>";
                        for (int j = 0; j < listDataBarang[i].length; j++) {
                            isi += "<td>" + listDataBarang[i][j].toString() + "</td>";
                        }
                        isi += "</tr>";
                    }
                }

            }

            String konten = "<h2>Stok Barang</h2>";
            konten += "<form action='StokBarangController' method='post'>";
            konten += "<table id='t01'>";
            konten += "<thead>"
                    + "<tr>"
                    + "<th>No.</th>"
                    + "<th>Kode Barang</th>"
                    + "<th>Nama Barang</th>"
                    + "<th>Stok</th>"
                    + "<th>Satuan</th>";
            konten += "</tr>";
            konten += "</thead>";
            konten += "<tbody>";
            konten += isi;
            konten += "</tbody>";
            konten += "<tfoot>"
                    + "<tr>"
                    + "<td colspan='5' align='center'>"
                    + kontenLihat
                    + "</td>"
                    + "</tr>";
            konten += "</tfoot>";
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
