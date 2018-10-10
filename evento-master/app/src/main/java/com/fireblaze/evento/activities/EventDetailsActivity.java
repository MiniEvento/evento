package com.fireblaze.evento.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.media.Rating;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
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

import java.util.ArrayList;
import java.util.List;

public class EventDetailsActivity extends BaseActivity {

    public static final String EVENT_ID_KEYWORD = "EVENT_ID";
    public static final String ORGANIZER_ID_KEYWORD = "ORGANIZER_ID";
    protected DatabaseReference mDatabase,mRatingDatebase;
    protected Event myEvent;
    private Toolbar toolbar;
    ActivityEventDetailsBinding binding;


    //Rating

    ListView listview;
    ArrayList<String> list1;
    ArrayAdapter<String> adapter1;
    getRating rate;

    //EndRating


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

//        final String ratingID = mDatabase.push().getKey();
//        final String okey = myEvent.getOrganizerID();

        binding.content.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rate = "Rating : " + ratingBar.getRating();

                mDatabase = FirebaseDatabase.getInstance().getReference().child("Ratings");
                mRatingDatebase = mDatabase.child(key);

                Toast.makeText(getApplicationContext(),rate,Toast.LENGTH_SHORT).show();

                mRatingDatebase.child(getUid()).child("Review").setValue(ratingBar.getRating());;
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference(getUid());

//        final ListView l1 = findViewById(R.id.listview);
//
//        final List<String> itemList;
//        itemList = new ArrayList<>();

//        mDatabase.child("Ratings").child(key).child(getUid()).child("Review").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot myItem: dataSnapshot.getChildren()){
//                    itemList.clear();
//                    getRating rInfo = myItem.getValue(getRating.class);
//
//                    itemList.add(rInfo.Review);
//                }
//
//                adapter = new ArrayAdapter<>(EventDetailsActivity.this,android.R.layout.simple_list_item_1);
//                l1.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        rate = new getRating();
        listview = findViewById(R.id.listview);
        list1 = new ArrayList<>();
        adapter1 = new ArrayAdapter<>(this,R.layout.rating,R.id.RatingInfo,list1);

        mDatabase.child("Ratings").child(key).child(getUid()).child("Review").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    rate = ds.getValue(getRating.class);
                    list1.add(rate.getReview());
                }

                listview.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
