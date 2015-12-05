package com.serveroverload.networkmaster.network;

import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

//Taken form http://stackoverflow.com/questions/14517338/android-check-whether-the-phone-is-dual-sim

public final class TelephonyDualSimInfo {

	private static final String TAG = "TelephonyDualSimInfo";
	private static TelephonyDualSimInfo telephonyInfo;
	private String imsiSIM1;
	private String imsiSIM2;
	private boolean isSIM1Ready;
	private boolean isSIM2Ready;

	public String getImsiSIM1() {
		return imsiSIM1;
	}

	/*
	 * public static void setImsiSIM1(String imsiSIM1) { TelephonyInfo.imsiSIM1
	 * = imsiSIM1; }
	 */

	public String getImsiSIM2() {
		return imsiSIM2;
	}

	/*
	 * public static void setImsiSIM2(String imsiSIM2) { TelephonyInfo.imsiSIM2
	 * = imsiSIM2; }
	 */

	public boolean isSIM1Ready() {
		return isSIM1Ready;
	}

	/*
	 * public static void setSIM1Ready(boolean isSIM1Ready) {
	 * TelephonyInfo.isSIM1Ready = isSIM1Ready; }
	 */

	public boolean isSIM2Ready() {
		return isSIM2Ready;
	}

	/*
	 * public static void setSIM2Ready(boolean isSIM2Ready) {
	 * TelephonyInfo.isSIM2Ready = isSIM2Ready; }
	 */

	public boolean isDualSIM() {
		return imsiSIM2 != null;
	}

	private TelephonyDualSimInfo() {
	}

