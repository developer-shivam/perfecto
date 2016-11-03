package developer.shivam.perfecto;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Printer {

    private static Toast currentToast;

    public static void writeMessage(String message) {
        Log.d("Message", message);
    }

    // Context used as method arg instead of static field due to possible memory leaks
    static void writeError(Context context, int messageResourceId){
        if (currentToast!=null) currentToast.cancel();
        currentToast = Toast.makeText(context, context.getString(messageResourceId), Toast.LENGTH_LONG);
        currentToast.show();
    }

    public static void writeError(String error) {
        Log.d("Error", error);
    }

    public static void writeError(String error, Context mContext, int messageResourceId) {
        Log.d(error, mContext.getString(messageResourceId));
    }
}
