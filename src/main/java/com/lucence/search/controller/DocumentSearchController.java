package com.lucence.search.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucence.search.pojo.CustomErrorType;
import com.lucence.search.pojo.ResultsPojo;
import com.lucence.search.searchPosition.LuceneSearchIndex;
import com.lucence.search.searchPosition.LuceneWriteIndex; 

@RestController
@CrossOrigin
@RequestMapping("/api/") 
public class DocumentSearchController {

	public static final Logger logger = LoggerFactory.getLogger(DocumentSearchController.class);
 
 
	// -------------------Get File for lucence---------------------------------------------

	@SuppressWarnings({ "unchecked", "rawtypes" }) 
	@RequestMapping(value = "documentSearchAllFile/listAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ResultsPojo>> searchDocumentAllFile(
			@RequestParam(name="pageNumber",required=false,defaultValue = "1") Integer pageNumber,
			@RequestParam(name="pageSize",required=false,defaultValue = "20") Integer pageSize, 
					@RequestParam(name="searchedWord",required=true) String searchedWord) {
		try {


	        long startTime = System.nanoTime();
	        ExecutorService executor = Executors.newFixedThreadPool(6);
	        long endTime = System.nanoTime();
	        System.out.println(endTime-startTime+" nanoseconds taken"); 
		   ArrayList<ResultsPojo> searchResultList = new ArrayList<ResultsPojo>();		 
		   List<ResultsPojo> documentList = LuceneSearchIndex.getDoc(searchedWord, searchResultList,pageNumber,pageSize);

			return new ResponseEntity<List<ResultsPojo>>(documentList, HttpStatus.OK); 
		} catch (Exception e) {
			logger.error("NewsController exception");
			return new ResponseEntity(new CustomErrorType("1100", "System Exception"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	} 
	
	// -------------------Create Index File by Lucence ------------------------------------------

	@SuppressWarnings({ "unchecked", "rawtypes" }) 
	@RequestMapping(value = "documentSearch/createIndexedFiles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createIndexedFiles() {
		try { 
			final File folder = new File("inputFiles");

			List<String> result = new ArrayList<>();

			new LuceneWriteIndex();
			LuceneWriteIndex.search(folder, result);
			// Input folder
			String docsPath = "inputFiles";
			// Output folder
			String indexPath = "indexedFiles";
			// org.apache.lucene.store.Directory instance
			Directory dir = FSDirectory.open(Paths.get(indexPath));

			// analyzer with the default stop words
			Analyzer analyzer = new StandardAnalyzer(LuceneSearchIndex.stopSet);

			// IndexWriter Configuration
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);

			// IndexWriter writes new index files to the directory
			IndexWriter writer = new IndexWriter(dir, iwc);

//	                for (String s : result) { 
			// Input Path Variable
			final Path docDir = Paths.get(docsPath);
			// Its recursive method to iterate all files and directories
			LuceneWriteIndex.indexDocs(writer, docDir);

			System.out.println(writer.maxDoc() + " documents written");
//	                }
	                writer.close(); 
			 
			return new ResponseEntity<String>("Success", HttpStatus.OK);
		} catch (Exception e) {
			logger.error("DocumentSearchController exception");
			return new ResponseEntity(new CustomErrorType("1100", "System Exception"),HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	} 
}