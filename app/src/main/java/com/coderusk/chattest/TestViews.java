package com.coderusk.chattest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class TestViews extends RecyclerView {

    private Context context;
    ArrayList<String> values = null;
    ReceiverRowAdapter adapter = null;
    LinearLayoutManager llm = null;

    private void setup()
    {
        adapter = new ReceiverRowAdapter(context);
        /**********************************/
        setupDragNDrop();
        swipeForAction();
        //swipeToDismiss();
        /**********************************/
        setAdapter(adapter);
    }

    private void swipeForAction() {
        SwipeController swipeController = new SwipeController(context,new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                values.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
            }
            @Override
            public void onLeftClicked(int position) {
                String value = values.get(position);
                Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
            }
        });


        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(this);

        addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private void setupDragNDrop() {
        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(this);
    }

    public void setData(ArrayList<String> receiverRows)
    {
        this.values = receiverRows;
        setup();
    }
    private void commonConstructor(Context context)
    {
        this.context = context;
        llm = new LinearLayoutManager(context);
        setLayoutManager(llm);
        DividerItemDecoration ver = new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
        addItemDecoration(ver);

        addOnScrollListener(new OnScrollListener() {
            /**
             * Callback method to be invoked when RecyclerView's scroll state changes.
             *
             * @param recyclerView The RecyclerView whose scroll state has changed.
             * @param newState     The updated scroll state. One of {@link #SCROLL_STATE_IDLE},
             *                     {@link #SCROLL_STATE_DRAGGING} or {@link #SCROLL_STATE_SETTLING}.
             */
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState)
                {
                    case SCROLL_STATE_IDLE:
                        Log.d("recycler","SCROLL_STATE_IDLE");
                        int first = llm.findFirstVisibleItemPosition();
                        int last = llm.findLastVisibleItemPosition();
                        Log.d("recycler",first+","+last);
                        break;
                    case SCROLL_STATE_SETTLING:
                        Log.d("recycler","SCROLL_STATE_SETTLING");
                        break;
                    case SCROLL_STATE_DRAGGING:
                        Log.d("recycler","SCROLL_STATE_DRAGGING");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int first = llm.findFirstVisibleItemPosition();
                Log.d("recycler","first="+first);
            }
        });





    }

    private void swipeToDismiss() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(context,
                0.5f,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT,
                new SwipeToDeleteCallback.OnDrawCallback() {
            @Override
            public void onDraw(@NonNull Canvas c, View itemView, float dX, float dY, int itemHeight) {
                float left = 0;
                float top = itemView.getTop();
                float bottom = itemView.getBottom();
                float width = itemView.getRight() - itemView.getLeft();
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                c.drawRect(left,top,width,bottom,paint);
            }
        }) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final String item = values.get(position);
                values.remove(position);
                adapter.notifyItemRemoved(position);

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(this);
    }

    public TestViews(@NonNull Context context) {
        super(context);
        commonConstructor(context);
    }

    public TestViews(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        commonConstructor(context);
    }

    public TestViews(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonConstructor(context);
    }
    /****************************************************/
    public class ItemMoveCallback extends ItemTouchHelper.Callback {

        private final ItemTouchHelperContract mAdapter;

        public ItemMoveCallback(ItemTouchHelperContract adapter) {
            mAdapter = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }



        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            return makeMovementFlags(dragFlags, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                      int actionState) {


            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                if (viewHolder instanceof ReceiverRowAdapter.MyViewHolder) {
                    ReceiverRowAdapter.MyViewHolder myViewHolder=
                            (ReceiverRowAdapter.MyViewHolder) viewHolder;
                    mAdapter.onRowSelected(myViewHolder);
                }

            }

            super.onSelectedChanged(viewHolder, actionState);
        }
        @Override
        public void clearView(RecyclerView recyclerView,
                              RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);

            if (viewHolder instanceof ReceiverRowAdapter.MyViewHolder) {
                ReceiverRowAdapter.MyViewHolder myViewHolder=
                        (ReceiverRowAdapter.MyViewHolder) viewHolder;
                mAdapter.onRowClear(myViewHolder);
            }
        }



    }public interface ItemTouchHelperContract {

        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(ReceiverRowAdapter.MyViewHolder myViewHolder);
        void onRowClear(ReceiverRowAdapter.MyViewHolder myViewHolder);

    }
    class ReceiverRowAdapter extends RecyclerView.Adapter<ReceiverRowAdapter.MyViewHolder> implements ItemTouchHelperContract{
        private int selectedIndex = -1;
        private Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.tv_text);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public ReceiverRowAdapter(Context context) {
            this.context = context;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ReceiverRowAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final String value = values.get(position);
            holder.textView.setText(value);
        }

        @Override
        public int getItemCount() {
            if(values ==null)
            {
                return 0;
            }
            return values.size();
        }

        @Override
        public void onRowMoved(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(values, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(values, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onRowSelected(MyViewHolder myViewHolder) {
            myViewHolder.textView.setBackgroundColor(Color.GRAY);

        }

        @Override
        public void onRowClear(MyViewHolder myViewHolder) {
            myViewHolder.textView.setBackgroundColor(Color.WHITE);

        }
    }

}
