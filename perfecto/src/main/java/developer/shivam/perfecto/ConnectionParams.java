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
        connection.setPostData(data.toString());
        return connection;
    }
}
