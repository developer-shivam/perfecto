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
    private Context mContext = null;
    private String urlString = "";

    /**
     * By default request type is GET
     */
    private String requestType = RequestType.GET;
    private String jsonData = "";

    Connection(Context context) {
        if (context == null) {
            Printer.writeError("Context cannot be null");
        } else {
            this.mContext = context;
        }
    }

    void setSingletonInstance(Connection connection) {
        this.connection = connection;
    }

    public ConnectionParams fromUrl(String url) {
        connection.urlString = url;
        ConnectionParams connectionParams = new ConnectionParams();
        connectionParams.setConnectionInstance(connection);
        return connectionParams;
    }

    public void connect(final OnNetworkRequest requestComplete) {
        if (connection.urlString.equals("") || connection.urlString == null) {
            Printer.writeError("URL cannot be empty");
        } else {
            if (isNetworkAvailable()) {
                new AsyncTask<String, String, String>() {

                    String responseMessage = "";
                    String errorStream = "";
                    int responseCode = 0;

                    @Override
                    protected String doInBackground(String... strings) {

                        try {
                            Printer.writeMessage(">>>>>>>>>>START OF NETWORK REQUEST>>>>>>>>>>");
                            URL url = new URL(connection.urlString);
                            Log.d("Connecting to:", connection.urlString);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod(getRequestType());

                            if (getRequestType().equals(RequestType.GET)) {
                                connection.setReadTimeout(10000);
                                connection.setConnectTimeout(10000);

                            } else if (getRequestType().equals(RequestType.POST)
                                    || getRequestType().equals(RequestType.PUT)
                                    || getRequestType().equals(RequestType.DELETE)) {
                                connection.setReadTimeout(10000);
                                connection.setConnectTimeout(10000);

                                connection.setDoInput(true);
                                connection.setDoOutput(true);

                                connection.setFixedLengthStreamingMode(getJsonData().getBytes().length);
                                connection.setRequestProperty("Content-type", "application/json");

                                OutputStream os = new BufferedOutputStream(connection.getOutputStream());
                                os.write(getJsonData().getBytes());
                                os.flush();
                            }

                            Log.d("Request type:", getRequestType());
                            Log.d("Read timeout:", String.valueOf(connection.getReadTimeout()) + " milliseconds");
                            Log.d("Connection timeout:", String.valueOf(connection.getConnectTimeout()) + " milliseconds");

                            if (!isNetworkAvailable()) {
                                return "failed";
                            }

                            if (connection.getResponseCode() == 200) {
                                responseCode = connection.getResponseCode();
                                responseMessage = ConvertInputStream.toString(connection.getInputStream());
                                return "success";
                            } else {
                                responseCode = connection.getResponseCode();
                                responseMessage = connection.getResponseMessage();
                                errorStream = connection.getErrorStream().toString();
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
                            requestComplete.onFailure(responseCode, responseMessage, errorStream);
                        }
                    }
                }.execute();
            } else {
                requestComplete.onFailure(0, "No connected to network", "No internet connection available");
            }
        }
    }

    private String handleError(Exception e) {
        e.printStackTrace();
        if (e instanceof NetworkErrorException) {
            Printer.writeError("NetworkErrorException", mContext, R.string.network_error);
        } else if (e instanceof MalformedURLException) {
            Printer.writeError("MalformedURLException", mContext, R.string.malformed_url);
        } else if (e instanceof SSLException) {
            Printer.writeError("SSLException", mContext, R.string.ssl_error);
        } else if (e instanceof SocketTimeoutException) {
            Printer.writeError("SocketTimeoutException", mContext, R.string.socket_timeout_error);
        } else if (e instanceof IOException) {
            Printer.writeError("IOException", mContext, R.string.io_error);
        } else {
            Printer.writeError("UnknownError", mContext, R.string.error);
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
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
