package com.jimmy.roomwordsample.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BulletSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jimmy.roomwordsample.R;
import com.jimmy.roomwordsample.businesslogic.storage.entities.Word;
import com.jimmy.roomwordsample.ui.helpers.ItemTouchHelperAdapter;
import com.jimmy.roomwordsample.ui.helpers.ItemTouchHelperViewHolder;
import com.jimmy.roomwordsample.ui.helpers.OnStartDragListener;

import java.util.Collections;
import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder>
        implements ItemTouchHelperAdapter {


    private boolean removeFromDB  = false;

    public interface SwipCallBack{

       public void removeItem(Word word);
    }

    private final LayoutInflater mInflater;
    private List<Word> mWords; // Cached copy of words
    private OnStartDragListener mDragStartListener;// interface implementer lister
    private SwipCallBack swipCallBack;

    public WordListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

     public WordListAdapter(Context context, OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
        mInflater = LayoutInflater.from(context);
    }

    public WordListAdapter(Context context, OnStartDragListener dragStartListener, SwipCallBack swipCallBack) {
        mDragStartListener = dragStartListener;
        mInflater = LayoutInflater.from(context);
        this.swipCallBack = swipCallBack;
    }

    @NonNull
    @Override
    public WordListAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final WordListAdapter.WordViewHolder holder, int position) {

        if (mWords != null) {

            Word current = mWords.get(position);
            SpannableString spannableWord = new SpannableString(current.getWord());
            SpannableString spannableMeaning= new SpannableString(current.getMeaning());

            spannableWord.setSpan(
                    new RelativeSizeSpan(1.3f),
                    0, 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannableMeaning.setSpan(
                    new BulletSpan(10, Color.GRAY),
                    /* start index */ 0, /* end index */ spannableMeaning.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.wordItemView.setText(spannableWord);
            holder.wordMeaningView.setText(spannableMeaning);

            // Start a drag whenever the handle view it touched
            holder.wordItemView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }

            });
        } else {
            // Covers the case of data not being ready yet.
            holder.wordItemView.setText("No Word");
            holder.wordMeaningView.setText("----------");
        }
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mWords != null) {
            Log.e("WORD LIST L", mWords.size() + "");
            return mWords.size();
        } else return 0;
    }

    public void setWords(List<Word> words){
        mWords = words;
        notifyDataSetChanged();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (mWords != null) {
           /*  Collections.swap
             Swaps the elements at the specified positions in the specified list.
            (If the specified positions are equal, invoking this method leaves the list unchanged.)
            */
            Collections.swap(mWords, fromPosition, toPosition);
            // Notify any registered observers that the item reflected at fromPosition has been moved to toPosition
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        if (mWords != null){

            removeFromDB = false;
            Word wrd = mWords.get(position);
            Log.e("WORD DEL", wrd.getWord() );
            swipCallBack.removeItem(wrd);

            mWords.remove(position);
            notifyItemRemoved(position);
        }
    }


    class WordViewHolder extends RecyclerView.ViewHolder  implements ItemTouchHelperViewHolder {
        private final TextView wordItemView;
        private final TextView wordMeaningView;

        private WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);
            wordMeaningView = itemView.findViewById(R.id.textView_meaning);
        }

        @Override
        public void onItemSelected() {}

        @Override
        public void onItemClear() {}
    }
}
