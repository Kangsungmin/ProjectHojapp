package com.SMK.Hojapp.Contents;

import android.content.Context;
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

import java.util.ArrayList;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.CommentViewHolder> {
    String TAG = "AdapterComment";
    Context context;
    ArrayList<Contents> commentArrayList = new ArrayList<>();
    RequestManager glide;

    public AdapterComment(Context context, ArrayList<Contents> commentArrayList){
        this.context = context;
        this.commentArrayList = commentArrayList;
        glide = Glide.with(context);
    }

    @Override
    public AdapterComment.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false);
        final CommentViewHolder viewHolder = new CommentViewHolder(view);

        //==========좋아요 버튼==========//
        return viewHolder;
    }

    @Override
    public void onBindViewHolder( AdapterComment.CommentViewHolder holder, int position) {
        final Contents contents = commentArrayList.get(position);

        holder.tvWriter.setText(contents.getwUid()); // TODO: 작성자 닉네임으로 수정할 것
        holder.tvBody.setText(contents.getBody());
        holder.tvTime.setText(contents.getCreateTime() + "");
        holder.tvLike.setText(contents.getLikeUserCount() + "");
        holder.tvCommentCount.setText(contents.getCommentsCount() + "");

        /*
        if(contents.getBodyPic() == 0) {
            holder.imageViewCommentPic.setVisibility(View.GONE);
        }
        else {
            holder.imageViewCommentPic.setVisibility(View.VISIBLE);
            glide.load(contents.getBodyPic()).into(holder.imageViewCommentPic);
        }*/
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvWriter, tvBody, tvTime, tvLike, tvCommentCount;
        ImageView imageViewCommentPic;

        public CommentViewHolder(View itemView) {
            super(itemView);

            tvWriter = (TextView) itemView.findViewById(R.id.commentWriter);
            tvBody = (TextView) itemView.findViewById(R.id.commentBody);
            tvTime = (TextView) itemView.findViewById(R.id.commentWriteTime);
            tvLike = (TextView) itemView.findViewById(R.id.commentLikeText);
            tvCommentCount = (TextView) itemView.findViewById(R.id.commentOfCommentText);
        }
    }
}
