package com.sedmelluq.discord.lavaplayer.container.adts;

import com.sedmelluq.discord.lavaplayer.tools.io.SeekableInputStream;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.BaseAudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.LocalAudioTrackExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Audio track that handles an ADTS packet stream
 */
public class AdtsAudioTrack extends BaseAudioTrack {
  private static final Logger log = LoggerFactory.getLogger(AdtsAudioTrack.class);

  private final SeekableInputStream inputStream;

  /**
   * @param trackInfo Track info
   * @param inputStream Input stream for the ADTS stream
   */
  public AdtsAudioTrack(AudioTrackInfo trackInfo, SeekableInputStream inputStream) {
    super(trackInfo);

    this.inputStream = inputStream;
  }

  @Override
  public void process(LocalAudioTrackExecutor localExecutor) throws Exception {
    AdtsStreamProvider provider = new AdtsStreamProvider(inputStream, localExecutor.getProcessingContext());

    try {
      log.debug("Starting to play ADTS stream {}", getIdentifier());

      localExecutor.executeProcessingLoop(provider::provideFrames, null);
    } finally {
      provider.close();
    }
  }
}