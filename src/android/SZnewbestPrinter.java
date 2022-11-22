package honeywell.printer;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class SZnewbestPrinter extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("printGBKText")) {
            String text = args.getString(0);
            this.printGBKText(text, callbackContext);
            return true;
        }
        return false;
    }

    private void printGBKText(String text, CallbackContext callbackContext) {
        callbackContext.success("打印指令发送OK.");
    }

    @Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking); 
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onNewIntent() {
        super.onNewIntent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
