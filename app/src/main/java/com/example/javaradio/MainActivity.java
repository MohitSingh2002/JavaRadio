package com.example.javaradio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.javaradio.adapters.RadioAdapter;
import com.example.javaradio.databinding.ActivityMainBinding;
//import com.example.javaradio.lists.RadioCustomList;
import com.example.javaradio.interfaces.OnRadioPlayedListener;
import com.example.javaradio.models.Radio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRadioPlayedListener {

    private static final String CHANNEL_ID = "notification";
    ActivityMainBinding binding;

    List<Radio> radioList = new ArrayList<>();

    RadioAdapter adapter;

    MediaPlayer mediaPlayer = new MediaPlayer();

    public boolean musicState = false;
    private NotificationManagerCompat managerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupList();

    }

    public String loadJsonFromAsset() {
        String json;
        try {
            InputStream inputStream = getAssets().open("radio.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private void setupList() {
        try {
            JSONObject jsonObject = new JSONObject(loadJsonFromAsset());
            JSONArray jsonArray = jsonObject.getJSONArray("radios");
            for (int i = 0; i <jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Radio radio = new Radio(
                        jsonObject1.getString("name"),
                        jsonObject1.getString("tagline"),
                        jsonObject1.getString("color"),
                        jsonObject1.getString("desc"),
                        jsonObject1.getString("url"),
                        jsonObject1.getString("icon"),
                        jsonObject1.getString("image"),
                        jsonObject1.getString("category"),
                        jsonObject1.getString("id")
                );
                radioList.add(radio);
                setupAdapter();
//                setupRadioListViews();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupAdapter() {
        adapter = new RadioAdapter(MainActivity.this, radioList, this);
        binding.recyclerView.setAdapter(adapter);
//        binding.recyclerView.getRecycledViewPool().setMaxRecycledViews(1, 50);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void onPressed(int position) {
        Log.e("TAG", radioList.get(position).name + "");
        musicState = !musicState;
        playFM(position);
    }

    private void playFM(int position) {
        if (musicState) {

            mediaPlayer.reset();

            Toast.makeText(MainActivity.this, "Please Wait!", Toast.LENGTH_SHORT).show();
//            b.buttonPlayPause.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(radioList.get(position).url);
                mediaPlayer.prepare();
                mediaPlayer.start();
                showFMNotification(radioList.get(position).name);
                Toast.makeText(MainActivity.this, "Playing!", Toast.LENGTH_SHORT).show();
            }catch (IOException e){
                // Catch the exception
                e.printStackTrace();
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }catch (SecurityException e){
                e.printStackTrace();
            }catch (IllegalStateException e){
                e.printStackTrace();
            }
        } else {

            managerCompat.cancelAll();

            Toast.makeText(this, "Stop!", Toast.LENGTH_SHORT).show();
            mediaPlayer.pause();
        }
    }

    private void showFMNotification(String title) {

        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music)
                .setContentTitle(title)
                .setContentText("Playing!")
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
