package com.joseph.sqlconnector;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConsultasSQL extends Activity {

    //Declaramos los componentes y clases necesarias para realizar consultas a una base de datos en MySQL
    private TextView txtBaseDatos;
    private EditText edConsulta;
    private TextView txtResultados;
    private TextView txtPuerto;
    private TextView txtServidor;
    private TextView txtUsuario;
    private Statement st;
    private ResultSet rs;
    private Connection con;
    private Bundle bundle;
    private String baseDatos = "Tienda";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas_sql);

        //Enlazamos los componentes con los recursos definidos en el layout
        txtBaseDatos = (TextView)findViewById(R.id.txtBaseDatos);
        edConsulta = (EditText)findViewById(R.id.edConsulta);
        txtResultados = (TextView)findViewById(R.id.txtResultados);
        txtPuerto = (TextView)findViewById(R.id.txtPuerto);
        txtServidor = (TextView)findViewById(R.id.txtServidor);
        txtUsuario = (TextView)findViewById(R.id.txtUsuario);

        bundle = getIntent().getExtras();
        //Obtenemos los valores introducidos en la Activity principal
        txtPuerto.setText(bundle.getString("puerto"));
        txtServidor.setText(bundle.getString("servidor"));
        txtBaseDatos.setText(bundle.getString("datos"));
        txtUsuario.setText(bundle.getString("usuario"));
    }
    //Evento On Click que conecta con el servidor MySQL y procesa las consultas mostrando los resultados
    public void mostrarResultados(View view)
    {
        try{
            if(edConsulta.getText().toString() != "")
            {
                //Asignamos el driver a una variable de tipo String
                String driver = "com.mysql.jdbc.Driver";
                //Constru�mos la url para establecer la conexi�n
                String urlMySQL = "jdbc:mysql://" + txtServidor.getText().toString() + ":"
                        + txtPuerto.getText().toString() + "/";
                //Cargamos el driver del conector JDBC
                Class.forName(driver).newInstance ();
                //Establecemos la conexi�n con el Servidor MySQL indic�ndole como par�metros la url construida,
                //la Base de Datos a la que vamos a conectarnos, y el usuario y contrase�a de acceso al servidor
                con = DriverManager.getConnection(urlMySQL + baseDatos, txtUsuario.getText().toString()
                        , "110904");
                st = con.createStatement();
                //Se ejecutar� la consulta indicada en el campo edConsulta
                rs = st.executeQuery(edConsulta.getText().toString());
                String resultadoSQL = "";
                Integer numColumnas = 0;

                //Variable que almacenar� el n�mero de columnas obtenidas de la consulta Transact-SQL
                numColumnas = rs.getMetaData().getColumnCount();

                //B�cle encargado de recorrer y mostrar los resultados a partir de la consulta ejecutada
                while (rs.next())
                {
                    for (int i = 1; i <= numColumnas; i++)
                    {
                        if (rs.getObject(i) != null)
                        {
                            if (resultadoSQL != "")
                                if (i < numColumnas)
                                    resultadoSQL = resultadoSQL + rs.getObject(i).toString() + ";";
                                else
                                    resultadoSQL = resultadoSQL + rs.getObject(i).toString();
                            else
                            if (i < numColumnas)
                                resultadoSQL = rs.getObject(i).toString() + ";";
                            else
                                resultadoSQL = rs.getObject(i).toString();
                        }
                        else
                        {
                            if (resultadoSQL != "")
                                resultadoSQL = resultadoSQL + "null;";
                            else
                                resultadoSQL = "null;";
                        }
                    }
                    resultadoSQL = resultadoSQL + "n";
                }
                txtResultados.setText(resultadoSQL);
                st.close();
                rs.close();
            }
        }catch(Exception ex)
        {
            Toast.makeText(this, "Error al obtener resultados de la consulta Transact-SQL: "
                    + ex.getMessage(), Toast.LENGTH_LONG).show();
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