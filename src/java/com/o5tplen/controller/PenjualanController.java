/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.o5tplen.controller;

import com.o5tplen.model.DataBarang;
import com.o5tplen.model.DataCustomer;
import com.o5tplen.model.Penjualan;
import com.o5tplen.view.MainForm;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@WebServlet(name = "PenjualanController", urlPatterns = {"/PenjualanController"})
public class PenjualanController extends HttpServlet {

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
        DataBarang dataBarang = new DataBarang();
        Penjualan penjualan = new Penjualan();
        Date tgl = new Date();
        int idCustomer = 0;
        long total = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String userName = "";

        String tombol = request.getParameter("tombol");
        String tombolHitung = request.getParameter("hitung");
        String tombolCustomer = request.getParameter("tombolCustomer");
        String kodePenjualan = request.getParameter("kodePenjualan");
        String namaCustomer = request.getParameter("namaCustomer");
        String tanggal = request.getParameter("tanggal");
        String kasir = request.getParameter("kasir");
        String mulaiParameter = request.getParameter("mulai");
        String jumlahParameter = request.getParameter("jumlah");
        String namaCustomerDipilih = request.getParameter("namaCustomerDipilih");
        String tombolDataBarang = request.getParameter("tombolDataBarang");
        String kodeBarang = request.getParameter("kodeBarang");
        String namaBarang = request.getParameter("namaBarang");
        String harga = request.getParameter("harga");
        String satuan = request.getParameter("satuan");
        String kodeBarangDipilih = request.getParameter("kodeBarangDipilih");
        String qty = request.getParameter("qty");

