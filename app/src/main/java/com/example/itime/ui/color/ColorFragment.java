package com.example.itime.ui.color;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.itime.MainActivity;
import com.example.itime.R;

public class ColorFragment extends DialogFragment {

    private ColorViewModel colorViewModel;
    private SeekBar color_RGB;
    private static int rgb = 0;
    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        colorViewModel =
                ViewModelProviders.of(this).get(ColorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_color, container, false);

        color_RGB = (SeekBar) root.findViewById(R.id.RGB);
        textView= (TextView) root.findViewById(R.id.int_RGB);
        textView.setText("选择颜色");

        LinearGradient test = new LinearGradient(0.f, 0.f, 1000.f, 0.0f,
                new int[]{0xFFFF3333,0xFFFFFF33,0xFF33FF33,0xFF33FFFF,
                        0xFF3333FF,0xFFFF33FF,0xFFFF3333},
                null, Shader.TileMode.CLAMP);
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(test);
        color_RGB.setProgressDrawable((Drawable) shape);

        color_RGB.setMax(256*7-1);
        color_RGB.setProgress(0);
        //设置点击事件
      color_RGB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    int r = 0;
                    int g = 0;
                    int b = 0;

                    if(progress < 256){
                        b = progress;
                    } else if(progress < 256*2) {
                        g = progress%256;
                        b = 256 - progress%256;
                    } else if(progress < 256*3) {
                        g = 255;
                        b = progress%256;
                    } else if(progress < 256*4) {
                        r = progress%256;
                        g = 256 - progress%256;
                        b = 256 - progress%256;
                    } else if(progress < 256*5) {
                        r = 255;
                        g = 0;
                        b = progress%256;
                    } else if(progress < 256*6) {
                        r = 255;
                        g = progress%256;
                        b = 256 - progress%256;
                    } else if(progress < 256*7) {
                        r = 255;
                        g = 255;
                        b = progress%256;
                    }
                    rgb=Color.argb(255, r, g, b);
                    ((MainActivity)  getActivity()).changeThemeColor(rgb);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return root;
    }
}
    //将hsl三个值转换为对应的rgb整型值得方法
   /* private int hsl2RGB(double[] hsl)
    {
        double c1o60  = 1.0 / 60.0;
        double c1o255 = 255.0;
        int tr= 0, tg = 0, tb = 0;
        double v1, v2, v3, h1;
        double s = hsl[1], l = hsl[2];
        double h = hsl[0];
        if (s == 0) {
            tr = (int) (c1o255 * l + 0.5);
           tg= (int) (c1o255 * l + 0.5);
            tb = (int) (c1o255 * l + 0.5);
            return Color.rgb(tr,tg,tb);
        } else {

            if (l < 127.5) {
                v2 = c1o255 * l * (255 + s);
            } else {
                v2 = l + s - c1o255 * s * l;
            }

            v1 = 2 * l - v2;
            v3 = v2 - v1;
            h1 = h + 120.0;
            if (h1 >= 360.0)
                h1 -= 360.0;

            if (h1 < 60.0) {
                tr = (int)(v1 + v3 * h1 * c1o60);
            }
            else if (h1 < 180.0) {
                tr = (int)v2;
            }
            else if (h1 < 240.0) {
                tr = (int)(v1 + v3 * (4 - h1 * c1o60));
            }
            else {
                tr = (int)v1;
            }

            h1 = h;
            if (h1 < 60.0) {
                tg = (int)(v1 + v3 * h1 * c1o60);
            }
            else if (h1 < 180.0) {
                tg = (int)v2;
            }
            else if (h1 < 240.0) {
                tg = (int)(v1 + v3 * (4 - h1 * c1o60));
            }
            else {
                tg = (int)v1;
            }

            h1 = h - 120.0;
            if (h1 < 0.0) {
                h1 += 360.0;
            }
            if (h1 < 60.0) {
                tb = (int)(v1 + v3 * h1 * c1o60);
            }
            else if (h1 < 180.0) {
                tb = (int)v2;
            }
            else if (h1 < 240.0) {
                tb = (int)(v1 + v3 * (4 - h1 * c1o60));
            }
            else {
                tb = (int)v1;
            }
        }
        return Color.rgb(tr,tg,tb);
    }*/
