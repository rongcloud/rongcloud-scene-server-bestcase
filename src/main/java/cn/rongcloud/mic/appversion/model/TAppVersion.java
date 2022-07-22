package cn.rongcloud.mic.appversion.model;

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
@Table(name = "t_appversion")
@Data
public class TAppVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String platform;

    @Column(name = "download_url")
    private String downloadUrl;

    private String version;

    @Column(name = "version_code")
    private Long versionCode;

    @Column(name = "force_upgrade")
    private Boolean forceUpgrade;

    @Column(name = "release_note")
    private String releaseNote;

    @Column(name = "create_dt")
    private Date createDt;

    @Column(name = "update_dt")
    private Date updateDt;

}
