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
import id.or.ppfi.entities.M_AllCabor;

public class ListAdapterAllCabor extends BaseAdapter implements Filterable {
	private Activity activity;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	private LayoutInflater inflater;
	private List<M_AllCabor> list, filterd;

	public ListAdapterAllCabor(Activity activity, List<M_AllCabor> list) {
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
			convertView = inflater.inflate(R.layout.listadapter_all_cabor_grid,
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

		TextView Text2_ = (TextView) convertView.findViewById(R.id.text2_);
		TextView Text3_ = (TextView) convertView.findViewById(R.id.text3_);

		TextView Text4 = (TextView) convertView.findViewById(R.id.text4);
		TextView Text4_ = (TextView) convertView.findViewById(R.id.text4_);


		M_AllCabor data = filterd.get(position);
		//thumbNail.setImageUrl(data.getGroupLogo(), imageLoader);
		if(data.getGroupLogo().length() > 0){
			if(data.getGroupLogo().contains("http://portal") || data.getGroupLogo().contains("https://portal"))
				thumbNail.setImageUrl(data.getGroupLogo().replace("portal.iamprima.com","iamprima.com/portal"), imageLoader);
			else
				thumbNail.setImageUrl(data.getGroupLogo(), imageLoader);
		}
		thumbNail.setDefaultImageResId(R.drawable.default_group);
		thumbNail.setErrorImageResId(R.drawable._corrupted);


		Text1.setText(data.getGroupName());
		Text2.setText(data.getQtyAtlet() + " Atlet");
		Text2_.setText(data.getQtyPelatih()+ " Pelatih");

		Text3.setText(data.getTotalAtletPutra()+" Putera");
		Text4.setText(data.getTotalAtletPutri()+" Puteri");

		Text3_.setVisibility(View.GONE);
		Text4_.setVisibility(View.GONE);
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
			List<M_AllCabor> filteredData = new ArrayList<M_AllCabor>();
			FilterResults result = new FilterResults();
			String filterString = constraint.toString().toLowerCase();
			for (M_AllCabor data : list) {
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
			filterd = (List<M_AllCabor>) results.values;
			notifyDataSetChanged();
		}

	}

}