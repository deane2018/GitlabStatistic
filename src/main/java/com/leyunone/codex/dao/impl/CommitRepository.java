package com.leyunone.codex.dao.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.codex.dao.CommitDao;
import com.leyunone.codex.dao.entry.Commit;
import com.leyunone.codex.dao.mapper.CommitMapper;
import com.leyunone.codex.model.bo.CommitBO;
import com.leyunone.codex.model.query.CodeTimeQuery;
import com.leyunone.codex.model.query.CommitQuery;
import com.leyunone.codex.model.vo.CommitVO;
import com.leyunone.codex.model.vo.UserVO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class CommitRepository extends BaseRepository<CommitMapper, Commit> implements CommitDao {

    @Override
    public Page<Commit> selectByPage(CommitQuery commitQuery) {
        Page<Commit> page = new Page(commitQuery.getIndex(), commitQuery.getSize());
        LambdaQueryWrapper<Commit> lambda = new QueryWrapper<Commit>().lambda();
        lambda.eq(StringUtils.isNotBlank(commitQuery.getCommitterName()), Commit::getCommitterName, commitQuery.getCommitterName());
        lambda.eq(ObjectUtil.isNotNull(commitQuery.getProjectId()), Commit::getProjectId, commitQuery.getProjectId());
        lambda.ge(StringUtils.isNotBlank(commitQuery.getStartDate()), Commit::getCommitDate, commitQuery.getStartDate());
        lambda.le(StringUtils.isNotBlank(commitQuery.getEndDate()), Commit::getCommitDate, commitQuery.getEndDate());
        lambda.orderByDesc(Commit::getCommitDate);
        return this.baseMapper.selectPage(page, lambda);
    }

    @Override
    public List<UserVO> selectSumGroupUser(String startDate, String endDate) {
        return this.baseMapper.selectSumGroupUser(startDate, endDate);
    }

    @Override
    public void saveBatch(List<CommitBO> commits) {
        this.baseMapper.batchInsert(commits);
    }

    @Override
    public Date selectLastDate(String url) {
        return this.baseMapper.selectLastDate(url);
    }

    @Override
    public List<CommitVO> selectProjectCodeGroupUser(CodeTimeQuery query) {
        return this.baseMapper.selectProjectCodeGroupUser(query);
    }

    @Override
    public List<String> preDate(String date) {
        return this.baseMapper.preDate(date);
    }
}
