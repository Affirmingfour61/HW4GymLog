package com.example.hw04_gymlog_v300;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hw04_gymlog_v300.database.GymLogRepository;
import com.example.hw04_gymlog_v300.database.entities.GymLog;
import com.example.hw04_gymlog_v300.databinding.ActivityMainBinding;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
private GymLogRepository repository;
String mExercise="";
double mWeight=0.0;
int mReps=0;

public static final String TAG= "DAC_GYMLOG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository=new GymLogRepository(getApplication());
        binding.logDisplayTextView.setMovementMethod(new ScrollingMovementMethod());

        binding.logButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                Toast.makeText(MainActivity.this,"It worked", Toast.LENGTH_SHORT).show();
           getInforamtionFromDisplay();
                insertGymLogRecord();
                updateDisplay();

            }
        });

    }
    private void insertGymLogRecord(){
        GymLog log= new GymLog(mReps,mWeight, mExercise);
        repository.insertGymLog(log);
    }

    public void updateDisplay(){
        String currentInfo=binding.logDisplayTextView.toString();
        Log.d(TAG,"current info: "+currentInfo);
        String newDisplay=String.format(Locale.US,"Exercise:%s%nWeight:%.2f%nReps:%d%n=-=-=-=%n%s",mExercise,mWeight,mReps,currentInfo);
        binding.logDisplayTextView.setText(newDisplay);

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