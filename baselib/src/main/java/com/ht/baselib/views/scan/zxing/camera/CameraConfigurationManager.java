package com.ht.baselib.views.scan.zxing.camera;/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.util.Collection;

//import com.google.zxing.client.android.PreferencesActivity;

/**
 * A class which deals with reading, parsing, and setting the camera parameters
 * which are used to configure the camera hardware.
 * 
 * 相机配置管理类
 * 
 */
final class CameraConfigurationManager {

	private static final String TAG = "CameraConfiguration";
	private static final int MIN_PREVIEW_PIXELS = 320 * 240; // small screen
	private static final int MAX_PREVIEW_PIXELS = 800 * 480; // large/HD screen

	private final Context context;
	/**
	 * 屏幕解析度
	 */
	private Point screenResolution;
	/**
	 * 相机解析度
	 */
	private Point cameraResolution;

	CameraConfigurationManager(Context context) {
		this.context = context;
	}

	/**
	 * Reads, one time, values from the camera that are needed by the app.
	 */
	void initFromCameraParameters(Camera camera) {
		Camera.Parameters parameters = camera.getParameters();
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		// We're landscape-only, and have apparently seen issues with display
		// thinking it's portrait
		// when waking from sleep. If it's not landscape, assume it's mistaken
		// and reverse them:
		if (width < height) {
			Log.i(TAG, "Display reports portrait orientation; assuming this is incorrect");
			int temp = width;
			width = height;
			height = temp;
		}
		screenResolution = new Point(height, width);
		Log.i(TAG, "Screen resolution: " + screenResolution);

		cameraResolution = findBestPreviewSizeValue(parameters, new Point(width, height), false);
		Log.i(TAG, "Camera resolution: " + cameraResolution);
	}

	/**
	 * 设置期望的相机配置
	 * 
	 * @param camera
	 */
	void setDesiredCameraParameters(Camera camera) {
		Camera.Parameters parameters = camera.getParameters();

		if (parameters != null) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

			initializeTorch(parameters, prefs);
			//获取 相机聚焦模式
			String focusMode = findSettableValue(parameters.getSupportedFocusModes(), Camera.Parameters.FOCUS_MODE_AUTO,
					Camera.Parameters.FOCUS_MODE_MACRO);
			if (focusMode != null) {
				//设置聚焦模式
				parameters.setFocusMode(focusMode);
			}
			//设置 预览大小
			parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
			/* 竖屏显示 */
			camera.setDisplayOrientation(90);
			camera.setParameters(parameters);
		}else{
			Log.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
		}
		
	}

	/**
	 * 获取 相机解析度
	 * @return
	 */
	Point getCameraResolution() {
		return cameraResolution;
	}

	/**
	 * 获取 屏幕解析度
	 * @return
	 */
	Point getScreenResolution() {
		return screenResolution;
	}

	/**
	 * 存储 prefs 的 key
	 */
	public static final String KEY_FRONT_LIGHT = "preferences_front_light";

	void setTorch(Camera camera, boolean newSetting) {
		Camera.Parameters parameters = camera.getParameters();
		doSetTorch(parameters, newSetting);
		camera.setParameters(parameters);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		boolean currentSetting = prefs.getBoolean(KEY_FRONT_LIGHT, false);// PreferencesActivity.
		if (currentSetting != newSetting) {
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean(KEY_FRONT_LIGHT, newSetting);// PreferencesActivity.
			editor.commit();
		}
	}

	/**
	 * 初始化 相机的Torch
	 * @param parameters
	 * @param prefs
	 */
	private static void initializeTorch(Camera.Parameters parameters, SharedPreferences prefs) {
		boolean currentSetting = prefs.getBoolean(KEY_FRONT_LIGHT, false);// PreferencesActivity.
		doSetTorch(parameters, currentSetting);
	}

	/**
	 * 设置闪光灯模式
	 * @param parameters
	 * @param newSetting
	 */
	private static void doSetTorch(Camera.Parameters parameters, boolean newSetting) {
		String flashMode;
		if (newSetting) {
			flashMode = findSettableValue(parameters.getSupportedFlashModes(), Camera.Parameters.FLASH_MODE_TORCH,
					Camera.Parameters.FLASH_MODE_ON);
		} else {
			flashMode = findSettableValue(parameters.getSupportedFlashModes(), Camera.Parameters.FLASH_MODE_OFF);
		}
		if (flashMode != null) {
			parameters.setFlashMode(flashMode);
		}
	}

	/**
	 * 获取最好的相机预览大小
	 * @param parameters 相机参数
	 * @param screenResolution 屏幕解析度
	 * @param portrait 画像
	 * @return
	 */
	private static Point findBestPreviewSizeValue(Camera.Parameters parameters, Point screenResolution, boolean portrait) {
		Point bestSize = null;
		int diff = Integer.MAX_VALUE;
		for (Camera.Size supportedPreviewSize : parameters.getSupportedPreviewSizes()) {
			int pixels = supportedPreviewSize.height * supportedPreviewSize.width;
			if (pixels < MIN_PREVIEW_PIXELS || pixels > MAX_PREVIEW_PIXELS) {
				continue;
			}
			int supportedWidth = portrait ? supportedPreviewSize.height : supportedPreviewSize.width;
			int supportedHeight = portrait ? supportedPreviewSize.width : supportedPreviewSize.height;
			int newDiff = Math.abs(screenResolution.x * supportedHeight - supportedWidth * screenResolution.y);
			if (newDiff == 0) {
				bestSize = new Point(supportedWidth, supportedHeight);
				break;
			}
			if (newDiff < diff) {
				bestSize = new Point(supportedWidth, supportedHeight);
				diff = newDiff;
			}
		}
		if (bestSize == null) {
			Camera.Size defaultSize = parameters.getPreviewSize();
			bestSize = new Point(defaultSize.width, defaultSize.height);
		}
		return bestSize;
	}

	/**
	 * 获取可设置的闪光灯模式的值
	 */
	private static String findSettableValue(Collection<String> supportedValues, String... desiredValues) {
		Log.i(TAG, "Supported values: " + supportedValues);
		String result = null;
		if (supportedValues != null) {
			for (String desiredValue : desiredValues) {
				if (supportedValues.contains(desiredValue)) {
					result = desiredValue;
					break;
				}
			}
		}
		Log.i(TAG, "Settable value: " + result);
		return result;
	}

}
