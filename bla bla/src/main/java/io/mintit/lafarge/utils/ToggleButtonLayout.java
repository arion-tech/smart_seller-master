package io.mintit.lafarge.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.mintit.lafarge.R;

public class ToggleButtonLayout extends RelativeLayout {


    @BindView(R.id.image_error)
    ImageView imageError;
    @BindView(R.id.image_check)
    ImageView imageCheck;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private Context context;
    private AttributeSet attrs;
    private int styleAttr;


    private View view;

    private Unbinder unbinder;

    public ToggleButtonLayout(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ToggleButtonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initView();
    }

    public ToggleButtonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.styleAttr = defStyleAttr;
        initView();
    }

    private void initView() {
        this.view = this;
        inflate(context, R.layout.layout_switch2, this);
        unbinder = ButterKnife.bind(this, view);

    }

    public void updateView(int i) {
        switchView(i, false);
    }

    private void switchView(int i, boolean b) {
        switch (i) {
            case 0:
                /*progressBar*/
                imageCheck.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                imageError.setVisibility(View.GONE);
                break;
            case 1:
                /*success*/
                imageCheck.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                imageError.setVisibility(View.GONE);
                break;
            case 2:
                /*Error*/
                imageCheck.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                imageError.setVisibility(View.VISIBLE);

                break;

        }


    }



}
