package cn.rongcloud.mic.game.pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetGameListReq implements Serializable {

    private String app_id;

    private String app_secret;
}
