package com.example.modernfurniture;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class DetailsPage extends AppCompatActivity {
    ImageView back;
    ImageView Img;
    TextView name,price,type,quantity;
    Button addtoCart, wishlist, view;
    ImageView add,remove;
    int totalQuntity = 1;

    private FirebaseFirestore db;
    FirebaseAuth auth;
    Products products = null;
    String pname,pcost,pimage,pcat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final Object obj = getIntent().getSerializableExtra("details");

        if(obj instanceof Products){
            products = (Products) obj;
        }

        view = findViewById(R.id.D360);
        Img = findViewById(R.id.Dimage);
        quantity = findViewById(R.id.Dquantity);
        name = findViewById(R.id.Dnamefill);
        price = findViewById(R.id.Dpricefill);
        type = findViewById(R.id.Dtypefill);
        addtoCart = findViewById(R.id.Dcart);
        wishlist = findViewById(R.id.Dwishlist);
        add = findViewById(R.id.Dplus);
        remove = findViewById(R.id.Dminus);

        Intent i1=getIntent();
        pname= i1.getStringExtra("name");
        pcost= i1.getStringExtra("cost");
        pcat= i1.getStringExtra("type");

        name.setText(pname);
        price.setText(pcost);
        type.setText(pcat);

        String uri =i1.getStringExtra("image");  // where myresource (without the extension) is the file
        pimage=i1.getStringExtra("image");
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());

      //  imageview= (ImageView)findViewById(R.id.imageView);
        Drawable res = getResources().getDrawable(imageResource);
        Img.setImageDrawable(res);
        //new products
        /*
        if(products != null){
            Picasso.get().load(products.getImageUrl()).into(Img);
            name.setText(products.getName());
            price.setText(String.valueOf(products.getPrice()));
            type.setText(products.getType());
        }
*/
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuntity<10){
                    totalQuntity++;
                    quantity.setText(String.valueOf(totalQuntity));
                }

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuntity>1){
                    totalQuntity--;
                    quantity.setText(String.valueOf(totalQuntity));
                }

            }
        });

        back = findViewById(R.id.Dback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),gallery.class));
            }
        });
        
        addtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtoCart();
            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtoWishlist();
            }
        });
    }

    private void addtoWishlist() {
        String price2 = price.getText().toString();
        String id = UUID.randomUUID().toString();
        final HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("name",pname);
        cartMap.put("type", pcat);
        cartMap.put("image", pimage);
        cartMap.put("price", ""+pcost);
        cartMap.put("id", id);


        try {
            DbParameter db1 = new DbParameter();
            String url = db1.getHostpath();
            url = url + "AddToWish.php?name=" + URLEncoder.encode(pname.toString())
                    + "&price=" + URLEncoder.encode(""+pcost);
                   // + "&quantity=" + URLEncoder.encode(""+quantity2);

            LoadData l1 = new LoadData();
            l1.execute(url);
        }catch(Exception e){
            Toast.makeText(DetailsPage.this,""+e,Toast.LENGTH_SHORT).show();
        }

        db.collection("Wishlist").document(auth.getCurrentUser().getUid())
                .collection("users").document(id)
                .set(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DetailsPage.this,"Added to Wishlist",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addtoCart() {
        try {
        String image2 = pimage;
        String name2 = pname;
        String price2 = pcost;
        String quantity2 = quantity.getText().toString();
        String id = UUID.randomUUID().toString();
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("id", id);
        cartMap.put("name", name2);
        cartMap.put("price", price2);
        cartMap.put("quantity", Double.parseDouble(quantity2));
       // cartMap.put("image", image2);


            SharedPreferences shr= PreferenceManager.getDefaultSharedPreferences(DetailsPage.this);
            String uid=shr.getString("uid","Na");


            DbParameter db1 = new DbParameter();
            String url = db1.getHostpath();
            url = url + "AddToCart.php?name=" + URLEncoder.encode(name2.toString())
                    + "&price=" + URLEncoder.encode(""+price2)
                    + "&uid=" + URLEncoder.encode(""+uid)
                    + "&quantity=" + URLEncoder.encode(""+Double.parseDouble(quantity2));
            Toast.makeText(DetailsPage.this, "Uid=="+uid, Toast.LENGTH_SHORT).show();
            LoadData l1 = new LoadData();
            l1.execute(url);
        }catch(Exception e){
            Toast.makeText(DetailsPage.this,""+e,Toast.LENGTH_SHORT).show();
        }

        /*
        db.collection("Cart").document(auth.getCurrentUser().getUid())
                .collection("users").document(id)
                .set(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DetailsPage.this,"Added to Cart",Toast.LENGTH_SHORT).show();
            }
        });

         */

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
            progress = ProgressDialog.show(DetailsPage.this, null,
                    "Updating Register Information...");

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            Toast.makeText(DetailsPage.this, "Added to Cart Sucessfully ", Toast.LENGTH_LONG).show();
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