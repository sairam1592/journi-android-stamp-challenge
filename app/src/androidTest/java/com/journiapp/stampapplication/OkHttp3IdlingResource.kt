package com.journiapp.stampapplication

import androidx.annotation.CheckResult
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback
import okhttp3.Dispatcher
import okhttp3.OkHttpClient

/** An [IdlingResource] for [OkHttpClient] */
class OkHttp3IdlingResource private constructor(private val name: String, private val dispatcher: Dispatcher) : IdlingResource {

    /**
     * Create a new [IdlingResource] from @code client as @code name. You must register
     * this instance using {@code Espresso.registerIdlingResources}.
     */
    @Volatile
    var callback: ResourceCallback? = null

    override fun getName(): String {
        return name
    }

    override fun isIdleNow(): Boolean {
        return dispatcher.runningCallsCount() == 0
    }

    override fun registerIdleTransitionCallback(callback: ResourceCallback) {
        this.callback = callback
    }

    companion object {
        /**
         * Create a new [IdlingResource] from `client` as `name`. You must register
         * this instance using `Espresso.registerIdlingResources`.
         */
        @CheckResult  // Extra guards as a library.
        fun create(name: String, client: OkHttpClient): OkHttp3IdlingResource {
            return OkHttp3IdlingResource(name, client.dispatcher)
        }
    }

    init {
        dispatcher.idleCallback = Runnable {
            val callback = callback
            callback?.onTransitionToIdle()
        }
    }
}