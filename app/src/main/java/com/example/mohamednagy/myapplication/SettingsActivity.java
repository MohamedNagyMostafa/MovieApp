package com.example.mohamednagy.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String SORT_POPULAR_SETTING_VALUE = "popularity.desc";
    private static final String SORT_RATING_SETTING_VALUE = "vote_average.desc";
    private static final String SORT_FAVORITE_SETTING_VALUE = "favorite";
    private static final String SORT_POPULAR_SUMMARY = "Most popular movies";
    private static final String SORT_RATING_SUMMARY = "Highest rated movies";
    private static final String SORT_FAVORITE_SUMMARY =  "Favorite Movies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

    public static class MovieCinemaPreferenceFragment extends PreferenceFragment
    implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference sort =
                    findPreference(getString(R.string.settings_sort_key));

            bindPreferenceSummaryToValue(sort);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String settingValue = o.toString();

            switch (settingValue){
                case SORT_POPULAR_SETTING_VALUE:
                    preference.setSummary(SORT_POPULAR_SUMMARY);
                    break;
                case SORT_RATING_SETTING_VALUE:
                    preference.setSummary(SORT_RATING_SUMMARY);
                    break;
                case SORT_FAVORITE_SETTING_VALUE:
                    preference.setSummary(SORT_FAVORITE_SUMMARY);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue
                (Preference preference){
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(
                            preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(),"");
            onPreferenceChange(preference,preferenceString);
        }

    }

}
