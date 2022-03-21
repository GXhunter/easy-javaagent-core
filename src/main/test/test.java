import com.gxhunter.agent.core.utils.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

public class test {
    public static void main(String[] args) throws IOException {
        String uriString = new File("D:\\code\\agent-debug-plugin\\target\\local-debug-plugin-jar-with-dependencies.jar").toURI().toString();
        uriString = URLEncoder.encode(uriString, "UTF-8");
        System.out.println(uriString);

//        InputStream inputStream = IOUtils.download();
        InputStream inputStream = IOUtils.download("http://test.e.4399.cn/adp-server/agent_plugin/download?code=core.jar");
        IOUtils.copy(inputStream,new FileOutputStream("C:\\Users\\hunter\\Desktop\\x.zip"));
        System.out.println(IOUtils.readStream2String(inputStream));
    }

    @Test
    public void test() {
    }
}
