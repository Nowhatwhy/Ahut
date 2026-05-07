package org.nowhatwhy.ahut.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.nowhatwhy.ahut.dto.ChargeDTO;
import org.nowhatwhy.ahut.entity.Charge;
import org.nowhatwhy.ahut.entity.ChargeRecord;
import org.nowhatwhy.ahut.mapper.ChargeRecordMapper;
import org.nowhatwhy.ahut.service.IChargeRecordService;
import org.nowhatwhy.ahut.utils.AhutLogin;
import org.nowhatwhy.ahut.utils.UserHolder;
import org.nowhatwhy.ahut.vo.ChargeRecordVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChargeRecordServiceImpl extends ServiceImpl<ChargeRecordMapper, ChargeRecord> implements IChargeRecordService {
    @Value("${ahut.USER_AGENT}")
    private String userAgent;
    @Value("${ahut.CHARGE_URL}")
    private String chargeUrl;
    private final ChargeRecordMapper chargeRecordMapper;
    private final AhutLogin ahutLogin;
    @Scheduled(cron = "0 0 21 * * ?")
    public void updateChargeRecords() {
        OkHttpClient client = ahutLogin.login();
        List<ChargeRecord> chargeRecords = listChargeRecordsFromAhut(client);
        if (!chargeRecords.isEmpty()) {
            chargeRecordMapper.saveChargeRecords(chargeRecords);
        }
    }
    public List<ChargeRecord> listChargeRecordsFromAhut(OkHttpClient client) {
        List<ChargeDTO> chargeDTOList = chargeRecordMapper.listChargeDTO();
        // IO 密集任务，使用线程池执行
        ExecutorService pool = new ThreadPoolExecutor(
                16,
                32,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        List<Future<ChargeRecord>> futures = chargeDTOList.stream()
                .map(dto -> pool.submit(() -> {
                    Charge charge = getChargeFromAhut(dto, client);
                    if (charge == null) {
                        return null;
                    }
                    return ChargeRecord.builder()
                            .dormId(charge.getDormId())
                            .balance(charge.getRemainingBalance())
                            .recordDate(LocalDate.now())
                            .build();
                }))
                .toList();

        return futures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        log.error("获取结果失败", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }
    public Charge getChargeFromAhut(ChargeDTO chargeDTO, OkHttpClient client) {
        try {
            log.info("开始查询电费");
            Request request2 = new Request.Builder()
                    .url(chargeUrl)
                    .header("User-Agent", userAgent)
                    .post(new FormBody.Builder()
                            .add("xiaoqu", chargeDTO.getCampus())
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
                    throw new RuntimeException(chargeDTO + msg);
                }
                JSONObject data = json.getJSONObject("Data");
                log.info("查询电费成功: {}", data);
                Charge charge = new Charge();
                charge.setRoomId(data.getString("room_id"));
                charge.setAllBalance(data.getBigDecimal("AllAmp"));
                charge.setRemainingBalance(data.getBigDecimal("RemainAmp"));
                charge.setUsedBalance(data.getBigDecimal("UsedAmp"));
                charge.setDormId(chargeDTO.getDormId());
                return charge;
            }
        } catch (RuntimeException e) {
            log.warn("查询电费失败{}", e.getMessage());
            return null;
        } catch (Exception e){
            log.error("查询电费异常", e);
            return null;
        }
    }

    @Override
    public List<ChargeRecordVO> listCharges() {
        Long userId = UserHolder.get().getId();
        return chargeRecordMapper.listChargeRecords(userId);
    }
}
