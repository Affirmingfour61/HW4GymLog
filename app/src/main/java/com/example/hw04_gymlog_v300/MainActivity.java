package com.example.hw04_gymlog_v300;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hw04_gymlog_v300.database.GymLogRepository;
import com.example.hw04_gymlog_v300.database.entities.GymLog;
import com.example.hw04_gymlog_v300.database.entities.User;
import com.example.hw04_gymlog_v300.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String MAIN_ACTIVITY_USER_ID ="com.example.hw04_gymlog_v300.MAIN_ACTIVITY_USER_ID" ;
    private static final int LOGGED_OUT = -1;
    private static final String SHARED_PREFENCE_USER_VALUE =" com.example.hw04_gymlog_v300.SHARED_PREFENCE_USER_VALUE";
    ActivityMainBinding binding;
private GymLogRepository repository;
String mExercise="";
static final String SHARED_PREFENCE_USER_ID="com.example.hw04_gymlog_v300.SHARED_PREFENCE_USER_ID";
double mWeight=0.0;
int mReps=0;
private int loggedInUserId=-1;

private User user;

public static final String TAG= "DAC_GYMLOG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginUser();

        if(loggedInUserId==-1){
            Intent intent =LoginActivity.loginIntentFactory(getApplicationContext());
            startActivity(intent);
        }
        invalidateOptionsMenu();

        repository=  GymLogRepository.getRepository(getApplication());
        binding.logDisplayTextView.setMovementMethod(new ScrollingMovementMethod());
        updateDisplay();

        binding.logButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){

           getInforamtionFromDisplay();
                insertGymLogRecord();
                updateDisplay();

            }
        });

binding.exerciseInputEditText.setOnClickListener((new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        updateDisplay();
    }
}));
    }

    private void loginUser() {


        SharedPreferences sharedPreference=getApplicationContext().getSharedPreferences(SHARED_PREFENCE_USER_ID,Context.MODE_PRIVATE);
loggedInUserId = sharedPreference.getInt(SHARED_PREFENCE_USER_VALUE,LOGGED_OUT);

if(loggedInUserId!=LOGGED_OUT){
return;
}
        loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID,LOGGED_OUT);

        if(loggedInUserId==LOGGED_OUT){
            return;
        }

    }
static Intent mainActivityIntentFactory(Context context,int userId){
        Intent intent =new Intent(context,MainActivity.class);
              intent.putExtra(MAIN_ACTIVITY_USER_ID,userId);
        return intent;
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.logout_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item =menu.findItem(R.id.logoutMenuItem);
        item.setVisible(true);

        item.setTitle(user.getUsername());
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                showLogoutDialog();

//                startActivity();

                return false;
            }
        });
        return true;
    }

    private void showLogoutDialog(){
        AlertDialog.Builder alertBuilder= new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alertDialog =alertBuilder.create();

        alertBuilder.setMessage("Logout?");

        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertBuilder.create().show();
    }
    private void logout() {
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    private void insertGymLogRecord(){
        if(mExercise.isEmpty()){
            return ;
        }

        GymLog log= new GymLog(mReps,mWeight, mExercise,loggedInUserId);
        repository.insertGymLog(log);
    }

    public void updateDisplay(){
        ArrayList<GymLog> allLogs=repository.getAllLogs();
        if(allLogs.isEmpty()){
            binding.logDisplayTextView.setText(R.string.nothing_to_show_time_to_hit_the_gym);
        }
        StringBuilder sb= new StringBuilder();
        for(GymLog log:allLogs){
            sb.append(log);
        }

         binding.logDisplayTextView.setText(sb.toString());
    }

    private void getInforamtionFromDisplay(){
        mExercise =binding.exerciseInputEditText.getText().toString();

        try {
            mWeight = Double.parseDouble(binding.weightInputEditText.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG, "Error reading value from weight edit text.");
        }

        try {
            mReps = Integer.parseInt(binding.repInputEditText.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG, "Error reading value from reps edit text.");
        }
    }
    }