package com.baliyaan.android.wordincontext;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class ScrollableRecyclerView extends RecyclerView {
    private boolean mSingleScroll = true;
    private VelocityTracker mVelocity = null;
    final private float mEscapeVelocity = 2000.0f;
    final private int mMinDistanceMoved = 20;
    private float mStartX = 0;
    private RecyclerViewPositionHelper _recyclerViewHelper;

    public ScrollableRecyclerView(Context context) {
        super(context);
    }

    public ScrollableRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollableRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent aMotionEvent)
    {
        if(null == _recyclerViewHelper)
        {
            _recyclerViewHelper = RecyclerViewPositionHelper.createHelper(this);
        }

        if (aMotionEvent.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (mSingleScroll && mVelocity == null)
                mVelocity = VelocityTracker.obtain();
            mStartX = aMotionEvent.getX();
            return super.dispatchTouchEvent(aMotionEvent);
        }

        if (aMotionEvent.getAction() == MotionEvent.ACTION_UP)
        {
            if (mVelocity != null)
            {
                if (Math.abs(aMotionEvent.getX() - mStartX) > mMinDistanceMoved)
                {
                    mVelocity.computeCurrentVelocity(1000);
                    float velocity = mVelocity.getYVelocity();

                    if (aMotionEvent.getX() > mStartX)
                    {
                        // always lock
                        if (velocity > mEscapeVelocity)
                            smoothScrollToPosition(_recyclerViewHelper.findLastVisibleItemPosition());
                        else
                        {
                            // lock if over half way there
                            View view = getChildAt(0);
                            if (view != null)
                            {
                                if (view.getRight() >= getWidth() / 2)
                                    smoothScrollToPosition(_recyclerViewHelper.findFirstVisibleItemPosition());
                                else
                                    smoothScrollToPosition(_recyclerViewHelper.findLastVisibleItemPosition());
                            }
                        }
                    }
                    else
                    {
                        if (velocity < -mEscapeVelocity)
                            smoothScrollToPosition(_recyclerViewHelper.findLastVisibleItemPosition());
                        else
                        {
                            // lock if over half way there
                            View view = getChildAt(1);
                            if (view != null)
                            {
                                if (view.getLeft() <= getWidth() / 2)
                                    smoothScrollToPosition(_recyclerViewHelper.findLastVisibleItemPosition());
                                else
                                    smoothScrollToPosition(_recyclerViewHelper.findFirstVisibleItemPosition());
                            }
                        }
                    }
                }
                mVelocity.recycle();
            }
            mVelocity = null;

            if (mSingleScroll)
            {
                if (Math.abs(aMotionEvent.getX() - mStartX) > mMinDistanceMoved)
                    return super.dispatchTouchEvent(aMotionEvent);
            }
            else
                return super.dispatchTouchEvent(aMotionEvent);
        }

        if (mSingleScroll)
        {
            if (mVelocity == null)
            {
                mVelocity = VelocityTracker.obtain();
                mStartX = aMotionEvent.getX();
            }
            mVelocity.addMovement(aMotionEvent);
        }

        return super.dispatchTouchEvent(aMotionEvent);
    }
}

class RecyclerViewPositionHelper {

    final RecyclerView recyclerView;
    final RecyclerView.LayoutManager layoutManager;

    RecyclerViewPositionHelper(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.layoutManager = recyclerView.getLayoutManager();
    }

    public static RecyclerViewPositionHelper createHelper(RecyclerView recyclerView) {
        if (recyclerView == null) {
            throw new NullPointerException("Recycler View is null");
        }
        return new RecyclerViewPositionHelper(recyclerView);
    }

    /**
     * Returns the adapter item count.
     *
     * @return The total number on items in a layout manager
     */
    public int getItemCount() {
        return layoutManager == null ? 0 : layoutManager.getItemCount();
    }

    /**
     * Returns the adapter position of the first visible view. This position does not include
     * adapter changes that were dispatched after the last layout pass.
     *
     * @return The adapter position of the first visible item or {@link RecyclerView#NO_POSITION} if
     * there aren't any visible items.
     */
    public int findFirstVisibleItemPosition() {
        final View child = findOneVisibleChild(0, layoutManager.getChildCount(), false, true);
        return child == null ? RecyclerView.NO_POSITION : recyclerView.getChildAdapterPosition(child);
    }

    /**
     * Returns the adapter position of the first fully visible view. This position does not include
     * adapter changes that were dispatched after the last layout pass.
     *
     * @return The adapter position of the first fully visible item or
     * {@link RecyclerView#NO_POSITION} if there aren't any visible items.
     */
    public int findFirstCompletelyVisibleItemPosition() {
        final View child = findOneVisibleChild(0, layoutManager.getChildCount(), true, false);
        return child == null ? RecyclerView.NO_POSITION : recyclerView.getChildAdapterPosition(child);
    }

    /**
     * Returns the adapter position of the last visible view. This position does not include
     * adapter changes that were dispatched after the last layout pass.
     *
     * @return The adapter position of the last visible view or {@link RecyclerView#NO_POSITION} if
     * there aren't any visible items
     */
    public int findLastVisibleItemPosition() {
        final View child = findOneVisibleChild(layoutManager.getChildCount() - 1, -1, false, true);
        return child == null ? RecyclerView.NO_POSITION : recyclerView.getChildAdapterPosition(child);
    }

    /**
     * Returns the adapter position of the last fully visible view. This position does not include
     * adapter changes that were dispatched after the last layout pass.
     *
     * @return The adapter position of the last fully visible view or
     * {@link RecyclerView#NO_POSITION} if there aren't any visible items.
     */
    public int findLastCompletelyVisibleItemPosition() {
        final View child = findOneVisibleChild(layoutManager.getChildCount() - 1, -1, true, false);
        return child == null ? RecyclerView.NO_POSITION : recyclerView.getChildAdapterPosition(child);
    }

    View findOneVisibleChild(int fromIndex, int toIndex, boolean completelyVisible,
                             boolean acceptPartiallyVisible) {
        OrientationHelper helper;
        if (layoutManager.canScrollVertically()) {
            helper = OrientationHelper.createVerticalHelper(layoutManager);
        } else {
            helper = OrientationHelper.createHorizontalHelper(layoutManager);
        }

        final int start = helper.getStartAfterPadding();
        final int end = helper.getEndAfterPadding();
        final int next = toIndex > fromIndex ? 1 : -1;
        View partiallyVisible = null;
        for (int i = fromIndex; i != toIndex; i += next) {
            final View child = layoutManager.getChildAt(i);
            final int childStart = helper.getDecoratedStart(child);
            final int childEnd = helper.getDecoratedEnd(child);
            if (childStart < end && childEnd > start) {
                if (completelyVisible) {
                    if (childStart >= start && childEnd <= end) {
                        return child;
                    } else if (acceptPartiallyVisible && partiallyVisible == null) {
                        partiallyVisible = child;
                    }
                } else {
                    return child;
                }
            }
        }
        return partiallyVisible;
    }
}