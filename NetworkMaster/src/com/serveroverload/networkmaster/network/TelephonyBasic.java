package com.serveroverload.networkmaster.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

public class TelephonyBasic {

	private TelephonyManager telephonyManager;

	public TelephonyBasic(Context context) {
		telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	// -------------------DEVICE iNFO ------------------
	// Get IMEI Number of Phone

	public String getIMEI() {
		return telephonyManager.getDeviceId();
	}

	// Get Subscriber ID
	public String getsubscriberID() {
		return telephonyManager.getDeviceId();
	}

	// Get the device software version
	public String getsoftwareVersion() {
		return telephonyManager.getDeviceSoftwareVersion();
	}

	// --------------- PHONEINFO ------------------------

	// Get Network Country ISO Code()
	public String getnetworkCountryISO() {
		return telephonyManager.getNetworkCountryIso();
	}

	// Get the connected network operator ID (MCC + MNC)
	public String getNetworkOperator() {
		return telephonyManager.getNetworkOperator();
	}

	// Get the connected network operator name
	public String getNetworkOperatorName() {
		return telephonyManager.getNetworkOperatorName();
	}

	public String GetVoiceMailNumber() {
		return telephonyManager.getVoiceMailNumber();
	}

	// Get the Phone Type() CDMA/GSM/NONE
	// Get the type of network you are connected with

	public String getPhoneType() {
		int phoneType = telephonyManager.getPhoneType();

		switch (phoneType) {
		case (TelephonyManager.PHONE_TYPE_CDMA):
			return "CDMA";
		case (TelephonyManager.PHONE_TYPE_GSM):
			return "GSM";
		case (TelephonyManager.PHONE_TYPE_NONE):
			return "NONE";
		}
		return null;
	}

	// Find whether the Phone is in Roaming, returns true if
	public boolean inRoaming() {
		return telephonyManager.isNetworkRoaming();

	}

	// ----------------------------SIM CARD --------------------

	// Get SIM Country ISO Code
	public String getSIMCountryISO() {
		return telephonyManager.getSimCountryIso();
	}

	public String getSIMSerialNumber() {
		return telephonyManager.getSimSerialNumber();
	}

	// Get the operator code of the active SIM (MCC + MNC)
	public String getSimOperatorCode() {
		return telephonyManager.getSimOperator();
	}

	// Get the name of the SIM operator
	public String getSimOperatorName() {
		return telephonyManager.getSimOperatorName();
	}

	// Get the SIM state/Details

	public String getSIMState() {
		int SIMState = telephonyManager.getSimState();
		switch (SIMState) {
		case TelephonyManager.SIM_STATE_ABSENT:
			return "ABSENT";
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			return "LOCKED";
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			return "PIN REQUIRED";
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			return "PUK REQUIRED";
		case TelephonyManager.SIM_STATE_READY:
			return "READY";
		case TelephonyManager.SIM_STATE_UNKNOWN:
			return "UNKNOWN";

		}
		return "??";
	}

	public String getSimPhoneNumber() {
		return telephonyManager.getLine1Number();
	}

	@SuppressLint("NewApi")
	public String getDataState() {

		switch (telephonyManager.getDataState()) {
		case TelephonyManager.DATA_DISCONNECTED:

			return "Disconnected";

		case TelephonyManager.DATA_CONNECTING:

			return "Connecting";
		case TelephonyManager.DATA_CONNECTED:
			return "Connected";

		case TelephonyManager.DATA_SUSPENDED:
			return "Suspended";
		default:
			return "??";
		}

	}
}
