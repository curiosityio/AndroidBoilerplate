package com.curiosityio.androidboilerplate.util

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.ViewGroup
import android.animation.AnimatorSet
import android.view.animation.Transformation
import android.view.animation.Animation
import android.R.id
import android.os.Build
import android.view.ViewAnimationUtils
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth

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
        fun pulse(viewToAnimate: View, scale: Float, duration: Int, animationEnd: (() -> Unit)? = null): ObjectAnimator {
            val scaleUpY = PropertyValuesHolder.ofFloat("scaleY", scale)
            val scaleUpX = PropertyValuesHolder.ofFloat("scaleX", scale)

            val scaleDownY = PropertyValuesHolder.ofFloat("scaleY", 1.0f)
            val scaleDownX = PropertyValuesHolder.ofFloat("scaleX", 1.0f)

            val animator = ObjectAnimator.ofPropertyValuesHolder(viewToAnimate, scaleUpY, scaleUpX)
            animator.addListener(getAnimationEndListener(object : AnimationEndListener {
                override fun onAnimationEnd(animator: Animator) {
                    val endAnimator = ObjectAnimator.ofPropertyValuesHolder(viewToAnimate, scaleDownY, scaleDownX).setDuration((duration / 2).toLong())
                    endAnimator.addListener(getAnimationEndListener(object : AnimationEndListener {
                        override fun onAnimationEnd(animator: Animator) {
                            animationEnd?.invoke()
                        }
                    }))

                    endAnimator.start()
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

        fun slideDownOutSlideInUpViews(exitView: View, enterView: View, duration: Int, animationStartListener: AnimationStartListener? = null,
        animationEndListener: AnimationEndListener? = null) {
            val slideOutDown = AnimationUtil.slideDownAndOut(exitView, duration)
            slideOutDown.addListener(AnimationUtil.getAnimationStartAndEndListener(object : AnimationStartAndEndListener {
                override fun onAnimationEnd(animator: Animator) {
                    enterView.visibility = View.VISIBLE
                    exitView.visibility = View.GONE

                    val slideInUp = AnimationUtil.slideInAndUp(enterView, duration)
                    slideInUp.addListener(AnimationUtil.getAnimationEndListener(object : AnimationUtil.AnimationEndListener {
                        override fun onAnimationEnd(animator: Animator) {
                            animationEndListener?.onAnimationEnd(animator)
                        }
                    }))
                    slideInUp.start()
                }

                override fun onAnimationStart(animator: Animator) {
                    enterView.visibility = View.GONE
                    exitView.visibility = View.VISIBLE

                    animationStartListener?.onAnimationStart(animator)
                }
            }))
            slideOutDown.start()
        }

        fun expandY(view: View, duration: Int) {
            view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val targetHeight = view.measuredHeight

            view.layoutParams.height = 1
            view.visibility = View.VISIBLE
            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    view.layoutParams.height = if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                    view.requestLayout()
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }

            // 1dp/ms
            //animation.setDuration((int)(targetHeight / view.getContext().getResources().getDisplayMetrics().density));
            animation.duration = duration.toLong()
            view.startAnimation(animation)
        }

        fun collapseY(view: View, duration: Int) {
            val initialHeight = view.measuredHeight

            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    if (interpolatedTime == 1f) {
                        view.visibility = View.GONE
                    } else {
                        view.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                        view.requestLayout()
                    }
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }

            // 1dp/ms
            //animation.setDuration((int)(initialHeight / view.getContext().getResources().getDisplayMetrics().density));
            animation.duration = duration.toLong()
            view.startAnimation(animation)
        }

        // Note: This only runs on version 21 and above.
        fun circularRevealFromCenter(view: View, duration: Int, preLollipopAnimator: (() -> Animator)? = null): Animator {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val centerX = (view.left + view.right) / 2
                val centerY = (view.top + view.bottom) / 2

                val startRadius = 0.toFloat()
                val dx = Math.max(centerX, view.width - centerX)
                val dy = Math.max(centerY, view.height - centerY)
                val finalRadius = Math.hypot(dx.toDouble(), dy.toDouble()).toFloat()

                val animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, finalRadius)
                animator.duration = duration.toLong()

                return animator
            } else {
                if (preLollipopAnimator != null) {
                    return preLollipopAnimator.invoke()
                } else {
                    return fadeIn(view, duration)
                }
            }
        }
    }

}