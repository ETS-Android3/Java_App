package uk.ac.abertay.cmp400.java_app;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
        //determine what view to inflate (Title Card OR Regular Card)
        if(displayModels.get(position).getIsTitle()){
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
        //Either inflates the display title layout or the display row layout depending on the viewType.

        if(viewType == 1){
            view = layoutInflater.inflate(R.layout.display_title, parent, false);
            return new ViewHolderImage(view);
        }
        view = layoutInflater.inflate(R.layout.display_row, parent, false);
        return new ViewHolderRow(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(displayModels.get(position).getIsTitle()){
            ViewHolderImage viewHolderTitle = (ViewHolderImage)holder;
            viewHolderTitle.title.setText(displayModels.get(position).getTitle());
            viewHolderTitle.desc.loadData(displayModels.get(position).getDescription(), "text/html; charset=utf-8", "UTF-8");
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

            //Set the Web view to work in dark mode if needed. also disables the vertical scroll bar.
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
            desc.setVerticalScrollBarEnabled(false);
        }
    }

    static class ViewHolderImage extends RecyclerView.ViewHolder{
        //class for each view that can be inflated along with the assignments.
        final TextView title;
        final WebView desc;

        public ViewHolderImage(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.ImageTitle);
            desc = itemView.findViewById(R.id.ImageDescription);
            int nightModeFlags =
                    title.getContext().getResources().getConfiguration().uiMode &
                            Configuration.UI_MODE_NIGHT_MASK;
            //This switch is used to allow the web view to toggle between dark mode and light mode. the default method did not toggle to dark mode so this solution was required.
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
            desc.setVerticalScrollBarEnabled(false);
        }
    }
}
