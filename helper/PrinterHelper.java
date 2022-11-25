package marisfrolg.printer.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.os.SystemClock;

import com.smartdevice.aidl.IZKCService;

import org.apache.cordova.CallbackContext;

import marisfrolg.printer.helper.entity.GoodsInfo;
import marisfrolg.printer.helper.entity.SupermakerBill;


public class PrinterHelper {

	/* 等待打印缓冲刷新的时间 */
	private static final int mIzkcService_BUFFER_FLUSH_WAITTIME = 150;
	/* 分割线 */
	private static final String mIzkcService_CUT_OFF_RULE = "--------------------------------\n";


	private Context mContext;

	private static PrinterHelper _instance;

	private PrinterHelper(Context mContext) {
		this.mContext = mContext;
	}

	synchronized public static PrinterHelper getInstance(Context mContext) {
		if (null == _instance)
			_instance = new PrinterHelper(mContext);
		return _instance;
	}


    synchronized public void printGBKText(IZKCService mIzkcService,String text, CallbackContext callbackContext) {
        try {
            if (mIzkcService!=null&&mIzkcService.checkPrinterAvailable()) {
                mIzkcService.printGBKText("\n\n");
                SystemClock.sleep(50);
                mIzkcService.printGBKText(text);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        callbackContext.success("createBarCode发送OK.");
    }

    synchronized public void printBarCode(IZKCService mIzkcService,String text, CallbackContext callbackContext) {
        try {
            Bitmap btMap = null;

            if (mIzkcService!=null&&mIzkcService.checkPrinterAvailable()) {
                btMap = mIzkcService.createBarCode(text, 8, 384, 60, true);
            }
            if (btMap != null) {
                mIzkcService.printGBKText("\n\n");
                SystemClock.sleep(50);
                mIzkcService.printBitmap(btMap);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        callbackContext.success("createBarCode发送OK.");
    }

	synchronized public void printQrcode(IZKCService mIzkcService,String text, CallbackContext callbackContext) {
		try {
			Bitmap btMap = null;

			if (mIzkcService!=null&&mIzkcService.checkPrinterAvailable()) {
				btMap = mIzkcService.createQRCode(text, 100, 100);
			}
			if (btMap != null) {
				mIzkcService.printGBKText("\n\n");
				SystemClock.sleep(50);
				mIzkcService.printBitmap(btMap);
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}
		callbackContext.success("createBarCode发送OK.");
	}

	synchronized public void printPurchaseBillModelOne(IZKCService mIzkcService, SupermakerBill bill) {

		try {
			if (mIzkcService!=null&&mIzkcService.checkPrinterAvailable()) {
				mIzkcService.printGBKText("\n\n");
				SystemClock.sleep(50);

				mIzkcService.printGBKText(PrintTag.PurchaseBillTag.SERIAL_NUMBER_TAG
						+ bill.batch_number + "\t\t\n" + bill.operate_time
						+ "\n");

				mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);


				for (int i = 0; i < bill.goosInfos.size(); i++) {
					if (i==0){
						mIzkcService.printGBKText(PrintTag.PurchaseBillTag.title1
								+ "        " + PrintTag.PurchaseBillTag.title2 + "  "
								+ PrintTag.PurchaseBillTag.title3 + "  " + "\n");

						mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);
					}

					String column1 = bill.goosInfos.get(i).column1;
					String column2 = bill.goosInfos.get(i).column2;
					String column3 = bill.goosInfos.get(i).column3;

					String space1 = "";
					String space2 = "";
					String space3 = "";

					int column1_length = column1.length();
					int column2_length = column2.length();
					int column3_length = column3.length();

					int space_length1 = 12 - column1_length;
					int space_length2 = 8 - column2_length;
					int space_length3 = 8 - column3_length;

					String name1 = "";
					String name2 = "";

					if (column1_length > 12) {
						name1 = column1.substring(0, 12);
						name2 = column1.substring(12, column1_length);

						for (int j = 0; j < space_length1; j++) {
							space1 += "  ";
						}
						for (int j = 0; j < space_length2; j++) {
							space2 += " ";
						}
						for (int j = 0; j < space_length3; j++) {
							space3 += " ";
						}

						mIzkcService.printGBKText(name1 + space1 + " " + column2 + space2 + column3 + "\n");
						mIzkcService.printGBKText(name2 + "\n");

					} else {
						for (int j = 0; j < space_length1; j++) {
							space1 += "  ";
						}
						for (int j = 0; j < space_length2; j++) {
							space2 += " ";
						}
						for (int j = 0; j < space_length3; j++) {
							space3 += " ";
						}

						mIzkcService.printGBKText(column1 + space1 + " " + column2 + space2 + column3 + "\n");
					}
				}

				mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);

				mIzkcService.printGBKText(PrintTag.PurchaseBillTag.label1
						+ bill.amount1 + "\n");
				mIzkcService.printGBKText(PrintTag.PurchaseBillTag.label2
						+ bill.amount2 + "\n");
				mIzkcService.printGBKText(PrintTag.PurchaseBillTag.label3
						+ bill.amount3 + "\n");
				mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);

				if (bill.end_bitmap != null) {
					SystemClock.sleep(200);
                    mIzkcService.printBitmap(bill.end_bitmap);
				}
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SupermakerBill getSupermakerBill(IZKCService mIzkcService) {
		SupermakerBill bill = new SupermakerBill();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		String dateStr = sdf.format(date);

		bill.batch_number = System.currentTimeMillis()+"";
		bill.operate_time = dateStr;
		bill.amount1 = "111111111111";
		bill.amount2 = "1600";
		bill.amount3 = "36000";


//		generalBitmap(mIzkcService, bill);
		addGoodsInfo(bill.goosInfos);

		return bill;

	}

	private void generalBitmap(IZKCService mIzkcService, SupermakerBill bill) {
		Bitmap btMap;
		try {
			btMap = mIzkcService.createQRCode("扫描关注本店，有惊喜喔", 240, 240);
			bill.end_bitmap = btMap;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addGoodsInfo(ArrayList<GoodsInfo> goosInfos) {

		GoodsInfo goodsInfo0 = new GoodsInfo();
		goodsInfo0.column1 = "A3232VI01001";
		goodsInfo0.column2 = "14.5";
		goodsInfo0.column3 = "100";
		goodsInfo0.column4 = "29";
		
		GoodsInfo goodsInfo1 = new GoodsInfo();
		goodsInfo1.column1 = "A3232VI01002";
		goodsInfo1.column2 = "2.5";
		goodsInfo1.column3 = "12";
		goodsInfo1.column4 = "30";

		goosInfos.add(goodsInfo0);
		goosInfos.add(goodsInfo1);

	}

}
