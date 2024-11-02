package testMatcher;

//import java.io.File;
import java.util.ArrayList;

//import analyzer.Analyzer;
//import analyzer.AnalyzerVariable;
//import exportFileGenerator.ExportFileGenerator;
//import exportFileGenerator.MatchingMethod;
//import exportFileGenerator.NaturalTest;
//import exportFileGenerator.NaturalTestClass;
import paramaterExtracter.ExtractClass;
//import paramaterExtracter.ExtractMethod;
import paramaterExtracter.Instance;
//import paramaterExtracter.ParamaterExtracter;
//import paramaterExtracter.PutInstanceVariable;
//import pathExtracter.PathExtracter;
import pathExtracter.TraceMethodBlock;
import testAnalyzer.Test;
//import testAnalyzer.TestAssertion;
import testAnalyzer.TestClass;
import testAnalyzer.TestMethod;
//import tracer.Lexer;
import tracer.Trace;
//import tracer.ValueOption;

public class TestMatcher {

//	public static void main(String args[]) {
//		TestMatcher executer = new TestMatcher();
//		
//		ArrayList<String> inputFileNameLists = executer.getInputFileNameLists();
//		ArrayList<String> inputEvoSuiteTestFileNameLists = executer.getInputEvoSuiteTestFileNameLists();
//		ArrayList<String> inputEvoSuiteTestTraceFileNameLists = executer.getInputEvoSuiteTestTraceFileNameLists();
//		String integrationTestTrace = "src/test/resources/IntegrationTestTrace/trace.json";
//		
//		// 1. product code analyze
//		Analyzer analyzer = new Analyzer();
//		analyzer.run(inputFileNameLists);
//		
//		// 2. evosuite test
//		PathExtracter pathExtracter = new PathExtracter();
//		ArrayList<TestClass> evoSuiteTestClassLists = pathExtracter.run(analyzer, inputEvoSuiteTestFileNameLists, inputEvoSuiteTestTraceFileNameLists);
//		
//		// 3. extract natural paramater
//		File file = new File(integrationTestTrace);
//		Lexer lexer = new Lexer(file);
//		ArrayList<Trace> traceLists = lexer.getTraceLists();
//		ParamaterExtracter paramaterExtracter = new ParamaterExtracter();
//		ArrayList<ExtractClass> extractClassLists = paramaterExtracter.run(analyzer.getMethodLists(), analyzer.getVariableLists(), traceLists);
//		
//		// 4. analyze extract path
//		executer.analyzeExtractPath(evoSuiteTestClassLists);
//		executer.analyzeExtractClassExtractPath(extractClassLists);
//		
//		// 5. summarize same extract method
//		executer.summarizeSameExecutePath(evoSuiteTestClassLists);
//		executer.summarizeExtractClassSameExecutePath(extractClassLists);
//
//		// 6. matching test
//		ArrayList<Result> resultLists = executer.matchingSameExecutePathTest(evoSuiteTestClassLists, extractClassLists);
//		for(int i = 0; i < resultLists.size(); i++) {
//			resultLists.get(i).display();
//		}
//		
//		// 7. create natural test
//		executer.setTraceMethodBlock(evoSuiteTestClassLists);
//	}
	
	public ArrayList<Result> run(ArrayList<TestClass> evoSuiteTestClassLists, ArrayList<ExtractClass> extractClassLists){
		// 4. analyze extract path
		this.analyzeExtractPath(evoSuiteTestClassLists);
		this.analyzeExtractClassExtractPath(extractClassLists);
		
		// 5. summarize same extract method
		this.summarizeSameExecutePath(evoSuiteTestClassLists);
		this.summarizeExtractClassSameExecutePath(extractClassLists);
		
		this.setTraceMethodBlock(evoSuiteTestClassLists);
		
		// 6. matching test
		ArrayList<Result> resultLists = this.matchingSameExecutePathTest(evoSuiteTestClassLists, extractClassLists);
		
		return resultLists;
	}
	
