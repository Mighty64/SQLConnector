package com.joseph.sqlconnector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();

    private void hacerSolicitudHttpGet(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseData = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Maneja la respuesta en el hilo principal (UI thread)
                        // Aquí puedes actualizar la interfaz de usuario con los datos recibidos
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Llamada al método
    private void realizarSolicitud() {
        String url = "remote@192.168.1.2:3306"; // URL de ejemplo, reemplázala con tu URL real
        hacerSolicitudHttpGet(url);
    }

    private EditText edServidor;
    private EditText edPuerto;
    private EditText edUsuario;
    private EditText edPassword;
    private String baseDatos = "studio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            edServidor = (EditText)findViewById(R.id.edServidor);
            edPuerto  = (EditText)findViewById(R.id.edPuerto);
            edUsuario = (EditText)findViewById(R.id.edUsuario);
            edPassword = (EditText)findViewById(R.id.edPassword);
    }


    //Funci�n que establecer� la conexi�n con el Servidor si los datos introducidos son correcto.
    //Devuelve un valor de verdadero o falso que indicar� si se ha establecido la conexi�n
    public boolean conectarMySQL() {
        boolean estadoConexion = false;
        Connection conexionMySQL = null;

        String user = edUsuario.getText().toString();
        String password = edPassword.getText().toString();
        String puerto = edPuerto.getText().toString();
        String ip = edServidor.getText().toString();

        String driver = "com.mysql.cj.jdbc.Driver";
        String urlMySQL = "jdbc:mysql://" + ip + ":" + puerto + "/" + baseDatos;

        try {
            Class.forName(driver).newInstance();
            conexionMySQL = DriverManager.getConnection(urlMySQL, user, password);

            if (!conexionMySQL.isClosed()) {
                estadoConexion = true;
                Toast.makeText(MainActivity.this, "Conexión Establecida", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "Error al comprobar las credenciales: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("TAG", "ex: " + ex.toString());
        } finally {
            if (conexionMySQL != null) {
                try {
                    conexionMySQL.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return estadoConexion;
    }


    //Evento On Click que realiza la llamada a la funci�n conectarMySQL() obteniendo el valor de verdadero
    //o falso para la petici�n de conexi�n
    public void abrirConexion(View view) throws SQLException {
        Intent intent = new Intent(this,ConsultasSQL.class);
        //Si el valor devuelto por la funci�n es true, pasaremos los datos de la conexi�n a la siguiente Activity


        if(conectarMySQL() == true)
    {
            Toast.makeText(this, "Los datos de conexión introducidos son correctos."
                    , Toast.LENGTH_LONG).show();
            intent.putExtra("servidor", edServidor.getText().toString());
            intent.putExtra("puerto", edPuerto.getText().toString());
            intent.putExtra("usuario", edUsuario.getText().toString());
            intent.putExtra("password", edPassword.getText().toString());
            intent.putExtra("datos", baseDatos);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }


}