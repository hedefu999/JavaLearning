package com.changingedu.elasticsearch;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ManyDSL2JavaTemplate {

    static class BaseQuery{
        static void testOne(){
            BoolQueryBuilder baseQueryBuilder = QueryBuilders.boolQuery();
            //基础查询：海风老师筛选
            String path = "teacher_attr_tags";
            String isHfParttimeFieldName = "teacher_attr_tags.is_hf_part_time_teacher";
            // baseQueryBuilder.must(QueryBuilders.nestedQuery(path, QueryBuilders.termQuery(isHfParttimeFieldName, true), org.apache.lucene.search.join.ScoreMode.None)).boost(0.0f);//boost干啥用的
            String isHfFieldName = "teacher_attr_tags.is_hf_teacher";
            // baseQueryBuilder.must(QueryBuilders.nestedQuery(path, QueryBuilders.termQuery(isHfFieldName, false), org.apache.lucene.search.join.ScoreMode.None)).boost(0.0f);

            //要想使用highlight功能，需要将这里的match匹配打开
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("address", "徐汇区");
            baseQueryBuilder.should(matchQueryBuilder);

            //老师专供城市
            BoolQueryBuilder boolFilter = QueryBuilders.boolQuery();
            boolFilter.must(QueryBuilders.prefixQuery("teacher_city_orientation","12"));
            //老师ID过滤
            Long[] teacherIds = {5764L,5763L,3362L,3856L,43113L,10001L};
            QueryBuilder teacherIdCon = QueryBuilders.termsQuery("teacher_id", teacherIds);
            boolFilter.must(teacherIdCon);
            System.out.println(boolFilter.toString());
        }
        public static void main(String[] args) {
            testOne();
        }
    }

    static class WorkbookSearch{
        static void DSL(){
            CompletionSuggestionBuilder compBuilder = SuggestBuilders.completionSuggestion("knowledge_point");
            compBuilder.text("回答我");
            compBuilder.size(7);

            SuggestBuilder suggestBuilder = new SuggestBuilder();
            suggestBuilder.addSuggestion("knowledge_point_suggestion", compBuilder);

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.suggest(suggestBuilder);
            searchSourceBuilder.trackTotalHits(true);
            searchSourceBuilder.timeout(new TimeValue(10000, TimeUnit.MILLISECONDS));

            System.out.println(searchSourceBuilder.toString());
        }

        public static void main(String[] args) {
            DSL();
        }
    }

    static class Test{
        public static void main(String[] args) {
            Long first = 1152921504606846975L;
            Long fourth = 1134907174816841727L;
            Long fifth = 281474976710400L;
            long second = 281474976710655L;
            long maxValue = Long.MAX_VALUE;

            System.out.print(Long.toBinaryString(fourth).length());
            System.out.print(Long.toBinaryString(fifth).length());
            // System.out.println(Long.toBinaryString(second).length());
        }

        static void test(long input){
            // List<Integer> five = transform(Arrays.asList(1152921504606846975L, 1152921504606846975L, 1152921504606846975L, 1134907174816841727L, 281474976710400L));

            while (input > 1){
                System.out.println(input % 2);
                input = input/2;
            }

        }
    }
}
