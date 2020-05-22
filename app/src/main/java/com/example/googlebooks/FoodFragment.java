package com.example.googlebooks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodFragment extends Fragment {
    View view;

    RecyclerView recyclerView;
    ProgressBar progressBar;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_food, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerview);
        progressBar = view.findViewById(R.id.progress);

        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        new backThread().execute("https://www.googleapis.com/books/v1/volumes?q=food");
    }


    class backThread extends AsyncTask<String, Void, List<BookModel>> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<BookModel> doInBackground(String... strings) {
            List<BookModel> bk = null;
            try {
                bk = Utiles.utils(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bk;
        }

        @Override
        protected void onPostExecute(List<BookModel> bookModels) {
            progressBar.setVisibility(View.GONE);
            if (bookModels == null) {
                Toast.makeText(getContext(), "Empty", Toast.LENGTH_SHORT).show();
            } else {
                booksAdapter bb = new booksAdapter(bookModels);
                recyclerView.setAdapter(bb);
            }

        }
    }


    public class booksAdapter extends RecyclerView.Adapter<booksAdapter.booksVH> {
        List<BookModel> bk;

        public booksAdapter(List<BookModel> bk) {
            this.bk = bk;
        }

        @NonNull
        @Override
        public booksVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item, null);
            return new booksVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull booksVH holder, int position)
        {
            BookModel bookModel = bk.get(position);

            holder.title.setText(bookModel.getTitle());
            holder.author.setText(bookModel.getAuthor());

            if (bookModel.getThumbnail().isEmpty()) {
                holder.bookimage.setImageResource(R.drawable.ic_study);
            } else {
                Picasso.get()
                        .load(bookModel.getThumbnail())
                        .error(R.drawable.ic_launcher_background)
                        .into(holder.bookimage);
            }

        }

        @Override
        public int getItemCount() {
            return bk.size();
        }

        class booksVH extends RecyclerView.ViewHolder {
            TextView title, author;
            ImageView bookimage;

            public booksVH(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.book_title);
                author = itemView.findViewById(R.id.book_author);
                bookimage = itemView.findViewById(R.id.book_image);

            }
        }

    }


}





