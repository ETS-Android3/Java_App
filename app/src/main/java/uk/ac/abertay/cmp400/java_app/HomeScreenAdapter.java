package uk.ac.abertay.cmp400.java_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeScreenAdapter extends RecyclerView.Adapter<HomeScreenHolder> {


    final Context c;
    final ArrayList<HomeScreenModel> homeScreenModels;

    public HomeScreenAdapter(Context c, ArrayList<HomeScreenModel> homeScreenModels) {
        this.c = c;
        this.homeScreenModels = homeScreenModels;
    }

    @NonNull
    @Override
    public HomeScreenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_screen_row, null);

        return new HomeScreenHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeScreenHolder holder, int position) {
        holder.mTitle.setText(homeScreenModels.get(position).getTitle());
        holder.mDes.setText(homeScreenModels.get(position).getDescription());
        holder.mImageView.setImageResource(homeScreenModels.get(position).getImg());

        holder.setItemClickListener((view, position1) -> {
            if(homeScreenModels.get(position1).getTitle().equals("The Basic Syntax of Java")){
                Intent intent = new Intent(c, DisplayScreen.class);
                intent.putExtra("index", 1);
                c.startActivity(intent);
            }
            if(homeScreenModels.get(position1).getTitle().equals("Variables")){
                Intent intent = new Intent(c, DisplayScreen.class);
                intent.putExtra("index", 2);
                c.startActivity(intent);
            }
            if(homeScreenModels.get(position1).getTitle().equals("Data Types")){
                Intent intent = new Intent(c, DisplayScreen.class);
                intent.putExtra("index", 3);
                c.startActivity(intent);
            }
            if(homeScreenModels.get(position1).getTitle().equals("Operators in Java")){
                Intent intent = new Intent(c, DisplayScreen.class);
                intent.putExtra("index", 4);
                c.startActivity(intent);
            }
            if(homeScreenModels.get(position1).getTitle().equals("Conditional Statements")){
                Intent intent = new Intent(c, DisplayScreen.class);
                intent.putExtra("index", 5);
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeScreenModels.size();
    }
}
