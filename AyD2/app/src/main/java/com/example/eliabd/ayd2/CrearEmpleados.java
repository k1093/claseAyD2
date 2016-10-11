package com.example.eliabd.ayd2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CrearEmpleados extends AppCompatActivity {

    ProgressDialog progressDialog;
    EditText textNombre;
    EditText textApellido;
    EditText textDpi;
    EditText textPuesto;
    EditText textSalario;
    CoordinatorLayout coordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_empleados);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinator = (CoordinatorLayout) findViewById(R.id.coordinator_crear);

        textNombre = (EditText) findViewById(R.id.text_nombre_crear);
        textApellido = (EditText) findViewById(R.id.text_apellido_crear);
        textDpi = (EditText) findViewById(R.id.text_dpi_crear);
        textPuesto = (EditText) findViewById(R.id.text_puesto_crear);
        textSalario = (EditText)findViewById(R.id.text_salario_crear);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsynCrear asynCrear = new AsynCrear();
                asynCrear.execute();

            }
        });
    }




    private class AsynCrear extends AsyncTask<Void, Void, Void> {

        List list = new ArrayList();


        private void makePostRequest() {


            HttpClient httpClient = new DefaultHttpClient();
            // replace with your url
            HttpPost httpPost = new HttpPost("http://192.168.43.18:9000/registro");


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
                object.accumulate("nombre",textNombre.getText());
                object.accumulate("apellido", textApellido.getText());
                object.accumulate("dpi", textDpi.getText());
                object.accumulate("puesto", textPuesto.getText());
                object.accumulate("salario", textSalario.getText());
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

                if(respuesta.equals("Usuario creado exitosamente")){

                    Snackbar.make(coordinator, "Usuario creado Exitosamente", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }else{

                    Snackbar.make(coordinator, "Ups! ocurrio un error", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                }

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


            progressDialog = new ProgressDialog(CrearEmpleados.this);
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
