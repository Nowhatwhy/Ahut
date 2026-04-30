package org.nowhatwhy.ahut.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.nowhatwhy.ahut.constant.ErrorCode;
import org.nowhatwhy.ahut.dto.ChargeDTO;
import org.nowhatwhy.ahut.entity.Charge;
import org.nowhatwhy.ahut.entity.ChargeRecord;
import org.nowhatwhy.ahut.exception.BusinessException;
import org.nowhatwhy.ahut.mapper.ChargeRecordMapper;
import org.nowhatwhy.ahut.service.IChargeRecordService;
import org.nowhatwhy.ahut.utils.AhutLogin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChargeRecordServiceImpl extends ServiceImpl<ChargeRecordMapper, ChargeRecord> implements IChargeRecordService {
    @Value("${ahut.USER_AGENT}")
    private String userAgent;
    @Value("${ahut.CHARGE_URL}")
    private String chargeUrl;

    private final AhutLogin ahutLogin;
    @Override
    public Charge queryCharge(ChargeDTO chargeDTO) {
        OkHttpClient client = ahutLogin.login();
        try {
            log.info("开始查询电费");
            Request request2 = new Request.Builder()
                    .url(chargeUrl)
                    .header("User-Agent", userAgent)
                    .post(new FormBody.Builder()
                            .add("xiaoqu", chargeDTO.getCampus())
                            .add("ld_Name", chargeDTO.getBuildingName())
                            .add("ld_Id", chargeDTO.getBuildingId())
                            .add("Room_No", chargeDTO.getRoomNo())
                            .add("etype", chargeDTO.getElectricityType())
                            .build())
                    .build();
            try (Response response2 = client.newCall(request2).execute()) {
                String body = response2.body() == null ? "" : response2.body().string();
                if (!response2.isSuccessful()) {
                    throw new RuntimeException("查询电费请求失败，HTTP状态码：" + response2.code());
                }
                JSONObject json = JSON.parseObject(body);
                Integer code = json.getInteger("Code");
                if (code == null || code != 0) {
                    String msg = json.getString("Msg");
                    throw new RuntimeException("查询电费失败：" + msg);
                }
                JSONObject data = json.getJSONObject("Data");
                log.info("查询电费成功: {}", data);
                Charge charge = new Charge();
                charge.setRoomId(data.getString("room_id"));
                charge.setAllBalance(data.getDouble("AllAmp"));
                charge.setRemainingBalance(data.getDouble("RemainAmp"));
                charge.setUsedBalance(data.getDouble("UsedAmp"));
                return charge;
            }
        } catch (Exception e) {
            log.error("查询电费失败", e);
            throw new BusinessException(ErrorCode.CHARGE_QUERY_FAIL, e.getMessage());
        }
    }
}
