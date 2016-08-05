package developer.shivam.perfecto;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connection {

    private Connection connection = null;
    private Context context = null;
    private String urlString = "";

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

    public Connection fromUrl(String url) {
        connection.urlString = url;
        return connection;
    }

    public void connect(final OnRequestComplete requestComplete){
        if (connection.urlString.equals("")) {
            Printer.writeError("Empty url");
        } else {

            new AsyncTask<String, String, String>() {

                String responseMessage = "";

                @Override
                protected String doInBackground(String... strings) {
                    try {
                        URL url = new URL(connection.urlString);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.connect();

                        if (connection.getResponseCode() == 200) {
                            responseMessage = ConvertInputStream.toString(connection.getInputStream());
                            return "success";
                        } else {
                            responseMessage = connection.getErrorStream().toString();
                            return "failed";
                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);

                    if (s.equals("success")) {
                        requestComplete.onComplete(responseMessage);
                    } else {
                        requestComplete.onFailure(responseMessage);
                    }
                }
            }.execute();
        }
    }
}
