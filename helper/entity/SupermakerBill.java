package marisfrolg.printer.helper.entity;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class SupermakerBill {

	public String batch_number;
	public String operate_time;

	public String amount1;
	public String amount2;
	public String amount3;

	public Bitmap end_bitmap;
	
	public ArrayList<GoodsInfo> goosInfos = new ArrayList<GoodsInfo>();
}
