package com.fireblaze.evento.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.fireblaze.evento.Constants;
import com.fireblaze.evento.R;
import com.fireblaze.evento.adapters.OrganizerGalleryAdapter;
import com.fireblaze.evento.databinding.ActivityOrganizerDetails2Binding;
import com.fireblaze.evento.models.Organizer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrganizerDetailsActivity2 extends BaseActivity {

    private ActivityOrganizerDetails2Binding binding;
    private DatabaseReference mDatabase;
    private Organizer mOrganizer;

    public static final String ORGANIZER_ID = "organizerID";
    private String organizerID;

    @Override
    public View getContainer() {
        return binding.getRoot();
    }

    public static void navigate(Context context, String organizerID){
        Intent i = new Intent(context,OrganizerDetailsActivity2.class);
        i.putExtra(ORGANIZER_ID,organizerID);
        context.startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_organizer_details2);

        setSupportActionBar(binding.toolbar);
        if(getSupportActionBar() != null){
            binding.toolbar.setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Bundle b = getIntent().getExtras();
        final String organizerID;

        if(b== null){
            throw new RuntimeException("bundle is null");
        }
        if((organizerID = b.getString(ORGANIZER_ID))== null){
            throw new RuntimeException("Pass organizerID in intent");
        }

        mDatabase.child(Constants.ORGANIZER_KEYWORD).child(organizerID).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Organizer organizer = dataSnapshot.getValue(Organizer.class);
                        setupView(organizer);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );


    }
    private void setupView(Organizer o){
        showProgressDialog();
        mOrganizer = o;
        organizerID = o.getOrganizerID();
        binding.content.textTitle.setText(o.getName());
        binding.content.textEmail.setText(o.getEmail());
        binding.content.textContact.setText(o.getPhone());
        //binding.content.textWebsite.setText(o.getWebsite());
        binding.content.textBookingCount.setText(String.valueOf(o.getBookmarkCount()));
//        binding.content.btnBecomeVolunteer.setOnClickListener(this);
//        TODO: setupImages();
//        binding.content.textEmail.setOnClickListener(this);
//        binding.fab.setOnClickListener(this);
//        updateVolunteerStatus();
        hideProgressDialog();
    }

//    private void sendMail(String email){
//        Intent emailIntent = new Intent(Intent.ACTION_SEND);
//        emailIntent.setData(Uri.parse("mailto:"));
//        emailIntent.setType("text/plain");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String []{email});
//        try {
//            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//        } catch (ActivityNotFoundException e){
//            Toast.makeText(OrganizerDetailsActivity2.this,"There is no email client installed!",Toast.LENGTH_SHORT).show();
//        }
//    }
    private void setupImages(){
        String [] images = {
                "http://placekitten.com/300/400",
                "http://placekitten.com/300/400",
                "http://placekitten.com/300/400"
        };
        OrganizerGalleryAdapter adapter = new OrganizerGalleryAdapter(this,images);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        binding.content.imagesRecycler.setLayoutManager(manager);
        binding.content.imagesRecycler.setAdapter(adapter);

    }

//    @Override
//    public void onClick(View view) {
//        switch(view.getId()){
//            case R.id.btn_become_volunteer:
//                becomeVolunteer();
//                break;
//            case R.id.text_email:
//                sendMail(mOrganizer.getEmail());
//                break;
//            case R.id.fab:
//                sendMail(mOrganizer.getEmail());
//                break;
//
//
//        }

//    }
//    private void becomeVolunteer(){
//        mDatabase.child(Constants.ORGANIZER_KEYWORD).child(organizerID).runTransaction(
//                new Transaction.Handler() {
//                    @Override
//                    public Transaction.Result doTransaction(MutableData mutableData) {
//                        Organizer o = mutableData.getValue(Organizer.class);
//                        if(o == null){
//                            return Transaction.success(mutableData);
//                        }
//                        if(o.getVolunteers().containsKey(getUid())){
//                            Snackbar.make(getContainer(), "Consider volunteering next time!",Snackbar.LENGTH_SHORT).show();
//                        } else {
//                            Snackbar.make(getContainer(),"You are now a volunteer", Snackbar.LENGTH_SHORT).show();
//                        }
//                        o.becomeVolunteer(getUid());
//                        mutableData.setValue(o);
//                        return Transaction.success(mutableData);
//                    }
//
//                    @Override
//                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//
//                    }
//                }
//        );
//    }
//    private void updateVolunteerStatus(){
//        if(mOrganizer.getVolunteers().containsKey(getUid())){
//           binding.content.btnBecomeVolunteer.setText(R.string.you_are_volunteer);
//        }
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