        if (tombol == null) {
            tombol = "";
        }
        if (kodePenjualan == null) {
            kodePenjualan = "";
        }
        if (tombolHitung == null) {
            tombolHitung = "";
        }
        if (tombolCustomer == null) {
            tombolCustomer = "";
        }
        if (namaCustomer == null) {
            namaCustomer = "";
        }
        if (tanggal == null) {
            tanggal = sdf.format(tgl);
        }
        if (kasir == null) {
            kasir = "kasir";
        }
        if (namaCustomerDipilih == null) {
            namaCustomerDipilih = "";
        }
        if (tombolDataBarang == null) {
            tombolDataBarang = "";
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
        if (satuan == null) {
            satuan = "pcs";
        }
        if (kodeBarangDipilih == null) {
            kodeBarangDipilih = "";
        }
        if (qty == null) {
            qty = "0";
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
            kasir = userName;

            if (tombolCustomer.equals("Cari")) {
                if (!namaCustomer.equals("")) {
                    if (dataCustomer.baca(namaCustomer)) {
                        namaCustomer = dataCustomer.getNamaCustomer();
                        idCustomer = dataCustomer.getIdCustomer();
                        keterangan = "<br>";
                    } else {
                        tanggal = sdf.format(tgl);
                        kasir = "kasir";
                        keterangan = "Nama Customer: <i>" + namaCustomer + "</i> tidak ada";
                    }
                } else {
                    keterangan = "Nama Customer harus diisi";
                }
            } else if (tombolCustomer.equals("Pilih")) {
                namaCustomer = namaCustomerDipilih;
                tanggal = sdf.format(tgl);
                if (!namaCustomerDipilih.equals("")) {
                    if (dataCustomer.baca(namaCustomerDipilih)) {
                        namaCustomer = dataCustomer.getNamaCustomer();
                        idCustomer = dataCustomer.getIdCustomer();
                        keterangan = "<br>";
                    } else {
                        keterangan = "Nama Customer: <i>" + namaCustomer + "</i> tidak ada";
                    }
                } else {
                    keterangan = "Tidak ada yang dipilih";
                }
            }

            String kontenLihat = "";
            if (tombolCustomer.equals("Lihat") || tombolCustomer.equals("Sebelumnya") || tombolCustomer.equals("Berikutnya") || tombolCustomer.equals("Tampilkan")) {
                kontenLihat = "<tr>";
                kontenLihat += "<td colspan='2' align='center'>";
                kontenLihat += "<table>";

                if (tombolCustomer.equals("Sebelumnya")) {
                    mulai -= jumlah;
                    if (mulai < 0) {
                        mulai = 0;
                    }
                }

                if (tombolCustomer.equals("Berikutnya")) {
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
                kontenLihat += "<td align='center'><input type='submit' name='tombolCustomer' value='Sebelumnya' style='width: 100px'></td>";
                kontenLihat += "<td align='center'><input type='submit' name='tombolCustomer' value='Pilih' style='width: 60px'></td>";
                kontenLihat += "<td align='center'><input type='submit' name='tombolCustomer' value='Berikutnya' style='width: 100px'></td>";
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
                kontenLihat += "<td align='center'><input type='submit' name='tombolCustomer' value='Tampilkan' style='width: 90px'></td>";
                kontenLihat += "</tr>";
                kontenLihat += "</table>";
                kontenLihat += "</td>";
                kontenLihat += "</tr>";
                kontenLihat += "<tr>";
                kontenLihat += "<td colspan='2' align='center'><br></td>";
                kontenLihat += "</tr>";
            }

            if (tombolDataBarang.equals("Cari")) {
                if (!kodeBarang.equals("")) {
                    if (dataBarang.baca(kodeBarang)) {
                        kodeBarang = dataBarang.getKodeBarang();
                        namaBarang = dataBarang.getNamaBarang();
                        harga = Long.toString(dataBarang.getHargaBarang());
                        keterangan = "<br>";
                    } else {
                        keterangan = "Kode Barang tersebut tidak ada";
                    }
                } else {
                    keterangan = "Kode Barang masih kosong";
                }
            } else if (tombolDataBarang.equals("Pilih")) {
                kodeBarang = kodeBarangDipilih;
                namaBarang = "";
                harga = "0";
                qty = "0";
                if (!kodeBarangDipilih.equals("")) {
                    if (dataBarang.baca(kodeBarangDipilih)) {
                        kodeBarang = dataBarang.getKodeBarang();
                        namaBarang = dataBarang.getNamaBarang();
                        harga = Long.toString(dataBarang.getHargaBarang());
                        keterangan = "<br>";
                    } else {
                        keterangan = "Kode barang tersebut tidak ada";
                    }
                } else {
                    keterangan = "Tidak ada yang dipilih";
                }
            }

            if (tombolDataBarang.equals("Lihat") || tombolDataBarang.equals("Sebelumnya") || tombolDataBarang.equals("Berikutnya") || tombolDataBarang.equals("Tampilkan")) {
                kontenLihat += "<tr>";
                kontenLihat += "<td colspan='2' align='center'>";
                kontenLihat += "<table>";

                if (tombolDataBarang.equals("Sebelumnya")) {
                    mulai -= jumlah;
                    if (mulai < 0) {
                        mulai = 0;
                    }
                }

                if (tombolDataBarang.equals("Berikutnya")) {
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
                kontenLihat += "<td align='center'><input type='submit' name='tombolDataBarang' value='Sebelumnya' style='width: 100px'></td>";
                kontenLihat += "<td align='center'><input type='submit' name='tombolDataBarang' value='Pilih' style='width: 60px'></td>";
                kontenLihat += "<td align='center'><input type='submit' name='tombolDataBarang' value='Berikutnya' style='width: 100px'></td>";
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
                kontenLihat += "<td align='center'><input type='submit' name='tombolDataBarang' value='Tampilkan' style='width: 90px'></td>";
                kontenLihat += "</tr>";
                kontenLihat += "</table>";
                kontenLihat += "</td>";
                kontenLihat += "</tr>";
                kontenLihat += "<tr>";
                kontenLihat += "<td colspan='2' align='center'><br></td>";
                kontenLihat += "</tr>";
            }

            if (!tombol.equals("")) {
                if (tombol.equals("Simpan")) {
                    if (!namaCustomer.equals("") && !kodeBarang.equals("")) {
                        penjualan.setKodePenjualan(kodePenjualan);
                        dataCustomer.setNamaCustomer(namaCustomer);
                        if(dataCustomer.baca(namaCustomer)){
                            idCustomer=dataCustomer.getIdCustomer();
                        }
                        penjualan.setListPenjualan(new Object[][]{{kodePenjualan, tanggal, idCustomer, kasir, kodeBarang, Integer.parseInt(qty)}});
                        if (penjualan.simpan()) {
                            //namaCustomer = "";
                            tanggal = sdf.format(tgl);
                            kasir = userName;
                            kodeBarang = "";
                            namaBarang = "";
                            harga = "0";
                            qty="0";
                            keterangan = "Sudah disimpan";
                        } else {
                            keterangan = "Gagal menyimpan:\n" + penjualan.getPesan();
                        }
                    } else {
                        keterangan = "Kode Penjualan, Nama Customer dan kode barang tidak boleh kosong";
                    }
                } else if (tombol.equals("Hapus")) {
                    if (!namaCustomer.equals("") && !kodeBarang.equals("")) {
                        if (penjualan.hapus(kodePenjualan, kodeBarang)) {
                            kodePenjualan="";
                            namaCustomer = "";
                            tanggal = sdf.format(tgl);
                            kasir = userName;
                            kodeBarang = "";
                            namaBarang = "";
                            harga = "0";
                            qty = "0";
                            keterangan = "Sudah dihapus";
                        } else {
                            keterangan = "Gagal menghapus:\n" + penjualan.getPesan();
                        }
                    } else {
                        keterangan = "Kode Penjualan, Nama Customer dan kode barang tidak boleh kosong";
                    }
                }else if(tombol.equals("Reset")){
                            kodePenjualan="";
                            namaCustomer = "";
                            tanggal = sdf.format(tgl);
                            kasir = userName;
                            kodeBarang = "";
                            namaBarang = "";
                            harga = "0";
                            qty = "0";
                    
                }
            }

            String konten = "<h2>Input Data Penjualan</h2>";
            konten += "<form action='PenjualanController' method='post'>";
            konten += "<table>";
            konten += "<tr>";
            konten += "<td align='right'><b>No. Penjualan</td>";
            konten += "<td align='left'><input type='text'  value='" + kodePenjualan + "' maxlength='15' name='kodePenjualan' style='width: 120px'><br></b></td>";
            konten += "</tr>";
            konten += "<tr>";
            konten += "<td align='right'>Nama Customer</td>";
            konten += "<td align='left'><input type='text' value='" + namaCustomer + "' name='namaCustomer' maxlength='15' style='width: 120px'>";
            konten += "<input type='submit' name='tombolCustomer' value='Cari'><input type='submit' name='tombolCustomer' value='Lihat'></td>";
            konten += "</tr>";
            konten += "<tr>";
            konten += "<td align='right'>Tanggal</td>";
            konten += "<td align='left'><input type='date' readonly value='" + tanggal + "' name='tanggal' style='width: 220px'></td>";
            konten += "</tr>";
            konten += "<tr>";
            konten += "<td align='right'>Kasir</td>";
            konten += "<td align='left'><input type='text' readonly value='" + kasir + "' name='kasir' style='width: 220px'></td>";
            konten += "</tr>";

            if (!tombolCustomer.equals("")) {
                if (!keterangan.equals("<br>")) {
                    konten += "<tr>";
                    konten += "<td colspan='2'><b>" + keterangan.replaceAll("\n", "<br>").replaceAll(";", ",") + "</b></td>";
                    konten += "</tr>";
                }
                konten += kontenLihat;
            }


            konten += "<tr>";
            konten += "<td align='right'>Kode Barang</td>";
            konten += "<td align='left'><input type='text' value='" + kodeBarang + "' name='kodeBarang' maxlength='15' style='width: 120px'>";
            konten += "<input type='submit' name='tombolDataBarang' value='Cari'><input type='submit' name='tombolDataBarang' value='Lihat'></td>";
            konten += "</tr>";
            konten += "<tr>";
            konten += "<td align='right'>Nama Barang</td>";
            konten += "<td align='left'><input type='text' readonly value='" + namaBarang + "' name='namaBarang' style='width: 220px'></td>";
            konten += "</tr>";
            konten += "<tr>";
            konten += "<td align='right'>Harga Rp.</td>";
            konten += "<td align='left'><input type='number' readonly value='" + harga + "' name='harga' style='width: 220px'></td>";
            konten += "</tr>";

            if (!tombolDataBarang.equals("")) {
                if (!keterangan.equals("<br>")) {
                    konten += "<tr>";
                    konten += "<td colspan='2'><b>" + keterangan.replaceAll("\n", "<br>").replaceAll(";", ",") + "</b></td>";
                    konten += "</tr>";
                }
                konten += kontenLihat;
            }

            konten += "<tr>";
            konten += "<td align='right'>Qty</td>";
            konten += "<td align='left'><input type='number' min='0' value='" + qty + "' name='qty' style='width: 50px'>"
                    + "</td>";
            int nilaiQty = 0;
            long nilaiHarga = 0;
            try {
                nilaiQty = Integer.parseInt(qty);
            } catch (NumberFormatException ex) {
            }
            try {
                nilaiHarga = Long.parseLong(harga);
            } catch (NumberFormatException ex) {
            }
            if (tombolHitung.equalsIgnoreCase("Hitung")) {
                total = nilaiHarga * nilaiQty;
            }

            konten += "</tr>";
            konten += "<tr>";
            konten += "<td align='right'>Total Rp.</td>";

            konten += "<td align='left'><input type='number' min='0'  readonly value='" + Long.toString(total) + "' style='width: 150px'>"
                    + "<input type='submit' name='hitung' value='Hitung'>"
                    + "</td>";
            konten += "</tr>";

            konten += "<tr>";

            konten += "<tr>";
            konten += "<td colspan='2' align='center'>";
            konten += "<table>";
            konten += "<tr>";
            konten += "<td align='center'><input type='submit' name='tombol' value='Simpan' style='width: 100px'></td>";
            konten += "<td align='center'><input type='submit' name='tombol' value='Hapus' style='width: 100px'></td>";
            konten += "</tr>";
            konten += "<tr>";
            konten += "<td  colspan='2' align='center'><input type='submit' value='Reset' name='tombol' style='width: 100px'></td>";
            konten += "</tr>";
            konten += "</table>";
            konten += "</td>";
            konten += "</tr>";

            if (!tombol.equals("") && !keterangan.equals("<br>")) {
                konten += "<tr>";
                konten += "<td colspan='2'><b>" + keterangan.replaceAll("\n", "<br>").replaceAll(";", ",") + "</b></td>";
                konten += "</tr>";
            }

            konten += "</table>";
            konten += "</form>";
            konten += "<br>";

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
