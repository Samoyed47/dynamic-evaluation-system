package com.itheima.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.itheima.mapper.dataMapper;
import com.itheima.model.Post;
import com.itheima.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class dataServiceImp implements dataService{
    @Autowired
    private dataMapper dataMapper;

    // 根据用户id获取用户的热度得分
    public User getUserById(Integer id) throws Exception{
        return dataMapper.getUserById(id);
    }
    // 根据帖子id获得帖子的热度得分
    public Post getPostById(Integer id) throws Exception{
        return dataMapper.getPostById(id);
    }
    // 获得某个领域排序靠前的n个帖子
    public List<Post> getPostsBySort(Integer n, Integer type) throws Exception{
        return dataMapper.getPostsBySort(n,type);
    }
    // 获得热度排序靠前的n个用户
    public List<User> getUsersBySort(Integer n) throws Exception{
        return dataMapper.getUsersBySort(n);
    }
    // 根据论坛新发送的数据修改数据库内容
    public void update(List<PostData> posts) throws Exception{
        BufferedWriter writer = null;
        String srcPath = getSrcPath();
        String path = srcPath+"\\bin\\data.json";
        File file = new File(path);
        if(!file.exists()){
            try {
                file.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        String jsonString = JSON.toJSONString(posts);
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false), "UTF-8"));
            writer.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("文件生成成功！");
        // 运行exe文件
        try {
            // 获取 exe 文件所在的目录
            String exePath = srcPath+"\\bin\\getScore.exe";
            File directory = new File(exePath).getParentFile();

            // 构造 ProcessBuilder 对象
            ProcessBuilder processBuilder = new ProcessBuilder(exePath);

            // 设置运行目录
            processBuilder.directory(directory);

            // 启动外部可执行文件
            Process process = processBuilder.start();

            // 等待子进程执行结束
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("外部程序执行成功！");
            } else {
                System.out.println("外部程序执行失败！");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // 读取算法运行后的json文件
        String postString = readJsonData(srcPath+"\\bin\\postScore.json");
        String userString = readJsonData(srcPath+"\\bin\\userScore.json");
        List<Post> postScore = new ArrayList<Post>();
        postScore = JSONObject.parseArray(postString, Post.class);
        for(Post post : postScore){
            dataMapper.updatePost(post.getId(),post.getScore(),post.getType());
        }
        List<User> userScore = new ArrayList<User>();
        userScore = JSONObject.parseArray(userString, User.class);
        for(User user : userScore) {
            dataMapper.updateUser(user.getId(), user.getScore());
        }
    }

    public static String readJsonData(String pactFile) throws IOException {
        // 读取文件数据
        //System.out.println("读取文件数据util");

        StringBuffer strbuffer = new StringBuffer();
        File myFile = new File(pactFile);
        if (!myFile.exists()) {
            System.err.println("Can't Find " + pactFile);
        }
        try {
            FileInputStream fis = new FileInputStream(pactFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
            BufferedReader in  = new BufferedReader(inputStreamReader);

            String str;
            while ((str = in.readLine()) != null) {
                strbuffer.append(str);  //new String(str,"UTF-8")
            }
            in.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        //System.out.println("读取文件结束util");
        return strbuffer.toString();
    }

    public static String getSrcPath() throws Exception{
        ClassLoader classLoader = dataService.class.getClassLoader();
        URL resourceURL = classLoader.getResource("");
        String resourcePath = null;
        try {
            resourcePath = URLDecoder.decode(resourceURL.getFile(), StandardCharsets.UTF_8.name());
            resourcePath = resourcePath.substring(1,resourcePath.length());
            Path windowsPath = Paths.get(resourcePath);
            String windowsPathWithChinese = windowsPath.toString();
            return windowsPathWithChinese;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
