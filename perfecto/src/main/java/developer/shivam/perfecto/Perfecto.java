package developer.shivam.perfecto;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

public class Perfecto {

    public static Connection with(Context context) {
        Connection connection = new Connection(context);
        connection.setSingletonInstance(connection);
        return connection;
    }

    public static Connection with(Activity activity) {
        Connection connection = new Connection(activity);
        connection.setSingletonInstance(connection);
        return connection;
    }

    public static Connection with(Fragment fragment) {
        Connection connection = new Connection(fragment.getActivity());
        connection.setSingletonInstance(connection);
        return connection;
    }
}
