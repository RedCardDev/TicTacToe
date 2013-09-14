package com.redcarddev.tictactoe;

import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.example.games.basegameutils.BaseGameActivity;

public class MainActivity extends BaseGameActivity implements View.OnClickListener {
	
	Menu menu;
	
	int REQUEST_LEADERBOARD = 1;
	int REQUEST_ACHIEVEMENTS = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    findViewById(R.id.sign_in_button).setOnClickListener(this); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.menu = menu;
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_leaderboard:
	        	
	        	String leaderboard = String.format(getResources().getString(R.string.leaderboard_fastest_win));
	        	
	        	startActivityForResult(getGamesClient().getLeaderboardIntent(leaderboard), REQUEST_LEADERBOARD);
	            return true;
	        case R.id.menu_achievements:
	        	
	        	startActivityForResult(getGamesClient().getAchievementsIntent(), REQUEST_ACHIEVEMENTS);
	        	
	        	return true;
	        	
	        case R.id.menu_signout:
	        	
	        	// sign out.
		        signOut();

		        // show sign-in button, hide the sign-out button
		        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
	        	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onClick(View view) {
	    if (view.getId() == R.id.sign_in_button) {
	        // start the asynchronous sign in flow
	        beginUserInitiatedSignIn();
	    }
	}
	
	public void onSignInSucceeded() {
	    // show sign-out button, hide the sign-in button
	    findViewById(R.id.sign_in_button).setVisibility(View.GONE);
	    this.menu.findItem(R.id.menu_signout).setVisible(true);

	    // (your code here: update UI, enable functionality that depends on sign in, etc)
	}
	
	@Override
	public void onSignInFailed() {
	    // Sign in has failed. So show the user the sign-in button.
	    findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
	    this.menu.findItem(R.id.menu_signout).setVisible(false);
	}

}
