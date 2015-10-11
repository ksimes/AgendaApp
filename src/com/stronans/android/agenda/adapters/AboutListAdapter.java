package com.stronans.android.agenda.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.model.AgendaLinkedMap;
import com.stronans.android.agenda.support.Utilities;

import java.text.MessageFormat;
import java.util.Map;

public class AboutListAdapter extends BaseAdapter {
    AgendaLinkedMap<String, String> items = new AgendaLinkedMap<>();
    Context context;
    AgendaConfiguration config;
    Resources resources;

    public AboutListAdapter(Context context) {
        super();
        this.context = context;
        config = AgendaStaticData.getStaticData().getConfig();
        resources = context.getResources();
        populateItems();
    }

    private void getAndroidVersion(Map<String, String> items) {
        // int versionId = android.os.Build.VERSION.SDK_INT;

        String name = android.os.Build.VERSION.CODENAME;
        String version = android.os.Build.VERSION.RELEASE;

        String android = MessageFormat.format("{0} {1}", name, version);

        items.put("Android version", android);
    }

    /**
     *
     */
    private void populateItems() {

        getAndroidVersion(items);

        // Space left in the onboard filesystem. Get the path from Environment()??
        // StatFs(String path)

        // String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.getEntry(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.aboutlistitem, null);
        }

        for (Map.Entry<String, String> entry : items.entrySet()) {
            Utilities.setTextView(view, R.id.about_item, entry.getKey());
            Utilities.setTextView(view, R.id.about_description, entry.getValue());
        }

        return view;
    }
}
