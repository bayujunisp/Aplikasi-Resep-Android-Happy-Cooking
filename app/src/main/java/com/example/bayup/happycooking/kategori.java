package com.example.bayup.happycooking;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

import static com.example.bayup.happycooking.konfigurasi.id_resep;

public class kategori extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public String kategori;
    private ListView listView;
    private String JSON_STRING;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user user = new user();
        kategori = user.getkategori();

        setTitle(user.getkategori());

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getResep();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent pulang = new Intent(kategori.this,MainActivity.class);
                startActivity(pulang);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showResep(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){

                JSONObject jo = result.getJSONObject(i);
                String id_resep = jo.getString(konfigurasi.TAG_id_resep);
                String gambar = jo.getString(konfigurasi.TAG_GAMBAR);
                String judul = jo.getString(konfigurasi.TAG_JUDUL);
                String kategori = jo.getString(konfigurasi.TAG_KATEGORI);

                HashMap<String,String> resep = new HashMap<>();
                resep.put(konfigurasi.TAG_id_resep,id_resep);
                resep.put(konfigurasi.TAG_GAMBAR,gambar);
                resep.put(konfigurasi.TAG_JUDUL,judul);
                resep.put(konfigurasi.TAG_KATEGORI,kategori);
                list.add(resep);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ListAdapter adapter = new MyAdapter(
                kategori.this, list, R.layout.list_item,
                new String[]{konfigurasi.TAG_id_resep, konfigurasi.TAG_GAMBAR, konfigurasi.TAG_JUDUL},
                new int[]{R.id.id_resep, R.id.gambar, R.id.judul});

        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, detailresep.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String Id = map.get(konfigurasi.TAG_id_resep).toString();
        user asal = new user();
        asal.setasal("kategori");
        intent.putExtra(konfigurasi.id_resep, Id);
        startActivity(intent);
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
        class GetResep extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showResep();

            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String kat = rh.sendGetRequestParam(konfigurasi.URL_GET_KAT,kategori);
                return kat;

            }
        }
        GetResep gbt = new GetResep();
        gbt.execute();
    }
}
