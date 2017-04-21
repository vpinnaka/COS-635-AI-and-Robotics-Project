package android.app.com.emilyrobot;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;

/**
 * Created by vinay on 4/20/2017.
 */

public class NetworkConnection extends AsyncTask<Void, Void, Void> {

    private final String ack_message = "OK from EMILY interface";

    String IPaddress;
    int Port;
    String response = "";


    public NetworkConnection(String IPaddress, int Port){
        this.IPaddress = IPaddress;
        this.Port = Port;

    }

    private JSONObject convertStringToJson(String data) throws JSONException {
        JSONObject jsondata = new JSONObject(data);

        return jsondata;
    }

    private void parseJSONdata(JSONObject jsonData){

        Iterator<String> keys_iter = jsonData.keys();

        while (keys_iter.hasNext()){

               String key = keys_iter.next();
               try {
                   Object value = jsonData.get(key);

                   Mydata.setCurrentStatus(key, value);


                   //if(key.equals("battary"))
                       //Log.i("battary",value.toString());
                        //Mydata.setBatteryStatus((int)value);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }


            //Boolean armed = jsonData.getBoolean("armed");

            //Mydata.setBatteryStatus(jsonData.getInt("battary"));



    }



    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Socket socket = null;

        try{
            socket = new Socket(IPaddress, Port);

            OutputStream outputStream = socket.getOutputStream();

            byte[] buffer = new byte[2048];

            int bytesRead;
            InputStream inputStream = socket.getInputStream();

            outputStream.write(ack_message.getBytes(), 0, ack_message.length());

			/*
             * notice: inputStream.read() will block if no data return
			 */
            while ((bytesRead = inputStream.read(buffer)) != -1) {

                try {
                    JSONObject jsonData = convertStringToJson(new String(buffer));

                    parseJSONdata(jsonData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


}
