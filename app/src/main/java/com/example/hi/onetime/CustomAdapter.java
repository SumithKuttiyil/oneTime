package com.example.hi.onetime;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.hi.onetime.DataModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public  class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener
{

    private LayoutInflater inflater, inflater2;
    Resources resources;

    public CustomAdapter(Context context, ArrayList<DataModel> dataModels)
    {
        super(context, R.layout.text, R.id.list, dataModels);


        inflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        DataModel datamodel = (DataModel) this.getItem(position);

        CheckBox checkBox;
        TextView textView;
        TextView textView2;
        TextView textView4;
        TextView textView5;
        ImageView imageView;


        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.text, parent, false);

            textView = (TextView) convertView.findViewById(R.id.textView);
            checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            textView2 = (TextView) convertView.findViewById(R.id.textView2);
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            textView4 = (TextView) convertView.findViewById(R.id.textView4);
            textView5 = (TextView) convertView.findViewById(R.id.textView5);

            convertView.setTag(new ViewHolder(textView, textView2, textView4, textView5,checkBox, imageView));


            checkBox.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    CheckBox cb = (CheckBox) v;
                    DataModel dataModel = (DataModel) cb.getTag();
                    dataModel.setChecked(cb.isChecked());
                }
            });

        }

        else
        {

            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            checkBox = viewHolder.getCheckBox();
            textView = viewHolder.getTextView();
            textView2 = viewHolder.getTextView2();
            imageView=viewHolder.getImageView();
            textView4 = viewHolder.getTextView4();
            textView5 = viewHolder.getTextView5();
        }


        checkBox.setTag(datamodel);




        // Display planet data
        checkBox.setChecked(datamodel.isChecked());
        textView.setText(datamodel.getName());
        textView2.setText(datamodel.getPhoneNumber());
        MLRoundedImageView mlRoundedImageView=new MLRoundedImageView(getContext());
        imageView.setImageBitmap(mlRoundedImageView.getCroppedBitmap(datamodel.getDisPic(),  130 ));
        textView4.setText(datamodel.getDistance());
        textView5.setText(datamodel.getDuration());
       // imageView.setImageURI(Uri.parse(datamodel.getDisPic()));




        return convertView;
    }

    private class ViewHolder{

        public ViewHolder(){

        }

        public ViewHolder(TextView textView,TextView textView2,TextView textView4,TextView textView5,CheckBox checkBox, ImageView imageView){
            this.checkBox=checkBox;
            this.textView=textView;
            this.textView2=textView2;
            this.imageView=imageView;
            this.textView4=textView4;
            this.textView5=textView5;

        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }

        public TextView getTextView2() {
            return textView2;
        }

        public void setTextView2(TextView textView2) {
            this.textView2 = textView2;
        }
        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        TextView textView;
        CheckBox checkBox;
        TextView textView2;
        ImageView imageView;
        TextView textView4;

        public TextView getTextView4() {
            return textView4;
        }

        public void setTextView4(TextView textView4) {
            this.textView4 = textView4;
        }

        public TextView getTextView5() {
            return textView5;
        }

        public void setTextView5(TextView textView5) {
            this.textView5 = textView5;
        }

        TextView textView5;


    }

    @Override
    public void onClick(View v) {


    }


}

