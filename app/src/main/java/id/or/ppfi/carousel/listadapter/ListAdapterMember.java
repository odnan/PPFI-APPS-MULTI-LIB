package id.or.ppfi.carousel.listadapter;

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
import id.or.ppfi.carousel.entities.Member;
import id.or.ppfi.config.AppController;
import id.or.ppfi.config.CircularNetworkImageView;

/**
 * Created by emergency on 15/12/2016.
 */
public class ListAdapterMember extends BaseAdapter implements Filterable {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Member> list, filterd;
   ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ListAdapterMember(Activity activity, List<Member> list) {
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
          //  convertView = inflater.inflate(R.layout.listadapter_perda,null);
            convertView = inflater.inflate(R.layout.listview_row,null);
        // }

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        /*
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
		 NetworkImageView thumbNail = (NetworkImageView) convertView
		 .findViewById(R.id.thumbnail);
		 */

        CircularNetworkImageView thumbNail = (CircularNetworkImageView) convertView.findViewById(R.id.thumbnail);
       // CircularNetworkImageView thumbNail = (CircularNetworkImageView) convertView.findViewById(R.id.thumbnail);


        TextView Text1 = (TextView) convertView.findViewById(R.id.text1);

        TextView Text2 = (TextView) convertView.findViewById(R.id.text2);
       TextView Text3 = (TextView) convertView.findViewById(R.id.text3);

        Member data = filterd.get(position);
        Text1.setText(data.getName());
        Text2.setText(data.getLevel());
        Text3.setText(data.getExperience());

        /*
        if(fileExists(data.getFile_name_server())){
            Text1.setTextColor(Color.parseColor("#01579B"));
        }
    */
         thumbNail.setImageUrl(data.getUrl_image(), imageLoader);
      //  Text1.setText("No. "+data.getNo_produk_hukum());
      //  Text2.setText(data.getTentang());
      //  Text3.setText("Tahun "+data.getTahun_pembuatan()+" | "+data.getName() + " | "+data.getCount_download()+" Unduh Berkas");

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
            List<Member> filteredData = new ArrayList<Member>();
            FilterResults result = new FilterResults();
            String filterString = constraint.toString().toLowerCase();
            for (Member data : list) {

            }
            result.count = filteredData.size();
            result.values = filteredData;
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            filterd = (List<Member>) results.values;
            notifyDataSetChanged();
        }

    }

}