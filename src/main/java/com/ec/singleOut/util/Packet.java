package com.ec.singleOut.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

public class Packet {

    private static final Logger logger = LogManager.getLogger(Packet.class);

    /****************** C++消息结构体 ********************/
    //
    // typedef struct tagHEADER
    // {
    // unsigned int lSize;
    // int lFlag;
    // unsigned short uCmd;
    // unsigned int lVersion;
    // unsigned short uSeq;
    // int lResult;
    // tagID id; //用户ID
    // int lReserve[4];//
    // }tagHEADER;
    //
    // //内部服务器通讯
    // typedef struct tagINNERMESSAGE : public tagHEADER
    // {
    // tagINNERMESSAGE()
    // {
    // lFlag = 0x36936988;
    // lSize = sizeof(tagINNERMESSAGE);
    // };
    // unsigned int lType; //消息类型
    // unsigned int lCmdEx; //扩展指令
    // unsigned char pData[0];
    // }tagINNERMESSAGE
    //
    //
    // //_tagWebSysInform + id array + extra buf + msg
    // typedef struct _tagWebSysInform //: public tagHEADER
    // {
    // u_int uChangeID;
    // u_int uCorpID;
    // u_short hdType;
    // u_short hdAction;
    // u_short hdIDCount;
    // u_short hdExtraLen; //extra info buf len
    // u_short hdMsgLen;
    // BYTE pWSIData[0];
    // }tagWebSysInform;
    // static const u_short ghdWebSysInform = sizeof(tagWebSysInform);
    //
    /****************** C++消息结构体 ********************/

    /****************** C++消息格式 ********************/
    // 公共IM:
    // type=999+content=内容+title=标题+url=链接接地址+utltitle=链接标题+acttitle=响应PV标题+ctitle=内容标题
    // 内容换行符号：$$
    // 说明：内容是必需参数，title标题如没有则默认为：系统消息；
    // 如果有url参数则连接才显示，无则不显示
    // utltitle:默认是值为：查看详情，表示连接的文字
    // ctitle:内容标题，默认可以留空
    // 严格按照顺序来。有url的时候，客户端会在url连接后面加上key值
    // 如果title或url或utltitle为空:type=999+content=内容+title=+url=+utltitle=
    //
    // acttitle=响应PV标题：默认为：直接用浏览器打开 参数不正确的（找不到对应的PV则连接不打开）
    // 即每个PV页面有自己的一个唯一标题，该参数就是告诉客户端，打开用哪个PV界面打开
    /****************** C++消息格式 ********************/

    /**
     * @Title: getHeader
     * @Description: 构造消息头
     * @param
     * @return
     * @return byte[]
     * @throws
     */
    private static byte[] getHeader(int mesageLengh) {
        ByteBuffer header = ByteBuffer.allocate(44); // 分配一定的空间,44
        header.putInt(78 + mesageLengh);
        header.putInt(0x36936988);
        header.putShort((short) 13);
        header.putInt(8006);
        short random = (short) new Random().nextInt(32767);
        header.putShort(random);
        header.putInt(0);
        header.putInt(1000000);
        header.putInt(10000);
        header.putInt(0);
        header.putInt(0);
        header.putInt(0);
        header.putInt(0);

        return header.array();
    }

    /**
     * @Title: getBody
     * @Description: 构造消息体内容
     * @param userid
     * @param packetBody
     * @return
     * @return byte[]
     * @throws
     */
    private static byte[] getBody(long userid, String packetBody) {
        ByteBuffer body = ByteBuffer.allocate(34).order(ByteOrder.LITTLE_ENDIAN); // 分配一定的空间,44
        body.putInt(0); // 消息类型
        body.putInt(0); // 扩展指令
        body.putInt(0); // uChangeID
        body.putInt(0); // uCorpID
        body.putShort((short) 0); // hdType
        body.putShort((short) 0); // hdAction
        body.putShort((short) 1); // hdIDCount
        body.putShort((short) 0); // hdExtraLen : extra info buf len
        body.putShort((short) packetBody.length()); // hdMsgLen
        ByteBuffer id = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN); // 分配一定的空间,44
        id.putInt(0);
        id.putInt(new Long(userid).intValue()); // userid

        body.put(id.array());
        return body.array();
    }

    /**
     * @Title: packet
     * @Description: 构造消息报文
     * @param userid
     * @param packetBody
     * @return
     * @return byte[]
     * @throws
     */
    public static byte[] packet(long userid, String packetBody) {
        int size = 70 + 8 + packetBody.getBytes().length; // 8 = 一个userId的长度
        logger.info("----------> message total length: " + size);
        ByteBuffer data = ByteBuffer.allocate(size); // 分配一定的空间,44
        data.put(getHeader(packetBody.getBytes().length));
        data.put(getBody(userid, packetBody));
        data.put(packetBody.getBytes());

        return data.array();
    }

}