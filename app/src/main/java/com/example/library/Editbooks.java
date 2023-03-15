package com.example.library;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Editbooks extends AppCompatActivity {
    private static Button btnQuery;
    private static EditText edtitemcode,names,At,pub,pdate,Author,Publisher,Publisherdate;
    private static TextView tv_civ;
    private static String cItemcode = "";
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "http://put your IP Address here /library/UpdateQty.php";
    private static String TAG_MESSAGE = "message" , TAG_SUCCESS = "success";
    private static String online_dataset = "";
    public static String String_isempty = "";
    public static final String TITLE = "TITLE";
    public static final String AUTHOR = "AUTHOR";
    public static final String PUBLISHER = "PUBLISHER";
    public static final String PUBDATE = "PUBDATE";
    public static final String ID = "ID";
    private String ttl,auth,publ,pbd,aydi;


    private static String title = "";
    private static String authors = "";
    private static String publisher = "";
    private static String publication_date = "";
    //Gender
    RadioButton male,female;
    RadioGroup RDgroup;
    View.OnClickListener MaleandFemale;

    // Spinner
    Spinner status;

    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_records);
        names = (EditText) findViewById(R.id.edtitemcode);
        At = (EditText) findViewById(R.id.author);
        pub = (EditText) findViewById(R.id.Publisher);
        pdate = (EditText) findViewById(R.id.date);
        Author = (EditText) findViewById(R.id.author);
        Publisher = (EditText) findViewById(R.id.Publisher);
        Publisherdate = (EditText) findViewById(R.id.date);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        edtitemcode = (EditText) findViewById(R.id.edtitemcode);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        tv_civ = (TextView) findViewById(R.id.textView3);

        Intent i = getIntent();
        ttl = i.getStringExtra(TITLE);
        pbd = i.getStringExtra(PUBDATE);
        auth = i.getStringExtra(AUTHOR);
        publ = i.getStringExtra(PUBLISHER);
        aydi = i.getStringExtra(ID);
        names.setText(ttl);
        At.setText(auth);
        pub.setText(publ);
        pdate.setText(pbd);


        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = edtitemcode.getText().toString();
                authors = Author.getText().toString();
                publisher = Publisher.getText().toString();
                publication_date = Publisherdate.getText().toString();
                new uploadDataToURL().execute();
            }
        });


    }

    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        String gens,civil;
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(Editbooks.this);

        public uploadDataToURL() { }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setMessage(cMessage);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            int nSuccess;
            try {
                ContentValues cv = new ContentValues();
                //insert anything in this cod

                cPostSQL = aydi;
                cv.put("id", cPostSQL);

                cPostSQL = " '" + title + "' ";
                cv.put("title", cPostSQL);

                cPostSQL = " '" + authors + "' ";
                cv.put("auth",cPostSQL);

                cPostSQL = " '" + publisher + "' ";
                cv.put("publ", cPostSQL);

                cPostSQL = " '" + publication_date + "' ";
                cv.put("pubd", cPostSQL);



                JSONObject json = jParser.makeHTTPRequest(urlHost, "POST" , cv);
                if(json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
                        online_dataset = json.getString(TAG_MESSAGE);
                        return online_dataset;
                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                } else {
                    return "HTTPSERVER_ERROR";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            String isEmpty = "";
            AlertDialog.Builder alert = new AlertDialog.Builder(Editbooks.this);
            if (s !=null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) { }
                Toast.makeText(Editbooks.this, s , Toast.LENGTH_SHORT).show();
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

}
