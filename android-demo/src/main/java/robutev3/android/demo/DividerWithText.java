package robutev3.android.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * @author Nearchos
 * Created: 28-Oct-19
 */
public class DividerWithText extends AppCompatTextView {

    private final float stroke = 2f;
    private final float padding = 8f;

    public DividerWithText(Context context) {
        super(context);
        init();
    }

    public DividerWithText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DividerWithText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }
        setGravity(Gravity.CENTER);
    }

    private final Rect mBounds = new Rect();
    private final Paint mPaint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(stroke);
        mPaint.setColor(getPaint().getColor());
        getPaint().getTextBounds(getText().toString(), 0, getText().length(), mBounds);
        canvas.drawLine(0, getHeight() / 2f, (getWidth() - mBounds.right) / 2f - padding, getHeight() / 2f, mPaint);
        canvas.drawLine(padding + (getWidth() + mBounds.right) / 2f, getHeight() / 2f, getWidth(), getHeight() / 2f, mPaint);
    }
}
