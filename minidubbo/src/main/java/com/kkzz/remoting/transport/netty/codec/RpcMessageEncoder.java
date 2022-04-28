package com.kkzz.remoting.transport.netty.codec;

import com.kkzz.compress.Compress;
import com.kkzz.compress.gzip.GzipCompress;
import com.kkzz.enums.CompressTypeEnum;
import com.kkzz.enums.SerializerTypeEnum;
import com.kkzz.remoting.constants.RpcConstants;
import com.kkzz.remoting.dto.RpcMessage;
import com.kkzz.serialize.Serializer;
import com.kkzz.serialize.fastjson.FastJsonSerializer;
import com.kkzz.serialize.hessian.HessianSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage rpcMessage, ByteBuf out) throws Exception {
        try {
            out.writeBytes(RpcConstants.MAGIC_NUMBER);
            out.writeByte(RpcConstants.VERSION);
            //这个位置需要写入总长度,先做标记,最后再写入
            out.writerIndex(out.writerIndex() + 4);
            byte messageType = rpcMessage.getMessageType();
            out.writeByte(messageType);
            out.writeByte(rpcMessage.getCodec());
            out.writeByte(CompressTypeEnum.GZIP.getCode());
            out.writeInt(ATOMIC_INTEGER.getAndIncrement());

            //写入消息体
            byte[] bodyBuf = null;
            int fullLength = RpcConstants.HEAD_LENGTH;
            if (messageType != RpcConstants.HEARTBEAT_REQUEST_TYPE && messageType != RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                //todo 使用ExtensionLoader
                String codecName = SerializerTypeEnum.getName(rpcMessage.getCodec());
                Serializer serializer = new HessianSerializer();
                bodyBuf = serializer.serialize(rpcMessage.getData());
                //todo 使用ExtensionLoader
                String compressName = CompressTypeEnum.getName(rpcMessage.getCompress());
                Compress compress = new GzipCompress();
                bodyBuf = compress.compress(bodyBuf);
                fullLength += bodyBuf.length;
            }
            if (bodyBuf != null) {
                out.writeBytes(bodyBuf);
            }
            int writeIndex = out.writerIndex();
            out.writerIndex(writeIndex - fullLength + RpcConstants.MAGIC_NUMBER.length + 1);
            out.writeInt(fullLength);
            out.writerIndex(writeIndex);
        } catch (Exception e) {
            log.error("编码失败", e);
        }
    }
}
