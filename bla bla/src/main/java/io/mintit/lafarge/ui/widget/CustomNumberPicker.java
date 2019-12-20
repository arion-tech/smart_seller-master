package io.mintit.lafarge.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.mintit.lafarge.R;


public class CustomNumberPicker extends LinearLayout {
    int initialPlace = 1;
    private boolean leap;
    private ImageView imageviewMore;
    private ImageView imageviewLess;
    private EditText textviewPlace;
    private int max = Integer.MAX_VALUE;

    public CustomNumberPicker(Context context) {

        super(context);
        init();
    }


    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public CustomNumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setMax(int max) {
        this.max = max;
        this.initialPlace = 1;
        textviewPlace.setText("1");
    }

    public void init() {
        inflate(getContext(), R.layout.number_picker, this);
        this.imageviewMore = (ImageView) findViewById(R.id.imageview_more);
        this.imageviewLess = (ImageView) findViewById(R.id.imageview_less);
        this.textviewPlace = (EditText) findViewById(R.id.textview_place);
        imageviewLess.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (initialPlace > 1) {
                    initialPlace--;
                    textviewPlace.setText(initialPlace + "");
                }
            }
        });
        imageviewMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (initialPlace < max) {
                    initialPlace++;
                    textviewPlace.setText(initialPlace + "");
                }
            }
        });
    }

    public String getPickedNumber() {
        return textviewPlace.getText().toString();
    }

    public void setPickedNumber(String number) {
        if (number == null) {
            number = "1";
        }
        if (TextUtils.isEmpty(number)) {
            number = "1";
        }

        textviewPlace.setText(number);
        initialPlace = Integer.parseInt(number);
    }


}
