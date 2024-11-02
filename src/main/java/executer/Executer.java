package executer;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import analyzer.Analyzer;
import executionPartGenerator.ExecutionClass;
import executionPartGenerator.ExecutionPartGenerator;
import naturalTestGenerator.NaturalTestGenerator;
import paramaterExtracter.ExtractClass;
import paramaterExtracter.ParamaterExtracter;
import pathExtracter.PathExtracter;
import testAnalyzer.TestClass;
import testMatcher.Result;
import testMatcher.TestMatcher;
import tracer.Lexer;
import tracer.Trace;

public class Executer {
	
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Executer executer = new Executer();
		
		ArrayList<String> inputFileNameLists = executer.getInputFileNameLists();
		ArrayList<String> inputEvoSuiteTestFileNameLists = executer.getInputEvoSuiteTestFileNameLists();
		ArrayList<String> inputEvoSuiteTestTraceFileNameLists = executer.getInputEvoSuiteTestTraceFileNameLists();
		String integrationTestTrace = "src/test/resources/IntegrationTestTrace/trace.json";
		
		// 1. product code analyze
		Analyzer analyzer = new Analyzer();
		analyzer.run(inputFileNameLists);
		
		// 2. evosuite test
		PathExtracter pathExtracter = new PathExtracter();
		ArrayList<TestClass> evoSuiteTestClassLists = pathExtracter.run(analyzer, inputEvoSuiteTestFileNameLists, inputEvoSuiteTestTraceFileNameLists);
		
		// 3. extract natural paramater
		File file = new File(integrationTestTrace);
		Lexer lexer = new Lexer(file);
		ArrayList<Trace> traceLists = lexer.getTraceLists();
		ParamaterExtracter paramaterExtracter = new ParamaterExtracter();
		ArrayList<ExtractClass> extractClassLists = paramaterExtracter.run(analyzer.getMethodLists(), analyzer.getVariableLists(), traceLists);
	
		// 4. matching test
		TestMatcher testMatcher = new TestMatcher();
		ArrayList<Result> resultLists = testMatcher.run(evoSuiteTestClassLists, extractClassLists);
//		for(int i = 0; i < resultLists.size(); i++) {
//			resultLists.get(i).display();
//		}
		
		// 5. create executionTest
		ExecutionPartGenerator executionPartGenerator = new ExecutionPartGenerator();
		ArrayList<ExecutionClass> executionClassLists = executionPartGenerator.getExecutionClass(resultLists);
		
		// 6. create natural test
		NaturalTestGenerator naturalTestGenerator = new NaturalTestGenerator();
		naturalTestGenerator.run(executionClassLists, resultLists, analyzer, extractClassLists);
		
		// 7. delete execution Test dir
		executer.deleteExecutionTestDir();
	}
	
	private void deleteExecutionTestDir() {
		File file = new File("src/main/java/executionTest");
		File[] files = file.listFiles();
         
	    //存在するファイル数分ループして再帰的に削除
	    for(int i=0; i<files.length; i++) {
	    	files[i].delete();
	    }
         
        file.delete();
	}
	
	public ArrayList<String> getInputFileNameLists(){
		ArrayList<String> result = new ArrayList<String>();

//		result.add("src/test/resources/ex07/Calculator.java");
//		result.add("src/test/resources/ex07/Executer.java");
//		result.add("src/test/resources/ex07/Formula.java");
		String path = "src/main/java/";
		File dir = new File(path);
		File[] files = dir.listFiles();
		
		ArrayList<String> filePathLists = new ArrayList<String>();
		
		for(int i = 0; i < files.length; i++) {
			String filePath = files[i].getPath();
			
			if(!filePath.contains(".java") && !filePath.contains(".class")) {
				filePathLists.add(filePath);
			}
		}
		
		while(filePathLists.size() > 0) {
			File pathDir = new File(filePathLists.get(0));
			filePathLists.remove(0);
			
			File[] pathDirFiles = pathDir.listFiles();
			
			for(int i = 0; i < pathDirFiles.length; i++) {
				String pathFilePath = pathDirFiles[i].getPath();
				
				if(pathFilePath.contains(".java")) {
					result.add(pathFilePath);
				}else if(!pathFilePath.contains(".class") && !pathFilePath.contains(".txt")){
					filePathLists.add(pathFilePath);
				}
			}
			
		}

		return result;
	}
	
	public ArrayList<String> getInputEvoSuiteTestFileNameLists(){
		ArrayList<String> result = new ArrayList<String>();
		
//		result.add("src/test/resources/EvoSuite/Calculator_ESTest.java");
//		result.add("src/test/resources/EvoSuite/Executer_ESTest.java");
//		result.add("src/test/resources/EvoSuite/Formula_ESTest.java");
		
		String path = "src/test/java/";
		File dir = new File(path);
		File[] files = dir.listFiles();
		
		ArrayList<String> filePathLists = new ArrayList<String>();
		
		for(int i = 0; i < files.length; i++) {
			String filePath = files[i].getPath();
			
			if(!filePath.contains(".java") && !filePath.contains(".class")) {
				filePathLists.add(filePath);
			}
		}
		
		while(filePathLists.size() > 0) {
			File pathDir = new File(filePathLists.get(0));
			filePathLists.remove(0);
			
			File[] pathDirFiles = pathDir.listFiles();
			
			if(pathDirFiles != null) {
				for(int i = 0; i < pathDirFiles.length; i++) {
					String pathFilePath = pathDirFiles[i].getPath();
					
					if(pathFilePath.contains("_ESTest.java")) {
						result.add(pathFilePath);
					}else if(!pathFilePath.contains(".class")){
						filePathLists.add(pathFilePath);
					}
				}
			}
		}

		return result;
	}
	
	public ArrayList<String> getInputEvoSuiteTestTraceFileNameLists(){
		ArrayList<String> result = new ArrayList<String>();
//		result.add("src/test/resources/EvoSuite/Calculator_ESTest.json");
//		result.add("src/test/resources/EvoSuite/Executer_ESTest.json");
//		result.add("src/test/resources/EvoSuite/Formula_ESTest.json");
		
		String path = "src/test/resources/EvoSuite/";
		File dir = new File(path);
		File[] files = dir.listFiles();
		for(int i = 0; i < files.length; i++) {
			String pathFilePath = files[i].getPath();
			
			if(pathFilePath.contains(".json")) {
				result.add(pathFilePath);
			}
		}

		return result;
	}
}
