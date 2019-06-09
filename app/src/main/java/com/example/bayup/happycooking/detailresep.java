package com.example.bayup.happycooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class detailresep extends AppCompatActivity {

    TextView txtjudul, txtisi, txtid_berita;

    ImageView imggambar;
    ScrollView detail;
    private String JSON_STRING;

    private String id;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailresep);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtid_berita = (TextView) findViewById(R.id.id_resep);
        txtjudul = (TextView) findViewById(R.id.judul);
        txtisi = (TextView) findViewById(R.id.isi);
        imggambar = (ImageView) findViewById(R.id.gambar);

        detail = (ScrollView) findViewById(R.id.detail);


        Intent intent = getIntent();

        id = intent.getStringExtra(konfigurasi.id_resep);
        getResep();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                user asal = new user();
                if (asal.getasal().equals("kategori")){
                    Intent pulang = new Intent(detailresep.this,kategori.class);
                    startActivity(pulang);
                }else {
                    Intent pulang = new Intent(detailresep.this, MainActivity.class);
                    startActivity(pulang);
                }

                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showResep(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String id_resep = c.getString(konfigurasi.TAG_id_resep);
            String gambar = c.getString(konfigurasi.TAG_GAMBAR);
            String judul = c.getString(konfigurasi.TAG_JUDUL);
            String isi = c.getString(konfigurasi.TAG_ISI);

            String plain = Html.fromHtml(isi).toString();

            setTitle(judul);
            txtjudul.setText(judul);
            txtisi.setText(plain);
            Picasso.with(detailresep.this).load(gambar).into(imggambar);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getResep(){
        class GetResep extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                detail.setVisibility(View.INVISIBLE);
                loading = ProgressDialog.show(detailresep.this,"Melihat Resep...","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                detail.setVisibility(View.VISIBLE);
                showResep(s);

            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String dtl = rh.sendGetRequestParam(konfigurasi.URL_GET_DTL,id);
                return dtl;

            }
        }
        GetResep ge = new GetResep();
        ge.execute();
    }
}
