package id.or.ppfi.carousel.main;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import id.or.ppfi.R;

public class CoverFlowAdapter extends BaseAdapter {

    private ArrayList<Game> data;
    private AppCompatActivity activity;

    public CoverFlowAdapter(AppCompatActivity context, ArrayList<Game> objects) {
        this.activity = context;
        this.data = objects;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Game getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_flow_view, null, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.gameImage.setImageResource(data.get(position).getImageSource());
        viewHolder.gameName.setText(data.get(position).getName());

        convertView.setOnClickListener(onClickListener(position));

        return convertView;
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(position == 7){
                    final Dialog dialog = new Dialog(activity);
                    dialog.setTitle(getItem(position).getName());
                  //  dialog.setContentView(R.layout.dialog_kontak);

                    dialog.setCancelable(true);
                    dialog.show();
                }else

                if(position == 9){

                   if(MainActivity.APP_VERSION == 0){
                       //no need to update
                       final Dialog dialog = new Dialog(activity);
                       dialog.setContentView(R.layout.dialog_app_version);
                       dialog.setTitle("App Version " + MainActivity.APP_VERSION_TEXT);
                       dialog.setCancelable(true);
                       dialog.show();
                   }else{
                       //need to update
                       final Dialog dialog = new Dialog(activity);
                       dialog.setContentView(R.layout.dialog_app_update);
                       dialog.setTitle(getItem(position).getName());
                       dialog.setCancelable(true);

                       TextView buttonYes = (TextView) dialog.findViewById(R.id.btn_yes);
                       TextView buttonNo = (TextView) dialog.findViewById(R.id.btn_no);

                       buttonYes.setOnClickListener(new View.OnClickListener() {
                           public void onClick(View view) {
                               MainActivity main = new MainActivity();

                           }
                       });

                       buttonNo.setOnClickListener(new View.OnClickListener() {
                           public void onClick(View view) {
                               dialog.dismiss();
                           }
                       });




                       dialog.show();
                   }

                }

            }
        };
    }


    private static class ViewHolder {
        private TextView gameName;
        private ImageView gameImage;

        public ViewHolder(View v) {
            gameImage = (ImageView) v.findViewById(R.id.image);
            gameName = (TextView) v.findViewById(R.id.name);
        }
    }


}