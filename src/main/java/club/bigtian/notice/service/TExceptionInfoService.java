package club.bigtian.notice.service;

import club.bigtian.notice.domain.TExceptionInfo;
import club.bigtian.notice.domain.dto.PageListDto;

import java.util.List;
import java.util.Map;

/**
 * @author bigtian
 */
public interface TExceptionInfoService {


    int deleteByPrimaryKey(Long id);

    int insert(TExceptionInfo record);

    int insertSelective(TExceptionInfo record);

    TExceptionInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TExceptionInfo record);

    int updateByPrimaryKey(TExceptionInfo record);

    List<TExceptionInfo> list(PageListDto dto);

    Long count(PageListDto dto);

    Map<Object, Object> groupCount();


}
