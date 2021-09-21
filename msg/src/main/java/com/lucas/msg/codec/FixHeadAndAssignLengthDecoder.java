package com.lucas.msg.codec;

import com.lucas.common.util.SerializationUtils;
import com.lucas.msg.util.CrcUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author: ren
 * @program: small-spider-rpc
 * @description: 自定义指定头且指明了消息长度的解析器
 * @create: 2021-08-29 00:46
 **/
public class FixHeadAndAssignLengthDecoder extends ByteToMessageDecoder implements CodecInterFace{

    private Class<?> genericClass;

    public FixHeadAndAssignLengthDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 拆分获取完整数据包
        byte[] dataBytes = decode(in);
        if (dataBytes != null && dataBytes.length > 0) {
            // 反序列化
            Object deserialize = SerializationUtils.deserialize(dataBytes, genericClass);
            // 交由handle逻辑处理
            out.add(deserialize);
        }
    }

    /**
     * @Author: ren
     * @Description: 拆分获取完整数据包
     * @Param: [in]
     * @return: byte[]
     * @Date: 2021/8/29
     */
    private byte[] decode(ByteBuf in) {
        // 判断当前可读长度
        int readableLength = in.readableBytes();
        if (readableLength < MIN_LENGTH) {
            return null;
        }
        // 声明记录数据包头位置
        int beginIndex;
        // 查询获取数据包头数据
        while (true) {
            if (in.readByte() == HEAD_FLAG) {
                // 记录读取坐标
                beginIndex = in.readerIndex();
                // 查询到数据包头
                break;
            }
            // 再次判断可读数据包是否满足最小长度限制条件
            if (in.readableBytes() < MIN_LENGTH) {
                return null;
            }
        }
        // 尝试获取数据包长度
        int dataLength = in.readChar();
        if (dataLength <= 0) {
            // 数据包长度异常
            return null;
        }
        // 判断数据包长度是否大于ByteBuf后续可读数据
        if (dataLength > in.readableBytes()) {
            // 数据长度不能满足一个完整的数据包，则更新ByteBuf读取下标
            // 真对于之前读取过的数据则会被忽略掉
            in.readerIndex(beginIndex);
        }
        // 声明完整的空数据包数组
        byte[] dataBytes = new byte[dataLength];
        // 将数据包读取到空数据包数组中
        in.readBytes(dataBytes);
        int crcValue = in.readChar();
        // crc校验
        if (CrcUtil.calcCrc16(dataBytes) != crcValue) {
            // 校验失败，打印日志 TODO
            StringBuilder errorData = new StringBuilder();
            for (byte b : dataBytes) {
                errorData.append(b + ",");
            }
            String errorDataStr = errorData.toString();
            System.out.println("crc校验失败，忽略此异常数据包：" + errorDataStr.substring(0, errorDataStr.length() - 2));
            return null;
        }
        return dataBytes;
    }

    public static void main(String[] args) {
        final int maxlength = 1 << 16;
        final int minlength = (1 << 2) + 2;
        System.out.println(maxlength);
        System.out.println(minlength);
    }
}
