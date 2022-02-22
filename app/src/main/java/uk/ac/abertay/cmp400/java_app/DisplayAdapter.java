package uk.ac.abertay.cmp400.java_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DisplayAdapter extends RecyclerView.Adapter {

    Context c;
    ArrayList<DisplayModel> displayModels;

    public DisplayAdapter(Context c, ArrayList<DisplayModel> displayModels) {
        this.c = c;
        this.displayModels = displayModels;
    }

    @Override
    public int getItemViewType(int position) {

        try{
            if(displayModels.get(position).getHasImage()){
                return 1;
            }
        }catch (Exception e){
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if(viewType == 1){
            view = layoutInflater.inflate(R.layout.display_image, parent, false);
            return new ViewHolderImage(view);
        }
        view = layoutInflater.inflate(R.layout.display_row, parent, false);
        return new ViewHolderRow(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(displayModels.get(position).getHasImage()){
            ViewHolderImage viewHolderImage = (ViewHolderImage)holder;
            viewHolderImage.title.setText(displayModels.get(position).getTitle());
            viewHolderImage.desc.setText(displayModels.get(position).getDescription());
            viewHolderImage.imageView.setImageResource(displayModels.get(position).getImg());

        }else{
            ViewHolderRow viewHolderRow = (ViewHolderRow)holder;
            viewHolderRow.title.setText(displayModels.get(position).getTitle());
            viewHolderRow.desc.setText(displayModels.get(position).getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return displayModels.size();
    }

    class ViewHolderRow extends RecyclerView.ViewHolder{

        TextView title, desc;

        public ViewHolderRow(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.RowTitle);
            desc = itemView.findViewById(R.id.RowDescription);
        }
    }

    class ViewHolderImage extends RecyclerView.ViewHolder{

        TextView title, desc;
        ImageView imageView;

        public ViewHolderImage(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ImageImageView);
            title = itemView.findViewById(R.id.ImageTitle);
            desc = itemView.findViewById(R.id.ImageDescription);
        }
    }
}
