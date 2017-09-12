package com.example.hi.onetime;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Hi on 30-05-2017.
 */

public class MLRoundedImageView extends AppCompatImageView {

    public MLRoundedImageView(Context context) {
        super(context);
    }

    public MLRoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MLRoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        int w = getWidth(), h = getHeight();

        Bitmap roundBitmap = getCroppedBitmap(new String(), w);
        canvas.drawBitmap(roundBitmap, 0, 0, null);

    }

    public  Bitmap getCroppedBitmap(String disPic, int radius) {

        Bitmap sbmp, bmp;
        Uri myUri = Uri.parse(disPic);
        try {
            bmp = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(myUri));
            if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
                float smallest = Math.min(bmp.getWidth(), bmp.getHeight());
                float factor = smallest / radius;
                sbmp = Bitmap.createScaledBitmap(bmp, (int)(bmp.getWidth() / factor), (int)(bmp.getHeight() / factor), false);
            } else {
                sbmp = bmp;
            }

            Bitmap output = Bitmap.createBitmap(radius, radius,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xffa19774;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, radius, radius);

            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.parseColor("#BAB399"));
            canvas.drawCircle(radius / 2 + 0.7f,
                    radius / 2 + 0.7f, radius / 2 + 0.1f, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(sbmp, rect, rect, paint);
            return output;



        }catch (Exception e){
          return null;
        }


    }



}