	private void setTraceMethodBlock(ArrayList<TestClass> evoSuiteTestClassLists) {
		for(int classNum = 0; classNum < evoSuiteTestClassLists.size(); classNum++) {
			TestClass testClass = evoSuiteTestClassLists.get(classNum);
			ArrayList<Test> testLists = testClass.getTestLists();
			for(int testNum = 0; testNum < testLists.size(); testNum++) {
				Test test = testLists.get(testNum);
				ArrayList<TraceMethodBlock> blockLists = test.getTraceMethodBlockLists();
				ArrayList<TestMethod> methodLists = test.getMethodLists();
				
				int blockBorderNum = 0;
				for(int methodNum = 0; methodNum < methodLists.size(); methodNum++) {
					TestMethod testMethod = methodLists.get(methodNum);
					String methodName = testMethod.getMethodName();
					String ownerClass = "";
					if(testMethod.getAnalyzerMethod() != null) {
						ownerClass = testMethod.getAnalyzerMethod().getOwnerClass().getName();
					}else {
						ownerClass = testMethod.getConstructorClass();
					}
					
					for(int blockNum = blockBorderNum; blockNum < blockLists.size(); blockNum++) {
						Trace firstTrace = blockLists.get(blockNum).getTraceLists().get(0);
						String[] split = firstTrace.getFilename().split("/");
						String className = split[split.length - 1];
						
						if(className.equals(ownerClass) && firstTrace.getMname().equals(methodName)) {
							blockBorderNum = blockNum + 1;
							testMethod.setTraceMethodBlock(blockLists.get(blockNum));
							break;
						}
					}
				}
			}
		}
	}
	
//	private ExecutePath createExecutePath(TraceMethodBlock traceMethodBlock) {
//		ArrayList<Trace> traceLists = traceMethodBlock.getTraceLists();
//		
//		ArrayList<Integer> lineLists = new ArrayList<Integer>();
//		
//		for(int traceNum = 0; traceNum < traceLists.size(); traceNum++) {
//			Trace trace = traceLists.get(traceNum);
//			
//			if(lineLists.size() == 0) {
//				lineLists.add(trace.getLine());
//			}else {
//				if(!lineLists.contains(trace.getLine())) {
//					lineLists.add(trace.getLine());
//				}
//			}
//		}
//		
//		ExecutePath executePath = new ExecutePath(lineLists, traceLists.get(0).getMname(), traceLists.get(0).getFilename());
//		return executePath;
//	}
	
