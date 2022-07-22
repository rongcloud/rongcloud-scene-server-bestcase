package cn.rongcloud.mic.room.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.net.URLConnection;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Override
    public int getFileSize(String fileUrl) throws Exception{
        int size;
        URL url = new URL(fileUrl);
        URLConnection conn = url.openConnection();
        size = conn.getContentLength();
        if (size < 0)
            System.out.println("无法获取文件大小。");
        else
            System.out.println("文件大小为：" + size + " bytes");
        conn.getInputStream().close();
        return size;
    }
}
