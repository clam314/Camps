package com.clam.camps.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.clam.camps.R;
import com.clam.camps.fragment.BeenFragment;
import com.clam.camps.fragment.MainFragment;
import com.clam.camps.utils.Result;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by clam314 on 2016/3/4.
 */
public class BeenRecyclerViewAdapter extends RecyclerView.Adapter {

    private Animator mCurrentAnimator;
    private Activity activity;
    private List<Result> list;
    private LayoutInflater inflater;
    private BeenFragment beenFragment;
    private SimpleDraweeView expandedImageView;



    public BeenRecyclerViewAdapter(Activity activity,List<Result> list,BeenFragment beenFragment){
        this.activity = activity;
        if(list!=null) {
            this.list = list;
        }else {
            this.list = new ArrayList<>();
        }
        inflater = LayoutInflater.from(activity);
        this.beenFragment = beenFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        expandedImageView = (SimpleDraweeView) beenFragment.getView().findViewById(R.id.expanded_image);
        return new ImageCardHolder(inflater.inflate(R.layout.card_been,null));

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ImageCardHolder imageCardHolder = (ImageCardHolder)holder;
        imageCardHolder.draweeView.setImageURI(Uri.parse(list.get(position).getUrl()));
        imageCardHolder.draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(imageCardHolder.draweeView,Uri.parse(list.get(position).getUrl()));
            }
        });
        imageCardHolder.tv_who.setText(list.get(position).getWho());
        imageCardHolder.tv_date.setText(list.get(position).getPublishedAt().substring(5,10));
    }

    public void setList(List<Result> list){
        this.list.clear();
        for(Result r : list){
            if (Collections.frequency(this.list,r) < 1) this.list.add(r);
        }
    }

    public void addList(List<Result> list){
        this.list.addAll(list);
    }

    private static class ImageCardHolder extends RecyclerView.ViewHolder{

        public SimpleDraweeView draweeView;
        public TextView tv_who;
        public TextView tv_date;

        public ImageCardHolder(View itemView){
            super(itemView);
            draweeView = (SimpleDraweeView)itemView.findViewById(R.id.draweeview_been);
            tv_who = (TextView)itemView.findViewById(R.id.tv_who);
            tv_date = (TextView)itemView.findViewById(R.id.tv_date);
        }
    }

    private void zoomImageFromThumb(final View thumbView, final Uri uri) {
        // 若已经有animation在进行中，立即删除该animation，然后推进新的animation
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // 加载高分辨率的放大图片

       // expandedImageView.setImageResource(R.drawable.page1);
        expandedImageView.setImageURI(uri);
        if (expandedImageView==null) Log.i("animator","expandedImageView==null");
        // 计算放大图片动画的开始边界和终止边界。该步骤设计较多的数学计算
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // 开始边界即为缩略图的全局可视正方形，终止边界即为container view的全局可视正方形。
        // The start bounds are the global visible rectangle of the thumbnail,
        // 同时设置container viewr的偏移量为边界的起点，因为这是positioning animation 参数（X,Y)的起点。
        thumbView.getGlobalVisibleRect(startBounds);
        //activity.getWindow().getDecorView().findViewById(android.R.id.content).getGlobalVisibleRect(finalBounds, globalOffset);
        beenFragment.getView().findViewById(R.id.fl_content).getGlobalVisibleRect(finalBounds,globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y );
        finalBounds.offset(-globalOffset.x, -globalOffset.y );

        // 用”center crop”技术调整开始边界与终止边界的宽高比一样。这样可以防止动画执行期间的不佳的拉伸。
        // 同时计算开始的缩放比例因子（终止的比例因子总是1.0）
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // 水平方向扩展开始边界
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // 垂直方向扩展开始边界
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // 隐藏缩略图，显示放大view。当动画开始时，将把放大view安放在缩略图的位置。
       // thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);
        //expandedImageView.setAlpha(1f);

        // 设置轴心点，为了SCALE_X和SCALE_Y转换到放大view的左上角（默认轴心点是view的中心）
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // 构建并运行平行的四个转换动画，即为依次对 (X, Y, SCALE_X, and SCALE_Y)四个属性执行转换.set中存放四个animator。
        AnimatorSet set = new AnimatorSet();
        AnimatorSet set1 = new AnimatorSet();
        ObjectAnimator animator = ObjectAnimator.ofFloat(expandedImageView, View.ALPHA, 0.5f, 1f);
        animator.setStartDelay(1);
        animator.setDuration(100);
        set1.play(ObjectAnimator.ofFloat(expandedImageView, View.X,         //View的X属性从startBounds.left变到finalBounds的left
                startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X ,   //View的SCALE_X和SCALE_Y（X轴和Y轴的放大倍数，0.0代表放大为0，1.0代表正常）
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y,
                        startScale, 1f));

        set1.setDuration(500);   //设置动画持续时间
        set1.setInterpolator(new AccelerateDecelerateInterpolator());//设置动画播放为减速器播放
        set.play(set1).with(animator);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();//开始执行预定的动画
        mCurrentAnimator = set; //做好缓存

        // 逆向的操作，从大图到小图，先设定AnimatorSet，然后按设定执行AnimatorSet，然后隐藏大图，显示小图，不详细讲了。
        //
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                ObjectAnimator animator = ObjectAnimator.ofFloat(expandedImageView, View.ALPHA,0.8f, 0f);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.setDuration(50);
                AnimatorSet set = new AnimatorSet();
                AnimatorSet set1 = new AnimatorSet();
                set1.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set1.setDuration(500);
                set1.setInterpolator(new AccelerateDecelerateInterpolator());
                set.play(set1).before(animator);
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                      //  thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.INVISIBLE);
                        mCurrentAnimator = null;
                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.INVISIBLE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
}
