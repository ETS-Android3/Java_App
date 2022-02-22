package uk.ac.abertay.cmp400.java_app;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeScreenHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView mImageView;
    TextView mTitle, mDes;
    ItemClickListener itemClickListener;

    HomeScreenHolder(@NonNull View itemView) {
        super(itemView);

        this.mImageView = itemView.findViewById(R.id.image);
        this.mTitle = itemView.findViewById(R.id.title);
        this.mDes = itemView.findViewById(R.id.description);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        this.itemClickListener.onItemClickListener(view, getLayoutPosition());

    }

    public void setItemClickListener(ItemClickListener ic){

        this.itemClickListener = ic;
    }
}
