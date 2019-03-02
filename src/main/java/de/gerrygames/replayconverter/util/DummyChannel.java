package de.gerrygames.replayconverter.util;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class DummyChannel implements Channel {
	public static final DummyChannel INSTANCE = new DummyChannel();

	@Override
	public ByteBufAllocator alloc() {
		return UnpooledByteBufAllocator.DEFAULT;
	}

	@Override
	public ChannelId id() {
		throw new UnsupportedOperationException();
	}

	@Override
	public EventLoop eventLoop() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ServerSocketChannel parent() {
		throw new UnsupportedOperationException();
	}

	@Override
	public SocketChannelConfig config() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isOpen() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRegistered() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isActive() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelMetadata metadata() {
		throw new UnsupportedOperationException();
	}

	@Override
	public InetSocketAddress localAddress() {
		throw new UnsupportedOperationException();
	}

	@Override
	public InetSocketAddress remoteAddress() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture closeFuture() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWritable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long bytesBeforeUnwritable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long bytesBeforeWritable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Unsafe unsafe() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelPipeline pipeline() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture bind(SocketAddress socketAddress) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture connect(SocketAddress socketAddress) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture disconnect() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture close() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture deregister() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture bind(SocketAddress socketAddress, ChannelPromise channelPromise) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture connect(SocketAddress socketAddress, ChannelPromise channelPromise) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress1, ChannelPromise channelPromise) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture disconnect(ChannelPromise channelPromise) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture close(ChannelPromise channelPromise) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture deregister(ChannelPromise channelPromise) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Channel read() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture write(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture write(Object o, ChannelPromise channelPromise) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Channel flush() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture writeAndFlush(Object o, ChannelPromise channelPromise) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture writeAndFlush(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelPromise newPromise() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelProgressivePromise newProgressivePromise() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture newSucceededFuture() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelFuture newFailedFuture(Throwable throwable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChannelPromise voidPromise() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> Attribute<T> attr(AttributeKey<T> attributeKey) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> boolean hasAttr(AttributeKey<T> attributeKey) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int compareTo(Channel o) {
		throw new UnsupportedOperationException();
	}
}
