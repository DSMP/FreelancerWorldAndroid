package not_an_example.com.freelancerworld;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import not_an_example.com.freelancerworld.Fragments.JobFiltersFragment;
import not_an_example.com.freelancerworld.Fragments.JobsTakenFragment;
import not_an_example.com.freelancerworld.Fragments.MainFragment;
import not_an_example.com.freelancerworld.Fragments.MakeJobFragment;
import not_an_example.com.freelancerworld.Fragments.MyRequestsFragment;
import not_an_example.com.freelancerworld.Fragments.SettingsFragment;
import not_an_example.com.freelancerworld.Fragments.UserProfileFragment;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.Utils.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainFragment.OnFragmentInteractionListener,
        JobsTakenFragment.OnFragmentInteractionListener, JobFiltersFragment.OnFragmentInteractionListener,
        UserProfileFragment.OnFragmentInteractionListener, MakeJobFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener, MyRequestsFragment.OnFragmentInteractionListener{

    UserModel userModel;
    Gson gson;

    private Fragment SelectedFragment;

    private TextView mNickNameView;
    private TextView mFullNameView;
    private TextView mSpecView;
    private ImageView mImageView;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userModel = new UserModel();
        gson = Utils.getGsonInstance();
        userModel = gson.fromJson(getIntent().getStringExtra("user_profile"), UserModel.class);


//        mMenu = gson.fromJson(getIntent().getStringExtra("ham-menu"), Menu.class);

        this.supportInvalidateOptionsMenu();

        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = MainFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentFL, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void refreshMenu()
    {
        if (mMenu!=null)
        {
            mMenu.clear();
            this.onCreateOptionsMenu(this.mMenu);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the mMenu; this adds items to the action bar if it is present.
        this.setMenu(menu);
        userModel = gson.fromJson(getIntent().getStringExtra("user_profile"), UserModel.class);
//        getIntent().putExtra("ham-menu", new Gson().toJson(menu));
        getMenuInflater().inflate(R.menu.main, menu);
        mNickNameView = (TextView) findViewById(R.id.nickNameView);
        mFullNameView = (TextView) findViewById(R.id.fullNameView);
        mSpecView = (TextView) findViewById(R.id.specView);
        mImageView = (ImageView) findViewById(R.id.logOffImageView);
        mNickNameView.setText(userModel.email);
        mFullNameView.setText(userModel.name + " " + userModel.lastName);
        mSpecView.setText("Brak specjalizacji");
        if (userModel.professions.length != 0) {
            mSpecView.setText(userModel.professions[0].name);
        }
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOff();
            }
        });
        return true;
    }

    private void logOff()
    {
        Intent LogingIntent = new Intent(this, LoginActivity.class);
//        setResult( Activity.RESULT_OK, LogingIntent );
        startActivity(LogingIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.Dashboard) {
            fragmentClass= MainFragment.class;
        }else if (id == R.id.JobsTaken) {
            fragmentClass = JobsTakenFragment.class;
        } else if (id == R.id.JobFilters) {
            fragmentClass = JobFiltersFragment.class;
        } else if (id == R.id.MakeJob) {
            fragmentClass = MakeJobFragment.class;
        } else if (id == R.id.MyRequests) {
            fragmentClass = MyRequestsFragment.class;
        } else if (id == R.id.YourProfile) {
            fragmentClass = UserProfileFragment.class;
        } else if (id == R.id.Settings) {
            fragmentClass = SettingsFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentFL, fragment).commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public UserModel getUserModel()
    {
        return userModel;
    }

    private void setMenu(Menu menu) {
        this.mMenu = menu;
    }


}
