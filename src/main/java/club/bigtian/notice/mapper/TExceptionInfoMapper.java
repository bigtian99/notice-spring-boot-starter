package club.bigtian.notice.mapper;

import club.bigtian.notice.domain.TExceptionInfo;
import club.bigtian.notice.domain.dto.PageListDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
    * @Description: ${description}
    * @author bigtian
    * @date 2022/9/215:22
    */
@Mapper
public interface TExceptionInfoMapper {
    /**
     * delete by primary key
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Long id);

    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(TExceptionInfo record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(TExceptionInfo record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    TExceptionInfo selectByPrimaryKey(Long id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TExceptionInfo record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TExceptionInfo record);

       List<TExceptionInfo> list(PageListDto dto);

    Long count(PageListDto dto);

    List<Map<String, Object>> groupCount();

}