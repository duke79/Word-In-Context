package com.baliyaan.android.wordincontext.Components.Definition.Data;

import android.util.Pair;

import com.baliyaan.android.wordincontext.Model.Definition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Pulkit Singh on 11/5/2017.
 */

public class OnlineDictionary {
    static ArrayList<Pair<String, String>> dictionaries = new ArrayList<>();

    static public ArrayList<Definition> getDefinitionsOf(String word) throws IOException {
        dictionaries.add(new Pair<>("ldoce5", "Longman Dictionary of Contemporary English (5th edition)")); //English
        dictionaries.add(new Pair<>("lasde", "Longman Active Study Dictionary")); //English
        dictionaries.add(new Pair<>("ldec", "Longman English-Chinese Dictionary of 100,000 Words (New 2nd Edition)"));
        dictionaries.add(new Pair<>("wordwise", "Longman Wordwise Dictionary")); //English
        dictionaries.add(new Pair<>("laesd", "Longman Afrikaans to English"));
        dictionaries.add(new Pair<>("laad3", "Longman Advanced American Dictionary")); //English
        dictionaries.add(new Pair<>("laes", "English to Latin American Spanish"));
        dictionaries.add(new Pair<>("lase", "Latin American Spanish to English"));
        dictionaries.add(new Pair<>("brep", "English to Brazilian Portuguese"));
        dictionaries.add(new Pair<>("brpe", "Brazilian Portuguese to English"));

        ArrayList<Definition> definitions = new ArrayList<>();

        String url = "https://api.pearson.com/v2/dictionaries/entries?headword=" + word;
        String res = getWebPage(url);

        try {
            JSONArray resultArray = new JSONObject(res).getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject result = resultArray.getJSONObject(i);
                String partOfSpeech = result.optString("part_of_speech");
                String id = result.optString("id");
                String headword = result.optString("headword");
                String dictionary = "";
                if (null != (result.opt("datasets"))) {
                    dictionary = ((JSONArray) result.opt("datasets")).getString(0);
                }
                String phonetics = "";
                String language = "";
                JSONArray pronunciations = (JSONArray) result.opt("pronunciations");
                if (null != pronunciations) {
                    phonetics = ((JSONObject) pronunciations.get(0)).optString("ipa");
                    language = ((JSONObject) pronunciations.get(0)).optString("lang");
                }
                JSONArray sensesArray = result.getJSONArray("senses");
                for (int j = 0; j < sensesArray.length(); j++) {
                    JSONObject sense = sensesArray.getJSONObject(j);
                    Object defObj = sense.opt("definition");
                    if (defObj instanceof String) {
                        Definition definition = new Definition();
                        definition._definition = (String) defObj;
                        definition._id = id;
                        definition._headword = headword;
                        definition._partOfSpeech = partOfSpeech;
                        definition._dictionary = dictionary;
                        definition._phonetics = phonetics;
                        definition._language = language;
                        definitions.add(definition);
                    } else if (defObj instanceof JSONObject) {
                        Definition definition = new Definition();
                        definition._definition = defObj.toString();
                        definition._id = id;
                        definition._headword = headword;
                        definition._partOfSpeech = partOfSpeech;
                        definition._dictionary = dictionary;
                        definition._phonetics = phonetics;
                        definition._language = language;
                        definitions.add(definition);
                    } else if (defObj instanceof JSONArray) {
                        for (int iDef = 0; iDef < ((JSONArray) defObj).length(); iDef++) {
                            Definition definition = new Definition();
                            definition._definition = ((JSONArray) defObj).getString(iDef);
                            definition._id = id;
                            definition._headword = headword;
                            definition._partOfSpeech = partOfSpeech;
                            definition._dictionary = dictionary;
                            definition._phonetics = phonetics;
                            definition._language = language;
                            definitions.add(definition);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return definitions;
    }

    static public String getSimpleDefinitionOf(String word) throws IOException {
        ArrayList<Definition> definitions = getDefinitionsOf(word);
        for (int iDef = 0; iDef < definitions.size(); iDef++) {
            String dictionary = definitions.get(iDef)._dictionary;
            String definition = definitions.get(iDef)._definition;
            if ("ldoce5".equals(dictionary)
                    || "lasde".equals(dictionary)
                    || "wordwise".equals(dictionary)
                    || "laad3".equals(dictionary))
            return definition;
        }
        return "";
    }

    static private String getWebPage(String iUrl) throws IOException {
        return Jsoup.connect(iUrl)
                .ignoreContentType(true)
                .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                //.timeout(300000)
                //.header("Accept", "text/javascript")
                .get()
                //.body()
                .text();

        /*URL url = new URL(iUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        Log.v("Http Response", String.valueOf(conn.getResponseCode()));
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line).append('\n');
        }
        return total.toString();*/
    }

}
