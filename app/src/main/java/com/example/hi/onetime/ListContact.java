package com.example.hi.onetime;

import java.util.ArrayList;

/**
 * Created by Hi on 31-05-2017.
 */

public class ListContact {
    public static ArrayList<DataModel> contactList = new ArrayList();

    public ArrayList<DataModel> getContactList() {
        return contactList;
    }

    public void setContactList(ArrayList<DataModel> contactList) {
        this.contactList = contactList;
    }
}
