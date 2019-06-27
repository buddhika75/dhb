package entity;

import entity.Category;
import entity.Participant;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-27T07:19:35")
@StaticMetamodel(Institution.class)
public class Institution_ { 

    public static volatile SingularAttribute<Institution, String> backgoundColour;
    public static volatile SingularAttribute<Institution, String> foregroundColour;
    public static volatile SingularAttribute<Institution, String> name;
    public static volatile SingularAttribute<Institution, Long> id;
    public static volatile SingularAttribute<Institution, Category> category;
    public static volatile ListAttribute<Institution, Participant> participants;

}