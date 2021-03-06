package com.empathy.empathy_android.ui.partnerinfo_detail

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.empathy.empathy_android.BaseActivity
import com.empathy.empathy_android.Constants
import com.empathy.empathy_android.R
import com.empathy.empathy_android.extensions.observe
import com.empathy.empathy_android.http.appchannel.ActivityLifecycleState
import com.empathy.empathy_android.http.appchannel.AppChannelApi
import com.empathy.empathy_android.http.appchannel.AppData
import com.skt.Tmap.TMapMarkerItem
import com.skt.Tmap.TMapPoint
import com.skt.Tmap.TMapView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_partner_info_detail.*
import javax.inject.Inject


internal class PartnerInfoDetailActivity: BaseActivity<PartnerInfoDetailViewModel>() {

    companion object {
        const val TYPE_PARTNER = "partner"
        const val TYPE_TOUR    = "tour"
    }

    private val tMapView by lazy {
        TMapView(this)
    }

    private val requestManager by lazy {
        Glide.with(this)
    }

    override fun getLayoutRes(): Int = R.layout.activity_partner_info_detail
    override fun getViewModel(): Class<PartnerInfoDetailViewModel> = PartnerInfoDetailViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTmap()

        subscribe()

        viewModel.channel.accept(ActivityLifecycleState.OnCreate(intent, savedInstanceState))
    }

    private fun subscribe() {
        observe(viewModel.showTourDetail, ::handleTourDetail)
        observe(viewModel.showPartnerDetail, ::handlePartnerDetail)
    }

    private fun handleTourDetail(showTourDetail: PartnerDetailLooknFeel.ShowTourDetail) {
        val tourDetail = showTourDetail.tourDetail

        requestManager.load(tourDetail.imageURL).into(info_detail_image)

        parnter_title.text = tourDetail.title
        content.text = tourDetail.overviewText

        if(tourDetail.businessHours == "") {
            date.text = "정보없음"
        } else {
            date.text = tourDetail.businessHours
        }

        if(tourDetail.dayOff == "") {
            time_or_dayoff.text = "정보없음"
        } else {
            time_or_dayoff.text = tourDetail.dayOff
        }

        price_info.text = "정보없음"

        credit_container.visibility = View.VISIBLE
        creditcard_available.text = tourDetail.creditCard

        pet_container.visibility = View.VISIBLE
        pet_available.text = tourDetail.withPet

        address.text = tourDetail.locationStr

        val latitude: String? = tourDetail.mapx
        val longtitude: String? = tourDetail.mapy
        val pinBitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_pin)

        Log.d("Z123", latitude.toString())

        val marker = TMapMarkerItem().apply {
            tMapPoint = TMapPoint(latitude?.toDouble() ?: Constants.DEFAULT_LATITUDE, longtitude?.toDouble() ?: Constants.DEFAULT_LONGTITUDE)
            icon = pinBitmap
        }

        tMapView.addMarkerItem("location", marker)
    }

    private fun handlePartnerDetail(showPartnerDetail: PartnerDetailLooknFeel.ShowPartnerDetail) {
        val partnerDetail = showPartnerDetail.partnerDetail

        requestManager.load(partnerDetail.imageURL).into(info_detail_image)

        parnter_title.text = partnerDetail.title
        content.text = partnerDetail.overview
        date.text = partnerDetail.duration
        time_or_dayoff.text = partnerDetail.playTime
        price_info.text = partnerDetail.priceInfo
        address.text = partnerDetail.locationStr

        val latitude = partnerDetail.mapx
        val longtitude = partnerDetail.mapy
        val pinBitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_pin)

        val marker = TMapMarkerItem().apply {
            tMapPoint = TMapPoint(latitude.toDouble(), longtitude.toDouble())
            icon = pinBitmap
        }

        tMapView.addMarkerItem("location", marker)
    }

    private fun setTmap() {
        with(t_map_container) {
            addView(tMapView.apply {
                zoomLevel = 14

                setIconVisibility(true)
            })
        }
    }
}