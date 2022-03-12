package uk.ac.abertay.cmp400.java_app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import java.util.ArrayList;

public class DisplayAdapter extends RecyclerView.Adapter {

    private static final String TAG = "DisplayAdapter";
    final Context c;
    final ArrayList<DisplayModel> displayModels;

    public DisplayAdapter(Context c, ArrayList<DisplayModel> displayModels) {
        this.c = c;
        this.displayModels = displayModels;
    }

    @Override
    public int getItemViewType(int position) {
        //determin what view to inflate
        if(displayModels.get(position).getHasImage()){
            return 1;
        }else{
            return 0;
        }
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
            viewHolderImage.desc.loadData(displayModels.get(position).getDescription(), "text/html; charset=utf-8", "UTF-8");
            viewHolderImage.imageView.setImageResource(displayModels.get(position).getImg());
            viewHolderImage.imageView.setContentDescription(displayModels.get(position).getTitle());

        }else{
            ViewHolderRow viewHolderRow = (ViewHolderRow)holder;
            viewHolderRow.title.setText(displayModels.get(position).getTitle());
            viewHolderRow.desc.loadData(displayModels.get(position).getDescription(), "text/html; charset=utf-8", "UTF-8");
        }
    }

    @Override
    public int getItemCount() {
        return displayModels.size();
    }

    static class ViewHolderRow extends RecyclerView.ViewHolder{
        //class for each view that can be inflated along with the assignments.
        final TextView title;
        final WebView desc;

        public ViewHolderRow(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.RowTitle);
            desc = itemView.findViewById(R.id.RowDescription);

            int nightModeFlags =
                   title.getContext().getResources().getConfiguration().uiMode &
                            Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags) {
                case Configuration.UI_MODE_NIGHT_YES:
                    if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                        WebSettingsCompat.setForceDark(desc.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
                    }
                    break;

                case Configuration.UI_MODE_NIGHT_NO:
                    if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                        WebSettingsCompat.setForceDark(desc.getSettings(), WebSettingsCompat.FORCE_DARK_OFF);
                        desc.setBackgroundColor(itemView.getResources().getColor(R.color.cardBackgroundLight));
                    }
                    break;

                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    break;
            }
            desc.getSettings().setJavaScriptEnabled(true);
            desc.setVerticalScrollBarEnabled(false);
        }
    }

    static class ViewHolderImage extends RecyclerView.ViewHolder{
        //class for each view that can be inflated along with the assignments.
        final TextView title;
        final WebView desc;
        final ImageView imageView;

        public ViewHolderImage(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ImageImageView);
            title = itemView.findViewById(R.id.ImageTitle);
            desc = itemView.findViewById(R.id.ImageDescription);
            desc.getSettings().setJavaScriptEnabled(true);
            int nightModeFlags =
                    title.getContext().getResources().getConfiguration().uiMode &
                            Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags) {
                case Configuration.UI_MODE_NIGHT_YES:
                    if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                        WebSettingsCompat.setForceDark(desc.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
                    }
                    break;

                case Configuration.UI_MODE_NIGHT_NO:
                    if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                        WebSettingsCompat.setForceDark(desc.getSettings(), WebSettingsCompat.FORCE_DARK_OFF);
                        desc.setBackgroundColor(itemView.getResources().getColor(R.color.cardBackgroundLight));
                    }
                    break;

                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    break;
            }
            desc.getSettings().setJavaScriptEnabled(true);
            desc.setVerticalScrollBarEnabled(false);
        }
    }
}
