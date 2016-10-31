<%-- 
    Document   : index
    Created on : Oct 26, 2016, 3:12:46 PM
    Author     : Irawan
--%>

<link href='style.css' rel='stylesheet' type='text/css' />
<title>Aplikasi Penjualan Barang - Web</title>
    </head>
    <body bgcolor="#808080">
        <%
            String menu="<br><b>Master Data</b><br>"
                    + "<a href=DataBarangController>Data Barang</a><br>"
                    + "<a href=DataCustomerController>Data Customer</a><br>"
                    + "<a href=DataUserController>Data User</a><br><br>"
                    + "<b>Transaksi</b><br>"
                    + "<a href=PenjualanController>Penjualan</a><br><br>"
                    + "<b>Laporan</b><br>"
                    + "<a href=LaporanPenjualanController>Penjualan</a><br>"
                    + "<a href=StokBarangController>Stok Barang</a><br><br>"
                    + "<a href=LoginController>Login</a><br><br>";
            
            String topMenu="<nav><ul>"
                    +"<li><a href=.>Home</a></li>"
                    +"<li class='dropdown'><a href=# class='dropbtn'>Master Data</a>"
                    +"<div class='dropdown-content'>"
                    +"<a href=DataBarangController>Data Barang</a>"
                    +"<a href=DataCustomerController>Data Customer</a>"
                    + "<a href=DataUserController>Data User</a>"
                    +"</div>"
                    +"</li>"
                    +"<li class='dropdown'><a href=# class='dropbtn'>Transaksi</a>"
                    +"<div class='dropdown-content'>"
                    +"<a href=PenjualanController>Penjualan</a>"
                    +"</div>"
                    +"</li>"
                    +"<li class='dropdown'><a href=# class='dropbtn'>Laporan</a>"
                    +"<div class='dropdown-content'>"
                    +"<a href=LaporanPenjualanController>Penjualan</a>"
                    + "<a href=StokBarangController>Stok Barang</a>"
                    +"</div>"
                    +"</li>"
                    +"<li><a href=LoginController style='text-align:right'>Login</a></li>"
                    + "<li><a href=Deskripsi>Deskripsi</a></li>"
                    +"</ul>"
                    +"</nav>";
            
            String konten="<br><h1>Selamat Datang</h1>";
            String userName="";
            
            if (!session.isNew()){
                try {
                    userName = session.getAttribute("userName").toString();
                } catch (Exception ex){}
                
                if (!((userName == null) || userName.equals(""))){
                    konten += "<h2>"+userName+"</h2>";
                    
                    try {
                        menu = session.getAttribute("menu").toString();
                    } catch (Exception ex){}
                    
                    try {
                        topMenu = session.getAttribute("topMenu").toString();
                    } catch (Exception ex){}
                }
            }
        %>
    <center>
        <table width="80%" bgcolor="#eeeeee">   
            <tr>
                <td colspan="2" align="center">
                    <br>
                    <h2 Style="margin-bottom:0px; margin-top:0px;">
                        Aplikasi Penjualan Barang - Web
                    </h2>
                    <h1 Style="margin-bottom:0px; margin-top:0px;">
                        TOKO 05TPLEN
                    </h1>
                    <h4 Style="margin-bottom:0px; margin-top:0px;">
                        Jl. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten
                    </h4>
                    <br>
                </td>
            </tr>
            <tr height="400">
                <td width="200" align="center" valign="top" bgcolor="#eeffee">
                    <br>
                <div id='menu'>
                    <%=menu %>
                </div>
                </td>
                <td align="center" valign="top" bgcolor="#ffffff">
                    <%=topMenu%>
                    <br>
                    <%=konten %>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center" bgcolor="#eeeeff">
                    <small>
                        Copyright &copy; 2016 TOKO 05TPLEN<br>
                        Jl. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten<br>                        
                    </small>
                </td>
            </tr>
        </table>
    </center>