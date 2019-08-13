package com.SMK.Hojapp.Contents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.SMK.Hojapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

/*
 * 뉴스피드에 적용되는 콘텐츠 어댑터
 */
public class AdapterContnets extends RecyclerView.Adapter<AdapterContnets.ContentsViewHolder> {

    Context context;
    ArrayList<Contents> contentsArrayList = new ArrayList<>();
    RequestManager glide;

    public AdapterContnets(Context context, ArrayList<Contents> contentsArrayList) {
        this.context = context;
        this.contentsArrayList = contentsArrayList;
        glide = Glide.with(context);
    }

    @Override
    public ContentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed, parent, false);
        ContentsViewHolder viewHolder = new ContentsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContentsViewHolder holder, int position) {
        final Contents contents = contentsArrayList.get(position);

        holder.tvTitle.setText(contents.title);
        holder.tvBody.setText(contents.body);
        holder.tvWriter.setText(contents.wName);
        holder.tvHitcount.setText(String.valueOf(contents.hitCount));
        holder.tvLike.setText(String.valueOf(contents.likeCount));
        holder.tvComments.setText(String.valueOf(contents.commentCount));
        holder.tvTime.setText( Long.toString(contents.createTime) ); // 작성 시간

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

        TextView tvTitle, tvBody, tvHitcount, tvTime, tvLike, tvComments, tvWriter;
        ImageView imageViewPostPic;
        public ContentsViewHolder(View itemView) {
            super(itemView);

            imageViewPostPic = (ImageView) itemView.findViewById(R.id.rowContentsPostImageView);

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
