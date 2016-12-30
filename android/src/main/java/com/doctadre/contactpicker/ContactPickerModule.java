package com.doctadre.contactpicker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;


import static android.app.Activity.RESULT_OK;
import static android.app.Activity.RESULT_CANCELED;

import com.codinguser.android.contactpicker.ContactsPickerActivity;


class ContactPickerModule extends ReactContextBaseJavaModule {
  private static final int GET_PHONE_NUMBER = 3007;
  private static final String DEBUG_TAG = "ContactPicker";

  private Promise mContactPickerPromise;
  private final ReactApplicationContext _reactContext;


  @Override
  public String getName() {
    return "ContactPicker";
  }

  ContactPickerModule(ReactApplicationContext reactContext) {
    super(reactContext);
    _reactContext = reactContext;
    _reactContext.addActivityEventListener(mActivityEventListener);
  }

  @ReactMethod
  public void pickContact(final Promise promise) {
    mContactPickerPromise = promise;
    ///startActivityForResult(new Intent(this, ContactsPickerActivity.class), GET_PHONE_NUMBER);
    Activity currentActivity = getCurrentActivity();
    Intent contactPickerIntent = new Intent(_reactContext, ContactsPickerActivity.class);
    currentActivity.startActivityForResult(contactPickerIntent, GET_PHONE_NUMBER);
  }

  // Listen for results.
  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
      switch (requestCode) {
        case GET_PHONE_NUMBER:
        // This is the standard resultCode that is sent back if the
        // activity crashed or didn't doesn't supply an explicit result.
        if (resultCode == RESULT_CANCELED){
          mContactPickerPromise.resolve("");
        }
        else {
          String phoneNumber = (String) data.getExtras().get(ContactsPickerActivity.KEY_PHONE_NUMBER);
          mContactPickerPromise.resolve(phoneNumber);
          break;
        }
        default:
        break;
      }
    }
  };

  /**
  * Need this so we don't get "need to implement onNewIntent" crash
  * Got the solution from https://github.com/marcshilling/react-native-image-picker/commit/146cf6ee56b9e0953e9136e1491e893b41edb672
  */
  @SuppressWarnings("unused")
  public void onNewIntent(Intent intent) {
    // no-op
  }
}
