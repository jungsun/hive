/**
 * @author Ahn Hyeok Jun
 */

package models;

import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.Page;

import play.data.format.*;
import play.data.validation.*;
import play.db.ebean.*;

@Entity
public class Article extends Model {
    private static final long serialVersionUID = 1L;

    public Article() {
        this.date = new Date();
        this.replyNum = 0;
    }

    @Id
    public Long articleNum;

    @Constraints.Required
    public String title;

    @Constraints.Required
    public String contents;
    // required
    public Long writerId;

    @Constraints.Required
    @Formats.DateTime(pattern = "YYYY/MM/DD/hh/mm/ss")
    public Date date;

    public int replyNum;

    public String filePath;

    private static Finder<Long, Article> find = new Finder<Long, Article>(Long.class, Article.class);

    public static Article findById(Long articleNum) {
        return find.byId(articleNum);
    }

    public static Page<Article> findOnePage(int pageNum) {
        return find.findPagingList(25).getPage(pageNum - 1);
    }

    public static Long write(Article article) {
        article.save();
        return article.articleNum;
    }

    public static void delete(Long articleNum) {
        find.byId(articleNum).delete();
        Reply.deleteByArticleNum(articleNum);
    }

    public static void replyAdd(Long articleNum) {
        Article article = findById(articleNum);
        article.replyNum++;
        article.update();
    }

    public String calcPassTime() {
        // TODO 경계값 검사하면 망할함수. 나중에 라이브러리 쓸예정
        Calendar today = Calendar.getInstance();

        long dTimeMili = today.getTime().getTime() - this.date.getTime();

        Calendar dTime = Calendar.getInstance();
        dTime.setTimeInMillis(dTimeMili);

        if (dTimeMili < 60 * 1000) {
            return "방금 전";
        } else if (dTimeMili < 60 * 1000 * 60) {
            return dTime.get(Calendar.MINUTE) + "분 전";
        } else if (dTimeMili < 60 * 1000 * 60 * 24) {
            return dTime.get(Calendar.HOUR) + "시간 전";
        } else if (dTimeMili < 60 * 1000 * 60 * 24 * 30) {
            return dTime.get(Calendar.DATE) + "일 전";
        } else if (dTimeMili < 60 * 1000 * 60 * 24 * 30 * 12) {
            return dTime.get(Calendar.MONDAY) + "달 전";
        } else {
            return dTime.get(Calendar.YEAR) + "년 전";
        }
    }

    public String writer() {
        return User.findById(writerId).name;
    }
}