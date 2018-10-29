package org.horizon.common;

import org.horizon.bean.Hr;
import org.horizon.bean.Role;
import org.horizon.common.security.model.UserContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sang on 2017/12/30.
 */
public class HrUtils {
    public static Hr getCurrentHr() {
        UserContext userContext = (UserContext)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Hr hr = new Hr();
        hr.setId(userContext.getId());
        return hr;
    }
}
