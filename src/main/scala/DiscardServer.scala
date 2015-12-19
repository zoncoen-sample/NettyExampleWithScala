package io.netty.example.discard

import java.lang.Boolean

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

import scala.util.{Failure, Success, Try}

class DiscardServerHandler extends ChannelInboundHandlerAdapter {
  override def channelRead(ctx: ChannelHandlerContext, msg: Any) = {
    msg match {
      case b: ByteBuf =>
        // This code will show received text.
        // while (b.isReadable()) {
        //   Console.print(b.readByte().asInstanceOf[Char])
        //   Console.flush()
        // }

        b.release()
    }
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
    cause.printStackTrace()
    ctx.close()
  }
}

class DiscardServer(private val port: Int) {
  def run(): Unit = {
    val bossGroup: EventLoopGroup = new NioEventLoopGroup()
    val workerGroup: EventLoopGroup = new NioEventLoopGroup()
    val result = Try {
      val b: ServerBootstrap = new ServerBootstrap()
      b.group(bossGroup, workerGroup)
        .channel(classOf[NioServerSocketChannel])
        .childHandler(new ChannelInitializer[SocketChannel] {
          override def initChannel(ch: SocketChannel) = {
            ch.pipeline().addLast(new DiscardServerHandler())
          }
        })
        .option[Integer](ChannelOption.SO_BACKLOG, 128)
        .childOption[Boolean](ChannelOption.SO_KEEPALIVE, true)

      val f: ChannelFuture = b.bind(port).sync()
      f.channel().closeFuture().sync()
    }
    result match {
      case Success(v) =>
        workerGroup.shutdownGracefully()
        bossGroup.shutdownGracefully()
      case Failure(e) =>
        workerGroup.shutdownGracefully()
        bossGroup.shutdownGracefully()
    }
  }
}

object DiscardServer {
  def main(args: Array[String]) {
    val port = 8080
    new DiscardServer(port).run()
  }
}