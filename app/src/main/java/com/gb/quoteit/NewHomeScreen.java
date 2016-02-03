/*
 * Copyright (C) 2016 Frederik Schweiger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gb.quoteit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gb.gallery.activities.ImageGalleryActivity;
import com.gb.gallery.enums.PaletteColorType;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.notification.quoteit.Alramsetting;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import link.fls.SwipeStack;

public class NewHomeScreen extends AppCompatActivity implements SwipeStack.SwipeStackListener {


    FloatingActionButton action_share;
    FloatingActionButton actionC_invite;
    FloatingActionButton action_gallery;
    FloatingActionButton action_setting;
    LikeButton likeButton;


    private InterstitialAd mInterstitialAd;


    private ArrayList<String> mData;
    private SwipeStack mSwipeStack;
    private SwipeStackAdapter mAdapter;

    String imgurl[] = {"http://cdn-media-1.lifehack.org/wp-content/files/2013/08/square-quote-4-export.png", "https://s-media-cache-ak0.pinimg.com/236x/bc/e0/77/bce0775f23fcb522932571d4d86d8807.jpg",
            "http://smashinghub.com/wp-content/uploads/2013/05/famous-quotes-17.jpg"};

    String dateString[]={"1st Jan 2016","2nd Jan 2016","3rd Jan 2016","4th Jan 2016"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stack_home_screen);

        mSwipeStack = (SwipeStack) findViewById(R.id.swipeStack);

        mData = new ArrayList<>();
        mAdapter = new SwipeStackAdapter(mData);
        mSwipeStack.setAdapter(mAdapter);
        mSwipeStack.setListener(this);


        fillWithTestData();


        bindFabButton();
        loadAdBanner();
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        mSwipeStack.resetStack();

        QuoteData quoteData =new QuoteData("http://cdn-media-1.lifehack.org/wp-content/files/2013/08/square-quote-4-export.png","1st Jan 2016","1","","");
        quoteData.save();

        QuoteData quoteData1 =new QuoteData("https://s-media-cache-ak0.pinimg.com/236x/bc/e0/77/bce0775f23fcb522932571d4d86d8807.jpg","2nd Jan 2016","0","","");
        quoteData1.save();


        QuoteData quoteData2 =new QuoteData("http://smashinghub.com/wp-content/uploads/2013/05/famous-quotes-17.jpg","3rd Jan 2016","1","","");
        quoteData2.save();

        QuoteData quoteData3 =new QuoteData("https://s-media-cache-ak0.pinimg.com/236x/bc/e0/77/bce0775f23fcb522932571d4d86d8807.jpg","4th Jan 2016","0","","");
        quoteData3.save();


    }

    private void fillWithTestData() {
        for (int x = 0; x < dateString.length; x++) {
            mData.add(dateString[x]);
        }


    }


    @Override
    public void onViewSwipedToRight(int position) {
        String swipedElement = mAdapter.getItem(position);

        if (position==2)
            likeButton.setLiked(true);
        else if (likeButton.isEnabled())
            likeButton.setLiked(false);


    }

    @Override
    public void onViewSwipedToLeft(int position) {
        String swipedElement = mAdapter.getItem(position);



    }

    @Override
    public void onStackEmpty() {
      //  Toast.makeText(this, "Back to Present", Toast.LENGTH_SHORT).show();
        mSwipeStack.resetStack();

    }

    public class SwipeStackAdapter extends BaseAdapter {

        private List<String> mData;

        public SwipeStackAdapter(List<String> data) {
            this.mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ImageViewHolder holder = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.card, parent, false);
                holder = new ImageViewHolder(convertView);
                convertView.setTag(holder);
            } else {

                holder = (ImageViewHolder) convertView.getTag();

            }



            if (position == 3) {
                holder.mImageView.setImageResource(R.drawable.demoimg);
                holder.mtextView.setText(dateString[position]);
            } else {
                String image = imgurl[position];
                setUpImage(holder.mImageView, image);
                holder.mtextView.setText(dateString[position]);

            }
            return convertView;
        }
    }


    private void setUpImage(ImageView iv, String imageUrl) {


        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(iv.getContext())
                    .load(imageUrl)
                    .resize(550, 800)
                    .centerCrop()
                    .into(iv);

        } else {
            iv.setImageResource(R.drawable.demoimg);
        }

    }


    public void loadAdBanner() {
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);

    }


    public void bindFabButton() {

        likeButton = (LikeButton) findViewById(R.id.like_button);
        FloatingActionsMenu floatingActionsMenu=(FloatingActionsMenu)findViewById(R.id.multiple_actions);
        action_share = (FloatingActionButton) findViewById(R.id.action_share);
        actionC_invite = (FloatingActionButton) findViewById(R.id.action_invitefriend);
        action_gallery = (FloatingActionButton) findViewById(R.id.action_gallery);
        action_setting = (FloatingActionButton) findViewById(R.id.action_setting);



        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {




            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });

        action_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareClick();

            }
        });

        actionC_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareClick();

            }
        });
        action_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryclick();

            }
        });
        action_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent alramIntent = new Intent(NewHomeScreen.this, Alramsetting.class);
                startActivity(alramIntent);
            }

        });


    }

    public void shareClick() {
        //create the send intent
        Intent shareIntent =
                new Intent(android.content.Intent.ACTION_SEND);

        //set the type
        shareIntent.setType("text/plain");

        //add a subject
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                "Insert Subject Here");

        //build the body of the message to be shared
        String shareMessage = "Insert message body here.";


        //add the message
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                shareMessage);

        //start the chooser for sharing
        startActivity(Intent.createChooser(shareIntent,
                "Share Quote Via"));
    }


    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdClosed() {
                finish();

            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();

        }
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.

        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }


    public void galleryclick() {
        Intent intent = new Intent(this, ImageGalleryActivity.class);

        ArrayList<String> images = new ArrayList<>();

        String imgurl[] = {"http://cdn-media-1.lifehack.org/wp-content/files/2013/08/square-quote-4-export.png", "https://s-media-cache-ak0.pinimg.com/236x/bc/e0/77/bce0775f23fcb522932571d4d86d8807.jpg",
                "http://smashinghub.com/wp-content/uploads/2013/05/famous-quotes-17.jpg"};

        for (int i = 0; i < imgurl.length; i++) {
            images.add(imgurl[i]);
        }

        intent.putStringArrayListExtra("images", images);
        // optionally set background color using Palette
        intent.putExtra("palette_color_type", PaletteColorType.VIBRANT);

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        showInterstitial();
        return;
    }


    public static class ImageViewHolder {
        private final ImageView mImageView;
        private final TextView mtextView;

        public ImageViewHolder(final View view) {
            mImageView = (ImageView) view.findViewById(R.id.landing_image);
            mtextView = (TextView) view.findViewById(R.id.textViewCard);
        }
    }


}
