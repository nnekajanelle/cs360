package com.app.weightloss;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

import com.app.weightloss.R;

/**
 * A custom circular progress ring with gradient fill and center text.
 *
 * - setMax(int)
 * - setProgress(int)
 * - setCenterText(String)
 * - setStartColor(int)
 * - setEndColor(int)
 * - setStrokeWidth(float)
 *
 * Then call invalidate().
 */
public class GradientCircleView extends View {

    private int max = 100;          // default max
    private int progress = 0;       // current progress
    private float strokeWidth = 20f; // ring thickness in px
    private int startColor = Color.BLUE;
    private int endColor   = Color.RED;
    private int bgColor    = Color.LTGRAY; // unfinished ring color
    private String centerText = "";
    private float centerTextSize = 22f; // scaled in sp

    private Paint bgPaint;
    private Paint progressPaint;
    private Paint textPaint;
    private RectF circleRect;       // the bounding box for the arc

    public GradientCircleView(Context context) {
        super(context);
        init(null);
    }

    public GradientCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GradientCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        if(attrs != null){
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.GradientCircleView);
            max = ta.getInt(R.styleable.GradientCircleView_gcv_max, max);
            progress = ta.getInt(R.styleable.GradientCircleView_gcv_progress, progress);
            startColor = ta.getColor(R.styleable.GradientCircleView_gcv_startColor, startColor);
            endColor   = ta.getColor(R.styleable.GradientCircleView_gcv_endColor, endColor);
            bgColor    = ta.getColor(R.styleable.GradientCircleView_gcv_bgColor, bgColor);
            strokeWidth = ta.getDimension(R.styleable.GradientCircleView_gcv_strokeWidth, strokeWidth);
            centerTextSize = ta.getDimension(R.styleable.GradientCircleView_gcv_centerTextSize, centerTextSize);
            String txt = ta.getString(R.styleable.GradientCircleView_gcv_centerText);
            if(txt != null){
                centerText = txt;
            }
            ta.recycle();
        }

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeCap(Paint.Cap.ROUND);
        bgPaint.setColor(bgColor);
        bgPaint.setStrokeWidth(strokeWidth);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(strokeWidth);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);

        circleRect = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);

        // define the bounding circle
        float padding = strokeWidth / 2f;
        circleRect.set(padding, padding, w - padding, h - padding);

        // build gradient for progress
        SweepGradient sweep = new SweepGradient(w/2f, h/2f, startColor, endColor);
        // we rotate it so that 0% progress starts at top
        Matrix matrix = new Matrix();
        matrix.setRotate(-90, w/2f, h/2f);
        sweep.setLocalMatrix(matrix);
        progressPaint.setShader(sweep);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = Math.min(centerX, centerY) - (strokeWidth / 2f);

        // Draw the background circle (unfinished ring)
        canvas.drawCircle(centerX, centerY, radius, bgPaint);

        // Draw the arc for progress
        float sweepAngle = 360f * progress / max;
        canvas.drawArc(circleRect, -90, sweepAngle, false, progressPaint);

        // Enhance the text appearance
        textPaint.setTextSize(centerTextSize);
        textPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)); // Use stylish font
        textPaint.setShadowLayer(4f, 1f, 1f, Color.DKGRAY); // Add shadow for depth
        textPaint.setColor(Color.LTGRAY); // Make text stand out

        // Center the text
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float textOffset = (textHeight / 2f) - fm.descent;
        canvas.drawText(centerText, centerX, centerY + textOffset, textPaint);
    }

    // ============== Public methods ==============
    public void setMax(int max) {
        this.max = max;
        invalidate();
    }
    public int getMax(){
        return max;
    }

    public void setProgress(int p){
        if(p < 0) p=0;
        if(p > max) p=max;
        this.progress = p;
        invalidate();
    }
    public int getProgress(){
        return progress;
    }

    public void setStartColor(int c){
        this.startColor = c;
        requestLayout(); // triggers onSizeChanged -> rebuild gradient
        invalidate();
    }
    public void setEndColor(int c){
        this.endColor = c;
        requestLayout();
        invalidate();
    }

    public void setBgColor(int c){
        this.bgColor = c;
        bgPaint.setColor(c);
        invalidate();
    }

    public void setStrokeWidth(float w){
        this.strokeWidth = w;
        bgPaint.setStrokeWidth(w);
        progressPaint.setStrokeWidth(w);
        requestLayout();
        invalidate();
    }

    public void setCenterText(String txt){
        this.centerText = txt;
        invalidate();
    }

    public void setCenterTextSize(float sp){
        // convert sp to px if you wish, or store in raw px
        this.centerTextSize = sp * getResources().getDisplayMetrics().scaledDensity;
        invalidate();
    }
}
