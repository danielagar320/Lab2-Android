package com.example.lab2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView breedText;
    private DogApiService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_dog);
        breedText = findViewById(R.id.text_breed);
        Button randomButton = findViewById(R.id.button_random);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dog.ceo/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(DogApiService.class);

        randomButton.setOnClickListener(v -> fetchRandomDogImage());
    }

    private void fetchRandomDogImage() {
        service.getRandomImage().enqueue(new Callback<DogResponse>() {
            @Override
            public void onResponse(Call<DogResponse> call, Response<DogResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    DogResponse dogResponse = response.body();
                    String imageUrl = dogResponse.getMessage();
                    String[] splitedUrl = imageUrl.split("/");
                    Glide.with(MainActivity.this).load(imageUrl).into(imageView);
                    breedText.setText(splitedUrl[4]);
                }
            }

            @Override
            public void onFailure(Call<DogResponse> call, Throwable t) {
                Log.d(String.valueOf(Log.DEBUG), "Me ñañe", t);
            }
        });
    }
}