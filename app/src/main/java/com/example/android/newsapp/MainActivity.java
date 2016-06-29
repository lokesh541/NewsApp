package com.example.android.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArticleAdapter adapter;
    ArrayList<Article> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        ArrayList<Article> articles = new ArrayList<Article>();


        adapter = new ArticleAdapter(this, articles);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        ArticleFetchTask articleFetchTask = new ArticleFetchTask();
        articleFetchTask.execute();

    }


    public class ArticleFetchTask extends AsyncTask<String, Void, Article[]> {

        private int maxResults = 14;

        private final String LOG_TAG = ArticleFetchTask.class.getSimpleName();


        @Override
        protected Article[] doInBackground(String... params) {

// These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.
            String articleListJsonStr = null;


            try {
                //Constructing url for book Api by adding parameters

                final String BOOKLIST_BASE_URL = " http://content.guardianapis.com/search?";
                //final String QUERY_PARAM = "q";
                final String MAXRES_PARAM = "page-size";
                final String API_KEY = "api-key";

                Uri builtUri = Uri.parse(BOOKLIST_BASE_URL).buildUpon()
                        //.appendQueryParameter(QUERY_PARAM, "footbal")
                        .appendQueryParameter("show-fields", "thumbnail")
                        .appendQueryParameter(MAXRES_PARAM, Integer.toString(maxResults))
                        .appendQueryParameter(API_KEY, "test")
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to google books api  and open the connection

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    articleListJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    articleListJsonStr = null;
                }
                articleListJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Articles string: " + articleListJsonStr);
            } catch (IOException e) {
                Log.e("Article list Activity", "Error ", e);
                // If the code didn't successfully books fata, there's no point in attempting
                // to parse it.
                articleListJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Article list activity","Error closing stream", e);
                    }
                }
            }


            try {
                return getArticleFromJson(articleListJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;

        }

        protected void onPostExecute(Article[] result) {
            if (result != null) {
                adapter.clear();

                for (Article article : result) {
                    adapter.add(article);
                }
            }
        }

        private Article[] getArticleFromJson(String articleListJsonStr) throws JSONException {

            JSONObject articleList = new JSONObject(articleListJsonStr);

            JSONObject response = articleList.getJSONObject("response");

            JSONArray results = response.getJSONArray("results");


            Article[] resultObj = new Article[maxResults];


            for (int i = 0; i < results.length(); i++) {

                JSONObject result = results.getJSONObject(i); //Gets each item i.e is book
                //gets the volume Info Object from Api which contains book title and authors name
                String title = result.getString("webTitle");
                String url  = result.getString("webUrl");
                JSONObject thumbNail = result.getJSONObject("fields");
                String thumbnailurl = thumbNail.getString("thumbnail");


                resultObj[i] = new Article(thumbnailurl, title,url);
            }


            return resultObj;


        }
    }


}
