package com.fireblaze.evento.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.fireblaze.evento.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RateusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rateus);

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final RatingBar simpleRatingBar = (RatingBar) findViewById(R.id.simpleRatingBar);
        final EditText editText = findViewById(R.id.editText2);
        final Button submitButton = (Button) findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rating = "Rating : " + simpleRatingBar.getRating();
                Toast.makeText(getApplicationContext(),rating,Toast.LENGTH_SHORT).show();

                final DatabaseReference rootRef,starRef,txtRef;

                rootRef = FirebaseDatabase.getInstance().getReference("Ratings");
                starRef = rootRef.child("Stars");
                txtRef = rootRef.child("Text");
//                starRef.push().setValue(simpleRatingBar.getRating());
//                txtRef.push().setValue(editText.getText().toString());


                FirebaseDatabase.getInstance().getReference(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        starRef.push().setValue(simpleRatingBar.getRating());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });


            }
        });
    }
}
