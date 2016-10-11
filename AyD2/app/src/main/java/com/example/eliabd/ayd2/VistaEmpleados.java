package com.example.eliabd.ayd2;

import android.app.ProgressDialog;
import android.content.Entity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class VistaEmpleados extends AppCompatActivity {
    private RecyclerView recycler;
    private EmpleadosAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    List items = new ArrayList();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_empleados);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recycler = (RecyclerView) findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        AsynClass sy = new AsynClass();
        sy.execute();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                items.clear();
                AsynClass asy = new AsynClass();
                asy.execute();

            }
        });

        FloatingActionButton fabCrear = (FloatingActionButton) findViewById(R.id.fab_crear);
        fabCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(VistaEmpleados.this, CrearEmpleados.class);
                startActivity(intent);
            }
        });
    }



    private class AsynClass extends AsyncTask<Void, Void, Void> {

        List list = new ArrayList();

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        private void makeGetRequest() {

            HttpClient client = new DefaultHttpClient();
            //192.168.43.31

            //http://echo.jsontest.com/id/4/nombre/jhon/apellido/smith/dpi/123456/puesto/manager/salario/2.5

            HttpGet request = new HttpGet("http://192.168.43.18:9000/getempleados");
            // replace with your url

            HttpResponse response;


                try{
                    response = client.execute(request);

                    Log.d("Response of GET request", response.toString());

                    try{


                        String json1 = EntityUtils.toString(response.getEntity()).replace("[" ,"");
                        String json2 = json1.replace("]","");
                        String json = "{ empleados:["+ json2 + "] }" ;
                        Log.d("El json trae", json);
                        JSONObject object = new JSONObject(json);
                        JSONArray jsonArray = object.getJSONArray("empleados");


                        for (int i = 0; i < jsonArray.length(); i++) {


                            String apellido = jsonArray.getJSONObject(i).getString("apellido");
                            String nombre = jsonArray.getJSONObject(i).getString("nombre");
                            String id = jsonArray.getJSONObject(i).getString("id");
                            String salario = jsonArray.getJSONObject(i).getString("salario");
                            String puesto = jsonArray.getJSONObject(i).getString("puesto");
                            String dpi = jsonArray.getJSONObject(i).getString("dpi");


                            Empleados empleado = new Empleados(nombre, apellido, Integer.parseInt(dpi), puesto, Double.parseDouble(salario));

                            items.add(empleado);

                            Log.d("apellido:", apellido);
                            Log.d("nombre:", nombre);




                        }


                    } catch (Exception ex) {
                        Log.e("Response", "Error: " + ex.getMessage());
                    }







                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    Log.i("Cagada","mas Cagada");
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.i("Cagada1","mas Cagada1");
                    e.printStackTrace();
                }











        }




        @Override
        protected void onPreExecute() {


            progressDialog = new ProgressDialog(VistaEmpleados.this);
            progressDialog.setMessage("Espere mientras se realiza la petición.");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();


        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Void... params) {

            makeGetRequest();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            adapter = new EmpleadosAdapter(items, VistaEmpleados.this);
            recycler.setAdapter(adapter);

            progressDialog.dismiss();

        }

    }
}
/*
        public void calcular(){


            String SOAP_ACTION = "http://192.168.1.5/projecto/getJob";
            String METHOD_NAME = "getJob";
            String NAMESPACE = "http://192.168.1.5/projecto";
            String URL = "http://192.168.1.5/projecto/servicio.php";
            //jsonplaceholder.typicode.com/users
            try{

                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(request);

                HttpTransportSE transportSE = new HttpTransportSE(URL.toString());

                transportSE.call(SOAP_ACTION,soapEnvelope);

                Object resultado = soapEnvelope.getResponse();
                String responseJson = resultado.toString();

                JSONArray jsonArray = new JSONArray(responseJson);


                for(int i = 0; i < jsonArray.length();i++){


                    String titulo = jsonArray.getJSONObject(i).getString("titulo");
                    String descripcion = jsonArray.getJSONObject(i).getString("descripcion");
                    String requisito = jsonArray.getJSONObject(i).getString("requisito");
                    String ofrecemos = jsonArray.getJSONObject(i).getString("ofrecemos");
                    String conocimiento = jsonArray.getJSONObject(i).getString("conocimiento");
                    String contacto = jsonArray.getJSONObject(i).getString("contacto");
                    String imagen = jsonArray.getJSONObject(i).getString("images");
                    URL newUrl = new URL(imagen);
                    Bitmap iconVal =  BitmapFactory.decodeStream(newUrl.openConnection().getInputStream());



                    if(!titulo.isEmpty() && !descripcion.isEmpty() && !requisito.isEmpty() && !ofrecemos.isEmpty() && !conocimiento.isEmpty()) {

                        items.add(new Job(titulo, iconVal, descripcion, requisito, ofrecemos, conocimiento, contacto));
                    }

                }
            }catch (Exception ex) {
                Log.e("Response", "Error: " + ex.getMessage());
            }


        }




    }



}
*/