package com.example.javaradio.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.javaradio.MainActivity;
import com.example.javaradio.R;
import com.example.javaradio.databinding.RadioChannelBinding;
import com.example.javaradio.interfaces.OnRadioPlayedListener;
import com.example.javaradio.models.Radio;

import java.io.IOException;
import java.util.List;

public class RadioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    public List<Radio> list;
    public int index;
    OnRadioPlayedListener listener;

    MediaPlayer mediaPlayer = new MediaPlayer();

    public RadioAdapter(Context context, List<Radio> list, OnRadioPlayedListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RadioChannelBinding binding = RadioChannelBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
        );
        return new RadioViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        RadioChannelBinding b =((RadioViewHolder) holder).binding;
        Radio radio = list.get(position);

        index = holder.getOldPosition();

        b.channelName.setText("FM : " + radio.name);
        b.channelTagline.setText("" + radio.tagline);
        b.channelCategory.setText("" + radio.category.toUpperCase());

        b.channelName.setTextColor(Color.WHITE);
        b.channelTagline.setTextColor(Color.WHITE);
        b.channelCategory.setTextColor(Color.WHITE);

        b.layout.setBackgroundColor(Color.parseColor("#" + radio.color));

        setupRadioChannelImage(b, radio);

    }

    private void setupRadioChannelImage(RadioChannelBinding b, Radio radio) {
        Glide.with(context)
                .load(radio.image)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        b.channelProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(b.channelImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RadioViewHolder extends RecyclerView.ViewHolder {

        RadioChannelBinding binding;

        public RadioViewHolder(@NonNull RadioChannelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.buttonPlayPause2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (((MainActivity) context).musicState) {
//                        binding.buttonPlayPause2.setBackgroundResource(R.drawable.ic_pause);
//                    } else {
//                        binding.buttonPlayPause2.setBackgroundResource(R.drawable.ic_play);
//                    }
                    listener.onPressed(getAdapterPosition());
                }
            });

        }

    }

}
