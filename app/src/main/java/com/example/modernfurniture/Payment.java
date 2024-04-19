package com.example.modernfurniture;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class Payment extends AppCompatActivity {

    CardForm cardForm;
    Button buy;
    AlertDialog.Builder alertBuilder;
    TextView btn;
    ImageView back;
    EditText address, name2;
    FirebaseAuth auth;
    private FirebaseFirestore db;
    getCartData cart = new getCartData();
    String method2 = "cash on delivery";
    String method1 = "Online paid";
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        back = findViewById(R.id.Pback);
        name2 = findViewById(R.id.Pname);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),cart.class));
            }
        });
        cardForm = findViewById(R.id.card_form);
        buy = findViewById(R.id.paybtn);
        btn = findViewById(R.id.option1);
        address = findViewById(R.id.Paddress);

        //cash on delivery database
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name2.getText().toString().equals("") || address.getText().toString().equals("")){
                    Toast.makeText(Payment.this, "Enter All details", Toast.LENGTH_LONG).show();
                }
                else {
                    addOrder1();
                   // startActivity(new Intent(getApplicationContext(), SuccessPay.class));
                }
            }
        });


        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(Payment.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name2.getText().toString().equals("") || address.getText().toString().equals("")) {
                    Toast.makeText(Payment.this, "Enter All details", Toast.LENGTH_LONG).show();
                } else {
                    if (cardForm.isValid()) {
                        alertBuilder = new AlertDialog.Builder(Payment.this);
                        alertBuilder.setTitle("Confirm before purchase");
                        alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
                                "Card expiry date: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                                "Card CVV: " + cardForm.getCvv() + "\n" +
                                "Postal code: " + cardForm.getPostalCode() + "\n" +
                                "Phone number: " + cardForm.getMobileNumber());
                        alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                //Toast.makeText(Payment.this, "Thank you for purchase", Toast.LENGTH_LONG).show();
                                addOrder1();

                            }
                        });
                        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alertDialog = alertBuilder.create();
                        alertDialog.show();

                    } else {
                        Toast.makeText(Payment.this, "Please complete the form", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    public void addOrder1(){

        try{
        SharedPreferences shr= PreferenceManager.getDefaultSharedPreferences(Payment.this);
        String uid=shr.getString("uid","Na");


        DbParameter db1 = new DbParameter();
        String url = db1.getHostpath();
        url = url + "PlaceOrder.php?uid="+URLEncoder.encode(""+uid)
            + "&add=" + URLEncoder.encode(address.getText().toString())
            + "&name=" + URLEncoder.encode(address.getText().toString());
                   // + "&pass=" + URLEncoder.encode(password);
        //Toast.makeText(Payment.this, "Uid=="+uid, Toast.LENGTH_SHORT).show();
     LoadData l1 = new LoadData();
        l1.execute(url);
    }catch(Exception e){
        Toast.makeText(Payment.this,""+e,Toast.LENGTH_SHORT).show();
    }





        /*
        String saveCurrentTime, saveCurrentDate;
        Calendar calforDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM/dd/yyyy");
        saveCurrentDate = currentDate.format(calforDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calforDate.getTime());
        String id = UUID.randomUUID().toString();

        final HashMap<String, Object> cartMap2 = new HashMap<>();
        cartMap2.put("date", saveCurrentDate);
        cartMap2.put("time", saveCurrentTime);
        cartMap2.put("method", method1);
        cartMap2.put("orderId", id);
        cartMap2.put("address", address.getText().toString());
        cartMap2.put("userName", name2.getText().toString());
        db.collection("Admin_Orders").document(id)
                .set(cartMap2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Toast.makeText(DetailsPage.this,"Added to Cart",Toast.LENGTH_SHORT).show();
            }
        });


        db.collection("Cart").document(auth.getCurrentUser().getUid())
                .collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> clist = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : clist){
                                final HashMap<String, Object> cartMap = new HashMap<>();
                                cartMap.put("date", saveCurrentDate);
                                cartMap.put("time", saveCurrentTime);
                                cartMap.put("method", method1);
                                cartMap.put("orderId", id);
                                cartMap.put("address", address.getText().toString());
                                cartMap.put("userName", name2.getText().toString());
                                db.collection("Orders").document(auth.getCurrentUser().getUid())
                                        .collection("users").document(id)
                                        .set(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //Toast.makeText(DetailsPage.this,"Added to Cart",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                getCartData p = d.toObject(getCartData.class);
                                CollectionReference dbOrder = db.collection("Orders").document(auth.getCurrentUser().getUid()).collection("users").document(id).collection("Ordered_products");
                                dbOrder.add(p);

                                getCartData p2 = d.toObject(getCartData.class);
                                CollectionReference dbOrder2 = db.collection("Admin_Orders").document(id).collection("Ordered_products");
                                dbOrder2.add(p2);

                            }
                        }
                    }
                });*/


    };

    public void addOrder2(){
        String saveCurrentTime, saveCurrentDate;
        Calendar calforDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM/dd/yyyy");
        saveCurrentDate = currentDate.format(calforDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calforDate.getTime());
        String id = UUID.randomUUID().toString();

        final HashMap<String, Object> cartMap2 = new HashMap<>();
        cartMap2.put("date", saveCurrentDate);
        cartMap2.put("time", saveCurrentTime);
        cartMap2.put("method", method2);
        cartMap2.put("orderId", id);
        cartMap2.put("address", address.getText().toString());
        cartMap2.put("userName", name2.getText().toString());
        db.collection("Admin_Orders").document(id)
                .set(cartMap2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Toast.makeText(DetailsPage.this,"Added to Cart",Toast.LENGTH_SHORT).show();
            }
        });

        db.collection("Cart").document(auth.getCurrentUser().getUid())
                .collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> clist = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : clist){
                                final HashMap<String, Object> cartMap = new HashMap<>();
                                cartMap.put("date", saveCurrentDate);
                                cartMap.put("time", saveCurrentTime);
                                cartMap.put("method", method2);
                                cartMap.put("orderId", id);
                                cartMap.put("address", address.getText().toString());
                                cartMap.put("userName", name2.getText().toString());
                                db.collection("Orders").document(auth.getCurrentUser().getUid())
                                        .collection("users").document(id)
                                        .set(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //Toast.makeText(DetailsPage.this,"Added to Cart",Toast.LENGTH_SHORT).show();
                                    }
                                });

                                getCartData p = d.toObject(getCartData.class);
                                CollectionReference dbOrder = db.collection("Orders").document(auth.getCurrentUser().getUid()).collection("users").document(id).collection("Ordered_products");
                                dbOrder.add(p);

                                getCartData p2 = d.toObject(getCartData.class);
                                CollectionReference dbOrder2 = db.collection("Admin_Orders").document(id).collection("Ordered_products");
                                dbOrder2.add(p2);

                            }
                        }
                    }
                });

    };




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
            progress = ProgressDialog.show(Payment.this, null,
                    "Processing Order...");

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            Toast.makeText(Payment.this, "Order Placed Sucessfully"+out, Toast.LENGTH_LONG).show();
           // startActivity(new Intent(getApplicationContext(), SuccessPay.class));

            SharedPreferences shr=PreferenceManager.getDefaultSharedPreferences(Payment.this);
            String name=shr.getString("name","NA");

            SmsManager sm=SmsManager.getDefault();
            sm.sendTextMessage("7758063576",null,"One Order received from "+name,null,null );

            progress.dismiss();
            //  String pass= pass11;
            //  SmsManager smgr = SmsManager.getDefault();
            // smgr.sendTextMessage(contact.getText().toString(),null,"user name: "+email.getText().toString()+" password: "+pass,null,null);
            //transactions.setText("Transactions :"+count);

            // Intent register_i1 = new Intent(AddUser.this,Login.class);
            //  startActivity(register_i1);
           finish();
        }



    }
}