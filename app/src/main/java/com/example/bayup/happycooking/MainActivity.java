package com.example.bayup.happycooking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    private ListView listView;
    private String JSON_STRING;

    ImageView gambar;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getResep();

        user asal = new user();
        asal.setasal("main");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        user usr = new user();

        if (id == R.id.nav_ayam) {
            Intent intent = new Intent(MainActivity.this, kategori.class);
            usr.setkategori("AYAM");
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_daging) {
            Intent intent = new Intent(MainActivity.this, kategori.class);
            usr.setkategori("DAGING");
            startActivity(intent);

        } else if (id == R.id.nav_ikan) {
            Intent intent = new Intent(MainActivity.this, kategori.class);
            usr.setkategori("IKAN");
            startActivity(intent);

        } else if (id == R.id.nav_tahu) {
            Intent intent = new Intent(MainActivity.this, kategori.class);
            usr.setkategori("TAHU");
            startActivity(intent);

        } else if (id == R.id.nav_sayuran) {
            Intent intent = new Intent(MainActivity.this, kategori.class);
            usr.setkategori("SAYURAN");
            startActivity(intent);

        } else if (id == R.id.nav_sambal) {
            Intent intent = new Intent(MainActivity.this, kategori.class);
            usr.setkategori("SAMBAL");
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //onclick listview
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, detailresep.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String Id = map.get(konfigurasi.TAG_id_resep).toString();
        intent.putExtra(konfigurasi.id_resep, Id);
        startActivity(intent);
    }

    private void showResep(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id_berita = jo.getString(konfigurasi.TAG_id_resep);
                String gambar = jo.getString(konfigurasi.TAG_GAMBAR);
                String judul = jo.getString(konfigurasi.TAG_JUDUL);
                String kategori = jo.getString(konfigurasi.TAG_KATEGORI);

                HashMap<String,String> news = new HashMap<>();
                news.put(konfigurasi.TAG_id_resep,id_berita);
                news.put(konfigurasi.TAG_GAMBAR,gambar);
                news.put(konfigurasi.TAG_JUDUL,judul);
                news.put(konfigurasi.TAG_KATEGORI,kategori);
                list.add(news);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ListAdapter adapter = new MyAdapter(
                MainActivity.this, list, R.layout.list_item,
                new String[]{konfigurasi.TAG_id_resep, konfigurasi.TAG_GAMBAR, konfigurasi.TAG_JUDUL},
                new int[]{R.id.id_resep, R.id.gambar, R.id.judul});

        listView.setAdapter(adapter);
    }
    public class MyAdapter extends SimpleAdapter {

        public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to){
            super(context, data, resource, from, to);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            // here you let SimpleAdapter built the view normally.
            View v = super.getView(position, convertView, parent);

            // Then we get reference for Picasso
            ImageView img = (ImageView) v.getTag();
            if(img == null){
                img = (ImageView) v.findViewById(R.id.gambar);
                v.setTag(img); // <<< THIS LINE !!!!
            }
            // get the url from the data you passed to the `Map`
            String url = (String) ((Map)getItem(position)).get(konfigurasi.TAG_GAMBAR);
            // do Picasso
            Picasso.with(v.getContext()).load(url).into(img);

            // return the view
            return v;
        }
    }

    private void getResep(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showResep();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(konfigurasi.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


}
