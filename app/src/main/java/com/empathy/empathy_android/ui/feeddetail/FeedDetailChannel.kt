package com.empathy.empathy_android.ui.feeddetail

import com.empathy.empathy_android.http.appchannel.ActivityLifecycleState
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import javax.inject.Inject


internal class FeedDetailChannel @Inject constructor(): FeedDetailChannelApi {

    private val lifecycleChannel: Relay<ActivityLifecycleState>        = PublishRelay.create()
    private val viewActionChannel: Relay<FeedDetailViewAction> = PublishRelay.create()
    private val navigationChannel: Relay<FeedDetailNavigation> = PublishRelay.create()

    override fun ofLifeCycle(): Observable<ActivityLifecycleState>        = lifecycleChannel
    override fun ofViewAction(): Observable<FeedDetailViewAction> = viewActionChannel
    override fun ofNavigation(): Observable<FeedDetailNavigation> = navigationChannel

    override fun accept(lifecycle: ActivityLifecycleState)        = lifecycleChannel.accept(lifecycle)
    override fun accept(viewAction: FeedDetailViewAction) = viewActionChannel.accept(viewAction)
    override fun accept(navigation: FeedDetailNavigation) = navigationChannel.accept(navigation)

}