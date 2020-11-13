package com.changingedu.elasticsearch;

import com.qingqing.api.proto.v1.app.AppCommon.HighlightTag;
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
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SearchServiceDemo {
    private static final Logger logger = LoggerFactory.getLogger(SearchServiceDemo.class);
    public static final String HOST = "http://api.es.idc.cedu.cn";
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

    static class Primary{
        //查询参数构建
        static SearchSourceBuilder searchSourceBuilder(){
            SearchSourceBuilder srchSrcBuilder = new SearchSourceBuilder();
            srchSrcBuilder.from(2).size(10);
            //复杂的查询条件，查询API测试
            searchSourceBuilder(srchSrcBuilder);
            srchSrcBuilder.trackScores(true);
            srchSrcBuilder.fetchSource(true);
            srchSrcBuilder.trackTotalHits(true);
            srchSrcBuilder.timeout(new TimeValue(5000, TimeUnit.MILLISECONDS));
            return srchSrcBuilder;
        }
        //基本查询，SQL的 where and and 在ES中怎么写，这里进行了演示
        //这里以teacher_attribute_tag为例进行演示
        //第一个：是海风老师
        static void baseQuery(SearchSourceBuilder srchBuilder){
            BoolQueryBuilder builder = QueryBuilders.boolQuery();

            //基础查询：海风老师筛选
            String path = "teacher_attr_tags";
            String isHfParttimeFieldName = "teacher_attr_tags.is_hf_part_time_teacher";
            builder.must(QueryBuilders.nestedQuery(path, QueryBuilders.termQuery(isHfParttimeFieldName, true), org.apache.lucene.search.join.ScoreMode.None)).boost(0.0f);//boost干啥用的
            String isHfFieldName = "teacher_attr_tags.is_hf_teacher";
            builder.must(QueryBuilders.nestedQuery(path, QueryBuilders.termQuery(isHfFieldName, true), org.apache.lucene.search.join.ScoreMode.None)).boost(0.0f);

            //过滤器：
            BoolQueryBuilder builder2 = QueryBuilders.boolQuery();
            BoolQueryBuilder builder3 = QueryBuilders.boolQuery();
            builder3.should(QueryBuilders.termQuery("teacher_city_orientation",12));
            //老师ID过滤

            builder2.must(builder3).must();


            builder.filter(builder2.boost(0.0f));

            //这个query可以传进去 BoolQueryBuilder FilterFunctionBuilder
            srchBuilder.query(builder);
        }
        //对搜索内容进行复杂排序,可以读取配置决定哪些字段的分值占据更高的比重，以便让某些老师前置
        static void FunctionScoreBuilder(SearchSourceBuilder srchBuilder,QueryBuilder queryBuilder){
            String path = "doc[\"intelligent_score\"]";
            String scriptTemplate = "if(%s.size() > 0){return %s.value;} else {return 0;}";
            String script = String.format(scriptTemplate, path, path);
            Script scriptEntity = new Script(ScriptType.INLINE, "painless", script, Collections.emptyMap());
            ScriptScoreFunctionBuilder funcBuilder = ScoreFunctionBuilders.scriptFunction(scriptEntity);
            //此处脚本的执行与排序类型有关，如搜索老师按距离学生远近排序时就要来个距离得分权重搜索，显然painless脚本就是拿来计算某个字段的得分的
            //FunctionScoreQueryBuilder可以用于权重赋分搜索 老师最低价、老师等级等信息通过painless脚本处理可以得到计算结果
            funcBuilder.setWeight(1+2+3+14);//??? 可以根据业务需要不断增加totalweight,每个weight配置在某个field维度上,这样totalWeight越来越大，不知啥用途？？？
            FilterFunctionBuilder[] filterFunctionBuilders = {new FilterFunctionBuilder(funcBuilder)};
            FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(queryBuilder, filterFunctionBuilders);
            functionScoreQueryBuilder.scoreMode(ScoreMode.SUM)//？？
                    .boostMode(CombineFunction.REPLACE);//??
            srchBuilder.query(queryBuilder);
        }
        //复杂的查询条件，查询API测试
        static void searchSourceBuilder(SearchSourceBuilder srchBuilder){
            //简单排序（多个字段进行排序，类似SQL的order by teacher_id, kabc）
            FieldSortBuilder teacherIdAscSort = SortBuilders.fieldSort("teacher_id").order(SortOrder.ASC);
            FieldSortBuilder kabcDescSort = SortBuilders.fieldSort("kabc").order(SortOrder.DESC);
            srchBuilder.sort(kabcDescSort).sort(teacherIdAscSort);
            //聚合操作？？也可以多个,甚至是嵌套
            AggregationBuilder aggBuilder = AggregationBuilders.terms("term_city_id_agg").field("city_id").size(100);
            AggregationBuilder nestedBuilder = AggregationBuilders.nested("nest_course_comment_agg", "teacher_phrases");//老师评论标签
            AggregationBuilder innerBulder = AggregationBuilders.terms("course_comment_phrase_agg").field("teacher_phrases.phrase_name.raw").size(100);
            nestedBuilder.subAggregation(innerBulder);
            srchBuilder.aggregation(aggBuilder).aggregation(nestedBuilder);
            //highlight字段
            HighlightBuilder hlbuilder = new HighlightBuilder();
            hlbuilder.preTags("<strong>").postTags("</strong>");
            srchBuilder.highlighter(hlbuilder);
        }
        //对某些字段应用painless脚本进行计算得到的结果作为单独的字段返回
        static void addPainlessScript2SrchSrcBuilder(SearchSourceBuilder srchBuilder){
            Map<String, Object> params = new HashMap<>();
            params.put("lon",new Double(12.3));
            params.put("lat",new Double(12.3));
            String path = "doc[\"address_geo\"]";
            String scriptTemplate = "if(%s.size() > 0){return %s.arcDistance(params.%s, params.%s)*0.001;} else {return -1;}";
            String painlessScriptContent = String.format(scriptTemplate, path, path, "lat", "lon");
            Script painlessScript = new Script(ScriptType.INLINE, "painless", painlessScriptContent, params);
            srchBuilder.scriptField("distanceX", painlessScript);
        }
        //解析搜索结果中的主体内容
        static void parseSearchResult(SearchHits searchHits){
            float maxScore = searchHits.getMaxScore();
            logger.info("搜索结果: maxScore = {}", maxScore);
            SortField[] sortFields = searchHits.getSortFields();
            for (SortField sortField : sortFields){
                logger.info("sortField = {}", sortField.getField());
            }
            for (SearchHit hit : searchHits.getHits()){
                Map<String, DocumentField> fields = hit.getFields();
                logger.info("hit中包含的field：{}", fields);
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                String distanceX = "distanceX";
                DocumentField distanceXField = hit.field(distanceX);
                if (distanceXField != null){
                    logger.info("hit.field()与hit.getResourceAsMap().get()是否同效果：{} - {}", distanceXField.getValue(), sourceAsMap.get(distanceX));
                }
                String strMainBody = hit.getSourceAsString();
                logger.info("搜索结果的主体内容是：{}", strMainBody);
            }
        }

        //解析搜索结果中的Highlighted字段
        static void researchHighlightedFields(SearchHits searchHits){
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

        static void pageQuery() throws IOException {
            SearchRequest schRequest = new SearchRequest(TEACHER_INDEX);
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
            schRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
            schRequest.source(searchSourceBuilder());
            //调client去搜索
            SearchResponse searchResponse = client.search(schRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            logger.info("搜索命中数量 = {}", searchHits.length);
            logger.info("命中数量 = {}", hits.getTotalHits() == null ? 0 : hits.getTotalHits().value);
            logger.info("尝试取totalHits: {}", hits.getTotalHits());
            parseSearchResult(hits);
        }



    }

    public static void main(String[] args) {
        try {
            Primary.pageQuery();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
