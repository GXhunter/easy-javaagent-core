import com.gxhunter.agent.core.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;

public class test {
    public static void main(String[] args) throws IOException {
        String uriString = "http://test,,,,.e.4399.cn/adp-server/agent_plugin/download?code=core.jar";
        uriString = URLEncoder.encode(uriString, "UTF-8");
        System.out.println(uriString);

//        InputStream inputStream = IOUtils.download();
        InputStream inputStream = IOUtils.download("http://test.e.4399.cn/adp-server/agent_plugin/download?code=core.jar");
        IOUtils.copy(inputStream,new FileOutputStream("C:\\Users\\hunter\\Desktop\\x.zip"));
        System.out.println(IOUtils.readStream2String(inputStream));
    }
}
