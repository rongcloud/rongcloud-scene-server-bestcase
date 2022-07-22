package cn.rongcloud.mic.room.model;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_room_sensitive")
@Data
public class TRoomSensitive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id")
    private String roomId;

    @Column(name = "name")
    private String name;

    @Column(name = "create_dt")
    private Date createDt;
}
