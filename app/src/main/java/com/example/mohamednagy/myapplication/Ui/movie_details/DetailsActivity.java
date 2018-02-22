package com.example.mohamednagy.myapplication.Ui.movie_details;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.mohamednagy.myapplication.R;

/**
 * Created by mohamednagy on 11/10/2016.
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        DetailsFragment detailsFragment =
                new DetailsFragment();
        detailsFragment.setArguments(bundle);
        detailsFragment.setToolbar(toolbar);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container,detailsFragment,"").commit();


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        try {
            ActionBar actionBar = getSupportActionBar();

            actionBar.setDisplayShowTitleEnabled(false);
        }catch (NullPointerException e){
            Log.e("error",e.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return false;
    }

}
