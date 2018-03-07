package test;


/*import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Stream;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import lib.org.ardverk.collection.PatriciaTrie;

import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class AllTests {
	
	
	private void TestTrieOrHashSet(Stream<String> datas) throws Exception{
		HashSet<String> hashSet = new HashSet<>();
		PatriciaTrie<String, Boolean> patriciaTrie = new PatriciaTrie<>();
		 
		long tStart = System.currentTimeMillis();
		 datas.map(word -> word.split(";")[1])
         	.map(word -> word.replace(" ","_"))
         	.forEach(word -> {
         		patriciaTrie.put(word,true);
         	});
		 
		 System.out.println("PatricianTrie build time: "+(System.currentTimeMillis()-tStart +"ms"));
		 datas.map(word -> word.split(";")[1])
      		.map(word -> word.replace(" ","_"))
      		.forEach(word -> {
      			hashSet.add(word);
      		});
		 
		 System.out.println("Hashet  build time: "+(System.currentTimeMillis()-tStart +"ms"));
		//fail();
	}
	
	private Stream<String> readFile(String filePath) throws IOException {
		return Files.readAllLines(Paths.get(filePath)).stream();
	}
	
    public void test() throws IOException, Exception {
    	//TestTrieOrHashSet(readFile("datas/jdm-mc.ser"));
    }
}*/

import org.junit.Test;
import static org.junit.Assert.assertEquals;
public class AllTests {
   @Test
   public void testSetup() {
      String str= "I am done with Junit setup";
      assertEquals("I am done with Junit setup",str);
   }
}
