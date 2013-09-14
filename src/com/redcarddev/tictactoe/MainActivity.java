package com.redcarddev.tictactoe;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig.Builder;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.BaseGameActivity;

public class MainActivity extends BaseGameActivity implements View.OnClickListener {
	
	private String LOGTAG = MainActivity.class.getName();
	
	Menu menu;
	
	int REQUEST_LEADERBOARD = 1;
	int REQUEST_ACHIEVEMENTS = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    findViewById(R.id.sign_in_button).setOnClickListener(this);
	    
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
		        
		        return true;
		        
	        case R.id.menu_invitations:
	        	
	        	// request code (can be any number, as long as it's unique)
	        	final int RC_INVITATION_INBOX = 10001;

	        	// launch the intent to show the invitation inbox screen
	        	Intent invitationIntent = getGamesClient().getInvitationInboxIntent();
	        	startActivityForResult(invitationIntent, RC_INVITATION_INBOX);
	        	
	        	
	        	return true;
	        	
	        case R.id.menu_send_invitations:
	        	
	        	// request code for the "select players" UI
	        	// can be any number as long as it's unique
	        	final int RC_SELECT_PLAYERS = 10000;

	        	// launch the player selection screen
	        	// minimum: 1 other player; maximum: 3 other players
	        	Intent sendInvitationIntent = getGamesClient().getSelectPlayersIntent(1, 1);
	        	startActivityForResult(sendInvitationIntent, RC_SELECT_PLAYERS);
	        	
	        	return true;
	        	
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
	}
	
	@Override
	public void onActivityResult(int request, int response, Intent data) {
	    if (request == 10001) {
	        if (response != Activity.RESULT_OK) {
	            // canceled
	            return;
	        }

	        // get the selected invitation
	        Bundle extras = data.getExtras();
	        Invitation invitation =
	            extras.getParcelable(getGamesClient().EXTRA_INVITATION);

	        // accept it!
	        RoomConfig roomConfig = makeBasicRoomConfigBuilder()
	                .setInvitationIdToAccept(invitation.getInvitationId())
	                .build();
	        getGamesClient().joinRoom(roomConfig);

	        // prevent screen from sleeping during handshake
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	        // go to game screen
	    }
	}
	
	private RoomConfig.Builder makeBasicRoomConfigBuilder() {
	    return RoomConfig.builder((RoomUpdateListener) this)
	            .setMessageReceivedListener((RealTimeMessageReceivedListener) this)
	            .setRoomStatusUpdateListener((RoomStatusUpdateListener) this);
	}

}
