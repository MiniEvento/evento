package com.fireblaze.evento.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fireblaze.evento.R;

public class AboutUsActivity extends AppCompatActivity {
//    ActivityAboutUsBinding binding;
//    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);

//        setSupportActionBar(binding.toolbar);
//        if(getSupportActionBar()!=null){
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//
//        getFromDatabase();
//
//    }
//    private void getFromDatabase(){
//        mDatabase.child(Constants.TEAM_KEYWORD).addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if(dataSnapshot != null){
//                            setupView(dataSnapshot);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                }
//        );
//    }

    }
}
