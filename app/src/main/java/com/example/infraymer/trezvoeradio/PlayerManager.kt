package com.example.infraymer.trezvoeradio

import android.content.Context
import android.net.Uri
import android.os.Bundle
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by infraymer on 29.11.17.
 */
class PlayerManager(context: Context) {

    companion object {
        const val URL = "http://eu3.radioboss.fm:8259/stream?1480499964434"
    }

    private var player: SimpleExoPlayer
    private var bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
    private var extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()
    private var trackSelectionFactory: TrackSelection.Factory
    private var trackSelector: TrackSelector
    private var defaultBandwidthMeter: DefaultBandwidthMeter
    private var dataSourceFactory: DataSource.Factory
    private var mediaSource: MediaSource

    init {

        trackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)

        trackSelector = DefaultTrackSelector(trackSelectionFactory)

        defaultBandwidthMeter = DefaultBandwidthMeter()
        dataSourceFactory = DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "mediaPlayerSample"), defaultBandwidthMeter)

        mediaSource = ExtractorMediaSource(Uri.parse(URL), dataSourceFactory, extractorsFactory, null, null)

        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)

        player.prepare(mediaSource)
    }

    fun play(playing: Boolean) {
        player.playWhenReady = playing
    }
}