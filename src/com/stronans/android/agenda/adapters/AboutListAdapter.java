package com.stronans.android.agenda.adapters;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.model.AboutData;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.support.Utilities;

public class AboutListAdapter extends BaseAdapter
{
    List<AboutData>     items = new ArrayList<AboutData>();
    Context             context;
    AgendaConfiguration config;
    Resources           resources;

    public AboutListAdapter(Context context)
    {
        super();
        this.context = context;
        config = AgendaStaticData.getStaticData().getConfig();
        resources = context.getResources();
        populateItems();
    }

    private AboutData getAndroidVersion()
    {
        // int versionId = android.os.Build.VERSION.SDK_INT;

        String name = android.os.Build.VERSION.CODENAME;
        String version = android.os.Build.VERSION.RELEASE;

        String android = MessageFormat.format("{0} {1}", name, version);

        return new AboutData("Android version", android);
    }

    /**
     * 
     */
    private void populateItems()
    {

        items.add(getAndroidVersion());

        // Space left in the onboard filesystem. Get the path from Environment()??
        // StatFs(String path)

        // String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
    }

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public Object getItem(int location)
    {
        return items.get(location);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;

        if (view == null)
        {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.aboutlistitem, null);
        }

        AboutData item = items.get(position);

        Utilities.setTextView(view, R.id.about_item, item.key());
        Utilities.setTextView(view, R.id.about_description, item.value());

        return view;
    }
}
