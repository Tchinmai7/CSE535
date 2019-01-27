package com.example.amine.learn2sign;

public class Constants {
    static final int REQUEST_VIDEO_CAPTURE = 1111;
    static final int REQUEST_CODE_UPLOAD = 2222;
    static final int RETURN_VIDEO_ACTIVITY_ABORT = 3333;
    static final int RETURN_VIDEO_ACTIVITY_SUCCESS = 4444;
    static final int VIDEO_REJECTED = 5555;
    static final int VIDEO_ACCEPTED = 6666;
    static final int REQUEST_SHOW_VIDEO = 7777;
    static ClicksLogger clicksLogger = ClicksLogger.getInstance();
    static String email;
    static String userId;

    static String getFilePath(String text, String packageName) {
        String path = "";
        if(text.equals("Alaska")) {
            path = "android.resource://" + packageName + "/" + R.raw.alaska;
        } else if(text.equals("Arizona")) {
            path = "android.resource://" + packageName + "/" + R.raw.arizona;
        } else if (text.equals("California")) {
            path = "android.resource://" + packageName + "/" + R.raw.california;
        }else if (text.equals("Colorado")) {
            path = "android.resource://" + packageName + "/" + R.raw.colorado;
        }else if (text.equals("Florida")) {
            path = "android.resource://" + packageName + "/" + R.raw.florida;
        }else if (text.equals("Georgia")) {
            path = "android.resource://" + packageName + "/" + R.raw.georgia;
        }else if (text.equals("Hawaii")) {
            path = "android.resource://" + packageName + "/" + R.raw.hawaii;
        }else if (text.equals("Illinois")) {
            path = "android.resource://" + packageName + "/" + R.raw.illinois;
        }else if (text.equals("Indiana")) {
            path = "android.resource://" + packageName + "/" + R.raw.indiana;
        }else if (text.equals("Kansas")) {
            path = "android.resource://" + packageName + "/" + R.raw.kansas;
        }else if (text.equals("Louisiana")) {
            path = "android.resource://" + packageName + "/" + R.raw.louisiana;
        }else if (text.equals("Massachusetts")) {
            path = "android.resource://" + packageName + "/" + R.raw.massachusetts;
        }else if (text.equals("Michigan")) {
            path = "android.resource://" + packageName + "/" + R.raw.michigan;
        }else if (text.equals("Minnesota")) {
            path = "android.resource://" + packageName + "/" + R.raw.minnesota;
        }else if (text.equals("Nevada")) {
            path = "android.resource://" + packageName + "/" + R.raw.nevada;
        }else if (text.equals("NewJersey")) {
            path = "android.resource://" + packageName + "/" + R.raw.new_jersey;
        }else if (text.equals("NewMexico")) {
            path = "android.resource://" + packageName + "/" + R.raw.new_mexico;
        }else if (text.equals("NewYork")) {
            path = "android.resource://" + packageName + "/" + R.raw.new_york;
        }else if (text.equals("Ohio")) {
            path = "android.resource://" + packageName + "/" + R.raw.ohio;
        }else if (text.equals("Pennsylvania")) {
            path = "android.resource://" + packageName + "/" + R.raw.pennsylvania;
        }else if (text.equals("SouthCarolina")) {
            path = "android.resource://" + packageName + "/" + R.raw.south_carolina;
        }else if (text.equals("Texas")) {
            path = "android.resource://" + packageName + "/" + R.raw.texas;
        }else if (text.equals("Utah")) {
            path = "android.resource://" + packageName + "/" + R.raw.utah;
        }else if (text.equals("Washington")) {
            path = "android.resource://" + packageName + "/" + R.raw.washington;
        }else if (text.equals("Wisconsin")) {
            path = "android.resource://" + packageName + "/" + R.raw.wisconsin;
        }
        return path;
    }

}
