package com.baliyaan.android.wordincontext.Data;

import com.baliyaan.android.wordincontext.Components.Examples.Model.Example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by Pulkit Singh on 10/9/2016.
 */

public class Scraper {

    public static Observable<List<Example>> GetExamples(final String iWord) {
        return new Observable<List<Example>>() {
            @Override
            protected void subscribeActual(Observer<? super List<Example>> observer) {
                List<Example> examples = new ArrayList<Example>();

                examples.clear();
                String url = "http://www.wordincontext.com/en/" + iWord;
                Document document = null;

                try {
                    document = Jsoup.connect(url).get();

                    Elements sentences = document.select("#content .sentence");
                    for (Element element : sentences) {
                        String sentence = element.text();
                        boolean add = examples.add(new Example(sentence));
                        System.out.println(sentence);
                    }


                    Elements books = document.select("#content .book");
                    int i = 0;
                    for (Element element : books) {
                        String book = element.text();
                        examples.get(i).set_link(book);
                        System.out.println(book);
                        i++;
                    }
                    observer.onNext(examples);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
