package cn.rongcloud.mic.common.jwt.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by sunyinglong on 2020/6/25
 */
public enum UserAgentTypeEnum {
	AGENT_ANDROID("Android", 1),
	AGENT_IOS("IOS", 2),
	AGENT_OTHER("Other", 3);

	private @Getter
    @Setter(AccessLevel.PRIVATE) String agent;
	private @Getter
    @Setter(AccessLevel.PRIVATE) int type;

	private UserAgentTypeEnum(String agent, int type) {
		this.agent = agent;
		this.type = type;
	}

	public static UserAgentTypeEnum getEnumByValue(int type) {
		for (UserAgentTypeEnum item : UserAgentTypeEnum.values()) {
			if (item.getType() == type) {
				return item;
			}
		}

		throw new IllegalArgumentException();
	}

	public static UserAgentTypeEnum getEnumByUserAgent(String userAgent) {
		UserAgentTypeEnum type = AGENT_OTHER;
		
		if (null != userAgent) {
			userAgent = userAgent.toLowerCase();
			if(userAgent.contains("iphone")||userAgent.contains("ipod")||userAgent.contains("ipad")){
		        type = AGENT_IOS;
		    } else if(userAgent.contains("android") ) { 
		        type = AGENT_ANDROID;
		    } 
		}

		return type;
	}

}
