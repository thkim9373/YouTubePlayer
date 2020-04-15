package com.hoony.youtubeplayer.video_view;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;
import com.hoony.youtubeplayer.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.google.android.exoplayer2.Player.STATE_BUFFERING;
import static com.google.android.exoplayer2.Player.STATE_ENDED;
import static com.google.android.exoplayer2.Player.STATE_IDLE;
import static com.google.android.exoplayer2.Player.STATE_READY;

public class PlayerHolder implements PlayerAdapter {

    private static final int PLAYBACK_POSITION_REFRESH_INTERVAL_MS = 500;

    private final Context mContext;
    private ExoPlayer mVideoPlayer, mAudioPlayer;
    private ControlDispatcher mControlDispatcher = new com.google.android.exoplayer2.DefaultControlDispatcher();
    private PlaybackInfoListener mVideoPlaybackInfoListener, mAudioPlaybackInfoListener;
    private ScheduledExecutorService mExecutor;
    private Runnable mSeekbarPositionUpdateTask;

    PlayerHolder(Context context) {
        this.mContext = context;
    }

    void initMediaPlayer() {
        if (mVideoPlayer == null) {
            mVideoPlayer = ExoPlayerFactory.newSimpleInstance(mContext);
            mAudioPlayer = ExoPlayerFactory.newSimpleInstance(mContext);
            mVideoPlayer.addListener(new Player.EventListener() {
                @Override
                public void onLoadingChanged(boolean isLoading) {
                    if (isLoading) {
                        initProgressCallback();
                    }
                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    switch (playbackState) {
                        case STATE_IDLE:
                            if (mVideoPlaybackInfoListener != null)
                                mVideoPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.IDLE);
                            break;
                        case STATE_BUFFERING:
                            if (mVideoPlaybackInfoListener != null) {
                                if (playWhenReady) {
                                    mVideoPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.PREPARED);
                                } else {
                                    mVideoPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.BUFFERING);
                                }
                            }
                            break;
                        case STATE_READY:
                            if (playWhenReady) {
                                startUpdatingCallbackWithPosition();
                                if (mVideoPlaybackInfoListener != null)
                                    mVideoPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.PLAYING);
                            } else {
                                stopUpdatingCallbackWithPosition();
                                if (mVideoPlaybackInfoListener != null)
                                    mVideoPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.PAUSED);
                            }
                            break;
                        case STATE_ENDED:
                            stopUpdatingCallbackWithPosition();
                            if (mVideoPlaybackInfoListener != null)
                                mVideoPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.ENDED);
                            break;
                    }
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }
            });
            mAudioPlayer.addListener(new Player.EventListener() {
                @Override
                public void onLoadingChanged(boolean isLoading) {
                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    switch (playbackState) {
                        case STATE_IDLE:
                            if (mAudioPlaybackInfoListener != null)
                                mAudioPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.IDLE);
                            break;
                        case STATE_BUFFERING:
                            if (mAudioPlaybackInfoListener != null) {
                                if (playWhenReady) {
                                    mAudioPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.PREPARED);
                                } else {
                                    mAudioPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.BUFFERING);
                                }
                            }
                            break;
                        case STATE_READY:
                            if (playWhenReady) {
                                startUpdatingCallbackWithPosition();
                                if (mAudioPlaybackInfoListener != null)
                                    mAudioPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.PLAYING);
                            } else {
                                stopUpdatingCallbackWithPosition();
                                if (mAudioPlaybackInfoListener != null)
                                    mAudioPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.PAUSED);
                            }
                            break;
                        case STATE_ENDED:
                            stopUpdatingCallbackWithPosition();
                            if (mAudioPlaybackInfoListener != null)
                                mAudioPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.ENDED);
                            break;
                    }
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }
            });
        }
    }

    void setPlayer(PlayerView playerView) {
        if (mVideoPlayer != null) {
            playerView.setPlayer(mVideoPlayer);
        }
    }

    void setPlaybackInfoListener(PlaybackInfoListener listener) {
        this.mVideoPlaybackInfoListener = listener;
    }

    void setAudioPlaybackInfoListener(PlaybackInfoListener listener) {
        this.mAudioPlaybackInfoListener = listener;
    }

    float getPlaySpeed() {
        if (mVideoPlayer != null) {
            return mVideoPlayer.getPlaybackParameters().speed;
        }
        return -1;
    }

    @Override
    public void loadMedia(int resId) {

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                mContext,
                Util.getUserAgent(mContext, mContext.getString(R.string.app_name))
        );

        MediaSource videoSource =
                new ProgressiveMediaSource
                        .Factory(dataSourceFactory)
                        .createMediaSource(RawResourceDataSource.buildRawResourceUri(resId));

        mVideoPlayer.prepare(videoSource);
        mVideoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void loadMediaFromUri(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                mContext,
                Util.getUserAgent(mContext, mContext.getString(R.string.app_name))
        );

        MediaSource videoSource =
                new ProgressiveMediaSource
                        .Factory(dataSourceFactory)
                        .createMediaSource(uri);

        mVideoPlayer.prepare(videoSource);
        mVideoPlayer.setPlayWhenReady(true);
    }

    void showThumbnail() {
        if (mVideoPlayer != null) {
            mVideoPlayer.seekTo(1);
        }
    }

    @Override
    public void release() {
        if (mVideoPlayer != null) {
            mVideoPlayer.release();
            mVideoPlayer = null;
        }
    }

    @Override
    public boolean isPlaying() {
        if (mVideoPlayer != null) {
            return mVideoPlayer.getPlayWhenReady();
        }
        return false;
    }

    @Override
    public void play() {
        if (mVideoPlayer != null) {
            mVideoPlayer.setPlayWhenReady(!mVideoPlayer.getPlayWhenReady());
        }
    }

    @Override
    public void pause() {
        if (mVideoPlayer != null) {
            mVideoPlayer.setPlayWhenReady(!mVideoPlayer.getPlayWhenReady());
        }
    }

    @Override
    public void fastRewind() {
        if (mVideoPlayer != null && mVideoPlayer.isCurrentWindowSeekable()) {
            seekTo(mVideoPlayer, mVideoPlayer.getCurrentPosition() - 3000);
            if (mVideoPlaybackInfoListener != null) {
                mVideoPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.FAST_REWIND);
            }
        }
    }

    @Override
    public void fastForward() {
        if (mVideoPlayer != null && mVideoPlayer.isCurrentWindowSeekable()) {
            seekTo(mVideoPlayer, mVideoPlayer.getCurrentPosition() + 3000);
            if (mVideoPlaybackInfoListener != null) {
                mVideoPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.FAST_FORWARD);
            }
        }
    }

    private void seekTo(Player player, long positionMs) {
        seekTo(player, player.getCurrentWindowIndex(), positionMs);
    }

    private boolean seekTo(Player player, int windowIndex, long positionMs) {
        long durationMs = player.getDuration();
        if (durationMs != C.TIME_UNSET) {
            positionMs = Math.min(positionMs, durationMs);
        }
        positionMs = Math.max(positionMs, 0);
        return mControlDispatcher.dispatchSeekTo(player, windowIndex, positionMs);
    }

    long getCurrentPosition() {
        if (mVideoPlayer != null) {
            return mVideoPlayer.getCurrentPosition();
        }
        return -1;
    }

    @Override
    public void seekTo(int position) {
        if (mVideoPlayer != null) {
            mVideoPlayer.seekTo(position);
        }
    }

    @Override
    public void replay() {
        if (mVideoPlayer != null) {
            mVideoPlayer.seekTo(0);
            mVideoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void playSpeedChange(float speed) {
        if (mVideoPlayer != null) {
            PlaybackParameters playbackParameters = new PlaybackParameters(speed);
            mVideoPlayer.setPlaybackParameters(playbackParameters);
        }
    }

    private void startUpdatingCallbackWithPosition() {
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadScheduledExecutor();
        }
        if (mSeekbarPositionUpdateTask == null) {
            mSeekbarPositionUpdateTask = this::updateProgressCallbackTask;
        }
        mExecutor.scheduleAtFixedRate(
                mSeekbarPositionUpdateTask,
                0,
                PLAYBACK_POSITION_REFRESH_INTERVAL_MS,
                TimeUnit.MILLISECONDS
        );
    }

    private void stopUpdatingCallbackWithPosition() {
        if (mExecutor != null) {
            mExecutor.shutdown();
            mExecutor = null;
            mSeekbarPositionUpdateTask = null;
        }
    }

    private void updateProgressCallbackTask() {
        if (mVideoPlayer != null && mVideoPlayer.getPlayWhenReady()) {
            long currentPosition = mVideoPlayer.getCurrentPosition();
            if (mVideoPlaybackInfoListener != null) {
                mVideoPlaybackInfoListener.onPositionChanged(currentPosition);
            }
        }
    }

    @Override
    public void initProgressCallback() {
        final long duration = mVideoPlayer.getDuration();
        if (mVideoPlaybackInfoListener != null) {
            mVideoPlaybackInfoListener.onDurationChanged(duration);
            mVideoPlaybackInfoListener.onPositionChanged(0);
        }
    }
}
