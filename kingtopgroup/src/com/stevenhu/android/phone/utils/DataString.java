package com.stevenhu.android.phone.utils;

import java.util.Calendar;
import java.util.TimeZone;

public class DataString {
	    private static String mYear;  
	    private static String mMonth;  
	    private static String mDay;  
	    private static String mWay;  
	      
	    public static String StringData(){  
	        final Calendar c = Calendar.getInstance();  
	        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));  
	        mYear = String.valueOf(c.get(Calendar.YEAR)); // ��ȡ��ǰ���  
	        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// ��ȡ��ǰ�·�  
	        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// ��ȡ��ǰ�·ݵ����ں���  
	        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));  
	        if("1".equals(mWay)){  
	            mWay ="��";  
	        }else if("2".equals(mWay)){  
	            mWay ="һ";  
	        }else if("3".equals(mWay)){  
	            mWay ="��";  
	        }else if("4".equals(mWay)){  
	            mWay ="��";  
	        }else if("5".equals(mWay)){  
	            mWay ="��";  
	        }else if("6".equals(mWay)){  
	            mWay ="��";  
	        }else if("7".equals(mWay)){  
	            mWay ="��";  
	        }  
	        return mYear + "��" + mMonth + "��" + mDay+"��"+"/����"+mWay;  
	    }  
	      
}
