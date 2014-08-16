package com.example.SegmentedGroupCustom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class SegmentedGroup extends RadioGroup {

    enum RadioButtonType {
        Left,
        Middle,
        Right
    }

    // =================  one piece =================

    private int oneDP;

    // =================  params =================

    private int mainColor;

    private int subColor;

    private float cornerRadius;

    private int strokeWidth;

    // =================  some values =================

    private float[] leftRbRadiusArray;

    private float[] rightRbRadiusArray;

    // =================  constructors =================

    public SegmentedGroup(Context context, AttributeSet attrs) {

        super(context, attrs);
        init(attrs);
    }

    // =================  override =================

    //  it means the all radioButtons instance have been created.
    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
        beautifyRadioGroup();
    }

    // =================  private =================

    private void init(AttributeSet attrs) {

        Resources resources = getResources();
        oneDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, resources.getDisplayMetrics());

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SegmentedGroup);

        int temp = typedArray.getColor(R.styleable.SegmentedGroup_mainColor, 0);
        mainColor = temp == 0 ? resources.getColor(android.R.color.holo_blue_light) : temp;

        temp = typedArray.getColor(R.styleable.SegmentedGroup_subColor, 0);
        subColor = temp == 0 ? resources.getColor(android.R.color.white) : temp;

        float temp2 = typedArray.getFloat(R.styleable.SegmentedGroup_cornerRadius, 5);
        cornerRadius = temp2 * oneDP;

        if (getOrientation() == LinearLayout.HORIZONTAL) {
            leftRbRadiusArray = new float[]{cornerRadius, cornerRadius, 0f, 0f, 0f, 0f, cornerRadius, cornerRadius};
            rightRbRadiusArray = new float[]{0f, 0f, cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0f, 0f};
        } else {
            leftRbRadiusArray = new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0f, 0f, 0f, 0f};
            rightRbRadiusArray = new float[]{0f, 0f, 0f, 0f, cornerRadius, cornerRadius, cornerRadius, cornerRadius};
        }

        strokeWidth = typedArray.getInt(R.styleable.SegmentedGroup_strokeWidth, 2);

        typedArray.recycle();
    }

    private void beautifyRadioGroup() {

        int count = getChildCount();
        if (count == 1) {
            Log.e("hehe", "You should use button instead of this");
            return;
        }

        for (int i = 0; i < count - 1; i++) {
            View child = getChildAt(i);
            LayoutParams initParams = (LayoutParams) child.getLayoutParams();
            if (getOrientation() == LinearLayout.HORIZONTAL) {
                initParams.setMargins(0, 0, -strokeWidth, 0);     // smart
            } else {
                initParams.setMargins(0, 0, 0, -strokeWidth);
            }
            if (i == 0) {
                addSelectEffect(child, RadioButtonType.Left);
            } else {
                addSelectEffect(child, RadioButtonType.Middle);
            }
        }

        addSelectEffect(getChildAt(count - 1), RadioButtonType.Right);
    }

    private void addSelectEffect(View view, RadioButtonType radioButtonType) {

        RadioButton radioButton = (RadioButton) view;
        addTextSelectEffect(radioButton);
        addBackgroundSelectEffect(radioButton, radioButtonType);
    }

    private void addTextSelectEffect(RadioButton radioButton) {

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{{-android.R.attr.state_checked}, {android.R.attr.state_checked}},
                new int[]{mainColor, subColor}
        );
        radioButton.setTextColor(colorStateList);
    }

    private void addBackgroundSelectEffect(RadioButton radioButton, final RadioButtonType radioButtonType) {

        GradientDrawable checkedDrawable = new GradientDrawable();
        checkedDrawable.setColor(mainColor);

        GradientDrawable uncheckedDrawable = new GradientDrawable();
        uncheckedDrawable.setColor(subColor);
        uncheckedDrawable.setStroke(strokeWidth, mainColor);

        switch (radioButtonType) {
            case Left:
                checkedDrawable.setCornerRadii(leftRbRadiusArray);
                uncheckedDrawable.setCornerRadii(leftRbRadiusArray);
                break;
            case Right:
                checkedDrawable.setCornerRadii(rightRbRadiusArray);
                uncheckedDrawable.setCornerRadii(rightRbRadiusArray);
                break;
        }

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, checkedDrawable);
        stateListDrawable.addState(new int[]{-android.R.attr.state_checked}, uncheckedDrawable);
        radioButton.setBackground(stateListDrawable);
    }

}
