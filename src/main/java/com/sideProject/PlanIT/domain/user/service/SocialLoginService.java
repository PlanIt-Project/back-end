package com.sideProject.PlanIT.domain.user.service;

import com.sideProject.PlanIT.common.util.JwtTokenProvider;
import com.sideProject.PlanIT.domain.user.dto.member.response.JwtResponseDto;
import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.user.entity.enums.MemberRole;
import com.sideProject.PlanIT.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginService {

    // Kakao 정보
    @Value("${naver.client-id}")
    private String NAVER_CLIENT_ID;
    @Value("${naver.client-secret}")
    private String NAVER_CLIENT_SECRET;
    @Value("${naver.login-uri}")
    private String NAVER_LOGIN_URI;
    @Value("${naver.redirect-uri}")
    private String NAVER_REDIRECT_URI;

    // Google 정보
    @Value("${google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${google.login-uri}")
    private String GOOGLE_LOGIN_URI;
    @Value("${google.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;

    private final static String NAVER_API_URI = "https://openapi.naver.com";
    private final static String NAVER_AUTH_URI = "https://nid.naver.com";

    private final static String GOOGLE_AUTH_URI = "https://oauth2.googleapis.com";
    private final static String GOOGLE_API_URI = "https://www.googleapis.com";

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String getNaverLoginURI() {
        return NAVER_LOGIN_URI +
                "reponse_type=code" +
                "&client_id=" + NAVER_CLIENT_ID +
                "&redirect_uri=" + NAVER_REDIRECT_URI;
    }

    public String getGoogleLoginURI() {
        return GOOGLE_LOGIN_URI +
                "client_id=" + GOOGLE_CLIENT_ID +
                "&redirect_uri=" + GOOGLE_REDIRECT_URI +
                "&response_type=code" +
                "&scope=email profile";
    }

    public JwtResponseDto naverSocialLogin(String code) throws Exception {
        Member loginMember = getNaverUserInfoWithToken(getTokenToNaver(code));
        return checkSocialLoginMember(loginMember);
    }

    public JwtResponseDto googleSocialLogin(String code) throws Exception {
        Member loginMember = getGoogleUserInfoWithToken(getTokenToGoogle(code));
        return checkSocialLoginMember(loginMember);
    }

    private String getTokenToNaver(String code) throws Exception {
        if (code == null) throw new Exception("Failed get authorization code");

        String accessToken;
        String refreshToken;

        try {
            HttpHeaders headers = new HttpHeaders();

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", NAVER_CLIENT_ID);
            params.add("client_secret", NAVER_CLIENT_SECRET);
            params.add("code", code);
            params.add("redirect_uri", NAVER_REDIRECT_URI);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    NAVER_AUTH_URI + "/oauth2.0/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());

            accessToken = (String) jsonObject.get("access_token");
            refreshToken = (String) jsonObject.get("refresh_token");
        } catch (Exception e) {
            throw new Exception("API call failed");
        }
        return accessToken;
    }

    private String getTokenToGoogle(String code) throws Exception {
        if (code == null) throw new Exception("Failed get authorization code");

        String accessToken;
        String refreshToken;

        try {
            HttpHeaders headers = new HttpHeaders();

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", GOOGLE_CLIENT_ID);
            params.add("client_secret", GOOGLE_CLIENT_SECRET);
            params.add("code", code);
            params.add("redirect_uri", GOOGLE_REDIRECT_URI);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    GOOGLE_AUTH_URI + "/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());

            accessToken = (String) jsonObject.get("access_token");
            refreshToken = (String) jsonObject.get("refresh_token");
        } catch (Exception e) {
            throw new Exception("API call failed");
        }
        log.info(accessToken);
        return accessToken;
    }

    private Member getNaverUserInfoWithToken(String accessToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                NAVER_API_URI + "/v1/nid/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj    = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject account = (JSONObject) jsonObj.get("response");

        String id = String.valueOf(account.get("id"));
        String email = String.valueOf(account.get("email"));
        String name = String.valueOf(account.get("name"));
        String phone_number = String.valueOf(account.get("mobile"));
        String birth = String.join("-", String.valueOf(account.get("birthyear")), String.valueOf(account.get("birthday")));

        return Member.builder()
                .email(email)
                .name(name)
                .phone_number(phone_number)
                .birth(LocalDate.parse(birth))
                .build();
    }

    private Member getGoogleUserInfoWithToken(String accessToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                GOOGLE_API_URI + "/oauth2/v1/userinfo?access_token=" + accessToken,
                HttpMethod.GET,
                httpEntity,
                String.class
        );

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj    = (JSONObject) jsonParser.parse(response.getBody());

        String email = String.valueOf(jsonObj.get("email"));
        String name = String.valueOf(jsonObj.get("name"));

        log.info(email);
        log.info(name);
        return Member.builder()
                .email(email)
                .name(name)
                .build();
    }

    private JwtResponseDto checkSocialLoginMember(Member member) {
        Member socialLoginMember = memberRepository.findByEmail(member.getEmail()).orElse(Member.builder()
                        .email(member.getEmail())
                        .name(member.getName())
                        .phone_number(member.getPhone_number())
                        .birth(member.getBirth())
                        .role(MemberRole.MEMBER)
                .build());

        // 최초 소셜 로그인
        Member tokenMember = memberRepository.save(socialLoginMember);

        return JwtResponseDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(tokenMember))
                .refreshToken(jwtTokenProvider.createRefreshToken(tokenMember))
                .build();
    }
}
