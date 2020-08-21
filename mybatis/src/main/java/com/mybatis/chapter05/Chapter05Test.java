package com.mybatis.chapter05;

import com.mybatis.chapter05.mapper.*;
import com.mybatis.chapter05.model.*;
import com.mybatis.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Chapter05Test {
    //演示一个全表关联的查询
    @Test
    public void test1(){
        EmployeeMapper mapper = SqlSessionFactoryUtils.getMapper(EmployeeMapper.class, "com/mybatis/chapter05/mapper/mybatis-config.xml");
        Employee employee = mapper.getEmployeeById(1);
        //测试中发现taskList与healthForm总是空---
        //EmployeeTaskMap的查询指定了resultType导致级联属性不能初始化
        //healthForm空的问题：数据错误，在male_healthform中存性别为female的员工数据，员工ID错误
        System.out.println(employee.getId());
        //((MaleEmployee)employee).getMaleHealthForm();
        //employee.getWorkCard();
        employee.getTaskList().get(0).toString();
    }
    @Test
    public void testAllInOneResult(){
        EmployeeMapper mapper = SqlSessionFactoryUtils.getMapper(EmployeeMapper.class, "com/mybatis/chapter05/mapper/mybatis-config.xml");
        Employee employee = mapper.getEmployeeInfoById(1);
        System.out.println(employee.getTaskList());
    }

    @Test
    public void test2(){
        EmployeeTaskMapper mapper = SqlSessionFactoryUtils.getMapper(EmployeeTaskMapper.class,"com/mybatis/chapter05/mapper/mybatis-config.xml");
        List<EmployeeTask> taskList = mapper.getEmpTaskByEmpId(1);
        System.out.println(taskList.size());
        System.out.println(taskList.get(0).getId());
        System.out.println(taskList.get(0).getTask().toString());
    }

    @Test
    public void test42(){
        WorkCardMapper workCardMapper = SqlSessionFactoryUtils.getMapper2(WorkCardMapper.class,"jdbc.properties",true);
        WorkCard workCard = workCardMapper.getWorkCardByEmpId(12);
        System.out.println(workCard.getDepartment());
    }

    /** -=-=-=-=-=- 专题研究 业务操作中常见的不存在就新增一条，在并发操作时发生重复插入记录的问题，如何从技术上解决？ =-=-=-=-= **/
    /**
     create table work_card (
     id         int auto_increment primary key,
     emp_id     int         null,
     real_name  varchar(20) null,
     department varchar(20) null,
     constraint work_card_emp_id_uindex
     unique (emp_id)
     );
     */
    static class InsertBeforeQueryTask implements Runnable{
        private String configFileRPath;
        private String jdbcFileRPath;
        private Integer empId;

        public InsertBeforeQueryTask(String configFileRPath, String jdbcFileRPath, Integer empId) {
            this.configFileRPath = configFileRPath;
            this.jdbcFileRPath = jdbcFileRPath;
            this.empId = empId;
        }

        @Override
        public void run() {
            //WorkCardMapper workCardMapper = SqlSessionFactoryUtils.getMapper2(WorkCardMapper.class,jdbcFileRPath);
            WorkCardMapper workCardMapper = SqlSessionFactoryUtils.getMapper(WorkCardMapper.class,configFileRPath);
            //查询是否存在
            WorkCard workCard = workCardMapper.getWorkCardByEmpId(empId);
            if (workCard == null){
                workCard = new WorkCard();
                workCard.setEmpId(empId);
                workCard.setRealName("lucy");
                workCard.setDepartment("tianqin");
                //不存在就初始化
                String currentThreadName = Thread.currentThread().getName();
                System.out.println(currentThreadName+" 开始执行初始化操作");
                try {
                    //workCardMapper.simpleInsert(workCard); //A方案
                    workCardMapper.insertIfNotExists(workCard);//B方案
                    //workCardMapper.insertOnDuplicateKeyUpdate(workCard);//C方案
                }catch (Exception e){
                    System.out.println(currentThreadName + " 操作发生异常："+ e.getMessage());
                }
                System.out.println(workCard.getId());
            }
        }
        //怀疑B方案是不是使用姿势不对,此处在没数据时仍然会抛出死锁异常，调整事务隔离级别也没有效果，不建议使用
        static class InsertDeliberatelyTask implements Runnable{
            private String configFileRPath;
            private String jdbcFileRPath;
            private Integer empId;

            public InsertDeliberatelyTask(String configFileRPath, String jdbcFileRPath, Integer empId) {
                this.configFileRPath = configFileRPath;
                this.jdbcFileRPath = jdbcFileRPath;
                this.empId = empId;
            }

            @Override
            public void run() {
                //WorkCardMapper workCardMapper = SqlSessionFactoryUtils.getMapper2(WorkCardMapper.class,jdbcFileRPath);
                WorkCardMapper workCardMapper = SqlSessionFactoryUtils.getMapper(WorkCardMapper.class,configFileRPath);
                WorkCard workCard = new WorkCard();
                workCard.setEmpId(empId);
                workCard.setRealName("lucy");
                workCard.setDepartment("dust9");
                try {
                    workCardMapper.insertIfNotExists(workCard);
                }catch (Exception e){
                    System.out.println(Thread.currentThread().getName()+" 插入操作发生异常："+e.getMessage());
                }
            }
        }

        /**
         * C方案 使用悲观锁，很遗憾，悲观所只对更新操作有效果，连查都查不到没法落锁，不适合查不到就插入一条的场景
         */
        static class PessimisticLockTask implements Runnable{
            private String configFileRPath;
            private String jdbcFileRPath;
            private Integer empId;

            public PessimisticLockTask(String configFileRPath, String jdbcFileRPath, Integer empId) {
                this.configFileRPath = configFileRPath;
                this.jdbcFileRPath = jdbcFileRPath;
                this.empId = empId;
            }
            @Override
            public void run() {
                SqlSessionFactory sqlSessionFact = SqlSessionFactoryUtils.getSqlSessionFactory2(jdbcFileRPath, false);
                try {
                    SqlSession sqlSession = sqlSessionFact.openSession();
                    WorkCardMapper workCardMapper = sqlSession.getMapper(WorkCardMapper.class);
                    //insertIfNotExist(workCardMapper);
                    selectAndUpdateStatusCode(workCardMapper);
                    sqlSession.commit();
                }catch (Exception e){
                    System.out.println(Thread.currentThread().getName()+" 操作发生异常："+e.getMessage());
                }
            }
            //悲观锁对不存在就插入的情况无解
            private void insertIfNotExist(WorkCardMapper workCardMapper){
                WorkCard workCard = workCardMapper.getByPessimisticId(empId);
                if (workCard == null){
                    workCard = new WorkCard();
                    workCard.setEmpId(empId);
                    workCard.setRealName("lucy");
                    workCard.setDepartment("dust9");
                }
                int i = workCardMapper.simpleInsert(workCard);
                System.out.println(Thread.currentThread().getName()+" 操作完成准备提交："+i+", 返回主键 = "+workCard.getId());
            }
            //有两个人事同时操作了员工的work_card,emp_id是稀有关联资源，需要在一条记录里记录使用此emp_id的历史人员姓名，但department只需要记录当前人员的部门名称
            //推而广之，依赖status_code进行业务判断和操作的也必然存在并发问题
            private void selectAndUpdateStatusCode(WorkCardMapper workCardMapper){
                Long start = System.currentTimeMillis();
                WorkCard workCard = workCardMapper.getByOptimisticEmpIdAndDepartName(12,"tianqin");
                System.out.println(Thread.currentThread().getName() + " 查询耗时："+ (System.currentTimeMillis() - start));
                if (workCard != null){
                    //确认部门信息,进行一些操作
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    workCard.setRealName("daniel");
                    workCard.setDepartment("tianyu");
                    workCardMapper.appendRealNameAndUpdateDepartById(workCard);
                }
            }
            //两个人事操作不同业务更新了同一条数据,时机难以掌握，但通过不同软件连接一个数据库可以模拟
        }
        //乐观锁使用version标记，这里研究的场景跟它扯不上关系。。。
        /**
         * D方案 java代码层面使用同步
         */
        static class CommonRecordLock implements Runnable{
            private String configFileRPath;
            private String jdbcFileRPath;
            private WorkCard workCard;

            public CommonRecordLock(String configFileRPath, String jdbcFileRPath, WorkCard workCard) {
                this.configFileRPath = configFileRPath;
                this.jdbcFileRPath = jdbcFileRPath;
                this.workCard = workCard;
            }

            private final Map<Integer,WorkCard> records = new ConcurrentHashMap();
            public synchronized void lock(WorkCard workCard){
                String threadName = Thread.currentThread().getName();
                while (records.containsKey(workCard.getEmpId())){
                    System.out.println(threadName+" waiting!");
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(threadName+" putting!");
                records.put(workCard.getEmpId(), workCard);
            }
            public synchronized void release(WorkCard workCard){
                System.out.println(Thread.currentThread().getName()+" notifying!");
                records.remove(workCard.getEmpId());
                notify();
            }

            @Override
            public void run() {
                String threadName = Thread.currentThread().getName();
                lock(this.workCard);
                try {
                    WorkCardMapper mapper = SqlSessionFactoryUtils.getMapper(WorkCardMapper.class, configFileRPath);
                    WorkCard workCard = mapper.getWorkCardByEmpId(this.workCard.getEmpId());
                    Thread.sleep(1000);//业务操作耗时
                    if (workCard != null){
                        this.workCard.setId(workCard.getId());
                        mapper.updateByPrimaryKey(this.workCard);
                        System.out.println(threadName+" updating!");
                    }else {
                        mapper.simpleInsert(this.workCard);
                        System.out.println(threadName+" inserting!");
                    }
                }catch (Exception e){
                    System.out.println(threadName + " 发生异常："+e.getMessage());
                }finally {
                    release(this.workCard);
                }
            }
        }
        //方案DD 简化版的同步锁控制
        static class SimpleBizOperationLock implements Runnable{
            private String configFileRPath;
            private String jdbcFileRPath;
            private WorkCard workCard;

            public SimpleBizOperationLock(String configFileRPath, String jdbcFileRPath, WorkCard workCard) {
                this.configFileRPath = configFileRPath;
                this.jdbcFileRPath = jdbcFileRPath;
                this.workCard = workCard;
            }
            public synchronized void selectInsertOrUpdate(){
                String threadName = Thread.currentThread().getName();
                WorkCardMapper mapper = SqlSessionFactoryUtils.getMapper(WorkCardMapper.class, configFileRPath);
                WorkCard workCard = mapper.getWorkCardByEmpId(this.workCard.getEmpId());
                if (workCard == null){
                    mapper.simpleInsert(this.workCard);
                    System.out.println(threadName + " inserting!");
                }else {
                    this.workCard.setId(workCard.getId());
                    mapper.updateByPrimaryKey(this.workCard);
                    System.out.println(threadName+ " updating!");
                }
            }
            @Override
            public void run() {
                selectInsertOrUpdate();
            }
        }
        public static void main(String[] args) {
            String configFileRPath = "com/mybatis/chapter05/mapper/mybatis-config.xml";
            String jdbcFileRPath = "jdbc.properties";
            Integer empId = 13;
            WorkCard workCard = new WorkCard(null,15,"alice","tianqin");

            InsertBeforeQueryTask oneQueryTask = new InsertBeforeQueryTask(configFileRPath,jdbcFileRPath,empId);
            InsertDeliberatelyTask deliberateTask = new InsertDeliberatelyTask(configFileRPath,jdbcFileRPath,empId);
            PessimisticLockTask pessimisticLockTask = new PessimisticLockTask(configFileRPath,jdbcFileRPath,empId);
            CommonRecordLock commonRecordLock = new CommonRecordLock(configFileRPath,jdbcFileRPath,workCard);
            SimpleBizOperationLock simpleBizOperationLock = new SimpleBizOperationLock(configFileRPath,jdbcFileRPath,workCard);

            Thread thread1 = new Thread(simpleBizOperationLock);thread1.setName("A线程");
            Thread thread2 = new Thread(simpleBizOperationLock);thread2.setName("B线程");
            thread1.start();
            thread2.start();
        }
        /**
         * 演示各种insert方式在并发场景下必然发生唯一索引冲突时的健壮性
         * A方式 无数据时会有一个线程抛出异常（是A是B不确定），正常插入一条数据。再次执行就是两个异常；
         * B方式 能够插入数据，使用getMapper方法：Deadlock found when trying to get lock; try restarting transaction，也有不发生死锁的情况；使用getMapper2方法：始终报唯一索引冲突
         * C方案 正常操作，只有一个线程能正常插入数据，且其他线程调用插入方法也不会抛出异常，当据说这种SQL在某些版本存在死锁的BUG
         * D方案 java代码层面的同步加锁完美解决：不存在就插入，存在就更新。日志情况：
         *  A线程 putting!
         *  B线程 waiting!
         *  A线程 inserting!
         *  A线程 notifying!
         *  B线程 putting!
         *  B线程 updating!
         *  B线程 notifying!
         * 整个过程没有异常抛出，数据正确插入。
         * DD方案效果一致
         */
    }

    /**-=-=-=-=-=-=-=-=-= 批量更新的3种方式 =-=-=-=-=-=-=-=*/
    /**
     * batch update方法，数据库连接配置必须带上&allowMultiQueries=true
     */
    List<WorkCard> workCards = new ArrayList<WorkCard>(){{
        add(new WorkCard(null, 2, null, "tianqin3"));
        add(new WorkCard(null, 45, null, "tianyu3"));
        add(new WorkCard(null,643,null,"tianzheng3"));
    }};
    @Test
    public void testBatchUpdate4BatUpdate() {
        WorkCardMapper workCardMapper = SqlSessionFactoryUtils.getMapper2(WorkCardMapper.class,"jdbc.properties",true);
        int affectedRows = workCardMapper.batchUpdate2BatUpdateWorkCards(workCards);
        System.out.println(affectedRows);
    }
    /**
     * case when写法 这种写法不受数据库连接配置的影响
     */
    @Test
    public void testCaseWhen4BatUpdate() {
        WorkCardMapper workCardMapper = SqlSessionFactoryUtils.getMapper2(WorkCardMapper.class,"jdbc.properties",true);
        int affectedRows = workCardMapper.caseWhen2BatUpdateWorkCards(workCards);
        System.out.println(affectedRows);
    }
    /**
     * join写法
     */
    @Test
    public void testJoin4BatUpdate() {
        WorkCardMapper workCardMapper = SqlSessionFactoryUtils.getMapper2(WorkCardMapper.class,"jdbc.properties",true);
        int affectedRows = workCardMapper.join2BatUpdateWorkCards2(workCards);
        System.out.println(affectedRows);
    }
    /**
     * 上述三种写法的性能对比：
     * JOIN > Batch Update > Case When ,更新数据量越大，差异越明显【未测试】
     */


}
