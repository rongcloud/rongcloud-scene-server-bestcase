package cn.rongcloud.mic.feedback.model;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "t_feedback")
public class TFeedBack implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_dt")
    private Date createDt;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "type")
    private Integer type;

    @Column(name = "good")
    private Boolean good;

    @Column(name = "reason")
    private String reason;
}
