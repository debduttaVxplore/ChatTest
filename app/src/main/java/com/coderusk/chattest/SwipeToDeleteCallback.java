package com.coderusk.chattest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

abstract public class SwipeToDeleteCallback extends ItemTouchHelper.Callback {

    private OnDrawCallback drawCallBack = null;
    Context mContext;
    private Paint mClearPaint;
    private float threshold = 0.7f;
    private int movementFlags = 0;


    public interface OnDrawCallback
    {
        void onDraw(@NonNull Canvas c,
                    View itemView,
                    float dX,
                    float dY,
                    int itemHeight);
    }

    SwipeToDeleteCallback(Context context,float threshold,int movementFlags, OnDrawCallback drawCallback) {
        this.movementFlags = movementFlags;
        this.threshold = threshold;
        this.drawCallBack = drawCallback;
        mContext = context;
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, movementFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c,
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX,
                            float dY,
                            int actionState,
                            boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        onDrawBack(c,itemView,dX,dY,itemHeight);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


    }

    protected void onDrawBack(@NonNull Canvas c,
                              View itemView,
                              float dX,
                              float dY,
                              int itemHeight)
    {
        if(drawCallBack!=null)
        {
            drawCallBack.onDraw(c,itemView,dX,dY,itemHeight);
        }
    }

    private void clearCanvas(Canvas c,
                             Float left,
                             Float top,
                             Float right,
                             Float bottom) {
        c.drawRect(left, top, right, bottom, mClearPaint);

    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return threshold;
    }
}