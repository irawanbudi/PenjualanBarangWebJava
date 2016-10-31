/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.o5tplen.controller;

import com.o5tplen.model.Penjualan;
import com.o5tplen.view.MainForm;
import java.io.IOException;
import java.io.OutputStream;
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
@WebServlet(name = "LaporanPenjualanController", urlPatterns = {"/LaporanPenjualanController"})
public class LaporanPenjualanController extends HttpServlet {

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
String[][] formatTypeData = {{"PDF (Portable Document Format)","pdf","application/pdf"},
            {"XLSX (Microsoft Excel)","xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {"XLS (Microsoft Excel 97-2003)","xls","application/vnd.ms-excel"},
            {"DOCX (Microsoft Word)","docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {"ODT (OpenDocument Text)","odt","application/vnd.oasis.opendocument.text"},
            {"RTF (Rich Text Format)","rtf","text/rtf"}};
        
        Date tgl=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        HttpSession session = request.getSession(true);
        String userName = "";
        
        String tombol = request.getParameter("tombol");
        String opsi = request.getParameter("opsi");
        String kodePenjualan = request.getParameter("kodepenjualan");
        String awalStr = request.getParameter("awal");
        String akhirStr=request.getParameter("akhir");
        String kelas = request.getParameter("kelas");
        String formatType = request.getParameter("formatType");
                
        if (tombol==null) tombol="";
        if (kodePenjualan==null) kodePenjualan="";
        if (opsi==null) opsi="";
        if (awalStr==null) awalStr="0";
        if (akhirStr==null) akhirStr="0";
        if (formatType==null) kelas="";
        
        String keterangan = "<br>";
        int noType = 0;
        
        for (int i=0; i<formatTypeData.length; i++){
            if (formatTypeData[i][0].equals(formatType)){
                noType = i;
            }
        }
        
        try {
            userName = session.getAttribute("userName").toString();
        } catch (Exception ex){}
        
        if (!((userName == null) || userName.equals(""))){
            boolean opsiSelected = false;
            
            if (tombol.equals("Cetak")){
                Penjualan penjualan = new Penjualan();
                
                Date awal=new Date(), akhir=new Date();
                  
                if (penjualan.cetakLaporan(opsi, kodePenjualan, awalStr, akhirStr, formatTypeData[noType][1], getServletConfig().getServletContext().getRealPath("reports/Penjualan.jrxml"))){
                    byte[] pdfasbytes = penjualan.getPdfasbytes();
                    try (OutputStream outStream = response.getOutputStream()) {
                        response.setHeader("Content-Disposition","inline; filename=LaporanPenjualan."+formatTypeData[noType][1]);
                        response.setContentType(formatTypeData[noType][2]);
                        
                        response.setContentLength(pdfasbytes.length);
                        outStream.write(pdfasbytes,0,pdfasbytes.length);
                        
                        outStream.flush();
                        outStream.close();
                    }
                } else {
                    keterangan += penjualan.getPesan();
                }
            }
            
            String konten = "<h2>Mencetak Laporan Penjualan</h2>";
            konten += "<form action='LaporanPenjualanController' method='post'>";
            konten += "<table>";
            konten += "<tr>";
            if (opsi.equalsIgnoreCase("kodepenjualan")){
                konten += "<td align='right'><input type='radio' checked name='opsi' value='kodepenjualan'></td>";
                opsiSelected = true;
            } else {
                konten += "<td align='right'><input type='radio' name='opsi' value='kodepenjualan'></td>";
            }            
            konten += "<td align='left'>Kode Penjualan</td>";
            konten += "<td align='left'><input type='text' value='"+kodePenjualan+"' name='kodepenjualan' maxlength='15' size='15'></td>";
            konten += "</tr>";
            
            konten += "<tr>";
            if(opsi.equalsIgnoreCase("tanggal")){
                konten+="<td align='right'><input type='radio' checked name='opsi' value='tanggal'></td>";
                opsiSelected=true;
            }else{
                konten+="<td align='right'><input type='radio' name='opsi' value='tanggal'></td>";
            }
            konten+="<td align='left'>Tanggal</td>";
            konten+="<td align='left'>"
                    + "<input type='date' name='awal' value='"+sdf.format(tgl)+"'> - "
                    + "<input type='date' name='akhir' value='"+sdf.format(tgl)+"'>"
                    + "</td>";
            
            konten += "</tr>";
                
            konten += "<tr>";
            if (!opsiSelected){
                konten += "<td align='right'><input type='radio' checked name='opsi' value='Semua'></td>";
            } else {
                konten += "<td align='right'><input type='radio' name='opsi' value='Semua'></td>";
            }
            konten += "<td align='left'>Semua</td>";
            konten += "<td><br></td>";            
            konten += "</tr>";
            
            konten += "<tr>";
            konten += "<td colspan='3'><br></td>";
            konten += "</tr>";
            
            konten += "<tr>";
            konten += "<td>Format Laporan</td>";
            konten += "<td colspan=2>";
            konten += "<select name='formatType'>";
            for (String[] formatLaporan : formatTypeData){
                if (formatLaporan[0].equals(formatType)){
                    konten += "<option selected value='"+formatLaporan[0]+"'>"+formatLaporan[0]+"</option>";
                } else {
                    konten += "<option value='"+formatLaporan[0]+"'>"+formatLaporan[0]+"</option>";
                }
            }
            konten += "</select>";
            konten += "</td>";
            konten += "</tr>";
            
            konten += "<tr>";
            konten += "<td colspan='3'><b>"+keterangan.replaceAll("\n", "<br>").replaceAll(";", ",")+"</b></td>";
            konten += "</tr>";
            
            konten += "<tr>";
            konten += "<td colspan='3' align='center'><input type='submit' name='tombol' value='Cetak' style='width: 100px'></td>";
            konten += "</tr>";
            
            konten += "</table>";
            konten += "</form>";            
            
            new MainForm().tampilkan(konten, request, response);
        } else {
            response.sendRedirect(".");
        }    }

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
