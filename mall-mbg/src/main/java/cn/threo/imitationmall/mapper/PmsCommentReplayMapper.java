package cn.threo.imitationmall.mapper;

import cn.threo.imitationmall.model.PmsCommentReplay;
import cn.threo.imitationmall.model.PmsCommentReplayExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsCommentReplayMapper {
    int countByExample(PmsCommentReplayExample example);

    int deleteByExample(PmsCommentReplayExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsCommentReplay record);

    int insertSelective(PmsCommentReplay record);

    List<PmsCommentReplay> selectByExample(PmsCommentReplayExample example);

    PmsCommentReplay selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PmsCommentReplay record, @Param("example") PmsCommentReplayExample example);

    int updateByExample(@Param("record") PmsCommentReplay record, @Param("example") PmsCommentReplayExample example);

    int updateByPrimaryKeySelective(PmsCommentReplay record);

    int updateByPrimaryKey(PmsCommentReplay record);
}