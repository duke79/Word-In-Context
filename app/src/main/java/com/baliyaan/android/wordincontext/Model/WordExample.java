package com.baliyaan.android.wordincontext.Model;

/**
 * Created by Pulkit Singh on 10/9/2016.
 */

public class WordExample {
    public String _content;
    public String _link;

    public WordExample(String content) {
        _content = content;
    }

    public WordExample(String _content, String _link) {
        this._content = _content;
        this._link = _link;
    }

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
}
