package com.SMK.Hojapp.Chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.SMK.Hojapp.R;
import com.SMK.Hojapp.TimeManager;

import java.util.ArrayList;

/**
 * Created by KPlo on 2018. 10. 28..
 */

public class AdapterChatRoom extends RecyclerView.Adapter<AdapterChatRoom.ChatRoomViewHolder> {
    Context context;
    private ArrayList<ChatRoomData> rommDataList = new ArrayList<>();
    private String myNickName;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView TextView_members;
        public TextView TextView_last_msg;
        public TextView TextView_update_time;
        public View rootView;
        public ChatRoomViewHolder(View v) {
            super(v);
            TextView_members = v.findViewById(R.id.TextView_members);
            TextView_last_msg = v.findViewById(R.id.TextView_last_msg);
            TextView_update_time = v.findViewById(R.id.TextView_update_time);
            rootView = v;

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterChatRoom(ArrayList<ChatRoomData> roomArrList, Context context, String myNickName) {
        //{"1","2"}
        this.context = context;
        rommDataList = roomArrList;
        this.myNickName = myNickName;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterChatRoom.ChatRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        final View view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_room, parent, false);

        final ChatRoomViewHolder viewHolder = new ChatRoomViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //액티비티 전환에 필요한 정보들을 넘겨준다.
                Intent i = new Intent(context, ChatActivity.class);
                final int position = viewHolder.getAdapterPosition();
                final ChatRoomData roomData = rommDataList.get(position);

                i.putExtra("ID_ROOM", roomData.getRoomID()); //채팅방 식별자를 넘겨준다.
                context.startActivity(i);
            }
        });

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ChatRoomViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ChatRoomData chatRoom = rommDataList.get(position);

        holder.TextView_members.setText(chatRoom.getMembersName());
        holder.TextView_last_msg.setText(chatRoom.getLastMsg());
        holder.TextView_update_time.setText( TimeManager.getFormedMinute(chatRoom.getUpdateTime()) );
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        //삼항 연산자
        return rommDataList == null ? 0 :  rommDataList.size();
    }

    public ChatRoomData getChatRoom(int position) {
        return rommDataList != null ? rommDataList.get(position) : null;
    }

    public void addChat(ChatRoomData chatRoom) {
        rommDataList.add(chatRoom);
        notifyItemInserted(rommDataList.size()-1); //갱신
    }

}