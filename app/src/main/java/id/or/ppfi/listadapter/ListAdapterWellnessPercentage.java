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
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import id.or.ppfi.R;
import id.or.ppfi.config.AppController;
import id.or.ppfi.entities.M_WellnessPercentage;

public class ListAdapterWellnessPercentage extends BaseAdapter implements Filterable {
	private Activity activity;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	private LayoutInflater inflater;
	private List<M_WellnessPercentage> list, filterd;



	public ListAdapterWellnessPercentage(Activity activity, List<M_WellnessPercentage> list) {
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
			convertView = inflater.inflate(R.layout.listadapter_wellness_percentage,
					null);
		// }
		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		
		ImageView imageView1 = (ImageView) convertView.findViewById(R.id.thumbnail);
		TableRow TableRow1 = (TableRow) convertView.findViewById(R.id.table_row);
		TextView Text1 = (TextView) convertView.findViewById(R.id.text1);
		TextView Text2 = (TextView) convertView.findViewById(R.id.text2);
		TextView Text3 = (TextView) convertView.findViewById(R.id.text3);
		TextView Text4 = (TextView) convertView.findViewById(R.id.text4);
		TextView Text5 = (TextView) convertView.findViewById(R.id.textWellness);

		M_WellnessPercentage data = filterd.get(position);

		Text2.setText("Total Atlet : "+data.getTotal_atlet());
		Text3.setText("Percentase : "+data.getTotal_percentage()+" %");


		/*
		Text1.setText(data.getName());

		if(data.getNomorEvent() == null) Text2.setText(data.getRoleName() + " | ");
		else Text2.setText(data.getRoleName() + " | " + data.getNomorEvent());

		Text3.setText("Cidera : "+data.getCidera());


		Text4.setText("Created Wellness : "+data.getWellnessTime());
		Text5.setText("username : "+data.getUserName());
		*/
			if(data.getWellness_rate().equals("0")){
				imageView1.setImageResource(R.drawable.ic_grey);
			}else if(data.getWellness_rate().equals("1")){
				imageView1.setImageResource(R.drawable.ic_red);
			}else if(data.getWellness_rate().equals("2")){
				imageView1.setImageResource(R.drawable.ic_orange);
			}else if(data.getWellness_rate().equals("3")){
				imageView1.setImageResource(R.drawable.ic_yellow);
			}else if(data.getWellness_rate().equals("4")){
				imageView1.setImageResource(R.drawable.ic_green);
			}else if(data.getWellness_rate().equals("5")) {
				imageView1.setImageResource(R.drawable.ic_green_arrow);
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
			List<M_WellnessPercentage> filteredData = new ArrayList<M_WellnessPercentage>();
			FilterResults result = new FilterResults();
			String filterString = constraint.toString().toLowerCase();
			for (M_WellnessPercentage data : list) {
				if (data.getUsername().toLowerCase().contains(filterString) ) {
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
			filterd = (List<M_WellnessPercentage>) results.values;
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