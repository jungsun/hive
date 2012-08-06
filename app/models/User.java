package models;

import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import models.enumeration.Direction;
import models.enumeration.Matching;
import models.support.FinderTemplate;
import models.support.OrderParams;
import models.support.SearchParams;

import com.avaje.ebean.Page;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class User extends Model {
    private static final long serialVersionUID = 1L;
    private static Finder<Long, User> find = new Finder<Long, User>(Long.class,
            User.class);
    
    public static final int USER_COUNT_PER_PAGE = 30;
    
    @Id
    public Long id;
    public String name;
    public String loginId;
    public String password;
    public String profileFilePath;
    public String email;
    public boolean isActive;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    public Set<ProjectUser> projectUser;

    public String getName() {
        return this.name;
    }

    public static User findByName(String name) {
        return find.where().eq("name", name).findUnique();
    }

    public static User findById(Long id) {
        return find.byId(id);
    }

    public static User findByLoginId(String loginId) {
        return find.where().eq("loginId", loginId).findUnique();
    }

    public static User authenticate(User user) {
        return find.where().eq("loginId", user.loginId)
                .eq("password", user.password).findUnique();
    }

    public static String findNameById(long id) {
        return find.byId(id).name;
    }

    public static String findLoginIdById(long id) {
        return find.byId(id).loginId;
    }

    public static Map<String, String> options() {
        LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();
        for (User user : User.find.orderBy("name").findList()) {
            options.put(user.id.toString(), user.name);
        }
        return options;
    }
    
    public static Page<User> findUsers(int pageNum, String loginId) {
        OrderParams orderParams = new OrderParams().add("loginId", Direction.ASC);
        SearchParams searchParams = new SearchParams();
        if(loginId != null) searchParams.add("loginId", loginId, Matching.CONTAINS);
        return FinderTemplate.getPage(orderParams, searchParams, find, USER_COUNT_PER_PAGE, pageNum);
    }   
}
