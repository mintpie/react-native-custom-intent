package com.crefun.RNCustomIntent;

import android.content.Intent;
import android.net.Uri;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

public class RNCustomIntentModule extends ReactContextBaseJavaModule {

    Promise promise;
    ReactApplicationContext reactContext;

    public RNCustomIntentModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "AndroidIntent";
    }

    @ReactMethod
    public void startApp(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivityForResult(intent,myRequestCode );
        return;
    }

    @ReactMethod
    public void startTelDial(String url) {
        Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
        getReactApplicationContext().startActivity(tel);
        return;
    }

    @ReactMethod
    public void startCnsPay(String url, final Promise promise) {
        try {
            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            String packageName = intent.getPackage();

            if (packageName != null) {
                try {
                    PackageManager pm = getPackageManager();
                    pm.getApplicationInfo(intent.getPackage(), PackageManager.GET_META_DATA);
                    startActivityForResult(intent, myRequestCode);
                } catch (PackageManager.NameNotFoundException e) {
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                    marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                    startActivity(marketIntent);
                }
            } else {
                startActivity(intent);
            }
        } catch (Exception e) {
            promise.reject("app not found");
            return;
        }
    }

    @ReactMethod
    public void startMarket(String url, final Promise promise) {
        try {
            Intent intent = Intent.parseUri(url, Intent.URL_INTENT_SCHEME);
            if (intent != null) {
                startActivity(intent);
            }
        } catch (Exception e) {
            promise.reject("app not found");
            return;
        }
    }


}