package com.example.infraymer.trezvoeradio

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.drm.FrameworkMediaDrm
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.util.Util.getUserAgent
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_about.*
import okhttp3.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.share
import org.jetbrains.anko.uiThread
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    /***********************************************************************************************
     * Properties
     ***********************************************************************************************/
    private lateinit var mPLayerManager: PlayerManager
    private var mPlay = false
    private var mAbout = false
    private lateinit var mClient: OkHttpClient

    /***********************************************************************************************
     * Override funs
     ***********************************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPLayerManager = PlayerManager(this)

        main_action_btn.setOnClickListener {
            mPlay = !mPlay
            mPLayerManager.play(mPlay)
            swapIcon(mPlay)
            runTaskGettingMetaData()
        }
        main_share_btn.setOnClickListener { share() }
        main_info_btn.setOnClickListener { showAbout(true) }
        about_back_btn.setOnClickListener { showAbout(false) }
//        about_link.setOnClickListener { browse(getString(R.string.site), false) }


        mClient = OkHttpClient()
    }

    override fun onBackPressed() {
        if (mAbout)
            showAbout(false)
        else
            super.onBackPressed()
    }

    /***********************************************************************************************
     * Private funs
     ***********************************************************************************************/
    private fun swapIcon(play: Boolean) {
        if (play) {
            main_play_image_view.animate()
                    .alphaBy(1f)
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction {
                        main_pause_image_view.alpha = 0f
                        main_pause_image_view.visible()
                        main_pause_image_view.animate()
                                .alphaBy(0f)
                                .alpha(1f)
                                .setDuration(100)
                                .start()
                    }
                    .start()
//            main_play_image_view.invisible()
//            main_pause_image_view.visible()
        } else {
            main_pause_image_view.animate()
                    .alphaBy(1f)
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction {
                        main_play_image_view.alpha = 0f
                        main_play_image_view.visible()
                        main_play_image_view.animate()
                                .alphaBy(0f)
                                .alpha(1f)
                                .setDuration(100)
                                .start()
                    }
                    .start()
//            main_play_image_view.visible()
//            main_pause_image_view.invisible()
        }
    }

    private fun showAbout(show: Boolean) {
        mAbout = show
        if (show) {
            layout_about.alpha = 0f
            layout_about.visible()
            layout_about.animate()
                    .setDuration(300)
                    .alphaBy(0f)
                    .alpha(1f)
                    .start()
        }
        else {
            layout_about.animate()
                    .setDuration(300)
                    .alphaBy(1f)
                    .alpha(0f)
                    .withEndAction { layout_about.gone() }
                    .start()
        }
    }

    private fun share() {
        share("Будь трезвым, слушай трезвое радио!")
    }

    private fun getMetaRadio(): String {
        try {
            val request = Request.Builder()
                    .url("http://eu3.radioboss.fm:8259/currentsong?sid=1")
                    .build()
            val response = mClient.newCall(request).execute()
            val s = response.body()?.string() ?: ""
            val result = String(s.toByteArray(Charsets.ISO_8859_1), Charset.forName("windows-1251"))
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            runTaskGettingMetaData().cancel(true)
            return ""
        }
    }

    private fun runTaskGettingMetaData() = doAsync {
        while (mPlay) {
            Log.d(javaClass.simpleName, "Getting meta data")
            val data = getMetaRadio()
            uiThread { main_radio_meta_tv.text = data }
            TimeUnit.SECONDS.sleep(5)
        }
        Log.d(javaClass.simpleName, "Finished thread")
    }
}
