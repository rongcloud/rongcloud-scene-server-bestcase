package cn.rongcloud.mic.game.pojos;

import lombok.Data;

import java.util.List;

@Data
public class GameListResp {

    private Long mg_id;

    private String mg_id_str;

    // 无法转化成对象，因为返回字段不符合java 语法
    private String name;

    private String desc;

    private String thumbnail332x332;

    private String big_loading_pic;

    private List<GameModel> game_mode_list;

    @Data
    public static class GameModel{

        private int mode;

        private int[] count;

        private int[] team_count;

        private int[] team_member_count;

    }
}