	public static TelephonyDualSimInfo getInstance(Context context) {

		if (telephonyInfo == null) {

			telephonyInfo = new TelephonyDualSimInfo();

			TelephonyManager telephonyManager = ((TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE));

			telephonyInfo.imsiSIM1 = telephonyManager.getDeviceId();
			;
			telephonyInfo.imsiSIM2 = null;

			try {
				telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context,
						"getDeviceIdGemini", 0);
				telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context,
						"getDeviceIdGemini", 1);
			} catch (GeminiMethodNotFoundException e) {
				e.printStackTrace();

				try {
					telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context,
							"getDeviceId", 0);
					telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context,
							"getDeviceId", 1);
				} catch (GeminiMethodNotFoundException e1) {
					// Call here for next manufacturer's predicted method name
					// if you wish
					e1.printStackTrace();
				}
			}

			telephonyInfo.isSIM1Ready = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
			telephonyInfo.isSIM2Ready = false;

			try {
				telephonyInfo.isSIM1Ready = getSIMStateBySlot(context,
						"getSimStateGemini", 0);
				telephonyInfo.isSIM2Ready = getSIMStateBySlot(context,
						"getSimStateGemini", 1);
			} catch (GeminiMethodNotFoundException e) {

				e.printStackTrace();

				try {
					telephonyInfo.isSIM1Ready = getSIMStateBySlot(context,
							"getSimState", 0);
					telephonyInfo.isSIM2Ready = getSIMStateBySlot(context,
							"getSimState", 1);
				} catch (GeminiMethodNotFoundException e1) {
					// Call here for next manufacturer's predicted method name
					// if you wish
					e1.printStackTrace();
				}
			}
		}

		return telephonyInfo;
	}

	private static String getDeviceIdBySlot(Context context,
			String predictedMethodName, int slotID)
			throws GeminiMethodNotFoundException {

		String imsi = null;

		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		try {

			Class<?> telephonyClass = Class.forName(telephony.getClass()
					.getName());

			Class<?>[] parameter = new Class[1];
			parameter[0] = int.class;
			Method getSimID = telephonyClass.getMethod(predictedMethodName,
					parameter);

			Object[] obParameter = new Object[1];
			obParameter[0] = slotID;
			Object ob_phone = getSimID.invoke(telephony, obParameter);

			if (ob_phone != null) {
				imsi = ob_phone.toString();

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GeminiMethodNotFoundException(predictedMethodName);
		}

		return imsi;
	}

	private static boolean getSIMStateBySlot(Context context,
			String predictedMethodName, int slotID)
			throws GeminiMethodNotFoundException {

		boolean isReady = false;

		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		try {

			Class<?> telephonyClass = Class.forName(telephony.getClass()
					.getName());

			Class<?>[] parameter = new Class[1];
			parameter[0] = int.class;
			Method getSimStateGemini = telephonyClass.getMethod(
					predictedMethodName, parameter);

			Object[] obParameter = new Object[1];
			obParameter[0] = slotID;
			Object ob_phone = getSimStateGemini.invoke(telephony, obParameter);

			if (ob_phone != null) {
				int simState = Integer.parseInt(ob_phone.toString());
				if (simState == TelephonyManager.SIM_STATE_READY) {
					isReady = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GeminiMethodNotFoundException(predictedMethodName);
		}

		return isReady;
	}

	private static class GeminiMethodNotFoundException extends Exception {

		private static final long serialVersionUID = -996812356902545308L;

		public GeminiMethodNotFoundException(String info) {
			super(info);
		}
	}

	public static void printTelephonyManagerMethodNamesForThisDevice(
			Context context) {

		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		Class<?> telephonyClass;
		try {
			telephonyClass = Class.forName(telephony.getClass().getName());
			Method[] methods = telephonyClass.getMethods();
			for (int idx = 0; idx < methods.length; idx++) {

				System.out.println("\n" + methods[idx] + " declared by "
						+ methods[idx].getDeclaringClass());
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	public static String api23DualSImDetection(Context context) {

		if (android.os.Build.VERSION.SDK_INT >= 23) {

			TelephonyManager telephony = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			return String.valueOf(telephony.getPhoneCount());
		}
		return "NA";

	}


	
	//http://stackoverflow.com/questions/14517338/android-check-whether-the-phone-is-dual-sim
	public static void samsungTwoSims(Context context) {
	    TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	 
	    try{ 
	 
	        Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
	 
	        Class<?>[] parameter = new Class[1];
	        parameter[0] = int.class;
	        Method getFirstMethod = telephonyClass.getMethod("getDefault", parameter);
	 
	        Log.d(TAG, getFirstMethod.toString());
	 
	        Object[] obParameter = new Object[1];
	        obParameter[0] = 0;
	        TelephonyManager first = (TelephonyManager) getFirstMethod.invoke(null, obParameter);
	 
	        Log.d(TAG, "Device Id: " + first.getDeviceId() + ", device status: " + first.getSimState() + ", operator: " + first.getNetworkOperator() + "/" + first.getNetworkOperatorName());
	 
	        obParameter[0] = 1;
	        TelephonyManager second = (TelephonyManager) getFirstMethod.invoke(null, obParameter);
	 
	        Log.d(TAG, "Device Id: " + second.getDeviceId() + ", device status: " + second.getSimState()+ ", operator: " + second.getNetworkOperator() + "/" + second.getNetworkOperatorName());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }    
	} 
	
	
	private static String[] simStatusMethodNames = {"getSimStateGemini", "getSimState"};
	 
	 //http://stackoverflow.com/questions/14517338/android-check-whether-the-phone-is-dual-sim
	public static boolean hasTwoActiveSims(Context context) {
	    boolean first = false, second = false;
	 
	    for (String methodName: simStatusMethodNames) {
	        // try with sim 0 first 
	        try { 
	            first = getSIMStateBySlot(context, methodName, 0);
	            // no exception thrown, means method exists 
	            second = getSIMStateBySlot(context, methodName, 1);
				return first && second;
	        } catch (GeminiMethodNotFoundException e) {
	            // method does not exist, nothing to do but test the next 
	        } 
	    } 
	    return false; 
	} 
	
	
	
//	void HighApicalls()
//	{
//		 if(android.os.Build.VERSION.SDK_INT >=22){
//	         DualSimManagerLolipop info = new DualSimLolipop(this);
//	         /*below method return Object of ActiveNetworkStatus*/ 
//	         /* you create a getter setter*/ 
//	         simManager.getActiveSubscriptionInfo(); 
//	    } 
//	         
//	         if(android.os.Build.VERSION.SDK_INT <22){
//	             DualSimManagerinfo info = new DualSimManager(this);
//	             /*below method return Object of ActiveNetworkStatus*/ 
//	             if(info.isSIMSupported()){
//	                  //write your code 
//	                  //Check device is SIM enabled 
//	                 if(info.isSIMSupported()){
//	                     //targeted device first Sim is active 
//	                     // write your code and pass "0" argu 
//	                     e.g. ingo.getIMSI(0); 
//	                 }else(info.DualSIMSupported() && info.isSecondSimActive()){ 
//	                    // check device is dual sim supported and second sim is active 
//	                    // pass 1 argu for any method 
//	                    e.g.  info.getIMSI(1); 
//	                 } 
//	             } 
//	        } 
//	}
}