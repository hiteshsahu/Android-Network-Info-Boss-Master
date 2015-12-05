package com.serveroverload.networkmaster.network;

import android.content.pm.FeatureInfo;
import android.content.pm.PermissionInfo;
import android.graphics.drawable.Drawable;

public class PackageInfoModel {

	/**
	 * @param appName
	 * @param packageName
	 * @param versioName
	 * @param versionCode
	 * @param icon
	 * @param packagePermission
	 * @param packageFeature
	 */
	public PackageInfoModel(String appName, String packageName, String versioName,
			String versionCode, Drawable icon, FeatureInfo[] packageFeature,
			PermissionInfo[] packagePermission) {
		super();
		this.appName = appName;
		this.packageName = packageName;
		this.versioName = versioName;
		this.versionCode = versionCode;
		Icon = icon;
		this.packageFeature = packageFeature;
		this.packagePermission = packagePermission;
	}

	public FeatureInfo[] getPackageFeature() {
		return packageFeature;
	}

	public void setPackageFeature(FeatureInfo[] packageFeature) {
		this.packageFeature = packageFeature;
	}

	public PermissionInfo[] getPackagePermission() {
		return packagePermission;
	}

	public void setPackagePermission(PermissionInfo[] packagePermission) {
		this.packagePermission = packagePermission;
	}

	private String appName, packageName, versioName, versionCode;

	FeatureInfo[] packageFeature;
	PermissionInfo[] packagePermission;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersioName() {
		return versioName;
	}

	public void setVersioName(String versioName) {
		this.versioName = versioName;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public Drawable getIcon() {
		return Icon;
	}

	Drawable Icon;

	public void setAppname(String string) {
		// TODO Auto-generated method stub

	}

	public void setPname(String packageName) {
		// TODO Auto-generated method stub

	}

	public void setVersionName(String versionName) {
		// TODO Auto-generated method stub

	}

	public void setVersionCode(int versionCode) {
		// TODO Auto-generated method stub

	}

	public void setIcon(Drawable loadIcon) {
		// TODO Auto-generated method stub

	}

}
