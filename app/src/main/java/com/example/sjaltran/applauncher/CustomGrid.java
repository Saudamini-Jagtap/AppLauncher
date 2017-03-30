package com.example.sjaltran.applauncher;

/**
 * Created by Saudamini on 29-03-2017.
 */

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomGrid extends BaseAdapter implements Filterable{
    private Context mContext;
    private List<ApplicationInfo> filteredAppList = null;
    private List<ApplicationInfo> apps;
    private AppFilter appFilter;
    private PackageManager packageManager;

    public CustomGrid(Context c, List<ApplicationInfo> apps ) {
        this.mContext = c;
        this.apps = apps;
        this.filteredAppList = apps;
        packageManager = c.getPackageManager();
        getFilter();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ((null != filteredAppList) ? filteredAppList.size() : 0);
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return filteredAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        grid = new View(mContext);
        grid = inflater.inflate(R.layout.grid_view_items, null);

        ApplicationInfo applicationInfo = filteredAppList.get(position);
        if (null != applicationInfo) {
            TextView appName = (TextView) grid.findViewById(R.id.textView);
            ImageView iconview = (ImageView) grid.findViewById(R.id.imageView);
            appName.setText(applicationInfo.loadLabel(packageManager));
            iconview.setImageDrawable(applicationInfo.loadIcon(packageManager));
        }
        return grid;
    }
    @Override
    public Filter getFilter() {
        if (appFilter == null) {
            appFilter = new AppFilter();
        }

        return appFilter;
    }

    /**
     * Custom filter for friend list
     * Filter content in friend list according to the search text
     */
    private class AppFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<ApplicationInfo> tempList= new ArrayList<ApplicationInfo>();
            if (constraint!=null && constraint.length()>0) {
                // search content in app list
                for (ApplicationInfo app : apps) {
                    if (app.loadLabel(packageManager).toString().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        tempList.add(app);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = 0;//apps.size();
                filterResults.values = null;//apps;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredAppList = (List<ApplicationInfo>) results.values;
            notifyDataSetChanged();
            
        }
    }

}
