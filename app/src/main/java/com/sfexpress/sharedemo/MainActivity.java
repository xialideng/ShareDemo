package com.sfexpress.sharedemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TypeAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
//                throw new RuntimeException();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class TypeAdapter extends RecyclerView.Adapter<TypeViewHolder> {
        private List<MenuType> menuTypeList;

        public TypeAdapter() {
            menuTypeList = new ArrayList<MenuType>();
            menuTypeList.add(new MenuType(getString(R.string.traditinal_linearlayout), R.layout.normal_linearlayout, LayoutActivity.class));
            menuTypeList.add(new MenuType(getString(R.string.percent_framelayout), R.layout.percent_framelayout, LayoutActivity.class));
            menuTypeList.add(new MenuType(getString(R.string.percent_relativelayout), R.layout.percent_relativelayout, LayoutActivity.class));
            menuTypeList.add(new MenuType(getString(R.string.title_activity_chat), 0, ChatActivity.class));
            menuTypeList.add(new MenuType(getString(R.string.title_activity_flex_box), 0, com.google.android.apps.flexbox.MainActivity.class));
        }

        @Override
        public TypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TypeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.type_item_view, parent, false));
        }

        @Override
        public void onBindViewHolder(final TypeViewHolder holder, final int position) {
            MenuType item = getItem(position);
            holder.textView.setText(item.title);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return menuTypeList.size();
        }

        public MenuType getItem(int position) {
            return menuTypeList.get(position);
        }
    }

    private class TypeViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public TypeViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            MenuType item = adapter.getItem(position);
            Intent intent = new Intent(MainActivity.this, item.activityClass);
            intent.putExtra("MenuType", item);
            startActivity(intent);
        }
    };

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        /*
        * RecyclerView的布局方向，默认先赋值
        * 为纵向布局
        * RecyclerView 布局可横向，也可纵向
        * 横向和纵向对应的分割想画法不一样
        * */
        private int mOrientation = LinearLayoutManager.VERTICAL;

        /**
         * item之间分割线的size，默认为1
         */
        private int mItemSize = 1;

        /**
         * 绘制item分割线的画笔，和设置其属性
         * 来绘制个性分割线
         */
        private Paint mPaint;

        /**
         * 构造方法传入布局方向，不可不传
         *
         * @param context
         * @param orientation
         */
        public DividerItemDecoration(Context context, int orientation) {
            this.mOrientation = orientation;
            if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
                throw new IllegalArgumentException("请传入正确的参数");
            }
            mItemSize = (int) TypedValue.applyDimension(mItemSize, TypedValue.COMPLEX_UNIT_DIP, context.getResources().getDisplayMetrics());
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(Color.BLUE);
         /*设置填充*/
            mPaint.setStyle(Paint.Style.FILL);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }

        /**
         * 绘制纵向 item 分割线
         *
         * @param canvas
         * @param parent
         */
        private void drawVertical(Canvas canvas, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
            final int childSize = parent.getChildCount();
            for (int i = 0; i < childSize; i++) {
                final View child = parent.getChildAt(i);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getBottom() + layoutParams.bottomMargin;
                final int bottom = top + mItemSize;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }

        /**
         * 绘制横向 item 分割线
         *
         * @param canvas
         * @param parent
         */
        private void drawHorizontal(Canvas canvas, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
            final int childSize = parent.getChildCount();
            for (int i = 0; i < childSize; i++) {
                final View child = parent.getChildAt(i);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int left = child.getRight() + layoutParams.rightMargin;
                final int right = left + mItemSize;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }

        /**
         * 设置item分割线的size
         *
         * @param outRect
         * @param view
         * @param parent
         * @param state
         */
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                outRect.set(0, 0, 0, mItemSize);
            } else {
                outRect.set(0, 0, mItemSize, 0);
            }
        }
    }
}
