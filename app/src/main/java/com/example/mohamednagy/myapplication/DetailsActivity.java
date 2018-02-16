package com.example.mohamednagy.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

/**
 * Created by mohamednagy on 11/10/2016.
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Create dynamic fragment
        if(savedInstanceState == null){
            // Get Data Intent
            /*
             * Test
             * Log.e("onCreate DetailActivity"," is called -----------");
             */

            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            DetailsFragment detailsFragment =
                    new DetailsFragment();
            detailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container,detailsFragment,"").commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Hide App name
        try {
            ActionBar actionBar = getSupportActionBar();

            actionBar.setDisplayShowTitleEnabled(false);
        }catch (NullPointerException e){
            Log.e("error",e.toString());
        }
    }

}
