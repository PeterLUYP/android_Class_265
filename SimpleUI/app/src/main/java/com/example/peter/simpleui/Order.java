package com.example.peter.simpleui;

import com.parse.ParseObject;
import com.parse.SaveCallback;

import io.realm.RealmObject;

/**
 * Created by Peter on 2016/4/25.
 */
public class Order extends RealmObject{
    private String note;
    private String menuResults;
    private String storeInfo;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMenuResults() {
        return menuResults;
    }

    public void setMenuResults(String menuResults) {
        this.menuResults = menuResults;
    }

    public String getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(String storeInfo) {
        this.storeInfo = storeInfo;
    }

    public void saveToRemote(SaveCallback saveCallback){
        ParseObject parseObject = new ParseObject("Order");
        parseObject.put("note", note);
        parseObject.put("storeInfo", storeInfo);
        parseObject.put("menuResults", menuResults);

        parseObject.saveInBackground(saveCallback);
    }
}
