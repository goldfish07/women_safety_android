package com.example.womensafety;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.material.checkbox.MaterialCheckBox;

public class ExpandableCardView extends LinearLayout {

    private String title;
    private View innerView;
    private ViewGroup containerView;
    private ImageButton arrowBtn;
    private ImageView headerIcon;
    private TextView textViewTitle;

    private MaterialCheckBox checkBox;
    private int innerViewRes;
    private Drawable iconDrawable;
    private CardView card;
    public static final int DEFAULT_ANIM_DURATION = 350;
    private long animDuration = DEFAULT_ANIM_DURATION;

    private final static int COLLAPSING = 0;
    private final static int EXPANDING = 1;

    private boolean isExpanded = false;
    private boolean isExpanding = false;
    private boolean isCollapsing = false;
    private boolean expandOnClick = false;
    private boolean setRefreshBtn = false;
    private boolean startExpanded = false;

    private int previousHeight = 0;

    private OnExpandedListener listener;

    RelativeLayout header;

    private final OnClickListener defaultClickListener = v -> {
        if (isExpanded())
            collapse();
        else
            expand();
    };

    public ExpandableCardView(Context context) {
        super(context);
    }

    public ExpandableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initAttributes(context, attrs);
        initView(context);
    }

    public ExpandableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttributes(context, attrs);
        initView(context);
    }

    private void initView(@NonNull Context context) {
        //Inflating View
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_expandable_cardview, this);
    }

    private void initAttributes(@NonNull Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableCardView);
        title = typedArray.getString(R.styleable.ExpandableCardView_title);
        iconDrawable = typedArray.getDrawable(R.styleable.ExpandableCardView_icon);
        innerViewRes = typedArray.getResourceId(R.styleable.ExpandableCardView_inner_view, View.NO_ID);
        expandOnClick = typedArray.getBoolean(R.styleable.ExpandableCardView_expandOnClick, false);
        setRefreshBtn = typedArray.getBoolean(R.styleable.ExpandableCardView_setRefreshBtn, false);

        animDuration = typedArray.getInteger(R.styleable.ExpandableCardView_animationDuration, DEFAULT_ANIM_DURATION);
        startExpanded = typedArray.getBoolean(R.styleable.ExpandableCardView_startExpanded, false);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        header = findViewById(R.id.header);
        checkBox = findViewById(R.id.checkBox);
        arrowBtn = findViewById(R.id.arrow);
        textViewTitle = findViewById(R.id.title);
        headerIcon = findViewById(R.id.icon);

        //Setting attributes
        if (!TextUtils.isEmpty(title)) textViewTitle.setText(title);

        if (iconDrawable != null) {
            headerIcon.setVisibility(VISIBLE);
            headerIcon.setBackground(iconDrawable);
        }

        card = findViewById(R.id.card);

        setInnerView(innerViewRes);

        containerView = findViewById(R.id.viewContainer);
        //setElevation(convertDpToPx(getContext(), 4));

        if (startExpanded) {
            //setAnimDuration(0);
            expand();
            setAnimDuration(animDuration);
        }

        if (expandOnClick) {
            card.setOnClickListener(defaultClickListener);
            arrowBtn.setOnClickListener(defaultClickListener);
        }
    }

    public void setChecked(boolean isChecked){
        checkBox.setChecked(isChecked);
    }

    public void expand() {
        final int initialHeight = card.getHeight();

        if (!isMoving()) {
            previousHeight = initialHeight;
        }

        card.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int targetHeight = card.getMeasuredHeight();

        if (targetHeight - initialHeight != 0) {
            animateViews(initialHeight, targetHeight - initialHeight, EXPANDING);
        }
    }

    public void collapse() {
        int initialHeight = card.getMeasuredHeight();
        Log.e("initial height", String.valueOf(initialHeight));
        Log.e("previous height", String.valueOf(previousHeight));
        if(previousHeight==0)
            previousHeight = header.getHeight();

        if (initialHeight - previousHeight != 0) {
            animateViews(initialHeight,
                    initialHeight - previousHeight,
                    COLLAPSING);
        }
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    private void animateViews(final int initialHeight, final int distance, final int animationType) {
        Animation expandAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    //Setting isExpanding/isCollapsing to false
                    isExpanding = false;
                    isCollapsing = false;

                    if (listener != null) {
                        listener.onExpandChanged(card, animationType == EXPANDING);
                    }
                }

                int height_plus = (int) (initialHeight + (distance * interpolatedTime));
                int height_minus = (int) (initialHeight - (distance * interpolatedTime));

//                Log.e("height_plus", String.valueOf(height_plus));
//                Log.e("height_minus", String.valueOf(height_minus));
                int height = header.getHeight();

                if (height_minus != 0) {
                    card.getLayoutParams().height = animationType == EXPANDING
                            ? height_plus : height_minus;
                } else {
                    card.getLayoutParams().height = animationType == EXPANDING
                            ? height_plus : height_minus + height;
                }

                card.findViewById(R.id.viewContainer).requestLayout();

                containerView.getLayoutParams().height = animationType == EXPANDING
                        ? height_plus : height_minus;
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        RotateAnimation arrowAnimation = animationType == EXPANDING ?
                new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f) :
                new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);

        arrowAnimation.setFillAfter(true);


        arrowAnimation.setDuration(animDuration);
        expandAnimation.setDuration(animDuration);

        isExpanding = animationType == EXPANDING;
        isCollapsing = animationType == COLLAPSING;

        startAnimation(expandAnimation);
        Log.e("SO", "Started animation: " + (animationType == EXPANDING ? "Expanding" : "Collapsing"));
        arrowBtn.startAnimation(arrowAnimation);
        isExpanded = animationType == EXPANDING;

    }

    private boolean isExpanding() {
        return isExpanding;
    }

    private boolean isCollapsing() {
        return isCollapsing;
    }

    private boolean isMoving() {
        return isExpanding() || isCollapsing();
    }

    public void setOnExpandedListener(OnExpandedListener listener) {
        this.listener = listener;
    }

    public void removeOnExpandedListener() {
        this.listener = null;
    }

    public void setTitle(String title) {
        if (textViewTitle != null) textViewTitle.setText(title);
    }

    public void setTitle(int resId) {
        if (textViewTitle != null) textViewTitle.setText(resId);
    }

    public void setIcon(Drawable drawable) {
        if (headerIcon != null) {
            headerIcon.setBackground(drawable);
            iconDrawable = drawable;
        }
    }

    public void setIcon(int resId) {
        if (headerIcon != null) {
            iconDrawable = ContextCompat.getDrawable(getContext(), resId);
            headerIcon.setBackground(iconDrawable);
        }
    }

    public void setInnerView(int resId) {
        ViewStub stub = findViewById(R.id.viewStub);
        stub.setLayoutResource(resId);
        innerView = stub.inflate();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        if (arrowBtn != null) {
            arrowBtn.setOnClickListener(l);
        }
        super.setOnClickListener(l);
    }

    public void setOnCheckChangeListener(CompoundButton.OnCheckedChangeListener checkChangeListener) {
        checkBox.setOnCheckedChangeListener(checkChangeListener);
    }

    public long getAnimDuration() {
        return animDuration;
    }

    public void setAnimDuration(long animDuration) {
        this.animDuration = animDuration;
    }

    @NonNull
    public static RotateAnimation getRotateAni() {
        RotateAnimation animation = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setRepeatCount(2);
        animation.setDuration(2000);
        return animation;
    }

    /**
     * Interfaces
     */
    public interface OnExpandedListener {
        void onExpandChanged(View v, boolean isExpanded);
    }
}
