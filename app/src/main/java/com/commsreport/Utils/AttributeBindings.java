package com.commsreport.Utils;


import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.commsreport.Utils.enums.TypeFaceType;


import java.util.List;

/**
 * MIT License
 * <p>
 * Copyright (c) 2017 Oleksiy
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

public class AttributeBindings {
    @BindingAdapter({"bind:typeface"})
    public static void setTypeFace(TextView view, TypeFaceType tft) {
        Log.i("AttributeBindings", "setting typeface:"+tft);
        Typeface typeface = null;
        switch (tft) {
            case NORMAL:
                typeface = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Rajdhani-Bold.ttf");
                break;
            case BOLD:
                typeface = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Caviar_Dreams_Bold.ttf");
                break;
            case ITALIC:
                typeface = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/CaviarDreams_Italic.ttf");
                break;
            case BOLD_ITALIC:
                typeface = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/CaviarDreams_BoldItalic.ttf");
                break;
            default:
                Log.w("AttributeBindings", "unknown typeface passed");
        }
        if (typeface!=null)
            view.setTypeface(typeface);
    }

    /*@BindingAdapter({"bind:srcUrl", "bind:error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        Picasso.with(view.getContext()).load(url).error(error).into(view);
    }

    @BindingAdapter({"bind:list","bind:layoutManager"})
    public static void setList(RecyclerView rv, List dataItems, RecyclerView.LayoutManager layoutManager){
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(new RecyclerViewBindingAdapter(dataItems));
    }*/
}
