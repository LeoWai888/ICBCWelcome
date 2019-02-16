package com.icbc.icbcwelcome.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icbc.icbcwelcome.R;
import com.icbc.icbcwelcome.config.constants;


@SuppressLint("AppCompatCustomView")
public class ShineTextView extends TextView {

    int mViewWidth;                          //TextView的宽度
    private LinearGradient mLinearGradient;     //渲染器
    private Matrix mMatrix;         //图片变换处理器
    private Paint mPaint;           //字体的笔
    int mTranslate=0;       //表示平移的速度


    private TextView borderText = null;///用于描边的TextView

    public ShineTextView(Context context) {
        super(context);
        borderText = new TextView(context);

        init(context);
    }

    public ShineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        borderText = new TextView(context,attrs);
        init(context);
    }

    public ShineTextView(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
        borderText = new TextView(context,attrs,defStyle);
        init(context);
    }

    @Override
    public void setLayoutParams (ViewGroup.LayoutParams params){
        super.setLayoutParams(params);
        borderText.setLayoutParams(params);
    }

    protected void onLayout (boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
        borderText.layout(left, top, right, bottom);
    }

    public void init(Context context){
        TextPaint tp1 = borderText.getPaint();
        tp1.setStrokeWidth(4);                                  //设置描边宽度
        tp1.setStyle(Paint.Style.STROKE);                             //对文字只描边
        borderText.setTextColor(getResources().getColor(R.color.border_text));  //设置描边颜色
        borderText.setGravity(getGravity());
        AssetManager assetManager = context.getAssets();
        Typeface mtypeface=Typeface.createFromAsset(assetManager,constants.FONTTYPEFACE);
        borderText.setTypeface(mtypeface);
    }
    @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            //在 onSizeChanged 方法中获取到宽度，并对各个类进行初始化
            if (mViewWidth == 0) {
                mViewWidth = getMeasuredWidth();

                if (mViewWidth > 0) {
                    //得到 父类 TextView 中写字的那支笔.，注意是继承Textview
                    mPaint = getPaint();
                    //初始化线性渲染器 不了解的请看上面连接
                    mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0,
                          /*  new int[]{Color.parseColor("#FFFFFF00"),
                                    Color.parseColor("#FFFF0000"),
                                    Color.parseColor("#FFFF0000"),
                                    Color.parseColor("#FFFFFF00") }, null, Shader.TileMode.CLAMP);*/

                            new int[]{Color.RED,
                                    Color.GRAY,
                                    Color.YELLOW,
                                    Color.RED,
                                    Color.YELLOW,
                                     }, null, Shader.TileMode.CLAMP);


                    //把渲染器给笔套上
                    mPaint.setShader(mLinearGradient);
                    //初始化 Matrix，Matrix的原意是对一个Bitmap的图片变化进行处理，它本身不能对图像或者View进行变换，但是可以与其他的API结合进行图形和View的变换，比如Canvas
                    mMatrix = new Matrix();

                }
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            //先让父类方法执行，由于上面我们给父类的 Paint 套上了渲染器，所以这里出现的文字已经是彩色的了
            borderText.draw(canvas);
            super.onDraw(canvas);

            if (mMatrix != null) {
                //利用 Matrix 的平移动作实现霓虹灯的效果，这里是每次滚动1/10
                mTranslate += mViewWidth / 10;
                //如果滚出了控件边界，就要拉回来重置开头，这里重置到了屏幕左边的空间
                if (mTranslate >  mViewWidth) {
                    mTranslate = -mViewWidth/2;
                }
                //设置平移距离
                mMatrix.setTranslate(mTranslate, 0);
                //平移效果生效
                mLinearGradient.setLocalMatrix(mMatrix);
                //延迟 100 毫秒再次刷新 View 也就是再次执行本 onDraw 方法
                postInvalidateDelayed(50);

            }
        }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CharSequence tt = borderText.getText();

        //两个TextView上的文字必须一致
        if(tt== null || !tt.equals(this.getText())){
            borderText.setText(getText());
            this.postInvalidate();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        borderText.measure(widthMeasureSpec, heightMeasureSpec);
    }
}
