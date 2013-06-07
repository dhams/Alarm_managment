package com.medplan.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.medplan.app.R;

public class BgColorAdapter extends ArrayAdapter<String>{
	private LayoutInflater mInflater;
	String _type[];
	String _number[];
	Drawable _images[];
	Context ctx;
	 
    public BgColorAdapter(Context context,int layoutId, String[] num_type, String[] color_type, 
			Drawable[] arr_images) {
    	
         super(context,layoutId,num_type);
         
        mInflater = LayoutInflater.from(context);
         ctx=context;
		_type=color_type;
		_number=num_type;
		_images=arr_images;
    }
 

	@Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
	 public class ViewHolder{
		TextView textNumber;
		TextView textValue;
		ImageView imageSearch;
		
	}

    public View getCustomView(int position, View convertView, ViewGroup parent) {

    	ViewHolder holder;
		View vi= convertView;
    	
            if(convertView==null){
			
			vi= mInflater.inflate(R.layout.spinner_row, null);
			holder = new ViewHolder();
			
			holder.textNumber=(TextView)vi.findViewById(R.id.text_number);
			holder.textValue=(TextView)vi.findViewById(R.id.text_values);
			holder.imageSearch=(ImageView)vi.findViewById(R.id.image);
			vi.setTag(holder);
			
			
		}
		else{
			holder = (ViewHolder) vi.getTag();
			}
            holder.textNumber.setText(_number[position]);
    		holder.textValue.setText(_type[position]);
    		holder.imageSearch.setImageDrawable(_images[position]);
        return vi;
        }
    }
