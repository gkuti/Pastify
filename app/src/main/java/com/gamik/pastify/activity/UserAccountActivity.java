package com.gamik.pastify.activity;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamik.pastify.R;
import com.gamik.pastify.model.DataItem;
import com.gamik.pastify.model.User;
import com.gamik.pastify.store.DataStore;
import com.gamik.pastify.store.Store;
import com.gamik.pastify.util.CircleTransformation;
import com.gamik.pastify.util.Date;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAccountActivity extends AppCompatActivity {
    private Store store;
    private DatabaseReference databaseReference;
    private
    List<DataItem> list = new ArrayList<>();
    String uid;
    TextView total, lastSync, email;
    RelativeLayout relativeLayoutSync, relativeLayoutRestore;
    ProgressDialog progressDialog;
    User user;
    ImageView imageView;
    DataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        initView();
        initComponent();
    }

    private void initView() {
        progressDialog = new ProgressDialog(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.user_account_toolbar);
        setSupportActionBar(toolbar);
        total = (TextView) findViewById(R.id.tv_total_data);
        lastSync = (TextView) findViewById(R.id.tv_last_sync);
        email = (TextView) findViewById(R.id.tv_user_acct_email);
        imageView = (ImageView) findViewById(R.id.imageView_user_acct);
        relativeLayoutSync = (RelativeLayout) findViewById(R.id.layout_sync);
        relativeLayoutRestore = (RelativeLayout) findViewById(R.id.layout_restore);
    }

    private void initComponent() {
        store = new Store(this);
        dataStore = new DataStore(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = getIntent().getParcelableExtra("user");
        uid = user.getId();
        getUserData();
    }

    public void storeUserData(View view) {
        progressDialog.setTitle("Syncing");
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        updateData(loadData());
        databaseReference.child("user-data").child(uid).setValue(loadData());
    }

    private List loadData() {
        List<DataItem> dataItemArrayList = new ArrayList<>();
        Cursor cursor = store.getAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String placeHolder = cursor.getString(cursor.getColumnIndex("PlaceHolder"));
            String value = cursor.getString(cursor.getColumnIndex("Value"));
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            int usage = cursor.getInt(cursor.getColumnIndex("Usage"));
            String date = cursor.getString(cursor.getColumnIndex("Date"));
            int status = cursor.getInt(cursor.getColumnIndex("Status"));
            DataItem dataItem = new DataItem(placeHolder, value, id, usage, date, status);
            dataItemArrayList.add(dataItem);
            cursor.moveToNext();
        }
        return dataItemArrayList;
    }

    private void getUserData() {
        progressDialog.setTitle("Checking user data");
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        lastSync.setText(dataStore.getData("lastsync"));
        Picasso.with(this)
                .load(user.getPictureUrl())
                .placeholder(R.drawable.ic_profile_template)
                .error(R.drawable.ic_profile_template)
                .transform(new CircleTransformation())
                .into(imageView);
        List<DataItem> list = loadData();
        int count = 0;
        for (DataItem dataItem : list) {
            if (dataItem.getStatus() == 1) {
                count++;
            }
        }
        total.setText(String.valueOf(count));
        email.setText(user.getEmail());
        progressDialog.dismiss();
    }

    public void updateData(List<DataItem> list) {
        for (DataItem dataItem : list) {
            store.updateDataByPlaceHolder(dataItem.getPlaceHolder(), "Status", 1);
            dataStore.saveData("lastsync", Date.getDate() + " " + Date.getTime());
            lastSync.setText(dataStore.getData("lastsync"));
        }
    }

    public void signOut(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void restore(View view) {
        progressDialog.setTitle("Restoring");
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<DataItem>> t = new GenericTypeIndicator<List<DataItem>>() {
                };
                List<DataItem> list = dataSnapshot.getValue(t);
                if (list != null) {
                    total.setText((String.valueOf(list.size())));
                    for (DataItem dataItem : list) {
                        Cursor cursor = store.getData(dataItem.getPlaceHolder());
                        if (cursor.getCount() > 0) {
                            store.updateDataById(dataItem.getPlaceHolder(), dataItem.getValue(), dataItem.getId());
                        } else {
                            store.saveData(dataItem.getPlaceHolder(), dataItem.getValue(), dataItem.getDate(), dataItem.getStatus(), dataItem.getUsage());
                        }
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        };
        databaseReference.child("user-data").child(uid).addValueEventListener(valueEventListener);
    }
}
