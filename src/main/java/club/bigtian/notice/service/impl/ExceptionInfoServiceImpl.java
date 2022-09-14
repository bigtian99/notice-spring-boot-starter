package club.bigtian.notice.service.impl;

import club.bigtian.notice.domain.TExceptionInfo;
import club.bigtian.notice.domain.dto.PageListDto;
import club.bigtian.notice.mapper.TExceptionInfoMapper;
import club.bigtian.notice.service.ExceptionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author bigtian
 */
@Service
public class ExceptionInfoServiceImpl implements ExceptionInfoService {

    @Autowired
    private TExceptionInfoMapper tExceptionInfoMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return tExceptionInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(TExceptionInfo record) {
        return tExceptionInfoMapper.insert(record);
    }

    @Override
    public int insertSelective(TExceptionInfo record) {
        return tExceptionInfoMapper.insertSelective(record);
    }

    @Override
    public TExceptionInfo selectByPrimaryKey(Long id) {
        return tExceptionInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(TExceptionInfo record) {
        return tExceptionInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TExceptionInfo record) {
        return tExceptionInfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<TExceptionInfo> list(PageListDto dto) {
        dto.setPage((dto.getPage() - 1) * dto.getLimit());
        return tExceptionInfoMapper.list(dto);
    }

    @Override
    public Long count(PageListDto dto) {
        return tExceptionInfoMapper.count(dto);
    }

    @Override
    public Map<Object, Object> groupCount() {

        return tExceptionInfoMapper.groupCount()
                .stream()
                .collect(Collectors.toMap(el -> el.get("status"), el -> el.get("count"), (el, el1) -> el));
    }

}
