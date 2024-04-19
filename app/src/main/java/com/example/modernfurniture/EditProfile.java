package com.example.modernfurniture;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class EditProfile extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText profileFullName,profileEmail,profilePhone;
    ImageView profileImageView;
    Button saveBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent data = getIntent();
        final String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");
        String phone = data.getStringExtra("phone");

        profileFullName = findViewById(R.id.profileFullName);
        profileEmail = findViewById(R.id.profileEmailAddress);
        profilePhone = findViewById(R.id.profilePhoneNo);
        profileImageView = findViewById(R.id.profileImageView);
        saveBtn = findViewById(R.id.saveProfileInfo);

        profileFullName.setText(fullName);
        profileEmail.setText(email);
        profilePhone.setText(phone);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences shr= PreferenceManager.getDefaultSharedPreferences(EditProfile.this);
                String uid=shr.getString("uid","NA");


                DbParameter db1 = new DbParameter();
                String url = db1.getHostpath();
                url = url + "UpdateProfile.php?name="+ URLEncoder.encode(profileFullName.getText().toString())
                        + "&cnum=" + URLEncoder.encode(profilePhone.getText().toString())
                        + "&uid=" + URLEncoder.encode(uid)
                        + "&email=" + URLEncoder.encode(profileEmail.getText().toString());

                //Toast.makeText(EditProfile.this,"Profile Updated Sucessfully",Toast.LENGTH_LONG).show();

               LoadData l1 = new LoadData();
                l1.execute(url);


            }
        });



        //Log.d(TAG, "onCreate: " + fullName + " " + email + " " + phone);
    }





    private class LoadData extends AsyncTask<String, Integer, String> {
        private ProgressDialog progress = null;
        String out="";
        int count=0;
        @Override
        protected String doInBackground(String... geturl) {


            try{
                //	String url= ;



                HttpClient http=new DefaultHttpClient();
                HttpPost http_get= new HttpPost(geturl[0]);
                HttpResponse response=http.execute(http_get);
                HttpEntity http_entity=response.getEntity();
                BufferedReader br= new BufferedReader(new InputStreamReader(http_entity.getContent()));
                out = br.readLine();

            }catch (Exception e){

                out= e.toString();
            }
            return out;
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(EditProfile.this, null,
                    "Updating Register Information...");

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            Toast.makeText(EditProfile.this, "Register Sucessfully"+result, Toast.LENGTH_LONG).show();
            progress.dismiss();
            finish();
            // String pass= pass11;
            //  SmsManager smgr = SmsManager.getDefault();
            // smgr.sendTextMessage(contact.getText().toString(),null,"user name: "+email.getText().toString()+" password: "+pass,null,null);
            //transactions.setText("Transactions :"+count);

            // Intent register_i1 = new Intent(AddUser.this,Login.class);
            //  startActivity(register_i1);
            // finish();
        }



    }


}
