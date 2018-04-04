package com.purcell;


import com.purcell.config.AppConfiguration;
import com.purcell.model.User;
import com.purcell.repository.ICacheRepository;
import com.purcell.repository.IHashRepository;
import com.purcell.repository.IRedisListDemo;
import com.purcell.repository.IRedisSetDemo;
import com.purcell.repository.RedisListTypeRepository;
import com.purcell.repository.RedisSetTypeRepository;
import com.purcell.repository.SimpleCacheRepository;
import com.purcell.repository.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class App {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);

        ICacheRepository<String,String> simpleCacheRepository =
                (SimpleCacheRepository)context.getBean("simpleCacheRepository");

        // delete the key if exists
        simpleCacheRepository.delete("best.real.news");
        // tet simple put first
        simpleCacheRepository.put("best.real.news", "Breitbart");
        System.out.println("get news :: " + simpleCacheRepository.get("best.real.news"));

        // Now to a multi put in the redis data store
        Map<String,String> links = new HashMap<String, String>();
        links.put("breitbart", "www.breitbart.com");
        links.put("gatewaypundit", "www.thegatewaypundit.com");
        links.put("frontpagemag", "www.frontpagemag.com");
        links.put("drudge", "www.drudgereport.com");
        simpleCacheRepository.multiPut(links);
        List<String> linkslist = simpleCacheRepository.multiGet(Arrays.asList("breitbart", "gatewaypundit", "frontpagemag", "drudge"));

        System.out.println("MultiGet demo :: " + linkslist);

       // list functions
        IRedisListDemo<String,String> redisListDemo =
                (RedisListTypeRepository)context.getBean("redisListTypeRepository");

        //Delete the key of exists
        redisListDemo.delete("list:demo");

        //Push the element to the right
        redisListDemo.push("list:demo", "10",true);
        redisListDemo.push("list:demo", "40",true);
        //Push the element to the left
        redisListDemo.push("list:demo", "30",false);
        redisListDemo.push("list:demo", "20",false);
        //Try multi add
        Collection<String> valuesToTestMultiPush = Arrays.asList(new String[]{"40","50","60"});
        //Add same collection to left and right push
        redisListDemo.multiAdd("list:demo",valuesToTestMultiPush,true);


        //Fetch the entire list for the list:demo
        Collection<String> values = redisListDemo.get("list:demo");

        System.out.println("list:demo :: " + values);

        //Try right pop
        String value = redisListDemo.pop("list:demo",true);
        System.out.println("Right Pop Value :: " + value);

        //Try left pop
        value = redisListDemo.pop("list:demo",false);
        System.out.println("Left Pop Value :: " + value);


        //Now trim the list
        redisListDemo.trim("list:demo", 0,2);
        //fetch the values
        values = redisListDemo.get("list:demo");
        System.out.println("After trimming list:demo :: " + values);

        //Get the size of the list
        Long size = redisListDemo.size("list:demo");
        System.out.println("size of list:demo :: " + size);


        // set
        IRedisSetDemo<String,String> redisSetDemo =
                (RedisSetTypeRepository)context.getBean("redisSetTypeRepository");

        redisSetDemo.delete("set:demo");
        Set<String> names = new HashSet<String>();

        redisSetDemo.add("set:demo", "John");
        redisSetDemo.add("set:demo", "Rusty");
        redisSetDemo.add("set:demo", "Scott");
        names = redisSetDemo.members("set:demo");
        System.out.println("set:demo :: " +  names);


        // hash pojo
        IHashRepository<User> userRepository =
                (UserRepository)context.getBean("userRepository");

        userRepository.delete();
        //Try simple put of user in data store
        User user1 = new User(1000L,"user1","*****",25,"scott" );
        User user2 = new User(1001L,"user2","*****",25,"scott" );

        //Add user1 and user 2 to redis data store
        userRepository.put(user1);
        userRepository.put(user2);

        //Fetch current users
        List<User> users = userRepository.getObjects();
        System.out.println("Users : " + users);

        //Fetch user1
        User user1FromDb = userRepository.get(user1);
        System.out.println("User1 : " + user1FromDb);


        //delete a user
        userRepository.delete(user2);
        users = userRepository.getObjects();
        System.out.println("Users After deleting user2 : " + users);

        //Try multi put and multi get

        users = new ArrayList<User>();
        for(int i = 0; i < 20; ++i) {
            User user = new User(1100L + i, "user" + i, "*****",25+i, "Scott");
            users.add(user);

        }
        userRepository.multiPut(users);
        List<User> fetchedUsers = userRepository.multiGet(users);
        System.out.println("Multi Get Users : " + fetchedUsers);



    }
}
