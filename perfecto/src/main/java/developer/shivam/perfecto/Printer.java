package developer.shivam.perfecto;

import android.util.Log;

public class Printer {

    public static void writeMessage(String message) {
        Log.d("Message", message);
    }

    public static void writeError(String error) {
        Log.d("Error", error);
    }
}
