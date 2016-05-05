package com.example.peter.simpleui;

import com.parse.ParseException;
import com.parse.SaveCallback;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Peter on 2016/5/5.
 */
public class SaveCallbackWithRealm implements SaveCallback {
    RealmObject realmObject;
    SaveCallback saveCallBack;

    public  SaveCallbackWithRealm(RealmObject realmObject, SaveCallback callback){
        this.realmObject = realmObject;
        this.saveCallBack = callback;
    }

    @Override
    public void done(ParseException e) {

        if(e == null) {


            Realm realm = Realm.getDefaultInstance();

            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(realmObject);
            realm.commitTransaction();


            realm.close();
        }

        saveCallBack.done(e);


    }
}
