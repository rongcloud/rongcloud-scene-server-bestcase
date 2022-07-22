package cn.rongcloud.mic.game.pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetUrlResp implements Serializable {

    private UrlList api;

    @Data
    public static class UrlList{
        private String get_mg_list;

        private String get_mg_info;

        private String get_game_report_info;

        private String get_game_report_info_page;
    }
}