	private ArrayList<Result> matchingSameExecutePathTest(ArrayList<TestClass> evoSuiteTestClassLists, ArrayList<ExtractClass> extractClassLists) {
		ArrayList<Result> resultLists = new ArrayList<Result>();
		
		for(int testClassNum = 0; testClassNum < evoSuiteTestClassLists.size(); testClassNum++) {
			TestClass evoSuiteTestClass = evoSuiteTestClassLists.get(testClassNum);
			String ownerClassName = evoSuiteTestClass.getClassName().replace("_ESTest", "");
			
			ExtractClass extractClass = null;
			for(int extractClassNum = 0; extractClassNum < extractClassLists.size(); extractClassNum++) {
				ExtractClass targetExtractClass = extractClassLists.get(extractClassNum);
				
				if(targetExtractClass.getOwnerClass().equals(ownerClassName)) {
					extractClass = targetExtractClass;
				}
			}
			
			Result result = new Result(evoSuiteTestClass);
			resultLists.add(result);

			ArrayList<SameExecutePath> evoSuiteSameExecutePathLists = evoSuiteTestClass.getSameExecutePathLists();
			ArrayList<SameExecuteExtractPath> addPathLists = new ArrayList<SameExecuteExtractPath>();
			for(int sameExecutePathNum = 0; sameExecutePathNum < evoSuiteSameExecutePathLists.size(); sameExecutePathNum++) {
				SameExecutePath evoSuiteSameExecutePath = evoSuiteSameExecutePathLists.get(sameExecutePathNum);
				ArrayList<ExecutePath> evoSuiteExecutePathLists = evoSuiteSameExecutePath.getTestLists().get(0).getExtractPathLists();
				
				if(extractClass == null) {
					result.addNotMatchingEvoSuiteLists(evoSuiteSameExecutePath);
				}else {
					result.setExtractClass(extractClass);
					ArrayList<SameExecuteExtractPath> sameExecuteExtractPathLists = extractClass.getSameExcuteExtractPathLists();
					
					// perfect matching
					boolean isMatchEvoSuite = false;
					for(int extractPathNum = 0; extractPathNum < sameExecuteExtractPathLists.size(); extractPathNum++) {
						SameExecuteExtractPath sameExecuteExtractPath = sameExecuteExtractPathLists.get(extractPathNum);
						ArrayList<ExecutePath> extractExecutePathLists = sameExecuteExtractPath.getInstanecLists().get(0).getExecutePathLists();
						ArrayList<ExecutePath> forMatchingLists = new ArrayList<ExecutePath>();
						
						if(extractExecutePathLists.size() >= evoSuiteExecutePathLists.size()) {
							// size modify
							for(int evoSuitePathNum = 0; evoSuitePathNum < evoSuiteExecutePathLists.size(); evoSuitePathNum++) {
								forMatchingLists.add(extractExecutePathLists.get(evoSuitePathNum));
							}
							
							if(this.isSameExecutePath(evoSuiteExecutePathLists, forMatchingLists)) {
								MatchingResult matchingResult = new MatchingResult(evoSuiteSameExecutePath, sameExecuteExtractPath);
								result.addMatchingResultLists(matchingResult);
								isMatchEvoSuite = true;
								addPathLists.add(sameExecuteExtractPath);
								break;
							}
						}
					}
					
					// partially matching
					if(!isMatchEvoSuite) {
						ArrayList<SameExecuteExtractPath> partiallyMatchingExtractPathLists = this.partiallyMatching(evoSuiteExecutePathLists, sameExecuteExtractPathLists);
						if(partiallyMatchingExtractPathLists.size() != 0) {
							PartiallyMatchingResult matchingResult = new PartiallyMatchingResult(evoSuiteSameExecutePath, partiallyMatchingExtractPathLists);
							result.addPartiallyMatchingResultLists(matchingResult);
							
							for(int parNum = 0; parNum < partiallyMatchingExtractPathLists.size(); parNum++) {
								addPathLists.add(partiallyMatchingExtractPathLists.get(parNum));
							}
							
							continue;
						}else {
							result.addNotMatchingEvoSuiteLists(evoSuiteSameExecutePath);
						}
					}
					
				}
			}
			
			if(extractClass != null) {
				ArrayList<SameExecuteExtractPath> pathLists = extractClass.getSameExcuteExtractPathLists();
				for(int pathNum = 0; pathNum < pathLists.size(); pathNum++) {
					if(!addPathLists.contains(pathLists.get(pathNum))) {
						result.addNotMatchingExtractLists(pathLists.get(pathNum));
					}
				}
			}
		}
		
		return resultLists;
	}
	
