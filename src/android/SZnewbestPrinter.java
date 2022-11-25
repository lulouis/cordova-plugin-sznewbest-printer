package marisfrolg.printer;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.smartdevice.aidl.IZKCService;

import android.content.Context;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;

import android.os.SystemClock;
import android.util.Log;

import marisfrolg.printer.helper.PrinterHelper;
import marisfrolg.printer.helper.entity.SupermakerBill;

/**
 * This class echoes a string called from JavaScript.
 */
public class SZnewbestPrinter extends CordovaPlugin {

    public static IZKCService mIzkcService;
    public static int DEVICE_MODEL = 0;
    public static int module_flag = 0;
    private Bitmap mBitmap = null;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if(mIzkcService==null){
            this.onStart();
            SystemClock.sleep(100);
        }

        Log.i("debug",String.format("执行了插件方法:%s",action));
        if (action.equals("printGBKText")) {
            String text = args.getString(0);
            this.printGBKText(text, callbackContext);
            return true;
        }
        if (action.equals("printBarCode")) {
            String text = args.getString(0);
            this.printBarCode(text, callbackContext);
            return true;
        }
        if (action.equals("printQrcode")) {
            String text = args.getString(0);
            this.printQrcode(text, callbackContext);
            return true;
        }
        if (action.equals("printTemplate")) {
            this.printTemplate(callbackContext);
            return true;
        }

        callbackContext.error("不支持的函数"+action);
        return false;
    }

    private void printGBKText(String text, CallbackContext callbackContext) {
        PrinterHelper.getInstance(cordova.getActivity()).printGBKText(mIzkcService, text,callbackContext);
        SystemClock.sleep(100);
        callbackContext.success("printGBKText发送OK.");
    }

    private void printBarCode(String text, CallbackContext callbackContext) {
        PrinterHelper.getInstance(cordova.getActivity()).printBarCode(mIzkcService, text,callbackContext);
        SystemClock.sleep(100);
        callbackContext.success("printBarCode发送OK.");
    }

    private void printQrcode(String text, CallbackContext callbackContext) {
        PrinterHelper.getInstance(cordova.getActivity()).printQrcode(mIzkcService, text,callbackContext);
        SystemClock.sleep(100);
        callbackContext.success("printBarCode发送OK.");
    }


    private void printTemplate(CallbackContext callbackContext) {
        SupermakerBill bill = PrinterHelper.getInstance(cordova.getActivity()).getSupermakerBill(mIzkcService);
        PrinterHelper.getInstance(cordova.getActivity()).printPurchaseBillModelOne(mIzkcService, bill);
        SystemClock.sleep(100);
        callbackContext.success("printTemplate发送OK.");
    }

    @Override
    public void onPause(boolean multitasking) {
        Log.i("info",String.format("执行了插件方法:%s","onPause"));
        super.onPause(multitasking);
    }

    @Override
    public void onResume(boolean multitasking) {
        bindService();
        Log.i("info",String.format("执行了插件方法:%s","onResume"));
        super.onResume(multitasking);
    }

    @Override
    public void onStart() {
        module_flag = cordova.getActivity().getIntent().getIntExtra("module_flag", 8);
        bindService();
        Log.i("info",String.format("执行了插件方法:%s","onStart"));
        super.onStart();
    }

    @Override
    public void onStop() {
        unbindService();
        Log.i("info",String.format("执行了插件方法:%s","onStop"));
        super.onStop();
    }

    @Override
    public void onDestroy() {
        unbindService();
        Log.i("info",String.format("执行了插件方法:%s","onDestroy"));
        super.onDestroy();
    }

    public void bindService() {
        //com.zkc.aidl.all为远程服务的名称，不可更改
        //com.smartdevice.aidl为远程服务声明所在的包名，不可更改，
        // 对应的项目所导入的AIDL文件也应该在该包名下
        Intent intent = new Intent("com.zkc.aidl.all");
        intent.setPackage("com.smartdevice.aidl");
        cordova.getActivity().getBaseContext().bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    public void unbindService() {
        if (mServiceConn!=null)
        cordova.getActivity().getBaseContext().unbindService(mServiceConn);
    }

    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("client", "onServiceDisconnected");
            mIzkcService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("client", "onServiceConnected");
            mIzkcService = IZKCService.Stub.asInterface(service);
            if(mIzkcService!=null){
                try {
                    DEVICE_MODEL = mIzkcService.getDeviceModel();
                    mIzkcService.setModuleFlag(module_flag);
                    if(module_flag==3){
                        mIzkcService.openBackLight(1);
                    }
                } catch (android.os.RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };


}
