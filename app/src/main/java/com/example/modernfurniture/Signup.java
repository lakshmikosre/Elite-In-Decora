package com.example.modernfurniture;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class Signup extends AppCompatActivity {
    EditText mFullName,mEmail,mPassword,mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFullName   = findViewById(R.id.name);
        mEmail      = findViewById(R.id.email);
        mPassword   = findViewById(R.id.pass);
        mPhone      = findViewById(R.id.mob);
        mRegisterBtn= findViewById(R.id.btnsignup);
        mLoginBtn   = findViewById(R.id.textViewLogin);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), profile.class));
            finish();
        }


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String fullName = mFullName.getText().toString();
                final String phone    = mPhone.getText().toString();

                if(TextUtils.isEmpty(fullName)){
                    mFullName.setError("Full Name is Required.");
                    return;
                }

                if(TextUtils.isEmpty(phone) || phone.length() != 10){
                    mPhone.setError("Phone number is Required.");
                    return;
                }

                if(TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }


                //if(status) {
                   // SharedPreferences shr= PreferenceManager.getDefaultSharedPreferences(NewRegistration.this);
                    //String id=shr.getString("id","Na");
                    // Toast.makeText(NewRegistration.this,"ID"+id,Toast.LENGTH_LONG).show();

                    DbParameter db1 = new DbParameter();
                    String url = db1.getHostpath();
                    url = url + "NewRegistration.php?name="+ URLEncoder.encode(fullName)
                            + "&cnum=" + URLEncoder.encode(phone)
                            + "&email=" + URLEncoder.encode(email)
                            + "&pass=" + URLEncoder.encode(password);

                    Toast.makeText(Signup.this,"Registration Done Sucessfully",Toast.LENGTH_LONG).show();

                    LoadData l1 = new LoadData();
                    l1.execute(url);
               // }


Intent i1= new Intent(Signup.this,Login.class);
startActivity(i1);
finish();



            }
        });



        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

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
            progress = ProgressDialog.show(Signup.this, null,
                    "Updating Register Information...");

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            Toast.makeText(Signup.this, "Register Sucessfully"+result, Toast.LENGTH_LONG).show();
            progress.dismiss();
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