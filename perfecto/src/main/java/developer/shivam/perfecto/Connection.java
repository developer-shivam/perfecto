package developer.shivam.perfecto;

import android.accounts.NetworkErrorException;
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

    private boolean withToasts = false;

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

    public void connect(final OnRequestComplete requestComplete) {
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
                                Printer.toastError(context, R.string.server_error);
                                return "failed";
                            }

                        } catch (MalformedURLException e) {
                            responseMessage = handleError(e);
                            return "failed";
                        } catch (SSLException e) {
                            responseMessage = handleError(e);
                            return "failed";
                        } catch (SocketTimeoutException e) {
                            responseMessage = handleError(e);
                            return "failed";
                        } catch (IOException e) {
                            responseMessage = handleError(e);
                            return "failed";
                        } catch (Exception e) {
                            responseMessage = handleError(e);
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
                Printer.toastError(context, R.string.network_error);
                requestComplete.onFailure("Not connected to internet");
            }
        }
    }

    private String handleError(Exception e) {
        e.printStackTrace();
        if (withToasts) {
            if (e instanceof NetworkErrorException) {
                Printer.toastError(context, R.string.network_error);
            } else if (e instanceof MalformedURLException) {
                Printer.toastError(context, R.string.malformed_url);
            } else if (e instanceof SSLException) {
                Printer.toastError(context, R.string.ssl_error);
            } else if (e instanceof SocketTimeoutException) {
                Printer.toastError(context, R.string.socket_timeout_error);
            } else if (e instanceof IOException) {
                Printer.toastError(context, R.string.io_error);
            } else {
                Printer.toastError(context, R.string.error);
            }

        }
        return e.getMessage();
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public void setJsonData(String data) {
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

    void setWithToasts(boolean withToasts) {
        this.withToasts = withToasts;
    }
}
