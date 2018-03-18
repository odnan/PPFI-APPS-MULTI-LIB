package id.or.ppfi.listadapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import id.or.ppfi.R;
import id.or.ppfi.config.AppController;
import id.or.ppfi.config.CircularNetworkImageView;
import id.or.ppfi.entities.M_WellnessRate;

public class ListAdapterWellnessRate extends BaseAdapter implements Filterable {
	private Activity activity;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	private LayoutInflater inflater;
	private List<M_WellnessRate> list, filterd;



	public ListAdapterWellnessRate(Activity activity, List<M_WellnessRate> list) {
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
			convertView = inflater.inflate(R.layout.listadapter_wellness_rate,
					null);
		// }
		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		 
		CircularNetworkImageView thumbNail = (CircularNetworkImageView) convertView.findViewById(R.id.thumbnail);

		TextView TextUsername = (TextView) convertView.findViewById(R.id.text_username);
		TextView Text1 = (TextView) convertView.findViewById(R.id.text1);
		TextView Text2 = (TextView) convertView.findViewById(R.id.text2);
		TextView Text3 = (TextView) convertView.findViewById(R.id.text3);
		ImageView imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);

		M_WellnessRate data = filterd.get(position);

		thumbNail.setDefaultImageResId(R.drawable._default);
		if(data.getGambar().length() > 0){
			if(data.getGambar().contains("http://portal") || data.getGambar().contains("https://portal"))
				thumbNail.setImageUrl(data.getGambar().replace("portal.iamprima.com","iamprima.com/portal"), imageLoader);
			else{
				try{
					thumbNail.setImageUrl(data.getGambar(), imageLoader);
				}catch (Exception e){
					thumbNail.setErrorImageResId(R.drawable._corrupted);
				}
			}
		}
		thumbNail.setDefaultImageResId(R.drawable._default);
		thumbNail.setErrorImageResId(R.drawable._corrupted);

		TextUsername.setText(data.getUsername_atlet());
		Text1.setText(data.getName());
		Text2.setText(data.getMaster_group_name());
		Text3.setText("Created Wellness : "+data.getCreated_time());

		if(Integer.parseInt(data.getWellness_rate()) == 0){
			imageView1.setImageResource(R.drawable.ic_grey);
		}else {
			if (Integer.parseInt(data.getValue_wellness()) > 0 &&
					Integer.parseInt(data.getValue_wellness()) <= 59) {
				imageView1.setImageResource(R.drawable.ic_red);
			} else if (Integer.parseInt(data.getValue_wellness()) >= 60 &&
					Integer.parseInt(data.getValue_wellness()) <= 69) {
				imageView1.setImageResource(R.drawable.ic_orange);
			} else if (Integer.parseInt(data.getValue_wellness()) >= 70 &&
					Integer.parseInt(data.getValue_wellness()) <= 79) {
				imageView1.setImageResource(R.drawable.ic_yellow);
			} else if (Integer.parseInt(data.getValue_wellness()) >= 80 &&
					Integer.parseInt(data.getValue_wellness()) <= 89) {
				imageView1.setImageResource(R.drawable.ic_green);
			} else if(Integer.parseInt(data.getValue_wellness()) >= 90 &&
					Integer.parseInt(data.getValue_wellness()) <= 100){
				imageView1.setImageResource(R.drawable.ic_green_arrow);
			}
			else {
				imageView1.setImageResource(R.drawable.ic_grey);
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
			List<M_WellnessRate> filteredData = new ArrayList<M_WellnessRate>();
			FilterResults result = new FilterResults();
			String filterString = constraint.toString().toLowerCase();
			for (M_WellnessRate data : list) {
				if (data.getName().toLowerCase().contains(filterString) ||
						data.getUsername_atlet().toLowerCase().contains(filterString) ||
						data.getGroupcode().toLowerCase().contains(filterString) ||
						data.getMaster_group_name().toLowerCase().contains(filterString)) {
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
			filterd = (List<M_WellnessRate>) results.values;
			notifyDataSetChanged();
		}

	}
	
	private Drawable LoadImageFromWebOperations(String url)
    {
          try{
        InputStream is = (InputStream) new URL(url).getContent();
        Drawable d = Drawable.createFromStream(is, "src name");
        return d;
      }catch (Exception e) {
        System.out.println("Exc="+e);
        return null;
      }
    }

}