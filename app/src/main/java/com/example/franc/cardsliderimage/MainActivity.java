package com.example.franc.cardsliderimage;


import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.franc.cardsliderimage.Adapter.ViewHolder;
import com.example.franc.cardsliderimage.Model.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference post;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TextSwitcher txtSwitcher;
    private int currentPosition;
    private static int size;
    private int overallXScroll = 0;
    private String count[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtSwitcher = findViewById(R.id.txtSwitcher);
        database = FirebaseDatabase.getInstance();
        post = database.getReference("Posts");
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        post.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                size = (int) dataSnapshot.getChildrenCount();
                count = new String[size];
                for (int i = 0; i < size; i++) {
                    count[i] = (i + 1)+"";
                }

                txtSwitcher.setFactory(new TextViewFactory(R.style.textSwitcher, true));
                txtSwitcher.setCurrentText(count[0]+"/"+size);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        loadData();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                overallXScroll = overallXScroll + dx;


                int pos = overallXScroll / (getWithScreen(MainActivity.this) - 40);
                if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
                    return;
                }
                onActiveCardChange(pos);
            }
        });




    }

    public static int getWithScreen(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    private void onActiveCardChange(int pos) {
        int animH[] = new int[]{R.anim.slide_in_right, R.anim.slide_out_left};


        final boolean left2right = pos < currentPosition;

        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;
        }

        txtSwitcher.setInAnimation(MainActivity.this, animH[0]);
        txtSwitcher.setOutAnimation(MainActivity.this, animH[1]);
        txtSwitcher.setText(count[pos % size] + "/" + size);

        currentPosition = pos;

    }

    private void loadData() {
        FirebaseRecyclerAdapter<Post, ViewHolder> adapter = new FirebaseRecyclerAdapter<Post, ViewHolder>(
                Post.class,
                R.layout.item,
                ViewHolder.class,
                post
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Post model, int position) {
                viewHolder.txtName.setText(model.getName());
                viewHolder.txtStatus.setText(model.getStatus());
                viewHolder.txtStatusType.setText(model.getStatus_type());
                viewHolder.txtEmail.setText(model.getGmail());
                Picasso.with(getApplicationContext()).load(model.getImage()).into(viewHolder.image);

            }
        };
        recyclerView.setAdapter(adapter);
    }

    private class TextViewFactory implements ViewSwitcher.ViewFactory {

        @StyleRes
        final int styleId;
        final boolean center;

        TextViewFactory(@StyleRes int styleId, boolean center) {
            this.styleId = styleId;
            this.center = center;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View makeView() {
            final TextView textView = new TextView(MainActivity.this);

            if (center) {
                textView.setGravity(Gravity.CENTER);
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(MainActivity.this, styleId);
            } else {
                textView.setTextAppearance(styleId);
            }

            return textView;
        }

    }
}
