package com.cse535.brainid;

import io.realm.Realm;

class Constants {
    static String fogServer = "http://requestbin.fullcontact.com/ura11rur";
    static String cloudServer = "https://en1ef1vfk93oy.x.pipedream.net";

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
