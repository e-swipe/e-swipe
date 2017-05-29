package com.e_swipe.e_swipe.layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.objects.ProfilTinderCard;
import com.e_swipe.e_swipe.server.Profil.Swipe;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
     * ProfilTinderCard related
     */
    private ProfilTinderCard mProfilTinderCard;
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
     * SharedPreferences
     */

    SharedPreferences sharedPreferences;

    /**
     * Constructor
     * @param context Application Context
     * @param profilTinderCard ProfilTinderCard displayed in tinderCard
     * @param swipeView Holder related to swipe events
     */
    public TinderCard(Context context, ProfilTinderCard profilTinderCard, SwipePlaceHolderView swipeView) {
        mContext = context;
        mProfilTinderCard = profilTinderCard;
        mSwipeView = swipeView;
        sharedPreferences = context.getSharedPreferences(
                    context.getString(R.string.user_file_key), Context.MODE_PRIVATE);
    }

    @Resolve
    /**
     * onResolved set the subviews to there values in profile
     */
    private void onResolved(){
        Glide.with(mContext).load(mProfilTinderCard.getImageUrl()).into(profileImageView);
        nameAgeTxt.setText(mProfilTinderCard.getName() + ", " + mProfilTinderCard.getAge());
        locationNameTxt.setText(mProfilTinderCard.getLocation());
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
        Swipe.decline(sharedPreferences.getString("auth",""), mProfilTinderCard.getUuid(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
        onSwipeListener.onSwipeCancel();
    }

    @SwipeIn
    private void onSwipeIn(){
        onSwipeListener.onSwipeCancel();
        Log.d("SwipeIn" , mProfilTinderCard.getUuid());
        Swipe.accept(sharedPreferences.getString("auth",""), mProfilTinderCard.getUuid(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("SwipeIn" , String.valueOf(response.code()));
            }
        });
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
