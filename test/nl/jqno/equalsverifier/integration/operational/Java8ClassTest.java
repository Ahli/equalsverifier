/*
 * Copyright 2014 Jan Ouwens
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.jqno.equalsverifier.integration.operational;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class Java8ClassTest {
	private static final String JAVA_8_CLASS =
			"\nimport java.util.List;" +
			"\nimport java.util.Objects;" +
			"\n" +
			"\npublic final class Java8Class {" +
			"\n    private final List<Object> objects;" +
			"\n    " +
			"\n    public Java8Class(List<Object> objects) {" +
			"\n        this.objects = objects;" +
			"\n    }" +
			"\n    " +
			"\n    public void doSomethingWithStreams() {" +
			"\n        objects.stream().forEach(System.out::println);" +
			"\n    }" +
			"\n    " +
			"\n    @Override" +
			"\n    public boolean equals(Object obj) {" +
			"\n        if (!(obj instanceof Java8Class)) {" +
			"\n            return false;" +
			"\n        }" +
			"\n        return objects == ((Java8Class)obj).objects;" +
			"\n    }" +
			"\n    " +
			"\n    @Override" +
			"\n    public int hashCode() {" +
			"\n        return Objects.hash(objects);" +
			"\n    }" +
			"\n}";
			
	private File tempFileLocation;
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Before
	public void setUp() throws IOException {
		tempFileLocation = tempFolder.newFolder();
	}
	
	@Test
	public void successfullyInstantiatesAJava8ClassWithStreams_whenJava8IsAvailable() throws IOException, ClassNotFoundException {
		if (!java8IsAvailable()) {
			return;
		}
		
		File sourceFile = writeJava8ClassToFile();
		compileJava8Class(sourceFile);
		verifyEqualsVerifierCanHandleJava8Class();
	}
	
	private boolean java8IsAvailable() {
		try {
			Class.forName("java.util.Optional");
			return true;
		}
		catch (ClassNotFoundException e) {
			return false;
		}
	}

	private File writeJava8ClassToFile() throws IOException {
		File sourceFile = new File(tempFileLocation, "Java8Class.java");
		FileWriter writer = new FileWriter(sourceFile);
		writer.write(JAVA_8_CLASS);
		writer.close();
		sourceFile.deleteOnExit();
		return sourceFile;
	}
	
	private void compileJava8Class(File sourceFile) throws IOException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = null;
		try {
			fileManager = compiler.getStandardFileManager(null, null, null);
			fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(tempFileLocation));
			Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));
			CompilationTask task = compiler.getTask(null, fileManager, null, null, null, javaFileObjects);
			
			boolean success = task.call();
			if (!success) {
				throw new AssertionError("Could not compile Java 8 class");
			}
		}
		finally {
			if (fileManager != null) {
				fileManager.close();
			}
		}
	}
	
	private void verifyEqualsVerifierCanHandleJava8Class() throws IOException, ClassNotFoundException {
		URLClassLoader cl = null;
		try {
			cl = createClassLoader();
			Class<?> type = cl.loadClass("Java8Class");
			
			EqualsVerifier.forClass(type)
					.verify();
		}
		finally {
			if (cl != null) {
				cl.close();
			}
		}
	}

	private URLClassLoader createClassLoader() throws MalformedURLException {
		URL[] urls = { tempFileLocation.toURI().toURL() };
		return new URLClassLoader(urls);
	}
}
