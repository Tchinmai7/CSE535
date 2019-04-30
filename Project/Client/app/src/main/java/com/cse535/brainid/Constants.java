package com.cse535.brainid;

import io.realm.Realm;

class Constants {
    static String fogServer = "http://192.168.0.15:8000";
    static String cloudServer = "http://34.74.223.6:8000";

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
}
