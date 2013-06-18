package com.medplan.db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.medpan.util.SoundManager;
import com.medplan.app.R;

public class SoundAdapter extends ArrayAdapter<String>{
	private LayoutInflater mInflater;
	String _type[];
	int _images[];
	Context ctx;
	int pos;
	private SoundManager mSoundManager;
	 
    public SoundAdapter(Context context,int layoutId,  String[] sound_type, 
			int[] arr_images,SoundManager _sound) {
    	
         super(context,layoutId,sound_type);
         
        mInflater = LayoutInflater.from(context);
        ctx=context;
		_type=sound_type;
		_images=arr_images;
		mSoundManager=_sound;
         
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
		TextView textValue;
		ImageView imageSearch;
		
	}

    public View getCustomView(int position, View convertView, ViewGroup parent) {

    	ViewHolder holder;
		View vi= convertView;
    	
            if(convertView==null){
			
			vi= mInflater.inflate(R.layout.sound_row, null);
			holder = new ViewHolder();
			
			holder.textValue=(TextView)vi.findViewById(R.id.text_values);
			holder.imageSearch=(ImageView)vi.findViewById(R.id.image_sound);
			vi.setTag(holder);
		}
		else{
			holder = (ViewHolder) vi.getTag();
			}
    		holder.textValue.setText(_type[position]);
    		holder.imageSearch.setBackgroundResource(_images[position]);
    		
    		holder.imageSearch.setTag(position);
    		pos=position;
    		holder.imageSearch.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					System.out.println("sound clicked");
					
					pos=(Integer) v.getTag();
					
					mSoundManager.stopSound();
					mSoundManager.playSound(pos);
				}
			});
    		holder.textValue.setTag(position);

//    		holder.textValue.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					pos=(Integer) v.getTag();
//					mSoundManager.stopSound();
//				}
//			});
        return vi;
        }
    }
