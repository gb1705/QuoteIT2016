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

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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


    private FloatingActionButton action_share;
    private FloatingActionButton actionC_invite;
    private FloatingActionButton action_gallery;
    private FloatingActionButton action_setting;
    private LikeButton likeButton;


    private InterstitialAd mInterstitialAd;
    private ArrayList<String> mData;
    private SwipeStack mSwipeStack;
    private SwipeStackAdapter mAdapter;

    private DBHandler dbHandler;

    String imgurl[] = {"http://cdn-media-1.lifehack.org/wp-content/files/2013/08/square-quote-4-export.png", "https://s-media-cache-ak0.pinimg.com/236x/bc/e0/77/bce0775f23fcb522932571d4d86d8807.jpg", "http://smashinghub.com/wp-content/uploads/2013/05/famous-quotes-17.jpg"};

    String dateString[]={"1st Jan 2016","2nd Jan 2016","3rd Jan 2016","4th Jan 2016"};

    String FAV[]={"0","0","0","0"};
    RelativeLayout mainbg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stack_home_screen);
         mainbg= (RelativeLayout) findViewById(R.id.mainbg);
        final int sdk = android.os.Build.VERSION.SDK_INT;





        dbHandler = new DBHandler(this);
        mSwipeStack = (SwipeStack) findViewById(R.id.swipeStack);


        mData = new ArrayList<>();
        bindFabButton();
        loadAdBanner();
      //  fillWithTestData();
        mAdapter = new SwipeStackAdapter(mData);
        mSwipeStack.setAdapter(mAdapter);
        mSwipeStack.setListener(this);


         // insertdat();
        fillWithTestData();


        mInterstitialAd = newInterstitialAd();
        loadInterstitial();


    }

    private void fillWithTestData() {

        String[][] imageData = dbHandler.genericSelect("*", DBHandler.TABLE_NAME, "", "", "", 5);

        imgurl = new String[imageData.length];
        FAV = new String[imageData.length];
        dateString = new String[imageData.length];


        for (int x = 0; x < imageData.length; x++) {
            mData.add(imageData[x][2]);
            imgurl[x] = imageData[x][1];
            FAV[x] = imageData[x][3];
            dateString[x] = imageData[x][2];

        }
        if (FAV[0].equals("1"))
            likeButton.setLiked(true);


    }


    @Override
    public void onViewSwipedToRight(int position) {
        //String swipedElement = mAdapter.getItem(position);


        if (position == (FAV.length - 1)) {
            position = 0;
        } else
            position = position + 1;

        if (FAV[position].equals("1"))
            likeButton.setLiked(true);
        else if (likeButton.isEnabled())
            likeButton.setLiked(false);


    }

    @Override
    public void onViewSwipedToLeft(int position) {
        // String swipedElement = mAdapter.getItem(position);

        if (position == (FAV.length - 1)) {
            position = 0;
        } else
            position = position + 1;

        if (FAV[position].equals("1"))
            likeButton.setLiked(true);
        else if (likeButton.isEnabled())
            likeButton.setLiked(false);


    }

    @Override
    public void onStackEmpty() {
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

            ImageViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.card, parent, false);
                holder = new ImageViewHolder(convertView);
                convertView.setTag(holder);
            } else {

                holder = (ImageViewHolder) convertView.getTag();

            }

            ImageView  mImageView = (ImageView) convertView.findViewById(R.id.landing_image);

            if (position == 3) {
                mImageView.setImageResource(R.drawable.demoimg);
                holder.mtextView.setText(dateString[position]);
            } else {
                String image = imgurl[position];
                setUpImage(mImageView, image);
                holder.mtextView.setText(dateString[position]);

            }
            return convertView;
        }
    }


    private void setUpImage(ImageView iv, String imageUrl) {


        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(iv.getContext())
                    .load(imageUrl)
                    .resize(550, 900)
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
        FloatingActionsMenu floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        action_share = (FloatingActionButton) findViewById(R.id.action_share);
        actionC_invite = (FloatingActionButton) findViewById(R.id.action_invitefriend);
        action_gallery = (FloatingActionButton) findViewById(R.id.action_gallery);
        action_setting = (FloatingActionButton) findViewById(R.id.action_setting);


        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                int pos = mSwipeStack.getCurrentPosition();
                dbHandler.genericUpdate(DBHandler.TABLE_NAME, "FAV", "1", "IMAGE", imgurl[pos]);

                String[][] imageData = dbHandler.genericSelect("*", DBHandler.TABLE_NAME, "", "", "", 5);

                imgurl = new String[imageData.length];
                FAV = new String[imageData.length];
                dateString = new String[imageData.length];


                for (int x = 0; x < imageData.length; x++) {

                    imgurl[x] = imageData[x][1];
                    FAV[x] = imageData[x][3];
                    dateString[x] = imageData[x][2];

                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                int pos = mSwipeStack.getCurrentPosition();
                dbHandler.genericUpdate(DBHandler.TABLE_NAME, "FAV", "0", "IMAGE", imgurl[pos]);
                String[][] imageData = dbHandler.genericSelect("*", DBHandler.TABLE_NAME, "", "", "", 5);

                imgurl = new String[imageData.length];
                FAV = new String[imageData.length];
                dateString = new String[imageData.length];


                for (int x = 0; x < imageData.length; x++) {

                    imgurl[x] = imageData[x][1];
                    FAV[x] = imageData[x][3];
                    dateString[x] = imageData[x][2];

                }
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

        mSwipeStack.resetStack();
    }


    public void galleryclick() {
        Intent intent = new Intent(this, ImageGalleryActivity.class);

        ArrayList<String> images = new ArrayList<>();

        String[][] favimageurl = dbHandler.genericSelect("*", dbHandler.TABLE_NAME, "FAV = '1'", "", "", 3);

        if (favimageurl != null) {
            for (int i = 0; i < favimageurl.length; i++) {
                images.add(favimageurl[i][1]);
            }
            intent.putStringArrayListExtra("images", images);
            // optionally set background color using Palette
            intent.putExtra("palette_color_type", PaletteColorType.VIBRANT);
            startActivity(intent);
        } else {
            Toast.makeText(this, "You haven't mark the Favourite", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        showInterstitial();
    }


    public static class ImageViewHolder {
        private final ImageView mImageView;
        private final TextView mtextView;

        public ImageViewHolder(final View view) {
            mImageView = (ImageView) view.findViewById(R.id.landing_image);
            mtextView = (TextView) view.findViewById(R.id.textViewCard);
        }
    }

    void insertdat() {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        for (int i = 0; i < imgurl.length; i++) {
            values.put("IMAGE", imgurl[i]);
            values.put("DATE", dateString[i]);
            values.put("FAV", FAV[i]);
            values.put("COL4", "");
            values.put("COL5", "");
            db.insert(DBHandler.TABLE_NAME, null, values);


        }


    }


}
