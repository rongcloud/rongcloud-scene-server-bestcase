package cn.rongcloud.mic.authorization.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qzyu
 * @date 2022/10/17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImTokenInfo {

    private String imToken;
    private String authorization;
    private String portrait;
}
