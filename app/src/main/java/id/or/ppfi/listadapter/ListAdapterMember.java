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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.or.ppfi.R;
import id.or.ppfi.config.AppController;
import id.or.ppfi.config.CircularNetworkImageView;
import id.or.ppfi.entities.M_Member;

public class ListAdapterMember extends BaseAdapter implements Filterable {
	private Activity activity;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	private LayoutInflater inflater;
	private List<M_Member> list, filterd;



	public ListAdapterMember(Activity activity, List<M_Member> list) {
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
			convertView = inflater.inflate(R.layout.listadapter_member,
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
		TextView Text5 = (TextView) convertView.findViewById(R.id.textWellness);
		ImageView imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);

		M_Member data = filterd.get(position);
		
		//thumbNail.setImageUrl(data.getGambar(), imageLoader);
		thumbNail.setDefaultImageResId(R.drawable._default);
		if(data.getGambar().length() > 0){
			if(data.getGambar().contains("http://portal") || data.getGambar().contains("https://portal"))
				thumbNail.setImageUrl(data.getGambar().replace("portal.iamprima.com","iamprima.com/portal"), imageLoader);
			else
				thumbNail.setImageUrl(data.getGambar(), imageLoader);
		}
		thumbNail.setDefaultImageResId(R.drawable._default);
		thumbNail.setErrorImageResId(R.drawable._corrupted);
		
		//bikin dari url 
// 	   	Drawable drawable = LoadImageFromWebOperations(data.getGambar());
// 	   	
//         //bikin circle
// 	   	Bitmap bm = ((BitmapDrawable) drawable).getBitmap();
// 	   	
// 	   imageView1.setImageBitmap(bm);
		
		//ga dipake dulu
//		Bitmap circleBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
//		BitmapShader shader = new BitmapShader (bm,  TileMode.CLAMP, TileMode.CLAMP);
//		Paint paint = new Paint();
//		paint.setShader(shader);
//		Canvas c = new Canvas(circleBitmap);
//		c.drawCircle(bm.getWidth()/2, bm.getHeight()/2, bm.getWidth()/2, paint);
//		imageView1.setImageBitmap(circleBitmap);
		
		
		Text1.setText(data.getName());

		if(data.getNomorEvent() == null) Text2.setText(data.getRoleName() + " | ");
		else Text2.setText(data.getRoleName() + " | " + data.getNomorEvent());

		Text3.setText("Cidera : "+data.getCidera());


		Text4.setText("Created Wellness : "+data.getWellnessTime());
		Text5.setText("username : "+data.getUserName());
//		Text5.setText("nilai wellness : "+data.getDummyNilaiWellness());
//		if(data.getDummyNilaiWellness()== null || data.getDummyNilaiWellness().equals("")){
////			TableRow1.setBackgroundColor(Color.parseColor("#d50000"));
//			Text5.setBackgroundColor(Color.parseColor("#d50000"));
//		}else 
		
//		if(Integer.parseInt(data.getValueWellness()) >= 0 && 
//		Integer.parseInt(data.getValueWellness()) <= 60){
//		TableRow1.setBackgroundColor(Color.parseColor("#70d50000"));
//		}else if(Integer.parseInt(data.getValueWellness()) > 60 &&
//		Integer.parseInt(data.getValueWellness()) < 80){
//		TableRow1.setBackgroundColor(Color.parseColor("#70FFEB3B"));
//		}else if(Integer.parseInt(data.getValueWellness()) >= 80 &&
//				Integer.parseInt(data.getValueWellness()) <= 90){
//		TableRow1.setBackgroundColor(Color.parseColor("#7076FF03"));
//		}else if(Integer.parseInt(data.getValueWellness()) > 90 &&
//				Integer.parseInt(data.getValueWellness()) <= 100){
//		TableRow1.setBackgroundColor(Color.parseColor("#701B5E20"));
//		}

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateNow = df.format(new Date());

		if(!data.getWellnessDate().equals(dateNow)){
			imageView1.setImageResource(R.drawable.ic_grey);
		}else{
			if(Integer.parseInt(data.getValueWellness()) > 0 &&
					Integer.parseInt(data.getValueWellness()) <= 59){
				imageView1.setImageResource(R.drawable.ic_red);
			}else if(Integer.parseInt(data.getValueWellness()) >= 60 &&
					Integer.parseInt(data.getValueWellness()) <= 69){
				imageView1.setImageResource(R.drawable.ic_orange);
			}else if(Integer.parseInt(data.getValueWellness()) >= 70 &&
					Integer.parseInt(data.getValueWellness()) <= 79){
				imageView1.setImageResource(R.drawable.ic_yellow);
			}else if(Integer.parseInt(data.getValueWellness()) >= 80 &&
					Integer.parseInt(data.getValueWellness()) <= 89){
				imageView1.setImageResource(R.drawable.ic_green);
			}else if(Integer.parseInt(data.getValueWellness()) >= 90 &&
					Integer.parseInt(data.getValueWellness()) <= 100){
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
			List<M_Member> filteredData = new ArrayList<M_Member>();
			FilterResults result = new FilterResults();
			String filterString = constraint.toString().toLowerCase();
			for (M_Member data : list) {
				if (data.getName().toLowerCase().contains(filterString) ||
						data.getNomorEvent().toLowerCase().contains(filterString) ||
						data.getRoleName().toLowerCase().contains(filterString) ||
						data.getUserName().toLowerCase().contains(filterString)) {
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
			filterd = (List<M_Member>) results.values;
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