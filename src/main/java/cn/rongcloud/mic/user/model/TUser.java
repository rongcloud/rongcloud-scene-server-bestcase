package cn.rongcloud.mic.user.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/5/25
 */
@Entity
@Table(name = "t_user")
@Data
public class TUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uid;
    private String name;
    private String portrait;
    private String mobile;
    private Integer type;
    @Column(name = "device_id")
    private String deviceId;
    private Date createDt;
    private Date updateDt;
    @Column(name = "belong_room_id")
    private String belongRoomId;
    private Integer status;
    private Date freezeTime;
    private Integer sex=1;

}
