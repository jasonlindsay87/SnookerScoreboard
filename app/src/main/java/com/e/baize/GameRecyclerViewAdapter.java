package com.e.baize;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameRecyclerViewAdapter extends RecyclerView.Adapter<GameRecyclerViewAdapter.ViewHolder> {
    private List<SavedGame> mData;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    GameRecyclerViewAdapter(Context context, List<SavedGame> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_saved_game, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SavedGame game = mData.get(position);
        String gamePlayers = game.PlayerOneName +" ["+ game.playerOneScore +"]   vs.   ["+ game.playerTwoScore + "] " + game.PlayerTwoName;
        String gameDate = game.CreatedDate + " [" + Game.gameTimer(game.GameID)+ "]";
        holder.textViewGame.setText(gameDate);
        holder.textViewPlayers.setText(gamePlayers);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    public Integer getItem(int position) {
        return mData.get(position).GameID;
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void deleteItem(int position){
        Game.deleteGame(getItem(position));
        mData.remove(position);
        notifyItemRemoved(position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewGame;
        TextView textViewPlayers;
        ImageView textViewDelete;
        LinearLayout layoutGame;

        ViewHolder(View itemView) {
            super(itemView);
            textViewGame = itemView.findViewById(R.id.tvGame);
            textViewDelete = itemView.findViewById(R.id.tvDelete);
            textViewPlayers = itemView.findViewById(R.id.tvPlayers);
            layoutGame = itemView.findViewById(R.id.layoutGame);

            layoutGame.setOnClickListener(this);
            textViewDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
            {
                switch (view.getId()) {
                    case R.id.layoutGame:
                        mClickListener.onSelect(view, getAdapterPosition());
                        break;
                    case R.id.tvDelete:
                        SavedGame game =  mData.get(getAdapterPosition());
                        mClickListener.onDelete(view, getAdapterPosition(), game);
                        break;
                }
            }
        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onSelect(View view, int position);
        void onDelete(View view, int position, SavedGame game);
    }

}
