package com.example.eliabd.ayd2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eliabd on 19/09/2016.
 */
public class EmpleadosAdapter extends RecyclerView.Adapter<EmpleadosAdapter.EmpleadosViewHolder> {



    public Context context ;
    public String respuestaGeneral;
    public int positionGeneral;
    public String dpiGeneral ;
   public List<Empleados> items;
    ProgressDialog progressDialog;


    public class EmpleadosViewHolder extends RecyclerView.ViewHolder{


        public TextView textNombre;
        public TextView textApellido;
        public TextView textDpi;
        public TextView textPuesto;
        public TextView textSalario;
        public Button btnEliminar;
        public Button btnEditar;

        public EmpleadosViewHolder(View itemView) {
            super(itemView);

            textNombre = (TextView) itemView.findViewById(R.id.text_nombre);
            textApellido = (TextView) itemView.findViewById(R.id.text_apellido);
            textDpi = (TextView) itemView.findViewById(R.id.text_dpi);
            textPuesto  = (TextView) itemView.findViewById(R.id.text_puesto);
            textSalario = (TextView) itemView.findViewById(R.id.text_salario);
            btnEliminar = (Button)itemView.findViewById(R.id.btn_eliminar);
            btnEditar = (Button) itemView.findViewById(R.id.btn_editar);

        }
    }


    public EmpleadosAdapter(List<Empleados> items, Context context){

        this.items = items;
        this.context = context;

    }

    @Override
    public EmpleadosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empleado_layout, parent,false);

        return new EmpleadosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EmpleadosViewHolder holder,final int position) {



        holder.textNombre.setText(items.get(position).getNombre());
        holder.textApellido.setText(items.get(position).getApellido());
        holder.textDpi.setText(""+items.get(position).getDpi());
        holder.textPuesto.setText(items.get(position).getPuesto());
        holder.textSalario.setText("" + items.get(position).getSalario());
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), EditarEmpleados.class);
                intent.putExtra("nombre",items.get(position).getNombre());
                intent.putExtra("apellido",items.get(position).getApellido());
                intent.putExtra("dpi",""+items.get(position).getDpi());
                intent.putExtra("salario",""+items.get(position).getSalario());
                intent.putExtra("puesto",items.get(position).getPuesto());

                view.getContext().startActivity(intent);

            }
        });
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dpiGeneral = "" + items.get(position).getDpi();
                positionGeneral = position;
                AsynClass asynClass = new AsynClass();

                asynClass.execute();


            }
        });

    }


    public void removeAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setFilters(List<Empleados> listaEmpleados){

        items = new ArrayList<>();
        items.addAll(listaEmpleados);
        notifyDataSetChanged();


    }



    private class AsynClass extends AsyncTask<Void, Void, Void> {

        List list = new ArrayList();

        private void makeGetRequest() {

            HttpClient client = new DefaultHttpClient();


            HttpGet request = new HttpGet("http://192.168.43.18:9000/eliminar/"+ dpiGeneral);


            HttpResponse response;


            try{
                response = client.execute(request);

                Log.d("Response of GET request", response.toString());

                try{


                    String json1 = EntityUtils.toString(response.getEntity()).replace("[" ,"");
                    respuestaGeneral = json1;
                    Log.d("El json trae", json1);


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


            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Espere mientras se realiza la petición.");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();


        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Void... params) {

            makeGetRequest();

            if(respuestaGeneral.equals("1")){

                removeAt(positionGeneral);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {



            progressDialog.dismiss();

        }

    }





}



