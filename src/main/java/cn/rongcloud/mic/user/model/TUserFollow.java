package cn.rongcloud.mic.user.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_user_follow")
@Data
public class TUserFollow implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uid;
    @Column(name = "follow_uid")
    private String followUid;
    private Integer status;
    private Date createDt;
    private Date updateDt;
}
