package com.example.modernfurniture;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class AdminDashboard extends AppCompatActivity {
    String contact[];
    String name[];
    String email[];
    String uid11[];
    ListView lst1;
    public RecyclerView mRecycleView;
    public RecyclerView.LayoutManager mManager;
    private FirebaseFirestore db;
    EditText search;
    CharSequence searchC = "";
    ArrayList<getUserData> list = new ArrayList<>();
    ArrayList<getUserData> filteredList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_report);
        lst1=(ListView)findViewById(R.id.lst);


        try{
           // SharedPreferences shr= PreferenceManager.getDefaultSharedPreferences(cart.this);
            //String uid=shr.getString("uid","NA");

            GetData gettrans = new GetData();
            DbParameter host = new DbParameter();
            String url = host.getHostpath();
            //  String url="http://mahavidyalay.in/Academic2021/FraudAppDetection/Demo.php";
            url = url + "ViewCartAdmin.php";
            // url = url + "pass=" + URLEncoder.encode(pass)    + "&";
            gettrans.execute(url);

        }catch(Exception e){
            Toast.makeText(AdminDashboard.this, "", Toast.LENGTH_SHORT).show();
        }


        lst1.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                UpdateData gettrans = new UpdateData();
                DbParameter host = new DbParameter();
                String url = host.getHostpath();


                url = url + "UpdateCart.php?uid="+ URLEncoder.encode(uid11[position])+"&";

                gettrans.execute(url);


            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.Anavigation);
        bottomNavigationView.setSelectedItemId(R.id.ureport);
        //navigation bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.addP:
                        startActivity(new Intent(getApplicationContext(),ViewUsers.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.order:
                        startActivity(new Intent(getApplicationContext(),ViewDisptachProduct.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.log:
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.ureport:
                        return true;

                }
                return false;
            }
        });



}



    private class GetData extends AsyncTask<String, Integer, String> {
        private ProgressDialog progress = null;
        String out = "";
        int count = 0;

        @Override
        protected String doInBackground(String... geturl) {

            try {
                //	String url= ;
                HttpClient http = new DefaultHttpClient();
                HttpPost http_get = new HttpPost(geturl[0]);
                HttpResponse response = http.execute(http_get);
                HttpEntity http_entity = response.getEntity();
                BufferedReader br = new BufferedReader(new InputStreamReader(http_entity.getContent()));
                out = br.readLine();

            } catch (Exception e) {

                out = e.toString();
            }
            return out;
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(AdminDashboard.this, null, "Please Wait...");

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            try {

                //Toast.makeText(ApproveStudent.this,""+out, Toast.LENGTH_LONG).show();

                JSONObject jsonResponse = new JSONObject(out);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("user_info");
                int arraylength = jsonMainNode.length();
                contact = new String[arraylength];
                name = new String[arraylength];
                email = new String[arraylength];
                uid11 = new String[arraylength];

                if (arraylength == 0) {
                    Toast.makeText(AdminDashboard.this, "Invalid Username Password", Toast.LENGTH_LONG);
                } else {

                    String uid = "";
                    String uname = "";

                    for (int i = 0; i < jsonMainNode.length(); i++) {

                        // Problability getting
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        uid11[i] = "" + jsonChildNode.optString("cid");
                        name[i] = "" + jsonChildNode.optString("UName");
                        contact[i] = "Address " + jsonChildNode.optString("Addresss");
                        email[i] = "Quantity " + jsonChildNode.optString("Quantity");


                    }

                    //Toast.makeText(Login.this,"Uid"+uid,Toast.LENGTH_LONG).show();

                }
                LevelAdapter2 lv1 = new LevelAdapter2(AdminDashboard.this, name, contact, email);
                lst1.setAdapter(lv1);

            } catch (Exception e) {
                Toast.makeText(AdminDashboard.this, " ", Toast.LENGTH_LONG).show();
            }


            progress.dismiss();
        }
    }




    private class UpdateData extends AsyncTask<String, Integer, String> {
        private ProgressDialog progress = null;
        String out = "";
        int count = 0;

        @Override
        protected String doInBackground(String... geturl) {

            try {
                //	String url= ;
                HttpClient http = new DefaultHttpClient();
                HttpPost http_get = new HttpPost(geturl[0]);
                HttpResponse response = http.execute(http_get);
                HttpEntity http_entity = response.getEntity();
                BufferedReader br = new BufferedReader(new InputStreamReader(http_entity.getContent()));
                out = br.readLine();

            } catch (Exception e) {

                out = e.toString();
            }
            return out;
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(AdminDashboard.this, null, "Please Wait...");

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            try {


                Toast.makeText(AdminDashboard.this,"Order Dispatch Successfully",Toast.LENGTH_LONG).show();

                Intent i1= new Intent(AdminDashboard.this,AdminDashboard.class);
                startActivity(i1);
                finish();


            } catch (Exception e) {
                Toast.makeText(AdminDashboard.this, " "+e, Toast.LENGTH_LONG).show();
            }


            progress.dismiss();
        }
    }
}
