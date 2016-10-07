package developer.shivam.perfecto;

import org.json.JSONObject;

public class ConnectionParams {

    private Connection connection;

    public void setConnectionInstance(Connection connectionInstance) {
        connection = connectionInstance;
    }

    public Connection ofTypeGet(){
        connection.setRequestType("GET");
        return connection;
    }

    public Connection ofTypePost(JSONObject data){
        connection.setRequestType("POST");
        connection.setJsonData(data.toString());
        return connection;
    }

    public Connection ofTypePut(JSONObject data){
        connection.setRequestType("PUT");
        connection.setJsonData(data.toString());
        return connection;
    }

    public Connection ofTypeDelete(JSONObject data){
        connection.setRequestType("DELETE");
        connection.setJsonData(data.toString());
        return connection;
    }

    public Connection withToasts(){
        connection.setWithToasts(true);
        return connection;
    }
}
