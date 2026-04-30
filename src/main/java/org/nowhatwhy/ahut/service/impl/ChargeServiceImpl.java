package org.nowhatwhy.ahut.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.nowhatwhy.ahut.constant.ErrorCode;
import org.nowhatwhy.ahut.dto.ChargeDTO;
import org.nowhatwhy.ahut.entity.Building;
import org.nowhatwhy.ahut.entity.Charge;
import org.nowhatwhy.ahut.exception.BusinessException;
import org.nowhatwhy.ahut.service.IChargeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class ChargeServiceImpl implements IChargeService {
    @Value("${ahut.LOGIN_URL}")
    private String loginUrl;
    @Value("${ahut.LOGIN_PAGE_URL}")
    private String loginPageUrl;
    @Value("${ahut.PUBLIC_KEY}")
    private String publicKey;
    @Value("${ahut.USERNAME}")
    private String userName;
    @Value("${ahut.PASSWORD}")
    private String passWord;
    @Value("${ahut.USER_AGENT}")
    private String userAgent;
    @Value("${ahut.CHARGE_URL}")
    private String chargeUrl;
    @Value("${ahut.BUILDINGS_URL}")
    private String buildingsUrl;
    @Override
    public Charge queryCharge(ChargeDTO chargeDTO) {
        OkHttpClient client = login();
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

    @Override
    public List<Building> queryBuildings(String name) {
        OkHttpClient client = login();
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
        } catch (Exception e) {
            log.error("查询宿舍失败", e);
            throw new BusinessException(ErrorCode.BUILDING_INFO_ERROR, e.getMessage());
        }
    }
    private OkHttpClient login() {
        log.info("开始登录");
        try {
            Thread.sleep(1000);
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            OkHttpClient client = new OkHttpClient.Builder()
                    .cookieJar(new JavaNetCookieJar(cookieManager))
                    .build();

            // 1.访问登录页
            Request request = new Request.Builder()
                    .url(loginPageUrl)
                    .header("User-Agent", userAgent)
                    .header("Referer", loginPageUrl)
                    .header("Origin", "https://pay.ahut.edu.cn")
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("登录页面请求失败");
                }
            }

            // 2.登录
            Request request1 = new Request.Builder()
                    .url(loginUrl)
                    .header("User-Agent", userAgent)
                    .header("Referer", loginPageUrl)
                    .header("Origin", "https://pay.ahut.edu.cn")
                    .post(new FormBody.Builder()
                            .add("username", userName)
                            .add("pwd", encryptPassword(passWord))
                            .build())
                    .build();

            try (Response response1 = client.newCall(request1).execute()) {
                String body = response1.body() == null ? "" : response1.body().string();

                if (!response1.isSuccessful()) {
                    throw new RuntimeException("登录请求失败");
                }

                JSONObject json = JSON.parseObject(body);
                Integer code = json.getInteger("Code");

                if (code == null || code != 0) {
                    throw new RuntimeException(json.getString("Msg"));
                }
            }

            log.info("登录成功");
            return client;

        } catch (Exception e) {
            log.error("登录失败", e);
            throw new BusinessException(ErrorCode.LOGIN_FAILED, "登录失败：" + e.getMessage());
        }
    }

    private String encryptPassword(String password) throws Exception {
        // 1. 先 Base64 编码密码
        String base64Pwd = Base64.getEncoder()
                .encodeToString(password.getBytes(StandardCharsets.UTF_8));

        // 2. 解析 RSA 公钥
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        PublicKey rsaPublicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);

        // 3. RSA/PKCS1Padding 加密
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);

        byte[] encryptedBytes = cipher.doFinal(base64Pwd.getBytes(StandardCharsets.UTF_8));

        // 4. 加密结果再 Base64
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}
