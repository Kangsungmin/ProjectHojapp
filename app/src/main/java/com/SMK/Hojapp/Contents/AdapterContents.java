package com.SMK.Hojapp.Contents;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.SMK.Hojapp.Contents.ContentsTypes.Contents;
import com.SMK.Hojapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * 뉴스피드에 적용되는 콘텐츠 어댑터
 * ||                           ||
 * ||---------------------------||스
 * ||                           ||크
 * ||           콘텐츠           ||롤
 * ||---------------------------||
 *
 */
public class AdapterContents extends RecyclerView.Adapter<AdapterContents.ContentsViewHolder> {

    Context context;
    ArrayList<Contents> contentsArrayList = new ArrayList<>();
    RequestManager glide;

    public AdapterContents(Context context, ArrayList<Contents> contentsArrayList) {
        this.context = context;
        this.contentsArrayList = contentsArrayList;
        glide = Glide.with(context);
    }

    @Override
    public ContentsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed, parent, false);

        final ContentsViewHolder viewHolder = new ContentsViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //액티비티 전환에 필요한 정보들을 넘겨준다.
                Intent i = new Intent(context, ContentsDetailActivity.class);
                final int position = viewHolder.getAdapterPosition();
                final Contents contents = contentsArrayList.get(position);

                i.putExtra("CATEGORY", contents.getCategory());
                i.putExtra("TITLE", contents.getTitle());
                i.putExtra("WRITER", contents.getwName());
                i.putExtra("TIME", contents.getCreateTime());
                i.putExtra("BODY", contents.getBody());
                context.startActivity(i);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContentsViewHolder holder, int position) {
        final Contents contents = contentsArrayList.get(position);

        holder.tvCategory.setText("#" + contents.getCategory());
        holder.tvTitle.setText(contents.getTitle());
        holder.tvBody.setText(contents.getBody());
        holder.tvWriter.setText(contents.getwName());
        holder.tvHitcount.setText(String.valueOf(contents.getHitCount()));
        holder.tvLike.setText(String.valueOf(contents.getLikeCount()));
        holder.tvComments.setText(String.valueOf(contents.getCommentCount()));

        //currentTimeMillis를 월/일 포맷으로 변환
        //TODO:1년 이내는 년도 표시 하지 않음. 7일 이내는 n일 전으로 표시.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Date resultDate = new Date(contents.getCreateTime());
        holder.tvTime.setText( sdf.format(resultDate) ); // 작성 시간

        if(contents.getBodyPic() == 0) {
            holder.imageViewPostPic.setVisibility(View.GONE);
        }
        else {
            holder.imageViewPostPic.setVisibility(View.VISIBLE);
            glide.load(contents.getBodyPic()).into(holder.imageViewPostPic);
        }
    }

    @Override
    public int getItemCount() {
        return contentsArrayList.size();
    }

    public class ContentsViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategory, tvTitle, tvBody, tvHitcount, tvTime, tvLike, tvComments, tvWriter;
        ImageView imageViewPostPic;
        public ContentsViewHolder(View itemView) {
            super(itemView);

            imageViewPostPic = (ImageView) itemView.findViewById(R.id.rowContentsPostImageView);

            tvCategory = (TextView) itemView.findViewById(R.id.rowContentsCategoryView);
            tvTitle = (TextView) itemView.findViewById(R.id.rowContentstitleView);
            tvBody = (TextView) itemView.findViewById(R.id.rowContentsBodyView);
            tvHitcount = (TextView) itemView.findViewById(R.id.rowContentsHitcountView);
            tvTime = (TextView) itemView.findViewById(R.id.rowContentsTimeView);
            tvLike = (TextView) itemView.findViewById(R.id.rowContentsLikeCountView);
            tvComments = (TextView) itemView.findViewById(R.id.rowContentsCommentCountView);
            tvWriter = (TextView) itemView.findViewById(R.id.rowContentsWriterView);
        }
    }
}
