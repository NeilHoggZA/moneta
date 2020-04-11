package za.co.gingergeek.moneta.extensions

import android.animation.Animator
import android.view.View
import za.co.gingergeek.moneta.R

fun View.fadeIn(duration: Long = 250, callback: () -> Unit = {}) {
    animate().alpha(1f)
        .setDuration(duration)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                setTag(R.string.ANIMATION_STARTED, false)
                callback.invoke()
            }

            override fun onAnimationStart(animation: Animator?) {
                setTag(R.string.ANIMATION_STARTED, true)
            }
        })
}

fun View.fadeOut(duration: Long = 250, callback: () -> Unit = {}) {
    animate().alpha(0f)
        .setDuration(duration)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                setTag(R.string.ANIMATION_STARTED, false)
                callback.invoke()
            }

            override fun onAnimationStart(animation: Animator?) {
                setTag(R.string.ANIMATION_STARTED, true)
            }
        })
}