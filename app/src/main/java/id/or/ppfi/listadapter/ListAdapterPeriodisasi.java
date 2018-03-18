package id.or.ppfi.listadapter;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import id.or.ppfi.R;
import id.or.ppfi.config.AppController;
import id.or.ppfi.entities.M_Periodisasi;
import id.or.ppfi.periodization.FeedImageView;

public class ListAdapterPeriodisasi extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<M_Periodisasi> M_Periodisasi;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public ListAdapterPeriodisasi(Activity activity, List<M_Periodisasi> M_Periodisasi) {
		this.activity = activity;
		this.M_Periodisasi = M_Periodisasi;
	}

	@Override
	public int getCount() {
		return M_Periodisasi.size();
	}

	@Override
	public Object getItem(int location) {
		return M_Periodisasi.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.listadapter_periodization, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView username = (TextView) convertView.findViewById(R.id.username);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView createddate = (TextView) convertView.findViewById(R.id.created_date);
		TextView group = (TextView) convertView.findViewById(R.id.group);
		TextView text1 = (TextView) convertView.findViewById(R.id.text1);
		TextView text2 = (TextView) convertView.findViewById(R.id.text2);
		TextView text3 = (TextView) convertView.findViewById(R.id.text3);


		NetworkImageView profilePic = (NetworkImageView) convertView.findViewById(R.id.profilePic);
		FeedImageView feedImageView = (FeedImageView) convertView.findViewById(R.id.feedImage1);

		M_Periodisasi item = M_Periodisasi.get(position);

		username.setText(item.getUsername());
		name.setText(item.getName());
		createddate.setText(item.getCreatedDate() + " " + item.getCreatedTime());
		group.setText(item.getMaster_group_name());
		text1.setText("Periodization : "+item.getPlannedType());
		text2.setText("Ttitle : "+item.getTitle());
		text3.setText(item.getNotes());

		// Converting timestamp into x ago format
		/*
		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
				Long.parseLong(item.getCreatedTime()),
				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		timestamp.setText(timeAgo);

		// Chcek for empty status message
		if (!TextUtils.isEmpty(item.getNotes())) {
			text3.setText(item.getNotes());
			text3.setVisibility(View.VISIBLE);
		} else {
			// status is empty, remove from view
			text3.setVisibility(View.GONE);
		}

		/*
		// Checking for null feed url
		if (item.getUrlImage() != null) {
			text1.setText(Html.fromHtml("<a href=\"" + item.getUrlImage() + "\">"
					+ item.getUrlImage() + "</a> "));
			// Making url clickable
			text1.setMovementMethod(LinkMovementMethod.getInstance());
			text1.setVisibility(View.VISIBLE);
		} else {
			// url is null, remove from the view
			text1.setVisibility(View.GONE);
		}
		*/

		// user profile pic
		//profilePic.setImageUrl(item.getProfilePic(), imageLoader);
		profilePic.setDefaultImageResId(R.drawable._default);

		if(item.getUrl_profile().length() > 0){
			if(item.getUrl_profile().contains("http://portal") || item.getUrl_profile().contains("https://portal"))
				profilePic.setImageUrl(item.getUrl_profile().replace("portal.iamprima.com","iamprima.com/portal"), imageLoader);
			else
				profilePic.setImageUrl(item.getUrl_profile(), imageLoader);
		}
		profilePic.setDefaultImageResId(R.drawable._default);
		profilePic.setErrorImageResId(R.drawable._corrupted);


		// Feed image
		if (item.getUrlImage() != null) {
			feedImageView.setDefaultImageResId(R.drawable._no_image_available);
			//feedImageView.setImageUrl(item.getUrlImage(), imageLoader);
			if(item.getUrlImage().length() > 0){
				if(item.getUrlImage().contains("http://portal") || item.getUrlImage().contains("https://portal"))
					feedImageView.setImageUrl(item.getUrlImage().replace("portal.iamprima.com","iamprima.com/portal"), imageLoader);
				else
					feedImageView.setImageUrl(item.getUrlImage(), imageLoader);
			}
			feedImageView.setDefaultImageResId(R.drawable._no_image_available);
			feedImageView.setErrorImageResId(R.drawable._corrupted);

			feedImageView.setVisibility(View.VISIBLE);
			feedImageView
					.setResponseObserver(new FeedImageView.ResponseObserver() {
						@Override
						public void onError() {

						}

						@Override
						public void onSuccess() {
						}
					});
		} else {
			feedImageView.setVisibility(View.GONE);
		}

		return convertView;
	}

}
