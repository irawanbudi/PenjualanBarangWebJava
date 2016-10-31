/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.o5tplen.controller;

import com.o5tplen.model.DataBarang;
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
@WebServlet(name = "DataBarangController", urlPatterns = {"/DataBarangController"})
public class DataBarangController extends HttpServlet {

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
        String userName = "";

        String tombol = request.getParameter("tombol");
        String kodeBarang = request.getParameter("kodeBarang");
        String namaBarang = request.getParameter("namaBarang");
        String harga = request.getParameter("harga");
        String stok = request.getParameter("stok");
        String satuan = request.getParameter("satuan");
        String mulaiParameter = request.getParameter("mulai");
        String jumlahParameter = request.getParameter("jumlah");
        String kodeBarangDipilih = request.getParameter("kodeBarangDipilih");

        if (tombol == null) {
            tombol = "";
        }
        if (kodeBarang == null) {
            kodeBarang = "";
        }
        if (namaBarang == null) {
            namaBarang = "";
        }
        if (harga == null) {
            harga = "0";
        }
        if (stok == null) {
            stok = "0";
        }
        if (satuan == null) {
            satuan = "pcs";
        }
        if (kodeBarangDipilih == null) {
            kodeBarangDipilih = "";
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
                if (!kodeBarang.equals("")) {
                    dataBarang.setKodeBarang(kodeBarang);
                    dataBarang.setNamaBarang(namaBarang);
                    dataBarang.setHargaBarang(Long.parseLong(harga));
                    dataBarang.setStokBarang(Integer.parseInt(stok));
                    dataBarang.setSatuanBarang(satuan);

                    if (dataBarang.simpan()) {
                        kodeBarang = "";
                        namaBarang = "";
                        harga = "0";
                        stok = "0";
                        keterangan = "Sudah tersimpan";
                    } else {
                        keterangan = "Gagal menyimpan:\n" + dataBarang.getPesan();
                    }
                } else {
                    keterangan = "Gagal menyimpan, kode barang tidak boleh kosong";
                }
            } else if (tombol.equals("Hapus")) {
                if (!kodeBarang.equals("")) {
                    if (dataBarang.hapus(kodeBarang)) {
                        kodeBarang = "";
                        namaBarang = "";
                        harga = "0";
                        stok = "0";
                        keterangan = "Data sudah dihapus";
                    } else {
                        keterangan = "Kode barang tersebut tidak ada, atau ada kesalahan:\n" + dataBarang.getPesan();
                    }
                } else {
                    keterangan = "Kode barang masih kosong";
                }
            } else if (tombol.equals("Cari")) {
                if (!kodeBarang.equals("")) {
                    if (dataBarang.baca(kodeBarang)) {
                        kodeBarang = dataBarang.getKodeBarang();
                        namaBarang = dataBarang.getNamaBarang();
                        harga = Long.toString(dataBarang.getHargaBarang());
                        stok = Integer.toString(dataBarang.getStokBarang());
                        satuan = dataBarang.getSatuanBarang();
                        keterangan = "<br>";
                    } else {
                        keterangan = "Kode barang tersebut tidak ada";
                    }
                } else {
                    keterangan = "Kode barang masih kosong";
                }
            } else if (tombol.equals("Pilih")) {
                kodeBarang = kodeBarangDipilih;
                namaBarang = "";
                stok = "0";
                harga = "0";
                if (!kodeBarangDipilih.equals("")) {
                    if (dataBarang.baca(kodeBarangDipilih)) {
                        kodeBarang = dataBarang.getKodeBarang();
                        namaBarang = dataBarang.getNamaBarang();
                        harga = Long.toString(dataBarang.getHargaBarang());
                        stok = Integer.toString(dataBarang.getStokBarang());
                        satuan = dataBarang.getSatuanBarang();
                        keterangan = "<br>";
                    } else {
                        keterangan = "Kode barang tersebut tidak ada";
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

                Object[][] listDataBarang = null;
                if (dataBarang.bacaData(mulai, jumlah)) {
                    listDataBarang = dataBarang.getList();
                } else {
                    keterangan = dataBarang.getPesan();
                }

                if (listDataBarang != null) {
                    for (int i = 0; i < listDataBarang.length; i++) {
                        kontenLihat += "<tr>";
                        kontenLihat += "<td>";
                        if (i == 0) {
                            kontenLihat += "<input type='radio' checked name='kodeBarangDipilih' value='" + listDataBarang[i][0].toString() + "'>";
                        } else {
                            kontenLihat += "<input type='radio' name='kodeBarangDipilih' value='" + listDataBarang[i][0].toString() + "'>";
                        }
                        kontenLihat += "</td>";
                        kontenLihat += "<td>";
                        kontenLihat += listDataBarang[i][0].toString();
                        kontenLihat += "</td>";
                        kontenLihat += "<td>";
                        kontenLihat += listDataBarang[i][1].toString();
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

            String konten = "<h2>Master Data Barang</h2>";
            konten += "<form action='DataBarangController' method='post'>";
            konten += "<table>";
            konten += "<tr>";
            konten += "<td align='right'>Kode Barang</td>";
            konten += "<td align='left'><input type='text'  value='" + kodeBarang + "' name='kodeBarang' maxlength='15' size='15'><input type='submit' name='tombol' value='Cari'></td>";
            konten += "</tr>";
            konten += "<tr>";
            konten += "<td align='right'>Nama Barang</td>";
            konten += "<td align='left'><input type='text'  value='" + namaBarang + "' name='namaBarang' maxlength='30' size='30'></td>";
            konten += "</tr>"
                    + "<tr>"
                    + "<td align='right'>Harga</td>"
                    + "<td align='left'><input type='number' value='" + harga + "' name='harga'></td>"
                    + "</tr>";
            konten += "<tr>";
            konten += "<td align='right'>Stok</td>";
            konten += "<td align='left'>";
            konten += "<input type='number' name='stok' value='" + stok + "'>";

            konten += "</td>";
            konten += "</tr>";
            konten += "<tr>";
            konten += "<td align='right'>Satuan</td>";
            konten += "<td align='left'><select name='satuan'>";
            if((satuan.equalsIgnoreCase("pcs"))||satuan.length()==0)
            konten += "<option  selected value='pcs'>pcs</option>";
            else konten += "<option value='pcs'>pcs</option>";
            if(satuan.equalsIgnoreCase("kg"))
            konten += "<option  selected value='kg'>Kg</option>";
            else konten += "<option value='kg'>Kg</option>";
            if(satuan.equalsIgnoreCase("L"))
            konten += "<option  selected value='L'>L</option>";
            else konten += "<option value='L'>L</option>";
            if(satuan.equalsIgnoreCase("box"))
            konten += "<option  selected value='box'>box</option>";
            else konten += "<option value='box'>box</option>";
            konten += "</select>";
            konten += "</td>";
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
