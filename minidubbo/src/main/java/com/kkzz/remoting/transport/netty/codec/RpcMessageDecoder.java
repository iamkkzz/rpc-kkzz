package com.kkzz.remoting.transport.netty.codec;

import com.kkzz.compress.Compress;
import com.kkzz.compress.gzip.GzipCompress;
import com.kkzz.enums.CompressTypeEnum;
import com.kkzz.enums.SerializerTypeEnum;
import com.kkzz.remoting.constants.RpcConstants;
import com.kkzz.remoting.dto.RpcMessage;
import com.kkzz.remoting.dto.RpcRequest;
import com.kkzz.remoting.dto.RpcResponse;
import com.kkzz.serialize.Serializer;
import com.kkzz.serialize.fastjson.FastJsonSerializer;
import com.kkzz.serialize.hessian.HessianSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * 4B  magic_number   1B version（版本）   4B full length（消息长度）    1B messageType（消息类型）
 * 1B codec（序列化类型）1B compress（压缩类型）    4B  requestId（请求的Id）
 * body（object类型数据）
 */
@Slf4j
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {

    public RpcMessageDecoder() {
        this(RpcConstants.MAX_FRAME_LENGTH, 5, 4, -9, 0);
    }

    public RpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decode = super.decode(ctx, in);
        if (decode instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decode;
            if (frame.readableBytes() >= RpcConstants.TOTAL_LENGTH) {
                try {
                    return decodeFrame(frame);
                } catch (Exception e) {
                    log.error("解码网络中的字节流失败", e);
                    throw e;
                } finally {
                    frame.release();
                }
            }
        }
        return decode;
    }

    private Object decodeFrame(ByteBuf in) {
        checkMagicNumber(in);
        checkVersion(in);
        int fullLength = in.readInt();
        byte messageType = in.readByte();
        byte codecType = in.readByte();
        byte compressType = in.readByte();
        int requestId = in.readInt();
        RpcMessage rpcMessage = RpcMessage.builder()
                .codec(codecType)
                .requestId(requestId)
                .messageType(messageType).build();
        if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
            rpcMessage.setData(RpcConstants.PING);
            return rpcMessage;
        }
        if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            rpcMessage.setData(RpcConstants.PONG);
            return rpcMessage;
        }
        int bodyLength=fullLength-RpcConstants.HEAD_LENGTH;
        if (bodyLength>0){
            byte[] buf = new byte[bodyLength];
            in.readBytes(buf);
            //进行解码
            String compressName = CompressTypeEnum.getName(compressType);
            //todo 利用ExtensionLoader获取Compress
            Compress compress = new GzipCompress();
            buf=compress.decompress(buf);
            String codecName = SerializerTypeEnum.getName(codecType);
            //todo 利用ExtensionLoader获取序列化器
            Serializer serializer = new HessianSerializer();
            if (messageType==RpcConstants.REQUEST_TYPE){
                RpcRequest rpcRequest = serializer.deserialize(buf, RpcRequest.class);
                rpcMessage.setData(rpcRequest);
            }else {
                RpcResponse rpcResponse = serializer.deserialize(buf, RpcResponse.class);
                rpcMessage.setData(rpcResponse);
            }
        }
        return rpcMessage;
    }

    private void checkVersion(ByteBuf in) {
        byte version = in.readByte();
        if (version != RpcConstants.VERSION) {
            throw new RuntimeException("不支持该版本协议" + version);
        }
    }

    private void checkMagicNumber(ByteBuf in) {
        int length = RpcConstants.MAGIC_NUMBER.length;
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        for (int i = 0; i < length; i++) {
            if (bytes[i] != RpcConstants.MAGIC_NUMBER[i]) {
                throw new IllegalArgumentException("不支持该协议" + Arrays.toString(bytes));
            }
        }
    }


}
