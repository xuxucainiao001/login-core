package dongxun.com.login;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;


public class ElasticSearchTest {

	private static RestHighLevelClient client = null;

	static {
		client = new RestHighLevelClient(RestClient.builder(new HttpHost("39.107.67.13", 9200, "http")));
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		StopWatch sw =new StopWatch();
		sw.start();
		get();
		sw.stop();
		System.out.println(sw.getTime(TimeUnit.MILLISECONDS));;
	}
	
	public static void agentmain (String agentArgs, Instrumentation inst) {

	}

	public static void get() throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("users");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder bq = QueryBuilders.boolQuery()
				.should(QueryBuilders.matchQuery("name", "李四"))
				.should(QueryBuilders.matchQuery("name", "张三"));
		searchSourceBuilder.query(bq);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse;
		try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			System.out.println(searchResponse.status());
			SearchHits searchHits = searchResponse.getHits();
			searchHits.forEach(sh -> {
				Map<String, Object> result = sh.getSourceAsMap();
				System.out.println(sh.getId() + ":" + result);
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			//client.close();
		}

	}

	public static void add() {
		Map<String, Object> sourceMap = new HashMap<>();
		sourceMap.put("orderCode", "123456");
		sourceMap.put("price", new BigDecimal("10.50"));
		IndexRequest indexRequest = new IndexRequest();
		indexRequest.source(sourceMap);
		indexRequest.index("orders");
		IndexResponse indexResponse;
		try {
			indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
			System.out.println(indexResponse.getId() + ":" + indexResponse);
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
