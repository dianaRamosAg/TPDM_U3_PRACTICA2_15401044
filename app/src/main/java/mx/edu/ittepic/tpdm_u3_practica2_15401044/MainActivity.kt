package mx.edu.ittepic.tpdm_u3_practica2_15401044

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.net.URL

class MainActivity : AppCompatActivity() {

    var descripcion : EditText? =null
    var fecha : EditText? = null
    var monto : EditText? = null
    var pagado : EditText? = null
    var insertar : Button? = null
    var cargar : Button?=null
    var etiqueta : TextView?=null
    var jsonRegreso = ArrayList<org.json.JSONObject>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        descripcion = findViewById(R.id.descripcion)
        fecha = findViewById(R.id.fecha)
        monto = findViewById(R.id.monto)
        pagado = findViewById(R.id.pagado)
        insertar = findViewById(R.id.insertar)
        cargar = findViewById(R.id.cargar)
       // etiqueta = findViewById(R.id.etiqueta)


        insertar?.setOnClickListener{

            var conexionWeb = ConexionWeb(this)
            conexionWeb.agregarVariables("descripcion", descripcion?.text.toString())
            conexionWeb.agregarVariables("monto", monto?.text.toString())
            conexionWeb.agregarVariables("fechavencimiento", fecha?.text.toString())
            conexionWeb.agregarVariables("pagado", pagado?.text.toString())

            conexionWeb.execute(URL("https://practica2heroku.herokuapp.com/insertar.html"))
            //formulario de insertar
        }

        cargar?.setOnClickListener {
            var conexionWeb = ConexionWeb(this)
            conexionWeb.execute(URL("https://practica2heroku.herokuapp.com/consultageneral.php"))
        }
    }

    fun mostrarRespuesta(cadena:String){

        var jsonArray = org.json.JSONArray(cadena)
        var total=jsonArray.length()
        (0..total).forEach {
            jsonRegreso.add((jsonArray.getJSONObject(it)))
        }
    }
}
