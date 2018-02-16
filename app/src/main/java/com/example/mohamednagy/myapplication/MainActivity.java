package com.example.mohamednagy.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.mohamednagy.myapplication.Ui.UriListener;
import com.example.mohamednagy.myapplication.helperClasses.Utility;

public class MainActivity extends AppCompatActivity
    implements UriListener {

    private static final String MAIN_FRAGMENT_TAG ="MainFragment";

    @Override
    protected void onResume() {
        super.onResume();

        MainActivityFragment mainActivityFragment =(MainActivityFragment)
                getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);

        if(mainActivityFragment != null)
            mainActivityFragment.setUriListener(this);
        Log.e("opopopop","onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("opopopop","onStart");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("opopopop","onStop");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("opopopop","onRestart");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("opopopop","onDestroy");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("opopopop","onPause");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add main_container To activity_main.xml As a fragment
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container,new MainActivityFragment(),MAIN_FRAGMENT_TAG).commit();
        }

        // Check current device (Tablet - Mobile )
        if(findViewById(R.id.detail_container) != null )
            Utility.setCurrentPaneUi(Utility.TWO_PANE_UI);
        else
            Utility.setCurrentPaneUi(Utility.ONE_PANE_UI);

        // Create a Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide App name
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Log.e("opopopop","onCreate");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settings_action){
            if(!Utility.getLoaderState()) {
                Intent settingsActivity =
                        new Intent(this, SettingsActivity.class);
                startActivity(settingsActivity);
                return true;
            }else{
                Toast.makeText(this,"Please wait until movies loading finish",Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUri(Uri uri) {

        switch (Utility.getCurrentPaneUi()){

            case Utility.ONE_PANE_UI :

                Intent detailActivity = new Intent(this,DetailsActivity.class);
                detailActivity.putExtra(Utility.URI_EXTRA_KEY,uri.toString());

                startActivity(detailActivity);

                break;

            case Utility.TWO_PANE_UI :

                DetailsFragment detailsFragment = new DetailsFragment();
                Bundle bundle = new Bundle();

                bundle.putString(Utility.URI_EXTRA_KEY,uri.toString());
                detailsFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_container,detailsFragment,"").commit();

                break;
        }
    }
}
