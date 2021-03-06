package models;

import models.enumeration.ResourceType;
import models.resource.Resource;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"category", "name"}))
public class Tag extends Model {

    /**
     *
     */
    private static final long serialVersionUID = -35487506476718498L;
    public static Finder<Long, Tag> find = new Finder<Long, Tag>(Long.class, Tag.class);

    @Id
    public Long id;

    @Required
    public String category;

    @Required
    public String name;

    @ManyToMany(mappedBy="tags")
    public Set<Project> projects;

    public Tag(String category, String name) {
        if (category == null) {
            category = "Tag";
        }
        this.category = category;
        this.name = name;
    }

    @Transient
    public boolean exists() {
        return find.where().eq("category", category).eq("name", name)
            .findRowCount() > 0;
    }

    @Override
    public void delete() {
        for(Project project: projects) {
            project.tags.remove(this);
            project.save();
        }
        super.delete();
    }

    @Override
    public String toString() {
        return category + " - " + name;
    }

    public Resource asResource() {
        return new Resource() {
            @Override
            public Long getId() {
                return id;
            }

            @Override
            public Project getProject() {
                return null;
            }

            @Override
            public ResourceType getType() {
                return ResourceType.TAG;
            }
        };
    }
}
