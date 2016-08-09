package developer.shivam.perfecto;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class Connection {

    private Connection connection = null;
    private Context context = null;
    private String urlString = "";
    private ConnectionParams connectionParams;
    ProgressDialog progressDialog = null;

    /**
     * By default request type is GET
     */
    private String requestType = "GET";
    private String postData = "";

    public Connection(Context context) {

        if (context == null) {
            Printer.writeError("Context cannot be null");
        } else {
            this.context = context;
        }
    }

    public void setSingletonInstance(Connection connection) {
        this.connection = connection;
    }

    public ConnectionParams fromUrl(String url) {
        connection.urlString = url;
        connectionParams = new ConnectionParams();
        connectionParams.setConnectionInstance(connection);
        return connectionParams;
    }

    public void connect(final OnRequestComplete requestComplete){
        if (connection.urlString.equals("")) {
            Printer.writeError("Empty url");
        } else {

            new AsyncTask<String, String, String>() {

                String responseMessage = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    /*progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();*/
                }

                @Override
                protected String doInBackground(String... strings) {
                    try {
                        URL url = new URL(connection.urlString);
                        Log.d("URL:", connection.urlString);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod(getRequestType());

                        if (getRequestType().equals("GET")) {
                            connection.setReadTimeout(5000);
                            connection.setConnectTimeout(5000);

                        } else if (getRequestType().equals("POST")) {
                            connection.setReadTimeout(5000);
                            connection.setConnectTimeout(5000);

                            connection.setDoInput(true);
                            connection.setDoOutput(true);

                            connection.setFixedLengthStreamingMode(getPostData().getBytes().length);
                            connection.setRequestProperty("Content-type", "application/json");

                            OutputStream os = new BufferedOutputStream(connection.getOutputStream());
                            os.write(getPostData().getBytes());
                            os.flush();
                        }

                        connection.connect();

                        if (connection.getResponseCode() == 200) {
                            responseMessage = ConvertInputStream.toString(connection.getInputStream());
                            return "success";
                        } else {
                            responseMessage = connection.getErrorStream().toString() + " " + connection.getResponseMessage() + " " + connection.getResponseCode();
                            return "failed";
                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        return "failed";
                    } catch (SocketTimeoutException e) {
                        e.printStackTrace();
                        return "failed";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);

                    if (s.equals("success")) {
                        requestComplete.onSuccess(responseMessage);
                    } else {
                        requestComplete.onFailure(responseMessage);
                    }

                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {

                        @Override
                        public void run() {
                            //progressDialog.dismiss();
                        }
                    };

                    handler.postDelayed(runnable, 1000);
                }
            }.execute();
        }
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public void setPostData(String data){
        this.postData = data;
    }

    public String getPostData() {
        return postData;
    }
}
