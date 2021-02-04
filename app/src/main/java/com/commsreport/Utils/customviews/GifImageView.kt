package com.wecompli.utils.customviews

import android.content.Context
import android.graphics.Movie
import android.util.AttributeSet
import android.view.View
import java.io.InputStream
import android.graphics.Canvas
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import java.io.FileNotFoundException


class GifImageView:View {
     var mInputStream: InputStream?=null
     var mMovie: Movie?=null
     var  mWidth:Int?=null
     var mHeight:Int?=null
    var  mStart:Long?=0L
    var  mContext:Context?=null

    constructor(context: Context) : super(context) {
        this.mContext = context
    }
     constructor(context: Context, attrs: AttributeSet) : super(context,attrs,0) {
        this.mContext=context

    }


     constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {

        this.mContext = context
        if (attrs.getAttributeName(1) == "background") {
            val id = Integer.parseInt(attrs.getAttributeValue(1).substring(1))
            setGifImageResource(id)
        }
    }

    private fun init() {
        isFocusable = true
        mMovie = Movie.decodeStream(mInputStream)
        mWidth = mMovie!!.width()
        mHeight = mMovie!!.height()

        requestLayout()
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mWidth!!, mHeight!!)
    }

    override fun onDraw(canvas: Canvas) {

        val now = SystemClock.uptimeMillis()
        if (mStart === 0L) {
            mStart = now
        }

        if (mMovie != null) {

            var duration = mMovie!!.duration()
            if (duration == 0) {
                duration = 1000
            }

            val relTime = ((now - mStart!!) % duration).toInt()

            mMovie!!.setTime(relTime)

            mMovie!!.draw(canvas, 0F, 0F)
            invalidate()
        }
    }

    fun setGifImageResource(id: Int) {
        mInputStream = mContext!!.getResources().openRawResource(id)
        init()
    }

    fun setGifImageUri(uri: Uri) {
        try {
            mInputStream = mContext!!.getContentResolver().openInputStream(uri)
            init()
        } catch (e: FileNotFoundException) {
            Log.e("GIfImageView", "File not found")
        }

    }

}
