package com.baliyaan.android.wordincontext.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pulkit Singh on 11/12/2017.
 */

public class Definition implements Parcelable{
    public String _headword;
    public String _id;
    public String _dictionary;
    public String _partOfSpeech;
    public String _phonetics;
    public String _language;
    public String _example;
    public String _definition;

    public Definition(){
    }

    protected Definition(Parcel in) {
        _headword = in.readString();
        _id = in.readString();
        _dictionary = in.readString();
        _partOfSpeech = in.readString();
        _phonetics = in.readString();
        _language = in.readString();
        _example = in.readString();
        _definition = in.readString();
    }

    public static final Creator<Definition> CREATOR = new Creator<Definition>() {
        @Override
        public Definition createFromParcel(Parcel in) {
            return new Definition(in);
        }

        @Override
        public Definition[] newArray(int size) {
            return new Definition[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_headword);
        dest.writeString(_id);
        dest.writeString(_dictionary);
        dest.writeString(_partOfSpeech);
        dest.writeString(_phonetics);
        dest.writeString(_language);
        dest.writeString(_example);
        dest.writeString(_definition);
    }
}
