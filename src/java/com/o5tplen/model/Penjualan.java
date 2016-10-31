/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.o5tplen.model;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author Irawan
 */
public class Penjualan {

    private DataBarang dataBarang = new DataBarang();
    private String kodePenjualan;
    private int idCustomer;
    private Date tanggal = new Date();
    private String pesan;
    private Object[][] listPenjualan;
    private byte[] pdfasbytes;
    private final SimpleDateFormat bentuk = new SimpleDateFormat("yyyy-MM-dd");
    private final koneksi conn = new koneksi();
    private String tanggalStr = bentuk.format(tanggal);

    public String getKodePenjualan() {
        return kodePenjualan;
    }

    public void setKodePenjualan(String kodePenjualan) {
        this.kodePenjualan = kodePenjualan;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public Object[][] getListPenjualan() {
        return listPenjualan;
    }

    public void setListPenjualan(Object[][] listPenjualan) {
        this.listPenjualan = listPenjualan;
    }

    public byte[] getPdfasbytes() {
        return pdfasbytes;
    }

    public boolean simpan() {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = conn.getConnection()) != null) {
            int jumlahSimpan = 0;

            String kode = listPenjualan[0][4].toString();
            int stok = (int) listPenjualan[0][5],
                    idCustomer=(int)listPenjualan[0][2];

            String SQLStatemen;
            Statement sta;
            /*
            try {
                SQLStatemen = "update penjualan set kodeBarang='" + kode + "', qty='" + listPenjualan[0][5]
                        + "', tanggal='" + tanggalStr + "', kasir='" 
                        + listPenjualan[0][3] + "' where kodepenjualan='" + kodePenjualan + "'";
                sta = connection.createStatement();
                jumlahSimpan = sta.executeUpdate(SQLStatemen);
            } catch (SQLException ex) {
            }
             */
            if (jumlahSimpan < 1) {
                try {
                    SQLStatemen = "insert into penjualan (`kodepenjualan`, "
                            + "`tanggal`, `idcustomer`, `kasir`, `kodebarang`,"
                            + " `qty`) values ('" + kodePenjualan + "','"
                            + tanggalStr + "','" + idCustomer + "','" + listPenjualan[0][3] + "','"
                            + listPenjualan[0][4] + "','" + listPenjualan[0][5] + "')";
                    sta = connection.createStatement();
                    jumlahSimpan += sta.executeUpdate(SQLStatemen);
                } catch (SQLException ex) {
                }
                if (dataBarang.baca(kode)) {
                    stok = dataBarang.getStokBarang() - stok;

                    try {
                        SQLStatemen = "update `barang` set `stok`=" + stok
                                + "  where `kode`='" + kode + "'";
                        System.out.println("update tabel barang: " + SQLStatemen);
                        sta = connection.createStatement();
                        stok = sta.executeUpdate(SQLStatemen);
                        System.out.println(stok);

                    } catch (SQLException e) {
                    }
                }

            }

            if (jumlahSimpan > 0) {
                adaKesalahan = false;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + conn.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

    public boolean baca(String kodePenjualan, String kodeBarang) {
        boolean adaKesalahan = false;
        Connection connection;

        this.kodePenjualan = kodePenjualan;

        listPenjualan = null;

        if ((connection = conn.getConnection()) != null) {
            String SQLStatemen;
            Statement sta;
            ResultSet rset;
            if (kodeBarang != null || kodeBarang.length() > 0) {
                SQLStatemen = "select * from penjualan where kodepenjualan='" + kodePenjualan
                        + "' and kodebarang='" + kodeBarang + "'";
            } else {
                SQLStatemen = "select * from penjualan where kodepenjualan='" + kodePenjualan + "'";
            }
            try {
                sta = connection.createStatement();
                rset = sta.executeQuery(SQLStatemen);

                rset.next();
                rset.last();
                listPenjualan = new Object[rset.getRow()][6];

                rset.first();
                int i = 0;
                do {
                    if (!rset.getString("kodepenjualan").equals("")) {
                        listPenjualan[i] = new Object[]{rset.getString("kodepenjualan"),
                            rset.getDate("tanggal"),
                            rset.getInt("idcustomer"),
                            rset.getString("kasir"),
                            rset.getString("kodebarang"),
                            rset.getInt("qty")};
                    }
                    i++;
                } while (rset.next());

                if (listPenjualan.length > 0) {
                    adaKesalahan = false;
                }

                sta.close();
                rset.close();
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membaca data penjualan\n" + ex.getMessage();
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + conn.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

    public boolean cetakLaporan(String opsi, String kodePenjualan, String awalStr, String akhirStr, String fileExt, String namaFile) {
        boolean adaKesalahan = false;
        Connection connection;
        pdfasbytes = null;

        if ((connection = conn.getConnection()) != null) {
            String SQLStatement = null;
            ResultSet resultSet = null;

            try {
                Statement statement = connection.createStatement();

                SQLStatement = "SELECT"
                        + "     penjualan.`kodepenjualan` AS penjualan_kodepenjualan,"
                        + "     penjualan.`tanggal` AS penjualan_tanggal,"
                        + "     penjualan.`idcustomer` AS penjualan_idcustomer,"
                        + "     penjualan.`kasir` AS penjualan_kasir,"
                        + "     penjualan.`kodebarang` AS penjualan_kodebarang,"
                        + "     penjualan.`qty` AS penjualan_qty,"
                        + "     customer.`nama` AS customer_nama,"
                        + "     barang.`nama` AS barang_nama,"
                        + "     barang.`harga` AS barang_harga,"
                        + "     barang.`stok` AS barang_stok,"
                        + "     barang.`satuan` AS barang_satuan,"
                        + "     customer.`alamat` AS customer_alamat,"
                        + "     customer.`telp` AS customer_telp,"
                        + " (barang.`harga` * penjualan.`qty`) AS penjualan_jumlah "
                        + "FROM"
                        + "     `penjualan` penjualan INNER JOIN `customer` customer ON penjualan.`idcustomer` = customer.`id`"
                        + "     INNER JOIN `barang` barang ON penjualan.`kodebarang` = barang.`kode`";

                if (opsi.equals("kodepenjualan")) {
                    SQLStatement = SQLStatement + " where penjualan.`kodepenjualan`='" + kodePenjualan + "'";
                } else if (opsi.equals("tanggal")) {
                    SQLStatement = SQLStatement + " where DATE_FORMAT(penjualan.`tanggal`,'%Y-%m-%d')>='" + awalStr + "' AND "
                            + "DATE_FORMAT(penjualan.`tanggal`,'%Y-%m-%d')<='" + akhirStr + "'";
                }

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

    public boolean hapus(String kodePenjualan, String kodeBarang) {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = conn.getConnection()) != null) {
            int jumlahHapus;
            Statement sta;

            try {
                String SQLStatemen = "delete from penjualan where kodepenjualan='" + kodePenjualan + "' and kodebarang='" + kodeBarang + "'";
                sta = connection.createStatement();
                jumlahHapus = sta.executeUpdate(SQLStatemen);

                if (jumlahHapus < 1) {
                    pesan = "Data penjualan untuk kode penjualan " + kodePenjualan + " dan kode barang  " + kodeBarang + " tidak ditemukan";
                    adaKesalahan = true;
                }

                sta.close();
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel penjualan\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + conn.getPesanKesalahan();
        }

        return !adaKesalahan;
    }
}