	private ArrayList<SameExecuteExtractPath> partiallyMatching(ArrayList<ExecutePath> evoSuiteExecutePathLists, ArrayList<SameExecuteExtractPath> sameExecuteExtractPathLists) {
		ArrayList<SameExecuteExtractPath> partiallyMatchingLists = new ArrayList<SameExecuteExtractPath>();
		
		for(int sameExecuteExtractPathNum = 0; sameExecuteExtractPathNum < sameExecuteExtractPathLists.size(); sameExecuteExtractPathNum++) {
			SameExecuteExtractPath targetSameExecuteExtractPath = sameExecuteExtractPathLists.get(sameExecuteExtractPathNum);
			ArrayList<ExecutePath> extractExecutePathLists = targetSameExecuteExtractPath.getInstanecLists().get(0).getExecutePathLists();
			
			// judge contain execute path
			boolean isContain = true;
			for(int evoSuiteExecutePathNum = 0; evoSuiteExecutePathNum < evoSuiteExecutePathLists.size(); evoSuiteExecutePathNum++) {
				boolean isContainUnit = false;
				ExecutePath evoSuiteExecutePath = evoSuiteExecutePathLists.get(evoSuiteExecutePathNum);
//				System.out.println();
//				evoSuiteExecutePath.display();
				
				for(int extractExecutePathNum = 0; extractExecutePathNum < extractExecutePathLists.size(); extractExecutePathNum++) {
					if(this.isSameExecutePath(evoSuiteExecutePath, extractExecutePathLists.get(extractExecutePathNum))) {
						isContainUnit = true;
					}
					
//					extractExecutePathLists.get(extractExecutePathNum).display();
				}
				
//				System.out.println(isContainUnit);
				
				if(!isContainUnit) {
					isContain = false;
					break;
				}
			}
			
			if(isContain) {
				partiallyMatchingLists.add(targetSameExecuteExtractPath);
			}
		}

		return partiallyMatchingLists;
	}
	
	private boolean isSameExecutePath(ExecutePath path1, ExecutePath path2) {
		if(!path1.getMname().equals(path2.getMname())) {
			return false;
		}
		
		if(!path1.getFileName().equals(path2.getFileName())) {
			return false;
		}
		
		ArrayList<Integer> lineLists1 = path1.getLineLists();
		ArrayList<Integer> lineLists2 = path2.getLineLists();
		
		if(lineLists1.size() != lineLists2.size()) {
			return false;
		}
		
		for(int lineNum = 0; lineNum < lineLists1.size(); lineNum++) {
			if(lineLists1.get(lineNum) != lineLists2.get(lineNum)) {
				return false;
			}
		}
		
		return true;
	}
	
	private void summarizeExtractClassSameExecutePath(ArrayList<ExtractClass> extractClassLists) {
		for(int classNum = 0; classNum < extractClassLists.size(); classNum++) {
			ExtractClass extractClass = extractClassLists.get(classNum);
			ArrayList<SameExecuteExtractPath> sameExecuteExtractPathLists = new ArrayList<SameExecuteExtractPath>();
			
			ArrayList<Instance> instanceLists = extractClass.getInstanceLists();
			for(int instanceNum = 0; instanceNum < instanceLists.size(); instanceNum++) {
				Instance instance = instanceLists.get(instanceNum);
				
				if(sameExecuteExtractPathLists.size() == 0) {
					SameExecuteExtractPath sameExecuteExtractPath = new SameExecuteExtractPath();
					sameExecuteExtractPath.addInstanceLists(instance);
					sameExecuteExtractPathLists.add(sameExecuteExtractPath);
					
				}else {
					boolean isRegistered = false;
					for(int sameExecuteNum = 0; sameExecuteNum < sameExecuteExtractPathLists.size(); sameExecuteNum++) {
						ArrayList<ExecutePath> instanceExecutePathLists = instance.getExecutePathLists();
						ArrayList<ExecutePath> listExecutePathLists = sameExecuteExtractPathLists.get(sameExecuteNum).getInstanecLists().get(0).getExecutePathLists();
						if(this.isSameExecutePath(instanceExecutePathLists, listExecutePathLists)) {
							sameExecuteExtractPathLists.get(sameExecuteNum).addInstanceLists(instance);
							isRegistered = true;
							break;
						}
					}
					
					if(!isRegistered) {
						SameExecuteExtractPath sameExecuteExtractPath = new SameExecuteExtractPath();
						sameExecuteExtractPath.addInstanceLists(instance);
						sameExecuteExtractPathLists.add(sameExecuteExtractPath);
					}
				}
			}
			
			extractClass.setSameExecuteExtractPathLists(sameExecuteExtractPathLists);
			
		}
	}
	
