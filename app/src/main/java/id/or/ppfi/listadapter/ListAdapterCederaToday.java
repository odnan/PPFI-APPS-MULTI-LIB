package id.or.ppfi.listadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.or.ppfi.R;
import id.or.ppfi.config.AppController;
import id.or.ppfi.config.CircularNetworkImageView;
import id.or.ppfi.entities.M_CederaToday;

public class ListAdapterCederaToday extends BaseAdapter implements Filterable {
	private Activity activity;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	private LayoutInflater inflater;
	private List<M_CederaToday> list, filterd;



	public ListAdapterCederaToday(Activity activity, List<M_CederaToday> list) {
		this.activity = activity;
		this.list = list;
		this.filterd = this.list;
	}

	@Override
	public int getCount() {
		return filterd.size();
	}

	@Override
	public Object getItem(int position) {
		return filterd.get(position);
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
		if (convertView == null)// {
			// LayoutInflater inflater = LayoutInflater.from(this.context);
			convertView = inflater.inflate(R.layout.listadapter_cidera,
					null);
		// }
		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		
//		 NetworkImageView thumbNail = (NetworkImageView) convertView
//		 .findViewById(R.id.thumbnail);
		 
		   CircularNetworkImageView thumbNail = (CircularNetworkImageView) convertView.findViewById(R.id.thumbnail);
		
//		ImageView imageView1 = (ImageView) convertView.findViewById(R.id.thumbnail);

		 TableRow TableRow1 = (TableRow) convertView.findViewById(R.id.table_row);
		TextView Text1 = (TextView) convertView.findViewById(R.id.text1);
		TextView Text2 = (TextView) convertView.findViewById(R.id.text2);
		TextView Text3 = (TextView) convertView.findViewById(R.id.text3);
		TextView Text4 = (TextView) convertView.findViewById(R.id.text4);
		TextView Text5 = (TextView) convertView.findViewById(R.id.text5);
		ImageView imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
		ImageView imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);

		M_CederaToday data = filterd.get(position);
		
	//	thumbNail.setImageUrl(data.getGambar(), imageLoader);

		if(data.getGambar().length() > 0){
			if(data.getGambar().contains("http://portal") || data.getGambar().contains("https://portal"))
				thumbNail.setImageUrl(data.getGambar().replace("portal.iamprima.com","iamprima.com/portal"), imageLoader);
			else
				thumbNail.setImageUrl(data.getGambar(), imageLoader);
		}
		thumbNail.setDefaultImageResId(R.drawable._default);
		thumbNail.setErrorImageResId(R.drawable._corrupted);

		
		Text1.setText(data.getNameAtlet());
		Text2.setText(data.getGroupName());
		Text3.setText("Cidera : "+data.getCidera());
		Text5.setText("username atlet : "+data.getUsernameAtlet());

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String dateNow = df.format(new Date());

		DateFormat formatTime = new SimpleDateFormat("HH:mm:ppfi");
		String timeNow = formatTime.format(new Date());

		Text4.setText("Created Wellness "+data.getCreatedTimeToday());


		if(Integer.parseInt(timeNow.replace(":","")) > Integer.parseInt(data.getTimeOutNotif().replace(":","")))
			imageView2.setImageResource(R.drawable.ic_blank);
		else
			imageView2.setImageResource(R.drawable.ic_new);


		if(!data.getCreatedDateToday().equals(dateNow)){
			imageView1.setImageResource(R.drawable.ic_heart_default);
		}else{
			if(Integer.parseInt(data.getValueWellness()) >= 0 &&
					Integer.parseInt(data.getValueWellness()) <= 59){
				imageView1.setImageResource(R.drawable.ic_heart_red);
			}else if(Integer.parseInt(data.getValueWellness()) >= 60 &&
					Integer.parseInt(data.getValueWellness()) <= 69){
				imageView1.setImageResource(R.drawable.ic_heart_orange);
			}else if(Integer.parseInt(data.getValueWellness()) >= 70 &&
					Integer.parseInt(data.getValueWellness()) <= 79){
				imageView1.setImageResource(R.drawable.ic_heart_yellow);
			}else if(Integer.parseInt(data.getValueWellness()) > 80 &&
					Integer.parseInt(data.getValueWellness()) <= 89){
				imageView1.setImageResource(R.drawable.ic_heart_green_light);
			}
			else {
				imageView1.setImageResource(R.drawable.ic_heart_green_dark);
			}

		}

		return convertView;

	}

	@Override
	public Filter getFilter() {
		DataFilter filter = new DataFilter();
		return filter;
	}

	/** Class filter untuk melakukan filter (pencarian) */
	private class DataFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			List<M_CederaToday> filteredData = new ArrayList<M_CederaToday>();
			FilterResults result = new FilterResults();
			String filterString = constraint.toString().toLowerCase();
			for (M_CederaToday data : list) {
				if (data.getNameAtlet().toLowerCase().contains(filterString) ||
						data.getCidera().toLowerCase().contains(filterString) ||
						data.getGroupName().toLowerCase().contains(filterString) ||
						data.getUsernameAtlet().toLowerCase().contains(filterString)) {
					filteredData.add(data);
				}
			}
			result.count = filteredData.size();
			result.values = filteredData;
			return result;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			filterd = (List<M_CederaToday>) results.values;
			notifyDataSetChanged();
		}

	}


}