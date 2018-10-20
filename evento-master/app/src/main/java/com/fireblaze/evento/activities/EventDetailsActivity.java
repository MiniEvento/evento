package com.fireblaze.evento.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fireblaze.evento.Constants;
import com.fireblaze.evento.R;
import com.fireblaze.evento.databinding.ActivityEventDetailsBinding;
import com.fireblaze.evento.models.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EventDetailsActivity extends BaseActivity {

    public static final String EVENT_ID_KEYWORD = "EVENT_ID";
    public static final String ORGANIZER_ID_KEYWORD = "ORGANIZER_ID";
    protected DatabaseReference mDatabase,mRatingDatebase;
    protected Event myEvent;
    private Toolbar toolbar;
    ActivityEventDetailsBinding binding;

    //Rating

    long total,total1,total2,total3,total4,total5;
    long tot,tot1,tot2,tot3,tot4;

    //End


    public static void navigate(@NonNull Context activity, @NonNull String eventID){
        Intent i = new Intent(activity,EventDetailsActivity.class);
        i.putExtra(EVENT_ID_KEYWORD,eventID);
        activity.startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_event_details);
        getViews();

        final String eventID = getIntent().getStringExtra(EVENT_ID_KEYWORD);
        if(eventID==null){
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
            finish();
            return;

        }
        showProgressDialog();
        mDatabase.child(Constants.EVENTS_KEYWORD).child(eventID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        myEvent = dataSnapshot.getValue(Event.class);
                        if(myEvent != null)
                            setupView();
                        else {
                            Toast.makeText(EventDetailsActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void setupView(){
        binding.content.textName.setText(myEvent.getName());
        binding.content.textBookingCount.setText(String.valueOf(myEvent.getBookingsCount()));
        binding.content.textEventDetails.setText(myEvent.getDescription());
        binding.content.textEventVenue.setText(myEvent.getVenue());
        binding.content.textCreatedDate.setText(myEvent.getCreatedDateString());
        binding.content.textCategory.setText(myEvent.getCategory());
        String fees = "" + myEvent.getParticipationFees();
        String prize = "Prize:" + myEvent.getPrizeAmount();
        binding.content.textFees.setText(fees);
        binding.content.textPrize.setText(prize);


        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        final String key = myEvent.getEventID();



        Glide.with(getApplicationContext()).load(myEvent.getImage()).into(binding.content.mainImage);

        toolbar.setTitle("");

        if(myEvent.getBookings().containsKey(getUid())){
            binding.content.btnBookNow.setEnabled(false);
            binding.content.btnBookNow.setBackgroundColor(Color.GRAY);
        }

        binding.content.btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent i = new Intent(EventDetailsActivity.this,PaymentActivity.class);
                    startActivity(i);


                myEvent.book(getUid());
            }
        });
        hideProgressDialog();

        //RatingBar


        binding.content.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rate = "Rating : " + ratingBar.getRating();

                mDatabase = FirebaseDatabase.getInstance().getReference().child("Ratings");
                mRatingDatebase = mDatabase.child(key);

                Toast.makeText(getApplicationContext(),rate,Toast.LENGTH_SHORT).show();

                mRatingDatebase.child("Rating 5");
                mRatingDatebase.child("Rating 4");
                mRatingDatebase.child("Rating 3");
                mRatingDatebase.child("Rating 2");
                mRatingDatebase.child("Rating 1");

                if(ratingBar.getRating() == 5){
                    mRatingDatebase.child("Rating 5").child(getUid()).setValue(ratingBar.getRating());

                    mRatingDatebase.child("Rating 4").child(getUid()).removeValue();
                    mRatingDatebase.child("Rating 3").child(getUid()).removeValue();
                    mRatingDatebase.child("Rating 2").child(getUid()).removeValue();
                    mRatingDatebase.child("Rating 1").child(getUid()).removeValue();
                }

                else if(ratingBar.getRating() == 4){
                    mRatingDatebase.child("Rating 4").child(getUid()).setValue(ratingBar.getRating());

                    mRatingDatebase.child("Rating 5").child(getUid()).removeValue();
                    mRatingDatebase.child("Rating 3").child(getUid()).removeValue();
                    mRatingDatebase.child("Rating 2").child(getUid()).removeValue();
                    mRatingDatebase.child("Rating 1").child(getUid()).removeValue();
                }

                else if(ratingBar.getRating() == 3){
                    mRatingDatebase.child("Rating 3").child(getUid()).setValue(ratingBar.getRating());

                    mRatingDatebase.child("Rating 5").child(getUid()).removeValue();
                    mRatingDatebase.child("Rating 4").child(getUid()).removeValue();
                    mRatingDatebase.child("Rating 2").child(getUid()).removeValue();
                    mRatingDatebase.child("Rating 1").child(getUid()).removeValue();
                }

                else if(ratingBar.getRating() == 2){
                    mRatingDatebase.child("Rating 2").child(getUid()).setValue(ratingBar.getRating());

                    mRatingDatebase.child("Rating 5").child(getUid()).removeValue();
                    mRatingDatebase.child("Rating 4").child(getUid()).removeValue();
                    mRatingDatebase.child("Rating 3").child(getUid()).removeValue();
                    mRatingDatebase.child("Rating 1").child(getUid()).removeValue();
                }

                else if(ratingBar.getRating() == 1){
                    mRatingDatebase.child("Rating 1").child(getUid()).setValue(ratingBar.getRating());

                    mRatingDatebase.child("Rating 5").child(getUid()).removeValue();
                    mRatingDatebase.child("Rating 4").child(getUid()).removeValue();
                    mRatingDatebase.child("Rating 3").child(getUid()).removeValue();
                    mRatingDatebase.child("Rating 2").child(getUid()).removeValue();
                }
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference();



        final TextView t1 = findViewById(R.id.textView1);
        final TextView t2 = findViewById(R.id.textView2);
        final TextView t3 = findViewById(R.id.textView3);
        final TextView t4 = findViewById(R.id.textView4);
        final TextView t5 = findViewById(R.id.textView5);
        final TextView t6 = findViewById(R.id.avgRating);
        final TextView t7 = findViewById(R.id.textView7);


        mDatabase.child("Ratings").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                    long cnt = dataSnapshot.child("Rating 5").getChildrenCount();
                    String cnt1 = String.valueOf(cnt);
                    t1.setText(cnt1);
                    tot1 = cnt;
                    total1 = cnt*5;

                    long cnt2 = dataSnapshot.child("Rating 4").getChildrenCount();
                    String cnt3 = String.valueOf(cnt2);
                    t2.setText(cnt3);
                    tot2 = cnt2;
                    total2 = cnt2*4;

                    long cnt4 = dataSnapshot.child("Rating 3").getChildrenCount();
                    String cnt5 = String.valueOf(cnt4);
                    t3.setText(cnt5);
                    tot3 = cnt4;
                    total3 = cnt4*3;

                    long cnt6 = dataSnapshot.child("Rating 2").getChildrenCount();
                    String cnt7 = String.valueOf(cnt6);
                    t4.setText(cnt7);
                    tot4 = cnt6;
                    total4 = cnt6*2;

                    long cnt8 = dataSnapshot.child("Rating 1").getChildrenCount();
                    String cnt9 = String.valueOf(cnt8);
                    t5.setText(cnt9);
                    total5 = cnt8;

                    tot = (int) tot1+tot2+tot3+tot4+total5;
                    total = total1+total2+total3+total4+total5;

                    String totReviews = String.valueOf(tot);
                    t7.setText(totReviews);

                    float avg = (float)total/tot;
                    String avgDisplay = String.valueOf(avg);
                    t6.setText(avgDisplay);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        mDatabase.child("Ratings").child(key).child("Rating 5").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
//
//                    long cnt = dataSnapshot.getChildrenCount();
//
//                    total1 = cnt*5;
//
//                    String cnt1 = String.valueOf(cnt);
//                    t1.setText(cnt1);
//
////                    float rating = Float.parseFloat(dataSnapshot.getValue().toString());
////                    ratingBar.setRating(rating);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        mDatabase.child("Ratings").child(key).child("Rating 4").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
//
//                    long cnt = dataSnapshot.getChildrenCount();
//
//                    total2 = cnt*4;
//
//                    String cnt1 = String.valueOf(cnt);
//                    t2.setText(cnt1);
//
////                    float rating = Float.parseFloat(dataSnapshot.getValue().toString());
////                    ratingBar.setRating(rating);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        mDatabase.child("Ratings").child(key).child("Rating 3").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
//
//                    long cnt = dataSnapshot.getChildrenCount();
//
//                    total3 = cnt*3;
//
//                    String cnt1 = String.valueOf(cnt);
//                    t3.setText(cnt1);
//
////                    float rating = Float.parseFloat(dataSnapshot.getValue().toString());
////                    ratingBar.setRating(rating);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        mDatabase.child("Ratings").child(key).child("Rating 2").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
//
//                    long cnt = dataSnapshot.getChildrenCount();
//
//                    total4 = cnt*2;
//
//                    String cnt1 = String.valueOf(cnt);
//                    t4.setText(cnt1);
//
////                    float rating = Float.parseFloat(dataSnapshot.getValue().toString());
////                    ratingBar.setRating(rating);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        mDatabase.child("Ratings").child(key).child("Rating 1").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
//
//                    long cnt = dataSnapshot.getChildrenCount();
//
//                    total5 = cnt*1;
//
//                    String cnt1 = String.valueOf(cnt);
//                    t5.setText(cnt1);
//
////                    float rating = Float.parseFloat(dataSnapshot.getValue().toString());
////                    ratingBar.setRating(rating);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


//        mDatabase.child("Ratings").child(key).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
//
//                    long total = total1+total2+total3+total4+total5;
//
//                    String avg = String.valueOf(total);
//                    t6.setText(avg);
//
////                    float rating = Float.parseFloat(dataSnapshot.getValue().toString());
////                    ratingBar.setRating(rating);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        //EndRating

    }

    private void getViews(){
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mBookedEventsDatabase = mDatabase.child(Constants.BOOKED_EVENTS);
    }
    @Override
    public View getContainer() {
        return binding.getRoot();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
