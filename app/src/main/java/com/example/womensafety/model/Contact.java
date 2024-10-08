package com.example.womensafety.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Contact implements Parcelable {

    //Name of user
    String name;
    //Ph No. of Contact
    String ph_no;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.ph_no = phoneNumber;
    }

    protected Contact(@NonNull Parcel in) {
        name = in.readString();
        ph_no = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @NonNull
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @NonNull
        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    /**
     * @return contact name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name set contact name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return phone number
     */
    public String getPh_no() {
        return ph_no;
    }

    /**
     * @param ph_no set phone number
     */
    public void setPh_no(String ph_no) {
        this.ph_no = ph_no;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(ph_no);
    }
}
