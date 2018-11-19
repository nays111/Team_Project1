package teampro.mju.yeogin_moreulkkeol;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    List<Item> items;
    int item_layout;

    public RecyclerAdapter(Context context, List<Item> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override // XML 디자인 한 부분 적용.
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, null);
        return new ViewHolder(v);
    }

    @Override // XML 디자인한 부분의 안에 내용 변경
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item = items.get(position);
        // 카드뷰의 각 리스트의 값을 넣는다.
        // 사진, 음식점이름, 카테고리, 등록일, 즐겨찾기, 주소

        //Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_action_name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //holder.image.setBackground(drawable);

            //Glide 라이브러리로 이미지 로딩
            Glide.with(context)
                    .load(item.getImageSrc())
                    .into(holder.image);
        }
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());
        holder.category.setText(item.getCategory());
        holder.address.setText(item.getAddress());

        //holder.bookmark.setImageDrawable(drawable);

        // detailed_page로 이동.
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, detailed_page.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("address", item.getAddress());
                intent.putExtra("image", item.getImageSrc());
                intent.putExtra("category", item.getCategory());
                intent.putExtra("date", item.getDate());

                context.startActivity(intent);
            }
        });
    }

    @Override // 아이템을 측정하는 카운터.
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, category, date, address;
        ImageButton bookmark;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            category = (TextView) itemView.findViewById(R.id.category);
            date = (TextView) itemView.findViewById(R.id.date);
            address = (TextView) itemView.findViewById(R.id.address);
            bookmark = (ImageButton) itemView.findViewById(R.id.bookmark);
            cardview = (CardView) itemView.findViewById(R.id.cardview);

        }
    }
}



// 음식점 아이템 클래스
class Item {
    String imageSrc;
    String title;
    String address;
    String date;
    String category;
    boolean bookmark;

    boolean getBookmark() {
        return this.bookmark;
    }
    void setBookmark(boolean bookmark){ this.bookmark = bookmark;}

    String getAddress() {
        return this.address;
    }
    void setAddress(String address){ this.address =address;}

    String getDate() {
        return this.date;
    }
    void setDate(String date){ this.date =date;}

    String getCategory() {
        return this.category;
    }
    void setCategory(String address){ this.category = category;}

    String getImageSrc() {
        return this.imageSrc;
    }
    void setImageSrc(String imageSrc) {
         this.imageSrc = imageSrc;
    }

    String getTitle() {
        return this.title;
    }
    void setTitle(String title) {
        this.title = title;
    }

    Item(String imageSrc, String title) {
        this.imageSrc = imageSrc;
        this.title = title;

    }

    Item(String imageSrc,
         String title,
         String date,
         String category,
         String address,
         boolean bookmark) {
        this.imageSrc = imageSrc;
        this.title = title;
        this.address = address;
        this.category = category;
        this.date = date;
        this.bookmark = bookmark;

    }
}