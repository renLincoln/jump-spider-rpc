package com.lucas.msg.codec;

public interface CodecInterFace {
    /**
     * 指定解析长度限制，最大与之最小值
     */
    int MAX_LENGTH = 1 << 16;
    /**
     * 头字节 + 两位数据包长度 + 两位crc验证字节
     */
    int MIN_LENGTH = 6;
    int CRC_LENGTH = 2;
    /**
     * 补全的字节 ：消息头、消息长度、Crc验证码
     */
    int EXTRA_LENGTH = 5;
    /**
     * 数据包固定头
     */
    byte HEAD_FLAG = 0x7C;
}
