package com.e_swipe.e_swipe.activity;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.fragments.ProfilFragment.OnFragmentInteractionListener;
import com.e_swipe.e_swipe.layout.TinderCard;
import com.e_swipe.e_swipe.model.EventCard;
import com.e_swipe.e_swipe.objects.ChatRoom;
import com.e_swipe.e_swipe.objects.Person;
import com.e_swipe.e_swipe.fragments.ChatFragment;
import com.e_swipe.e_swipe.fragments.EventsFragment;
import com.e_swipe.e_swipe.fragments.ProfilFragment;
import com.e_swipe.e_swipe.fragments.SwipeFragment;
import com.e_swipe.e_swipe.viewPager.CustomViewPager;

import java.util.ArrayList;

/**
 * Activity related to the creation of fragments (Profil , Swipe , Events ..)
 */
public class TabbedActivity extends AppCompatActivity
        implements ProfilFragment.FragmentListenerCallback, SwipeFragment.OnFragmentInteractionListener, ChatFragment.OnFragmentInteractionListener, EventsFragment.OnFragmentInteractionListener, OnFragmentInteractionListener {

    /**
     * Adapter that will handle fragments/Pages
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private CustomViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        // Create the adapter that will return a fragment for each of the fourth primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    /**
     * Meethod that inflate the menu; this adds items to the action bar if it is present.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        return true;
    }

    @Override
    /**
     * Method that handle action bar item clicks here.
     * The action bar will automatically handle clicks on the Home/Up button,
     * so long as you specify a parent activity in AndroidManifest.xml.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void askForFinish() {
        Log.d("Debug", "Finish from tabbed");
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.finish();
        finish();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        /*
         * Constructor
         */
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        /**
         * Return fragment depending of the position (Profil , Swipe ...)
         */
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ProfilFragment.newInstance(getApplicationContext());
                case 1:
                    SwipeFragment swipeFragment = SwipeFragment.newInstance(getApplicationContext());
                    swipeFragment.setOnSwipeEventListener(new SwipeFragment.onSwipeEventListener() {
                        @Override
                        public void onCardChange(TinderCard tinderCard) {
                            mViewPager.setSwipeable(true);
                        }

                        @Override
                        public void onSwipeCancel() {
                            mViewPager.setSwipeable(true);
                        }

                        @Override
                        public void onSwipeStarted() {
                            mViewPager.setSwipeable(false);
                        }
                    });
                    return swipeFragment;
                case 2:
                    return EventsFragment.newInstance(getApplicationContext(), new ArrayList<EventCard>());
                case 3:
                    return ChatFragment.newInstance(getApplicationContext());
                default:
                    return ProfilFragment.newInstance(getApplicationContext());
            }
        }

        @Override
        /**
         * @return max number of fragments to create
         */
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        /**
         * @return Title of every fragments depending of the position
         */
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Profil";
                case 1:
                    return "Swipe";
                case 2:
                    return "Events";
                case 3:
                    return "Chats";
            }
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
