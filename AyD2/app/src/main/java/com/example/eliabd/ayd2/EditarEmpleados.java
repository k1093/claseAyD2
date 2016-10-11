package com.example.eliabd.ayd2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class EditarEmpleados extends AppCompatActivity {


    public String nombre;
    public String apellido;
    public String dpi;
    public String puesto;
    public String salario;

    private EditText editNombre;
    private EditText editApellido;
    private EditText editDpi;
    private EditText editPuesto;
    private EditText editSalario;

    public Context context ;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_empleados);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = EditarEmpleados.this;
        editNombre = (EditText) findViewById(R.id.editNombre);
        editApellido = (EditText) findViewById(R.id.editApellido);
        editDpi = (EditText) findViewById(R.id.editDpi);
        editPuesto = (EditText) findViewById(R.id.editPuesto);
        editSalario = (EditText) findViewById(R.id.editSalario);

        Bundle extras = getIntent().getExtras();


        editNombre.setText(extras.getString("nombre"));

        editApellido.setText(extras.getString("apellido"));
        editDpi.setText(extras.getString("dpi"));
        editDpi.setEnabled(false);
        editPuesto.setText(extras.getString("puesto"));
        editSalario.setText(extras.getString("salario"));

        nombre = editNombre.getText().toString();
        apellido = editApellido.getText().toString();
        dpi = editDpi.getText().toString();
        salario = editSalario.getText().toString();
        puesto = editPuesto.getText().toString();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AsynEditar ae = new AsynEditar();
                ae.execute();

              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
    }

    private class AsynEditar extends AsyncTask<Void, Void, Void> {

        List list = new ArrayList();


        private void makePostRequest() {


            HttpClient httpClient = new DefaultHttpClient();
            // replace with your url
            HttpPost httpPost = new HttpPost("http://192.168.43.31:9000/editar/"+ dpi);


            //Post Data
           /* List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(5);
            nameValuePair.add(new BasicNameValuePair("nombre", nombre));
            nameValuePair.add(new BasicNameValuePair("apellido", apellido));
            nameValuePair.add(new BasicNameValuePair("dpi", dpi));
            nameValuePair.add(new BasicNameValuePair("puesto", puesto));
            nameValuePair.add(new BasicNameValuePair("salario", salario));
            */

            //Encoding POST data
            try {

                JSONObject object = new JSONObject();
                object.accumulate("nombre",editNombre.getText());
                object.accumulate("apellido", editApellido.getText());
                object.accumulate("dpi", editDpi.getText());
                object.accumulate("puesto", editPuesto.getText());
                object.accumulate("salario", editSalario.getText());
                Log.i("el json object " , object.toString());
                httpPost.setEntity(new StringEntity(object.toString(),"UTF-8"));
                httpPost.setHeader("Accept","application/json");
                httpPost.setHeader("Content-type","application/json");
            } catch (UnsupportedEncodingException e) {
                // log exception
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //making POST request.
            try {
                HttpResponse response = httpClient.execute(httpPost);
                // write response to log
                String respuesta = EntityUtils.toString(response.getEntity());
                Log.d("Http Post Response:", respuesta);
            } catch (ClientProtocolException e) {
                // Log exception
                e.printStackTrace();
            } catch (IOException e) {
                // Log exception
                e.printStackTrace();
            }

        }


        @Override
        protected void onPreExecute() {


            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Espere mientras se realiza la petición.");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();


        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Void... params) {
            makePostRequest();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {



            progressDialog.dismiss();

        }

    }


}
