package lib.android.timingbar.com.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;


/**
 * 圆角按钮
 * Created by Wx on 2017/5/26.
 */
public class ShapeTextView extends AppCompatTextView {
    private Context mContext;
    private int strokeWidth;
    private int strokeColor;
    private int selectStrokeColor;
    private float roundRadius;
    private int defaultColor;
    private int pressedColor;
    private boolean isRipple;
    private float parameter;
    private float topLeftRadius;
    private float topRightRadius;
    private float bottomLeftRadius;
    private float bottomRightRadius;

    public ShapeTextView(Context context) {
        this(context, null);
    }

    public ShapeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setGravity(Gravity.CENTER);
        initAttr(attrs);
        Drawable colorDrawable = getColorDrawable();
        if (colorDrawable != null) {
            this.setBackgroundDrawable(colorDrawable);
        }
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ShapeButton);
        strokeWidth = typedArray.getDimensionPixelOffset(R.styleable.ShapeButton_strokeWidth, 0);
        strokeColor = typedArray.getColor(R.styleable.ShapeButton_strokeColor, 0);
        selectStrokeColor = typedArray.getColor(R.styleable.ShapeButton_selectStrokeColor, 0);
        pressedColor = typedArray.getColor(R.styleable.ShapeButton_pressedColor, 0);
        defaultColor = typedArray.getColor(R.styleable.ShapeButton_defaultColor, 0);
        isRipple = typedArray.getBoolean(R.styleable.ShapeButton_isRipple, true);
        parameter = typedArray.getFloat(R.styleable.ShapeButton_parameter, 0.2f);
        roundRadius = typedArray.getDimensionPixelOffset(R.styleable.ShapeButton_roundRadius, 0);
        topLeftRadius = typedArray.getDimensionPixelOffset(R.styleable.ShapeButton_topLeftRadius, 0);
        topRightRadius = typedArray.getDimensionPixelOffset(R.styleable.ShapeButton_topRightRadius, 0);
        bottomLeftRadius = typedArray.getDimensionPixelOffset(R.styleable.ShapeButton_bottomLeftRadius, 0);
        bottomRightRadius = typedArray.getDimensionPixelOffset(R.styleable.ShapeButton_bottomRightRadius, 0);
        typedArray.recycle();
    }

    private Drawable getColorDrawable() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && isRipple) {
            if (pressedColor == 0) {
                pressedColor = getLightOrDarken(defaultColor, 0.2D);
            }
            ColorStateList pressedColorDw = ColorStateList.valueOf(pressedColor);
            return new RippleDrawable(pressedColorDw, getGradientDrawable(0, 0), getShape());
        } else {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, getGradientDrawable(pressedColor, parameter));
            stateListDrawable.addState(new int[]{android.R.attr.state_focused}, getGradientDrawable(pressedColor, parameter * 2));
            stateListDrawable.addState(new int[]{}, getGradientDrawable(0, 0));
            return stateListDrawable;
        }
    }

    private Drawable getGradientDrawable(int color, double parameter) {
        GradientDrawable dw = new GradientDrawable();
        if (parameter == 0) {
            if (strokeWidth != 0) {
                dw.setStroke(strokeWidth, strokeColor == 0 ? Color.TRANSPARENT : strokeColor);
            }
            if (defaultColor != 0) {
                dw.setColor(defaultColor);
            }
        } else {
            if (strokeWidth != 0) {
                dw.setStroke(strokeWidth, selectStrokeColor == 0 ? strokeColor == 0 ? Color.TRANSPARENT : strokeColor : selectStrokeColor);
            }
            if (color == 0) {
                dw.setColor(getLightOrDarken(defaultColor, parameter));
            } else {
                dw.setColor(color);
            }
        }
        if (roundRadius != 0) {
            dw.setCornerRadius(roundRadius);
        } else if (topLeftRadius != 0 || topRightRadius != 0 || bottomRightRadius != 0 || bottomLeftRadius != 0) {
            dw.setCornerRadii(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius});
        }
        return dw;
    }

    private Drawable getShape() {
        ShapeDrawable mask = new ShapeDrawable(new RectShape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {
                final float width = this.getWidth();
                final float height = this.getHeight();
                RectF rectF = new RectF(0, 0, width, height);
                if (roundRadius != 0) {
                    canvas.drawRoundRect(rectF, roundRadius, roundRadius, paint);
                } else if (topLeftRadius != 0 || topRightRadius != 0 || bottomRightRadius != 0 || bottomLeftRadius != 0) {
                    Path path = new Path();
                    path.addRoundRect(rectF, new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius}, Path.Direction.CW);
                    canvas.drawPath(path, paint);
                } else {
                    canvas.drawRect(rectF, paint);
                }
            }
        });
        return mask;
    }

    //单色变暗
    private int darkenColor(int color, double parameter) {
        return (int) Math.max(color - color * parameter, 0);
    }

    //颜色变暗
    private int darkenColors(int color, double parameter) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = darkenColor(red, parameter);
        green = darkenColor(green, parameter);
        blue = darkenColor(blue, parameter);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, red, green, blue);
    }

    //单色变亮
    private int lightColor(int color, double parameter) {
        return (int) Math.min(color + color * parameter, 255);
    }

    //颜色变亮
    private int lightColors(int color, double parameter) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = lightColor(red, parameter);
        green = lightColor(green, parameter);
        blue = lightColor(blue, parameter);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, red, green, blue);
    }

    //判断颜色 变亮或变暗
    private boolean isLightOrDarken(int color, double parameter) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return red + (red * parameter) < 255 && green + (green * parameter) < 255 && blue + (blue * parameter) < 255;
    }

    //获取变亮或变暗颜色
    private int getLightOrDarken(int color, double parameter) {
        parameter = parameter < 0 ? 0 : parameter > 1 ? 1 : parameter;
        if (isLightOrDarken(color, parameter)) {
            return lightColors(color, parameter);
        } else {
            return darkenColors(color, parameter);
        }
    }


}
