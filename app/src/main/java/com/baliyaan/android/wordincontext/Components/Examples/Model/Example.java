package com.baliyaan.android.wordincontext.Components.Examples.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pulkit Singh on 10/9/2016.
 */

public class Example implements Parcelable{
    public String _content;
    public String _link;

    public Example(){
    }

    public Example(String content) {
        _content = content;
    }

    public Example(String _content, String _link) {
        this._content = _content;
        this._link = _link;
    }

    protected Example(Parcel in) {
        _content = in.readString();
        _link = in.readString();
    }

    public static final Creator<Example> CREATOR = new Creator<Example>() {
        @Override
        public Example createFromParcel(Parcel in) {
            return new Example(in);
        }

        @Override
        public Example[] newArray(int size) {
            return new Example[size];
        }
    };

    public String get_link() {
        return _link;
    }

    public void set_link(String _link) {
        this._link = _link;
    }

    public String get_content() {
        return _content;
    }

    public void set_content(String _content) {
        this._content = _content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_content);
        dest.writeString(_link);
    }
}
