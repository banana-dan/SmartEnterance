package com.example.schoolhelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StudentSettingsActivity extends AppCompatActivity {

    public static final String PREF_NAME = "learner_name";
    public static final String PREF_ID = "learner_id";

    EditText name_edit_text;
    EditText id_edit_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_settings);
        Toolbar toolbar = findViewById(R.id.base_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name_edit_text = findViewById(R.id.student_settings_edit_text_name);
        id_edit_text = findViewById(R.id.student_settings_edit_text_id);

        // выставление текста для редактирования
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // имя
        String name = preferences.getString(StudentSettingsActivity.PREF_NAME,
                getResources().getString(R.string.no_name_text));
        if (!name.equals(getResources().getString(R.string.no_name_text)) && !name.equals("")) {
            name_edit_text.setText(name);
        }
        Long id = preferences.getLong(StudentSettingsActivity.PREF_ID, -1L);
        if (id > 0L && id < 9999999999L) {
            id_edit_text.setText(id.toString());
        }

        // сброс пользовательского режима
        findViewById(R.id.student_settings_delete_user_mode_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                // name
                editor.putString(StudentSettingsActivity.PREF_NAME, getResources().getString(R.string.no_name_text));
                editor.putLong(StudentSettingsActivity.PREF_ID, -1L);


                editor.putInt(MainActivity.PREF_USER_MODE, -1);
                editor.apply();

                setResult(StudentMainPage.RESULT_CLEAR_DATA);
                finish();
            }
        });

        // сохранение information по нажатию на кнопку
        findViewById(R.id.student_settings_save_user_info_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();

                if (!name_edit_text.getText().toString().equals("")) {
                    editor.putString(PREF_NAME, name_edit_text.getText().toString());
                } else {
                    editor.putString(PREF_NAME, getResources().getString(R.string.no_name_text));
                }
                if (android.text.TextUtils.isDigitsOnly(id_edit_text.getText().toString().trim())) {
                    if (id_edit_text.getText().toString().trim().equals("")) {
                        editor.putLong(PREF_ID, -1L);
                    } else {
                        editor.putLong(PREF_ID, Long.parseLong(id_edit_text.getText().toString().trim()));
                        Log.e("TAG", "onClick: " + Long.parseLong(id_edit_text.getText().toString().trim()));
                    }
                } else {
                    editor.putLong(PREF_ID, -1L);
                }
                editor.apply();

                setResult(StudentMainPage.RESULT_UPDATE);
                finish();
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {//кнопка назад в actionBar
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    interface MyBackDialogInterface {
        void onBackData(String tag);
    }
}