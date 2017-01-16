package com.curiosityio.androidboilerplate.util

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.animation.PropertyValuesHolder
import android.view.View
import android.opengl.ETC1.getWidth
import android.view.ViewGroup
import android.animation.AnimatorSet
import com.curiosityio.androidboilerplate.util.AnimationUtil.AnimationEndListener

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

        // Scale can default to 1.5f. It's the magnitude of change you want. 2 will double the size of your view for example.
        fun pulse(viewToAnimate: View, scale: Float, duration: Int): ObjectAnimator {
            val scaleUpY = PropertyValuesHolder.ofFloat("scaleY", scale)
            val scaleUpX = PropertyValuesHolder.ofFloat("scaleX", scale)

            val scaleDownY = PropertyValuesHolder.ofFloat("scaleY", 1.0f)
            val scaleDownX = PropertyValuesHolder.ofFloat("scaleX", 1.0f)

            val animator = ObjectAnimator.ofPropertyValuesHolder(viewToAnimate, scaleUpY, scaleUpX)
            animator.addListener(getAnimationEndListener(object : AnimationEndListener {
                override fun onAnimationEnd(animator: Animator) {
                    ObjectAnimator.ofPropertyValuesHolder(viewToAnimate, scaleDownY, scaleDownX).setDuration((duration / 2).toLong()).start()
                }
            }))
            animator.duration = (duration / 2).toLong()

            return animator
        }

        private fun getPlayTogetherSet(duration: Int, vararg animators: ObjectAnimator): AnimatorSet {
            val set = AnimatorSet()
            set.playTogether(*animators)
            set.duration = duration.toLong()

            return set
        }

        fun shake(viewToAnimate: View, duration: Int): ObjectAnimator {
            return ObjectAnimator.ofFloat(viewToAnimate, "translationX", 0.toFloat(), 25.toFloat(), -25.toFloat(), 25.toFloat(), -25.toFloat(), 15.toFloat(), -15.toFloat(), 6.toFloat(), -6.toFloat(), 0.toFloat()).setDuration(duration.toLong())
        }

        fun fadeOut(viewToAnimate: View, duration: Int): ObjectAnimator {
            return ObjectAnimator.ofFloat(viewToAnimate, "alpha", 1.toFloat(), 0.toFloat()).setDuration(duration.toLong())
        }

        fun fadeIn(viewToAnimate: View, duration: Int): ObjectAnimator {
            return ObjectAnimator.ofFloat(viewToAnimate, "alpha", 0.toFloat(), 1.toFloat()).setDuration(duration.toLong())
        }

        fun slideDownAndOut(viewToAnimate: View, duration: Int): AnimatorSet {
            val parent = viewToAnimate.parent as ViewGroup
            val distance = parent.height - viewToAnimate.top

            return getPlayTogetherSet(duration,
                    ObjectAnimator.ofFloat(viewToAnimate, "alpha", 1.toFloat(), 0.toFloat()),
                    ObjectAnimator.ofFloat(viewToAnimate, "translationY", 0.toFloat(), distance.toFloat()))
        }

        fun slideInAndUp(viewToAnimate: View, duration: Int): AnimatorSet {
            val parent = viewToAnimate.parent as ViewGroup
            val distance = parent.height - viewToAnimate.top

            return getPlayTogetherSet(duration,
                    ObjectAnimator.ofFloat(viewToAnimate, "alpha", 0.toFloat(), 1.toFloat()),
                    ObjectAnimator.ofFloat(viewToAnimate, "translationY", distance.toFloat(), 0.toFloat()))
        }

        fun slideOutToRight(viewToAnimate: View, duration: Int): AnimatorSet {
            val parent = viewToAnimate.parent as ViewGroup
            val distance = parent.width - viewToAnimate.left

            return getPlayTogetherSet(duration,
                    ObjectAnimator.ofFloat(viewToAnimate, "alpha", 1.toFloat(), 0.toFloat()),
                    ObjectAnimator.ofFloat(viewToAnimate, "translationX", 0.toFloat(), distance.toFloat()))
        }

        fun slideInFromRight(viewToAnimate: View, duration: Int): AnimatorSet {
            val parent = viewToAnimate.parent as ViewGroup
            val distance = parent.width - viewToAnimate.left

            return getPlayTogetherSet(duration,
                    ObjectAnimator.ofFloat(viewToAnimate, "alpha", 0.toFloat(), 1.toFloat()),
                    ObjectAnimator.ofFloat(viewToAnimate, "translationX", distance.toFloat(), 0.toFloat()))
        }
    }

}