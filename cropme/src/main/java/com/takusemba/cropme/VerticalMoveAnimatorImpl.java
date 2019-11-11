package com.takusemba.cropme;

import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import androidx.dynamicanimation.animation.*;

import static android.view.View.TRANSLATION_Y;

/**
 * VerticalMoveAnimatorImpl is responsible for animating {@link CropImageView} vertically.
 **/
class VerticalMoveAnimatorImpl implements MoveAnimator {

    private SpringAnimation spring;
    private FlingAnimation fling;
    private ObjectAnimator animator;

    private RectF restrictionRect;
    private int maxScale;

    private DynamicAnimation.OnAnimationUpdateListener updateListener = new DynamicAnimation.OnAnimationUpdateListener() {
        @Override
        public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float value, float velocity) {
            reMoveIfNeeded(velocity);
        }
    };
    private boolean isFlinging = false;
    private DynamicAnimation.OnAnimationEndListener endListener = new DynamicAnimation.OnAnimationEndListener() {
        @Override
        public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean b, float v, float v1) {
            isFlinging = false;
        }
    };

    VerticalMoveAnimatorImpl(View target, RectF restrictionRect, int maxScale) {
        this.maxScale = maxScale;
        this.restrictionRect = restrictionRect;

        spring = new SpringAnimation(target,
                new FloatPropertyCompat<View>("Y") {
                    @Override
                    public float getValue(View view) {
                        return view.getY();
                    }

                    @Override
                    public void setValue(View view, float value) {
                        view.setY(value);
                    }
                })
                .setSpring(new SpringForce()
                        .setStiffness(STIFFNESS)
                        .setDampingRatio(DAMPING_RATIO)
                );

        fling = new FlingAnimation(target, DynamicAnimation.Y).setFriction(FRICTION);

        animator = new ObjectAnimator();
        animator.setProperty(TRANSLATION_Y);
        animator.setTarget(target);

    }

    @Override
    public void move(float delta) {
        View target = (View) animator.getTarget();
        if (target != null) {
            cancel();
            animator.setInterpolator(null);
            animator.setDuration(0);
            animator.setFloatValues(target.getTranslationY() + delta);
            animator.start();
        }
    }

    @Override
    public void reMoveIfNeeded(float velocity) {
        View target = (View) animator.getTarget();
        if (target != null) {
            Rect targetRect = new Rect();
            target.getHitRect(targetRect);

            float scale;
            Rect afterRect;
            if (maxScale < target.getScaleY()) {
                scale = maxScale;
                int heightDiff = (int) ((targetRect.height() - targetRect.height() * (maxScale / target.getScaleY())) / 2);
                int widthDiff = (int) ((targetRect.width() - targetRect.width() * (maxScale / target.getScaleY())) / 2);
                afterRect = new Rect(targetRect.left + widthDiff, targetRect.top + heightDiff, targetRect.right - widthDiff, targetRect.bottom - heightDiff);
            } else if (target.getScaleY() < 1) {
                scale = 1;
                int heightDiff = (target.getHeight() - targetRect.height()) / 2;
                int widthDiff = (target.getWidth() - targetRect.width()) / 2;
                afterRect = new Rect(targetRect.left + widthDiff, targetRect.top + heightDiff, targetRect.right - widthDiff, targetRect.bottom - heightDiff);
            } else {
                scale = target.getScaleY();
                afterRect = targetRect;
            }
            float verticalDiff = (target.getHeight() * scale - target.getHeight()) / 2;

            if (restrictionRect.top < afterRect.top) {
                cancel();
                spring.setStartVelocity(velocity).animateToFinalPosition(restrictionRect.top + verticalDiff);
            } else if (afterRect.bottom < restrictionRect.bottom) {
                cancel();
                spring.setStartVelocity(velocity).animateToFinalPosition(restrictionRect.bottom - target.getHeight() - verticalDiff);
            }
        }
    }

    @Override
    public void fling(float velocity) {
        cancel();
        isFlinging = true;
        fling.addUpdateListener(updateListener);
        fling.addEndListener(endListener);
        fling.setStartVelocity(velocity).start();
    }

    @Override
    public boolean isNotFlinging() {
        return !isFlinging;
    }

    private void cancel() {
        isFlinging = false;
        animator.cancel();
        spring.cancel();
        fling.cancel();
        fling.removeUpdateListener(updateListener);
        fling.removeEndListener(endListener);
    }
}
