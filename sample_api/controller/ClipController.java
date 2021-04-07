package com.dalbit.clip.controller;

import com.dalbit.clip.service.ClipService;
import com.dalbit.clip.vo.procedure.*;
import com.dalbit.clip.vo.request.*;
import com.dalbit.common.service.CommonService;
import com.dalbit.exception.GlobalException;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/clip")
public class SampleController {

    @Autowired
    ClipService clipService;
    @Autowired
    CommonService commonService;
    @Autowired
    GsonUtil gsonUtil;

    /**
     * 주간 클립 테이블
     */
    @GetMapping("/recommend/list")
    public String clipRecommendList(@Valid ClipRecommendListInputVo clipRecommendListInputVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ClipRecommendListInputVo apiData = new P_ClipRecommendListInputVo(clipRecommendListInputVo, request);
        String result = clipService.callClipRecommendList(apiData);
        return result;
    }
}