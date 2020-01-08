package com.example.pklapps;

public class Api {

    public static final String ROOT = "http://pkl-fk.000webhostapp.com";

    //LOGIN
    public static final String URL_LOGIN= ROOT + "/Android_login/Api.php?apicall=login";

    //CRUD
    private static final String ROOT_URL = ROOT + "/k i.php?apicall=";
    public static final String URL_CREATE_KELAS = ROOT_URL + "createkelas";
    public static final String URL_READ_KELAS = ROOT_URL + "getkelas&nim_mahasiswa=";
    public static final String URL_UPDATE_KELAS = ROOT_URL + "updatekelas";
    public static final String DELETE = "&nim_mahasiswa=";
    public static final String STAT = "&status_pinjam=";
    public static final String URL_DELETE_KELAS = ROOT_URL + "deletekelas&id_peminjaman=";

    //Spinner
    public static final String SpinnerKelas = ROOT + "/kelas/daftarkelas/getdata.php";
    public static final String SpinnerJam = ROOT + "/kelas/daftarkelas/getdataJam.php";
    public static final String KelasTableArray = "nama_ruangan";
    public static final String namaKelas  = "nama_ruangan";
    public static final String JSON_ARRAY = "result";
    public static final String JamArray = "list_waktu";
    public static final String jam  = "list_waktu";
    public static final String JSON_ARRAY_JAM = "resultJam";

    //LIST PINJAM KELAS SEMUA
    public static final String URL_READ_KELAS_PINJAM = ROOT_URL + "getkelaspinjam&status_pinjam=Booked";
}
