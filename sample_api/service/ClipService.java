package com.dalbit.clip.service;

import com.dalbit.clip.dao.ClipDao;
import com.dalbit.clip.vo.*;
import com.dalbit.clip.vo.procedure.*;
import com.dalbit.clip.vo.request.ClipGiftRankTop3Vo;
import com.dalbit.clip.vo.request.ClipMainSubjectTop3Vo;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.main.service.MainService;
import com.dalbit.main.vo.BannerVo;
import com.dalbit.member.dao.MemberDao;
import com.dalbit.member.service.MypageService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.clip.vo.procedure.P_FolderAddVo;
import com.dalbit.clip.vo.procedure.P_FolderDeleteVo;
import com.dalbit.clip.vo.procedure.P_FolderEditVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.IPUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Service
public class ClipService {
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    ClipDao clipDao;
    @Autowired
    RestService restService;
    @Autowired
    CommonService commonService;
    @Autowired
    MypageService mypageService;
    @Autowired
    MemberDao memberDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    IPUtil ipUtil;
    @Autowired
    MainService mainService;

    @Value("${item.direct.code}")
    private String[] ITEM_DIRECT_CODE;
    @Value("${item.direct.min}")
    private int[] ITEM_DIRECT_MIN;
    @Value("${item.direct.max}")
    private int[] ITEM_DIRECT_MAX;
    @Value("${item.direct.code.main}")
    private String ITEM_DIRECT_CODE_MAIN;

    /**
     * 주간 클립 테이블
     */
    public String callClipRecommendList(P_ClipRecommendListInputVo pClipRecommendListInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pClipRecommendListInputVo);
        List<P_ClipRecommendListOutputVo> listVo = clipDao.callClipRecommendList(procedureVo);
        ProcedureVo procedureVo1 = new ProcedureVo();
        List<P_ClipRecommendLeaderListVo> leaderListVo = clipDao.callClipRecommendLeaderList(procedureVo1);
        HashMap resultMap = new HashMap();
        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            List<ClipRecommendListOuputVo> outVoList = new ArrayList<>();
            List<ClipRecommendLeaderListVo> leaderList = new ArrayList<>();
            if(!DalbitUtil.isEmpty(listVo)) {
                for(int i=0; i<listVo.size(); i++) {
                    outVoList.add(new ClipRecommendListOuputVo(listVo.get(i)));
                }
            }
            String minDate="";
            if(!DalbitUtil.isEmpty(leaderListVo) && Integer.parseInt(procedureVo1.getRet()) > 0) {
                for(int i=0; i<leaderListVo.size(); i++) {
                    leaderList.add(new ClipRecommendLeaderListVo(leaderListVo.get(i)));
                    minDate = leaderList.get(i).getRecDate();
                }
            }

            P_ClipRecommendInfoVo infoVo = new Gson().fromJson(procedureVo.getExt(), P_ClipRecommendInfoVo.class);
            ClipRecommendInfoVo recommendInfo = new ClipRecommendInfoVo(infoVo);
            Collections.reverse(leaderList);
            resultMap.put("recommendInfo", recommendInfo);
            resultMap.put("list", outVoList);
            resultMap.put("leaderList", leaderList);
            resultMap.put("minRecDate", minDate);

            result = gsonUtil.toJson(new JsonOutputVo(Status.달대리추천클립조회_성공, resultMap));
        } else if(procedureVo.getRet().equals(Status.달대리추천클립조회_회원아님.getMessageCode())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.달대리추천클립조회_회원아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.달대리추천클립조회_실패));
        }
        return result;
    }
}
