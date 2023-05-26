package com.snd.app.ui.tree;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space; // 여백 크기

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        // 각 아이템의 여백 설정
        outRect.left = space+10;
        outRect.right = space;
        outRect.top = space;
        outRect.bottom = space;
    }
}
