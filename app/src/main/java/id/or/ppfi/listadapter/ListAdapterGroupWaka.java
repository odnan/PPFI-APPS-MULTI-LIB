package id.or.ppfi.listadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import id.or.ppfi.R;
import id.or.ppfi.config.AppController;
import id.or.ppfi.config.CircularNetworkImageView;
import id.or.ppfi.entities.M_GroupWaka;

public class ListAdapterGroupWaka extends BaseAdapter implements Filterable {
	private Activity activity;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	private LayoutInflater inflater;
	private List<M_GroupWaka> list, filterd;

	public ListAdapterGroupWaka(Activity activity, List<M_GroupWaka> list) {
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
			convertView = inflater.inflate(R.layout.listadapter_all_cabor,
					null);
		// }
		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
//		 NetworkImageView thumbNail = (NetworkImageView) convertView
//		 .findViewById(R.id.thumbnail);

		CircularNetworkImageView thumbNail = (CircularNetworkImageView) convertView.findViewById(R.id.thumbnail);


		TextView Text1 = (TextView) convertView.findViewById(R.id.text1);
		TextView Text2 = (TextView) convertView.findViewById(R.id.text2);
		TextView Text3 = (TextView) convertView.findViewById(R.id.text3);

		M_GroupWaka data = filterd.get(position);
		thumbNail.setImageUrl(data.getGroupLogo(), imageLoader);
		Text1.setText(data.getGroupName());
		Text2.setText("Total Atlet   : "+data.getQtyAtlet());
		Text3.setText("Total Pelatih : "+data.getQtyPelatih());

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
			List<M_GroupWaka> filteredData = new ArrayList<M_GroupWaka>();
			FilterResults result = new FilterResults();
			String filterString = constraint.toString().toLowerCase();
			for (M_GroupWaka data : list) {
				if (data.getGroupName().toLowerCase().contains(filterString)) {
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
			filterd = (List<M_GroupWaka>) results.values;
			notifyDataSetChanged();
		}

	}

}