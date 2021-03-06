package com.fireblaze.evento.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fireblaze.evento.Constants;
import com.fireblaze.evento.R;
import com.fireblaze.evento.databinding.ActivityEventDetails2Binding;
import com.fireblaze.evento.models.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventDetailsActivity2 extends BaseActivity {

    public static final String EVENT_ID_KEYWORD = "EVENT_ID";
    public static final String ORGANIZER_ID_KEYWORD = "ORGANIZER_ID";
    private DatabaseReference mDatabase;
    private Event myEvent;
    private Toolbar toolbar;
    ActivityEventDetails2Binding binding;


    public static void navigate(@NonNull Context activity, @NonNull String eventID){
        Intent i = new Intent(activity,EventDetailsActivity2.class);
        i.putExtra(EVENT_ID_KEYWORD,eventID);
        activity.startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_event_details2);
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
                            Toast.makeText(EventDetailsActivity2.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
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


        Glide.with(getApplicationContext()).load(myEvent.getImage()).into(binding.content.mainImage);

        toolbar.setTitle("");

        hideProgressDialog();
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
