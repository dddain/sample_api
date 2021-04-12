package com.dalbit.clip.dao;

import com.dalbit.clip.vo.procedure.*;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ClipDao {

    @Transactional(readOnly = true)
    List<P_ClipRecommendListOutputVo> callClipRecommendList(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_ClipRecommendLeaderListVo> callClipRecommendLeaderList(ProcedureVo procedureVo);
    
}
