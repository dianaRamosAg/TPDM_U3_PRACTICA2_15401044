package mx.edu.ittepic.tpdm_u3_practica2_15401044

import android.app.ProgressDialog
import android.os.AsyncTask
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class ConexionWeb(p:MainActivity) :AsyncTask<URL, Void, String>(){
    var puntero=p
    var variablesEnvio=ArrayList<String>()
    var dialogo=ProgressDialog(puntero)

    override fun onPreExecute() {
        super.onPreExecute()

        dialogo.setTitle("ATENCION")
        dialogo.setMessage("Conectando con el servidor ...")
        dialogo.show()
    }

    fun agregarVariables(clave:String,valor:String){
        var cad = clave+"&"+valor
        variablesEnvio.add(cad)
    }

    override fun doInBackground(vararg p0: URL?): String {
        var respuesta=""
        var cadenaEnvioPOST=""
        var total = variablesEnvio.size-1
        (0..total).forEach {
            try {
                var data=variablesEnvio.get(it).split("&")
                cadenaEnvioPOST +=data[0]+"="+URLEncoder.encode(data[1],"utf-8")+" "
            }catch (err:UnsupportedEncodingException){
                respuesta="error en codificacion url"
            }

        }

        cadenaEnvioPOST=cadenaEnvioPOST.trim()
        cadenaEnvioPOST=cadenaEnvioPOST.replace(" ","&")

        var conexion: HttpURLConnection?=null
        try {

            conexion = p0[0]?.openConnection() as HttpURLConnection
            conexion?.doOutput = true
            conexion?.setFixedLengthStreamingMode(cadenaEnvioPOST.length)
            conexion?.requestMethod = "POST"
            conexion?.setRequestProperty("Conent-Type","application/x-www*form-encoded")


            var salida = BufferedOutputStream(conexion?.outputStream)

            salida.write(cadenaEnvioPOST.toByteArray())

            salida.flush()
            salida.close()

            if(conexion?.responseCode==200){
                var flujoEntrada = InputStreamReader(conexion?.inputStream, "UTF-8")
                var entrada = BufferedReader(flujoEntrada)
                respuesta = """${entrada.readLine()}"""
                entrada.close()

            }else{
                respuesta = "Error"+conexion?.responseCode

            }


        }catch(err:IOException){
            respuesta="ERROR EN FLUJO ENTRADA O SALIDA"
        }finally {
            if(conexion!=null){
                conexion?.disconnect()
            }
        }

        return respuesta
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        dialogo.dismiss()
        puntero.mostrarRespuesta(result!!)
    }

}