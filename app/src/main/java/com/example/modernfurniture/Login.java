package com.example.modernfurniture;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class Login extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn,forgotTextLink, adminBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.pass);
        progressBar = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.btnlogin);
        mCreateBtn = findViewById(R.id.textViewSignUp);
        forgotTextLink = findViewById(R.id.forgotPass);
        adminBtn = findViewById(R.id.textViewAdmin);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(Login.this,Signup.class);
                startActivity(i1);
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

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


                try{
                    GetData gettrans = new GetData();
                    DbParameter host = new DbParameter();
                    String url = host.getHostpath();
                    //  String url="http://mahavidyalay.in/Academic2021/FraudAppDetection/Demo.php";
                    url = url + "Login.php?uname=" + URLEncoder.encode(email) + "&";
                    url = url + "pass=" + URLEncoder.encode(password) + "&";
                    gettrans.execute(url);

                }catch(Exception e){
                      Toast.makeText(Login.this,""+e,Toast.LENGTH_LONG).show();
                }



            }
        });



        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),admin_login.class));
            }
        });

        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });


    }





    private class GetData extends AsyncTask<String, Integer, String> {
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
            progress = ProgressDialog.show(Login.this, null,"Please Wait...");

            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            try{

                //       Toast.makeText(Login.this,""+out,Toast.LENGTH_LONG).show();

                JSONObject jsonResponse = new JSONObject(out);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("user_info");
                int arraylength=jsonMainNode.length();
                if(arraylength==0){
                    Toast.makeText(Login.this,"Invalid Username Password",Toast.LENGTH_LONG);
                }else {

                    String uid="";
                    String uname="";
                    String cnumber="";
                    String status="";

                    for (int i = 0; i < jsonMainNode.length(); i++) {

                        // Problability getting
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        uid = jsonChildNode.optString("Uid");
                        uname=jsonChildNode.optString("Name");
                        cnumber=jsonChildNode.optString("Cnumber");
                        status=jsonChildNode.optString("status");
                        String email=jsonChildNode.optString("Email");

                        SharedPreferences shr= PreferenceManager.getDefaultSharedPreferences(Login.this);
                        SharedPreferences.Editor edit =shr.edit();
                        edit.putString("uid",uid);
                        edit.putString("name",uname);
                        edit.putString("cnumber",cnumber);
                        edit.putString("email",email);
                        edit.commit();
                    }

                    Intent i1 = new Intent(Login.this, UserDashboard.class);
                    startActivity(i1);









                }


            }catch(Exception e){
                Toast.makeText(Login.this," ", Toast.LENGTH_LONG).show();
            }


            progress.dismiss();
        }



    }
}

