/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.o5tplen.controller;

import com.o5tplen.model.DataCustomer;
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
@WebServlet(name = "DataCustomerController", urlPatterns = {"/DataCustomerController"})
public class DataCustomerController extends HttpServlet {

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
        DataCustomer dataCustomer = new DataCustomer();
        String userName = "";

        String tombol = request.getParameter("tombol");
        String namaCustomer = request.getParameter("nama");
        String alamatCustomer = request.getParameter("alamat");
        String telpon = request.getParameter("telepon");
        String mulaiParameter = request.getParameter("mulai");
        String jumlahParameter = request.getParameter("jumlah");
        String namaCustomerDipilih = request.getParameter("namaCustomerDipilih");

        if (tombol == null) {
            tombol = "";
        }
        if (namaCustomer == null) {
            namaCustomer = "";
        }
        if (alamatCustomer == null) {
            alamatCustomer = "";
        }
        if (telpon == null) {
            telpon = "";
        }
        if (namaCustomerDipilih == null) {
            namaCustomerDipilih = "";
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
                if (!namaCustomer.equals("")) {
                    dataCustomer.setNamaCustomer(namaCustomer);
                    dataCustomer.setAlamatCustomer(alamatCustomer);
                    dataCustomer.setTelpCustomer(telpon);

                    if (dataCustomer.simpan()) {
                        namaCustomer = "";
                        alamatCustomer = "";
                        telpon = "";
                        keterangan = "Sudah tersimpan";
                    } else {
                        keterangan = "Gagal menyimpan:\n" + dataCustomer.getPesan();
                    }
                } else {
                    keterangan = "Gagal menyimpan, nama customer tidak boleh kosong";
                }
            } else if (tombol.equals("Hapus")) {
                if (!namaCustomer.equals("")) {
                    if (dataCustomer.hapus(namaCustomer)) {
                        namaCustomer = "";
                        alamatCustomer = "";
                        telpon = "";
                         keterangan = "Data sudah dihapus";
                    } else {
                        keterangan = "Nama Customer tersebut tidak ada, atau ada kesalahan:\n" + dataCustomer.getPesan();
                    }
                } else {
                    keterangan = "Nama Customer masih kosong";
                }
            } else if (tombol.equals("Cari")) {
                if (!namaCustomer.equals("")) {
                    if (dataCustomer.baca(namaCustomer)) {
                        namaCustomer = dataCustomer.getNamaCustomer();
                        alamatCustomer = dataCustomer.getAlamatCustomer();
                        telpon = dataCustomer.getTelpCustomer();
                        keterangan = "<br>";
                    } else {
                        keterangan = "Nama Customer tersebut tidak ada";
                    }
                } else {
                    keterangan = "Nama Customer masih kosong";
                }
            } else if (tombol.equals("Pilih")) {
                namaCustomer = namaCustomerDipilih;
                alamatCustomer = "";
                telpon = "";
                if (!namaCustomerDipilih.equals("")) {
                    if (dataCustomer.baca(namaCustomerDipilih)) {
                        namaCustomer = dataCustomer.getNamaCustomer();
                        alamatCustomer = dataCustomer.getAlamatCustomer();
                        telpon = dataCustomer.getTelpCustomer();
                        keterangan = "<br>";
                    } else {
                        keterangan = "Nama Customer tersebut tidak ada";
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

                Object[][] listDataCustomer = null;
                if (dataCustomer.bacaData(mulai, jumlah)) {
                    listDataCustomer = dataCustomer.getList();
                } else {
                    keterangan = dataCustomer.getPesan();
                }

                if (listDataCustomer != null) {
                    for (int i = 0; i < listDataCustomer.length; i++) {
                        kontenLihat += "<tr>";
                        kontenLihat += "<td>";
                        if (i == 0) {
                            kontenLihat += "<input type='radio' checked name='namaCustomerDipilih' value='" + listDataCustomer[i][1].toString() + "'>";
                        } else {
                            kontenLihat += "<input type='radio' name='namaCustomerDipilih' value='" + listDataCustomer[i][1].toString() + "'>";
                        }
                        kontenLihat += "</td>";
                        kontenLihat += "<td>";
                        kontenLihat += listDataCustomer[i][1].toString();
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

            String konten = "<h2>Master Data Customer</h2>";
            konten += "<form action='DataCustomerController' method='post'>";
            konten += "<table>";
            konten += "<tr>";
            konten += "<td align='right'>Nama Customer</td>";
            konten += "<td align='left'><input type='text'  value='" + namaCustomer + "' name='nama' maxlength='30' size='15'><input type='submit' name='tombol' value='Cari'></td>";
            konten += "</tr>";
            konten += "<tr>";
            konten += "<td align='right'>Alamat</td>";
            konten += "<td align='left'><textarea   name='alamat' rows='3' cols='30'>"+alamatCustomer+"</textarea></td>";
            konten += "</tr>";
            konten+="<tr>"
                    + "<td align='right'>Telepon</td>"
                    + "<td align='left'><input type='tel' value='"+telpon+"' name='telepon'></td>"
                    + "</tr>";
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
