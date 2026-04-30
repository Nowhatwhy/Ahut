package org.nowhatwhy.ahut.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.nowhatwhy.ahut.constant.ErrorCode;
import org.nowhatwhy.ahut.entity.Building;
import org.nowhatwhy.ahut.exception.BusinessException;
import org.nowhatwhy.ahut.mapper.BuildingMapper;
import org.nowhatwhy.ahut.service.IBuildingService;
import org.nowhatwhy.ahut.utils.AhutLogin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements IBuildingService {
    @Value("${ahut.USER_AGENT}")
    private String userAgent;
    @Value("${ahut.BUILDINGS_URL}")
    private String buildingsUrl;

    private final AhutLogin ahutLogin;
    @Override
    public List<Building> queryBuildings(String name) {
        OkHttpClient client = ahutLogin.login();
        try {
            log.info("开始查询宿舍");
            Request request = new Request.Builder()
                    .url(buildingsUrl)
                    .header("User-Agent", userAgent)
                    .post(new FormBody.Builder()
                            .add("xiaoqu", name)
                            .build())
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String body = response.body() == null ? "" : response.body().string();
                if (!response.isSuccessful()) {
                    throw new RuntimeException("查询宿舍请求失败，HTTP状态码：" + response.code());
                }
                JSONObject json = JSON.parseObject(body);
                Integer code = json.getInteger("Code");
                if (code == null || code != 0) {
                    String msg = json.getString("Msg");
                    throw new RuntimeException("查询宿舍失败：" + msg);
                }
                log.info("查询宿舍成功: {}", json);
                JSONArray data = json.getJSONArray("Data");
                return data.stream().map(item -> {
                    JSONObject jsonObject = (JSONObject) item;
                    Building building = new Building();
                    building.setBuildingId(jsonObject.getString("Id"));
                    building.setBuildingName(jsonObject.getString("Name"));
                    return building;
                }).toList();
            }
        }catch (Exception e) {
            log.error("查询宿舍失败", e);
            throw new BusinessException(ErrorCode.BUILDING_INFO_ERROR, e.getMessage());
        }
    }
}
