package com.example.android.books;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.R.attr.author;
import static android.R.attr.rating;
import static com.example.android.books.R.id.authors;
import static com.example.android.books.R.id.date;

/**
 * Created by Professor on 6/15/2017.
 */

public class ListElementAdapter extends ArrayAdapter<Book> {

    private ArrayList<Book> books;

    public ListElementAdapter(Context context, ArrayList<Book> books){
        super(context, 0, books);
        this.books = books;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Book book = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        ImageView image = (ImageView)convertView.findViewById(R.id.image);
        if(!book.getImage().equals(""))
            Picasso.with(getContext()).load(book.getImage()).into(image);
        else
            image.setVisibility(View.INVISIBLE);

        TextView title = (TextView)convertView.findViewById(R.id.title);
        if(!book.getTitle().equals(""))
            title.setText(book.getTitle());
        else
            title.setText("");

        TextView date = (TextView)convertView.findViewById(R.id.date);
        if(!book.getDate().equals(""))
            date.setText(book.getDate());
        else
            date.setText("");

        TextView description = (TextView)convertView.findViewById(R.id.description);
        if(!book.getDescription().equals(""))
            description.setText(book.getDescription());
        else
            description.setText("");

        RatingBar rating = (RatingBar)convertView.findViewById(R.id.rate);
        if(book.getRate()==-1) {
            rating.setVisibility(View.INVISIBLE);
        } else {
            rating.setRating((float) book.getRate());
        }

        TextView author = (TextView)convertView.findViewById(R.id.authors);
        if(book.getAuthors()!=null && book.getAuthors().size()>0) {
            ArrayList<String> authors = book.getAuthors();
            String result = "";
            for (int i=0; i<authors.size(); i++) {
                result += authors.get(i)+" ";
                if(i == authors.size()-2) {
                    result += "and ";
                } else {
                    if (i != authors.size()-1) {
                        result += ", ";
                    }
                }
            }
            author.setText(result);
        } else {
            author.setText("");
        }
        return convertView;
    }
}
