package cn.rongcloud.mic.room.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * Desc: 
 *
 * @author zhouxingbin
 * @date 2021/10/25
 */
@Entity
@Table(name = "t_task_mic")
@Data
public class TTaskMic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskType;
    private String taskKey;
    private String userId;
    private String roomId;
    private String msg;
    private Integer delFlag;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String remark;

}
