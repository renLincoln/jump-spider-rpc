package com.lucas.msg.codec;

import com.lucas.common.util.SerializationUtils;
import com.lucas.msg.util.CrcUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author: ren
 * @program: small-spider-rpc
 * @description: 自定义指定头且指明了消息长度的编码器
 * @create: 2021-08-29 00:46
 **/
public class FixHeadAndAssignLengthEncoder extends MessageToByteEncoder implements CodecInterFace{

    private Class<?> genericClass;

    public FixHeadAndAssignLengthEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msgObj, ByteBuf byteBuf) throws Exception {
        // 判断传递消息是否为空
        if(msgObj==null){
            return;
        }
        // 序列化对象
        byte[] msgBytes = SerializationUtils.serialize(msgObj);
        int dataLength = msgBytes.length;
        // 获取得到Crc验证码
        int crcValue = CrcUtil.calcCrc16(msgBytes);
        // 写值 添加消息头、消息长度、Crc验证码
        byteBuf.writeByte(HEAD_FLAG);
        byteBuf.writeChar(dataLength);
        byteBuf.writeBytes(msgBytes);
        byteBuf.writeChar(crcValue);
    }
}
