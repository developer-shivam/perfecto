package developer.shivam.perfecto;

import android.app.Activity;
import android.content.Context;

public class Perfecto {

    volatile static Connection connection = null;

    public static Connection with(Activity activity) {
        connection = new Connection(activity);
        connection.setSingletonInstance(connection);
        return connection;
    }
}
