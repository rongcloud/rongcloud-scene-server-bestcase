package cn.rongcloud.common.im;

import lombok.Data;

/**
 * Created by sunyinglong on 2020/7/8
 */
@Data
public class ChrmEntrySetInfo {

    private String chrmId; //聊天室 Id

    private String userId; //操作用户 Id。通过 Server API 非聊天室中用户可以进行设置。

    private String key; //聊天室属性名称，Key 支持大小写英文字母、数字、部分特殊符号 + = - _ 的组合方式，大小写敏感。最大长度 128 字符

    private String value; //聊天室属性对应的值，最大长度 4096 个字符

    private Boolean autoDelete; //用户退出聊天室后，是否删除此 Key 值。为 true 时删除此 Key 值，为 false 时用户退出后不删除此 Key，默认为 false

    private String objectName; //通知消息类型，设置属性后是否发送通知消息，如需要发送则设置为 RC:chrmKVNotiMsg 或其他自定义消息，为空或不传时不向聊天室发送通知消息，默认为不发送。

    private String content; //通知消息内容，JSON 结构，当 objectName 为 RC:chrmKVNotiMsg 时，content 必须包含 type、key、value 属性

}
