package com.example.android.books;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private EditText mEditText;
    private Button mButton;
    private ListView mListView;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mEditText = (EditText) view.findViewById(R.id.search_text);
        mListView = (ListView) view.findViewById(R.id.list);
        mButton = (Button) view.findViewById(R.id.seach_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Fetch().execute(mEditText.getText().toString());
            }
        });
        return view;
    }

    class Fetch extends AsyncTask<String, Void, ArrayList<Book>> {

        private final String LOG_TAG = Fetch.class.getSimpleName();
        private final int INTERNET_ERROR = 1;
        private final int SEARCH_ERROR = 2;
        private int error;

        @Override
        protected ArrayList<Book> doInBackground(String... params) {
            if(params==null || params[0].length()==0){
                error = SEARCH_ERROR;
                return null;
            }
            String search = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;
            try {
                URL url = new URL("https://www.googleapis.com/books/v1/volumes?q="+search);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                forecastJsonStr = buffer.toString();
                JSONObject obj = new JSONObject(forecastJsonStr);
                if(obj.getInt("totalItems")<=0) {
                    error = SEARCH_ERROR;
                    return null;
                }
                JSONArray array = obj.getJSONArray("items");
                ArrayList<Book> books = new ArrayList<Book>(array.length());
                for(int i=0; i<array.length(); i++) {
                    JSONObject item = array.getJSONObject(i).getJSONObject("volumeInfo");

                    String title = "";
                    if(!item.isNull("title"))
                        title = item.getString("title");

                    ArrayList<String> authors = new ArrayList<String>();
                    if(!item.isNull("authors")) {
                        JSONArray jsonAuthors = item.getJSONArray("authors");
                        for (int j=0; j<jsonAuthors.length(); j++) {
                            authors.add(jsonAuthors.getString(j));
                        }
                    }

                    String description = "";
                    if(!item.isNull("description"))
                        description = item.getString("description");

                    String date = "";
                    if(!item.isNull("publishedDate"))
                        date = item.getString("publishedDate");

                    double rate = -1;
                    if(!item.isNull("averageRating"))
                        rate = item.getDouble("averageRating");

                    String image = "";
                    if (!item.isNull("imageLinks"))
                        image = item.getJSONObject("imageLinks").getString("smallThumbnail");

                    books.add(new Book(title, authors, date, description, rate, image));
                }
                return books;
            } catch (Exception e) {
                error = INTERNET_ERROR;
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            if(books == null) {
                if(error == INTERNET_ERROR)
                    Toast.makeText(getContext(), "please check internet connection", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "please search with a valid word", Toast.LENGTH_SHORT).show();
            } else {
                mListView.setAdapter(new ListElementAdapter(getContext(), books));
            }
        }
    }

}
