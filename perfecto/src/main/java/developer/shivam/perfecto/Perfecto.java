package developer.shivam.perfecto;

import android.content.Context;

public class Perfecto {

    volatile static Connection connection = null;

    public static Connection with(Context context) {
        connection = new Connection(context);
        connection.setSingletonInstance(connection);
        return connection;
    }
}
