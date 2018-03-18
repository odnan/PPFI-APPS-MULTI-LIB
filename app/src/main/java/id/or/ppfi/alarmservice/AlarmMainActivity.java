package id.or.ppfi.alarmservice;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import id.or.ppfi.R;


public class AlarmMainActivity extends Activity
{

	private PendingIntent pendingIntent;
	NotificationCompat.Builder notification;
	PendingIntent pIntent;
	NotificationManager manager;
	Intent resultIntent;
	TaskStackBuilder stackBuilder;

	Button b1;



	@Override
	public void onCreate(Bundle savedInstanceState) 
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);

		b1=(Button) findViewById(R.id.button1);

		b1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startAlert();

			}
		});



	} //end onCreate


	public void startAlert() {
		EditText text = (EditText) findViewById(R.id.time);
		int i = Integer.parseInt(text.getText().toString());
		Intent intent = new Intent(this, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				this.getApplicationContext(), 234324243, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ (i * 1000), pendingIntent);
		Toast.makeText(this, "Alarm set in " + i + " seconds",Toast.LENGTH_LONG).show();


	}


}