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
import com.SMK.Hojapp.GlobalData;
import com.SMK.Hojapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    String TAG = "AdapterContents";
    private DatabaseReference mDatabase;
    Context context;
    ArrayList<Contents> contentsArrayList = new ArrayList<>();
    RequestManager glide;
    private GlobalData globalData;

    public AdapterContents(Context context, ArrayList<Contents> contentsArrayList) {
        this.context = context;
        globalData = (GlobalData) context.getApplicationContext();
        this.contentsArrayList = contentsArrayList;
        glide = Glide.with(context);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public ContentsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed, parent, false);
        final ContentsViewHolder viewHolder = new ContentsViewHolder(view);

        //==========좋아요 버튼==========//
        ImageView likeImage = (ImageView) view.findViewById(R.id.rowContentsLikeImage);
        final TextView likeCountView = (TextView) view.findViewById(R.id.rowContentsLikeCountView );

        likeImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                int position = viewHolder.getAdapterPosition();
                final Contents contents = contentsArrayList.get(position);

                //TODO: 내가 좋아요를 누른 게시물이 아닐 경우!
                boolean am_i_liked = contents.isInLikeUserMap(globalData.getAccount().getUid());
                if(am_i_liked == false) {
                    contents.insertLikeUser(globalData.getAccount().getUid()); // 좋아요 유저 추가
                    likeCountView.setText( contents.getLikeUserCount() + "" ); // 텍스트 표시

                    //업데이트 쿼리 수행
                    updateVarOfContents(contents.getCid(), contents);
                    //addDatabaseLikeUserOfContents(contents.getCid(), globalData.getAccount().getUid()); // 좋아요 유저 DB 추가
                }
            }
        });

        //==========게시물 이동==========//
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //액티비티 전환에 필요한 정보들을 넘겨준다.
                Intent i = new Intent(context, ContentsDetailActivity.class);
                final int position = viewHolder.getAdapterPosition();
                final Contents contents = contentsArrayList.get(position);

                //조회수 1증가
                contents.setHitCount(contents.getHitCount() + 1);
                //업데이트 쿼리 수행
                updateVarOfContents(contents.getCid(), contents);

                i.putExtra("CONTENTS_ID", contents.getCid() );
                i.putExtra("CATEGORY", contents.getCategory() );
                i.putExtra("TITLE", contents.getTitle() );
                i.putExtra("WRITER", contents.getwUid() ); //TODO: 닉네임으로 수정 할 것
                i.putExtra("TIME", getFormedDate(contents.getCreateTime()) );
                i.putExtra("BODY", contents.getBody() );
                i.putExtra("HIT_COUNT", contents.getHitCount() + "" );
                i.putExtra("LIKE_COUNT", contents.getLikeUserCount() + "" );

                context.startActivity(i);
            }
        });

        return viewHolder;
    }

    // 콘텐츠 업데이트 함수
    private void updateVarOfContents(String cid, Contents getContents) {
        // contents 하위 항목인 likeUserMap 와 commentList 에 객체를 추가한다.
        mDatabase.child("contents").child(cid).child("hitCount").setValue( getContents.getHitCount() );
        mDatabase.child("contents").child(cid).child("likeUserMap").setValue( getContents.getLikeUserMap() );
    }

    @Override
    public void onBindViewHolder(ContentsViewHolder holder, int position) {
        final Contents contents = contentsArrayList.get(position);

        holder.tvCategory.setText("#" + contents.getCategory());
        holder.tvTitle.setText(contents.getTitle());
        holder.tvBody.setText(contents.getBody());
        holder.tvWriter.setText(contents.getwName());
        holder.tvHitCount.setText(String.valueOf( contents.getHitCount() ));
        holder.tvLike.setText(String.valueOf( contents.getLikeUserCount() ));
        holder.tvComments.setText(String.valueOf( contents.getCommentsCount() ));
        holder.tvTime.setText( getFormedDate(contents.getCreateTime()) ); // 작성 시간

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

        TextView tvCategory, tvTitle, tvBody, tvHitCount, tvTime, tvLike, tvComments, tvWriter;
        ImageView imageViewPostPic;
        public ContentsViewHolder(View itemView) {
            super(itemView);

            imageViewPostPic = (ImageView) itemView.findViewById(R.id.rowContentsPostImageView);

            tvCategory = (TextView) itemView.findViewById(R.id.rowContentsCategoryView);
            tvTitle = (TextView) itemView.findViewById(R.id.rowContentstitleView);
            tvBody = (TextView) itemView.findViewById(R.id.rowContentsBodyView);
            tvHitCount = (TextView) itemView.findViewById(R.id.rowContentsHitcountView);
            tvTime = (TextView) itemView.findViewById(R.id.rowContentsTimeView);
            tvLike = (TextView) itemView.findViewById(R.id.rowContentsLikeCountView);
            tvComments = (TextView) itemView.findViewById(R.id.rowContentsCommentCountView);
            tvWriter = (TextView) itemView.findViewById(R.id.rowContentsWriterView);
        }
    }

    public String getFormedDate(long rawDate) {
        //currentTimeMillis를 월/일 포맷으로 변환
        //TODO:1년 이내는 년도 표시 하지 않음. 7일 이내는 n일 전으로 표시.

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Date resultDate = new Date(rawDate);
        return sdf.format(resultDate);
    }
}
