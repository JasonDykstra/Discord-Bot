package DiscordBot.discordbot;

import java.nio.ByteBuffer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
//import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 * 
 * Don't worry about this class, auto generated.
 * 
 * 
 * 
 * 
 * This is a wrapper around AudioPlayer which makes it behave as an AudioSendHandler for JDA. As JDA calls canProvide
 * before every call to provide20MsAudio(), we pull the frame in canProvide() and use the frame we already pulled in
 * provide20MsAudio().
 */
public class AudioPlayerSendHandler implements AudioSendHandler {
	private final AudioPlayer audioPlayer;
	private AudioFrame lastFrame;

	/**
	 * @param audioPlayer Audio player to wrap.
	 */
	public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
	}

	@Override
	public boolean canProvide() {
		if (lastFrame == null) {
			lastFrame = audioPlayer.provide();
		}

		return lastFrame != null;
	}

//	@Override
//	public byte[] provide20MsAudio() {
//		if (lastFrame == null) {
//			lastFrame = audioPlayer.provide();
//		}
//
//		byte[] data = lastFrame != null ? lastFrame.data : null;
//		lastFrame = null;
//
//		return data;
//	}

	@Override
	public boolean isOpus() {
		return true;
	}

	@Override
	public ByteBuffer provide20MsAudio() {
		// TODO Auto-generated method stub
		return null;
	}
}
