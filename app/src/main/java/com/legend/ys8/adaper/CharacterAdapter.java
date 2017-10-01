package com.legend.ys8.adaper;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legend.ys8.R;
import com.legend.ys8.conf.RxBus;
import com.legend.ys8.conf.YsApplication;
import com.legend.ys8.event.CharacterInfoEvent;
import com.legend.ys8.model.CharacterImpl;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;


/**
 *
 * Created by legend on 2017/8/24.
 */

public class CharacterAdapter extends BaseAdapter<CharacterAdapter.ViewHolder>{


    private ArrayList<CharacterImpl> characters;

    RecyclerView recyclerView;

    int width=0;
    int height=0;

    Observable observable;


    public CharacterAdapter(ArrayList<CharacterImpl> characters) {
        this.characters = characters;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;




        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);

                status=newState;


//                if (newState==0) {
//                    RxBus.getDefault().post(newState);
//                }

//                observable=Observable
//                        .create(new ObservableOnSubscribe<Integer>() {
//                            @Override
//                            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//                                e.onNext(newState);
//                            }
//                        })
//                        .distinct()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread());


                Log.d("test->>>>>>>>>>",newState+"");

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public CharacterAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.character_list,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);

        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) viewHolder.imageView.getLayoutParams();
        int w= YsApplication.getContext().getResources().getDisplayMetrics().widthPixels;

        int space=YsApplication.getContext().getResources().getDimensionPixelSize(R.dimen.main_default_space)*2;


        int width=w-space;

        this.width=width;

        int h= (int) (0.888888*width);
        this.height=h;

        layoutParams.height=h;
        viewHolder.imageView.setLayoutParams(layoutParams);

        viewHolder.view.setOnClickListener(event-> {
            CharacterImpl character=characters.get(viewHolder.getAdapterPosition());
            EventBus.getDefault().post(new CharacterInfoEvent(character));
        });
        return viewHolder;
    }

    int status=-1;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        CharacterImpl character=characters.get(position);

        holder.textView.setText(character.getName());


        holder.imageView.setTag(character.getBook_url());


        holder.imageView.setImageResource(R.drawable.default_bg);

        Log.d("starus----->",status+"");

        if (status == -1||status==0||status==1) {
            imageLoader.setImage(character.getBook_url(),holder.imageView,width,height);
        }


//        RxBus
//                .getDefault()
//                .tObservable(Integer.class)
//                .distinct()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Integer>() {
//
//            @Override
//            public void accept(Integer integer) throws Exception {
////                Log.d("integer------>",integer.intValue()+"");
//
//                switch (integer.intValue()){
//                    case SCROLL_STATE_IDLE://0,停止滑动
//                        imageLoader.setImage(character.getBook_url(),holder.imageView,width,height);
//
//                        break;
//                    case SCROLL_STATE_DRAGGING://1 手指慢慢滑动
//
////                        imageLoader.setImage(character.getBook_url(),holder.imageView,width,height);
//                        break;
//                    case SCROLL_STATE_SETTLING://2 自然滑动&快速滑动
//
//                        status=SCROLL_STATE_SETTLING;
//
//                        break;
//                }
//            }
//        });

//        if (recyclerView!=null){
//            switch (recyclerView.getScrollState()){
//                case SCROLL_STATE_IDLE:
//                    imageLoader.setImage(character.getBook_url(),holder.imageView,width,height);
//                    break;
//                case SCROLL_STATE_DRAGGING:
//
//                    break;
//
//                case SCROLL_STATE_SETTLING:
//
//                    break;
//            }
//        }




    }

    @Override
    public int getItemCount() {
        if (null==characters){
            return 0;
        }
        return characters.size();
    }

    static class ViewHolder extends BaseAdapter.ViewHolder{

        View view;
        TextView textView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view=itemView;
            this.textView=itemView.findViewById(R.id.character_name);
            this.imageView=itemView.findViewById(R.id.character_album);


        }
    }

    public void setCharacters(ArrayList<CharacterImpl> characters) {
        this.characters = characters;
        notifyDataSetChanged();
    }


}
