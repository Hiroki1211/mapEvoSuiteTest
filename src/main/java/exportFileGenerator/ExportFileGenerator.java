package exportFileGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import paramaterExtracter.ExtractClass;
import paramaterExtracter.ExtractMethod;
import paramaterExtracter.Instance;
import testAnalyzer.Test;
import testAnalyzer.TestClass;
import testAnalyzer.TestMethod;
import testMatcher.MatchingResult;
import testMatcher.PartiallyMatchingResult;
import testMatcher.Result;
import testMatcher.SameExecuteExtractPath;
import testMatcher.SameExecutePath;
import tracer.ValueOption;

public class ExportFileGenerator {

	ArrayList<NaturalTestClass> naturalTestClassLists;
	ArrayList<ExtractClass> extractClassLists;
	ArrayList<TestClass> testClassLists;
	ArrayList<Result> resultLists;
	
	public ExportFileGenerator(ArrayList<NaturalTestClass> n, ArrayList<ExtractClass> e, ArrayList<TestClass> t, ArrayList<Result> r) {
		naturalTestClassLists = n;
		extractClassLists = e;
		testClassLists = t;
		resultLists = r;
	}
	
	public void run() {
		String dirName = "src/test/java/mappingTest";
		File dir = new File(dirName);
		dir.mkdir();
		
		for(int classNum = 0; classNum < naturalTestClassLists.size(); classNum++) {
			NaturalTestClass naturalTestClass = naturalTestClassLists.get(classNum);
			ArrayList<String> contents = naturalTestClass.getContentLists();
			
			File file = new File(dirName + "/" + naturalTestClass.getClassName() + ".java");
			file.setExecutable(true);
			file.setReadable(true);
			file.setWritable(true);
			
			try {
				FileWriter fw = new FileWriter(file);
				for(int i = 0; i < contents.size(); i++) {
					fw.write(contents.get(i) + "\n");
				}
				
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void createEx01() {
		String path = "src/test/java/mappingTest/ex01.txt";
		
		File file = new File(path);
		file.setReadable(true);
		file.setWritable(true);
		
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			
			for(int extractNum = 0; extractNum < extractClassLists.size(); extractNum++) {
				ArrayList<Instance> instanceLists = extractClassLists.get(extractNum).getInstanceLists();
				for(int instanceNum = 0; instanceNum < instanceLists.size(); instanceNum++) {
					Instance instance = instanceLists.get(instanceNum);
					ArrayList<ExtractMethod> methodLists = instance.getExtractMethodLists();
					
					for(int methodNum = 0; methodNum < methodLists.size(); methodNum++) {
						ExtractMethod extractMethod = methodLists.get(methodNum);
						
						ArrayList<ValueOption> argumentLists = extractMethod.getArgmentLists();
						if(argumentLists.size() != 0) {
							String statement = instance.getId() + "\t" + instance.getOwnerClass() + "\t" + extractMethod.getMethodName();
							
							for(int argNum = 0; argNum < argumentLists.size(); argNum++) {
								statement += "\t" + argumentLists.get(argNum).getValue();
							}
							
							fw.write(statement + "\n");
						}
					}
				}
			}

			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createEx02(){
		String path = "src/test/java/mappingTest/ex02.txt";
		
		File file = new File(path);
		file.setReadable(true);
		file.setWritable(true);
		
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			
			for(int resultNum = 0; resultNum < resultLists.size(); resultNum++) {
				Result result = resultLists.get(resultNum);
				
				int evoSuiteNum = result.getEvoSuiteTestClass().getTestLists().size();
				int instanceNum = result.getExtractClass().getInstanceLists().size();
				
				int matchingEvoSuiteNum = 0;
				int matchingExtractNum = 0;
				ArrayList<MatchingResult> matchingResultLists = result.getMatchingResultLists();
				for(int matchNum = 0; matchNum < matchingResultLists.size(); matchNum++) {
					MatchingResult matchingResult = matchingResultLists.get(matchNum);
					matchingEvoSuiteNum += matchingResult.getSameExecuteEvoSuitePath().getTestLists().size();
					matchingExtractNum += matchingResult.getSameExecuteExtractPath().getInstanecLists().size();
				}
				
				int partiallyMatchingEvoSuiteNum = 0;
				int partiallyMatchingExtractNum = 0;
				ArrayList<PartiallyMatchingResult> partiallyMatchingResultLists = result.getPartiallyMatchingResultLists();
				for(int partMatchNum = 0; partMatchNum < partiallyMatchingResultLists.size(); partMatchNum++) {
					PartiallyMatchingResult partiallyMatchingResult = partiallyMatchingResultLists.get(partMatchNum);
					partiallyMatchingEvoSuiteNum += partiallyMatchingResult.getSameExecuteEvoSuitePath().getTestLists().size();
					ArrayList<SameExecuteExtractPath> sameExecuteExtractLists = partiallyMatchingResult.getPartiallyMatchingExtractPathLists();
					for(int sameExtractNum = 0; sameExtractNum < sameExecuteExtractLists.size(); sameExtractNum++) {
						partiallyMatchingExtractNum += sameExecuteExtractLists.get(sameExtractNum).getInstanecLists().size();
					}
				}
				
				int notMatchingEvoSuiteNum = 0;
				ArrayList<SameExecutePath> notMatchingEvoSuiteLists = result.getNotMatchingEvoSuiteLists();
				for(int sameEvoNum = 0; sameEvoNum < notMatchingEvoSuiteLists.size(); sameEvoNum++) {
					notMatchingEvoSuiteNum += notMatchingEvoSuiteLists.get(sameEvoNum).getTestLists().size();
				}
				
				int notMatchingInstanceNum = 0;
				ArrayList<SameExecuteExtractPath> notMatchingSameExecuteExtractPaths = result.getNotMatchingExtractLists();
				for(int sameInstNum = 0; sameInstNum < notMatchingSameExecuteExtractPaths.size(); sameInstNum++) {
					notMatchingInstanceNum += notMatchingSameExecuteExtractPaths.get(sameInstNum).getInstanecLists().size();
				}
				
				String statement = result.getEvoSuiteTestClass().getClassName().replace("_ESTest", "") + "\t\t" + 
						evoSuiteNum + "\t" + matchingEvoSuiteNum + "\t" + partiallyMatchingEvoSuiteNum + "\t" + notMatchingEvoSuiteNum + "\t" +
						instanceNum + "\t" + matchingExtractNum + "\t" + partiallyMatchingExtractNum + "\t" + notMatchingInstanceNum;
				fw.write(statement + "\n");
			}
			
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createEx03() {
		String path = "src/test/java/mappingTest/ex03.txt";
		
		File file = new File(path);
		file.setReadable(true);
		file.setWritable(true);
		
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			
			for(int resultNum = 0; resultNum < resultLists.size(); resultNum++) {
				Result result = resultLists.get(resultNum);
				
				String className = result.getExtractClass().getOwnerClass();
				
				ArrayList<MatchingResult> matchingResultLists = result.getMatchingResultLists();
				for(int matchNum = 0; matchNum < matchingResultLists.size(); matchNum++) {
					MatchingResult matchingResult = matchingResultLists.get(matchNum);
					ArrayList<Test> evoSuiteTestLists = matchingResult.getSameExecuteEvoSuitePath().getTestLists();
					
					for(int testNum = 0; testNum < evoSuiteTestLists.size(); testNum++) {
						Test test = evoSuiteTestLists.get(testNum);
						ArrayList<TestMethod> methodLists = test.getMethodLists();
						
						for(int methodNum = 0; methodNum < methodLists.size(); methodNum++) {
							TestMethod method = methodLists.get(methodNum);
							String statement = "EvoSuite" + "\t" + test.getMethodName() + "\t" + className + "\t" + method.getMethodName();
							ArrayList<String> argumentLists = method.getArgumentLists();
							
							for(int argNum = 0; argNum < argumentLists.size(); argNum++) {
								statement += "\t" + argumentLists.get(argNum);
							}
							
							fw.write(statement + "\n");
						}
					}
					
					ArrayList<Instance> instanceLists = matchingResult.getSameExecuteExtractPath().getInstanecLists();
					for(int instanceNum = 0; instanceNum < instanceLists.size(); instanceNum++) {
						Instance instance = instanceLists.get(instanceNum);
						
						fw.write("Extract" + "\t" + instance.getId() + "\t" + className + "\n");
					}
				}
				
				ArrayList<PartiallyMatchingResult> partiallyMatchingLists = result.getPartiallyMatchingResultLists();
				for(int partNum = 0; partNum < partiallyMatchingLists.size(); partNum++) {
					PartiallyMatchingResult partiallyMatchingResult = partiallyMatchingLists.get(partNum);
					ArrayList<Test> testLists = partiallyMatchingResult.getSameExecuteEvoSuitePath().getTestLists();
					
					for(int testNum = 0; testNum < testLists.size(); testNum++) {
						Test test = testLists.get(testNum);
						ArrayList<TestMethod> methodLists = test.getMethodLists();
						
						for(int methodNum = 0; methodNum < methodLists.size(); methodNum++) {
							TestMethod method = methodLists.get(methodNum);
							String statement = "EvoSuite" + "\t" + test.getMethodName() + "\t" + className + "\t" + method.getMethodName();
							ArrayList<String> argumentLists = method.getArgumentLists();
							
							for(int argNum = 0; argNum < argumentLists.size(); argNum++) {
								statement += "\t" + argumentLists.get(argNum);
							}
							
							fw.write(statement + "\n");
						}
					}
					
					ArrayList<SameExecuteExtractPath> sameExecuteExtractPathLists = partiallyMatchingResult.getPartiallyMatchingExtractPathLists();
					
					for(int executeNum = 0; executeNum < sameExecuteExtractPathLists.size(); executeNum++) {
						ArrayList<Instance> instanceLists = sameExecuteExtractPathLists.get(executeNum).getInstanecLists();
						for(int instanceNum = 0; instanceNum < instanceLists.size(); instanceNum++) {
							Instance instance = instanceLists.get(instanceNum);
							
							fw.write("Extract" + "\t" + instance.getId() + "\t" + className + "\n");
						}
					}
				}
				
			}
			
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
