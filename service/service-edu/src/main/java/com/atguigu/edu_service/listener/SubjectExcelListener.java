package com.atguigu.edu_service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.edu_service.entity.EduSubject;
import com.atguigu.edu_service.entity.vo.ExcelSubjectData;
import com.atguigu.edu_service.service.EduSubjectService;
import com.atguigu.edu_service.service.impl.EduSubjectServiceImpl;
import com.atguigu.common_utils.exception.GuLiException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Author Administrator
 * @CreateTime 2020-11-23
 * @Description
 */
public class SubjectExcelListener extends AnalysisEventListener<ExcelSubjectData> {

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 6;
//    List<EduSubject> list = new ArrayList<EduSubject>();

    private EduSubjectService eduSubjectService;

    public SubjectExcelListener() {
        eduSubjectService = new EduSubjectServiceImpl();
    }

    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(ExcelSubjectData user, AnalysisContext analysisContext) {
        if(user == null) {
            throw new GuLiException(20001,"添加失败");
        }

        System.out.println("************************");
        System.out.println(user);
        System.out.println("************************");

        // 添加一级分类
        // 先判断数据库中是否有此一级分类
        if (StringUtils.isEmpty(user.getOneSubjectName())) {
            return;
        }
        EduSubject oneSubject = this.existOneSubject(eduSubjectService, user.getOneSubjectName());
        if (oneSubject == null) {
            // 数据库中没有此一级分类，可添加到数据库
            oneSubject = new EduSubject();
            oneSubject.setTitle(user.getOneSubjectName());
            oneSubject.setParentId("0");
            eduSubjectService.save(oneSubject);
        }

        if (StringUtils.isEmpty(user.getTwoSubjectName())) {
            return;
        }

        // 获取一级分类id
        String pid = oneSubject.getId();

        // 添加二级分类
        // 先判断数据库中是否有此二级分类
        EduSubject twoSubject = this.existTwoSubject(eduSubjectService, pid, user.getTwoSubjectName());
        if (twoSubject == null) {
            // 数据库中没有此二级分类，可添加到数据库
            twoSubject = new EduSubject();
            twoSubject.setTitle(user.getTwoSubjectName());
            twoSubject.setParentId(pid);
            eduSubjectService.save(twoSubject);
        }

        // 达到BATCH_COUNT了，需要去存储一次数据库，防止几万条数据在内存，容易OOM（内存不足）
//        if (list.size() >= BATCH_COUNT) {
//            saveData();
//            // 存储完成清理 list
//            list.clear();
//        }

    }

    private EduSubject existTwoSubject(EduSubjectService eduSubjectService, String pid, String twoSubjectName) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", twoSubjectName).eq("parent_id", pid);
        return eduSubjectService.getOne(wrapper);
    }

    /**
     * 判断数据库中一级分类是否已经存在
     */
    private EduSubject existOneSubject(EduSubjectService eduSubjectService, String oneSubjectName) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", oneSubjectName).eq("parent_id", 0);
        return eduSubjectService.getOne(wrapper);
    }

    /**
     * 读取表头信息
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        super.invokeHeadMap(headMap, context);
        System.out.println("表头信息: " + headMap);
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
//        saveData();
    }

    /**
     * 批量存储到数据库
     */
//    private void saveData() {
//        eduSubjectService.saveBatch(list);
//    }
}
