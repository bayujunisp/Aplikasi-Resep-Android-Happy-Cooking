package com.example.bayup.happycooking;

/**
 * Created by bayup on 10-May-19.
 */

public class user {
    private static String kategori;
    private static String asal;

    // ini method setter
    public void setkategori(String kategori){

        user.kategori = kategori;
    }
    public void setasal(String asal){

        user.asal = asal;
    }

    // ini method getter
    public String getkategori(){

        return kategori;
    }

    public String getasal(){

        return asal;
    }
}
