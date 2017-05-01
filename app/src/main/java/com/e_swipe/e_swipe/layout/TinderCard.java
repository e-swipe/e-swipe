package com.e_swipe.e_swipe.layout;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.fragments.SwipeFragment;
import com.e_swipe.e_swipe.objects.Profile;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

/**
 * Class that represent a tinder card view
 */
@Layout(R.layout.tinder_card_view)
public class TinderCard {
    /**
     * Subviews
     */
    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    private TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;

    /**
     * Profile related
     */
    private Profile mProfile;
    /**
     * Application Context
     */
    private Context mContext;

    /**
     * View that will handle the swap events
     */
    private SwipePlaceHolderView mSwipeView;

    /**
     * Current listener
     */
    private onSwipeListener onSwipeListener;

    /**
     * Constructor
     * @param context Application Context
     * @param profile Profile displayed in tinderCard
     * @param swipeView Holder related to swipe events
     */
    public TinderCard(Context context, Profile profile, SwipePlaceHolderView swipeView) {
        mContext = context;
        mProfile = profile;
        mSwipeView = swipeView;
    }

    @Resolve
    /**
     * onResolved set the subviews to there values in profile
     */
    private void onResolved(){
        Glide.with(mContext).load(mProfile.getImageUrl()).into(profileImageView);
        nameAgeTxt.setText(mProfile.getName() + ", " + mProfile.getAge());
        locationNameTxt.setText(mProfile.getLocation());
    }

    public void setOnSwipeListener(onSwipeListener onSwipeListener){
        this.onSwipeListener = onSwipeListener;
    }

    /**
     * Listeners to swipe events
     */
    @SwipeOut
    private void onSwipedOut() {
        onSwipeListener.onCardChange(this);
        onSwipeListener.onSwipeCancel();
        mSwipeView.addView(this);
        mSwipeView.clearAnimation();
        mSwipeView.clearFocus();
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
        onSwipeListener.onSwipeCancel();
    }

    @SwipeIn
    private void onSwipeIn(){
        onSwipeListener.onSwipeCancel();
        Log.d("EVENT", "onSwipedIn");
    }

    @SwipeInState
    private void onSwipeInState(){
        onSwipeListener.onSwipeStarted();
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        onSwipeListener.onSwipeStarted();
        Log.d("EVENT", "onSwipeOutState");
    }

    public interface onSwipeListener{
        public void onCardChange(TinderCard tinderCard);
        public void onSwipeCancel();
        public void onSwipeStarted();
    }
    //Tinder card -> Fragment Parent -> CustomViewPager
}
