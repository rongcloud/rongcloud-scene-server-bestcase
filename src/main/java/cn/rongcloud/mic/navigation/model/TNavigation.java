package cn.rongcloud.mic.navigation.model;

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
 *@Author gaoxin
 *@Date 2021/9/7 2:40 下午
 *@Version 1.0
 **/
@Entity
@Table(name = "t_navigation")
@Data
public class TNavigation implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String desc;

	@Column(name = "cover_image")
	private String coverImage;

	private Integer layout;

	@Column(name = "effective_type")
	private Integer effectiveType;

	@Column(name = "create_user")
	private String createUser;

	@Column(name = "create_dt")
	private Date createDt;

	@Column(name = "update_user")
	private String updateUser;

	@Column(name = "update_dt")
	private Date updateDt;
}
