package com.baliyaan.android.wordincontext.Components.Definition.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by Pulkit Singh on 11/5/2017.
 */

public class OnlineDictionary {

    static public String getDefinitionOf(String word) throws IOException {
        String definitions = "";

        String url = "https://api.pearson.com/v2/dictionaries/entries?headword=" + word;
        String res = getWebPage(url);

        try {
            JSONArray resultArray = new JSONObject(res).getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                String definition = "";
                JSONObject result = resultArray.getJSONObject(i);
                String partOfSpeech = result.getString("part_of_speech");
                definition += partOfSpeech + "\n";
                JSONArray sensesArray = result.getJSONArray("senses");
                for (int j = 0; j < sensesArray.length(); j++) {
                    JSONObject sense = sensesArray.getJSONObject(j);
                    Object defObj = sense.get("definition");
                    if (defObj instanceof String)
                        definition += defObj + "\n";
                    else if (defObj instanceof JSONObject)
                        definition += defObj.toString() + "\n";
                    else if (defObj instanceof JSONArray)
                        definition += ((JSONArray) defObj).getString(0) + "\n";
                }
                definitions +=definition;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return definitions;
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
