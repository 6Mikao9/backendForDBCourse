package com.backend.dao;

import com.mongodb.MongoClient;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import org.bson.*;
import org.bson.conversions.*;
//import org.junit.*;

import java.util.*;

public class MongoUtils {
    /**
     * 获取连接，无需密码
     */
    MongoClient mongoClient ;
    // 连接到数据库
    MongoDatabase mongoDatabase;

    public MongoCollection<Document> collectionDA;

    public MongoCollection<Document> collectionBlockTrans;

    public MongoCollection<Document> collectionBlockTransaction;

    public MongoCollection<Document> collectionContractInfo;

    public MongoCollection<Document> collectionERC20Transaction;

    public MongoCollection<Document> collectionERC721Transaction;

    public MongoCollection<Document> collectionInternalTransaction;

    private static final String dataBase = "BlockData";
    public MongoUtils(String ip){//ip作为参数构造该工具类，应该只需要用到query方法
        try {
            ServerAddress serverAddress = new ServerAddress(ip, 27017);
            List<ServerAddress> addrs = new ArrayList<>();
            addrs.add(serverAddress);
            //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
            MongoCredential credential = MongoCredential.createScramSha1Credential("FengRuPro",
                    "BlockData", "429".toCharArray());
            List<MongoCredential> credentials = new ArrayList<>();
            credentials.add(credential);
            //通过连接认证获取MongoDB连接
            mongoClient = new MongoClient(addrs, credentials);
            mongoDatabase = mongoClient.getDatabase(dataBase);
            collectionDA = mongoDatabase.getCollection("D_A");
            collectionBlockTrans=mongoDatabase.getCollection("BlockTrans");
            
            System.out.println("数据库工具初始化完成！Ciallo～(∠・ω< )⌒★");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public  <T>  FindIterable<Document> commonQuery(String collectionName, String key, T value) {//查询格式为字符串如"date",
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
            Bson filter = Filters.eq(key, value);
//            System.out.println("正在查询集合"+collectionName+"（ᗜ ‸ ᗜ）");
            return collection.find(filter);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }

    }

    /**
     * 查询日交易量数据
     */
    public  String queryAmount(String date) {//查询格式为字符串如"20200315"
        ArrayList<String> strings=new ArrayList<>();
        try {
            //System.out.println("Connect to database successfully");
            //System.out.println("集合 test 选择成功，collection：" + collection);
            // 创建查询条件：查找键为""且值为""的文档
            Bson filter = Filters.eq("date", date);
// 执行查询
            FindIterable<Document> results = collectionDA.find(filter);
// 遍历结果

            for (Document doc : results) {
//                System.out.println(doc.get("amount"));
                strings.add(doc.toJson());
            }
            if(strings.isEmpty())
                return null;
//            System.out.println("查询成功！／人◕ ‿‿ ◕人＼");
            return strings.get(0).split("\"")[13];
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }

    }
    public  ArrayList<String> QueryBlockTrans(String key, int value) {//查询格式为字符串如"date",
        ArrayList<String> strings=new ArrayList<>();
        try {

            Bson filter = Filters.eq(key, value);
// 执行查询
            FindIterable<Document> results = collectionBlockTrans.find(filter);
// 遍历结果
            for (Document doc : results) {
                strings.add(doc.toJson());
            }
            if(strings.isEmpty())
                return null;
            return strings;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }

    }

    public  ArrayList<String> QueryBlockTrans(String key, String value) {//查询格式为字符串如"date",
        ArrayList<String> strings=new ArrayList<>();
        try {

            Bson filter = Filters.eq(key, value);
// 执行查询
            FindIterable<Document> results = collectionBlockTrans.find(filter);
// 遍历结果
            for (Document doc : results) {
                strings.add(doc.toJson());
            }
            if(strings.isEmpty())
                return null;
            return strings;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }

    }

    /**
     * 插入文档
     */

    public void insertOne(ArrayList<String> key,ArrayList<String> value){
        Document document = new Document();
        for (int i = 0; i < key.size(); i++) {
            document.append(key.get(i),value.get(i) );
        }
        collectionDA.insertOne(document);
        System.out.println("this is"+(key.size()==value.size()));
    }

    public void insertOne(String Date,String amount) {
        try {

            //插入文档
            //1. 创建文档 org.bson.Document 参数为key-value的格式
            //2. 创建文档集合List<Document>
            //3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用 mongoCollection.insertOne(Document)
            Document document = new Document("date",Date ).append("amount", amount);
            //List<Document> documents = new ArrayList<>();
            //documents.add(document);
            collectionDA.insertOne(document);
            System.out.println("文档插入成功");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 检索所有文档
     */

    public void find() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("BlockData");
            System.out.println("Connect to database successfully");
            MongoCollection<Document> collection = mongoDatabase.getCollection("D_A");
            System.out.println("集合 D_A 选择成功");

            //检索所有文档
            //1.获取迭代器FindIterable<Document>
            //2.获取游标MongoCursor<Document>
            //3.通过游标遍历检索出的文档集合
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            System.out.println("获取游标成功，mongoCursor：" + mongoCursor);
            while (mongoCursor.hasNext()) {
                System.out.println(mongoCursor.next());
            }
            System.out.println("检索所有文档完成");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 更新文档
     */

    public void updateMany() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("mongo");
            System.out.println("Connect to database successfully");
            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            System.out.println("集合 test 选择成功");

            //更新文档   将文档中likes=100的文档修改为likes=200
            collection.updateMany(Filters.eq("likes", 100), new Document("$set", new Document("likes", 200)));
            //检索查看结果
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            System.out.println("获取游标成功，mongoCursor：" + mongoCursor);
            while (mongoCursor.hasNext()) {
                System.out.println(mongoCursor.next());
            }
            System.out.println("更新文档完成");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 删除文档
     */

    public void findOneRemove() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("mongo");
            System.out.println("Connect to database successfully");
            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            System.out.println("集合 test 选择成功");

            //删除符合条件的第一个文档
            collection.deleteOne(Filters.eq("likes", 200));
            //删除所有符合条件的文档
            collection.deleteMany(Filters.eq("likes", 200));
            //检索查看结果
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            System.out.println("获取游标成功，mongoCursor：" + mongoCursor);
            while (mongoCursor.hasNext()) {
                System.out.println(mongoCursor.next());
            }
            System.out.println("删除文档完成");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void getMongoConnectionByAddress() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("BlockData");
            System.out.println("Connect to database successfully");

            MongoCollection<Document>  test = mongoDatabase.getCollection("D_A");
            FindIterable<Document> documents = test.find();
            System.out.println("D_A documents : " + documents);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 获取连接，需密码
     */

    public void getMongoConnectionByUser() {
        try {
            //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
            // ServerAddress()两个参数分别为 服务器地址 和 端口
            ServerAddress serverAddress = new ServerAddress("localhost", 27017);
            List<ServerAddress> addrs = new ArrayList<>();
            addrs.add(serverAddress);
            //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
            MongoCredential credential = MongoCredential.createScramSha1Credential("username", "databaseName", "password".toCharArray());
            List<MongoCredential> credentials = new ArrayList<>();
            credentials.add(credential);
            //通过连接认证获取MongoDB连接
            MongoClient mongoClient = new MongoClient(addrs, credentials);
            //连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("databaseName");
            System.out.println("Connect to database successfully，mongoDatabase：" + mongoDatabase);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 创建集合
     */
    public void createCollection(String collectionName) {
        try {
            mongoDatabase.createCollection(collectionName);
            System.out.println(collectionName+"集合创建成功~／人◕ ‿‿ ◕人＼");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public MongoUtils(){//也可以改这个
        try {
            mongoClient = new MongoClient("localhost", 27017);
            mongoDatabase = mongoClient.getDatabase(dataBase);
            collectionDA = mongoDatabase.getCollection("D_A");
            collectionBlockTrans=mongoDatabase.getCollection("BlockTrans");
            collectionBlockTransaction=mongoDatabase.getCollection("BlockTransaction");
            collectionContractInfo=mongoDatabase.getCollection("BlockContractInfo");
            collectionERC20Transaction=mongoDatabase.getCollection("ERC20Transaction");
            collectionERC721Transaction=mongoDatabase.getCollection("ERC721Transaction");
            collectionInternalTransaction=mongoDatabase.getCollection("InternalTransaction");
            System.out.println("数据库工具初始化完成！Ciallo～(∠・ω< )⌒★");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