	private boolean isSameExecutePath(ArrayList<ExecutePath> lists1, ArrayList<ExecutePath> lists2) {
		if(lists1.size() == lists2.size()) {
			for(int listNum = 0; listNum < lists1.size(); listNum++) {
				ExecutePath executePath1 = lists1.get(listNum);
				ExecutePath executePath2 = lists2.get(listNum);
				
				if(!(executePath1.getMname().equals(executePath2.getMname()) && executePath1.getFileName().equals(executePath2.getFileName()))) {
					return false;
				}
				
				ArrayList<Integer> lineLists1 = executePath1.getLineLists();
				ArrayList<Integer> lineLists2 = executePath2.getLineLists();
				
				if(lineLists1.size() != lineLists2.size()) {
					return false;
				}
				
				for(int lineNum = 0; lineNum < lineLists1.size(); lineNum++) {
					if(lineLists1.get(lineNum) != lineLists2.get(lineNum)) {
						return false;
					}
				}
			}
			
			return true;
		}else {
			return false;
		}
	}
	
	private void analyzeExtractClassExtractPath(ArrayList<ExtractClass> extractClassLists) {
		for(int classNum = 0; classNum < extractClassLists.size(); classNum++) {
			ExtractClass extractClass = extractClassLists.get(classNum);
			ArrayList<Instance> instanceLists = extractClass.getInstanceLists();
			for(int instanceNum = 0; instanceNum < instanceLists.size(); instanceNum++) {
				Instance instance = instanceLists.get(instanceNum);
				ArrayList<TraceMethodBlock> traceMethodBlockLists = instance.getTraceMethodBlockLists();
				
				for(int i = 0; i < traceMethodBlockLists.size(); i++) {
					for(int j = 0; j < traceMethodBlockLists.size() - 1; j++) {
						TraceMethodBlock frontTraceMethodBlock = traceMethodBlockLists.get(j);
						TraceMethodBlock backTraceMethodBlock = traceMethodBlockLists.get(j + 1);
						
						int frontSeqNum = frontTraceMethodBlock.getTraceLists().get(0).getSeqNum();
						int backSeqNum = backTraceMethodBlock.getTraceLists().get(0).getSeqNum();
						
						if(frontSeqNum > backSeqNum) {
							traceMethodBlockLists.set(j, backTraceMethodBlock);
							traceMethodBlockLists.set(j+1, frontTraceMethodBlock);
						}
						
					}
				}
				
				for(int blockNum = 0; blockNum < traceMethodBlockLists.size(); blockNum++) {
					TraceMethodBlock traceMethodBlock = traceMethodBlockLists.get(blockNum);
					ArrayList<Trace> traceLists = traceMethodBlock.getTraceLists();
					
					ArrayList<Integer> lineLists = new ArrayList<Integer>();
					
					for(int traceNum = 0; traceNum < traceLists.size(); traceNum++) {
						Trace trace = traceLists.get(traceNum);
						
						if(lineLists.size() == 0) {
							lineLists.add(trace.getLine());
						}else {
							if(!lineLists.contains(trace.getLine())) {
								lineLists.add(trace.getLine());
							}
						}
					}
					
					ExecutePath executePath = new ExecutePath(lineLists, traceLists.get(0).getMname(), traceLists.get(0).getFilename());
					instance.addExecutePathLists(executePath);
					
				}
				
//				System.out.println("~~~~~~~");
//				instance.display();
//				for(int i = 0; i < instance.getExecutePathLists().size(); i++) {
//					instance.getExecutePathLists().get(i).display();
//				}
//				System.out.println();
			}
			
		}
	}

	
	// TraceMethodBlockから実行経路を解析する
	private void analyzeExtractPath(ArrayList<TestClass> testClassLists) {
		for(int testClassNum = 0; testClassNum < testClassLists.size(); testClassNum++) {
			TestClass testClass = testClassLists.get(testClassNum);
			
			ArrayList<Test> testLists = testClass.getTestLists();
			for(int testNum = 0; testNum < testLists.size(); testNum++) {
				Test test = testLists.get(testNum);

				ArrayList<TraceMethodBlock> traceMethodBlockLists = test.getTraceMethodBlockLists();
				for(int traceMethodBlockNum = 0; traceMethodBlockNum < traceMethodBlockLists.size(); traceMethodBlockNum++) {
					ArrayList<Integer> lineLists = new ArrayList<Integer>();
					TraceMethodBlock traceMethodBlock = traceMethodBlockLists.get(traceMethodBlockNum);
					ArrayList<Trace> traceLists = traceMethodBlock.getTraceLists();
					
					for(int traceNum = 0; traceNum < traceLists.size(); traceNum++) {
						Trace trace = traceLists.get(traceNum);
						
						if(lineLists.size() == 0) {
							lineLists.add(trace.getLine());
						}else {
							if(!lineLists.contains(trace.getLine())) {
								lineLists.add(trace.getLine());
							}
						}
					}
					
					ExecutePath extractPath = new ExecutePath(lineLists, traceLists.get(0).getMname(), traceLists.get(0).getFilename());
					test.addExtractPathLists(extractPath);
					
				}
			}
		}
	}
	
