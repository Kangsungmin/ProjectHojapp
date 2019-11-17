package com.SMK.Hojapp.Contents;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.SMK.Hojapp.Chat.ChatActivity;
import com.SMK.Hojapp.Chat.ChatRoomData;
import com.SMK.Hojapp.Contents.ContentsTypes.Contents;
import com.SMK.Hojapp.Contents.ContentsTypes.ViewType;
import com.SMK.Hojapp.GlobalData;
import com.SMK.Hojapp.R;
import com.SMK.Hojapp.TimeManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterDetailedContents extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    String TAG = "AdapterDetailedContents";
    private DatabaseReference mDatabase;
    Context context;
    private GlobalData globalData;
    ArrayList<Contents> detailedContentsArrayList = null;
    RequestManager glide;

    public AdapterDetailedContents(Context context, ArrayList<Contents> commentArrayList){
        this.context = context;
        globalData = (GlobalData) context.getApplicationContext();
        this.detailedContentsArrayList = commentArrayList;
        glide = Glide.with(context);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(viewType == ViewType.ROW_CONTENTS_DETAIL){
            view = inflater.inflate(R.layout.row_feed_detail, parent, false);
            final ContentsDetailViewHolder viewHolder = new ContentsDetailViewHolder(view);
            ImageView likeImage = (ImageView) view.findViewById(R.id.detailedContentsLikeImage);
            final TextView likeCountView = (TextView) view.findViewById(R.id.detailedContentsLikeCountView);
            final TextView tvWriter = (TextView) view.findViewById(R.id.detailedContentsWriterView);

            // 게시글 좋아요 이벤트 리스너
            likeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getAdapterPosition();
                    Contents contents = detailedContentsArrayList.get(position);

                    if(contents != null) {
                        boolean am_i_liked = contents.isInLikeUserMap(globalData.getAccount().getUid());
                        if(am_i_liked == true) {
                            contents.deleteLikeUser(globalData.getAccount().getUid()); // 좋아요 유저 추가
                            likeCountView.setText( contents.getLikeUserCount() + "" ); // 텍스트 표시
                            //업데이트 쿼리 수행
                            updateVarOfContents(contents.getCid(), contents);
                            //addDatabaseLikeUserOfContents(contents.getCid(), globalData.getAccount().getUid()); // 좋아요 유저 DB 추가
                        }
                        else {
                            contents.insertLikeUser(globalData.getAccount().getUid()); // 좋아요 유저 추가
                            likeCountView.setText( contents.getLikeUserCount() + "" ); // 텍스트 표시
                            //업데이트 쿼리 수행
                            updateVarOfContents(contents.getCid(), contents);
                            //addDatabaseLikeUserOfContents(contents.getCid(), globalData.getAccount().getUid()); // 좋아요 유저 DB 추가
                        }
                    }
                }
            });

            // 작성자 클릭 이벤트 리스너
            tvWriter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: 상대방 정보를 표시하는 액티비티가 실행된다.
                    //       하단의 상대방과 1:1 채팅 버튼이 존재하고 클릭 시 상대방과 1:1 채팅방을 생성한다.
                    //       이후 DB 에 새로 생성한 채팅방을 추가하고 해당 채팅방의 채팅 Activity 를 실행한다.
                    int position = viewHolder.getAdapterPosition();
                    Contents contents = detailedContentsArrayList.get(position);
                    if(contents != null)
                    {
                        // 채팅방 생성
                        ArrayList<String> firstMember = new ArrayList<String>();
                        firstMember.add(contents.getwUid());
                        ChatRoomData chatRoom = new ChatRoomData("대화를 시작하세요.", firstMember, System.currentTimeMillis());
                        // 채팅방 DB 추가
                        mDatabase.child("message_room_list").child(chatRoom.getRoomID()).setValue(chatRoom);
                        // 해당 채팅방으로 이동하는 Activity 실행
                        Intent chatActivityIntent = new Intent(context, ChatActivity.class);
                        chatActivityIntent.putExtra("ID_ROOM", chatRoom.getRoomID()); //채팅방 식별자를 넘겨준다.
                        context.startActivity(chatActivityIntent);
                    }
                }
            });

            return viewHolder;
        }
        else {// if(viewType == ViewType.COMMENT){
            view = inflater.inflate(R.layout.row_comment, parent, false);
            final CommentViewHolder viewHolder = new CommentViewHolder(view);
            ImageView likeImage = (ImageView) view.findViewById(R.id.commentLikeImage);
            final TextView likeCountView = (TextView) view.findViewById(R.id.commentLikeText);

            // 댓글 좋아요 이벤트 리스너
            likeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = viewHolder.getAdapterPosition();
                    Contents contents = detailedContentsArrayList.get(position);

                    if(contents != null) {
                        contents.insertLikeUser(globalData.getAccount().getUid()); // 좋아요 유저 추가
                        likeCountView.setText( contents.getLikeUserCount() + "" ); // 텍스트 표시
                        //업데이트 쿼리 수행
                        updateVarOfContents(contents.getCid(), contents);
                    }

                }
            });
            return viewHolder;
        }
    }

    // 콘텐츠 업데이트 함수
    private void updateVarOfContents(String cid, Contents getContents) {
        // contents 하위 항목인 likeUserMap 와 commentList 에 객체를 추가한다.
        mDatabase.child("contents").child(cid).child("hitCount").setValue( getContents.getHitCount() );
        mDatabase.child("contents").child(cid).child("likeUserMap").setValue( getContents.getLikeUserMap() );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ContentsDetailViewHolder){ // 게시글일 때
            // 위젯 데이터 세팅
            ((ContentsDetailViewHolder) holder).tvCategory.setText(detailedContentsArrayList.get(position).getCategory());
            ((ContentsDetailViewHolder) holder).tvTitle.setText(detailedContentsArrayList.get(position).getTitle());
            ((ContentsDetailViewHolder) holder).tvBody.setText(detailedContentsArrayList.get(position).getBody());
            ((ContentsDetailViewHolder) holder).tvWriter.setText(detailedContentsArrayList.get(position).getwUid());
            ((ContentsDetailViewHolder) holder).tvHitCount.setText( String.valueOf(detailedContentsArrayList.get(position).getHitCount()) );
            ((ContentsDetailViewHolder) holder).tvLike.setText( String.valueOf(detailedContentsArrayList.get(position).getLikeUserCount()) );
            ((ContentsDetailViewHolder) holder).tvComments.setText( String.valueOf(detailedContentsArrayList.get(position).getCommentsCount()) );
            ((ContentsDetailViewHolder) holder).tvTime.setText( TimeManager.getFormedDate(detailedContentsArrayList.get(position).getCreateTime()) );
        }
        else if(holder instanceof CommentViewHolder) { // 댓글일 때
            // 위젯 데이터 세팅
            ((CommentViewHolder) holder).tvBody.setText(detailedContentsArrayList.get(position).getBody());
            ((CommentViewHolder) holder).tvTime.setText( TimeManager.getFormedDate(detailedContentsArrayList.get(position).getCreateTime()) );
            ((CommentViewHolder) holder).tvLike.setText( String.valueOf(detailedContentsArrayList.get(position).getLikeUserCount()) );
            ((CommentViewHolder) holder).tvCommentCount.setText( String.valueOf(detailedContentsArrayList.get(position).getCommentsCount()) );
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
            tvHitCount = itemView.findViewById(R.id.detailedContentsHitCountView);
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