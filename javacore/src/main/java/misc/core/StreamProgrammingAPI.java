package misc.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 流式API架构
 * interface AutoCloseable{void close()}
 * interface BaseStream<T, S extends BaseStream<T, S>> extends AutoCloseable
 *      提供iterator、spliterator、isParallel、sequential、parallel、unordered onClose(Runnable closeHandler) 方法
 * interface Stream<T> extends BaseStream<T, Stream<T>>
 *      提供 filter map mapToInt flatMap? flatMapToInt? distinct sorted peek? limit? skip? forEach toArray reduce collect min max count anyMatch? allMatch noneMatch findFirst? findAny? builder? empty of iterate? generate? concat
 * interface IntStream/LongStream extends BaseStream<Integer, IntStream>
 * 流式编程API案例展示
 * 对流排序不会修改数据源的顺序
 */
public class StreamProgrammingAPI {
    private static final Logger logger = getLogger(StreamProgrammingAPI.class);

    @Data
    @AllArgsConstructor
    public static class Student {
        public static Student DEFAULT = new Student("",0,0.0,"");
        private String name;
        private Integer number;
        private Double aDouble;
        private String city;

        public static Comparator NUMBER_COMPARATOR = new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.getNumber().compareTo(o2.getNumber());
            }
        };
        @Override
        public String toString() {
            return "Container{" +
                    "string='" + name + '\'' +
                    ", number=" + number +
                    ", aDouble=" + aDouble +
                    '}';
        }
    }
    static List<Student> data = new ArrayList<>();
    static {
        data.add(new Student("jack1",12,123.3,"shanghai"));
        data.add(new Student("java2",21,1.8,"beijing"));
        data.add(new Student("car3",1200,151.2,"newyork"));
        data.add(new Student("jinsi",134,45.6,"newyork"));
        data.add(new Student("soldier4", 160, 32.00,"shanghai"));
        data.add(new Student("carl", 7740, 3.0,"newyork"));
    }
    static class StreamOpsFeatures{
        public static void main(String[] args) {
            System.out.println(data); //E
            Stream<Student> stream = data.stream();
            Stream<Student> filteredStream = stream.filter(stu -> stu.getNumber() > 3000); //A
            Stream<String> filteredMappedStream = filteredStream.map(Student::getName); //B
            List<String> stuNames = filteredMappedStream.collect(Collectors.toList()); //C
            System.out.println(filteredMappedStream.count());//D
            System.out.println(stuNames);
            System.out.println(data); //F
        }
        /**
         B处复用了A处返回的流，
         C处的collect与D处的count都是终端操作，都对filteredMappedStream进行操作，结果是D处抛出IllegalStateException: stream has already been operated upon or closed
         外层的E F行显示data源数据没有发生改变
         */
    }
    /**
     * 查找
     * Collectors#allMatch 是否全部匹配
     * anyMatch 是否存在一个或多个满足条件
     * noneMatch = !allMatch
     * findFirst 找到第一个满足条件的item, filter(xxx).findFirst()
     * findAny 在并行流中效率高于串行流，在串行流中 = findFirst
     */
    static class TestFind{
        public static void main(String[] args) {
            boolean gt7000 = data.stream().anyMatch(stu -> stu.getNumber() > 7000);
            boolean wangExist = data.stream().anyMatch(student -> student.getName().contains("wang"));
            logger.info("gt7000 = {}. wangExist = {}", gt7000, wangExist);
            Arrays.asList(1, 2, 1, 3, 3, 2, 4).stream().filter(i -> i % 2 == 0).distinct().forEach(System.out::println);
            System.out.println(data);
            List<Student> students = data.stream().filter(stu -> stu.getNumber() > 100).limit(3).collect(Collectors.toList());
            System.out.println(students);
        }
    }

    /**
     * 规约
     *
     */
    @Test
    public void test8() {
        //求和,注意stream的数学计算不能用于金融业务，需要转BigDecimal
        double result;
        data.stream().mapToDouble(Student::getADouble).sum();
        // result = data.stream().map(item -> new BigDecimal(item.getADouble())).reduce(BigDecimal::doubleValue).sum();
        data.stream().mapToDouble(Student::getADouble).max().ifPresent(System.out::println);
        data.stream().mapToDouble(Student::getADouble).min().getAsDouble();
        result = data.stream().mapToDouble(Student::getADouble).average().getAsDouble();
        System.out.println(result);
    }

    @Test
    public void testReduece() {
        /**
         * reduce方法参数有多种
         * 只有一个BinaryOperator接口入参的
         * @see java.util.stream.Stream#reduce(java.util.function.BinaryOperator) 返回 Optional<T>
         * BinaryOperator接口的声明是 public interface BinaryOperator<T> extends BiFunction<T,T,T>
         *     其中声明了两个静态方法 minBy 和 maxBy
         *
         */
        Integer integer = Stream.of(1, 2, 3, 4).reduce(Integer::sum).orElse(0);
        Integer result = Stream.of(2, 5, 4, 3).reduce((left, right) -> {
            logger.info("current: left = {}, right = {}", left, right);
            // left += right;
            // return left;
            right += left;
            return right;
        }).orElse(0);
        System.out.println(result);

        /**
         * 上面的intStream不可复用！否则报IllegalStateException: stream has already been operated upon or closed
         * @see Stream#reduce(T identity, java.util.function.BinaryOperator<T> accumulator) 返回泛型T
         * identity用于指定stream循环的初始值,当stream中没有元素时，就直接返回这个identity
         * 带有identity入参的方法返回的就不是Optional
         */
        Integer result2 = Stream.of(2, 5, 4, 3).reduce(3, (sum, item) -> {
            logger.info("current: sum = {}, item = {}", sum, item);
            sum += item;
            return sum;//注意返回的是中间结果
        });
        System.out.println(result2);

        Integer result3 = new ArrayList<Integer>().stream().reduce(0, (sum, item) -> {
            logger.info("current: sum = {}, item = {}", sum, item);
            sum += item;
            return sum;//注意返回的是中间结果
        });
        System.out.println(result3);

        Integer result4 = new ArrayList<Integer>().stream().reduce((sum, item) -> {
            logger.info("current: sum = {}, item = {}", sum, item);
            sum += item;
            return sum;//注意返回的是中间结果
        }).orElse(0);
        System.out.println(result4);
        /**
         * 最复杂入参的reduce，方法签名
         * @see java.util.stream.Stream#reduce(U identity, java.util.function.BiFunction<U, ? super T, U> accumulator, java.util.function.BinaryOperator<U> combiner)
         * 包含一个累加器accumulator和一个合成器combiner,combiner用于将accumulator的结果合并起来。应用于并行流，详细测试见并行流 ParallelStreamDemo
         * 这个方法直接返回结果，而不是Optional
         */
        Integer multiplyResult2 = Stream.of(1,2,3,4,5).reduce(1,(total, item) -> total*item*2,(left, right) -> left*right);
        Integer multiplyResult = Stream.of(1,2,3,4,5).reduce(1, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer totalMulti, Integer item) {
                logger.info("accumulator：totalMulti = {}, item = {}", totalMulti, item);
                return totalMulti * (item * 2);
            }
        }, new BinaryOperator<Integer>() {
            @Override
            public Integer apply(Integer left, Integer right) {
                logger.info("combiner：left = {}, right = {}", left, right);
                return left * right;
            }
        });
        System.out.println(multiplyResult);

    }

    /**
     * 并行流
     */
    static class ParallelStreamDemo{
        /**
         * 并行流可以显著提高流处理的速度
         * 应用到并行流的任何操作都必须符合缩减操作的三个约束条件：无状态、不干预、关联性
         */
        static class ParallelStreamStart{
            public static void main(String[] args) {
                List<Integer> ints = Arrays.asList(1,2,3,4,5,6);
                ints.parallelStream().reduce((a,b) -> a+b).ifPresent(System.out::println);
                Optional<Integer> multiply = ints.parallelStream().reduce((a,b) -> a*b);
                if (multiply.isPresent())
                    logger.info("计算总乘积：{}", multiply.get());//720
                Integer parallelResult = ints.parallelStream().reduce(1, (a, b) -> a * b, (a, b) -> a * b);
                logger.info("乘积是：{}", parallelResult);//720
                //对集合中的每个元素扩大2倍后计算累乘积
                Integer multiplyResult = Stream.of(1,2,3,4,5).parallel().reduce(1, new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer totalMulti, Integer item) {
                        logger.info("{}: accumulator：totalMulti = {}, item = {}", Thread.currentThread().getName(), totalMulti, item);
                        return totalMulti * (item * 2);
                    }
                }, new BinaryOperator<Integer>() {
                    @Override
                    public Integer apply(Integer left, Integer right) {
                        logger.info("{}: combiner：left = {}, right = {}", Thread.currentThread().getName(), left, right);
                        return left * right;
                    }
                });
                System.out.println(multiplyResult);
                /** 并行流特性总结：
                 * accumulator每次都会使用identity作为a参与运算
                 * accumulator本身是并发取数的，所以从List取数操作的顺序是无序的
                 * accumulator与combiner的各自到执行也是并发的，收集的同时在进行累积操作
                 * 进行流式处理的List中有n个元素，accumulator执行n次，combiner执行(n-1)次
                 */
                //并行流里进行最大值比较效率更高？
                int intMax = data.stream().parallel().mapToInt(Student::getNumber).max().orElse(0);
                System.out.println(intMax);
            }
        }
        /**
         * 如果集合或数组中的元素是有序的，对应的流也是有序的，鉴于无序流可以提升点性能，所以可以主动要求使用无序流unordered()???
         * 如果希望并行流操作时也保持顺序，可使用forEachOrdered()方法
         */
        static class ParallelStreamAdvanceFeature{
            public static void main(String[] args) {
                data.stream().map(Student::getName).forEach(System.out::println);
                System.out.println("=-=-=-= 默认并行流是无序的 =-=-=-=");
                data.stream().parallel().map(Student::getName).forEach(System.out::println);
                System.out.println("=-=-=-= 指定有序（原始顺序）的并行流 =-=-=-=");
                data.stream().parallel().map(Student::getName).forEachOrdered(System.out::println);
                System.out.println("=-=-=-= 指定无序,似乎没啥效果 =-=-=-=");
                data.stream().unordered().map(Student::getName).forEach(System.out::println);
            }
        }
    }

    /**
     * 映射流
     */
    static class MapStreamDemo{
        @Data @AllArgsConstructor
        static class Teacher{
            private String name;
            private Integer age;
            private List<Student> students;
        }
        @Data @AllArgsConstructor
        static class Student{
            private String name;
            private Integer age;
            private String typeNumber;
        }
        public static void main(String[] args) {
            //使用映射流+并行流处理集合元素扩大2倍计算累乘积的写法
            Integer multiply = Arrays.asList(1, 2, 3, 4).parallelStream().map(item -> item * 2).reduce(1, (a, b) -> a * b);
            /**
             * 关于flatMap：用于一对多场景下，A对象包含B对象List属性，使用flatMap可以直接将A流映射为B流，flat就是将List<B>打扁了放到流里
             * 案例：提供一个List<Teacher> 一个Teacher里有一个List<Student>字段，提取所有的Student组成一个stream并打印
             */
            Student stu1 = new Student("jack",12,"1");
            Student stu2 = new Student("lucy",12,"3");
            Student stu3 = new Student("daniel",12,"3");
            Student stu4 = new Student("lily",12,"4");
            Teacher teacherWang = new Teacher("wang",32, Arrays.asList(stu1, stu2));
            Teacher teacherLee = new Teacher("lee",28,Arrays.asList(stu3, stu4));
            /**
             * 使用 static <T> Stream<T> Stream#concat(Stream<? extends T> a, Stream<? extends T> b) 合并两个流
             */
            Stream.concat(teacherWang.getStudents().stream(),teacherLee.getStudents().stream()).forEach(System.out::println);

            /**
             * 使用映射流 <R> Stream<R> Stream#flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
             * distinct是终端操作，将使得流操作后关闭，不可重用
             */
            List<Student> students = Arrays.asList(teacherLee, teacherWang).stream().flatMap(teacher -> teacher.getStudents().stream()).collect(Collectors.toList());//.distinct().forEach(System.out::println);
/**
             * 使用映射流提取所有Student的number，去重的同时转为数字放到list里
             * 使用到 flatMapToInt mapToInt
             */
            List<Integer> studentNumerTags = Arrays.asList(teacherLee, teacherWang).stream().flatMapToInt(
                    teacher -> teacher.getStudents().stream().mapToInt(item -> Integer.parseInt(item.getTypeNumber())))
                    .distinct().collect(ArrayList::new,ArrayList::add, ArrayList::addAll);
            System.out.println(studentNumerTags);
        }
    }

    /**
     * 收集操作：流映射转换好后通常要收集起来返回
     * Stream提供了两种collect方法,collect是一个终端操作
     * - <R, A> R collect(Collector<? super T, A, R> collector);
     * - <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner);
     */
    static class CollectOperationDemo{
        public static void testCollectorsToMap() {
            //collect太常用，这里只列举几个坑
            data.add(new Student("spy5", 160, 320.00,""));
            //间谍spy因number重复导致hashMapkey重复，从而导致下面一行的写法抛出merge相关的异常！异常的定义见Collectors.throwingMerger
            // Map<Integer, Student> containerMap = data.stream().collect(Collectors.toMap(item -> item.getNumber(), item -> item));
            // Map<Integer, Container> containerMap = data.stream().collect(HashMap::new, (map, item) -> map.put(item.getNumber(), item), HashMap::putAll);
            // System.out.println(containerMap.size());
            ArrayList<Integer> noDuplicateIds = data.stream().map(Student::getNumber).collect(Collectors.collectingAndThen(Collectors.toSet(), ArrayList::new));
            System.out.println(noDuplicateIds);
        }

        public static void testJoining() {
            String sbuffer = data.stream().map(Student::getName).collect(Collectors.joining());
            String namesWithConnector = data.stream().map(Student::getName).collect(Collectors.joining(",", ":", ";"));//:jack1,java2,car3,soldier4;
            System.out.println(namesWithConnector);
        }

        /**
         * 使用mapping操作将元素放入集合，使用groupingBy确定key和集合作为value组成的map
         * 学生可能在同一个城市，按照城市分组，返回map<cityName, Set<StudentName>>
         */
        public static void testMapping() {
            data.add(new Student("carls",7741,2.0,null));
            Map<String, Set<String>> cityNamesMap = data.stream().collect(
                    Collectors.groupingBy(Student::getCity,
                            Collectors.mapping(Student::getName, Collectors.toSet()) //这里toList也可以，但那样无法去重
                    )
            );
            System.out.println(cityNamesMap);
            /**
             * 相同城市的重名学生toSet时可以达到去重效果
             * name为null不影响：{newyork=[null, car3, carl], shanghai=[jack1, soldier4], beijing=[java2]}
             * cityName为null程序抛出NPE：element cannot be mapped to a null key
             */
            // Collectors.groupingBy(Student::getCity,
            //         Collectors.mapping(Student::getName, Collectors.toSet()) //这里toList也可以，但那样无法去重
        }

        /**
         * 求最值
         */
        public static void testExtremeValueCalc() {
            //3. 找到number最大/最小的学生
            Integer maxNumber = data.stream().collect(Collectors.collectingAndThen(Collectors.reducing((c1, c2) -> c1.getNumber() > c2.getNumber() ? c1 : c2), item -> item.orElse(Student.DEFAULT).getNumber()));
            System.out.println("学生的最大number = " + maxNumber);
            Optional<Student> collect = (Optional<Student>) data.stream().collect(Collectors.minBy(Student.NUMBER_COMPARATOR));
            Student minNumStu = ((Optional<Student>) data.stream().collect(Collectors.reducing(BinaryOperator.minBy(Student.NUMBER_COMPARATOR)))).get();
            //显然，使用自定义Comparator会另代码看起来很奇怪，而且编译器无法推测类型
            Student maxNumStu = data.stream().collect(Collectors.reducing(BinaryOperator.maxBy(Comparator.comparing(Student::getNumber)))).get();
            Integer minNumber = data.stream().collect(Collectors.minBy(Comparator.comparing(Student::getNumber))).get().getNumber();//reducing动作可以去掉
            Integer minNum = data.stream().min(Comparator.comparing(Student::getNumber)).orElse(Student.DEFAULT).getNumber();//还可以进一步简化
            System.out.println(maxNumStu);
            System.out.println(minNumber);
            System.out.println("学生最小number = " + minNum);
        }

        public static void testCountSumAndSummarize() {
            //额， list转stream后计数。。。还有三种方式
            data.stream().count();
            Long counting = data.stream().collect(Collectors.counting());
            Long reducing = data.stream().collect(Collectors.reducing(0L, e -> 1L, Long::sum));
            //求number总和
            Integer numberSum = data.stream().collect(Collectors.summingInt(Student::getNumber));
            //IntSummaryStatistics的设计,对int流进行摘要计算
            IntSummaryStatistics numberSummarize = data.stream().collect(Collectors.summarizingInt(Student::getNumber));
            Double numberAverage = data.stream().collect(Collectors.averagingInt(Student::getNumber));
            logger.info("summing = {}, summarize = {}, numberAverage = {}", numberSum, numberSummarize, numberAverage);
        }

        /**
         * 分组
         * Collectors.groupingBy方法类似SQL的group by
         */
        public static void testGroupingBy() {
            //1级分组:按城市分组
            Map<String, List<Student>> groupedStudent = data.stream().collect(Collectors.groupingBy(Student::getCity));
            //上面写法等同于 data.stream().collect(Collectors.groupingBy(Student::getCity, Collectors.toList())); 见源码
            System.out.println(groupedStudent);
            //2级分组：按城市+number分组。。虽然number各不相同
            Map<String, Map<Integer, List<Student>>> cityNumberGroupedStuMap = data.stream().collect(
                    Collectors.groupingBy(Student::getCity, Collectors.groupingBy(Student::getNumber)));
            //groupingBy的第二个参数是Collector，而其本身返回的也是Collector，所以可以嵌套。
            //向groupBy传递其他类型的Collector
            Map<String, Long> groupByCityGetCountMap = data.stream().collect(Collectors.groupingBy(Student::getCity, Collectors.counting()));
            System.out.println(groupByCityGetCountMap);
            //更复杂的Collector见testOther
        }
        /**
         * 分区
         */
        public static void testPartitioningBy() {
            //partitioningBy方法接收一个谓词
            //按number是否大于1000给学生分组
            Map<Boolean, List<Student>> partitionedStus = data.stream().collect(Collectors.partitioningBy(student -> student.getNumber() > 1000));
            System.out.println(partitionedStus);
        }


        /**
         * 是男人就调stream api 9层，效果有点像SQL筛选数据
         * collectingAndThen
         * groupingBy
         */
        public static void testOther() {
            /**
             //1. 生成一个不可变List
             List<Student> immutableStus = data.stream().collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
             immutableStus.iterator().remove();//UnsupportedOperationException
            */
            /** -=-=-=-=-=-=-=-= 到这里，代码使用java8 stream 将追求奇技淫巧 -=-=-=-=-=-=-=-=-=*/

            //4. 找到每个城市里学生number最大的学生，按cityName - stuName组成map返回，你能给出几种写法？
            //法 1
            Map<String,String> cityMaxStuNumberNameMap = data.stream().collect(
                    Collectors.groupingBy(Student::getCity,
                            Collectors.collectingAndThen(
                                    Collectors.reducing(( c1,  c2) -> c1.getNumber() > c2.getNumber() ? c1 : c2),//为什么最后不能用这个替换？？？ BinaryOperator.maxBy(Student.NUMBER_COMPARATOR)
                                    item -> item.orElse(Student.DEFAULT).getName()) //Optional::get
                    )
            );
            //法 2
            Map<String,String> cityMaxStuNumberNameMap2 = data.stream().collect(
                    Collectors.groupingBy(Student::getCity,
                            Collectors.collectingAndThen(
                                    Collectors.reducing(BinaryOperator.maxBy(Comparator.comparing(Student::getNumber))),//要这样写才可以，似乎Student.NUMBER_COMPARATOR这个内部comparator用不到了，显然stream让你连这个都省了
                                    item -> item.orElse(Student.DEFAULT).getName()) //Optional::get
                    )
            );
            System.out.println(cityMaxStuNumberNameMap2);

            //5. 将一个城市里number最大和最小的两个学生组成List作为value与cityName作为key组成map返回，如何用一行代码实现！
            Map<String, List<Student>> cityNameMaxMinStudentsMap = data.stream()
                    .collect(Collectors.toMap(Student::getCity, stu -> Arrays.asList(stu, stu),
                            (l1,l2) -> Arrays.asList(
                                    (l1.get(0).getNumber()>l2.get(0).getNumber()? l2: l1).get(0),
                                    (l1.get(1).getNumber()<l2.get(1).getNumber()? l2: l1).get(1))));
            //有点问题，在一个城市只有一个学生的情况下会导致list中有两个重复的数据，无可厚非
            System.out.println(cityNameMaxMinStudentsMap);

        }
        public static void main(String[] args) {
            // testGroupingBy();
            // testOther();
            // testPartitioningBy();
            // testCountSumAndSummarize();
            testCollectorsToMap();
        }
    }

    /**
     * 迭代器
     * Spliterator不同于传统iterator的一点是其支持并行迭代
     */
    static class SpliteratorDemo{
        public static void main(String[] args) {
            /*
            //1. 传统的iterator操作
            //forEach在编译后会转换成iterator遍历操作
             Iterator<Container> iterator = data.stream().iterator();
             while (iterator.hasNext()){
             System.out.println(iterator.next().getString());
             }


             //2. 使用 Spliterator#tryAdvance 进行遍历
            Spliterator<Container> spliterator = data.stream().spliterator();
            while (spliterator.tryAdvance(System.out::println));
             */

            //3. forEachRemaining和trySplit方法
            Spliterator<Student> spliterator1 = data.stream().spliterator();
            Spliterator<Student> spliterator2 = spliterator1.trySplit();
            if (spliterator2 != null){
                spliterator2.forEachRemaining(System.out::println);
            }
            System.out.println("=-=-=-=-=-=-=-=-=");
            spliterator1.forEachRemaining(System.out::println);
            /*
             * 4分2了，各打印两个元素
             * spliterator由API内部调用，现有设计不满足时才考虑自定义split处理大数据集
             */
        }
    }

    static class QuestionToResearch{
        public static void main(String[] args) {
            //使用reduce似乎可以当作collect使用,但在并行流下会乱掉，未知原因 todo ???
            ArrayList<Integer> accResult = Stream.of(1,2,3,4).parallel().reduce(new ArrayList<>(), (accumulator, item) -> {
                accumulator.add(item);
                return accumulator;
            }, (left, right) -> {
                left.addAll(right);
                return left;
            });
            System.out.println(accResult);
            /**
             * 打印结果还会变。。。
             * [1, 2, 4, 1, 2, 3, 1, 2, 4, 1, 2, 3, 1, 2, 4, 1, 2, 3, 1, 2, 4, 1, 2, 3]
             * [null, null, 2, null, null, 2, 4, null, null, 2, null, null, 2, 4, null, null, 2, null, null, 2, 4, null, null, 2, null, null, 2, 4]
             */
        }
    }

}
