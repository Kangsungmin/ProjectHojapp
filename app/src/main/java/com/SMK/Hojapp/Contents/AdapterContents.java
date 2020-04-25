package com.SMK.Hojapp.Contents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.SMK.Hojapp.Contents.ContentsTypes.Contents;
import com.SMK.Hojapp.Contents.ContentsTypes.ViewType;
import com.SMK.Hojapp.GlobalData;
import com.SMK.Hojapp.R;
import com.SMK.Hojapp.TimeManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

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
public class AdapterContents extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    String TAG = "AdapterContents";
    private DatabaseReference mDatabase;
    Context context;
    ArrayList<Contents> contentsArrayList = new ArrayList<>();
    RequestManager glide;
    private GlobalData globalData;

    private int screenWidth;

    public AdapterContents(Context context, ArrayList<Contents> contentsArrayList) {
        this.context = context;
        globalData = (GlobalData) context.getApplicationContext();
        this.contentsArrayList = contentsArrayList;
        glide = Glide.with(context);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view;
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*
        if(viewType == ViewType.ROW_TITLE) // 배너 타이틀 현재 비활성화 되어있다.
        {
            view = inflater.inflate(R.layout.row_store_title, parent, false);
            final TitleViewHolder viewHolder = new TitleViewHolder(view);

            ImageView bannerImage = (ImageView) view.findViewById(R.id.storeTitleImageView);
            // 배달 목록 리스트 액티비티로 이동
            bannerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DeliveryDetailedActivity.class);
                    int position = viewHolder.getAdapterPosition();
                    Contents contents = contentsArrayList.get(position);
                    i.putExtra("TITLE", contents.getTitle() );
                    context.startActivity(i);
                }
            });
            return viewHolder;
        }
        */

        // 라이더 프로필 미리보기 어댑
        if(viewType == ViewType.ROW_PREVIEW_RIDER)
        {
            view = inflater.inflate(R.layout.row_preview_rider, parent, false);
            final RiderPreViewHolder viewHolder = new RiderPreViewHolder(view);

            //ImageView bannerImage = (ImageView) view.findViewById(R.id.riderProfileImageView);
            // 라이더 상세페이지로 이동.
            return viewHolder;
        }
        else if(viewType == ViewType.ROW_PREVIEW_DELIVERY)
        {
            //view = inflater.inflate(R.layout.row_preview_delivery, parent, false); not use.
            view = inflater.inflate(R.layout.row_preview_rider, parent, false);
            //final DeliveryPreViewHolder viewHolder = new DeliveryPreViewHolder(view);
            RiderPreViewHolder viewHolder = new RiderPreViewHolder(view);
            //ImageView previewImage = (ImageView) view.findViewById(R.id.deliveryTitleImageView);
            return viewHolder;
        }
        else if(viewType == ViewType.ROW_CONTENTS_PREVIEW)
        {
            view = inflater.inflate(R.layout.row_feed, parent, false);
            final ContentsViewHolder viewHolder = new ContentsViewHolder(view);
            //==========좋아요 버튼==========//
            ImageView likeImage = (ImageView) view.findViewById(R.id.rowContentsLikeImage);
            final TextView likeCountView = (TextView) view.findViewById(R.id.rowContentsLikeCountView);

            likeImage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // your code here
                    int position = viewHolder.getAdapterPosition();
                    Contents contents = contentsArrayList.get(position);

                    if(contents != null)
                    {
                        boolean am_i_liked = contents.isInLikeUserMap(globalData.getAccount().getUid());
                        if(am_i_liked == false) {
                            contents.insertLikeUser(globalData.getAccount().getUid()); // 좋아요 유저 추가
                            likeCountView.setText( contents.getLikeUserCount() + "" ); // 텍스트 표시

                            //업데이트 쿼리 수행
                            updateVarOfContents(contents.getCid(), contents);
                            //addDatabaseLikeUserOfContents(contents.getCid(), globalData.getAccount().getUid()); // 좋아요 유저 DB 추가
                        }
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
                    Contents contents = contentsArrayList.get(position);
                    if(contents != null)
                    {
                        //조회수 1증가
                        contents.setHitCount(contents.getHitCount() + 1);
                        //업데이트 쿼리 수행
                        updateVarOfContents(contents.getCid(), contents);
                    }

                    i.putExtra("CONTENTS_ID", contents.getCid() );
                    i.putExtra("CATEGORY", contents.getCategory() );
                    i.putExtra("TITLE", contents.getTitle() );
                    i.putExtra("WRITER", contents.getwUid() ); //TODO: 닉네임으로 수정 할 것
                    i.putExtra("WRITER_UID", contents.getwUid() );
                    i.putExtra("TIME", contents.getCreateTime() + "");
                    i.putExtra("BODY", contents.getBody() );
                    i.putExtra("HIT_COUNT", contents.getHitCount() + "" );

                    context.startActivity(i);
                }
            });

            return viewHolder;
        }


        Log.w(TAG,"view holder is null" );
        return  null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof AdapterContents.TitleViewHolder) { // 타이틀일때.
            Contents getContents =  contentsArrayList.get(position);
            //이미지를 적용한다.
            //이미지 세팅

            Picasso.get().load( getContents.getTitleUrl() )
                    .centerCrop()
                    // 현재 화면 가로 사이즈를 기준으로 이미지 높이를 자동으로 맞춘다.
                    .resize(screenWidth,0)
                    .into(((AdapterContents.TitleViewHolder) holder).titleImage);
            ((AdapterContents.TitleViewHolder) holder).titleImage.requestLayout();

        }
        else if(holder instanceof  AdapterContents.RiderPreViewHolder)
        {
            final Contents getContents = contentsArrayList.get(position);
            ((RiderPreViewHolder)holder).profileTextState.setText(getContents.getState());
            ((RiderPreViewHolder)holder).profileTextTitle.setText(getContents.getTitle());
            ((RiderPreViewHolder)holder).profileTextSubTitle.setText(getContents.getSubTitle());
            ((RiderPreViewHolder)holder).profileTextBody.setText(getContents.getBody());
            Log.w(TAG, "body text::" + getContents.getBody());

            Log.w(TAG, "PROFILE URL::" + getContents.getProfileUrl());
            if(getContents.getProfileUrl() != null && getContents.getProfileUrl().equals("-") == false)
            {
                Picasso.get().load( getContents.getProfileUrl() )
                        .centerCrop()
                        // 현재 화면 가로 사이즈를 기준으로 이미지 높이를 자동으로 맞춘다.
                        .resize(100,100)
                        .into(((RiderPreViewHolder) holder).profileImage);
                ((AdapterContents.RiderPreViewHolder) holder).profileImage.requestLayout();
            }

        }
        else if(holder instanceof AdapterContents.ContentsViewHolder) // 일반 뷰홀더일 때
        {
            final Contents contents = contentsArrayList.get(position);

            ((ContentsViewHolder)holder).tvCategory.setText("#" + contents.getCategory());
            ((ContentsViewHolder)holder).tvTitle.setText(contents.getTitle());
            ((ContentsViewHolder)holder).tvBody.setText(contents.getBody());
            ((ContentsViewHolder)holder).tvWriter.setText(contents.getwName());
            ((ContentsViewHolder)holder).tvHitCount.setText(String.valueOf( contents.getHitCount() ));
            ((ContentsViewHolder)holder).tvLike.setText(String.valueOf( contents.getLikeUserCount() ));
            ((ContentsViewHolder)holder).tvComments.setText(String.valueOf( contents.getCommentsCount() ));
            ((ContentsViewHolder)holder).tvTime.setText( TimeManager.getFormedDate(contents.getCreateTime()) ); // 작성 시간
        }
    }

    // 콘텐츠 업데이트 함수
    private void updateVarOfContents(String cid, Contents getContents) {
        // contents 하위 항목인 likeUserMap 와 commentList 에 객체를 추가한다.
        mDatabase.child("contents").child(cid).child("hitCount").setValue( getContents.getHitCount() );
        mDatabase.child("contents").child(cid).child("likeUserMap").setValue( getContents.getLikeUserMap() );
    }

    @Override
    public int getItemCount() {
        return contentsArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return contentsArrayList.get(position).getViewType();
    }

    public class ContentsViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategory, tvTitle, tvBody, tvHitCount, tvTime, tvLike, tvComments, tvWriter;
        public ContentsViewHolder(View itemView) {
            super(itemView);

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

    public class TitleViewHolder extends RecyclerView.ViewHolder {
        ImageView titleImage;
        public TitleViewHolder(View itemView) {
            super(itemView);
            titleImage = (ImageView) itemView.findViewById(R.id.storeTitleImageView);
        }
    }

    /* 미리보기 배너 미사용 전환.
    public class DeliveryPreViewHolder extends RecyclerView.ViewHolder {
        ImageView previewImage;
        TextView previewTextTitle, previewTextBody;
        public DeliveryPreViewHolder(View itemView) {
            super(itemView);
            previewImage = (ImageView) itemView.findViewById(R.id.deliveryTitleImageView);
            previewTextTitle = (TextView) itemView.findViewById(R.id.deliveryPreviewTitle);
            previewTextBody = (TextView) itemView.findViewById(R.id.deliveryPreviewBody);
        }
    }
    */

    public class RiderPreViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView profileTextTitle, profileTextState, profileTextSubTitle, profileTextBody;
        public RiderPreViewHolder(View itemView) {
            super(itemView);
            profileImage = (ImageView) itemView.findViewById(R.id.riderProfileImageView);
            profileTextState = (TextView) itemView.findViewById(R.id.rowRiderStateView);
            profileTextTitle = (TextView) itemView.findViewById(R.id.rowRiderTitleView);
            profileTextSubTitle = (TextView) itemView.findViewById(R.id.rowRiderSubTitleView);
            profileTextBody = (TextView) itemView.findViewById(R.id.rowRiderInfoView);
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