	// 同じ実行経路のテストをまとめる
	private void summarizeSameExecutePath(ArrayList<TestClass> testClassLists) {
		
		for(int testClassNum = 0; testClassNum < testClassLists.size(); testClassNum++) {
			ArrayList<SameExecutePath> sameExecutePathLists = new ArrayList<SameExecutePath>();
			TestClass testClass = testClassLists.get(testClassNum);
			
			ArrayList<Test> testLists = testClass.getTestLists();
			for(int testNum = 0; testNum < testLists.size(); testNum++) {
				Test test = testLists.get(testNum);
				
				if(sameExecutePathLists.size() == 0) {
					SameExecutePath sameExecutePath = new SameExecutePath();
					sameExecutePath.addTestLists(test);
					sameExecutePathLists.add(sameExecutePath);
					
				}else {
					boolean registeredFlag = false;
					for(int sameExecutePathNum = 0; sameExecutePathNum < sameExecutePathLists.size(); sameExecutePathNum++) {
						if(this.isSameExecutePath(test, sameExecutePathLists.get(sameExecutePathNum).getTestLists().get(0))) {
							sameExecutePathLists.get(sameExecutePathNum).addTestLists(test);
							registeredFlag = true;
							break;
						}
					}
					
					if(!registeredFlag) {
						SameExecutePath sameExtractPath = new SameExecutePath();
						sameExtractPath.addTestLists(test);
						sameExecutePathLists.add(sameExtractPath);
					}
				}
			}
			
			testClass.setSameExecutePathLists(sameExecutePathLists);
		
//			System.out.println("##########");
//			System.out.println(testClass.getClassName());
//			for(int i = 0; i < sameExecutePathLists.size(); i++) {
//				SameExecutePath x = sameExecutePathLists.get(i);
//				for(int j = 0; j < x.getTestLists().size(); j++) {
//					System.out.println(x.getTestLists().get(j).getMethodName());
//					ArrayList<ExecutePath> a = x.getTestLists().get(j).getExtractPathLists();
//					for(int n = 0; n < a.size(); n++) {
//						a.get(n).display();
//					}
//				}
//				
//				System.out.println();
//			}
//			System.out.println();
		}
	}
	
