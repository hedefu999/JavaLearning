package com.changingedu.elasticsearch;

import org.apache.http.HttpHost;
import org.apache.lucene.search.SortField;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery.ScoreMode;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.index.query.functionscore.ScriptScoreFunctionBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SearchServiceDemo {
    private static final Logger logger = LoggerFactory.getLogger(SearchServiceDemo.class);
    public static final String HOST = "172.22.12.3";//"http://api.es.idc.cedu.cn";
    public static final Integer PORT = 9201;
    public static final String TEACHER_INDEX = "apptest-teacher_index_9-4-0-0";
    static RestHighLevelClient client = buildClient();

    //com.qingqing.search.core.es.EsRestHighLevelClient
    static RestHighLevelClient buildClient(){
        List<HttpHost> hosts = new ArrayList<>();
        String[] ipAddresses = {HOST};
        for (String ipAddress : ipAddresses){
            HttpHost httpHost = new HttpHost(ipAddress.trim(), PORT);
            hosts.add(httpHost);
        }
        //HttpHost.class
        HttpHost[] httpHosts = {};//T[] 泛型传值，也可写 new HttpHost[]{}
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(hosts.toArray(httpHosts)));
        return client;
    }

    static class MasterSearchBuilder{
        static void first(){
            SearchSourceBuilder srchSrcBuilder = new SearchSourceBuilder();
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("teacher_id", 5762);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(termQueryBuilder);
            srchSrcBuilder.query(termQueryBuilder);
            Boolean explain = srchSrcBuilder.explain();
            System.out.println(srchSrcBuilder.toString());
        }

        /**
         * 需求 (teacher_id = 5762 AND student_id = 5763) OR (teacher_id = 5764 AND student_id = 5765)
         */
        static void second(){
            SearchSourceBuilder srchSrcBuilder = new SearchSourceBuilder();
            BoolQueryBuilder boolQueryBuilderOut = QueryBuilders.boolQuery();

            TermQueryBuilder termQueryBuilder1 = QueryBuilders.termQuery("teacher_id", 5762);
            TermQueryBuilder termQueryBuilder2 = QueryBuilders.termQuery("student_id", 5763);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(termQueryBuilder1);
            boolQueryBuilder.must(termQueryBuilder2);

            TermQueryBuilder termQueryBuilder3 = QueryBuilders.termQuery("teacher_id", 5764);
            TermQueryBuilder termQueryBuilder4 = QueryBuilders.termQuery("student_id", 5765);
            BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
            boolQueryBuilder2.must(termQueryBuilder3);
            boolQueryBuilder2.must(termQueryBuilder4);

            boolQueryBuilderOut.should(boolQueryBuilder);
            boolQueryBuilderOut.should(boolQueryBuilder2);
            srchSrcBuilder.query(boolQueryBuilderOut);
            System.out.println(srchSrcBuilder.toString());
        }

        public static void main(String[] args) {
            second();
        }
    }
    //由teacher_index引出的搜索API
    static class Primary{
        //查询参数构建基本查询入参的构建，如分页参数、设置超时
        static SearchSourceBuilder searchSourceBuilder(){
            SearchSourceBuilder srchSrcBuilder = new SearchSourceBuilder();
            srchSrcBuilder.from(0).size(10);
            //调用方法生成查询条件，这里可以写地很复杂
            //基本查询
            BoolQueryBuilder baseQueryBuilder = baseQuery(srchSrcBuilder);
            //添加FunctionScore计算分值用于结果排序
            functionScoreBuilder(srchSrcBuilder,baseQueryBuilder);
            fieldSortAndHighlight(srchSrcBuilder);
            //使用painless处理字段
            addIntelligentScoreScript(srchSrcBuilder);
            //聚合操作
            aggregateOperation(srchSrcBuilder);
            //_source里目前有123个字段，都显示太长了
            specifyFields(srchSrcBuilder);
            srchSrcBuilder.trackScores(true);
            srchSrcBuilder.fetchSource(true);
            srchSrcBuilder.trackTotalHits(true);
            srchSrcBuilder.timeout(new TimeValue(5000, TimeUnit.MILLISECONDS));
            System.out.println(srchSrcBuilder.toString());
            return srchSrcBuilder;
        }

        static void teacherIdQuery(SearchSourceBuilder srchBuilder){
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            long[] teacherIds = {5762,5763};
            QueryBuilder teacherIdsCon = QueryBuilders.termsQuery("teacher_id", teacherIds);

            boolQueryBuilder.must(teacherIdsCon);
            srchBuilder.query(boolQueryBuilder);
            System.out.println(srchBuilder.toString());
        }

        //SQL的 where and and 在ES中怎么写，这里进行了演示，注意 and or的嵌套容易出错
        //这里以teacher_attribute_tag为例进行演示 分别是：海风老师、老师专供城市、老师ID
        static BoolQueryBuilder baseQuery(SearchSourceBuilder srchBuilder){
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
            // boolFilter.must(QueryBuilders.termQuery("teacher_city_orientation",12));
            //老师ID过滤
            Long[] teacherIds = {5764L,5763L,3362L,3856L,43113L,10001L};
            QueryBuilder teacherIdCon = QueryBuilders.termsQuery("teacher_id", teacherIds);
            // boolFilter.must(teacherIdCon);

            ExistsQueryBuilder kabcExist = QueryBuilders.existsQuery("kabc");
            boolFilter.must(kabcExist);
            //上面的查询条件当作Filter用
            baseQueryBuilder.filter(boolFilter.boost(0.0f));
            srchBuilder.query(baseQueryBuilder);
            return baseQueryBuilder;
        }

        //使用脚本依据字段值进行分数计算，计算结果放在_score里
        static void functionScoreBuilder(SearchSourceBuilder srchBuilder, QueryBuilder queryBuilder){
            String path = "doc[\"kabc\"]";
            String scriptTemplate = "if(%s.size() > 0){if(%s.value == 1){return 3;}else{return 2;}} else {return -8;}";
            String script = String.format(scriptTemplate, path, path, path);
            Script scriptEntity = new Script(ScriptType.INLINE, "painless", script, Collections.emptyMap());
            ScriptScoreFunctionBuilder funcBuilder = ScoreFunctionBuilders.scriptFunction(scriptEntity);
            //此处脚本的执行与排序类型有关，如搜索老师按距离学生远近排序时就要来个距离得分权重搜索，显然painless脚本就是拿来计算某个字段的得分的
            //FunctionScoreQueryBuilder可以用于权重赋分搜索 老师最低价、老师等级等信息通过painless脚本处理可以得到计算结果
            funcBuilder.setWeight(2+3);//这个weight会乘以5
            //functionScoreQuery可以传入多个ScriptScoreFunctionBuilder
            FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(queryBuilder, funcBuilder);
            functionScoreQueryBuilder.scoreMode(ScoreMode.SUM).boostMode(CombineFunction.SUM);//??
            srchBuilder.query(functionScoreQueryBuilder);
        }//更复杂的多函数分值计算见 https://my.oschina.net/u/3734816/blog/3105125

        //复杂的查询条件，查询API测试
        static void fieldSortAndHighlight(SearchSourceBuilder srchSrcBuilder){
            //简单排序（多个字段进行排序，类似SQL的order by teacher_id, kabc）
            FieldSortBuilder teacherIdAscSort = SortBuilders.fieldSort("teacher_id").order(SortOrder.ASC);
            FieldSortBuilder kabcDescSort = SortBuilders.fieldSort("kabc").order(SortOrder.DESC);
            //使用FunctionScore的计算结果进行排序
            FieldSortBuilder scoreDescSort = SortBuilders.fieldSort("_score").order(SortOrder.DESC);
            srchSrcBuilder.sort(scoreDescSort).sort(teacherIdAscSort);
            //highlight字段
            //需要确保有QueryBuilder将address match匹配进来，实际结果就是会将address中出现的"徐汇区"三个字使用highlight中提供的前后缀进行包裹，放在与_source同级的highlight字段中
            //highlight{address:"<strong>徐汇区</strong>xxx"}
            HighlightBuilder hlbuilder = new HighlightBuilder();
            hlbuilder.field("address");
            hlbuilder.preTags("<strong>").postTags("</strong>");
            srchSrcBuilder.highlighter(hlbuilder);
        }

        /*
         聚合操作，相当于SQL里的 group by
         这里以 group by kabc,sex为例
         会在_source同级生成一个aggregations,其中下一级是AggregationBuilders指定的名称，也就是分组名称，
         每个分组下依据数据差异情况分为多个bucket，每个bucket的key提示这一小组的相同取值，在doc_count里是小组内成员数量
         聚合可以嵌套
         */
        static void aggregateOperation(SearchSourceBuilder srchSrcBuilder){
            AggregationBuilder kabcAgg = AggregationBuilders.terms("field_kabc").field("kabc").size(100);
            //如果_source下的field含有子field，才使用nested，如teacher_attribute_tag.is_hf_teacher
            AggregationBuilder schoolAgeAgg = AggregationBuilders.terms("field_schoolage").field("school_age");//老师性别
            kabcAgg.subAggregation(schoolAgeAgg);
            srchSrcBuilder.aggregation(kabcAgg);//.aggregation(nestedBuilder);
        }
        //对某些字段应用painless脚本进行计算得到的结果作为单独的字段返回
        static void addPainlessScript2SrchSrcBuilder(SearchSourceBuilder srchSrcBuilder){
            Map<String, Object> params = new HashMap<>();
            params.put("lon",12.3);
            params.put("lat",12.3);
            String path = "doc[\"address_geo\"]";
            String scriptTemplate = "if(%s.size() > 0){return %s.arcDistance(params.%s, params.%s)*0.001;} else {return -1;}";
            String painlessScriptContent = String.format(scriptTemplate, path, path, "lat", "lon");
            Script painlessScript = new Script(ScriptType.INLINE, "painless", painlessScriptContent, params);
            srchSrcBuilder.scriptField("distanceX", painlessScript);
        }
        //测试painless脚本，对intelligent_score进行实时计算，结果放在fields中
        static void addIntelligentScoreScript(SearchSourceBuilder srchSrcBuilder){
            String scriptStr = "if(doc['intelligent_score'].size() > 0){  return doc['intelligent_score'].value + 5;}else {return -12;}";
            Script script = new Script(ScriptType.INLINE,"painless",scriptStr, new HashMap<>());
            srchSrcBuilder.scriptField("intelligent_score",script);
        }
        //你想返回哪些字段
        static void specifyFields(SearchSourceBuilder srchSrcBuilder){
            String[] includeFields = new String[]{"teacher_id","assistant_id","assistant_name","encrypt_assistant_phone","address","teacher_status","sex","real_name","nick","address_geo","register_time","school_age","teacher_attr_tags","course_id","student_count","total_teach_time","teacher_source","kabc","intelligent_score"};
            String[] excludeFields = {};
            srchSrcBuilder.fetchSource(includeFields, excludeFields);
        }

        //解析搜索结果中的主体内容
        static void parseSearchResult(SearchHits searchHits){
            float maxScore = searchHits.getMaxScore();
            logger.info("maxScore = {}, 命中总数 totalCount = {}", maxScore, searchHits.getTotalHits() == null ? 0 : searchHits.getTotalHits().value);
            SearchHit[] pageItems = searchHits.getHits();
            logger.info("当前分页命中记录数量 currentPageSize = {}", pageItems.length);
            SortField[] sortFields = searchHits.getSortFields();
            if (sortFields != null){
                for (SortField sortField : sortFields){
                    logger.info("sortField = {}", sortField.getField());
                }
            }
            //hit位于返回结果的_source字段中
            for (SearchHit hit : pageItems){
                Map<String, DocumentField> fields = hit.getFields();
                logger.info("hit中包含的field数量：{}", fields.size());
                fields.entrySet().stream().forEach(item -> {//被painless处理的field会放在这里
                    logger.info("hit中处理好的field：name = {}, value = {}", item.getKey(), item.getValue());
                });
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                String encryptPhone = "encrypt_phone_number";
                DocumentField distanceXField = hit.field(encryptPhone);
                if (distanceXField != null){
                    logger.info("hit.field()与hit.getResourceAsMap().get()是否同效果：{} - {}", distanceXField.getValue(), sourceAsMap.get(encryptPhone));
                }
                String strMainBody = hit.getSourceAsString();
                logger.info("搜索结果的主体内容是：{}", strMainBody);
            }
        }

        //解析搜索结果中的Highlighted字段
        static void parseHighlightedFields(SearchHits searchHits){
            for (SearchHit hit : searchHits.getHits()){
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                highlightFields.entrySet().stream().forEach(entry -> {
                    String name = entry.getKey();
                    HighlightField field = entry.getValue();
                    Text[] fragments = field.getFragments();
                    logger.info("highlight field里有啥？：name = {}, field = {}", name, Arrays.asList(fragments).stream().map(Text::string).collect(Collectors.joining("。")));
                });
            }

        }

        //搜索总入口
        static void searchMainEntrance() throws IOException {
            SearchRequest srchRequest = new SearchRequest(TEACHER_INDEX);
            /*
             关于SearchType （ref https://blog.csdn.net/wangyunpeng0319/article/details/78218332）
             在数据分片的情况下，获取全部数据某种排名某个数量的记录数目
             query_and_fetch 速度最快，但各分片返回的数据总量是用户请求的n（分片数）倍
             QUERY_THEN_FETCH (DEFAULT)
                分两步，先向所有的 shard 发出请求， 各分片只返回文档 id(注意， 不包括文档 document)和排名相关的信息(也就是文档对应的分值)， 然后按照各分片返回的文档的分数进行重新排序和排名， 取前 size 个文档。
             　　第二步， 根据文档 id 去相关的 shard 取 document。 这种方式返回的 document 数量与用户要求的大小是相等的。但性能一般，数据排名不准确
             DFS_QUERY_THEN_FETCH
                DEFAULT的优化,比其多一个计算分词频率的步骤，以达到更精确的分值。先对所有分片发送请求，把所有分片中的词频和文档频率等打分依据汇总，再执行操作
                准确性最好，性能最弱
                > DFS是一个怎样的过程
                DFS就是在真正查询之前，先把各个分片的词频率和文档频率收集下来，然后在词搜索的时候，各分片依据全局的词频和文档频率进行搜索和排名。
             */
            srchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
            srchRequest.source(searchSourceBuilder());

            //调client去搜索
            SearchResponse searchResponse = client.search(srchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            parseSearchResult(hits);
            parseHighlightedFields(hits);
        }



    }

    static class SuggestionCompletion{
        static void buildKeywordsSuggestionQuery() {
    //		CompletionSuggestionBuilder compBuilder = new CompletionSuggestionBuilder(PHRASE_NAME);  todo 两者区别
            CompletionSuggestionBuilder compBuilder = SuggestBuilders.completionSuggestion("phrase_name_suggestion");
            compBuilder.text("你好");
            compBuilder.size(12);
    //		compBuilder.field(PHRASE_NAME_SUGGESTION);

            SuggestBuilder suggestBuilder = new SuggestBuilder();
            suggestBuilder.addSuggestion("phrase_name",compBuilder);
            SearchRequest searchRequest = new SearchRequest("indexName");
            searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.suggest(suggestBuilder);
            searchSourceBuilder.trackTotalHits(true);
            searchSourceBuilder.timeout(new TimeValue(30000, TimeUnit.MILLISECONDS));
            searchRequest.source(searchSourceBuilder);

            System.out.println(searchRequest.toString());
        }

        public static void main(String[] args) {
            String dateWithTimeDistrict = "2020-12-02T11:48:01.475+08:00";
            Date date = new Date(dateWithTimeDistrict);

        }

    }

    public static void main(String[] args) {
        try {
            Primary.searchMainEntrance();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
