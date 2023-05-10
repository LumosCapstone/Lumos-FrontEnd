package com.example.lumoshp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.lumoshp.ui.home.HomeFragment;
import com.example.lumoshp.ui.notifications.notifFragment;
import com.example.lumoshp.ui.profile.profileFrag;
import com.example.lumoshp.ui.search.searchfragment;
import com.example.lumoshp.ui.searchresources.srfragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    HomeFragment homeF = new HomeFragment();
    notifFragment notF = new notifFragment();
    searchfragment searchF =  new searchfragment();
    srfragment sr = new  srfragment();
    profileFrag profileF = new profileFrag();

    private TextView text_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Api
        text_result = findViewById(R.id.text_result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://lumos.benjaminwoodward.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //instance for interface
        hpApi hpapi = retrofit.create(hpApi.class);

        Call<homePage> call = hpapi.getId(1);

        call.enqueue(new Callback <homePage>() {
            @Override
            public void onResponse(Call<homePage> call, Response<homePage> response) {
                if(response.code() != 200){
                    text_result.setText("Check Connection");
                    return;
                }

                //get data
                String json = "";
                json = "ID= " + response.body().getId() +
                        "\n name= " + response.body().getName() +
                        "\n type= " + response.body().getType() +
                        "\n quantity= " + response.body().getQuantity();
                text_result.append(json);


//                if(!response.isSuccessful()){
//                    text_result.setText("Code: " + response.code());
//                    return;
//                }
//                homePage posts = response.body();

//                for (homePage hp : posts){
//                    String content = "";
//                    content += "ID: " +  hp.getId() + "\n";
//                    content += "Name" + hp.getName() + "\n\n";
//
//                    text_result.append(content);
//
//                }

            }

            @Override
            public void onFailure(Call <homePage> call, Throwable t) {
                text_result.setText(t.getMessage());

            }
        });





        //bottom Nav
        bottomNav = findViewById(R.id.bottomNavigationView);

        //App always opens on the homepage
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeF).commit();

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeF).commit();
                        return true;
                    case R.id.search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, searchF).commit();
                        return true;
                    case R.id.notifications:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, notF).commit();
                        return true;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileF).commit();
                        return true;
                    case R.id.searchresource:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, sr).commit();
                        return true;
                }
                return false;
            }
        });
    }
}