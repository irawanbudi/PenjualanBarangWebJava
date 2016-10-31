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
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import com.o5tplen.view.PesanDialog;
import java.io.ByteArrayOutputStream;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

/**
 *
 * @author Irawan
 */
public class DataBarang {

    private String kodeBarang, namaBarang, satuanBarang;
    private int stokBarang;
    private long hargaBarang;
    private String pesan;
    private Object[][] list;
    private final koneksi conn = new koneksi();
    private final PesanDialog pesanDialog = new PesanDialog();
    private byte[] pdfasbytes;

    public byte[] getPdfasbytes() {
        return pdfasbytes;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getSatuanBarang() {
        return satuanBarang;
    }

    public void setSatuanBarang(String satuanBarang) {
        this.satuanBarang = satuanBarang;
    }

    public int getStokBarang() {
        return stokBarang;
    }

    public void setStokBarang(int stokBarang) {
        this.stokBarang = stokBarang;
    }

    public long getHargaBarang() {
        return hargaBarang;
    }

    public void setHargaBarang(long hargaBarang) {
        this.hargaBarang = hargaBarang;
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

    public boolean baca(String kodeBarang) {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = conn.getConnection()) != null) {
            Statement sta;
            ResultSet rset;

            try {
                String SQLStatemen = "select * from barang where kode='" + kodeBarang + "'";
                sta = connection.createStatement();
                rset = sta.executeQuery(SQLStatemen);

                rset.next();
                if (rset.getRow() > 0) {
                    this.kodeBarang = rset.getString("kode");
                    this.namaBarang = rset.getString("nama");
                    this.hargaBarang = rset.getLong("harga");
                    this.stokBarang = rset.getInt("stok");
                    this.satuanBarang = rset.getString("satuan");
                } else {
                    adaKesalahan = true;
                    pesan = "Kode barang \"" + kodeBarang + "\" tidak ditemukan";
                }

                sta.close();
                rset.close();
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel barang\n" + ex;
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
                SQLStatemen = "select kode,nama,harga,stok,satuan from barang";
                sta = connection.createStatement();
                rset = sta.executeQuery(SQLStatemen);

                rset.next();
                rset.last();
                list = new Object[rset.getRow()][5];
                if (rset.getRow() > 0) {
                    rset.first();
                    int i = 0;
                    do {
                        list[i] = new Object[]{rset.getString("kode"), rset.getString("nama"),
                            rset.getInt("stok"), rset.getString("satuan")};
                        i++;
                    } while (rset.next());
                }

                sta.close();
                rset.close();
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel barang" + ex;
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
                String SQLStatemen = "select * from barang where kode='" + kodeBarang + "'";
                Statement sta = connection.createStatement();
                ResultSet rset = sta.executeQuery(SQLStatemen);

                rset.next();
                if (rset.getRow() > 0) {

                    try{
                    simpan = true;
                        SQLStatemen = "update barang set nama='" + namaBarang + "', harga='" + hargaBarang
                                + "', stok='" + stokBarang + "', satuan='" + satuanBarang + "' where kode='" + kodeBarang + "'";
                        sta = connection.createStatement();
                        jumlahSimpan = sta.executeUpdate(SQLStatemen);
                    }catch(SQLException ex){}
                    
                } else {
                    simpan = true;
                    SQLStatemen = "insert into barang(kode, nama, harga, stok, satuan) values ('"
                            + kodeBarang + "','" + namaBarang + "','" + hargaBarang + "','" + stokBarang + "','" + satuanBarang + "')";
                    sta = connection.createStatement();
                    jumlahSimpan = sta.executeUpdate(SQLStatemen);
                }

                if (simpan) {
                    if (jumlahSimpan < 1) {
                        adaKesalahan = true;
                        pesan = "Gagal menyimpan data barang";
                    }
                }

                sta.close();
                rset.close();
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel barang\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + conn.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

    public boolean hapus(String kodeBarang) {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = conn.getConnection()) != null) {
            int jumlahHapus;
            Statement sta;

            try {
                String SQLStatemen = "delete from barang where kode='" + kodeBarang + "'";
                sta = connection.createStatement();
                jumlahHapus = sta.executeUpdate(SQLStatemen);

                if (jumlahHapus < 1) {
                    pesan = "Data barang dengan kode " + kodeBarang + " tidak ditemukan";
                    adaKesalahan = true;
                }

                sta.close();
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel barang\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + conn.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

    public boolean cetakLaporanStokBarang() {
        boolean adaKesalahan = false;

        Connection connection;

        if ((connection = conn.getConnection()) != null) {
            String SQLStatement;
            ResultSet resultSet = null;

            try {
                Statement statement = connection.createStatement();

                SQLStatement = "SELECT\n"
                        + "\n"
                        + "     barang.`nama` AS barang_nama,\n"
                        + "     barang.`harga` AS barang_harga,\n"
                        + "     barang.`stok` AS barang_stok,\n"
                        + "     barang.`satuan` AS barang_satuan,\n"
                        + "     barang.`kode` AS barang_kode\n"
                        + "FROM\n"
                        + "     `barang` barang\n"
                        + "ORDER BY\n"
                        + "`barang`.`stok` ASC,\n"
                        + "`barang`.`kode` ASC";
                
                System.out.println("Query: " + SQLStatement);
                resultSet = statement.executeQuery(SQLStatement);

            } catch (SQLException ex) {
                pesan = "Tidak dapat membaca data\n" + ex;
            }

            if (resultSet != null) {
                try {
                    JasperDesign disain = JRXmlLoader.load("src/report/stokbarang.jrxml");
                    JasperReport stokBarangLaporan = JasperCompileManager.compileReport(disain);
                    JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(resultSet);
                    System.out.println("Cetak Laporan");
                    JasperPrint cetak = JasperFillManager.fillReport(stokBarangLaporan, new HashMap(), resultSetDataSource);
                    JasperViewer.viewReport(cetak, false);
                } catch (JRException ex) {
                    pesan = "Tidak dapat mencetak laporan\n" + ex;
                }
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + conn.getPesanKesalahan();
        }

        return !adaKesalahan;
    }
    public boolean cetakLaporanStokBarang(String fileExt, String namaFile) {
        boolean adaKesalahan = false;
        Connection connection;
        pdfasbytes = null;

        if ((connection = conn.getConnection()) != null) {
            String SQLStatement = null;
            ResultSet resultSet = null;

            try {
                Statement statement = connection.createStatement();

                SQLStatement =  "SELECT\n"
                        + "\n"
                        + "     barang.`nama` AS barang_nama,\n"
                        + "     barang.`harga` AS barang_harga,\n"
                        + "     barang.`stok` AS barang_stok,\n"
                        + "     barang.`satuan` AS barang_satuan,\n"
                        + "     barang.`kode` AS barang_kode\n"
                        + "FROM\n"
                        + "     `barang` barang\n"
                        + "ORDER BY\n"
                        + "`barang`.`stok` ASC,\n"
                        + "`barang`.`kode` ASC";

                resultSet = statement.executeQuery(SQLStatement);
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membaca data\n" + ex;
            }

            if ((!adaKesalahan) && (resultSet != null)) {
                try {
                    JasperDesign disain = JRXmlLoader.load(namaFile);
                    JasperReport nilaiLaporan = JasperCompileManager.compileReport(disain);
                    JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(resultSet);
                    JasperPrint cetak = JasperFillManager.fillReport(nilaiLaporan, new HashMap(), resultSetDataSource);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    JRExporter exporter = null;
                    if (fileExt.equalsIgnoreCase("PDF")) {
                        exporter = new JRPdfExporter();
                    } else if (fileExt.equalsIgnoreCase("XLSX")) {
                        exporter = new JRXlsxExporter();
                    } else if (fileExt.equalsIgnoreCase("XLS")) {
                        exporter = new JRXlsExporter();
                    } else if (fileExt.equalsIgnoreCase("DOCX")) {
                        exporter = new JRDocxExporter();
                    } else if (fileExt.equalsIgnoreCase("ODT")) {
                        exporter = new JROdtExporter();
                    } else if (fileExt.equalsIgnoreCase("RTF")) {
                        exporter = new JRRtfExporter();
                    } else {
                        adaKesalahan = true;
                        pesan = "Format file dengan ektensi " + fileExt + " tidak terdaftar";
                    }

                    if (!adaKesalahan && (exporter != null)) {
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, cetak);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
                        exporter.exportReport();
                        pdfasbytes = byteArrayOutputStream.toByteArray();
                    }
                } catch (JRException ex) {
                    adaKesalahan = true;
                    pesan = "Tidak dapat mencetak laporan\n" + ex;
                }
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + conn.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

}
