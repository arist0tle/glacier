package alpha.common.base.constant.login;

import lombok.Data;

/**
 * @author tanghaiyang on 2018/12/26.
 */
@Data
public class LoginUser {
    private Long userId;
    private String name;
    private String phone;
    private String email;
    private String appSessionId;
}
