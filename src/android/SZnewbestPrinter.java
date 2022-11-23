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
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

/**
 * This class echoes a string called from JavaScript.
 */
public class SZnewbestPrinter extends CordovaPlugin {

    public static IZKCService mIzkcService;
    public static int DEVICE_MODEL = 0;
    public static int module_flag = 0;

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

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.onStart();
        SystemClock.sleep(500);
        Log.i("debug",String.format("执行了插件方法:%s",action));
        if (action.equals("printGBKText")) {
            String text = args.getString(0);
            this.printGBKText(text, callbackContext);
            return true;
        }
        if (action.equals("createBarCode")) {
            String text = args.getString(0);
            this.createBarCode("A1154005501", callbackContext);
            return true;
        }

        callbackContext.error("不支持的函数"+action);
        return false;
    }

    private void printGBKText(String text, CallbackContext callbackContext) {
        try {

            //mIzkcService.sendRAWData("print",new byte[]{0x1B,0x74,0x0F});
            mIzkcService.setPrinterLanguage("CP936",15);

            mIzkcService.printGBKText("GBK编码打印："+text);

            byte[] buffer = new byte[]{(byte) 0x1C, (byte) 0x43, (byte) 0xFF};
            mIzkcService.sendRAWData("print", buffer);
            SystemClock.sleep(100);



        } catch (RemoteException e) {
            e.printStackTrace();
        }
        callbackContext.success("printGBKText发送OK.");
    }

    private void createBarCode(String text, CallbackContext callbackContext) {
        try {

            //mIzkcService.sendRAWData("print",new byte[]{0x1B,0x74,0x0F});
            mIzkcService.setPrinterLanguage("CP936",15);

            mIzkcService.createBarCode(text,8,3,162,true);

            byte[] buffer = new byte[]{(byte) 0x1C, (byte) 0x43, (byte) 0xFF};
            mIzkcService.sendRAWData("print", buffer);
            SystemClock.sleep(100);



        } catch (RemoteException e) {
            e.printStackTrace();
        }
        callbackContext.success("createBarCode发送OK.");
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
        //cordova.getActivity().getBaseContext().unbindService(mServiceConn);
    }


}
