package com.baliyaan.android.wordincontext.IO;

import com.baliyaan.android.wordincontext.Model.WordExample;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pulkit Singh on 10/9/2016.
 */

public class Scraper {

    public static List<WordExample> GetExamples(String iWord) throws IOException {
        List<WordExample> wordExamples = new ArrayList<WordExample>();

        wordExamples.clear();
        String url = "http://www.wordincontext.com/en/"+iWord;
        Document document = Jsoup.connect(url).get();
        Elements sentences = document.select("#content .sentence");
        for(Element element : sentences)
        {
            String sentence = element.text();
            boolean add = wordExamples.add(new WordExample(sentence));
            System.out.println(sentence);
        }


        Elements books = document.select("#content .book");
        int i=0;
        for(Element element : books)
        {
            String book = element.text();
            wordExamples.get(i).set_link(book);
            System.out.println(book);
            i++;
        }
        return wordExamples;
    }
}