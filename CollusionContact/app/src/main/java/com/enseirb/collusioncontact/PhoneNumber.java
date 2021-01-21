package com.enseirb.collusioncontact;


import android.provider.ContactsContract;

public class PhoneNumber {
    private final int type;
    private final String number;

    public PhoneNumber(int type, String number) {
        this.type = type;
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    public String getTypeString (){
        switch (type){
            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                return "Home :";
            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                return "Mobile :";
            default:
                return "Default :";
        }
    }
}
