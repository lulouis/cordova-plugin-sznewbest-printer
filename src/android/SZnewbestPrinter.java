package marisfrolg.printer;

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
        callbackContext.error("不支持的函数"+action);
        return false;
    }

    private void printGBKText(String text, CallbackContext callbackContext) {
        callbackContext.success("printGBKText发送OK.");
    }

}
