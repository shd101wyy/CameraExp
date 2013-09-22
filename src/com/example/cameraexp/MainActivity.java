package com.example.cameraexp;


import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Camera;

import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;


@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
public class MainActivity extends Activity{
    /**
     * WebViewClient subclass loads all hyperlinks in the existing WebView
     */
    public class GeoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // When user clicks a hyperlink, load in the existing WebView
            view.loadUrl(url);
            return true;
        }
    }
 
    /**
     * WebChromeClient subclass handles UI-related calls
     * Note: think chrome as in decoration, not the Chrome browser
     */
    public class GeoWebChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                GeolocationPermissions.Callback callback) {
            // Always grant permission since the app itself requires location
            // permission and the user has therefore already granted it
            callback.invoke(origin, true, false);
        }
    }
	
	private Camera mCamera;
	private CameraPreview mPreview;
	private WebView webview;
	//Intent camera;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		int num_of_camera=Camera.getNumberOfCameras();
		if(num_of_camera==0){
			System.out.print("NO CAMERA FOUND");
		}
		
        // Create an instance of Camera
        mCamera = getCameraInstance();

        // rotate 90 degree
        //Camera.Parameters parameters = mCamera.getParameters();
        //parameters.set("orientation", "portrait");
        //mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);
        
        
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
        
        // init webview
        webview=(WebView)findViewById(R.id.webView1);
        webview.setBackgroundColor(0x00000000);
        
     // Brower niceties -- pinch / zoom, follow links in place
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setWebViewClient(new GeoWebViewClient());
        // Below required for geolocation
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setGeolocationEnabled(true);
        webview.setWebChromeClient(new GeoWebChromeClient());

        webview.getSettings().setDisplayZoomControls(false);
        webview.getSettings().setSupportZoom(false);
        String url="file:///android_asset/www/index.html";
        webview.loadUrl(url);
	}
	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	


}
