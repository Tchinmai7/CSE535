package com.example.amine.learn2sign;

public class Constants {
    static final int REQUEST_VIDEO_CAPTURE = 9999;
    static final int REQUEST_CODE_UPLOAD = 2000;
    static final int RETURN_VIDEO_ACTIVITY_ABORT = 7777;
    static final int RETURN_VIDEO_ACTIVITY_SUCCESS = 8888;

    static String getFilePath(String text, String packageName) {
        String path = "";
        if (text.equals("About")) {
            path = "android.resource://" + packageName + "/" + R.raw._about;
        } else if(text.equals("And")) {
            path = "android.resource://" + packageName + "/" + R.raw._and;
        } else if (text.equals("Can")) {
            path = "android.resource://" + packageName + "/" + R.raw._can;
        } else if (text.equals("Cat")) {
            path = "android.resource://" + packageName + "/" + R.raw._cat;
        } else if (text.equals("Cop")) {
            path = "android.resource://" + packageName + "/" + R.raw._cop;
        } else if (text.equals("Cost")) {
            path = "android.resource://" + packageName + "/" + R.raw._cost;
        } else if (text.equals("Day")) {
            path = "android.resource://" + packageName + "/" + R.raw._day;
        } else if (text.equals("Deaf")) {
            path = "android.resource://" + packageName + "/" + R.raw._deaf;
        } else if (text.equals("Decide")) {
            path = "android.resource://" + packageName + "/" + R.raw._decide;
        } else if (text.equals("Father")) {
            path = "android.resource://" + packageName + "/" + R.raw._father;
        } else if (text.equals("Find")) {
            path = "android.resource://" + packageName + "/" + R.raw._find;
        } else if (text.equals("Go Out")) {
            path = "android.resource://" + packageName + "/" + R.raw._go_out;
        } else if (text.equals("Gold")) {
            path = "android.resource://" + packageName + "/" + R.raw._gold;
        } else if (text.equals("Goodnight")) {
            path = "android.resource://" + packageName + "/" + R.raw._good_night;
        } else if (text.equals("Hearing")) {
            path = "android.resource://" + packageName + "/" + R.raw._hearing;
        } else if (text.equals("Here")) {
            path = "android.resource://" + packageName + "/" + R.raw._here;
        } else if (text.equals("Hospital")) {
            path = "android.resource://" + packageName + "/" + R.raw._hospital;
        } else if (text.equals("Hurt")) {
            path = "android.resource://" + packageName + "/" + R.raw._hurt;
        } else if (text.equals("If")) {
            path = "android.resource://" + packageName + "/" + R.raw._if;
        } else if (text.equals("Large")) {
            path = "android.resource://" + packageName + "/" + R.raw._large;
        } else if (text.equals("Hello")) {
            path = "android.resource://" + packageName + "/" + R.raw._hello;
        } else if (text.equals("Help")) {
            path = "android.resource://" + packageName + "/" + R.raw._help;
        } else if (text.equals("Sorry")) {
            path = "android.resource://" + packageName + "/" + R.raw._sorry;
        } else if (text.equals("After")) {
            path = "android.resource://" + packageName + "/" + R.raw._after;
        } else if (text.equals("Tiger")) {
            path = "android.resource://" + packageName + "/" + R.raw._tiger;
        }
        return path;
    }
}
