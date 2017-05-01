package com.e_swipe.e_swipe;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.e_swipe.e_swipe.layout.TinderCard;
import com.e_swipe.e_swipe.objects.Chat;
import com.e_swipe.e_swipe.objects.Event;
import com.e_swipe.e_swipe.objects.JsonLoader;
import com.e_swipe.e_swipe.objects.Person;
import com.e_swipe.e_swipe.fragments.ChatFragment;
import com.e_swipe.e_swipe.fragments.EventsFragment;
import com.e_swipe.e_swipe.fragments.ProfilFragment;
import com.e_swipe.e_swipe.fragments.SwipeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity related to the creation of fragments (Profil , Swipe , Events ..)
 */
public class TabbedActivity extends AppCompatActivity
        implements ProfilFragment.OnFragmentInteractionListener, SwipeFragment.OnFragmentInteractionListener, ChatFragment.OnFragmentInteractionListener, EventsFragment.OnFragmentInteractionListener {

    /**
     * Adapter that will handle fragments/Pages
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private CustomViewPager mViewPager;

    /**
     * The user profile
     */
    private Profil profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the fourth primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //Get FB userId
        Intent intent = getIntent();
        String userId = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String surname = intent.getStringExtra("surname");
        String birthday = intent.getStringExtra("birthday");

        profil = new Profil(userId,name,surname,birthday);
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
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return ProfilFragment.newInstance(getApplicationContext(),profil);
                case 1:
                    SwipeFragment swipeFragment = SwipeFragment.newInstance(getApplicationContext());
                    swipeFragment.setOnSwipeEventListener(new SwipeFragment.onSwipeEventListener() {
                        @Override
                        public void onFragmentCreated() {
                            Log.d("TEST","Fragment Created");
                        }

                        @Override
                        public void onCardChange(TinderCard tinderCard) {
                            Log.d("TEST","Card Change");
                        }

                        @Override
                        public void onSwipeCancel() {
                            Log.d("TEST","Swipe Cancel");
                            mViewPager.setSwipeable(true);
                        }

                        @Override
                        public void onSwipeStarted() {
                            Log.d("TEST","Swipe Started");
                            mViewPager.setSwipeable(false);
                        }
                    });
                    return swipeFragment;
                case 2:
                    List<Event> events = JsonLoader.loadEvents(getApplicationContext());
                    return EventsFragment.newInstance(getApplicationContext(),events);
                case 3:
                    ArrayList<Chat> chats = new ArrayList<Chat>();
                    chats.add(new Chat("Chat 1", new Person("Person 1", "token", "imageUrl"), new Person("Person 2", "token", "imageUrl")));
                    return ChatFragment.newInstance(chats);
                default:
                    return ProfilFragment.newInstance(getApplicationContext(),profil);
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
                    return "Chat";
            }
            return null;
        }
    }
}
