package com.cse535.brainid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;

class Constants {
    static String fogServer = "http://10.152.81.93:8000";
    static String cloudServer = "http://35.196.131.42:8000";

    static AuthenticationHistory getAhObject(Realm realm) {
        AuthenticationHistory ah;
        ah = realm.where(AuthenticationHistory.class).findFirst();
        if (ah == null) {
            realm.beginTransaction();
            ah = new AuthenticationHistory();
            realm.copyToRealm(ah);
            realm.commitTransaction();
        }
        return ah;
    }

    static boolean isValidUserName(String username) {
        final String regex = "S[0-1][0-9][0-9]";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(username);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }
}
