package com.stronans.android.agenda;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.stronans.android.agenda.activities.About;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.fragments.AgendaFragment;
import com.stronans.android.agenda.fragments.DayFragment;
import com.stronans.android.agenda.fragments.MonthFragment;
import com.stronans.android.agenda.fragments.TasksFragment;
import com.stronans.android.agenda.fragments.WeekFragment;
import com.stronans.android.agenda.fragments.YearFragment;
import com.stronans.android.controllers.AgendaController;

public class TabHandler extends FragmentActivity
{
    static AgendaController controller;
    TabHost mTabHost;
    Resources resources;

    /* (non-Javadoc)
     * @see android.app.Activity#onContentChanged()
     */
    @Override
    public void onContentChanged()
    {
        // TODO Auto-generated method stub
        super.onContentChanged();
    }

    public void onCreate(Bundle savedInstanceState) {
        ViewPager  mViewPager;
        TabsAdapter mTabsAdapter;
        
        super.onCreate(savedInstanceState);
        
        // Initial display resources used by the program
        AgendaStaticData.getStaticData().getConfig().setResources(getResources());
        resources = getResources();
        controller = AgendaController.getInst();
        
        AgendaData.getInst().setContext(getBaseContext());
        
        setContentView(R.layout.tablayout);

        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (ViewPager)findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

        mTabsAdapter.addTab(getSpec(mTabHost, resources.getString(R.string.agenda), R.drawable.agendaview), AgendaFragment.class, null);
        mTabsAdapter.addTab(getSpec(mTabHost, resources.getString(R.string.tasks),  R.drawable.taskview),   TasksFragment.class, null);
        mTabsAdapter.addTab(getSpec(mTabHost, resources.getString(R.string.day),    R.drawable.dayview),    DayFragment.class, null);
        mTabsAdapter.addTab(getSpec(mTabHost, resources.getString(R.string.week),   R.drawable.weekview),   WeekFragment.class, null);
        mTabsAdapter.addTab(getSpec(mTabHost, resources.getString(R.string.month),  R.drawable.monthview),  MonthFragment.class, null);
        mTabsAdapter.addTab(getSpec(mTabHost, resources.getString(R.string.year),   R.drawable.yearview),   YearFragment.class, null);

        mTabHost.setCurrentTab(controller.getViewInt());        // Start Tab (to be loaded from perm. storage)
        controller.setTabHost(mTabHost);            // To allow other parts of the program to access this.
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(outState != null)
        {
//          outState.putString("tab", mTabHost.getCurrentTabTag());
//          super.onSaveInstanceState(outState);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.menu_exit:
            finish();
            return true;
            
        case R.id.menu_config:
            Toast.makeText(this, "Config is Selected", Toast.LENGTH_SHORT).show();
            return true;

        case R.id.menu_about:
            Intent intent = new Intent(this, About.class);
            startActivity(intent);      
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private TabHost.TabSpec getSpec(TabHost tabHost, String labelId, int drawableId)
    {
        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId); 
        
        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);
        
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(labelId);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);
        
        spec.setIndicator(tabIndicator);
        
        return spec;
    }
    
    /**
     * This is a helper class that implements the management of tabs and all
     * details of connecting a ViewPager with associated TabHost.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between pages.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct paged in the ViewPager whenever the selected
     * tab changes.
     */
    public static class TabsAdapter extends FragmentPagerAdapter
            implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo {
//            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
//                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.addOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        @Override
        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
