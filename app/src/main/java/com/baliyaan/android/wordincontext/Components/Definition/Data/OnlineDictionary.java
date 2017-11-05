package com.baliyaan.android.wordincontext.Components.Definition.Data;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by Pulkit Singh on 11/5/2017.
 */

public class OnlineDictionary {

    static public String getDefinitionOf(String word) throws IOException, JSONException {
        String definition = "";

        String url = "https://api.pearson.com/v2/dictionaries/entries?headword=" + word;
        String res = getWebPage(url);
        definition =
                new JSONObject(res)
                        .getJSONArray("results").getJSONObject(0)
                        .getJSONArray("senses").getJSONObject(0)
                        .getJSONArray("definition").getString(0);
        return definition;
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
