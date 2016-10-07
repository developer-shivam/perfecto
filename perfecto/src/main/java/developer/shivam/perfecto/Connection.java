package developer.shivam.perfecto;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.net.ssl.SSLException;

public class Connection {

    private Connection connection = null;
    private Context context = null;
    private String urlString = "";
    private ConnectionParams connectionParams;

    /**
     * By default request type is GET
     */
    private String requestType = "GET";
    private String jsonData = "";

    Connection(Context context) {
        if (context == null) {
            Printer.writeError("Context cannot be null");
        } else {
            this.context = context;
        }
    }

    void setSingletonInstance(Connection connection) {
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

            if (isNetworkAvailable()) {

                new AsyncTask<String, String, String>() {

                    String responseMessage = "";

                    @Override
                    protected String doInBackground(String... strings) {

                        try {
                            Printer.writeMessage(">>>>>>>>>>START OF NETWORK REQUEST>>>>>>>>>>");
                            URL url = new URL(connection.urlString);
                            Log.d("Connecting to:", connection.urlString);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod(getRequestType());

                            if (getRequestType().equals("GET")) {
                                connection.setReadTimeout(5000);
                                connection.setConnectTimeout(5000);

                            } else if (getRequestType().equals("POST")
                                    || getRequestType().equals("PUT")
                                    || getRequestType().equals("DELETE")) {
                                connection.setReadTimeout(5000);
                                connection.setConnectTimeout(5000);

                                connection.setDoInput(true);
                                connection.setDoOutput(true);

                                connection.setFixedLengthStreamingMode(getJsonData().getBytes().length);
                                connection.setRequestProperty("Content-type", "application/json");

                                OutputStream os = new BufferedOutputStream(connection.getOutputStream());
                                os.write(getJsonData().getBytes());
                                os.flush();
                            }

                            Log.d("Request type:", getRequestType());
                            Log.d("Read timeout:", String.valueOf(connection.getReadTimeout()) + "milliseconds");
                            Log.d("Connection timeout:", String.valueOf(connection.getConnectTimeout()) + "milliseconds");

                            if (isNetworkAvailable()) {
                                connection.connect();
                                return "failed";
                            }

                            // For 2XX response codes
                            if (connection.getResponseCode() / 100 == 2) {
                                responseMessage = ConvertInputStream.toString(connection.getInputStream());
                                return "success";
                            } else {
                                responseMessage = connection.getErrorStream().toString() + " " + connection.getResponseMessage() + " " + connection.getResponseCode();
                                return "failed";
                            }

                        } catch (MalformedURLException e) {
                            responseMessage = e.getMessage();
                            e.printStackTrace();
                            return "failed";
                        } catch (SSLException e) {
                            responseMessage = e.getMessage();
                            e.printStackTrace();
                            return "failed";
                        } catch (SocketTimeoutException e) {
                            responseMessage = e.getMessage();
                            e.printStackTrace();
                            return "failed";
                        } catch (IOException e) {
                            responseMessage = e.getMessage();
                            e.printStackTrace();
                            return "failed";
                        } catch (Exception e) {
                            responseMessage = e.getMessage();
                            e.printStackTrace();
                            return "failed";
                        }
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);

                        if (s.equals("success")) {
                            Printer.writeMessage(">>>>>>>>>>END OF NETWORK REQUEST>>>>>>>>>>");
                            requestComplete.onSuccess(responseMessage);
                        } else {
                            Printer.writeMessage(">>>>>>>>>>END OF NETWORK REQUEST>>>>>>>>>>");
                            requestComplete.onFailure(responseMessage);
                        }
                    }
                }.execute();
            } else {
                requestComplete.onFailure("Not connected to internet");
            }
        }
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public void setJsonData(String data){
        this.jsonData = data;
    }

    public String getJsonData() {
        return jsonData;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
