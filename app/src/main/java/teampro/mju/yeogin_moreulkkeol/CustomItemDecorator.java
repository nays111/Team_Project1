package teampro.mju.yeogin_moreulkkeol;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class CustomItemDecorator extends RecyclerView.ItemDecoration {
    int halfSize;

    public CustomItemDecorator(int size){
        halfSize = size/2;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if(parent.getPaddingLeft()!=halfSize){
            parent.setPadding(halfSize,halfSize,halfSize,halfSize);
            parent.setClipToPadding(false);
        }
        outRect.bottom = halfSize;
        outRect.left = halfSize;
        outRect.right =  halfSize;
        outRect.top = halfSize;

    }
}
