package com.chouchase.service;

import com.chouchase.common.ServerResponse;
import com.chouchase.domain.PayInfo;
import org.springframework.stereotype.Service;


public interface PayService {
    public ServerResponse<PayInfo> pay(Integer userId,Long orderNo, String password);
}
