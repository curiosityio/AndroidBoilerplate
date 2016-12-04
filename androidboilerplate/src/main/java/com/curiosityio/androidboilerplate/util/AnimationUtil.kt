package com.curiosityio.androidboilerplate.util

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.animation.PropertyValuesHolder
import android.view.View


open class AnimationUtil() {

    interface AnimationEndListener {
        fun onAnimationEnd(animator: Animator)
    }

    interface AnimationStartListener {
        fun onAnimationStart(animator: Animator)
    }

    interface AnimationStartAndEndListener {
        fun onAnimationEnd(animator: Animator)
        fun onAnimationStart(animator: Animator)
    }

    companion object {
        fun getAnimationStartListener(animationStartListener: AnimationStartListener): Animator.AnimatorListener {
            return object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    animationStartListener.onAnimationStart(animation)
                }
                override fun onAnimationEnd(animation: Animator) {
                }
                override fun onAnimationCancel(animation: Animator) {
                }
                override fun onAnimationRepeat(animation: Animator) {
                }
            }
        }

        fun getAnimationStartAndEndListener(animationStartAndEndListener: AnimationStartAndEndListener): Animator.AnimatorListener {
            return object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    animationStartAndEndListener.onAnimationStart(animation)
                }
                override fun onAnimationEnd(animation: Animator) {
                    animationStartAndEndListener.onAnimationEnd(animation)
                }
                override fun onAnimationCancel(animation: Animator) {
                }
                override fun onAnimationRepeat(animation: Animator) {
                }
            }
        }

        fun getAnimationEndListener(animationStartListener: AnimationEndListener): Animator.AnimatorListener {
            return object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }
                override fun onAnimationEnd(animation: Animator) {
                    animationStartListener.onAnimationEnd(animation)
                }
                override fun onAnimationCancel(animation: Animator) {
                }
                override fun onAnimationRepeat(animation: Animator) {
                }
            }
        }

        fun slideRight(viewToSlideRight: View, width: Float, duration: Long): ObjectAnimator {
            val animation = ObjectAnimator.ofFloat(viewToSlideRight, "translationX", 0f, width).setDuration(duration)
            animation.interpolator = AccelerateDecelerateInterpolator()

            return animation
        }

        fun slideLeft(viewToSlideLeft: View, width: Float, duration: Long): ObjectAnimator {
            val animation = ObjectAnimator.ofFloat(viewToSlideLeft, "translationX", width, 0f).setDuration(duration)
            animation.interpolator = AccelerateDecelerateInterpolator()

            return animation
        }

        fun pulse(viewToAnimate: View, scale: Float, duration: Long): ObjectAnimator {
            val scaleUpY = PropertyValuesHolder.ofFloat("scaleY", scale)
            val scaleUpX = PropertyValuesHolder.ofFloat("scaleX", scale)

            val scaleDownY = PropertyValuesHolder.ofFloat("scaleY", 1.0f)
            val scaleDownX = PropertyValuesHolder.ofFloat("scaleX", 1.0f)

            val animator = ObjectAnimator.ofPropertyValuesHolder(viewToAnimate, scaleUpY, scaleUpX)
            animator.addListener(getAnimationEndListener(object : AnimationEndListener {
                override fun onAnimationEnd(animator: Animator) {
                    ObjectAnimator.ofPropertyValuesHolder(viewToAnimate, scaleDownY, scaleDownX).setDuration(duration / 2).start()
                }
            }))
            animator.duration = (duration / 2)

            return animator
        }
    }

}