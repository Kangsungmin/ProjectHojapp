package com.SMK.Hojapp.Contents;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.SMK.Hojapp.Contents.ContentsTypes.Contents;
import com.SMK.Hojapp.Contents.ContentsTypes.ViewType;
import com.SMK.Hojapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

public class AdapterDetailedContents extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    String TAG = "AdapterDetailedContents";
    Context context;
    ArrayList<Contents> detailedContentsArrayList = null;
    RequestManager glide;

    public AdapterDetailedContents(Context context, ArrayList<Contents> commentArrayList){
        this.context = context;
        this.detailedContentsArrayList = commentArrayList;
        glide = Glide.with(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(viewType == ViewType.ROW_CONTENTS_DETAIL){
            view = inflater.inflate(R.layout.row_feed_detail, parent, false);
            return new ContentsDetailViewHolder(view);
        }
        else if(viewType == ViewType.COMMENT){
            view = inflater.inflate(R.layout.row_comment, parent, false);
            return new CommentViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ContentsDetailViewHolder){
            // 위젯 데이터 세팅
            ((ContentsDetailViewHolder) holder).tvCategory.setText(detailedContentsArrayList.get(position).getCategory());
            ((ContentsDetailViewHolder) holder).tvBody.setText(detailedContentsArrayList.get(position).getBody());
        }
        else if(holder instanceof CommentViewHolder) {
            // 위젯 데이터 세팅
            ((CommentViewHolder) holder).tvBody.setText(detailedContentsArrayList.get(position).getBody());
        }
    }

    @Override
    public int getItemCount() {
        return detailedContentsArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return detailedContentsArrayList.get(position).getViewType();
    }

    public class ContentsDetailViewHolder extends RecyclerView.ViewHolder {
        // 콘텐츠 위젯
        TextView tvCategory;
        TextView tvTitle;
        TextView tvBody;
        TextView tvWriter;
        TextView tvHitCount;
        TextView tvLike;
        TextView tvComments;
        TextView tvTime;

        public ContentsDetailViewHolder(View itemView) {
            super(itemView);

            tvCategory = itemView.findViewById(R.id.detailedContentsCategoryView);
            tvTitle = itemView.findViewById(R.id.detailedContentsTitleView);
            tvBody = itemView.findViewById(R.id.contentsTextView);
            tvWriter = itemView.findViewById(R.id.detailedContentsWriterView);
            tvHitCount = itemView.findViewById(R.id.detailedContentsHitcountView);
            tvLike = itemView.findViewById(R.id.detailedContentsLikeCountView);
            tvComments = itemView.findViewById(R.id.detailedContentsCommentCountView);
            tvTime = itemView.findViewById(R.id.detailedContentsTimeView);
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        // 댓클 위젯
        TextView tvWriter;
        TextView tvBody;
        TextView tvTime;
        TextView tvLike;
        TextView tvCommentCount;
        ImageView imageViewCommentPic;

        public CommentViewHolder(View itemView) {
            super(itemView);

            tvWriter = itemView.findViewById(R.id.commentWriter);
            tvBody = itemView.findViewById(R.id.commentBody);
            tvTime = itemView.findViewById(R.id.commentWriteTime);
            tvLike = itemView.findViewById(R.id.commentLikeText);
            tvCommentCount = itemView.findViewById(R.id.commentOfCommentCountText);

        }
    }
}