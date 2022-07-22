package cn.rongcloud.mic.shumei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_shumei_audit")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShumeiAudit implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 唯一id
     */
    private String uid;


    /**
     * 回调类型1：审核开始;2:审核结束;3:审核状态异常 ;4: 审核结果回调
     */
    private Integer type;

    /**
     * 审核任务的状态码
     */
    private Integer code;

    /**
     * 房间 id
     */
    private String roomId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 当前音视频流 所属的会话 ID
     */
    private String sessionId;

    /**
     * 审核事件详情
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间
     */
    private Date updateTime;



}
