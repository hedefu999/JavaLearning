package com.changingedu.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkItemResponse.Failure;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkProcessor.Builder;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.TermVectorsRequest;
import org.elasticsearch.client.core.TermVectorsResponse;
import org.elasticsearch.client.core.TermVectorsResponse.TermVector.Term;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JavaRestClientTest {
    private static final Logger logger = LoggerFactory.getLogger(JavaRestClientTest.class);
    public static final String INDEX_TEACHER = "apptest-teacher_index_9-4-0-0";
    public static final String INDEX_TEACHER2 = "dev108-teacher_index_9-4-0-0";
    public static final String COLUMN_TEACHER_ID = "teacher_id";

    /*
     high level client实例内部使用low-level client构建
     low-level cleint 维护了一个连接池，在使用完毕时应调用close方法关闭内部low-level client
     */
    //ref https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high-getting-started-initialization.html
    private static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
            new HttpHost("api.es.idc.cedu.cn",9201,"http")
    ));

    //ref https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high-document-index.html
    static class TestIndexAPI{

        static void first() throws Exception {
            //request的3种创建方式
            IndexRequest request = new IndexRequest(INDEX_TEACHER);//index
            request.id("100");//doc id 用途不明
            String jsonString  = "{\"teacher_id\":\"5762\"}";
            request.source(jsonString, XContentType.JSON);

            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("teacher_id", "5762");
            jsonMap.put("is_hf_teacher",true);
            IndexRequest request1 = new IndexRequest(INDEX_TEACHER).id("2").source(jsonMap);

            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.field("teacher_id","5762");
                builder.field("is_hf_teacher",true);
            }
            builder.endObject();
            IndexRequest indexRequest = new IndexRequest(INDEX_TEACHER).id("1").source(builder);

            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            System.out.println(response.toString());
        }

        static void indexAsyncUsage(){
            IndexRequest indexRequest = new IndexRequest(INDEX_TEACHER).id("1").source(COLUMN_TEACHER_ID,"5762","is_hf_teacher",true);
            ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {

                }
                @Override
                public void onFailure(Exception e) {

                }
            };
            client.indexAsync(indexRequest,RequestOptions.DEFAULT, listener);
        }

        static void statusConflict(){
            IndexRequest request = new IndexRequest("posts").id("1").source("field", "value").opType(DocWriteRequest.OpType.CREATE);
            try {
                IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            } catch(ElasticsearchException e) {
                if (e.status() == RestStatus.CONFLICT) {

                }
            } catch (IOException e){}
        }
    }

    static class GetApi{

        static void first() throws IOException {
            GetRequest getRequest = new GetRequest(INDEX_TEACHER, "1");
            getRequest.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);//默认关闭sourse retrieval
            String[] includes = new String[]{COLUMN_TEACHER_ID, "5762"};
            String[] excludes = Strings.EMPTY_ARRAY;
            FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
            getRequest.fetchSourceContext(fetchSourceContext);
            GetResponse getResponse = client.get(getRequest,RequestOptions.DEFAULT);
            String index = getResponse.getIndex();
            String id = getResponse.getId();
            logger.info("index = {}, id = {}", index, id);
            if (getResponse.isExists()) {
                long version = getResponse.getVersion();
                String sourceAsString = getResponse.getSourceAsString();
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
                byte[] sourceAsBytes = getResponse.getSourceAsBytes();
                logger.info(version+ " " +sourceAsString + " " + sourceAsMap);
            } else {

            }
        }

        static void notExistIndex(){
            GetRequest request = new GetRequest("does_not_exist", "1");
            try {
                GetResponse getResponse = client.get(request, RequestOptions.DEFAULT);
            } catch (ElasticsearchException e) {
                if (e.status() == RestStatus.NOT_FOUND) {
                    logger.info("NOT FOUND");
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    static class ExistAPI{
        static void first() throws IOException {
            GetRequest getRequest = new GetRequest(INDEX_TEACHER, "1");
            //由于Exist API只返回true or false，所以把fetching _source的动作关闭掉，不再取任何存储字段，这样request可以轻量化
            getRequest.fetchSourceContext(new FetchSourceContext(false));
            getRequest.storedFields("_none_");
            boolean exist = client.exists(getRequest, RequestOptions.DEFAULT);
            System.out.println(exist);
        }
    }

    static class UpdateAPI{
        static void updateWithScript(){
            UpdateRequest request = new UpdateRequest(INDEX_TEACHER2, "1");
            Map<String,Object> params = Collections.singletonMap("count",4);
            Script inlineScript = new Script(ScriptType.INLINE, "painless", "ctx._source.field += params.count", params);
            request.script(inlineScript);
        }
        static void partialDocumentUpdate(){
            UpdateRequest request = new UpdateRequest(INDEX_TEACHER, "1");
            String jsonString = "{\"update\":\"2017-01-01\"}";
            request.doc(jsonString, XContentType.JSON);
            //这个局部文档同样可以使用Map、XContentBuilder、对象key-value pair提供，与前文类似
            request.upsert(jsonString, XContentType.JSON);//这里支持一种upsert操作
        }
    }

    static class TermVectorsAPI{
        static void first() throws Exception {
            // TermVectorsRequest request = new TermVectorsRequest("authors", "1");
            // request.setFields("user");
            XContentBuilder docBuilder = XContentFactory.jsonBuilder();
            docBuilder.startObject().field("teacher_id", "5763").endObject();
            TermVectorsRequest request = new TermVectorsRequest(INDEX_TEACHER2, docBuilder);
            request.setFieldStatistics(false);//省略文档数量、文档频率总数、sum of total term frequencies
            request.setTermStatistics(true);//展示总分词频率和文档频率
            request.setPositions(false);
            request.setOffsets(false);
            request.setPayloads(false);

            Map<String, Integer> filterSettings = new HashMap<>();
            filterSettings.put("max_num_terms", 3);
            filterSettings.put("min_term_freq", 1);
            filterSettings.put("max_term_freq", 10);
            filterSettings.put("min_doc_freq", 1);
            filterSettings.put("max_doc_freq", 100);
            filterSettings.put("min_word_length", 1);
            filterSettings.put("max_word_length", 10);
            request.setFilterSettings(filterSettings);

            // Map<String, String> perFieldAnalyzer = new HashMap<>();
            // perFieldAnalyzer.put("user", "keyword");
            // request.setPerFieldAnalyzer(perFieldAnalyzer);
            request.setRealtime(false);
            // request.setRouting("routing");

            TermVectorsResponse response = client.termvectors(request, RequestOptions.DEFAULT);
            String index = response.getIndex();
            String type = response.getType();
            String id = response.getId();
            boolean found = response.getFound();
            logger.info("index = {}, type = {}, id = {}, found = {}", index, type, id, found);
            logger.info("term vectors = {}", response.getTermVectorsList());
            for (TermVectorsResponse.TermVector tv : response.getTermVectorsList()) {
                String fieldname = tv.getFieldName();
                int docCount = tv.getFieldStatistics().getDocCount();
                long sumTotalTermFreq =
                        tv.getFieldStatistics().getSumTotalTermFreq();
                long sumDocFreq = tv.getFieldStatistics().getSumDocFreq();
                logger.info("docCount = {}, sumTotalTermFreq = {}, sumDocFreq = {}", docCount, sumTotalTermFreq, sumDocFreq);
                if (tv.getTerms() != null) {
                    List<Term> terms =
                            tv.getTerms();
                    for (TermVectorsResponse.TermVector.Term term : terms) {
                        String termStr = term.getTerm();
                        int termFreq = term.getTermFreq();
                        int docFreq = term.getDocFreq();
                        long totalTermFreq = term.getTotalTermFreq();
                        float score = term.getScore();
                        logger.info("termStr = {}, termFreq = {}, docFreq = {}, totalTermFreq = {}, score = {}", termStr, termFreq, docFreq, totalTermFreq, score);
                        if (term.getTokens() != null) {
                            List<TermVectorsResponse.TermVector.Token> tokens =
                                    term.getTokens();
                            for (TermVectorsResponse.TermVector.Token token : tokens) {
                                int position = token.getPosition();
                                int startOffset = token.getStartOffset();
                                int endOffset = token.getEndOffset();
                                String payload = token.getPayload();
                                logger.info("position = {}, startOffset = {}, endOffset = {}, payload = {}", position, startOffset, endOffset, payload);
                            }
                        }
                    }
                }
            }
        }
    }

    //一次请求同时执行index、CRUD操作
    static class BulkAPI{
        static void first() throws IOException {
            BulkRequest request = new BulkRequest();
            request.add(new IndexRequest(INDEX_TEACHER2).id("1").source(XContentType.JSON,"field", "foo"));
            request.add(new IndexRequest(INDEX_TEACHER2).id("2").source(XContentType.JSON,"field", "bar"));
            request.add(new UpdateRequest(INDEX_TEACHER2, "2").doc(XContentType.JSON,"other", "test"));
            //可选参数
            request.timeout(TimeValue.timeValueMinutes(2));
            request.timeout("2m");
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
            request.setRefreshPolicy("wait_for");
            request.waitForActiveShards(2);
            request.waitForActiveShards(ActiveShardCount.ALL);

            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
            if (bulkResponse.hasFailures()){
                //快速检查请求是否存在失败
            }
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if (bulkItemResponse.isFailed()){
                    //具体判断是哪个请求存在错误
                    Failure failure = bulkItemResponse.getFailure();
                }
                DocWriteResponse itemResponse = bulkItemResponse.getResponse();
                switch (bulkItemResponse.getOpType()) {
                    case INDEX:
                    case CREATE:
                        IndexResponse indexResponse = (IndexResponse) itemResponse;
                        break;
                    case UPDATE:
                        UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                        break;
                    case DELETE:
                        DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                }
            }
        }

        //BulkProcessor 在异步执行bulk时使用，简化BulkAPI 提供了一个工具类可以无感知地执行index/update/delete操作
        static void bulkProcessor() throws InterruptedException {
            BulkProcessor.Listener listener = new BulkProcessor.Listener() {
                @Override
                public void beforeBulk(long executionId, BulkRequest request) {
                    int numofAction = request.numberOfActions();
                }
                @Override
                public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                    if (response.hasFailures()){
                        //部分失败的BulkRequest
                    }
                }
                @Override
                public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                    //整个bulkRequest失败
                    logger.error("Failed to execute bulk: {}", failure.getCause());
                }
            };
            Builder builder = BulkProcessor.builder(
                    (request, bulkListener) -> client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                    listener);
            builder.setBulkActions(500);//设置刷新新的bulk request的时间 默认1000，-1表示禁用
            builder.setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB));//清空请求缓存的时机 - 默认5Mb，-1表示禁用
            builder.setConcurrentRequests(0);//设置并发执行的请求数量
            builder.setFlushInterval(TimeValue.timeValueSeconds(10L));
            builder.setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3));
            BulkProcessor bulkProcessor = builder.build();
            //BulkProcessor创建完成后可以增加请求任务
            IndexRequest one = new IndexRequest("posts").id("1")
                    .source(XContentType.JSON, "title", "In which order are my Elasticsearch queries executed?");
            IndexRequest two = new IndexRequest("posts").id("2")
                    .source(XContentType.JSON, "title", "Current status and upcoming changes in Elasticsearch");
            IndexRequest three = new IndexRequest("posts").id("3")
                    .source(XContentType.JSON, "title", "The Future of Federated Search in Elasticsearch");
            bulkProcessor.add(one);
            bulkProcessor.add(two);
            bulkProcessor.add(three);


            //bulk请求需要调用awaitClose方法进行关闭，awaitClose会等待所有的请求执行完，或者是在某个超时时间到达后关闭
            boolean terminated = bulkProcessor.awaitClose(30L, TimeUnit.SECONDS);
            //而close方法会立刻关闭BulkRequest
            //两个方法都会清空请求队列，并且禁止再加入新的请求
        }
    }

    static class SearchAPIs{

    }

    public static void main(String[] args){
        try {
            TermVectorsAPI.first();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
