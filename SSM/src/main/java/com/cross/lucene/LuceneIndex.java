package com.cross.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.cross.pojo.User;

public class LuceneIndex {

	private Directory directory = null;
	
	/**
	 * 获取IndexWriter实例
	 * @return
	 * @throws IOException
	 */
	private IndexWriter getWriter() throws IOException {
		directory = FSDirectory.open(Paths.get("C://lucene"));
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(directory, config);
		return writer;
	}
	
	/**
	 * 添加数据
	 * @param user
	 * @throws IOException
	 */
	public void addIndex(User user) throws IOException{
		IndexWriter writer = getWriter();
		Document doc = new Document();
		
		/**
		 * yes是会将数据存进索引，如果查询结果中需要将记录显示出来就要存进去，如果查询结果
		 * 只是显示标题之类的就可以不用存，而且内容过长不建议存进去
		 * 使用TextField类是可以用于查询的。
		 */
		doc.add(new StringField("id", String.valueOf(user.getUserId()), Field.Store.YES));
		doc.add(new TextField("username", user.getUsername(), Field.Store.YES));
		doc.add(new TextField("description",user.getDescription(), Field.Store.YES));
		
		writer.addDocument(doc);
		writer.close();
	}
	
	/**
	 * 更新索引
	 * @param user
	 * @throws IOException
	 */
	public void updateIndex(User user) throws IOException{
		IndexWriter writer = getWriter();
		Document doc = new Document();
		
		doc.add(new StringField("id", String.valueOf(user.getUserId()), Field.Store.YES));
		doc.add(new TextField("username", user.getUsername(), Field.Store.YES));
		doc.add(new TextField("description",user.getDescription(), Field.Store.YES));
		
		Term term = new Term("id", String.valueOf(user.getUserId()));
		writer.updateDocument(term, doc);
		
		writer.close();
	}
	
	/**
	 * 删除索引
	 * @param userId
	 * @throws IOException
	 */
	public void deleteIndex(String userId) throws IOException{
		IndexWriter writer = getWriter();
		
		Term term = new Term("id", userId);
		writer.deleteDocuments(term);
		writer.forceMergeDeletes();
		writer.commit();
		writer.close();
	}
	
	/**
	 * 查询用户
	 * @param q 查询关键字
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws InvalidTokenOffsetsException 
	 */
	public List<User> searchBlog(String q) throws IOException, ParseException, InvalidTokenOffsetsException {
		
		directory = FSDirectory.open(Paths.get("C://lucene"));
		
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		
		Builder builder = new Builder();
		
		SmartChineseAnalyzer analyzer =  new SmartChineseAnalyzer();
		
		QueryParser parser = new QueryParser("username", analyzer);
		Query query = parser.parse(q);
		
		QueryParser parser2 = new QueryParser("description", analyzer);
		Query query2 = parser2.parse(q);
		
		builder.add(query, Occur.SHOULD);
		builder.add(query2, Occur.SHOULD);
		
		TopDocs topDocs = searcher.search(builder.build(), 100);
		QueryScorer scorer = new QueryScorer(query);// 高亮
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
		
		//这里可以根据自己的需要来自定义查找关键字高亮时的样式。
		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
		Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
		highlighter.setTextFragmenter(fragmenter);
		
		List<User> userList = new LinkedList<User>();
		
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			
			User user=new User();
			
			Integer id = Integer.parseInt(doc.get(("id")));
			String username = doc.get("username");
			String description = doc.get("description");
			
			user.setUserId(id);
			user.setDescription(description);
			
			if(username != null){
				TokenStream tokenStream = analyzer.tokenStream("username", new StringReader(username));
				String hname= highlighter.getBestFragment(tokenStream, username);
				if(StringUtils.isEmpty(hname)){
					user.setUsername(username);
				}else{
					user.setUsername(hname);
				}
			}
			
			if(description != null){
				TokenStream tokenStream = analyzer.tokenStream("description", new StringReader(description));
				String hContent = highlighter.getBestFragment(tokenStream, description);
				if(StringUtils.isEmpty(hContent)){
					if(description.length() <= 200){
						user.setDescription(description);
					}else{
						user.setDescription(description.substring(0, 200));
					}
				}else{
					user.setDescription(hContent);
				}
			}
			
			userList.add(user);
		}
		
		return userList;
	}
}
