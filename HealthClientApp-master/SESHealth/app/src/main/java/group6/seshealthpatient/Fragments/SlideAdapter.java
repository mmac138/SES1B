package group6.seshealthpatient.Activities;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.ViewGroup;

import group6.seshealthpatient.R;

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;

    public int[] list_images = {
            R.drawable.male_body,
            R.drawable.female_body
    };

    public String[] list_title = {
        "Weight",
        "Height"
    };

    public String[] list_value={
        "Value 1",
        "Value 2"
    };

    public SlideAdapter (Context context) {
        this.context = context;
    }

    @Override
    public int getCount(){
        return list_title.length;
    }

    @Override
    public boolean isViewFromObject(View view,Object object){
        return(view==(ConstraintLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container,int position){
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide, container, false);
        ConstraintLayout layoutslide = (ConstraintLayout) view.findViewById(R.id.slideLayout);
        ImageView image = (ImageView) view.findViewById(R.id.info_bodyIV);
        TextView measure = (TextView) view.findViewById(R.id.info_measureTV);
        TextView value =  (TextView) view.findViewById(R.id.info_valueTV);
        image.setImageResource(list_images[position]);
        measure.setText(list_title[position]);
        value.setText(list_value[position]);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

}