package com.example.library;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ManageBooks extends AppCompatActivity {

    private static Button btnQuery;
    TextView textView,txtDefault,txtDefault_gender,txtDefault_civilStatus,txtDefault_ID, txtDefault_author, txtDefault_publisher, txtDefault_publisherdate;
    private static  EditText edtitemcode;
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "http://192.168.137.166/library/SelectItemDetails.php";
    private static String urlHostDelete = "http://192.168.137.166/library/delete.php";
    private static String urlHostAuthor = "http://192.168.137.166/library/selectAuthor.php";
    private static String urlHostPublisher = "http://192.168.137.166/library/selectPublisher.php";
    private static String urlHostPublisherdate = "http://192.168.137.166/library/selectPublisherdate.php";
    private static String urlHostID = "http://192.168.137.166/library/selectid.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    private static String cItemcode = "";

    public static String wew = "";
    public static String gender = "";
    public static String civilstats = "";
    public static String author = "";
    public static String publisher = "";
    public static String publisherdate = "";

    private String ems,gen,civ,aydi,ttl,auth,publ,pdb;

    String cItemSelected,cItemSelected_gender,cItemSelected_civilStatus,cItemSelected_ID,cItemSelected_author,cItemSelected_publisher,cItemSelected_publisherdate;
    ArrayAdapter <String> adapter_fnames;
    ArrayAdapter <String> adapter_gender;
    ArrayAdapter <String> adapter_civilStatus;
    ArrayAdapter <String> adapter_author;
    ArrayAdapter <String> adapter_publisher;
    ArrayAdapter <String> adapter_publisherdate;
    ArrayAdapter <String> adapter_ID;
    ArrayList <String> list_fnames;
    ArrayList <String> list_gender;
    ArrayList <String> list_author;
    ArrayList <String> list_publisher;
    ArrayList <String> list_publisherdate;
    ArrayList <String> list_civilStatus;
    ArrayList <String> list_ID;
    AdapterView.OnItemLongClickListener longItemListener_fnames;
    Context context = this;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_records);

        btnQuery = (Button) findViewById(R.id.btnQuery);
        edtitemcode = (EditText) findViewById(R.id.edtitemcode);
        txtDefault = (TextView) findViewById(R.id.tv_default);
        listView = (ListView) findViewById(R.id.listview);
        textView = (TextView) findViewById(R.id.textView4);
        txtDefault_author = (TextView) findViewById(R.id.txt_author);
        txtDefault_publisher = (TextView) findViewById(R.id.txt_publisher);
        txtDefault_publisherdate = (TextView) findViewById(R.id.txt_publisherdate);
        txtDefault_ID = (TextView) findViewById(R.id.txt_ID);

        txtDefault.setVisibility(View.GONE);
        txtDefault_author.setVisibility(View.GONE);
        txtDefault_publisher.setVisibility(View.GONE);
        txtDefault_publisherdate.setVisibility(View.GONE);
        txtDefault_ID.setVisibility(View.GONE);


        Toast.makeText(ManageBooks.this, "Nothing Selected", Toast.LENGTH_SHORT).show();

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cItemcode = edtitemcode.getText().toString();

                new uploadDataToURL().execute();
                new Author().execute();
                new Publisher().execute();
                new Publisherdate().execute();
                new id().execute();



            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                cItemSelected = adapter_fnames.getItem(position);
                cItemSelected_author = adapter_author.getItem(position);
                cItemSelected_publisher= adapter_publisher.getItem(position);
                cItemSelected_publisherdate = adapter_publisherdate.getItem(position);
                cItemSelected_ID = adapter_ID.getItem(position);

                androidx.appcompat.app.AlertDialog.Builder alert_confirm =
                        new androidx.appcompat.app.AlertDialog.Builder(context);
                alert_confirm.setMessage("Edit the records of" + " " + cItemSelected);
                alert_confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        txtDefault.setText(cItemSelected);
                        txtDefault_author.setText(cItemSelected_author);
                        txtDefault_publisher.setText(cItemSelected_publisher);
                        txtDefault_publisherdate.setText(cItemSelected_publisherdate);
                        txtDefault_ID.setText(cItemSelected_ID);

                        ttl = txtDefault.getText().toString().trim();
                        auth = txtDefault_author.getText().toString().trim();
                        publ = txtDefault_publisher.getText().toString().trim();
                        pdb = txtDefault_publisherdate.getText().toString().trim();
                        aydi = txtDefault_ID.getText().toString().trim();

                        Intent intent = new Intent(ManageBooks.this,Editbooks.class);
                        intent.putExtra(Editbooks.TITLE,ttl);
                        intent.putExtra(Editbooks.AUTHOR,auth);
                        intent.putExtra(Editbooks.PUBLISHER,publ);
                        intent.putExtra(Editbooks.PUBDATE,pdb);
                        intent.putExtra(Editbooks.ID,aydi);

                        startActivity(intent);

                    }
                });
                alert_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert_confirm.show();
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                cItemSelected = adapter_fnames.getItem(position);
                cItemSelected_author = adapter_author.getItem(position);
                cItemSelected_publisher = adapter_publisher.getItem(position);
                cItemSelected_publisherdate = adapter_publisherdate.getItem(position);
                cItemSelected_ID = adapter_ID.getItem(position);

                androidx.appcompat.app.AlertDialog.Builder alert_confirm =
                        new androidx.appcompat.app.AlertDialog.Builder(context);
                alert_confirm.setMessage("Are you sure you want to delete" + " " + cItemSelected);
                alert_confirm.setPositiveButton(R.string.msg2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        txtDefault_ID.setText(cItemSelected_ID);
                        aydi = txtDefault_ID.getText().toString().trim();
                        new delete().execute();
                    }
                });
                alert_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert_confirm.show();

            }
        });



    }


    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBooks.this);

        public uploadDataToURL() {
        }

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

                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHost, "POST", cv);
                if (json != null) {
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
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBooks.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) { }
                //toast.makeText(ManageRecords.this, s, Toast.LENGTH_SHORT).show();
                String wew = s;

                String str = wew;
                final String fnames[] = str.split("-");
                list_fnames = new ArrayList<String>(Arrays.asList(fnames));
                adapter_fnames = new ArrayAdapter<String>(ManageBooks.this,
                        android.R.layout.simple_list_item_1,list_fnames);

                listView.setAdapter(adapter_fnames);
                textView.setText(listView.getAdapter().getCount() + " " +"record(s) fround.");


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class Author extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBooks.this);

        public Author() {
        }

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

                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHostAuthor, "POST", cv);
                if (json != null) {
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


            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String Author) {
            super.onPostExecute(Author);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBooks.this);
            if (Author != null) {
                if (isEmpty.equals("") && !Author.equals("HTTPSERVER_ERROR")) { }


                String author = Author;

                String str = author;
                final String Authors[] = str.split("-");
                list_author = new ArrayList<String>(Arrays.asList(Authors));
                adapter_author = new ArrayAdapter<String>(ManageBooks.this,
                        android.R.layout.simple_list_item_1,list_author);

                //listView.setAdapter(adapter_gender);



            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class Publisherdate extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBooks.this);

        public Publisherdate() {
        }

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

                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHostPublisherdate, "POST", cv);
                if (json != null) {
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
        protected void onPostExecute(String pbl) {
            super.onPostExecute(pbl);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBooks.this);
            if (pbl != null) {
                if (isEmpty.equals("") && !pbl.equals("HTTPSERVER_ERROR")) { }

                String publisher = pbl;

                String str = publisher;
                final String pblsh[] = str.split("-");
                list_publisher = new ArrayList<String>(Arrays.asList(pblsh));
                adapter_publisher = new ArrayAdapter<String>(ManageBooks.this,
                        android.R.layout.simple_list_item_1,list_publisher);

                //listView.setAdapter(adapter_gender);



            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }


    private class Publisher extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBooks.this);

        public Publisher() {
        }

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

                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHostPublisher, "POST", cv);
                if (json != null) {
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
        protected void onPostExecute(String pbd) {
            super.onPostExecute(pbd);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBooks.this);
            if (pbd != null) {
                if (isEmpty.equals("") && !pbd.equals("HTTPSERVER_ERROR")) { }

                String publisherdate = pbd;

                String str = publisherdate;
                final String pblshd[] = str.split("-");
                list_publisherdate = new ArrayList<String>(Arrays.asList(pblshd));
                adapter_publisherdate = new ArrayAdapter<String>(ManageBooks.this,
                        android.R.layout.simple_list_item_1,list_publisherdate);

                //listView.setAdapter(adapter_gender);



            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }



    private class id extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBooks.this);

        public id() {
        }

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

                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHostID, "POST", cv);
                if (json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1){
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
        protected void onPostExecute(String aydi) {
            super.onPostExecute(aydi);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBooks.this);
            if (aydi != null) {
                if (isEmpty.equals("") && !aydi.equals("HTTPSERVER_ERROR")) { }
                Toast.makeText(ManageBooks.this, "Data selected", Toast.LENGTH_SHORT).show();

                String AYDI = aydi;

                String str = AYDI;
                final String ayds[] = str.split("-");
                list_ID = new ArrayList<String>(Arrays.asList(ayds));
                adapter_ID = new ArrayAdapter<String>(ManageBooks.this,
                        android.R.layout.simple_list_item_1,list_ID);

                //listView.setAdapter(adapter_gender);



            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class delete extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBooks.this);

        public delete() {
        }

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

                cPostSQL = cItemSelected_ID;
                cv.put("id", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHostDelete, "POST", cv);
                if (json != null) {
                    nSuccess =json.getInt(TAG_SUCCESS);
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
        protected void onPostExecute(String del) {
            super.onPostExecute(del);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBooks.this);
            if (aydi != null) {
                if (isEmpty.equals("") && !del.equals("HTTPSERVER_ERROR")) { }
                Toast.makeText(ManageBooks.this, "Data Deleted", Toast.LENGTH_SHORT).show();
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}