	private boolean isSameExecutePath(Test test1, Test test2) {
		ArrayList<ExecutePath> pathLists1 = test1.getExtractPathLists();
		ArrayList<ExecutePath> pathLists2 = test2.getExtractPathLists();
		
		if(pathLists1.size() != pathLists2.size()) {
			return false;
		}
		
		for(int pathNum = 0; pathNum < pathLists1.size(); pathNum++) {
			ExecutePath executePath1 = pathLists1.get(pathNum);
			ExecutePath executePath2 = pathLists2.get(pathNum);
			
			if(!(executePath1.getMname().equals(executePath2.getMname()) && executePath1.getFileName().equals(executePath2.getFileName()))) {
				return false;
			}
			
			ArrayList<Integer> lineLists1 = executePath1.getLineLists();
			ArrayList<Integer> lineLists2 = executePath2.getLineLists();
			
			if(lineLists1.size() != lineLists2.size()) {
				return false;
			}
			
			for(int lineNum = 0; lineNum < lineLists1.size(); lineNum++) {
				if(lineLists1.get(lineNum) != lineLists2.get(lineNum)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
//	private ArrayList<String> getInputFileNameLists(){
//		ArrayList<String> result = new ArrayList<String>();
//
//		result.add("src/test/resources/ex07/Calculator.java");
//		result.add("src/test/resources/ex07/Executer.java");
//		result.add("src/test/resources/ex07/Formula.java");
////		String path = "src/main/java/";
////		File dir = new File(path);
////		File[] files = dir.listFiles();
////		
////		ArrayList<String> filePathLists = new ArrayList<String>();
////		
////		for(int i = 0; i < files.length; i++) {
////			String filePath = files[i].getPath();
////			
////			if(!filePath.contains(".java") && !filePath.contains(".class")) {
////				filePathLists.add(filePath);
////			}
////		}
////		
////		while(filePathLists.size() > 0) {
////			File pathDir = new File(filePathLists.get(0));
////			filePathLists.remove(0);
////			
////			File[] pathDirFiles = pathDir.listFiles();
////			
////			for(int i = 0; i < pathDirFiles.length; i++) {
////				String pathFilePath = pathDirFiles[i].getPath();
////				
////				if(pathFilePath.contains(".java")) {
////					result.add(pathFilePath);
////				}else if(!pathFilePath.contains(".class")){
////					filePathLists.add(pathFilePath);
////				}
////			}
////			
////		}
//
//		return result;
//	}
//	
//	private ArrayList<String> getInputEvoSuiteTestFileNameLists(){
//		ArrayList<String> result = new ArrayList<String>();
//		
//		result.add("src/test/resources/EvoSuite/Calculator_ESTest.java");
//		result.add("src/test/resources/EvoSuite/Executer_ESTest.java");
//		result.add("src/test/resources/EvoSuite/Formula_ESTest.java");
//		
////		String path = "src/test/java/";
////		File dir = new File(path);
////		File[] files = dir.listFiles();
////		
////		ArrayList<String> filePathLists = new ArrayList<String>();
////		
////		for(int i = 0; i < files.length; i++) {
////			String filePath = files[i].getPath();
////			
////			if(!filePath.contains(".java") && !filePath.contains(".class")) {
////				filePathLists.add(filePath);
////			}
////		}
////		
////		while(filePathLists.size() > 0) {
////			File pathDir = new File(filePathLists.get(0));
////			filePathLists.remove(0);
////			
////			File[] pathDirFiles = pathDir.listFiles();
////			
////			if(pathDirFiles != null) {
////				for(int i = 0; i < pathDirFiles.length; i++) {
////					String pathFilePath = pathDirFiles[i].getPath();
////					
////					if(pathFilePath.contains("_ESTest.java")) {
////						result.add(pathFilePath);
////					}else if(!pathFilePath.contains(".class")){
////						filePathLists.add(pathFilePath);
////					}
////				}
////			}
////		}
//
//		return result;
//	}
//	
//	private ArrayList<String> getInputEvoSuiteTestTraceFileNameLists(){
//		ArrayList<String> result = new ArrayList<String>();
//		result.add("src/test/resources/EvoSuite/Calculator_ESTest.json");
//		result.add("src/test/resources/EvoSuite/Executer_ESTest.json");
//		result.add("src/test/resources/EvoSuite/Formula_ESTest.json");
//		
////		String path = "src/test/resources/EvoSuite/";
////		File dir = new File(path);
////		File[] files = dir.listFiles();
////		for(int i = 0; i < files.length; i++) {
////			String pathFilePath = files[i].getPath();
////			
////			if(pathFilePath.contains(".json")) {
////				result.add(pathFilePath);
////			}
////		}
//
//		return result;
//	}

}
